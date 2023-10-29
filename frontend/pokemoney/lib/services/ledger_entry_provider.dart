import 'package:flutter/material.dart';
import 'package:pokemoney/services/LedgerEntry.dart';
import 'package:pokemoney/services/ledger_entery_services.dart';

class LedgerEntryProvider with ChangeNotifier {
  final LedgerEntryService _ledgerEntryService = LedgerEntryService();
  List<LedgerEntry> _ledgerEntries = [];

  List<LedgerEntry> get ledgerEntries => _ledgerEntries;

  fetchAllLedgerEntries() async {
    _ledgerEntries = await _ledgerEntryService.getAllLedgerEntries();
    notifyListeners();
  }

  addLedgerEntry(LedgerEntry entry) async {
    await _ledgerEntryService.addLedgerEntry(entry);
    fetchAllLedgerEntries();
  }

  // Add other methods for update, delete, etc., and call `notifyListeners()` when necessary
}
