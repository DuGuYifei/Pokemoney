import 'package:pokemoney/model/barrel.dart';

class Transaction {
  final int? id;
  final int ledgerBookId;
  final int categoryId; // Reference to a Category ID
  final String invoiceNumber;
  final DateTime billingDate;
  final double amount;
  final String type;

  Transaction({
    this.id,
    required this.ledgerBookId,
    required this.invoiceNumber,
    required this.billingDate,
    required this.amount,
    required this.type,
    required this.categoryId, // Use the category ID as a foreign key
  });

  // A method to clone the Transaction with modified fields
  Transaction copyWith({
    int? id,
    int? ledgerBookId,
    String? invoiceNumber,
    DateTime? billingDate,
    double? amount,
    String? type,
    int? categoryId,
  }) {
    return Transaction(
      id: id ?? this.id,
      ledgerBookId: ledgerBookId ?? this.ledgerBookId,
      invoiceNumber: invoiceNumber ?? this.invoiceNumber,
      billingDate: billingDate ?? this.billingDate,
      amount: amount ?? this.amount,
      type: type ?? this.type,
      categoryId: categoryId ?? this.categoryId,
    );
  }

  // Convert a Transaction object into a Map object
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'ledgerBookId': ledgerBookId,
      'categoryId': categoryId, // Store the category ID as a foreign key
      'invoiceNumber': invoiceNumber,
      'billingDate': billingDate.toIso8601String(), // Store as ISO8601 String
      'amount': amount,
      'type': type,
    };
  }

  // Extract a Transaction object from a Map object
  // Assuming a Category object must be provided separately after fetching from the database.
  factory Transaction.fromMap(Map<String, dynamic> map) {
    print('Mapping Transaction from: $map'); // Add this line to see what's in the map

    if (map['ledgerBookId'] == null || map['categoryId'] == null) {
      throw ArgumentError('An essential integer field is null: $map'); // This will now show the offending map
    }

    return Transaction(
      id: map['id'] as int?, // Cast as int? because 'id' is nullable
      ledgerBookId: map['ledgerBookId'] as int, // Cast as int to ensure non-null value is passed
      categoryId: map['categoryId'] as int, // Cast as int to ensure non-null value is passed
      invoiceNumber: map['invoiceNumber'] as String,
      billingDate: DateTime.parse(map['billingDate'] as String),
      amount: (map['amount'] as num).toDouble(), // Cast as num first to cover both int and double cases
      type: map['type'] as String,
    );
  }
}
