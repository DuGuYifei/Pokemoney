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
      Category(name: 'Job', iconPath: 'assets/category_icons/job_icon.svg'),
    ];

    for (var category in initialCategories) {
      // Check if this category already exists
      var result = await db.query(
        'categories',
        where: 'name = ?',
        whereArgs: [category.name],
      );

      // If the category does not exist, insert it
      if (result.isEmpty) {
        await db.insert(
          'categories',
          category.toMap(),
          conflictAlgorithm: ConflictAlgorithm.ignore,
        );
      }
    }
  }

  initDb() async {
    String path = join(await getDatabasesPath(), 'pokemoney.db');
    var theDb = await openDatabase(
      path, version: 1, onCreate: _onCreate,
      onUpgrade: _onUpgrade, // Provide the onUpgrade method
    );
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
          initialBalance REAL NOT NULL DEFAULT 0, 
          creationDate TEXT NOT NULL,
          FOREIGN KEY (accountId) REFERENCES accounts(id))
''');

// Creating the 'categories' table
    await db.execute('''
  CREATE TABLE categories(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    iconPath TEXT)
''');

// Creating the 'transactions' table
    await db.execute('''
  CREATE TABLE transactions(
    id INTEGER PRIMARY KEY, 
    ledgerBookId INTEGER NOT NULL, 
    categoryId INTEGER NOT NULL, 
    invoiceNumber TEXT NOT NULL, 
    billingDate TEXT NOT NULL, 
    amount REAL NOT NULL, 
    type TEXT NOT NULL, 
    FOREIGN KEY (ledgerBookId) REFERENCES ledger_books(id),
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
  }

  Future<void> _onUpgrade(Database db, int oldVersion, int newVersion) async {
    if (oldVersion < 2) {
      // Create a new table without the 'balance' column
      await db.execute('''
        CREATE TABLE new_ledger_books(
          id INTEGER PRIMARY KEY,
          accountId INTEGER NOT NULL,
          title TEXT NOT NULL,
          description TEXT,
          initialBalance REAL NOT NULL DEFAULT 0, 
          creationDate TEXT NOT NULL,
          FOREIGN KEY (accountId) REFERENCES accounts(id))
      ''');

      // Copy data from the old table to the new table
      await db.execute('''
        INSERT INTO new_ledger_books(id, accountId, title, description, creationDate, initialBalance)
        SELECT id, accountId, title, description, creationDate, balance FROM ledger_books
      ''');

      // Drop the old table
      await db.execute('DROP TABLE ledger_books');

      // Rename the new table to the old table's name
      await db.execute('ALTER TABLE new_ledger_books RENAME TO ledger_books');
    }
    // Handle any other version changes if necessary
  }
}