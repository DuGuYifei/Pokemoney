import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:flutter_svg/svg.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart'; // Make sure to import your provider
import 'package:pokemoney/model/barrel.dart' as pokemoney; // Make sure to import your Transaction model
import 'package:pokemoney/providers/CategoryProvider.dart';
import 'package:pokemoney/providers/FundProvider.dart';

class EditTransactionPage extends StatefulWidget {
  final pokemoney.Transaction transaction;

  const EditTransactionPage({Key? key, required this.transaction}) : super(key: key);

  @override
  _EditTransactionPageState createState() => _EditTransactionPageState();
}

class _EditTransactionPageState extends State<EditTransactionPage> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _invoiceNumberController;
  late TextEditingController _amountController;
  late TextEditingController _dateController;
  final TextEditingController _customTypeController = TextEditingController();

  late String _selectedType;
  late int _selectedCategoryId;
  late int _selectedFundId;
  late DateTime selectedDate;

  List<DropdownMenuItem<int>> _categoryItems = []; // Store category items here
  List<DropdownMenuItem<int>> _fundItems = [];

  @override
  void initState() {
    super.initState();
    // Initialize text editing controllers with the current transaction's values
    _invoiceNumberController = TextEditingController(text: widget.transaction.invoiceNumber);
    _amountController = TextEditingController(text: widget.transaction.amount.toString());
    _dateController =
        TextEditingController(text: DateFormat('yyyy-MM-dd').format(widget.transaction.billingDate));
    _selectedType = widget.transaction.type;
    _selectedFundId = widget.transaction.fundId;
    _selectedCategoryId = widget.transaction.categoryId; // default or initial value

    fetchAndUpdateCategories();
    fetchAndUpdateFunds();
  }

  void fetchAndUpdateCategories() {
    final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
    if (categoryProvider.categories.isEmpty) {
      categoryProvider.fetchAllCategory().then((_) {
        updateCategoryItems(categoryProvider.categories);
      });
    } else {
      updateCategoryItems(categoryProvider.categories);
    }
  }

  void fetchAndUpdateFunds() {
    final fundProvider = Provider.of<FundProvider>(context, listen: false);
    if (fundProvider.funds.isEmpty) {
      fundProvider.fetchAllFunds().then((_) {
        _updateFundItems(fundProvider.funds);
      });
    } else {
      _updateFundItems(fundProvider.funds);
    }
  }

  void updateCategoryItems(List<pokemoney.Category> categories) {
    if (categories.isNotEmpty) {
      setState(() {
        //_selectedCategoryId = categories.first.id!;
        _categoryItems = categories
            .map((category) => DropdownMenuItem<int>(
                  value: category.id,
                  child: Row(
                    children: <Widget>[
                      SvgPicture.asset(
                        category.iconPath, // The path to the SVG asset
                        width: 45,
                        height: 45,
                      ),
                      SizedBox(width: 10),
                      Text(category.name),
                    ],
                  ),
                ))
            .toList();
      });
    }
  }

  void _updateTransaction() {
    // Create an updated transaction instance
    pokemoney.Transaction updatedTransaction = widget.transaction.copyWith(
      invoiceNumber: _invoiceNumberController.text,
      amount: double.parse(_amountController.text),
      type: _selectedType,
      billingDate: DateTime.parse(_dateController.text),
      categoryId: _selectedCategoryId,
      fundId: _selectedFundId,
    );

    // Use the transaction provider to update the transaction
    Provider.of<TransactionProvider>(context, listen: false).updateTransaction(updatedTransaction).then((_) {
      Navigator.of(context).pop(); // Dismiss the page when the update is complete
    }).catchError((error) {
      // Handle any errors here
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating transaction: $error')),
      );
    });
  }

// Updates category dropdown items based on available categories
  void _updateFundItems(List<pokemoney.Fund> funds) {
    if (funds.isNotEmpty) {
      setState(() {
        _selectedFundId = funds.first.id!;
        _fundItems = funds
            .map((fund) => DropdownMenuItem<int>(
                  value: fund.id,
                  child: Row(
                    children: <Widget>[
                      Text('\$ ${fund.balance.toString()}'),
                      const SizedBox(width: 20),
                      Text(fund.name),
                    ],
                  ),
                ))
            .toList();
      });
    }
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: widget.transaction.billingDate,
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != widget.transaction.billingDate) {
      setState(() {
        // Update the text field with the new date
        _dateController.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Edit Transaction'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Consumer<TransactionProvider>(
          builder: (context, provider, child) {
            return Form(
              key: _formKey,
              child: GestureDetector(
                onTap: () => FocusScope.of(context).unfocus(),
                child: SingleChildScrollView(
                  child: ConstrainedBox(
                    constraints: BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.9),
                    child: Column(
                      children: <Widget>[
                        _buildTextField(
                            _invoiceNumberController, 'Invoice Number', 'Please enter the invoice number'),
                        _buildDropdownField<int>(_selectedCategoryId, 'Category', _categoryItems,
                            (value) => setState(() => _selectedCategoryId = value!)),
                        _buildDateField(context),
                        _buildTextField(_amountController, 'Amount', 'Please enter the amount',
                            keyboardType: TextInputType.number),
                        _buildDropdownField<String>(
                            _selectedType,
                            'Type',
                            ['Expense', 'Income', 'Other'].map<DropdownMenuItem<String>>((String value) {
                              return DropdownMenuItem<String>(
                                value: value,
                                child: Text(value),
                              );
                            }).toList(),
                            (value) => setState(() => _selectedType = value!)),
                        if (_selectedType == 'Other')
                          _buildTextField(_customTypeController, 'Custom Type', 'Please enter a type'),
                        _buildDropdownField<int>(_selectedFundId, 'Fund', _fundItems,
                            (value) => setState(() => _selectedFundId = value!)),
                        ElevatedButton(
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              // Only save if the form is valid (all validations passed)
                              _updateTransaction();
                            }
                          },
                          child: const Text('Save'),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _buildDropdownField<T>(
      T currentValue, String label, List<DropdownMenuItem<T>> items, ValueChanged<T?> onChanged) {
    return DropdownButtonFormField<T>(
      value: currentValue,
      items: items,
      onChanged: onChanged,
      decoration: InputDecoration(labelText: label),
      validator: (value) => value == null ? 'Please select a $label' : null,
    );
  }

  // Builds the date field
  Widget _buildDateField(BuildContext context) {
    return TextFormField(
      controller: _dateController,
      decoration: InputDecoration(
        labelText: 'Billing Date',
        suffixIcon: IconButton(
          icon: const Icon(Icons.calendar_today),
          onPressed: () => _selectDate(context),
        ),
      ),
      readOnly: true,
      validator: (value) => value == null || value.isEmpty ? 'Please select the billing date' : null,
    );
  }

  Widget _buildTextField(TextEditingController controller, String label, String errorMessage,
      {TextInputType keyboardType = TextInputType.text}) {
    return TextFormField(
      controller: controller,
      decoration: InputDecoration(labelText: label),
      keyboardType: keyboardType,
      validator: (value) {
        if (value == null || value.isEmpty) return errorMessage;
        if (keyboardType == TextInputType.number && double.tryParse(value) == null)
          return 'Please enter a valid number';
        return null;
      },
    );
  }

  @override
  void dispose() {
    // Dispose controllers when the page is disposed of
    _invoiceNumberController.dispose();
    _amountController.dispose();
    _dateController.dispose();
    super.dispose();
  }
}
