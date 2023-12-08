import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:pokemoney/providers/SubCategoryProvider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

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
  final TextEditingController _commentController = TextEditingController();

  late String _selectedType;
  late int _selectedSubCategoryId;
  late int _selectedFundId;
  DateTime _selectedDate = DateTime.now();
  //List<DropdownMenuItem<int>> _categoryItems = [];
  List<DropdownMenuItem<int>> _subCategoryItems = [];
  List<DropdownMenuItem<int>> _fundItems = [];

  @override
  void initState() {
    super.initState();
    _selectedType = 'Expense'; // Default transaction type
    _dateController.text = DateFormat.yMd().format(_selectedDate);
    _selectedFundId = -1; // Initial value indicating fund not yet selected
    _selectedSubCategoryId = -1; // Initial value indicating subcategory not yet selected
      WidgetsBinding.instance.addPostFrameCallback((_) {
      _updateSubCategoriesAndFundPostFrame();
      });

  }

  // Updates categories after the frame is built
void _updateSubCategoriesAndFundPostFrame() {
  final subCategoryProvider = Provider.of<SubCategoryProvider>(context, listen: false);
  final fundProvider = Provider.of<FundProvider>(context, listen: false);

  subCategoryProvider.fetchAllSubCategoriesFromSyncAndUnsync().then((_) {
    if (mounted) {
      fetchAndUpdateSubCategories();
    }
  });

  fundProvider.fetchAllFunds().then((_) {
    if (mounted) {
      fetchAndUpdateFunds();
    }
  });
}


  // Fetches categories and updates dropdown items
  void fetchAndUpdateSubCategories() {
    final subCategoryProvider = Provider.of<SubCategoryProvider>(context, listen: false);
    List<pokemoney.SubCategory> allSubCategories = [];

    if (subCategoryProvider.subCategories.isEmpty) {
      subCategoryProvider.fetchAllSubCategoriesFromSyncAndUnsync().then((_) {
        allSubCategories = subCategoryProvider.subCategories.values
            .expand((x) => x)
            .toList(); //Flatten the Map into a Single List
        _updateSubCategoryItems(allSubCategories);
      });
    } else {
      allSubCategories = subCategoryProvider.subCategories.values
          .expand((x) => x)
          .toList(); //Flatten the Map into a Single List
      _updateSubCategoryItems(allSubCategories);
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
  void _updateSubCategoryItems(List<pokemoney.SubCategory> subCategories) {
    if (subCategories.isNotEmpty) {
      setState(() {
        _selectedSubCategoryId = subCategories.first.id!;
        _subCategoryItems = subCategories
            .map((subCategory) => DropdownMenuItem<int>(
                  value: subCategory.id,
                  child: Row(
                    children: <Widget>[
                      ClipRRect(
                        borderRadius: BorderRadius.circular(10), // Adjust the radius as needed
                        child: SvgPicture.asset(
                          subCategory.iconPath ?? 'assets/category_icons/custom_icon.svg',
                          width: 45,
                          height: 45,
                        ),
                      ),
                      const SizedBox(width: 10),
                      Text(subCategory.name),
                    ],
                  ),
                ))
            .toList();
      });
    }
  }

  // Updates funds dropdown items based on available funds
  void _updateFundItems(List<pokemoney.Fund> funds) async {
    // Retrieve preferences
    final prefs = await SharedPreferences.getInstance();
    int? id = prefs.getInt('id');

    if (funds.isNotEmpty) {
      setState(() {
        _selectedFundId = funds.isNotEmpty ? funds.first.id! : -1;
        _fundItems = [
          ...funds.map((fund) => DropdownMenuItem<int>(
                value: fund.id,
                child: Row(
                  children: <Widget>[
                    Text('\$ ${fund.balance.toString()}'),
                    const SizedBox(width: 20),
                    Text(fund.name),
                  ],
                ),
              )),
          DropdownMenuItem<int>(
            value: -1,
            child: SizedBox(
              width: 200,
              child: ListTile(
                leading: const Icon(Icons.add),
                title: const Text('Add Fund'),
                onTap: () {
                  _showAddItemDialog(context, 'Fund', (name, balance) {
                    final newFund = pokemoney.Fund(
                      name: name,
                      balance: balance,
                      creationDate: DateTime.now(),
                      owner: id!,
                      editors: id.toString(),
                      updateAt: DateTime.now(),
                      delFlag: 0,
                    );
                    Provider.of<FundProvider>(context, listen: false).addFund(newFund).then((addedFundId) {
                      setState(() {
                        _selectedFundId =
                            addedFundId; // Assuming addFund returns the ID of the newly added fund
                        _updateFundItems(Provider.of<FundProvider>(context, listen: false).funds);
                      });
                    });
                  });
                },
              ),
            ),
          ),
        ];
      });
    }
  }

  void _showAddItemDialog(BuildContext context, String itemType, Function(String, double) onAdd) {
    TextEditingController nameController = TextEditingController();
    TextEditingController balanceController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Add $itemType'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              TextField(
                controller: nameController,
                decoration: InputDecoration(hintText: "$itemType Name"),
              ),
              if (itemType == 'Fund')
                TextField(
                  controller: balanceController,
                  decoration: const InputDecoration(hintText: "Balance"),
                  keyboardType: const TextInputType.numberWithOptions(decimal: true),
                ),
            ],
          ),
          actions: <Widget>[
            TextButton(
              child: const Text('Cancel'),
              onPressed: () => Navigator.of(context).pop(),
            ),
            TextButton(
              child: const Text('Add'),
              onPressed: () {
                String name = nameController.text;
                double balance = itemType == 'Fund' ? double.tryParse(balanceController.text) ?? 0 : 0;
                onAdd(name, balance);
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
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
  Future<void> _submitForm(TransactionProvider transactionProvider) async {
    final subCategoryProvider = Provider.of<SubCategoryProvider>(context, listen: false);

    // Retrieve preferences asynchronously before showing the dialog
    final prefs = await SharedPreferences.getInstance();
    int? id = prefs.getInt('id');

    if (id == null) {
      // Handle null id appropriately
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('User ID is not available')),
      );
      return;
    }
    // Wait for all subcategories to be fetched
    await subCategoryProvider.fetchAllSubCategoriesFromSyncAndUnsync();

    if (subCategoryProvider.subCategories.isEmpty) {
      // Handle empty subcategory list
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('No subcategories available')),
      );
      return;
    }

    List<pokemoney.SubCategory> allSubCategories = subCategoryProvider.subCategories.values
        .expand((x) => x)
        .toList(); // Flatten the Map into a Single List

    if (subCategoryProvider.subCategories.isNotEmpty) {
      subCategoryProvider.fetchAllSubCategoriesFromSyncAndUnsync().then((_) {
        allSubCategories = subCategoryProvider.subCategories.values
            .expand((x) => x)
            .toList(); //Flatten the Map into a Single List
      });

      // Handle the case where no matching subcategory is found
      pokemoney.SubCategory? subCategory =
          allSubCategories.firstWhere((subcategory) => subcategory.id == _selectedSubCategoryId, orElse: () {
        // Throw an error indicating that no matching subcategory was found
        throw Exception('No matching subcategory found with ID: $_selectedSubCategoryId');
      });

      if (_formKey.currentState?.validate() ?? false) {
        final pokemoney.Transaction newTransaction = pokemoney.Transaction(
          ledgerBookId: widget.ledgerBook.id!,
          fundId: _selectedFundId,
          invoiceNumber: _invoiceNumberController.text,
          billingDate: _selectedDate,
          amount: double.parse(_amountController.text),
          type: _selectedType == 'Other' ? _customTypeController.text : _selectedType,
          categoryId: subCategory.categoryId,
          subCategoryId: _selectedSubCategoryId,
          comment: _commentController.text,
          updatedBy: id,
          delFlag: 0,
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
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Form validation failed')),
        );
      }
    }
  }

@override
Widget build(BuildContext context) {
  final subCategoryProvider = Provider.of<SubCategoryProvider>(context, listen: false);
  final fundProvider = Provider.of<FundProvider>(context, listen: false);
  final transactionProvider = Provider.of<TransactionProvider>(context, listen: false);

  return Scaffold(
    appBar: AppBar(title: const Text('Add Transaction')),
    body: Padding(
      padding: const EdgeInsets.all(16.0),
      child: FutureBuilder(
        future: Future.wait([
          subCategoryProvider.fetchAllSubCategoriesFromSyncAndUnsync(),
          fundProvider.fetchAllFunds(),
        ]),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const CircularProgressIndicator(); // Show loading indicator
          }

          if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}'); // Handle error state
          }

          // Once data is fetched, build the form
          return Form(
            key: _formKey,
            child: _buildFormFields(context, subCategoryProvider, fundProvider,transactionProvider),
          );
        },
      ),
    ),
  );
}


  // Builds the form fields
Widget _buildFormFields(BuildContext context, SubCategoryProvider subCategoryProvider, FundProvider fundProvider, TransactionProvider transactionProvider) {
  //fetchAndUpdateSubCategories(); // Call this to update items
  //fetchAndUpdateFunds(); // Call this to update items
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
        _buildDropdownField<int>(
          _selectedSubCategoryId,
          'Category',
          _subCategoryItems,
          (value) => setState(() => _selectedSubCategoryId = value!),
        ),
        _buildDropdownField<int>(
            _selectedFundId, 'Fund', _fundItems, (value) => setState(() => _selectedFundId = value!)),
        _buildTextField(_commentController, 'comment', 'Please enter the amount',
            maxLines: 3, validate: false),
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
      {TextInputType keyboardType = TextInputType.text,
      int maxLines = 1,
      int? minLines,
      bool validate = true}) {
    return TextFormField(
      controller: controller,
      decoration: InputDecoration(labelText: label),
      keyboardType: keyboardType,
      maxLines: maxLines,
      minLines: minLines,
      validator: validate
          ? (value) {
              if (value == null || value.isEmpty) return errorMessage;
              if (keyboardType == TextInputType.number && double.tryParse(value) == null) {
                return 'Please enter a valid number';
              }
              return null;
            }
          : null,
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

// class CustomSubCategoryDropdown extends StatefulWidget {
//   final List<pokemoney.SubCategory> subCategories;
//   final Function(int) onSelected;

//   const CustomSubCategoryDropdown({
//     Key? key,
//     required this.subCategories,
//     required this.onSelected,
//   }) : super(key: key);

//   @override
//   _CustomSubCategoryDropdownState createState() => _CustomSubCategoryDropdownState();
// }

// class _CustomSubCategoryDropdownState extends State<CustomSubCategoryDropdown> {
//   List<pokemoney.SubCategory> filteredSubCategories = [];
//   String searchValue = '';

//   @override
//   void initState() {
//     super.initState();
//     filteredSubCategories = widget.subCategories;
//   }

//   void _filterSubCategories(String enteredKeyword) {
//     List<pokemoney.SubCategory> results = [];
//     if (enteredKeyword.isEmpty) {
//       results = widget.subCategories;
//     } else {
//       results = widget.subCategories
//           .where((subCategory) => subCategory.name.toLowerCase().contains(enteredKeyword.toLowerCase()))
//           .toList();
//     }

//     setState(() {
//       filteredSubCategories = results;
//     });
//   }

//   @override
//   Widget build(BuildContext context) {
//     return PopupMenuButton<int>(
//       onSelected: widget.onSelected,
//       itemBuilder: (BuildContext context) {
//         return [
//           PopupMenuItem(
//             enabled: false,
//             child: TextField(
//               onChanged: (value) => _filterSubCategories(value),
//               decoration: InputDecoration(
//                 hintText: 'Search',
//                 prefixIcon: Icon(Icons.search),
//                 border: OutlineInputBorder(borderRadius: BorderRadius.circular(20)),
//               ),
//             ),
//           ),
//           ...filteredSubCategories.map((subCategory) {
//             return PopupMenuItem<int>(
//               value: subCategory.id,
//               child: Text(subCategory.name),
//             );
//           }).toList(),
//         ];
//       },
//       child: Container(
//         padding: EdgeInsets.symmetric(horizontal: 10, vertical: 5),
//         decoration: BoxDecoration(
//           borderRadius: BorderRadius.circular(5),
//           border: Border.all(color: Colors.grey),
//         ),
//         child: Row(
//           children: <Widget>[
//             Text('Select SubCategory'),
//             Spacer(),
//             Icon(Icons.arrow_drop_down),
//           ],
//         ),
//       ),
//     );
//   }
// }
