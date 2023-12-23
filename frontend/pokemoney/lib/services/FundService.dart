import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:sqflite/sqflite.dart';

class FundService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addFund(Fund fund) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert(" t_funds_unsync", fund.toMap());
    return res;
  }

  Future<List<Fund>> getAllFunds() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_funds_unsync");
    return result.map((map) => Fund.fromMap(map)).toList();
  }

  Future<List<Fund>> getAllFundsFromSyncAndUnsync() async {
    var dbClient = await _dbHelper.db;

    // Fetching from unsync table
    var unsyncedResult = await dbClient.query("t_funds_unsync");
    var unsyncedFunds = unsyncedResult.map((map) => Fund.fromMap(map)).toList();

    // Fetching from sync table
    var syncedResult = await dbClient.query("t_funds_sync");
    var syncedFunds = syncedResult.map((map) => Fund.fromMap(map)).toList();

    // Combine both lists, ensuring unique entries (based on ID or other criteria)
    var combinedFunds = {...unsyncedFunds, ...syncedFunds}.toList();

    return combinedFunds;
  }

  Future<int> deleteFund(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete(" t_funds_unsync", where: "id = ?", whereArgs: [id]);
  }

  Future<Fund> getFundById(int fundId) async {
    var dbClient = await _dbHelper.db;

    // Fetching from unsync table
    List<Map> maps = await dbClient.query(
      " t_funds_unsync",
      columns: ['id', 'name', 'balance', 'creationDate', 'owner', 'editors', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [fundId],
    );

    // Fetching from sync table
    if (maps.isEmpty) {
      maps = await dbClient.query(
        " t_funds_sync",
        columns: ['id', 'name', 'balance', 'creationDate', 'owner', 'editors', 'updateAt', 'delFlag'],
        where: 'id = ?',
        whereArgs: [fundId],
      );
    }

    if (maps.isNotEmpty) {
      return Fund.fromMap(maps.first.cast<String, dynamic>());
    }

    throw Exception('Fund with id $fundId not found');
  }

  Future<int> updateFund(Fund fund) async {
    var dbClient = await _dbHelper.db;

    // First, check if the fund exists in the unsync table
    List<Map> foundInUnsync = await dbClient.query(
      "t_funds_unsync",
      where: "id = ?",
      whereArgs: [fund.id],
    );

    if (foundInUnsync.isNotEmpty) {
      // If exists in unsync, update it directly
      return await dbClient.update(
        "t_funds_unsync",
        fund.toMap(),
        where: "id = ?",
        whereArgs: [fund.id],
      );
    } else {
      // If not found in unsync, check in sync table
      List<Map> foundInSync = await dbClient.query(
        "t_funds_sync",
        where: "id = ?",
        whereArgs: [fund.id],
      );

      if (foundInSync.isNotEmpty) {
        // If found in sync, delete from sync table
        await dbClient.delete(
          "t_funds_sync",
          where: "id = ?",
          whereArgs: [fund.id],
        );

        // Insert updated fund into unsync table
        return await dbClient.insert(
          "t_funds_unsync",
          fund.toMap(),
          conflictAlgorithm: ConflictAlgorithm.replace,
        );
      } else {
        // If not found in either table, you might want to handle this case (e.g., insert or throw error)
        // For now, let's throw an error
        throw Exception('Fund with id ${fund.id} not found in any table');
      }
    }
  }

  Future<void> updateFundBalance(int fundId, double amount, bool isIncome) async {
    var dbClient = await _dbHelper.db;
    bool isUnsync = true;
    // Fetch the current balance
    List<Map> maps = await dbClient.query(
      "t_funds_unsync",
      columns: ['balance'],
      where: 'id = ?',
      whereArgs: [fundId],
    );

    if (maps.isEmpty) {
      isUnsync = false;
      maps = await dbClient.query(
        "t_funds_sync",
        columns: ['balance'],
        where: 'id = ?',
        whereArgs: [fundId],
      );
    }

    if (maps.isNotEmpty) {
      double currentBalance = maps.first['balance'];
      double newBalance = isIncome ? currentBalance + amount : currentBalance - amount;

      // Update the balance in the database
      if (isUnsync) {
        await dbClient.update(
          "t_funds_unsync",
          {'balance': newBalance},
          where: "id = ?",
          whereArgs: [fundId],
        );
      } else {
        await dbClient.update(
          "t_funds_sync",
          {'balance': newBalance},
          where: "id = ?",
          whereArgs: [fundId],
        );
      }
    }
  }
}
