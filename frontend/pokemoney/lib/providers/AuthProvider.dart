import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:pokemoney/services/AuthService.dart';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:http/http.dart';

class AuthProvider with ChangeNotifier {
  final AuthService _authService;
  final SecureStorage _secureStorage;
  bool _isLoggedIn = false;
  bool _isLoading = false;
  String? _errorMessage;

  String? get errorMessage => _errorMessage;
  bool get isLoading => _isLoading;

  set isLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  AuthProvider(this._authService, this._secureStorage) {
    checkLoginStatus();
  }

  bool get isLoggedIn => _isLoggedIn;

  Future<void> checkLoginStatus() async {
    String? token = await _secureStorage.getToken();
    _isLoggedIn = token != null && token.isNotEmpty;
    notifyListeners();
  }

  Future<void> login(String email, String password) async {
    isLoading = true;
    _errorMessage = null;

    try {
      await _authService.login(email, password);
      await checkLoginStatus();
    } on HttpException catch (e) {
      _errorMessage = "Server error: ${e.message}";
    } on ClientException catch (e) {
      _errorMessage = "Network error: ${e.message}";
    } on TimeoutException catch (e) {
      _errorMessage = "Connection timeout";
    } catch (e) {
      _errorMessage = "An unexpected error occurred";
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  Future<void> startSignUp(String username, String email, String password) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _authService.registerTry(username, email, password);
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> completeSignUp(String username, String email, String password, String verificationCode) async {
    _isLoading = true;
    try {
      await _authService.registerVerify(username, email, password, verificationCode);
      _isLoggedIn = true;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    await _authService.logout();
    await checkLoginStatus();
  }

  // Add additional authentication logic as needed
}
