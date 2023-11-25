import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/CategoryProvider.dart';
import 'package:provider/provider.dart';

class TransactionForm extends StatefulWidget {
  final pokemoney.LedgerBook ledgerBook;

  const TransactionForm({Key? key, required this.ledgerBook}) : super(key: key);

  @override
  _TransactionFormState createState() => _TransactionFormState();
}

class _TransactionFormState extends State<TransactionForm> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _invoiceNumberController = TextEditingController();
  final TextEditingController _customTypeController = TextEditingController();

  late String _selectedType;
  late int _selectedCategoryId;
  late int _selectedFundId;
  DateTime _selectedDate = DateTime.now();
  List<DropdownMenuItem<int>> _categoryItems = [];
  List<DropdownMenuItem<int>> _fundItems = [];

  @override
  void initState() {
    super.initState();
    _selectedType = 'Expense'; // Default transaction type
    _selectedCategoryId = -1; // Initial value indicating category not yet selected
    _dateController.text = DateFormat.yMd().format(_selectedDate);
    fetchAndUpdateCategories();
    fetchAndUpdateFunds();
  }

  // Fetches categories and updates dropdown items
  void fetchAndUpdateCategories() {
    final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
    if (categoryProvider.categories.isEmpty) {
      categoryProvider.fetchAllCategory().then((_) {
        _updateCategoryItems(categoryProvider.categories);
      });
    } else {
      _updateCategoryItems(categoryProvider.categories);
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

  // Updates category dropdown items based on available categories
  void _updateCategoryItems(List<pokemoney.Category> categories) {
    if (categories.isNotEmpty) {
      setState(() {
        _selectedCategoryId = categories.first.id!;
        _categoryItems = categories
            .map((category) => DropdownMenuItem<int>(
                  value: category.id,
                  child: Row(
                    children: <Widget>[
                      SvgPicture.asset(category.iconPath, width: 45, height: 45),
                      const SizedBox(width: 10),
                      Text(category.name),
                    ],
                  ),
                ))
            .toList();
      });
    }
  }

  // Updates funds dropdown items based on available funds
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

  // Shows date picker dialog
  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate,
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
        _dateController.text = DateFormat.yMd().format(_selectedDate);
      });
    }
  }

  // Validates and submits the form
  void _submitForm(TransactionProvider transactionProvider) {
    if (_formKey.currentState!.validate()) {
      final pokemoney.Transaction newTransaction = pokemoney.Transaction(
        ledgerBookId: widget.ledgerBook.id!,
        fundId: _selectedFundId,
        invoiceNumber: _invoiceNumberController.text,
        billingDate: _selectedDate,
        amount: double.parse(_amountController.text),
        type: _selectedType == 'Other' ? _customTypeController.text : _selectedType,
        categoryId: _selectedCategoryId,
      );

      transactionProvider.addTransaction(newTransaction).then((_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Transaction added successfully')),
        );
        transactionProvider.fetchTransactionsForLedgerBook(widget.ledgerBook.id!);
        Navigator.of(context).pop();
      }).catchError((error) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to add transaction: $error')),
        );
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Transaction')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Consumer<CategoryProvider>(
          builder: (context, categoryProvider, child) {
            _updateCategoriesPostFrame(categoryProvider);
            return Consumer<TransactionProvider>(
              builder: (context, transactionProvider, child) {
                return Form(
                  key: _formKey,
                  child: _buildFormFields(context, transactionProvider),
                );
              },
            );
          },
        ),
      ),
    );
  }

  // Updates categories after the frame is built
  void _updateCategoriesPostFrame(CategoryProvider categoryProvider) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_categoryItems.isEmpty && categoryProvider.categories.isNotEmpty) {
        _updateCategoryItems(categoryProvider.categories);
      }
    });
  }

  // Builds the form fields
  Widget _buildFormFields(BuildContext context, TransactionProvider transactionProvider) {
    return ListView(
      children: [
        _buildTextField(_invoiceNumberController, 'Invoice Number', 'Please enter the invoice number'),
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
        _buildDropdownField<int>(_selectedCategoryId, 'Category', _categoryItems,
            (value) => setState(() => _selectedCategoryId = value!)),
        _buildDropdownField<int>(
            _selectedFundId, 'Fund', _fundItems, (value) => setState(() => _selectedFundId = value!)),
        ElevatedButton(
          onPressed: () => _submitForm(transactionProvider),
          child: const Text('Submit'),
        ),
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancel'),
        ),
      ],
    );
  }

  // Builds a generic text field
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

  // Builds a generic dropdown field
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

  @override
  void dispose() {
    _dateController.dispose();
    _amountController.dispose();
    _invoiceNumberController.dispose();
    _customTypeController.dispose();
    super.dispose();
  }
}
