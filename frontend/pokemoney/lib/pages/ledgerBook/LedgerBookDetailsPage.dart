import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/ledgerBook/TransactionForm.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:provider/provider.dart';

class LedgerBookDetailsPage extends StatelessWidget {
  final LedgerBook ledgerBook;

  const LedgerBookDetailsPage(this.ledgerBook, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Fetch ledger book details and transactions when the page is built
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<LedgerBookProvider>(context, listen: false).fetchLedgerBookDetails(ledgerBook.id!);
      Provider.of<TransactionProvider>(context, listen: false).fetchTransactionsForLedgerBook(ledgerBook.id!);
    });
    return Scaffold(
      appBar: AppBar(
        title: Text(
          ledgerBook.title,
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
                LedgerBookCard(ledgerBook, 'assets/backgorund_credit/small_background_creditcard.png', false),
                const SizedBox(height: 10),
                const CollaboratorSection(),
                const SizedBox(height: 10),
                HistoryTransactionsSection(
                  transactions: provider.transactions, // Here we use the transactions from the provider
                  ledgerBook: ledgerBook,
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
                builder: (context) => TransactionForm(ledgerBook: ledgerBook),
              ),
            );
            // Refresh ledger book details and transactions after adding a transaction
            Provider.of<LedgerBookProvider>(context, listen: false).fetchLedgerBookDetails(ledgerBook.id!);
            Provider.of<TransactionProvider>(context, listen: false)
                .fetchTransactionsForLedgerBook(ledgerBook.id!);
          },
          icon: const Icon(Icons.add),
          label: const Text('Transactions')),
    );
  }
}
