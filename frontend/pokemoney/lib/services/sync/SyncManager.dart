import 'package:flutter/material.dart';
import 'package:pokemoney/services/sync/GraphQLClientService.dart';
import 'package:pokemoney/services/sync/SyncResponseParser.dart'; // Parser for GraphQL responses
import 'package:pokemoney/services/DatabaseService.dart';

class SyncManager {
  final DatabaseService dbService;
  final GraphQLClientService graphqlClient;

  SyncManager(this.dbService, this.graphqlClient);

  Future<void> syncData(BuildContext context) async {
    try {
      // Show loading indicator
      _showLoadingIndicator(context);

      // Fetch unsynced data from the local database
      var unsyncedData = await _fetchUnsyncedData();

      // Send unsynced data to the backend
      var syncResponse = await graphqlClient.syncAll(unsyncedData);

      // Process the sync response
      await _processSyncResponse(syncResponse);

      // Hide loading indicator
      Navigator.of(context, rootNavigator: true).pop();

      // Show success message
      _showSuccessDialog(context);
    } catch (error) {
      // Hide loading indicator
      Navigator.of(context, rootNavigator: true).pop();

      // Show error message
      _showErrorDialog(context, error);
    }
  }

  Future<Map<String, dynamic>> _fetchUnsyncedData() async {
    final db = dbService;

    // Fetch data from each unsynced table
    List<Map<String, dynamic>> unsyncedUsers = await dbService.getUnsyncedUsers();
    List<Map<String, dynamic>> unsyncedFunds = await dbService.getUnsyncedFunds();
    List<Map<String, dynamic>> unsyncedLedgerBooks = await dbService.getUnsyncedLedgerBooks();
    List<Map<String, dynamic>> unsyncedTransactions = await dbService.getUnsyncedTransactions();
    List<Map<String, dynamic>> unsyncedCategories = await dbService.getUnsyncedCategories();
    List<Map<String, dynamic>> unsyncedSubCategories = await dbService.getUnsyncedSubCategories();

    // Prepare the data in the structure expected by your GraphQL backend
    Map<String, dynamic> unsyncedData = {
      'maxOperationId': 1,
      'users': unsyncedUsers,
      'funds': unsyncedFunds,
      'ledgerBooks': unsyncedLedgerBooks,
      'transactions': unsyncedTransactions,
      'categories': unsyncedCategories,
      'subcategories': unsyncedSubCategories,
    };

    return unsyncedData;
  }

  Future<void> _processSyncResponse(Map<String, dynamic> syncResponseData) async {
    // Implement logic to process the response from the GraphQL backend
    // and update the local database
    // For example, parsing the response and inserting data into t_users_sync, t_ledger_books_sync, etc.
    // You can use the SyncResponseParser class to parse the response
    // and the DatabaseService class to interact with the local database

    // Parse the GraphQL response using SyncResponseParser
    SyncData parsedResponse = SyncResponseParser.parseSyncResponse(syncResponseData);

    // Update users
    for (var user in parsedResponse.users) {
      await dbService.updateSyncUsers(user.toMap());
    }

    // Update ledger books
    for (var ledgerBook in parsedResponse.ledgerBooks) {
      await dbService.updateSyncLedgerBooks(ledgerBook.toMap());
    }

    // Update funds
    for (var fund in parsedResponse.funds) {
      await dbService.updateSyncFunds(fund.toMap());
    }

    // Update transactions
    for (var transaction in parsedResponse.transactions) {
      await dbService.updateSyncTransactions(transaction.toMap());
    }

    // Update categories
    for (var category in parsedResponse.categories) {
      await dbService.updateSyncCategories(category.toMap());
    }

    // Update subcategories
    for (var subcategory in parsedResponse.subcategories) {
      await dbService.updateSyncSubCategories(subcategory.toMap());
    }

  }

  void _showLoadingIndicator(BuildContext context) {
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (BuildContext context) {
        return const AlertDialog(
          content: Row(
            children: [
              CircularProgressIndicator(),
              SizedBox(width: 10),
              Text("Syncing data..."),
            ],
          ),
        );
      },
    );
  }

  void _showSuccessDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Sync Complete"),
          content: const Text("Data has been successfully synchronized."),
          actions: <Widget>[
            TextButton(
              child: const Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  void _showErrorDialog(BuildContext context, dynamic error) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Sync Failed"),
          content: Text("Error occurred during sync: $error"),
          actions: <Widget>[
            TextButton(
              child: const Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}
