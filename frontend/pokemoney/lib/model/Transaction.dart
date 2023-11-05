class Transaction {
  final int? id;
  final int ledgerBookId;
  final String invoiceNumber;
  final DateTime billingDate;
  final double amount;
  final String type;
  final String category;

  Transaction({
    this.id,
    required this.ledgerBookId,
    required this.invoiceNumber,
    required this.billingDate,
    required this.amount,
    required this.type,
    required this.category,
  });

  //get someFieldValue => null;

// Copy with method to clone the Transaction with modified fields
  Transaction copyWith({
    int? id,
    String? invoiceNumber,
    String? category,
    double? amount,
    String? type,
    DateTime? billingDate,
  }) {
    return Transaction(
      id: id ?? this.id,
      invoiceNumber: invoiceNumber ?? this.invoiceNumber,
      category: category ?? this.category,
      amount: amount ?? this.amount,
      type: type ?? this.type,
      billingDate: billingDate ?? this.billingDate,
      ledgerBookId: ledgerBookId,
      // ... other field assignments using ?? to fallback to old values ...
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
