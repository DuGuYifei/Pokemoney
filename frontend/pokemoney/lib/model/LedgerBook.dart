import 'package:pokemoney/model/barrel.dart';

class LedgerBook {
  final int? id;
  final int accountId; // Foreign key
  final String title;
  String description;
  double balance;
  DateTime creationDate;
  List<Transaction> transactions;

  LedgerBook({
    this.id,
    required this.accountId,
    required this.title,
    required this.description,
    required this.balance,
    required this.creationDate,
    required this.transactions,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'accountId': accountId,
      'title': title,
      'description': description,
      'balance': balance,
      'creationDate': creationDate.toIso8601String(),
    };
  }

  static LedgerBook fromMap(Map<String, dynamic> map) {
    return LedgerBook(
      id: map['id'],
      accountId: map['accountId'],
      title: map['title'],
      description: map['description'],
      balance: map['balance'],
      creationDate: DateTime.parse(map['creationDate']),
      transactions: [],
    );
  }
}
