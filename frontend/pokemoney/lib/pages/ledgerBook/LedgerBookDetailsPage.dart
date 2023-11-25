import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/ledgerBook/TransactionForm.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:provider/provider.dart';

class LedgerBookDetailsPage extends StatefulWidget {
  final LedgerBook ledgerBook;

  const LedgerBookDetailsPage(this.ledgerBook, {Key? key}) : super(key: key);

  @override
  State<LedgerBookDetailsPage> createState() => _LedgerBookDetailsPageState();
}

class _LedgerBookDetailsPageState extends State<LedgerBookDetailsPage> {
  @override
  Widget build(BuildContext context) {
    // Watching providers
    final ledgerBookProvider = context.watch<LedgerBookProvider>();
    final transactionProvider = context.watch<TransactionProvider>();

    // Retrieve the updated ledger book or fallback to the one passed in the widget
    LedgerBook currentLedgerBook = ledgerBookProvider.ledgerBooks.firstWhere(
      (lb) => lb.id == widget.ledgerBook.id,
      orElse: () => widget.ledgerBook,
    );

    return Scaffold(
      appBar: AppBar(
        title: Text(
          currentLedgerBook.title,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.surface,
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            LedgerBookCard(
                currentLedgerBook, 'assets/backgorund_credit/small_background_creditcard.png', false),
            const SizedBox(height: 10),
            const CollaboratorSection(),
            const SizedBox(height: 10),
            HistoryTransactionsSection(
              transactions: transactionProvider.transactions,
              ledgerBook: currentLedgerBook,
            ),
            const SizedBox(height: 10),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
          onPressed: () async {
            await Navigator.of(context).push(
              MaterialPageRoute(
                builder: (context) => TransactionForm(ledgerBook: widget.ledgerBook),
              ),
            );
          },
          icon: const Icon(Icons.add),
          label: const Text('Transactions')),
    );
  }
}
