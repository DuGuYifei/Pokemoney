import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:flutter_svg/svg.dart';
import 'package:pokemoney/model/barrel.dart'; // Import your Transaction model
import 'package:pokemoney/pages/screens/ledgerBook/EditTransactionPage.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionProvider.dart'; // Import your TransactionProvider
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

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_onScroll);
    _searchController.addListener(_onSearchChanged);
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
    if (_searchController.text.isEmpty) {
      setState(() {
        _filteredTransactions = Provider.of<TransactionProvider>(context, listen: false).transactions;
      });
    } else {
      setState(() {
        _filteredTransactions =
            Provider.of<TransactionProvider>(context, listen: false).transactions.where((transaction) {
          return transaction.categoryId.toString().contains(_searchController.text.toLowerCase()) ||
              transaction.invoiceNumber.toLowerCase().contains(_searchController.text.toLowerCase());
        }).toList();
      });
    }
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
      backgroundColor: AppColors.surface,
      body: Container(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                controller: _searchController,
                decoration: const InputDecoration(
                    labelText: "Search",
                    hintText: "Search by category or invoice number",
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
                      : transactionProvider.transactions;

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
                  context.read<TransactionProvider>().deleteTransaction(transaction.id!);
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
          context.read<TransactionProvider>().fetchAllTransactions();
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
    Category? category =
        Provider.of<TransactionProvider>(context, listen: false).getCategoryForTransaction(transaction);

    // If the category is null, the provider will fetch and update the UI accordingly.
    // Hence, you don't need a FutureBuilder if you are managing state with a provider.

    // Build a UI for the transaction list item
    return Card(
      child: ListTile(
        leading: category != null
            ? SvgPicture.asset(
                category.iconPath, // Use the iconPath from the Category object
                width: 45,
                height: 45,
              )
            : SizedBox(
                width: 45,
                height: 45,
                child: CircularProgressIndicator(), // Show a progress indicator while category is null
              ),
        title: Text('${transaction.amount}\$'),
        subtitle: Text('${transaction.invoiceNumber}'
            '\n${DateFormat('yMMMd').format(transaction.billingDate)}'),
        trailing: IconButton(
          icon: Icon(Icons.delete),
          onPressed: onTransactionDeleted,
        ),
      ),
    );
  }
}
