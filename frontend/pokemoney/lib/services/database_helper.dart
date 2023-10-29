import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class DBHelper {
  static final DBHelper _instance = DBHelper.internal();

  factory DBHelper() => _instance;

  DBHelper.internal();

  static Database? _db;

  Future<Database> get db async {
    if (_db != null) return _db!;
    _db = await initDb();
    return _db!;
  }

  initDb() async {
    String path = join(await getDatabasesPath(), 'budgeting_app.db');
    var theDb = await openDatabase(path, version: 1, onCreate: _onCreate);
    print('Database path: $path');
    return theDb;
  }

  void _onCreate(Database db, int version) async {
    await db.execute('''
      CREATE TABLE LedgerEntry(
        id INTEGER PRIMARY KEY, 
        amount REAL, 
        description TEXT, 
        category TEXT, 
        timestamp TEXT)
      ''');
  }
}
