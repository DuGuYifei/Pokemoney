import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import 'package:pokemoney/model/barrel.dart';

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

  Future<void> insertInitialCategories(Database db) async {
    final List<Category> initialCategories = [
      Category(name: 'Restaurant', iconPath: 'assets/category_icons/restaurant_icon.svg'),
      Category(name: 'Transportation', iconPath: 'assets/category_icons/transportation_icon.svg'),
      Category(name: 'Rent', iconPath: 'assets/category_icons/rent_icon.svg'),
      Category(name: 'Grocery', iconPath: 'assets/category_icons/groceries_icon.svg'),
      Category(name: 'Shopping', iconPath: 'assets/category_icons/shopping_icon.svg'),
      Category(name: 'Intertainment', iconPath: 'assets/category_icons/intertainment_icon.svg'),
      Category(name: 'Saving', iconPath: 'assets/category_icons/saving_icon.svg'),
      Category(name: 'Other', iconPath: 'assets/category_icons/other_icon.svg'),
    ];

    for (var category in initialCategories) {
      await db.insert(
        'categories',
        category.toMap(),
        conflictAlgorithm: ConflictAlgorithm.ignore,
      );
    }
  }

  initDb() async {
    String path = join(await getDatabasesPath(), 'pokemonay.db');
    var theDb = await openDatabase(path, version: 1, onCreate: _onCreate);
    insertInitialCategories(theDb);
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
    categoryId INTEGER NOT NULL, 
    FOREIGN KEY (ledgerBookId) REFERENCES ledger_books(id))
    FOREIGN KEY (categoryId) REFERENCES categories(id))
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

// Creating the 'categories' table
    await db.execute('''
  CREATE TABLE categories(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    iconPath TEXT)
''');
  }
}
