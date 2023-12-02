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
    await db.transaction((txn) async {
      final List<Category> initialCategories = [
        Category(id: 1, name: 'Restaurant', iconPath: 'assets/category_icons/restaurant_icon.svg'),
        Category(id: 2, name: 'Transportation', iconPath: 'assets/category_icons/transportation_icon.svg'),
        Category(id: 3, name: 'Rent', iconPath: 'assets/category_icons/rent_icon.svg'),
        Category(id: 4, name: 'Grocery', iconPath: 'assets/category_icons/groceries_icon.svg'),
        Category(id: 5, name: 'Shopping', iconPath: 'assets/category_icons/shopping_icon.svg'),
        Category(id: 6, name: 'Intertainment', iconPath: 'assets/category_icons/intertainment_icon.svg'),
        Category(id: 7, name: 'Saving', iconPath: 'assets/category_icons/saving_icon.svg'),
        Category(id: 8, name: 'Other', iconPath: 'assets/category_icons/other_icon.svg'),
        Category(id: 9, name: 'Job', iconPath: 'assets/category_icons/job_icon.svg'),
      ];

      final List<SubCategory> initialSubCategories = [
        SubCategory(
            categoryId: 1,
            name: 'Restaurant',
            iconPath: 'assets/category_icons/restaurant_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 2,
            name: 'Transportation',
            iconPath: 'assets/category_icons/transportation_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 3,
            name: 'Rent',
            iconPath: 'assets/category_icons/rent_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 4,
            name: 'Grocery',
            iconPath: 'assets/category_icons/groceries_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 5,
            name: 'Shopping',
            iconPath: 'assets/category_icons/shopping_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 6,
            name: 'Intertainment',
            iconPath: 'assets/category_icons/intertainment_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 7,
            name: 'Saving',
            iconPath: 'assets/category_icons/saving_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 8,
            name: 'Other',
            iconPath: 'assets/category_icons/other_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
        SubCategory(
            categoryId: 9,
            name: 'Job',
            iconPath: 'assets/category_icons/job_icon.svg',
            updateAt: DateTime.now(),
            delFlag: 0),
      ];

      try {
        for (var category in initialCategories) {
          var result = await txn.query(
            't_categories_unsync',
            where: 'name = ?',
            whereArgs: [category.name],
          );

          if (result.isEmpty) {
            await txn.insert(
              't_categories_unsync',
              category.toMap(),
              conflictAlgorithm: ConflictAlgorithm.ignore,
            );
          }
        }

        for (var subCategory in initialSubCategories) {
          var result = await txn.query(
            't_subcategories_unsync',
            where: 'name = ? AND categoryId = ?',
            whereArgs: [subCategory.name, subCategory.categoryId],
          );

          if (result.isEmpty) {
            await txn.insert(
              't_subcategories_unsync',
              subCategory.toMap(),
              conflictAlgorithm: ConflictAlgorithm.ignore,
            );
          }
        }
      } catch (e) {
        print('Error inserting initial categories and subcategories: $e');
        // Optionally, rethrow the error if you want to handle it further up the call stack
        // throw e;
      }
    });
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
    // Create the 't_users' table // TODO : change it to be editors
    await db.execute('''
      CREATE TABLE t_users_unsync(
        id INTEGER PRIMARY KEY, 
        userName TEXT NOT NULL,
        email TEXT NOT NULL,
        pictureUrl TEXT, 
        headerPicture TEXT)
    ''');

    // Create the 't_ledger_books' table
    await db.execute('''
      CREATE TABLE t_ledger_books_unsync(
        id INTEGER PRIMARY KEY,
        title TEXT NOT NULL,
        budget REAL DEFAULT 1000000,
        owner INTEGER NOT NULL,
        editors TEXT NOT NULL,
        createAt TEXT NOT NULL,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        FOREIGN KEY (owner) REFERENCES t_users_unsync(id))
    ''');

    // Create the 't_categories' table
    await db.execute('''
      CREATE TABLE t_categories_unsync(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        iconPath TEXT)
    ''');

    await db.execute('''
      CREATE TABLE t_subcategories_unsync(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        categoryId INTEGER,
        name TEXT,
        iconPath TEXT default null,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        foreign key (categoryId) references t_categories_unsync(id)
        )
    ''');
    // Create the 't_funds' table
    await db.execute('''
      CREATE TABLE t_funds_unsync(
        id INTEGER PRIMARY KEY, 
        name TEXT NOT NULL, 
        balance REAL NOT NULL, 
        creationDate TEXT NOT NULL, 
        owner INTEGER NOT NULL,
        editors TEXT NOT NULL,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        FOREIGN KEY (owner) REFERENCES t_users_unsync(id))
    ''');

    // Create the 't_transactions' table
    // TODO: add updatedBy
    await db.execute('''
      CREATE TABLE t_transactions_unsync(
        id INTEGER PRIMARY KEY, 
        ledgerBookId INTEGER NOT NULL, 
        categoryId INTEGER NOT NULL,
        subCategoryId INTEGER, 
        fundId INTEGER NOT NULL, 
        billingDate TEXT NOT NULL, 
        invoiceNumber TEXT NOT NULL, 
        amount REAL NOT NULL, 
        type TEXT NOT NULL, 
        updatedBy INTEGER NOT NULL,
        relevantEntity TEXT DEFAULT NULL, 
        comment TEXT DEFAULT NULL, 
        FOREIGN KEY (fundId) REFERENCES t_funds_unsync(id),
        FOREIGN KEY (ledgerBookId) REFERENCES t_ledger_books_unsync(id),
        FOREIGN KEY (categoryId) REFERENCES t_categories_unsync(id),
        FOREIGN KEY (subCategoryId) REFERENCES t_subcategories_unsync(id)
        )
    ''');

    await db.execute('''
      CREATE TABLE t_users_sync(
        id INTEGER PRIMARY KEY, 
        userName TEXT NOT NULL,
        email TEXT NOT NULL,
        pictureUrl TEXT, 
        headerPicture TEXT)
    ''');

    // Create the 't_ledger_books' table
    await db.execute('''
      CREATE TABLE t_ledger_books_sync(
        id INTEGER PRIMARY KEY,
        title TEXT NOT NULL,
        budget REAL DEFAULT 1000000,
        owner INTEGER NOT NULL,
        editors TEXT NOT NULL,
        createAt TEXT NOT NULL,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        FOREIGN KEY (owner) REFERENCES t_users_sync(id))
    ''');

    // Create the 't_categories' table
    await db.execute('''
      CREATE TABLE t_categories_sync(
        id INTEGER PRIMARY KEY,
        name TEXT,
        iconPath TEXT)
    ''');

    await db.execute('''
      CREATE TABLE t_subcategories_sync(
        id INTEGER PRIMARY KEY,
        categoryId INTEGER,
        name TEXT,
        iconPath TEXT default null,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        foreign key (categoryId) references t_categories_sync(id)
        )
    ''');

    // Create the 't_funds' table
    await db.execute('''
      CREATE TABLE t_funds_sync(
        id INTEGER PRIMARY KEY, 
        name TEXT NOT NULL, 
        balance REAL NOT NULL, 
        creationDate TEXT NOT NULL, 
        owner INTEGER NOT NULL,
        editors TEXT NOT NULL,
        updateAt TEXT NOT NULL,
        delFlag INTEGER NOT NULL,
        FOREIGN KEY (owner) REFERENCES t_users_sync(id))
    ''');

    // Create the 't_transactions' table
    await db.execute('''
      CREATE TABLE t_transactions_sync(
        id INTEGER PRIMARY KEY, 
        ledgerBookId INTEGER NOT NULL, 
        categoryId INTEGER NOT NULL,
        subCategoryId INTEGEr, 
        fundId INTEGER NOT NULL, 
        billingDate TEXT NOT NULL, 
        invoiceNumber TEXT NOT NULL, 
        amount REAL NOT NULL, 
        type TEXT NOT NULL, 
        relevantEntity TEXT DEFAULT NULL, 
        comment TEXT DEFAULT NULL, 
        FOREIGN KEY (fundId) REFERENCES t_funds_sync(id),
        FOREIGN KEY (ledgerBookId) REFERENCES t_ledger_books_sync(id),
        FOREIGN KEY (categoryId) REFERENCES t_categories_sync(id),
        FOREIGN KEY (subCategoryId) REFERENCES t_subcategories_sync(id))
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
