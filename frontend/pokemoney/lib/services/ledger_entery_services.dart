import 'LedgerEntry.dart';
import 'database_helper.dart';

class LedgerEntryService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addLedgerEntry(LedgerEntry entry) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("LedgerEntry", entry.toMap());
  
    return res;
  }

  Future<List<LedgerEntry>> getAllLedgerEntries() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("LedgerEntry");
    return result.map((map) => LedgerEntry.fromMap(map)).toList();
  }

  Future<int> updateLedgerEntry(LedgerEntry entry) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update("LedgerEntry", entry.toMap(),
        where: "id = ?", whereArgs: [entry.id]);
  }

  Future<int> deleteLedgerEntry(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("LedgerEntry", where: "id = ?", whereArgs: [id]);
  }
}
