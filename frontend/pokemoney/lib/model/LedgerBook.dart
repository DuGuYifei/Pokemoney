class LedgerBook {
  final int? id;
  final String title;
  final double budget; // New field
  final int owner; // New field, corresponding to accountId
  final String editors; // New field
  final DateTime creationDate;
  final DateTime? updateAt; // New nullable field
  final int delFlag; // New field

  double currentBalance = 0.0;

  LedgerBook({
    this.id,
    required this.title,
    this.budget = 1000000, // Default value or required
    required this.owner,
    required this.editors,
    required this.creationDate,
    this.updateAt, // Nullable
    this.delFlag = 0, // Default value or required
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'budget': budget,
      'owner': owner,
      'editors': editors,
      'createAt': creationDate.toIso8601String(), // Renamed to match database field
      'updateAt': updateAt?.toIso8601String(), // Nullable field
      'delFlag': delFlag,
    };
  }

  static LedgerBook fromMap(Map<String, dynamic> map) {
    return LedgerBook(
      id: map['id'],
      title: map['title'] ?? 'Default Title', // Provide a default value if null
      budget: map['budget'] ?? 1000000,
      owner: map['owner'],
      editors: map['editors'] ?? '', // Provide a default or empty string if null
      creationDate: DateTime.parse(map['createAt']),
      updateAt: map['updateAt'] != null ? DateTime.parse(map['updateAt']) : null,
      delFlag: map['delFlag'],
    );
  }
}
