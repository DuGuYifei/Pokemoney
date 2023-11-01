import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/ledgerBook/TransactionForm.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionProvider.dart';
import 'package:provider/provider.dart';

class LedgerBookDetailsPage extends StatelessWidget {
  final LedgerBook _ledgerBook;

  LedgerBookDetailsPage(this._ledgerBook, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          _ledgerBook.title,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.surface,
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Consumer<TransactionProvider>(
          builder: (context, provider, child) {
            return ListView(
              children: [
                LedgerBookCard(_ledgerBook, 'assets/backgorund_credit/small_background_creditcard.png', false),
                const SizedBox(height: 10),
                CollaboratorSection(),
                const SizedBox(height: 10),
                HistoryTransactionsSection(
                  transactions: provider.transactions, // Here we use the transactions from the provider
                ),
                const SizedBox(height: 10),
              ],
            );
          },
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
          onPressed: () async {
            await Navigator.of(context).push(
              MaterialPageRoute(
                builder: (context) => TransactionForm(ledgerBook: _ledgerBook),
              ),
            );
            // You can optionally call `fetchAllTransactions` here to refresh after coming back from the form
            // Provider.of<TransactionProvider>(context, listen: false).fetchAllTransactions();
          },
          icon: const Icon(Icons.add),
          label: const Text('Transactions')),
    );
  }
}
