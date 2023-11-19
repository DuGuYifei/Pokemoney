class LedgerBook {
  final int? id;
  final int accountId; // Foreign key
  final String title;
  String description;
  double initialBalance; // Represents the starting balance without transactions
  DateTime creationDate;

  double currentBalance = 0.0; // Represents the current balance including transactions

  LedgerBook({
    this.id,
    required this.accountId,
    required this.title,
    required this.description,
    required this.initialBalance,
    required this.creationDate,
  }) : currentBalance = initialBalance;

  // Updated the toMap method to include initialBalance.
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'accountId': accountId,
      'title': title,
      'description': description,
      'initialBalance': initialBalance,
      'creationDate': creationDate.toIso8601String(),
    };
  }

  // Updated the fromMap method to use initialBalance.
  static LedgerBook fromMap(Map<String, dynamic> map) {
    return LedgerBook(
      id: map['id'],
      accountId: map['accountId'],
      title: map['title'],
      description: map['description'],
      initialBalance: map['initialBalance'], // Use the initialBalance from the map
      creationDate: DateTime.parse(map['creationDate']),
      // Removed the transactions from the constructor call.
    );
  }
}
