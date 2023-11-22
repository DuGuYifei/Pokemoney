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
  late LedgerBook _currentLedgerBook;

  @override
  void initState() {
    super.initState();
    _currentLedgerBook = widget.ledgerBook;
    _fetchData();
  }

  void _fetchData() {
    var ledgerBookProvider = Provider.of<LedgerBookProvider>(context, listen: false);
    var transactionProvider = Provider.of<TransactionProvider>(context, listen: false);

    ledgerBookProvider.fetchLedgerBookDetails(_currentLedgerBook.id!).then((_) {
      setState(() {
        _currentLedgerBook = ledgerBookProvider.ledgerBooks.firstWhere(
          (lb) => lb.id == _currentLedgerBook.id,
          orElse: () => _currentLedgerBook,
        );
      });
    });

    transactionProvider.fetchTransactionsForLedgerBook(_currentLedgerBook.id!);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          _currentLedgerBook.title,
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
                _currentLedgerBook, 'assets/backgorund_credit/small_background_creditcard.png', false),
            const SizedBox(height: 10),
            const CollaboratorSection(),
            const SizedBox(height: 10),
            HistoryTransactionsSection(
              transactions: Provider.of<TransactionProvider>(context).transactions,
              ledgerBook: widget.ledgerBook,
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
            _fetchData();
          },
          icon: const Icon(Icons.add),
          label: const Text('Transactions')),
    );
  }
}
