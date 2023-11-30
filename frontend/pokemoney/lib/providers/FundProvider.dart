import 'package:pokemoney/model/barrel.dart';
import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'package:pokemoney/services/FundService.dart';

class FundProvider with ChangeNotifier {
  final FundService _fundService = FundService();
  List<Fund> _funds = [];
  String? _error;

  List<Fund> get funds => _funds;
  String? get error => _error;

  Future<void> fetchAllFunds() async {
    try {
      _funds = await _fundService.getAllFunds();
      _error = null; // Reset the error on successful fetch
    } catch (e) {
      _error = 'Failed to fetch funds: ${e.toString()}';
    }
    notifyListeners();
  }

  Future<int> addFund(Fund fund) async {
    try {
      await _fundService.addFund(fund);
      await fetchAllFunds();
      _error = null;
      return fund.id!;
    } catch (e) {
      _error = 'Failed to add fund: ${e.toString()}';
      notifyListeners(); // Notify listeners even if addition fails
      return -1;
    }
  }

  Future<void> deleteFund(int id) async {
    try {
      await _fundService.deleteFund(id);
      await fetchAllFunds();
      _error = null;
    } catch (e) {
      _error = 'Failed to delete fund: ${e.toString()}';
      notifyListeners(); // Notify listeners even if deletion fails
    }
  }

  Future<void> fetchFund(int fundId) async {
    try {
      Fund fund = await _fundService.getFundById(fundId);
      _funds = [fund]; // Update the list with only the fetched fund
      _error = null;
    } catch (e) {
      throw Exception('Fund with id $fundId not found');
    }
    notifyListeners();
  }

  Future<void> updateFund(Fund fund) async {
    try {
      await _fundService.updateFund(fund);
      await fetchAllFunds();
      _error = null;
    } catch (e) {
      _error = 'Failed to update fund: ${e.toString()}';
      notifyListeners(); // Notify listeners even if update fails
    }
  }

  // Updates the fund balance both in the database and the provider
  Future<void> updateFundBalance(int fundId, double amount, bool isIncome) async {
    try {
      // Update the balance in the database
      await _fundService.updateFundBalance(fundId, amount, isIncome);

      // Fetch the updated fund to reflect the new balance
      Fund updatedFund = await _fundService.getFundById(fundId);

      // Update the local list of funds
      int index = _funds.indexWhere((fund) => fund.id == fundId);
      if (index != -1) {
        _funds[index] = updatedFund;
        await fetchAllFunds();
        notifyListeners();
      }
    } catch (e) {
      // Handle exceptions
      print('Error updating fund: $e');
    }
  }

  // get a fund by id
  Future<Fund> getFundById(int fundId) async {
    try {
      Fund fund = await _fundService.getFundById(fundId);
      return fund;
    } catch (e) {
      throw Exception('Fund with id $fundId not found');
    }
  }
}
