import 'package:flutter/material.dart';
import 'package:pokemoney/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseProvider with ChangeNotifier {
  Database? _db;

  DatabaseProvider() {
    _initDb();
  }

  Future<void> _initDb() async {
    _db = await DBHelper().db;
    // Perform additional setup or data loading if necessary
    print("Initlzarion of the database with provider is done");

    notifyListeners(); // Notify listeners once the database is ready
  }

}
