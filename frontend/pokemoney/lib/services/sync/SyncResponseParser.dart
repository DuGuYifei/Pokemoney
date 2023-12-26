import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/services/EditorService.dart';

class SyncResponseParser {
  // This method parses the GraphQL sync response
  static final EditorService _editorService = EditorService();

  static SyncData parseSyncResponse(Map<String, dynamic> response) {
    // Assuming the response is a map containing data for each entity
    // Extract data for each entity and convert it into models or a format
    // that can be easily used to update your local database

    User user = _parseUsers(response['syncAll']['user']);
    List<Fund> funds = _parseFunds(response['syncAll']['funds']);
    List<LedgerBook> ledgerBooks = _parseLedgerBooks(response['syncAll']['ledgers']);
    // Continue for other entities like transactions, categories, subcategories
    List<Transaction> transactions = _parseTransactions(response['syncAll']['transactions']);
    //List<Category> categories = _parseCategories(response['syncAll']['categories']);
    List<SubCategory> subcategories = _parseSubCategories(response['syncAll']['subcategories']);

    // Assuming EditorService is accessible for handling editors
    _parseAndHandleEditors(response['syncAll']['ledgers']);

    return SyncData(user, funds, ledgerBooks, transactions, subcategories); // Other entities
  }

  // parsing method for users
  static User _parseUsers(dynamic data) {
    // Convert the data to a list of User models
    return User.fromJson(data);
  }

  // Similarly, create parsing methods for other entities
  static List<Fund> _parseFunds(dynamic data) {
    return List<Fund>.from(data.map((item) => Fund.fromJson(item)));
  }

  // Similarly, create parsing methods for other entities
  static List<LedgerBook> _parseLedgerBooks(dynamic data) {
    return List<LedgerBook>.from(data.map((item) => LedgerBook.fromJson(item)));
  }

  static List<Transaction> _parseTransactions(dynamic data) {
    return List<Transaction>.from(data.map((item) => Transaction.fromJson(item)));
  }

  // static List<Category> _parseCategories(dynamic data) {
  //   return List<Category>.from(data.map((item) => Category.fromJson(item)));
  // }

  static List<SubCategory> _parseSubCategories(dynamic data) {
    return List<SubCategory>.from(data.map((item) => SubCategory.fromJson(item)));
  }

  static void _parseAndHandleEditors(dynamic ledgerData) {
    List<Editor> editors = [];
    for (var ledger in ledgerData) {
      if (ledger['editors'] != null) {
        for (var editorData in ledger['editors']) {
          Editor editor = Editor.fromMap(editorData);

          // Use EditorService to upsert the editor
          _editorService.upsertEditor(editor);
        }
      }
    }
  }
}

// You might want to create a model to hold all the parsed data together
class SyncData {
  final User user;
  final List<Fund> funds;
  final List<LedgerBook> ledgerBooks;
  final List<Transaction> transactions;
  final List<SubCategory> subcategories;

  SyncData(this.user, this.funds, this.ledgerBooks, this.transactions, this.subcategories);
}
