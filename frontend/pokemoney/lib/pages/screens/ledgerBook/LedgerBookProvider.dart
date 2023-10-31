import 'package:flutter/foundation.dart';
import 'package:pokemoney/pages/screens/ledgerBook/LedgerBookService.dart';
import 'package:pokemoney/model/barrel.dart';

class LedgerBookProvider with ChangeNotifier {
  final LedgerBookService _ledgerBookService = LedgerBookService();
  List<LedgerBook> _ledgerBooks = [];

  List<LedgerBook> get ledgerBooks => _ledgerBooks;

  fetchAllLedgerBooks() async {
    _ledgerBooks = await _ledgerBookService.getAllLedgerBooks();
    notifyListeners();
  }

  addLedgerBook(LedgerBook ledgerBook) async {
    await _ledgerBookService.addLedgerBook(ledgerBook);
    fetchAllLedgerBooks();
  }

  deleteLedgerBook(int id) async {
    await _ledgerBookService.deleteLedgerBook(id);
    fetchAllLedgerBooks();
  }
}
