import 'package:graphql_flutter/graphql_flutter.dart';

class GraphQLClientService {
  late GraphQLClient _client;

  GraphQLClientService() {
    // Initialize the GraphQL client
    _client = _initClient();
  }

  GraphQLClient _initClient() {
    // Configuration for the GraphQL client
    // Replace with your GraphQL endpoint and any necessary headers
    final HttpLink httpLink = HttpLink('YOUR_GRAPHQL_ENDPOINT');

    // If you need to add headers for authentication, you can use Link
    // from 'package:graphql_flutter/graphql_flutter.dart'
    // final Link link = httpLink; // Add any necessary middleware

    return GraphQLClient(
      cache: GraphQLCache(store: InMemoryStore()),
      link: httpLink, // Use the link with headers if necessary
    );
  }

  Future<Map<String, dynamic>> syncAll(Map<String, dynamic> unsyncedData) async {
    // Define the mutation string, replace with your actual mutation
    const String syncAllMutation = '''
      mutation SyncAllData(\$userData: SyncUserInput!, \$fundData: [SyncFundInput]!, \$ledgerData: [SyncLedgerInput]!, \$transactionData: [SyncTransactionInput]!, \$subcategoryData: [SyncSubcategoryInput]!) {
        syncAll(
          maxOperationId: 1,
          user: \$userData,
          fund: \$fundData,
          ledger: \$ledgerData,
          transaction: \$transactionData,
          subcategory: \$subcategoryData
        ) {
    user {
      userId
      email
      name
    }
    funds {
      fundId
      name
      balance
    }
    ledgers {
      ledgerId
      name
      budget
    }
    transactions {
      transactionId
      money
      typeId
    }
    categories {
      categoryId
      categoryName
    }
    subcategories {
      subcategoryId
      subcategoryName
    }
    notifications {
      ledgerInvitation {
        id
        invitedBy {
          userId
        }
      }
      fundInvitation {
        id
        invitedBy {
          userId
        }
      }
    }
    operationId
  }
      }
    ''';

    // Extracting individual data categories from unsyncedData
    var userData = unsyncedData['users'];
    var fundData = unsyncedData['funds'];
    var ledgerData = unsyncedData['ledgerBooks'];
    var transactionData = unsyncedData['transactions'];
    var subcategoryData = unsyncedData['subcategories'];

    // Execute the mutation
    final MutationOptions options = MutationOptions(
      document: gql(syncAllMutation),
      variables: {
        'userData': userData,
        'fundData': fundData,
        'ledgerData': ledgerData,
        'transactionData': transactionData,
        'subcategoryData': subcategoryData,
      },
    );

    final QueryResult result = await _client.mutate(options);

    if (result.hasException) {
      // Handle errors appropriately
      throw result.exception!;
    }

    return result.data!;
  }
}
