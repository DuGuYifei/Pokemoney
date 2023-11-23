import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class FundService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addFund(Fund fund) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert(" t_funds", fund.toMap());
    return res;
  }

  Future<List<Fund>> getAllFunds() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_funds");
    return result.map((map) => Fund.fromMap(map)).toList();
  }

  Future<int> deleteFund(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete(" t_funds", where: "id = ?", whereArgs: [id]);
  }

  Future<Fund> getFundById(int fundId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      " t_funds",
      columns: ['id', 'name', 'balance', 'creationDate', 'owner', 'editors', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [fundId],
    );

    if (maps.isNotEmpty) {
      return Fund.fromMap(maps.first.cast<String, dynamic>());
    }

    throw Exception('Fund with id $fundId not found');
  }

  Future<int> updateFund(Fund fund) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update(" t_funds", fund.toMap(), where: "id = ?", whereArgs: [fund.id]);
  }

  Future<void> updateFundBalance(int fundId, double amount, bool isIncome) async {
    var dbClient = await _dbHelper.db;

    // Fetch the current balance
    List<Map> maps = await dbClient.query(
      "t_funds",
      columns: ['balance'],
      where: 'id = ?',
      whereArgs: [fundId],
    );

    if (maps.isNotEmpty) {
      double currentBalance = maps.first['balance'];
      double newBalance = isIncome ? currentBalance + amount : currentBalance - amount;

      // Update the balance in the database
      await dbClient.update(
        "t_funds",
        {'balance': newBalance},
        where: "id = ?",
        whereArgs: [fundId],
      );
    }
  }
}
