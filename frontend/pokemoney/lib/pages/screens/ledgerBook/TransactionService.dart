import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class TransactionService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("transactions", transaction.toMap());
    return res;
  }

  Future<List<pokemoney.Transaction>> getAllTransactions() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("transactions");
    return result.map((map) => pokemoney.Transaction.fromMap(map)).toList();
  }

  Future<int> updateTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update(
      "transactions",
      transaction.toMap(),
      where: "id = ?",
      whereArgs: [transaction.id],
    );
  }

  Future<int> deleteTransaction(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("transactions", where: "id = ?", whereArgs: [id]);
  }

  Future<pokemoney.Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      "categories",
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
}
