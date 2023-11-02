import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart'; // Import your Transaction model
import 'package:pokemoney/pages/screens/ledgerBook/TransactionProvider.dart'; // Import your TransactionProvider
import 'package:provider/provider.dart';
import 'package:pokemoney/constants/AppColors.dart';

class TransactionsPage extends StatefulWidget {
  final LedgerBook ledgerBook;

  TransactionsPage({Key? key, required this.ledgerBook}) : super(key: key);

  @override
  _TransactionsPageState createState() => _TransactionsPageState();
}

class _TransactionsPageState extends State<TransactionsPage> {
  late ScrollController _scrollController;

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_onScroll);
    // You could initialize the transaction fetch here if needed
  }

  void _onScroll() {
    if (_scrollController.position.pixels == _scrollController.position.maxScrollExtent) {
      // This is where you would trigger fetching more transactions
    }
  }

  @override
  Widget build(BuildContext context) {
    // Here we'll use the Consumer widget to listen to TransactionProvider
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Transactions',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.surface,
      body: Consumer<TransactionProvider>(
        builder: (context, transactionProvider, child) {
          var transactions = transactionProvider.transactions; // Assuming this is the correct getter

          return ListView.builder(
            controller: _scrollController,
            itemCount: transactions.length,
            itemBuilder: (context, index) {
              return TransactionListItem(transaction: transactions[index]);
            },
          );
        },
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

  TransactionListItem({Key? key, required this.transaction}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Build a UI for the transaction list item
    return Card(
      child: ListTile(
        leading: Icon(Icons.attach_money), // Just an example icon
        title: Text(transaction.invoiceNumber),
        subtitle: Text(transaction.category),
        trailing: Text('\$${transaction.amount.toStringAsFixed(2)}'),
        onTap: () {
          // Handle the tap if needed, possibly navigate to a detail view
        },
      ),
    );
  }
}
