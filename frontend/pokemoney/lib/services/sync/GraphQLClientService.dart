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
    final HttpLink httpLink = HttpLink('http://43.131.33.18/api/v1/hadoop/client/graphql');

    // If you need to add headers for authentication, you can use Link
    // from 'package:graphql_flutter/graphql_flutter.dart'
    // final Link link = httpLink; // Add any necessary middleware

    return GraphQLClient(
      cache: GraphQLCache(store: InMemoryStore()),
      link: httpLink, // Use the link with headers if necessary
    );
  }

  Future<Map<String, dynamic>> syncAll(Map<String, dynamic> unsyncedData) async {
    // Define the mutation string with variable definitions
    const String syncAllMutation = '''
    mutation SyncAllData(
      \$maxOperationId: ID!,
      \$userData: SyncUserInput!,
      \$fundData: [SyncFundInput]!,
      \$ledgerData: [SyncLedgerInput]!,
      \$transactionData: [SyncTransactionInput]!,
      \$subcategoryData: [SyncSubcategoryInput]!
    ) {
      syncAll(
        maxOperationId: \$maxOperationId,
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
      fundInfo {
        fundIds
        delFundIds
      }
      ledgerInfo {
        ledgerIds
        delLedgerIds
      }
      appInfo {
        categories {
          categoryId
          categoryName
          delFlag
        }
        subCategories {
          subcategoryId
          categoryId
          subcategoryName
          updateAt
          delFlag
        }
      }
    }
    funds {
      fundId
      name
      balance
      owner
      editors {
        userId
        email
        name
      }
      createAt
      updateAt
      delFlag
    }
    ledgers {
      ledgerId
      name
      budget
      owner
      editors {
        userId
        email
        name
      }
      createAt
      updateAt
      delFlag
    }
    transactions {
      transactionId
      money
      typeId
      relevantEntity
      comment
      fundId
      categoryId
      subcategoryId
      ledgerId
      happenAt
      updateAt
      delFlag
    }
    categories {
      categoryId
      categoryName
      delFlag
    }
    subcategories {
      subcategoryId
      categoryId
      subcategoryName
      updateAt
      delFlag
    }
    notifications {
      ledgerInvitation {
      	id
        invitedBy {
          userId
          email
          name
        }
        ledgerId
      }
      fundInvitation {
        id
        invitedBy {
          userId
          email
          name
        }
        fundId
      }
    }
    operationId
  }
      }
    ''';

// Ensure you extract 'maxOperationId' from unsyncedData or define it beforehand
    var maxOperationId = unsyncedData['maxOperationId'];

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
        'maxOperationId': maxOperationId,
        'user': userData,
        'fund': fundData,
        'ledger': ledgerData,
        'transaction': transactionData,
        'subcategory': subcategoryData,
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
