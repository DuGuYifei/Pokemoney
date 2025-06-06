import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class LedgerBookService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert(" t_ledger_books_unsync", ledgerBook.toMap());
    return res;
  }

  Future<List<LedgerBook>> getAllLedgerBooks() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_ledger_books_unsync");
    return result.map((map) => LedgerBook.fromMap(map)).toList();
  }

  Future<List<LedgerBook>> getAllLedgerBooksFromSyncAndUnsync() async {
    var dbClient = await _dbHelper.db;

    // Fetching from unsync table
    var unsyncedResult = await dbClient.query("t_ledger_books_unsync");
    var unsyncedLedgerBooks = unsyncedResult.map((map) => LedgerBook.fromMap(map)).toList();

    // Fetching from sync table
    var syncedResult = await dbClient.query("t_ledger_books_sync");
    var syncedLedgerBooks = syncedResult.map((map) => LedgerBook.fromMap(map)).toList();

    // Combine both lists, ensuring unique entries (based on ID or other criteria)
    var combinedLedgerBooks = {...unsyncedLedgerBooks, ...syncedLedgerBooks}.toList();

    return combinedLedgerBooks;
  }

  Future<int> updateLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    return await dbClient
        .update(" t_ledger_books_unsync", ledgerBook.toMap(), where: "id = ?", whereArgs: [ledgerBook.id]);
  }

  Future<int> deleteLedgerBook(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete(" t_ledger_books_unsync", where: "id = ?", whereArgs: [id]);
  }

  Future<LedgerBook> getLedgerBookById(int ledgerBookId) async {
    var dbClient = await _dbHelper.db;

    // First, query the unsync table
    List<Map<String, dynamic>> maps = await dbClient.query(
      "t_ledger_books_unsync",
      columns: ['id', 'budget', 'title', 'owner', 'createAt', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [ledgerBookId],
    );

    if (maps.isNotEmpty) {
      return LedgerBook.fromMap(maps.first);
    }

    // If not found, then query the sync table
    maps = await dbClient.query(
      "t_ledger_books_sync",
      columns: ['id', 'budget', 'title', 'owner', 'createAt', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [ledgerBookId],
    );

    if (maps.isNotEmpty) {
      return LedgerBook.fromMap(maps.first);
    }

    throw Exception('LedgerBook with id $ledgerBookId not found in either table');
  }
}
