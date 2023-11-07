import 'package:pokemoney/model/barrel.dart';
class Transaction {
  final int? id;
  final int ledgerBookId;
  final String invoiceNumber;
  final DateTime billingDate;
  final double amount;
  final String type;
  final int categoryId; // Reference to a Category ID

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
      'invoiceNumber': invoiceNumber,
      'billingDate': billingDate.toIso8601String(), // Store as ISO8601 String
      'amount': amount,
      'type': type,
      'categoryId': categoryId, // Store the category ID as a foreign key
    };
  }

  // Extract a Transaction object from a Map object
  // Assuming a Category object must be provided separately after fetching from the database.
  factory Transaction.fromMap(Map<String, dynamic> map) {
    return Transaction(
      id: map['id'],
      ledgerBookId: map['ledgerBookId'],
      invoiceNumber: map['invoiceNumber'],
      billingDate: DateTime.parse(map['billingDate']), // Convert back to DateTime
      amount: map['amount'].toDouble(), // Ensure the amount is a double
      type: map['type'],
      categoryId: map['categoryId'], // Use the category ID to link with Category
    );
  }
}