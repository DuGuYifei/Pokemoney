import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;
import 'package:sqflite/sqflite.dart';

class TransactionService {
  final DBHelper _dbHelper = DBHelper();
  static const int idThreshold = 1 << 62; // 2^62

  Future<int> addTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_transactions_unsync", transaction.toMap());
    return res;
  }

  Future<List<pokemoney.Transaction>> getAllTransactions() async {
    var dbClient = await _dbHelper.db;
    var unsyncedResult = await dbClient.query("t_transactions_unsync");
    var syncedResult = await dbClient.query("t_transactions_sync");

    var unsyncedTransactions = unsyncedResult.map((map) => pokemoney.Transaction.fromMap(map)).toList();
    var syncedTransactions = syncedResult.map((map) => pokemoney.Transaction.fromMap(map)).toList();

    var combinedTransactions = {...unsyncedTransactions, ...syncedTransactions}.toList();

    return combinedTransactions;
  }

  // Method to get t_transactions_unsync by LedgerBookId
  Future<List<pokemoney.Transaction>> getTransactionsByLedgerBookId(int ledgerBookId) async {
    var dbClient = await _dbHelper.db;

    // Fetching from unsync table
    var unsyncedResult = await dbClient.query(
      "t_transactions_unsync",
      where: "ledgerBookId = ?",
      whereArgs: [ledgerBookId],
    );
    var unsyncedTransactions = unsyncedResult.map((map) => pokemoney.Transaction.fromMap(map)).toList();

    // Fetching from sync table
    var syncedResult = await dbClient.query(
      "t_transactions_sync",
      where: "ledgerBookId = ?",
      whereArgs: [ledgerBookId],
    );
    var syncedTransactions = syncedResult.map((map) => pokemoney.Transaction.fromMap(map)).toList();

    // Combine both lists, ensuring unique entries (based on ID or other criteria)
    var combinedTransactions = {...unsyncedTransactions, ...syncedTransactions}.toList();

    return combinedTransactions;
  }

  Future<int> updateTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;

    // Check if the transaction exists in the sync table
    List<Map> foundInSync = await dbClient.query(
      "t_transactions_sync",
      where: "id = ?",
      whereArgs: [transaction.id],
    );

    if (foundInSync.isNotEmpty) {
      // If exists in sync, delete from sync table
      await dbClient.delete(
        "t_transactions_sync",
        where: "id = ?",
        whereArgs: [transaction.id],
      );

      // Then insert updated transaction into unsync table
      return await dbClient.insert(
        "t_transactions_unsync",
        transaction.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace,
      );
    } else {
      // If not found in sync table, update directly in unsync table
      return await dbClient.update(
        "t_transactions_unsync",
        transaction.toMap(),
        where: "id = ?",
        whereArgs: [transaction.id],
      );
    }
  }

  Future<int> deleteTransaction(int id) async {
    var dbClient = await _dbHelper.db;

    // Check if the transaction exists in the sync table
    List<Map> foundInSync = await dbClient.query(
      "t_transactions_sync",
      where: "id = ?",
      whereArgs: [id],
    );

    if (foundInSync.isNotEmpty) {
      // Delete from sync table
      await dbClient.delete(
        "t_transactions_sync",
        where: "id = ?",
        whereArgs: [id],
      );

      // Insert with del_flag = 1 into unsync table
      // Cast the Map to the correct type
      Map<String, dynamic> transactionData = Map<String, dynamic>.from(foundInSync.first);
      transactionData['delFlag'] = 1;

      return await dbClient.insert(
        "t_transactions_unsync",
        transactionData,
        conflictAlgorithm: ConflictAlgorithm.replace,
      );
    } else {
      // If not found in sync table, check the id threshold
      if (id < idThreshold) {
        // If id is less than the threshold, directly delete it
        return await dbClient.delete(
          "t_transactions_unsync",
          where: "id = ?",
          whereArgs: [id],
        );
      } else {
        // If id is greater than or equal to the threshold, set del_flag = 1
        return await dbClient.update(
          "t_transactions_unsync",
          {'delFlag': 1},
          where: "id = ?",
          whereArgs: [id],
        );
      }
    }
  }

  Future<pokemoney.Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      "t_categories_unsync",
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
    var dbClient = await _dbHelper.db;

    // Query for unsync table
    var unsyncResult = await dbClient.rawQuery('''
      SELECT SUM(
          CASE 
              WHEN type IN (1, 4, 5) THEN amount 
              ELSE -amount 
          END
      ) as total
      FROM t_transactions_unsync
      WHERE ledgerBookId = ?
    ''', [ledgerBookId]);

    // Query for sync table
    var syncResult = await dbClient.rawQuery('''
      SELECT SUM(
          CASE 
              WHEN type IN (1, 4, 5) THEN amount 
              ELSE -amount 
          END
      ) as total
      FROM t_transactions_sync
      WHERE ledgerBookId = ?
    ''', [ledgerBookId]);

    // Sum the results from both tables
    num totalUnsync = unsyncResult.first['total'] as num? ?? 0.0;
    num totalSync = syncResult.first['total'] as num? ?? 0.0;

    return (totalUnsync + totalSync).toDouble();
  }
}
