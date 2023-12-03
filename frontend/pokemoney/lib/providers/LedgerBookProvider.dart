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
      ledgerBook.currentBalance = transactionTotal; // Assuming a property for current balance
    }
    notifyListeners();
  }

  Future<void> fetchAllLedgerBooksFromSyncAndUnsync() async {
    _ledgerBooks = await _ledgerBookService.getAllLedgerBooksFromSyncAndUnsync();
    for (var ledgerBook in _ledgerBooks) {
      double transactionTotal = await _transactionService.getTotalBalanceForLedgerBook(ledgerBook.id!);
      ledgerBook.currentBalance = transactionTotal;
    }
    notifyListeners();
  }

  addLedgerBook(LedgerBook ledgerBook) async {
    await _ledgerBookService.addLedgerBook(ledgerBook);
    fetchAllLedgerBooksFromSyncAndUnsync();
  }

  deleteLedgerBook(int id) async {
    await _ledgerBookService.deleteLedgerBook(id);
    fetchAllLedgerBooksFromSyncAndUnsync();
  }

  Future<void> fetchLedgerBookDetails(int ledgerBookId) async {
    // Fetch the ledger book details
    LedgerBook ledgerBook = await _ledgerBookService.getLedgerBookById(ledgerBookId);

    // Calculate the current balance
    double transactionTotal = await _transactionService.getTotalBalanceForLedgerBook(ledgerBookId);
    ledgerBook.currentBalance = transactionTotal;

    // Find the ledger book in the list and update it
    int index = _ledgerBooks.indexWhere((lb) => lb.id == ledgerBookId);
    if (index != -1) {
      // Update existing ledger book
      _ledgerBooks[index] = ledgerBook;
    } else {
      // Append new ledger book if not in the list
      _ledgerBooks.add(ledgerBook);
    }

    notifyListeners();
  }
}
