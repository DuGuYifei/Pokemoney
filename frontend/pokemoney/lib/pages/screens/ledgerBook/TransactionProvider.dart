import 'package:flutter/foundation.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionService.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class TransactionProvider with ChangeNotifier {
  final TransactionService _transactionService = TransactionService();
  List<pokemoney.Transaction> _transactions = [];
// You may want to create a Map to cache categories to avoid repeated database calls.
  Map<int, pokemoney.Category> _categoryCache = {};

  List<pokemoney.Transaction> get transactions => _transactions;

  // Add isFetching to keep track of whether the provider is fetching data.
  bool _isFetching = false;

  // Public getter to expose _isFetching.
  bool get isFetching => _isFetching;

  // Call this before starting the fetch operation
  void _setFetching(bool fetching) {
    _isFetching = fetching;
    notifyListeners(); // Notify all listeners of the change.
  }

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

  Map<DateTime, double> _aggregatedTransactions = {};
  TimeGrouping _currentGrouping = TimeGrouping.day; // Default to day

  // Add getter if you need to access currentGrouping outside
  TimeGrouping get currentGrouping => _currentGrouping;

  Map<DateTime, double> get aggregatedTransactions => _aggregatedTransactions;

  void setCurrentGrouping(TimeGrouping grouping) {
    _currentGrouping = grouping;
    // Trigger the aggregation process whenever the grouping changes
    aggregateTransactions(grouping);
  }

  // Update existing methods to use _setFetching where appropriate.
  void aggregateTransactions(TimeGrouping grouping) async {
    _setFetching(true); // Set isFetching to true when fetch starts
    try {
      _aggregatedTransactions = await _transactionService.getTransactionsSumByGrouping(grouping);
    } catch (e) {
      // Handle exceptions by perhaps logging them or storing an error message in the provider.
    } finally {
      _setFetching(false); // Ensure we're not stuck in a loading state.
      notifyListeners(); // Notify all listeners of the change.
    }
  }
  // Call your database helper method to update the transaction in the SQLite database
  // Example: DBProvider.db.updateTransaction(transaction);

  // ... other methods and properties
}
