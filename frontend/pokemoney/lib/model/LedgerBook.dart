import 'package:pokemoney/model/barrel.dart';
class LedgerBook {
  int id;
  String title;
  String description;
  double balance;
  DateTime creationDate;
  List<Transaction> transactions;

  LedgerBook({required this.id, required this.title, required this.description, required this.balance, required this.creationDate,  required this.transactions
});
}
