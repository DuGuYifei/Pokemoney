import 'package:flutter/foundation.dart';
import 'package:pokemoney/services/TransactionService.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:pokemoney/services/CategoryService.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class TransactionProvider with ChangeNotifier {
  final TransactionService _transactionService = TransactionService();
  final CategoryService _categoryService = CategoryService();
  final LedgerBookProvider _ledgerBookProvider = LedgerBookProvider();

  List<pokemoney.Transaction> _transactions = [];
  Map<int, pokemoney.Category> _categoryCache = {};

  List<pokemoney.Transaction> get transactions => _transactions;

  // Fetches all transactions and their associated categories
  Future<void> fetchAllTransactions() async {
    _transactions = await _transactionService.getAllTransactions();

    for (var transaction in _transactions) {
      if (!_categoryCache.containsKey(transaction.categoryId)) {
        try {
          pokemoney.Category category = await _categoryService.getCategoryById(transaction.categoryId);
          _categoryCache[transaction.categoryId] = category;
        } catch (e) {
          // Handle exceptions, e.g., category not found
          print('Error fetching category: $e');
        }
      }
    }
    notifyListeners();
  }

  // Fetches transactions for a specific ledger book
  Future<void> fetchTransactionsForLedgerBook(int ledgerBookId) async {
    _transactions = await _transactionService.getTransactionsByLedgerBookId(ledgerBookId);

    for (var transaction in _transactions) {
      if (!_categoryCache.containsKey(transaction.categoryId)) {
        try {
          pokemoney.Category category = await _categoryService.getCategoryById(transaction.categoryId);
          _categoryCache[transaction.categoryId] = category;
        } catch (e) {
          // Handle exceptions, e.g., category not found
          print('Error fetching category: $e');
        }
      }
    }
    notifyListeners();
  }

  // Adds a new transaction
  Future<void> addTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.addTransaction(transaction);
    if (!_categoryCache.containsKey(transaction.categoryId)) {
      try {
        pokemoney.Category category = await _categoryService.getCategoryById(transaction.categoryId);
        _categoryCache[transaction.categoryId] = category;
        _transactionService.getTotalBalanceForLedgerBook(transaction.ledgerBookId);
      } catch (e) {
        // Handle exceptions, e.g., category not found
        print('Error fetching category: $e');
      }
    }
  }

  updateTransaction(pokemoney.Transaction transaction, int ledgerBookId) async {
    await _transactionService.updateTransaction(transaction);
    fetchTransactionsForLedgerBook(ledgerBookId);
    await _ledgerBookProvider.fetchLedgerBookDetails(ledgerBookId);
  }

  // Deletes a transaction
  Future<void> deleteTransaction(int transactionId, int ledgerBookId) async {
    await _transactionService.deleteTransaction(transactionId);
    fetchTransactionsForLedgerBook(ledgerBookId);
  }

  Future<void> getTotalBalanceForLedgerBook(int ledgerBookId) async {
    await _transactionService.getTotalBalanceForLedgerBook(ledgerBookId);
  }

  // Helper method to get a category from the cache
  pokemoney.Category? getCategoryForTransaction(pokemoney.Transaction transaction) {
    return _categoryCache[transaction.categoryId];
  }

  pokemoney.Category? getCategoryNameForTransaction(pokemoney.Transaction transaction) {
    return _categoryCache[transaction.categoryId];
  }
}
