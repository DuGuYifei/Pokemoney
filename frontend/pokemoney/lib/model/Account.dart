import 'package:pokemoney/model/barrel.dart';

class Account {
  int id;
  String accountName;
  String type;
  String pictureUrl;
  String headerPicture;
  List<LedgerBook> ledgerBooks;
  List<Alert> alerts; // list of user alerts

  Account({
    required this.id,
    required this.accountName,
    required this.type,
    required this.pictureUrl,
    required this.headerPicture,
    required this.ledgerBooks,
    required this.alerts,
  });

  // Add a new alert
  void addAlert(Alert alert) {
    alerts.add(alert);
  }

  void markAlertAsRead(String alertId) {
    final alertIndex = alerts.indexWhere((a) => a.id == alertId);

    if (alertIndex != -1) {
      alerts[alertIndex].isRead = true;
    }
  }

  // Delete an alert
  void deleteAlert(int alertId) {
    alerts.removeWhere((a) => a.id == alertId);
  }
}
