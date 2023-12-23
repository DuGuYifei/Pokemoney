import 'package:flutter/foundation.dart';
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:pokemoney/services/TransactionService.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:pokemoney/services/CategoryService.dart';
import 'package:pokemoney/services/SubCategoryService.dart';
import 'package:pokemoney/services/LedgerBookService.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;
import 'package:pokemoney/services/FundService.dart';

class TransactionProvider with ChangeNotifier {
  final TransactionService _transactionService = TransactionService();
  final CategoryService _categoryService = CategoryService();
  final SubCategoryService _subCategoryService = SubCategoryService();
  final FundService _fundService = FundService(); // FundService instance
  final LedgerBookService _ledgerBookService = LedgerBookService(); // FundService instance
  FundProvider? _fundProvider;
  LedgerBookProvider? _ledgerBookProvider;

  // Constructor takes an initial FundProvider, which can be null
  TransactionProvider({FundProvider? fundProvider, LedgerBookProvider? ledgerBookProvider})
      : _fundProvider = fundProvider,
        _ledgerBookProvider = ledgerBookProvider;

  void updateProviders(FundProvider fundProvider, LedgerBookProvider ledgerBookProvider) {
    _fundProvider = fundProvider;
    _ledgerBookProvider = ledgerBookProvider;
    // Optional: Perform any initialization or data fetching here
    notifyListeners();
  }

  List<pokemoney.Transaction> _transactions = []; // List of transactions
  List<pokemoney.Transaction> _filteredTransactions = []; // List of filtered transactions
  final Map<int, pokemoney.Category> _categoryCache = {};
  final Map<int, pokemoney.SubCategory> _subCategoryCache = {};
  final Map<int, pokemoney.Fund> _fundCache = {}; // Cache for funds
  final Map<int, pokemoney.LedgerBook> _ledgerBookCache = {}; // Cache for ledger books

// Method to get all transactions
  List<pokemoney.Transaction> get transactions => _transactions;

  // Method to get filtered transactions for a specific ledger book
  List<pokemoney.Transaction> get filteredTransactions => _filteredTransactions;

  // Fetches all transactions and their associated categories and funds
  Future<void> fetchAllTransactions() async {
    _transactions = await _transactionService.getAllTransactions();

    for (var transaction in _transactions) {
      await _fetchCategoryAndFund(transaction);
    }
    notifyListeners();
  }

  // Fetches transactions for a specific ledger book
  Future<void> fetchTransactionsForLedgerBook(int ledgerBookId) async {
    _filteredTransactions = await _transactionService.getTransactionsByLedgerBookId(ledgerBookId);

    for (var transaction in _transactions) {
      await _fetchCategoryAndFund(transaction); 
    }
    notifyListeners();
  }

  // Adds a new transaction
  Future<void> addTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.addTransaction(transaction);
    await _fetchCategoryAndFund(transaction);

    if (_ledgerBookProvider != null) {
      await _ledgerBookProvider!.fetchLedgerBookDetails(transaction.ledgerBookId);
    }
    // Determine if the transaction is an income or expense
    bool isIncome = transaction.type.toLowerCase() == 'income';

    // Update the fund's balance
    await _fundProvider?.updateFundBalance(transaction.fundId, transaction.amount, isIncome);

    notifyListeners();
  }

  updateTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.updateTransaction(transaction);

    fetchTransactionsForLedgerBook(transaction.ledgerBookId);

    if (_ledgerBookProvider != null) {
      await _ledgerBookProvider!.fetchLedgerBookDetails(transaction.ledgerBookId);
    }
    // Determine if the transaction is an income or expense
    bool isIncome = transaction.type.toLowerCase() == 'income';

    // Update the fund's balance
    await _fundProvider?.updateFundBalance(transaction.fundId, transaction.amount, isIncome);
    notifyListeners();
  }

  // Deletes a transaction
  Future<void> deleteTransaction(pokemoney.Transaction transaction) async {
    await _transactionService.deleteTransaction(transaction.id!);
    fetchTransactionsForLedgerBook(transaction.ledgerBookId);
    if (_ledgerBookProvider != null) {
      await _ledgerBookProvider!.fetchLedgerBookDetails(transaction.ledgerBookId);
    }

    // Determine if the transaction is an income or expense
    bool isIncome = transaction.type.toLowerCase() == 'income';

    // Update the fund's balance
    await _fundProvider?.updateFundBalance(transaction.fundId, transaction.amount, !isIncome);
    notifyListeners();
  }

  Future<void> getTotalBalanceForLedgerBook(int ledgerBookId) async {
    await _transactionService.getTotalBalanceForLedgerBook(ledgerBookId);
  }

  // Fetches and caches category and fund data for a given transaction
  Future<void> _fetchCategoryAndFund(pokemoney.Transaction transaction) async {
    if (!_categoryCache.containsKey(transaction.categoryId)) {
      try {
        pokemoney.Category category = await _categoryService.getCategoryById(transaction.categoryId);
        _categoryCache[transaction.categoryId] = category;
      } catch (e) {
        print('Error fetching category: $e');
      }
    }

    if (!_subCategoryCache.containsKey(transaction.subCategoryId)) {
      try {
        pokemoney.SubCategory subCategory =
            await _subCategoryService.getSubCategoryById(transaction.subCategoryId);
        _subCategoryCache[transaction.subCategoryId] = subCategory;
      } catch (e) {
        print('Error fetching category: $e');
      }
    }

    if (!_fundCache.containsKey(transaction.fundId)) {
      // Similar logic for funds
      try {
        pokemoney.Fund fund = await _fundService.getFundById(transaction.fundId);
        _fundCache[transaction.fundId] = fund;
      } catch (e) {
        print('Error fetching fund: $e');
      }
    }

    if (!_ledgerBookCache.containsKey(transaction.ledgerBookId)) {
      try {
        pokemoney.LedgerBook ledgerBook =
            await _ledgerBookService.getLedgerBookById(transaction.ledgerBookId);
        _ledgerBookCache[transaction.ledgerBookId] = ledgerBook;
      } catch (e) {
        print('Error fetching ledgerbook: $e');
      }
    }
  }

  // Helper method to get a category from the cache
  pokemoney.Category? getCategoryForTransaction(pokemoney.Transaction transaction) {
    return _categoryCache[transaction.categoryId];
  }

  // Helper method to get a subcategory from the cache
  pokemoney.SubCategory? getSubCategoryForTransaction(pokemoney.Transaction transaction) {
    return _subCategoryCache[transaction.subCategoryId];
  }

  //helper method to get a ledgerbook
  pokemoney.LedgerBook? getLedgerBookForTransaction(pokemoney.Transaction transaction) {
    return _ledgerBookCache[transaction.ledgerBookId];
  }

  // Helper method to get a fund from the cache
  pokemoney.Fund? getFundForTransaction(pokemoney.Transaction transaction) {
    return _fundCache[transaction.fundId];
  }

  // Method to calculate the total income
  double getTotalIncome() {
    return _transactions
        .where((transaction) => transaction.type.toLowerCase() == 'income')
        .fold(0, (total, transaction) => total + transaction.amount);
  }

  // Method to calculate the total expense
  double getTotalExpense() {
    return _transactions
        .where((transaction) => transaction.type.toLowerCase() == 'expense')
        .fold(0, (total, transaction) => total + transaction.amount);
  }
}
