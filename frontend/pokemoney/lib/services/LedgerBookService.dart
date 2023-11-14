import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class LedgerBookService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("ledger_books", ledgerBook.toMap());
    return res;
  }

  Future<List<LedgerBook>> getAllLedgerBooks() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("ledger_books");
    return result.map((map) => LedgerBook.fromMap(map)).toList();
  }

  Future<int> updateLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update("ledger_books", ledgerBook.toMap(), where: "id = ?", whereArgs: [ledgerBook.id]);
  }

  Future<int> deleteLedgerBook(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("ledger_books", where: "id = ?", whereArgs: [id]);
  }
}
