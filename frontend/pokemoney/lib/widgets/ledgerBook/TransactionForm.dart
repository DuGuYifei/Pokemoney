import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;
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

  late String _selectedType; // default value
  final TextEditingController _customTypeController = TextEditingController();
  late int _selectedCategoryId;
  DateTime selectedDate = DateTime.now();

  //bool _isCategoriesFetched = false;
  List<DropdownMenuItem<int>> categoryItems = []; // Store category items here

  @override
  void initState() {
    super.initState();
    // Initialize category ID to an invalid state (-1 for instance) to indicate not fetched or not selected.
    _selectedCategoryId = -1;
    _selectedType = 'Expense';
    fetchAndUpdateCategories();

    _dateController.text =
        DateFormat.yMd().format(selectedDate); // Setting the initial value for the date field.
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
        _selectedCategoryId = categories.first.id!;
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

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: selectedDate,
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != selectedDate) {
      setState(() {
        selectedDate = picked;
        _dateController.text = DateFormat.yMd().format(selectedDate);
      });
    }
  }

  void _submitForm(TransactionProvider transactionProvider) {
    if (_formKey.currentState!.validate()) {
      final pokemoney.Transaction newTransaction = pokemoney.Transaction(
        ledgerBookId: widget.ledgerBook.id!,
        fundId: 1, // TODO: Replace with actual fund ID
        invoiceNumber: _invoiceNumberController.text,
        billingDate: selectedDate,
        amount: double.parse(_amountController.text),
        type: _selectedType == 'Other' ? _customTypeController.text : _selectedType,
        categoryId: _selectedCategoryId, // Use the category ID as a foreign key
      );

      // Assuming you have a method like `addTransaction` on your TransactionProvider
      //transactionProvider.addTransaction(newTransaction);
      Provider.of<TransactionProvider>(context, listen: false).addTransaction(newTransaction).then((_) {
        // Show a success message
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Transaction added successfully')),
        );
        Provider.of<TransactionProvider>(context, listen: false)
            .fetchTransactionsForLedgerBook(widget.ledgerBook.id!);
        Navigator.of(context).pop();
      }).catchError((error) {
        // Handle any errors here
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
            WidgetsBinding.instance.addPostFrameCallback((_) {
              if (categoryItems.isEmpty && categoryProvider.categories.isNotEmpty) {
                updateCategoryItems(categoryProvider.categories);
              }
            });
            return Consumer<TransactionProvider>(
              builder: (context, transactionProvider, child) {
                return Form(
                  key: _formKey,
                  child: ListView(
                    children: [
                      TextFormField(
                        controller: _invoiceNumberController,
                        decoration: const InputDecoration(labelText: 'Invoice Number'),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter the invoice number';
                          }
                          return null;
                        },
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

                      // TextFormField(
                      //   controller: _typeController,
                      //   decoration: const InputDecoration(labelText: 'Type'),
                      //   validator: (value) {
                      //     if (value == null || value.isEmpty) {
                      //       return 'Please enter the transaction type';
                      //     }
                      //     return null;
                      //   },
                      // ),
                      DropdownButtonFormField<String>(
                        value: _selectedType,
                        items: ['Expense', 'Income', 'Other']
                            .map((type) => DropdownMenuItem<String>(
                                  value: type,
                                  child: Text(type),
                                ))
                            .toList(),
                        onChanged: (value) {
                          setState(() {
                            _selectedType = value!;
                          });
                        },
                        decoration: const InputDecoration(labelText: 'Type'),
                      ),
                      if (_selectedType == 'Other') // Conditional Text Field
                        TextFormField(
                          controller: _customTypeController,
                          decoration: const InputDecoration(labelText: 'Custom Type'),
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter a type';
                            }
                            return null;
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
                      ElevatedButton(
                        onPressed: () => _submitForm(transactionProvider),
                        child: const Text('Submit'),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.of(context).pop(); // Close the dialog
                        },
                        child: const Text('Cancel'),
                      ),
                    ],
                  ),
                );
              },
            );
          },
        ),
      ),
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
