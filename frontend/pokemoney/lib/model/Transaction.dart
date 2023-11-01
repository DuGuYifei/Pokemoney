class Transaction {
  final int? id;
  final int ledgerBookId;
  final String invoiceNumber;
  final DateTime billingDate; // Changed to DateTime
  final double amount;
  final String type;
  final String category;

  Transaction({
    this.id,
    required this.ledgerBookId,
    required this.invoiceNumber,
    required this.billingDate, // Changed to DateTime
    required this.amount,
    required this.type,
    required this.category,
  });

  // Convert a Transaction object into a Map object
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'ledgerBookId': ledgerBookId,
      'invoiceNumber': invoiceNumber,
      'billingDate': billingDate.toIso8601String(), // Store as ISO8601 String
      'amount': amount,
      'type': type,
      'category': category,
    };
  }

  // Extract a Transaction object from a Map object
  factory Transaction.fromMap(Map<String, dynamic> map) {
    return Transaction(
      id: map['id'],
      ledgerBookId: map['ledgerBookId'],
      invoiceNumber: map['invoiceNumber'],
      billingDate: DateTime.parse(map['billingDate']), // Convert back to DateTime
      amount: map['amount'],
      type: map['type'],
      category: map['category'],
    );
  }
}
