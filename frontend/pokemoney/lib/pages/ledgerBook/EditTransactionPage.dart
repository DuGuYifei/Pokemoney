import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:flutter_svg/svg.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart'; // Make sure to import your provider
import 'package:pokemoney/model/barrel.dart' as pokemoney; // Make sure to import your Transaction model
import 'package:pokemoney/providers/CategoryProvider.dart';

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
  late TextEditingController _typeController;

  late int _selectedCategoryId;
  late DateTime selectedDate;

  List<DropdownMenuItem<int>> categoryItems = []; // Store category items here

  @override
  void initState() {
    super.initState();
    // Initialize text editing controllers with the current transaction's values
    _invoiceNumberController = TextEditingController(text: widget.transaction.invoiceNumber);
    _amountController = TextEditingController(text: widget.transaction.amount.toString());
    _typeController = TextEditingController(text: widget.transaction.type);
    _dateController = TextEditingController(); // Add this line
    _dateController.text = DateFormat('yyyy-MM-dd')
        .format(widget.transaction.billingDate); // Format the initial date value to a string
    _selectedCategoryId = widget.transaction.categoryId; // default or initial value

    fetchAndUpdateCategories();
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

  void updateCategoryItems(List<pokemoney.Category> categories) {
    if (categories.isNotEmpty) {
      setState(() {
        //_selectedCategoryId = categories.first.id!;
        categoryItems = categories
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
      type: _typeController.text,
      billingDate: DateTime.parse(_dateController.text),
      categoryId: _selectedCategoryId,
    );

    // Use the transaction provider to update the transaction
    Provider.of<TransactionProvider>(context, listen: false)
        .updateTransaction(updatedTransaction, updatedTransaction.ledgerBookId)
        .then((_) {
      Navigator.of(context).pop(); // Dismiss the page when the update is complete
    }).catchError((error) {
      // Handle any errors here
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating transaction: $error')),
      );
    });
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
              child: Column(
                children: <Widget>[
                  TextFormField(
                    controller: _invoiceNumberController, // Your existing controller
                    decoration: InputDecoration(labelText: 'Invoice Number'),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter an Invoice Number';
                      }
                      return null; // return null if the input is valid
                    },
                  ),
                  DropdownButtonFormField<int>(
                    value: _selectedCategoryId,
                    items: categoryItems,
                    onChanged: (value) {
                      setState(() {
                        _selectedCategoryId = value!;
                      });
                    },
                    validator: (value) {
                      if (value == null) {
                        return 'Please select a category';
                      }
                      return null;
                    },
                    decoration: const InputDecoration(labelText: 'Category'),
                  ),
                  TextFormField(
                    controller: _dateController,
                    decoration: InputDecoration(
                      labelText: 'Billing Date',
                      suffixIcon: IconButton(
                        icon: const Icon(Icons.calendar_today),
                        onPressed: () => _selectDate(context),
                      ),
                    ),
                    readOnly: true,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please select the billing date';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _amountController,
                    decoration: const InputDecoration(labelText: 'Amount'),
                    keyboardType: TextInputType.number,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter the amount';
                      }
                      if (double.tryParse(value) == null) {
                        return 'Please enter a valid number';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _typeController,
                    decoration: const InputDecoration(labelText: 'Type'),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter the transaction type';
                      }
                      return null;
                    },
                  ),
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
            );
          },
        ),
      ),
    );
  }

  @override
  void dispose() {
    // Dispose controllers when the page is disposed of
    _invoiceNumberController.dispose();
    _amountController.dispose();
    _dateController.dispose();
    _typeController.dispose();
    super.dispose();
  }
}
