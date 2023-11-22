import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class TransactionService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_transactions", transaction.toMap());
    return res;
  }

  Future<List<pokemoney.Transaction>> getAllTransactions() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_transactions");
    return result.map((map) => pokemoney.Transaction.fromMap(map)).toList();
  }

  // Method to get t_transactions by LedgerBookId
  Future<List<pokemoney.Transaction>> getTransactionsByLedgerBookId(int ledgerBookId) async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query(
      "t_transactions",
      where: "ledgerBookId = ?",
      whereArgs: [ledgerBookId],
    );
    return result.map((map) => pokemoney.Transaction.fromMap(map)).toList();
  }

  Future<int> updateTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update(
      "t_transactions",
      transaction.toMap(),
      where: "id = ?",
      whereArgs: [transaction.id],
    );
  }

  Future<int> deleteTransaction(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("t_transactions", where: "id = ?", whereArgs: [id]);
  }

  Future<pokemoney.Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      "t_categories",
      columns: ["id", "name", "iconPath"],
      where: "id = ?",
      whereArgs: [categoryId],
    );
    if (maps.isNotEmpty) {
      // Cast the map to Map<String, dynamic> before passing it to fromMap
      return pokemoney.Category.fromMap(maps.first.cast<String, dynamic>());
    } else {
      throw Exception('Category not found for id $categoryId');
    }
  }

  Future<double> getTotalBalanceForLedgerBook(int ledgerBookId) async {
    var dbClient = await DBHelper().db;
    var result = await dbClient.rawQuery('''
    SELECT SUM(CASE WHEN type='Income' THEN amount ELSE -amount END) as total
    FROM t_transactions
    WHERE ledgerBookId = ?
  ''', [ledgerBookId]);

    // Explicitly cast the result to 'num' and then to 'double'
    num total = result.first['total'] as num? ?? 0.0;
    return total.toDouble();
  }
}
