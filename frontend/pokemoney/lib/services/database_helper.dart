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
    String path = join(await getDatabasesPath(), 'pokemonay.db');
    var theDb = await openDatabase(path, version: 1, onCreate: _onCreate);
    print('Database path: $path');
    return theDb;
  }

  void _onCreate(Database db, int version) async {
    // Creating the 'accounts' table
    await db.execute('''
  CREATE TABLE accounts(
    id INTEGER PRIMARY KEY, 
    accountName TEXT NOT NULL, 
    type TEXT NOT NULL, 
    pictureUrl TEXT, 
    headerPicture TEXT)
''');

// Creating the 'ledger_books' table
    await db.execute('''
  CREATE TABLE ledger_books(
    id INTEGER PRIMARY KEY, 
    accountId INTEGER NOT NULL, 
    title TEXT NOT NULL, 
    description TEXT, 
    balance REAL NOT NULL, 
    creationDate TEXT NOT NULL, 
    FOREIGN KEY (accountId) REFERENCES accounts(id))
''');

// Creating the 'transactions' table
    await db.execute('''
  CREATE TABLE transactions(
    id INTEGER PRIMARY KEY, 
    ledgerBookId INTEGER NOT NULL, 
    invoiceNumber TEXT NOT NULL, 
    billingDate TEXT NOT NULL, 
    amount REAL NOT NULL, 
    type TEXT NOT NULL, 
    category TEXT NOT NULL, 
    FOREIGN KEY (ledgerBookId) REFERENCES ledger_books(id))
''');

// Creating the 'funds' table
    await db.execute('''
  CREATE TABLE funds(
    id INTEGER PRIMARY KEY, 
    accountId INTEGER NOT NULL, 
    balance REAL NOT NULL, 
    name TEXT NOT NULL, 
    creationDate TEXT NOT NULL, 
    type TEXT NOT NULL, 
    FOREIGN KEY (accountId) REFERENCES accounts(id))
''');

// Creating the 'funds' table
    await db.execute('''
  CREATE TABLE categories(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    iconName TEXT)
''');
  
  }
}
