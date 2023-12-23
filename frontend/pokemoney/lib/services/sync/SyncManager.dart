import 'package:flutter/material.dart';
import 'package:pokemoney/services/sync/GraphQLClientService.dart';
import 'package:pokemoney/services/sync/SyncResponseParser.dart'; // Parser for GraphQL responses
import 'package:pokemoney/services/DatabaseService.dart';
import 'package:pokemoney/constants/ApiConstants.dart';

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

      await dbService.clearUnsyncedData();

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
    Map<String, dynamic> unsyncedUsers = await dbService.getUserInfo();
    List<Map<String, dynamic>> unsyncedFunds = await dbService.getUnsyncedFunds();
    List<Map<String, dynamic>> unsyncedLedgerBooks = await dbService.getUnsyncedLedgerBooks();
    List<Map<String, dynamic>> unsyncedTransactions = await dbService.getUnsyncedTransactions();
    List<Map<String, dynamic>> unsyncedCategories = await dbService.getUnsyncedCategories();
    List<Map<String, dynamic>> unsyncedSubCategories = await dbService.getUnsyncedSubCategories();

// Create a new list to hold modified maps
    List<Map<String, dynamic>> modifiedUnsyncedFunds = [];
    List<Map<String, dynamic>> modifiedUnsyncedLedgerBooks = [];
    List<Map<String, dynamic>> modifiedUnsyncedTransactions = [];
    List<Map<String, dynamic>> modifiedUnsyncedSubcategories = [];

    // Iterate over unsyncedUsers and create a modified copy of each map
    // for (var map in unsyncedUsers) {
    //   Map<String, dynamic> modifiedMap = Map.from(map);

    //   // Rename 'id' to 'userId' and 'userName' to 'name'
    //   modifiedMap['userId'] = modifiedMap.remove('id')?.toString();
    //   modifiedMap['name'] = modifiedMap.remove('userName');

    //   // Ensure updateAt is set to a non-null value
    //   modifiedMap['updateAt'] = '0';

    //   // Add the modified map to the new list
    //   modifiedUnsyncedUsers.add(modifiedMap);
    // }

    // Iterate over unsyncedFunds and create a modified copy of each map
    for (var map in unsyncedFunds) {
      Map<String, dynamic> modifiedMap = Map.from(map);

      // Rename 'id' to 'userId' and 'userName' to 'name'
      modifiedMap['fundId'] = modifiedMap.remove('id')?.toString();
      modifiedMap['createAt'] = modifiedMap.remove('creationDate');
      modifiedMap['owner'] = modifiedMap.remove('owner')?.toString();
      modifiedMap.remove('editors');

      if (modifiedMap.containsKey('createAt') && modifiedMap['createAt'] is String) {
        // Parse the ISO 8601 string to DateTime
        DateTime createAt = DateTime.parse(modifiedMap['createAt']);
        DateTime updateAt = DateTime.parse(modifiedMap['updateAt']);

        int timestampCreateAt = createAt.millisecondsSinceEpoch;
        int timestampUpdateAt = updateAt.millisecondsSinceEpoch;

        // Convert DateTime to Unix timestamp (in seconds)
        modifiedMap['createAt'] = timestampCreateAt.toString();
        modifiedMap['updateAt'] = timestampUpdateAt.toString();
      } else {
        // Handle the case where billingDate is missing or not a string
        modifiedMap['createAt'] = "0"; // or some default value
        modifiedMap['updateAt'] = "0"; // or some default value
      }

      // Add the modified map to the new list
      modifiedUnsyncedFunds.add(modifiedMap);
    }

    // Iterate over unsyncedLedgerBooks and create a modified copy of each map
    for (var map in unsyncedLedgerBooks) {
      Map<String, dynamic> modifiedMap = Map.from(map);

      // Rename 'id' to 'ledgerId'
      modifiedMap['ledgerId'] = modifiedMap.remove('id')?.toString();
      modifiedMap['owner'] = modifiedMap.remove('owner')?.toString();
      modifiedMap['name'] = modifiedMap.remove('title');
      modifiedMap.remove('editors');

      if (modifiedMap.containsKey('createAt') && modifiedMap['createAt'] is String) {
        // Parse the ISO 8601 string to DateTime
        DateTime createAt = DateTime.parse(modifiedMap['createAt']);
        DateTime updateAt = DateTime.parse(modifiedMap['updateAt']);

        int timestampCreateAt = createAt.millisecondsSinceEpoch;
        int timestampUpdateAt = updateAt.millisecondsSinceEpoch;

        // Convert DateTime to Unix timestamp (in seconds)
        modifiedMap['createAt'] = timestampCreateAt.toString();
        modifiedMap['updateAt'] = timestampUpdateAt.toString();
      } else {
        // Handle the case where billingDate is missing or not a string
        modifiedMap['createAt'] = "0"; // or some default value
        modifiedMap['updateAt'] = "0"; // or some default value
      }
      // Add the modified map to the new list
      modifiedUnsyncedLedgerBooks.add(modifiedMap);
    }

    // Iterate over unsyncedTransactions and create a modified copy of each map
    for (var map in unsyncedTransactions) {
      Map<String, dynamic> modifiedMap = Map.from(map);

      // Rename 'id' to 'ledgerId'
      modifiedMap['transactionId'] = modifiedMap.remove('id')?.toString();
      modifiedMap['ledgerId'] = modifiedMap.remove('ledgerBookId')?.toString();
      modifiedMap['money'] = modifiedMap.remove('amount');
      modifiedMap['subcategoryId'] = modifiedMap.remove('subCategoryId')?.toString();
      modifiedMap['fundId'] = modifiedMap.remove('fundId')?.toString();
      // modifiedMap['happenAt'] =
      //     DateTime.parse(modifiedMap.remove("billDate")).millisecondsSinceEpoch.toString();
      modifiedMap['updateAt'] = modifiedMap
          .remove('updatedBy')
          .toString(); // TODO: to change the updateBy to a date in the model and the application and the database

      modifiedMap.remove(
          'relevantEntity'); // TODO: user need to add the description of any releted entity, this will come from the transaction form

      // Check the value of 'type' and convert it to an integer for 'typeId'

      modifiedMap['typeId'] = typeStringToTypeID[modifiedMap['type']];
      modifiedMap['relevantEntity'] = modifiedMap['type'];

      if (modifiedMap.containsKey('billingDate') && modifiedMap['billingDate'] is String) {
        // Parse the ISO 8601 string to DateTime
        DateTime billingDate = DateTime.parse(modifiedMap['billingDate']);
        int timestamp = billingDate.millisecondsSinceEpoch;

        // Convert DateTime to Unix timestamp (in seconds)
        modifiedMap['happenAt'] = timestamp.toString();
      } else {
        // Handle the case where billingDate is missing or not a string
        modifiedMap['happenAt'] = "0"; // or some default value
      }

      modifiedMap.remove('billingDate');
      // Remove the original 'type' key
      modifiedMap.remove('type');
      modifiedMap.remove('invoiceNumber');

      // Add the modified map to the new list
      modifiedUnsyncedTransactions.add(modifiedMap);
    }

    // Iterate over unsyncedCategories and create a modified copy of each map
    for (var map in unsyncedSubCategories) {
      Map<String, dynamic> modifiedMap = Map.from(map);

      // Rename 'id' to 'categoryId'
      modifiedMap['subcategoryId'] = modifiedMap.remove('id')?.toString();
      modifiedMap['categoryId'] = modifiedMap.remove('categoryId')?.toString();
      modifiedMap['subcategoryName'] = modifiedMap.remove('name');

      if (modifiedMap.containsKey('updateAt') && modifiedMap['updateAt'] is String) {
        // Parse the ISO 8601 string to DateTime
        DateTime updateAt = DateTime.parse(modifiedMap['updateAt']);

        int timestamp = updateAt.millisecondsSinceEpoch;

        // Convert DateTime to Unix timestamp (in seconds)
        modifiedMap['updateAt'] = timestamp.toString();
      } else {
        // Handle the case where billingDate is missing or not a string
        modifiedMap['updateAt'] = "0"; // or some default value
      }

      modifiedMap.remove('iconPath');
      // Add the modified map to the new list
      modifiedUnsyncedSubcategories.add(modifiedMap);
    }

    // Prepare the data in the structure expected by your GraphQL backend
    Map<String, dynamic> unsyncedData = {
      'maxOperationId': 1,
      'users': unsyncedUsers,
      'funds': modifiedUnsyncedFunds,
      'ledgerBooks': modifiedUnsyncedLedgerBooks,
      'transactions': modifiedUnsyncedTransactions,
      'categories': unsyncedCategories,
      'subcategories': modifiedUnsyncedSubcategories,
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

    await dbService.updateSyncUsers(parsedResponse.user.toMap());

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
    // for (var category in parsedResponse.categories) {
    //   await dbService.updateSyncCategories(category.toMap());
    // }

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
