import 'package:flutter/foundation.dart';
import 'package:pokemoney/services/TransactionService.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class TransactionProvider with ChangeNotifier {
  final TransactionService _transactionService = TransactionService();
  List<pokemoney.Transaction> _transactions = [];
// You may want to create a Map to cache categories to avoid repeated database calls.
  Map<int, pokemoney.Category> _categoryCache = {};

  List<pokemoney.Transaction> get transactions => _transactions;

  // fetchAllTransactions() async {
  //   _transactions = await _transactionService.getAllTransactions();
  //   notifyListeners();
  // }

  fetchAllTransactions() async {
    _transactions = await _transactionService.getAllTransactions();
    // Fetch category for each transaction here.
    for (var transaction in _transactions) {
      // Use _categoryCache to reduce database hits for already fetched categories.
      if (!_categoryCache.containsKey(transaction.categoryId)) {
        _categoryCache[transaction.categoryId] =
            await _transactionService.getCategoryById(transaction.categoryId);
      }
      // Now each transaction has an associated category in the cache, which you can use.
    }
    notifyListeners();
  }
  
   // Method to fetch transactions for a specific ledger book
  fetchTransactionsForLedgerBook(int ledgerBookId) async {
    _transactions = await _transactionService.getTransactionsByLedgerBookId(ledgerBookId);
    // Fetch and cache categories for these transactions
    // ... existing category caching logic ...
    notifyListeners();
  }

  // addTransaction(pokemoney.Transaction transactions) async {
  //   await _transactionService.addTransaction(transactions);
  //   fetchAllTransactions();
  //   print("Transaction added");
  // }

  addTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.addTransaction(transaction);
    // Optionally fetch and cache the category for the new transaction as well
    _categoryCache[transaction.categoryId] =
        await _transactionService.getCategoryById(transaction.categoryId);
    fetchAllTransactions();
    print("Transaction added");
  }

  deleteTransaction(int id) async {
    await _transactionService.deleteTransaction(id);
    fetchAllTransactions();
  }

  updateTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.updateTransaction(transaction);
    fetchAllTransactions(); // Re-fetch the list of transactions after updating
  }

  // Helper method to get category from cache
  pokemoney.Category? getCategoryForTransaction(pokemoney.Transaction transaction) {
    return _categoryCache[transaction.categoryId];
  }

    // Call your database helper method to update the transaction in the SQLite database
    // Example: DBProvider.db.updateTransaction(transaction);

  // ... other methods and properties
}