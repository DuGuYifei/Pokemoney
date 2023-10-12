import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';

class FundsModel extends ChangeNotifier {
  List<Fund> _funds = [];
  List<Fund> get funds => _funds;

  void addFund(Fund fund) {
    _funds.add(fund);
    notifyListeners();
  }

  void sortByName() {
    _funds.sort((a, b) => a.name.compareTo(b.name));
    notifyListeners();
  }

  void sortByBalance() {
    _funds.sort((a, b) => b.balance.compareTo(a.balance)); // Descending order
    notifyListeners();
  }

  void sortByCreationDate() {
    _funds.sort((a, b) => b.creationDate.compareTo(a.creationDate)); // Newest first
    notifyListeners();
  }

  void searchByName(String query) {
    _funds = _funds.where((fund) => fund.name.toLowerCase().contains(query.toLowerCase())).toList();
    notifyListeners();
  }
}
