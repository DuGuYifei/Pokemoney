import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class LedgerBookService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert(" t_ledger_books", ledgerBook.toMap());
    return res;
  }

  Future<List<LedgerBook>> getAllLedgerBooks() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_ledger_books");
    return result.map((map) => LedgerBook.fromMap(map)).toList();
  }

  Future<int> updateLedgerBook(LedgerBook ledgerBook) async {
    var dbClient = await _dbHelper.db;
    return await dbClient
        .update(" t_ledger_books", ledgerBook.toMap(), where: "id = ?", whereArgs: [ledgerBook.id]);
  }

  Future<int> deleteLedgerBook(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete(" t_ledger_books", where: "id = ?", whereArgs: [id]);
  }

  Future<LedgerBook> getLedgerBookById(int ledgerBookId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      " t_ledger_books",
      columns: ['id', 'budget', 'title', 'owner', 'createAt', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [ledgerBookId],
    );

    if (maps.isNotEmpty) {
      return LedgerBook.fromMap(maps.first.cast<String, dynamic>());
    }

    throw Exception('LedgerBook with id $ledgerBookId not found');
  }
  // Future<void> recalculateAndStoreBalance(int ledgerBookId) async {
  //   var dbClient = await _dbHelper.db;

  //   // Efficiently calculate the sum of all transactions for this ledger book
  //   var sumResult = await dbClient.rawQuery('''
  //     SELECT SUM(CASE WHEN type='Income' THEN amount ELSE -amount END) as total
  //     FROM transactions
  //     WHERE ledgerBookId = ?
  //   ''', [ledgerBookId]);

  //   double transactionsSum = sumResult.first["total"] ?? 0.0;

  //   // Fetch the initial balance if necessary
  //   var ledgerResult = await dbClient.query(
  //     ' t_ledger_books',
  //     columns: ['initial_balance'],
  //     where: 'id = ?',
  //     whereArgs: [ledgerBookId],
  //   );
  //   double initialBalance = ledgerResult.first["initial_balance"] ?? 0.0;

  //   // Update the ledger book's balance
  //   double newBalance = initialBalance + transactionsSum;
  //   await dbClient.update(
  //     ' t_ledger_books',
  //     {'balance': newBalance},
  //     where: 'id = ?',
  //     whereArgs: [ledgerBookId],
  //   );
  // }
}
