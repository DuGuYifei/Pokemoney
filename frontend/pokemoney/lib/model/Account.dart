import 'package:pokemoney/model/barrel.dart';

class Account {
  int id;
  String accountName;
  String type;
  String pictureUrl;
  String headerPicture;
  List<LedgerBook> ledgerBooks;
  // Assuming Alerts is another class, which you'll define.
  // List<Alert> alerts;

  Account({
    required this.id,
    required this.accountName,
    required this.type,
    required this.pictureUrl,
    required this.headerPicture,
    required this.ledgerBooks,
  });
}
