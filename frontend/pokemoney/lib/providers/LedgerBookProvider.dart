import 'package:flutter/foundation.dart';
import 'package:pokemoney/services/LedgerBookService.dart';
import 'package:pokemoney/services/TransactionService.dart';
import 'package:pokemoney/model/barrel.dart';

class LedgerBookProvider with ChangeNotifier {
  final LedgerBookService _ledgerBookService = LedgerBookService();
  final TransactionService _transactionService = TransactionService();

  List<LedgerBook> _ledgerBooks = [];

  List<LedgerBook> get ledgerBooks => _ledgerBooks;

  Future<void> fetchAllLedgerBooks() async {
    _ledgerBooks = await _ledgerBookService.getAllLedgerBooks();
    for (var ledgerBook in _ledgerBooks) {
      double transactionTotal = await _transactionService.getTotalBalanceForLedgerBook(ledgerBook.id!);
      ledgerBook.currentBalance =
          ledgerBook.initialBalance + transactionTotal; // Assuming a property for current balance
    }
    notifyListeners();
  }

  addLedgerBook(LedgerBook ledgerBook) async {
    await _ledgerBookService.addLedgerBook(ledgerBook);
    fetchAllLedgerBooks();
  }

  deleteLedgerBook(int id) async {
    await _ledgerBookService.deleteLedgerBook(id);
    fetchAllLedgerBooks();
  }

  Future<void> fetchLedgerBookDetails(int ledgerBookId) async {
    // Fetch the ledger book details
    LedgerBook ledgerBook = await _ledgerBookService.getLedgerBookById(ledgerBookId);

    // Calculate the current balance
    double transactionTotal = await _transactionService.getTotalBalanceForLedgerBook(ledgerBookId);
    ledgerBook.currentBalance = ledgerBook.initialBalance + transactionTotal;

    // Update the local state
    // You might want to store the fetched ledger book in a field or a map
    _ledgerBooks[ledgerBookId] = ledgerBook;

    notifyListeners();
  }
}
