import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class TransactionService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addTransaction(Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("transactions", transaction.toMap());
    return res;
  }

  Future<List<Transaction>> getAllTransactions() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("transactions");
    return result.map((map) => Transaction.fromMap(map)).toList();
  }

  Future<int> updateTransaction(Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update("transactions", transaction.toMap(), where: "id = ?", whereArgs: [transaction.id]);
  }

  Future<int> deleteTransaction(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("transactions", where: "id = ?", whereArgs: [id]);
  }
}
