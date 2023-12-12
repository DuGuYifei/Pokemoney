import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:flutter_svg/svg.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/pages/ledgerBook/EditTransactionPage.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/SubCategoryProvider.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/constants/AppColors.dart';

class TransactionsPage extends StatefulWidget {
  final LedgerBook ledgerBook;

  const TransactionsPage({Key? key, required this.ledgerBook}) : super(key: key);

  @override
  _TransactionsPageState createState() => _TransactionsPageState();
}

class _TransactionsPageState extends State<TransactionsPage> {
  late ScrollController _scrollController;
  final TextEditingController _searchController = TextEditingController();
  List<Transaction> _filteredTransactions = [];
  String _selectedSubCategory = 'All'; // Default value for category filter
  bool _sortAscending = true; // Default value for sorting order

  List<String> _subCategories = [];
  Map<String, int> subCategoryMap = {
    'Restaurant': 1,
    'Transportation': 2,
    'Rent': 3,
    'Grocery': 4,
    'Shopping': 5,
    'Entertainment': 6,
    'Saving': 7,
    'Other': 8,
    'Job': 9,
    'All': -1
  };

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_onScroll);
    _searchController.addListener(_onSearchChanged);
    _subCategories = subCategoryMap.keys.toList(); // Use keys from the map
    // You could initialize the transaction fetch here if needed
    // Provider.of<TransactionProvider>(context, listen: false).fetchAllTransactions();
    // Other initializations...
  }

  void _onScroll() {
    if (_scrollController.position.pixels == _scrollController.position.maxScrollExtent) {
      // This is where you would trigger fetching more transactions
    }
  }

  void _onSearchChanged() {
    _applyFilterAndSort();
  }

  void _applyFilterAndSort() {
    final transactionProvider = Provider.of<TransactionProvider>(context, listen: false);
    List<Transaction> transactions = transactionProvider.filteredTransactions;

    // Filter by search text
    if (_searchController.text.isNotEmpty) {
      transactions = transactions.where((transaction) {
        return transaction.comment!.toLowerCase().contains(_searchController.text.toLowerCase()) ||
            transaction.invoiceNumber.toLowerCase().contains(_searchController.text.toLowerCase());
      }).toList();
    }

// Filter by selected subcategory if not 'All'
    if (_selectedSubCategory != 'All') {
      int subCategoryId = subCategoryMap[_selectedSubCategory] ?? -1;
      transactions = transactions.where((transaction) {
        return transaction.subCategoryId == subCategoryId;
      }).toList();
    }

    // Sort transactions
    transactions.sort((a, b) {
      if (_sortAscending) {
        // Replace 'transactionDate' with the actual property name in your Transaction class
        return a.billingDate.compareTo(b.billingDate);
      } else {
        return b.billingDate.compareTo(a.billingDate);
      }
    });

    // Update the filtered transactions list
    setState(() {
      _filteredTransactions = transactions;
    });
  }

  String _getCategoryNameById(int subCategoryId) {
    // Assuming you have a method in SubCategoryProvider to get the category name by ID
    final subCategoryProvider = Provider.of<SubCategoryProvider>(context, listen: false);
    return subCategoryProvider.getCategoryNameById(subCategoryId);
  }

  int _getCategoryIdByName(String categoryName) {
    // Implement this method based on how your categories are stored or mapped
    Map<String, int> categoryMap = {
      'Restaurant': 1,
      'Transportation': 2,
      'Rent': 3,
      'Grocery': 4,
      'Shopping': 5,
      'Entertainment': 6,
      'Saving': 7,
      'Other': 8,
      'Job': 9,
      'All': -1
    };
    return categoryMap[categoryName] ?? -1;
  }

  @override
  Widget build(BuildContext context) {
    // Here we'll use the Consumer widget to listen to TransactionProvider
    return Scaffold(
      appBar: AppBar(
        title: Text('${widget.ledgerBook.title} Transactions',
            style: const TextStyle(fontWeight: FontWeight.bold)),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.whiteBackgorund,
      body: Container(
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                // The trail of the filter button with subcategories
                // DropdownButton(
                //   value: _selectedSubCategory,
                //   items: _subCategories.map((String subCategory) {
                //     return DropdownMenuItem(value: subCategory, child: Text(subCategory));
                //   }).toList(),
                //   onChanged: (String? newValue) {
                //     setState(() {
                //       _selectedSubCategory = newValue!;
                //       _applyFilterAndSort();
                //     });
                //   },
                // ),
                Spacer(),
                const Text(
                  "Sort by date:",
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                IconButton(
                  icon: Icon(_sortAscending ? Icons.arrow_upward : Icons.arrow_downward),
                  onPressed: () {
                    setState(() {
                      _sortAscending = !_sortAscending;
                      _applyFilterAndSort();
                    });
                  },
                ),
                Spacer(),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                controller: _searchController,
                decoration: const InputDecoration(
                    labelText: "Search",
                    hintText: "Search by comment or invoice number",
                    prefixIcon: Icon(Icons.search),
                    border: OutlineInputBorder(borderRadius: BorderRadius.all(Radius.circular(25.0)))),
              ),
            ),
// Inside the Scaffold's body, within the Expanded widget
            Expanded(
              child: Consumer<TransactionProvider>(
                builder: (context, transactionProvider, child) {
                  // This time we need to handle both cases when _filteredTransactions is empty or not
                  var transactionsToShow = _searchController.text.isNotEmpty
                      ? _filteredTransactions
                      : transactionProvider.filteredTransactions;

                  if (transactionsToShow.isEmpty) {
                    // If the list is empty, display "No results"
                    return Center(
                      child: Text(
                        "No results",
                        style: TextStyle(fontSize: 18, color: Colors.grey[600]),
                      ),
                    );
                  } else {
                    // If the list has items, display them as usual
                    return ListView.builder(
                      controller: _scrollController,
                      itemCount: transactionsToShow.length,
                      itemBuilder: (context, index) {
                        return TransactionListItem(
                          transaction: transactionsToShow[index],
                          onTransactionDeleted: () {
                            setState(() {
                              // Rebuild the UI to reflect changes
                              transactionsToShow.removeAt(index); // Or however you need to refresh your list
                            });
                          },
                        );
                      },
                    );
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }
}

class TransactionListItem extends StatelessWidget {
  final Transaction transaction;
  final VoidCallback onTransactionDeleted; // Add a callback

  const TransactionListItem({
    Key? key,
    required this.transaction,
    required this.onTransactionDeleted,
  }) : super(key: key);

  void _onMenuSelected(String value, BuildContext context) {
    switch (value) {
      case 'Delete':
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            title: const Text("Delete Confirmation"),
            content: const Text("Are you sure you want to delete this transaction?"),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.of(ctx).pop();
                },
                child: const Text("Cancel"),
              ),
              TextButton(
                onPressed: () {
                  context.read<TransactionProvider>().deleteTransaction(transaction);
                  Navigator.of(ctx).pop();
                  onTransactionDeleted(); // Call the callback after deletion
                },
                child: const Text("Delete"),
              ),
            ],
          ),
        );
        break;
      case 'Edit':
        // Navigate to the EditTransactionPage with the current transaction
        Navigator.of(context)
            .push(
          MaterialPageRoute(
            builder: (context) => EditTransactionPage(transaction: transaction),
          ),
        )
            .then((_) {
          // If you need to do something when coming back from the edit page,
          // like refreshing the list, you can call it here.
          //context.read<TransactionProvider>().fetchAllTransactions();
        });
        break;
      // Handle other options here
    }
  }

  Widget _buildPopupMenu(BuildContext context) {
    return PopupMenuButton<String>(
        surfaceTintColor: Colors.white,
        icon: const Icon(Icons.more_vert_outlined),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10.0),
        ),
        itemBuilder: (BuildContext context) => [
              const PopupMenuItem<String>(
                value: 'Edit',
                child: ListTile(leading: Icon(Icons.edit), title: Text('Edit')),
              ),
              const PopupMenuDivider(),
              const PopupMenuItem<String>(
                value: 'Delete',
                child: ListTile(
                  leading: Icon(Icons.delete, color: Colors.red),
                  title: Text('Delete', style: TextStyle(color: Colors.red)),
                ),
              ),
            ],
        onSelected: (value) => _onMenuSelected(value, context));
  }

  @override
  Widget build(BuildContext context) {
    // Access the category directly from the provider's cache
    SubCategory? subCategory =
        Provider.of<TransactionProvider>(context, listen: false).getSubCategoryForTransaction(transaction);

    Fund? fund = Provider.of<TransactionProvider>(context, listen: false).getFundForTransaction(transaction);

    return Card(
      child: ListTile(
        leading: subCategory != null
            ? ClipRRect(
                borderRadius: BorderRadius.circular(10), // Adjust the radius as needed
                child: SvgPicture.asset(
                  subCategory.iconPath!,
                  width: 50,
                  height: 50,
                ),
              )
            : const SizedBox(
                width: 45,
                height: 45,
                child: CircularProgressIndicator(), // Show a progress indicator while category is null
              ),
        title: Container(
          padding: const EdgeInsets.symmetric(vertical: 3.0, horizontal: 8.0),
          decoration: BoxDecoration(
            color: transaction.type == 'Income' ? Colors.green[100] : Colors.red[100],
            borderRadius: BorderRadius.circular(4.0),
            border: Border.all(
              color: transaction.type == 'Income' ? Colors.green : Colors.red,
              width: 1.0,
            ),
          ),
          child: Text(
            '\$${transaction.amount.toStringAsFixed(2)}',
            style: TextStyle(
              color: transaction.type == 'Income' ? Colors.green : Colors.red,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Invoice Number: ${transaction.invoiceNumber}'
                '\nDate: ${DateFormat('yMMMd').format(transaction.billingDate)}'
                '\nType: ${transaction.type}'
                '\nFund: ${fund != null ? fund.name : ' fund is null'}'),
            if (transaction.comment != null) Text('Comment: ${transaction.comment}')
          ],
        ),
        trailing: _buildPopupMenu(context),
      ),
    );
  }
}

  // void _applyFilterAndSort() {
  //   // Get a reference to TransactionProvider
  //   var transactionProvider = Provider.of<TransactionProvider>(context, listen: false);

  //   var transactions = transactionProvider.transactions;

  //   // Filter transactions by selected category
  //   if (_selectedCategory != 'All') {
  //     transactions = transactions.where((transaction) {
  //       String categoryName = transactionProvider.getCategoryNameForTransaction(transaction)?.name ?? '';
  //       return categoryName == _selectedCategory;
  //     }).toList();
  //   }

  //   // Sort transactions
  //   transactions.sort((a, b) =>
  //       _sortAscending ? a.billingDate.compareTo(b.billingDate) : b.billingDate.compareTo(a.billingDate));

  //   // Apply search filter
  //   if (_searchController.text.isNotEmpty) {
  //     transactions = transactions.where((transaction) {
  //       return transaction.categoryId.toString().contains(_searchController.text.toLowerCase()) ||
  //           transaction.invoiceNumber.toLowerCase().contains(_searchController.text.toLowerCase());
  //     }).toList();
  //   }

  //   setState(() {
  //     _filteredTransactions = transactions;
  //   });
  // }