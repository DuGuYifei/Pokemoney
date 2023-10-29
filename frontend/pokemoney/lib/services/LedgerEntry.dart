class LedgerEntry {
  final int? id;
  final double amount;
  final String description;
  final String category;
  final DateTime timestamp;

  LedgerEntry({
    this.id,
    required this.amount,
    required this.description,
    required this.category,
    required this.timestamp,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'amount': amount,
      'description': description,
      'category': category,
      'timestamp': timestamp.toIso8601String(),
    };
  }

  static LedgerEntry fromMap(Map<String, dynamic> map) {
    return LedgerEntry(
      id: map['id'],
      amount: map['amount'],
      description: map['description'],
      category: map['category'],
      timestamp: DateTime.parse(map['timestamp']),
    );
  }
}
