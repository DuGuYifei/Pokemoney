import 'package:pokemoney/model/barrel.dart';

class SyncResponseParser {
  // This method parses the GraphQL sync response
  static SyncData parseSyncResponse(Map<String, dynamic> response) {
    // Assuming the response is a map containing data for each entity
    // Extract data for each entity and convert it into models or a format
    // that can be easily used to update your local database

    List<User> users = _parseUsers(response['users']);
    List<Fund> funds = _parseFunds(response['funds']);
    List<LedgerBook> ledgerBooks = _parseLedgerBooks(response['ledgers']);
    // Continue for other entities like transactions, categories, subcategories
    List<Transaction> transactions = _parseTransactions(response['transactions']);
    List<Category> categories = _parseCategories(response['categories']);
    List<SubCategory> subcategories = _parseSubCategories(response['subcategories']);

    return SyncData(users, funds, ledgerBooks,transactions,subcategories,categories); // Other entities
  }

  // parsing method for users
  static List<User> _parseUsers(dynamic data) {
    // Convert the data to a list of User models
    return List<User>.from(data.map((item) => User.fromJson(item)));
  }

  // Similarly, create parsing methods for other entities
  static List<Fund> _parseFunds(dynamic data) {
    return List<Fund>.from(data.map((item) => Fund.fromJson(item)));
  }

  // Similarly, create parsing methods for other entities
  static List<LedgerBook> _parseLedgerBooks(dynamic data) {
    return List<LedgerBook>.from(data.map((item) => Fund.fromJson(item)));
  }

  static List<Transaction> _parseTransactions(dynamic data) {
    return List<Transaction>.from(data.map((item) => Transaction.fromJson(item)));
  }

  static List<Category> _parseCategories(dynamic data) {
    return List<Category>.from(data.map((item) => Category.fromJson(item)));
  }

  static List<SubCategory> _parseSubCategories(dynamic data) {
    return List<SubCategory>.from(data.map((item) => SubCategory.fromJson(item)));
  }
}

// You might want to create a model to hold all the parsed data together
class SyncData {
  final List<User> users;
  final List<Fund> funds;
  final List<LedgerBook> ledgerBooks;
  final List<Transaction> transactions;
  final List<Category> categories;
  final List<SubCategory> subcategories;
  // ... other entities

  SyncData(this.users, this.funds, this.ledgerBooks, this.transactions,this.subcategories, this.categories);
}
