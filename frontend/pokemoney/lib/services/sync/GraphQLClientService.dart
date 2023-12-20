import 'package:graphql_flutter/graphql_flutter.dart';
import 'package:pokemoney/services/SecureStorage.dart';

class GraphQLClientService {
  late GraphQLClient _client;

 GraphQLClientService._(this._client); // Private constructor

  static Future<GraphQLClientService> initialize() async {
    final storage = SecureStorage();
    String? token = await storage.getToken();

    final HttpLink httpLink = HttpLink('http://43.131.33.18/api/v1/hadoop/client/graphql');

    // Add the authentication token to the headers
    final AuthLink authLink = AuthLink(
      getToken: () => token ?? '',
    );

    final Link link = authLink.concat(httpLink);

    GraphQLClient client = GraphQLClient(
      cache: GraphQLCache(store: InMemoryStore()),
      link: link,
    );

    return GraphQLClientService._(client);
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
        funds
        delFunds
      }
      ledgerInfo {
        ledgers
        delLedgers
      }
      appInfo {
        categories {
          categoryId
          categoryName
        }
        subcategories {
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
      updateBy
      updateAt
      delFlag
    }
    categories {
      categoryId
      categoryName
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
    var maxOperationId = unsyncedData['maxOperationId'].toString();

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
