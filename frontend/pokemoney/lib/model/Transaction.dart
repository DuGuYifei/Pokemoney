class Transaction {
  final int? id;
  final int ledgerBookId;
  final int categoryId;
  final int fundId; // New field
  final String invoiceNumber;
  final DateTime billingDate;
  final double amount;
  final String type;
  final String? relevantEntity; // New nullable field
  final String? comment; // New nullable field

  Transaction({
    this.id,
    required this.ledgerBookId,
    required this.categoryId,
    required this.fundId, // Include new field
    required this.invoiceNumber,
    required this.billingDate,
    required this.amount,
    required this.type,
    this.relevantEntity, // Include new nullable field
    this.comment, // Include new nullable field
  });

  Transaction copyWith({
    int? id,
    int? ledgerBookId,
    int? categoryId,
    int? fundId, // Include new field
    String? invoiceNumber,
    DateTime? billingDate,
    double? amount,
    String? type,
    String? relevantEntity, // Include new nullable field
    String? comment, // Include new nullable field
  }) {
    return Transaction(
      id: id ?? this.id,
      ledgerBookId: ledgerBookId ?? this.ledgerBookId,
      categoryId: categoryId ?? this.categoryId,
      fundId: fundId ?? this.fundId, // Include new field
      invoiceNumber: invoiceNumber ?? this.invoiceNumber,
      billingDate: billingDate ?? this.billingDate,
      amount: amount ?? this.amount,
      type: type ?? this.type,
      relevantEntity: relevantEntity ?? this.relevantEntity, // Include new nullable field
      comment: comment ?? this.comment, // Include new nullable field
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'ledgerBookId': ledgerBookId,
      'categoryId': categoryId,
      'fundId': fundId, // Include new field
      'invoiceNumber': invoiceNumber,
      'billingDate': billingDate.toIso8601String(),
      'amount': amount,
      'type': type,
      'relevantEntity': relevantEntity, // Include new nullable field
      'comment': comment, // Include new nullable field
    };
  }

  factory Transaction.fromMap(Map<String, dynamic> map) {
    print('Mapping Transaction from: $map');

    if (map['ledgerBookId'] == null || map['categoryId'] == null || map['fundId'] == null) {
      throw ArgumentError('An essential integer field is null: $map');
    }

    return Transaction(
      id: map['id'] as int?,
      ledgerBookId: map['ledgerBookId'] as int,
      categoryId: map['categoryId'] as int,
      fundId: map['fundId'] as int, // Extract new field
      invoiceNumber: map['invoiceNumber'] as String,
      billingDate: DateTime.parse(map['billingDate'] as String),
      amount: (map['amount'] as num).toDouble(),
      type: map['type'] as String,
      relevantEntity: map['relevantEntity'] as String?, // Extract new nullable field
      comment: map['comment'] as String?, // Extract new nullable field
    );
  }
}
