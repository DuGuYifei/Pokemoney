import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionProvider.dart';
import 'package:provider/provider.dart';

class TransactionForm extends StatefulWidget {
  final LedgerBook ledgerBook;

  const TransactionForm({Key? key, required this.ledgerBook}) : super(key: key);

  @override
  _TransactionFormState createState() => _TransactionFormState();
}

class _TransactionFormState extends State<TransactionForm> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _invoiceNumberController = TextEditingController();
  final TextEditingController _typeController = TextEditingController();
  final TextEditingController _categoryController = TextEditingController();

  DateTime selectedDate = DateTime.now();

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
      final Transaction newTransaction = Transaction(
        ledgerBookId: widget.ledgerBook.id!,
        invoiceNumber: _invoiceNumberController.text,
        billingDate: selectedDate,
        amount: double.parse(_amountController.text),
        type: _typeController.text,
        category: _categoryController.text,
      );

      // Assuming you have a method like `addTransaction` on your TransactionProvider
      //transactionProvider.addTransaction(newTransaction);
      Provider.of<TransactionProvider>(context, listen: false).addTransaction(newTransaction).then((_) {
        // Show a success message
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Transaction added successfully')),
        );
        // Then pop back to the previous screen
        Navigator.of(context).pop();
      }).catchError((error) {
        // Handle any errors here
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to add transaction: $error')),
        );
      });
    }

    // Assume TransactionProvider has an addTransaction method

    //}
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Add Transaction')),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Consumer<TransactionProvider>(
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
                        icon: Icon(Icons.calendar_today),
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
                    decoration: InputDecoration(labelText: 'Amount'),
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
                  TextFormField(
                    controller: _categoryController,
                    decoration: const InputDecoration(labelText: 'Category'),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter the category';
                      }
                      return null;
                    },
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
        ),
      ),
    );
  }

  @override
  void dispose() {
    _dateController.dispose();
    _amountController.dispose();
    _invoiceNumberController.dispose();
    _typeController.dispose();
    _categoryController.dispose();
    super.dispose();
  }
}
// import 'package:flutter/material.dart';
// import 'package:intl/intl.dart';
// import 'package:pokemoney/model/barrel.dart';

// class TransactionForm extends StatefulWidget {
//   final LedgerBook ledgerBook;

//   const TransactionForm({Key? key, required this.ledgerBook}) : super(key: key);

//   @override
//   _TransactionFormState createState() => _TransactionFormState();
// }

// class _TransactionFormState extends State<TransactionForm> {
//   final _formKey = GlobalKey<FormState>();
//   final TextEditingController _dateController = TextEditingController();
//   final TextEditingController _amountController = TextEditingController();
//   final TextEditingController _invoiceNumberController = TextEditingController();
//   final TextEditingController _typeController = TextEditingController();
//   final TextEditingController _categoryController = TextEditingController();

//   DateTime selectedDate = DateTime.now();

//   Future<void> _selectDate(BuildContext context) async {
//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: selectedDate,
//       firstDate: DateTime(2000),
//       lastDate: DateTime(2101),
//     );
//     if (picked != null && picked != selectedDate) {
//       setState(() {
//         selectedDate = picked;
//         _dateController.text = DateFormat.yMd().format(selectedDate);
//       });
//     }
//   }

//   void _submitForm() {
//     if (_formKey.currentState!.validate()) {
//       final Transaction newTransaction = Transaction(
//         ledgerBookId: widget.ledgerBook.id!,
//         invoiceNumber: _invoiceNumberController.text,
//         billingDate: selectedDate,
//         amount: double.parse(_amountController.text),
//         type: _typeController.text,
//         category: _categoryController.text,
//       );

//       // Process the transaction e.g., save to database or send to an API

//       // Show success message or navigate away
//     }
//   }

//   @override
//   Widget build(BuildContext context) {
//     return AlertDialog(
//       title: Text('Add Transaction'),
//       content: Form(
//         key: _formKey,
//         child: SingleChildScrollView(
//           child: Column(
//             mainAxisSize: MainAxisSize.min,
//             children: <Widget>[
//               TextFormField(
//                 controller: _invoiceNumberController,
//                 decoration: InputDecoration(labelText: 'Invoice Number'),
//                 validator: (value) {
//                   if (value == null || value.isEmpty) {
//                     return 'Please enter the invoice number';
//                   }
//                   return null;
//                 },
//               ),
//               TextFormField(
//                 controller: _dateController,
//                 decoration: InputDecoration(
//                   labelText: 'Billing Date',
//                   suffixIcon: IconButton(
//                     icon: Icon(Icons.calendar_today),
//                     onPressed: () => _selectDate(context),
//                   ),
//                 ),
//                 readOnly: true,
//                 validator: (value) {
//                   if (value == null || value.isEmpty) {
//                     return 'Please select the billing date';
//                   }
//                   return null;
//                 },
//               ),
//               TextFormField(
//                 controller: _amountController,
//                 decoration: InputDecoration(labelText: 'Amount'),
//                 keyboardType: TextInputType.number,
//                 validator: (value) {
//                   if (value == null || value.isEmpty) {
//                     return 'Please enter the amount';
//                   }
//                   if (double.tryParse(value) == null) {
//                     return 'Please enter a valid number';
//                   }
//                   return null;
//                 },
//               ),
//               TextFormField(
//                 controller: _typeController,
//                 decoration: InputDecoration(labelText: 'Type'),
//                 validator: (value) {
//                   if (value == null || value.isEmpty) {
//                     return 'Please enter the transaction type';
//                   }
//                   return null;
//                 },
//               ),
//               TextFormField(
//                 controller: _categoryController,
//                 decoration: InputDecoration(labelText: 'Category'),
//                 validator: (value) {
//                   if (value == null || value.isEmpty) {
//                     return 'Please enter the category';
//                   }
//                   return null;
//                 },
//               ),
//             ],
//           ),
//         ),
//       ),
//       actions: <Widget>[
//         TextButton(
//           onPressed: () {
//             Navigator.of(context).pop(); // Close the dialog
//           },
//           child: Text('Cancel'),
//         ),
//         ElevatedButton(
//           onPressed: () {
//             if (_formKey.currentState!.validate()) {
//               _submitForm();
//               Navigator.of(context).pop(); // Close the dialog
//             }
//           },
//           child: Text('Save'),
//         ),
//       ],
//     );
//   }

//   @override
//   void dispose() {
//     _dateController.dispose();
//     _amountController.dispose();
//     _invoiceNumberController.dispose();
//     _typeController.dispose();
//     _categoryController.dispose();
//     super.dispose();
//   }
// }
