import 'package:flutter/foundation.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionService.dart';
import 'package:pokemoney/model/barrel.dart';

class TransactionProvider with ChangeNotifier {
  final TransactionService _transactionService = TransactionService();
  List<Transaction> _transactions = [];

  List<Transaction> get transactions => _transactions;

  fetchAllTransactions() async {
    _transactions = await _transactionService.getAllTransactions();
    notifyListeners();
  }

  addTransaction(Transaction transactions) async {
    await _transactionService.addTransaction(transactions);
    fetchAllTransactions();
    print("Transaction added");
  }

  deleteTransaction(int id) async {
    await _transactionService.deleteTransaction(id);
    fetchAllTransactions();
  }
}
