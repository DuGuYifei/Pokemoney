import 'package:pokemoney/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DatabaseService {
  final DBHelper _dbHelper = DBHelper();

  // Methods for interacting with the unsync tables
  Future<List<Map<String, dynamic>>> getUnsyncedUsers() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_users_unsync', columns: ['id', 'username', 'email']);
  }

// Method that will get the information of the user
  Future<Map<String, dynamic>> getUserInfo() async {
    final prefs = await SharedPreferences.getInstance();

    //fetch user info from shared preferences

    int? id = prefs.getInt('id');
    String? username = prefs.getString('username');
    String? email = prefs.getString('email');

    //create a map to store the user info
    Map<String, dynamic> userData = {};

    //check if the user info is not null
    if (username != null && email != null && id != null) {
      userData = {
        'userId': id.toString(),
        'name': username,
        'email': email,
        'updateAt': "0" // TODO: Update this value
      };
    }

    return userData;
  }

//Methods for interacting with the unsync tables for ledgerbook
  Future<List<Map<String, dynamic>>> getUnsyncedLedgerBooks() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_ledger_books_unsync');
  }

//Methods for interacting with the unsync tables for fund
  Future<List<Map<String, dynamic>>> getUnsyncedFunds() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_funds_unsync');
  }

//Methods for interacting with the unsync tables for transaction
  Future<List<Map<String, dynamic>>> getUnsyncedTransactions() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_transactions_unsync');
  }

//Methods for interacting with the unsync tables for category
  Future<List<Map<String, dynamic>>> getUnsyncedCategories() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_categories_unsync');
  }

//Methods for interacting with the unsync tables for subcategory
  Future<List<Map<String, dynamic>>> getUnsyncedSubCategories() async {
    final dbClient = await _dbHelper.db;
    return dbClient.query('t_subcategories_unsync');
  }

  Future<void> clearUnsyncedData() async {
    final db = await _dbHelper.db;
    // Implement logic to clear unsynced tables
  }

  // Methods for interacting with the sync tables
  Future<void> updateSyncData(/* Parameters */) async {
    final db = await _dbHelper.db;
    // Implement logic to update sync tables with new data
  }

  // Methods for interacting with the sync tables for users
  Future<void> updateSyncUsers(Map<String, dynamic> user) async {
    final db = await _dbHelper.db;
    await db.insert('t_users_sync', user, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Methods for interacting with the sync tables for ledgerbook
  Future<void> updateSyncLedgerBooks(Map<String, dynamic> ledgerBook) async {
    final db = await _dbHelper.db;
    await db.insert('t_ledger_books_sync', ledgerBook, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Methods for interacting with the sync tables for fund
  Future<void> updateSyncFunds(Map<String, dynamic> fund) async {
    final db = await _dbHelper.db;
    await db.insert('t_funds_sync', fund, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Methods for interacting with the sync tables for transaction
  Future<void> updateSyncTransactions(Map<String, dynamic> transaction) async {
    final db = await _dbHelper.db;
    await db.insert('t_transactions_sync', transaction, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Methods for interacting with the sync tables for category
  Future<void> updateSyncCategories(Map<String, dynamic> category) async {
    final db = await _dbHelper.db;
    await db.insert('t_categories_sync', category, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Methods for interacting with the sync tables for subcategory
  Future<void> updateSyncSubCategories(Map<String, dynamic> subcategory) async {
    final db = await _dbHelper.db;
    await db.insert('t_subcategories_sync', subcategory, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // Additional utility methods as needed, e.g., insert, delete, update, etc.
}
