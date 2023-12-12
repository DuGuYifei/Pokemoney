class Transaction {
  final int? id;
  final int ledgerBookId;
  final int categoryId;
  final int subCategoryId;
  final int fundId;
  final String invoiceNumber;
  final DateTime billingDate;
  final double amount;
  final String type;
  final String? relevantEntity; // New nullable field
  final String? comment; // New nullable field
  final int? updatedBy;
  final int? delFlag; // New field

  Transaction({
    this.id,
    required this.ledgerBookId,
    required this.categoryId,
    required this.subCategoryId,
    required this.fundId,
    required this.invoiceNumber,
    required this.billingDate,
    required this.amount,
    required this.type,
    required this.updatedBy,
    this.relevantEntity,
    this.comment,
    this.delFlag = 0, // Default value or required
  });

  Transaction copyWith({
    int? id,
    int? ledgerBookId,
    int? categoryId,
    int? subCategoryId,
    int? fundId, // Include new field
    String? invoiceNumber,
    DateTime? billingDate,
    double? amount,
    String? type,
    int? updatedBy,
    String? relevantEntity, // Include new nullable field
    String? comment, // Include new nullable field
    int? delFlag, // Include new field
  }) {
    return Transaction(
      id: id ?? this.id,
      ledgerBookId: ledgerBookId ?? this.ledgerBookId,
      categoryId: categoryId ?? this.categoryId,
      subCategoryId: subCategoryId ?? this.subCategoryId,
      fundId: fundId ?? this.fundId, // Include new field
      invoiceNumber: invoiceNumber ?? this.invoiceNumber,
      billingDate: billingDate ?? this.billingDate,
      amount: amount ?? this.amount,
      type: type ?? this.type,
      updatedBy: updatedBy ?? this.updatedBy,
      relevantEntity: relevantEntity ?? this.relevantEntity, // Include new nullable field
      comment: comment ?? this.comment, // Include new nullable field
      delFlag: delFlag ?? this.delFlag, // Include new field
    );
  }

  factory Transaction.fromMap(Map<String, dynamic> map) {
    print('Mapping Transaction from: $map');

    if (map['ledgerBookId'] == null ||
        map['categoryId'] == null ||
        map['fundId'] == null ||
        map['subCategoryId'] == null) {
      throw ArgumentError('An essential integer field is null: $map');
    }

    return Transaction(
      id: map['id'] as int?,
      ledgerBookId: map['ledgerBookId'] as int,
      categoryId: map['categoryId'] as int,
      subCategoryId: map['subCategoryId'] as int,
      fundId: map['fundId'] as int,
      invoiceNumber: map['invoiceNumber'] as String,
      billingDate: DateTime.parse(map['billingDate'] as String),
      amount: (map['amount'] as num?)?.toDouble() ?? 0.0,
      type: map['type'] as String,
      updatedBy: map['updatedBy'] as int?,
      relevantEntity: map['relevantEntity'] as String?,
      comment: map['comment'] as String?,
      delFlag: map['delFlag'] as int?,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'ledgerBookId': ledgerBookId,
      'categoryId': categoryId,
      'subCategoryId': subCategoryId,
      'fundId': fundId ?? 0, // Convert fundId to int and provide default value
      'invoiceNumber': invoiceNumber,
      'billingDate': billingDate.toIso8601String(),
      'amount': amount,
      'type': type,
      'updatedBy': updatedBy,
      'relevantEntity': relevantEntity,
      'comment': comment,
      'delFlag': delFlag,
    };
  }

  //create for me a fromJson method
  static Transaction fromJson(Map<String, dynamic> json) {
    return Transaction(
      id: json['id'],
      ledgerBookId: json['ledgerBookId'],
      categoryId: json['categoryId'],
      subCategoryId: json['subCategoryId'],
      fundId: json['fundId'],
      invoiceNumber: json['invoiceNumber'],
      billingDate: DateTime.parse(json['billingDate']),
      amount: json['amount'],
      type: json['type'],
      updatedBy: json['updatedBy'],
      relevantEntity: json['relevantEntity'],
      comment: json['comment'],
      delFlag: json['delFlag'],
    );
  }
}
