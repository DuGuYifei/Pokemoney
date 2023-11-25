import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/services/AuthService.dart';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart';

class AuthProvider with ChangeNotifier {
  final AuthService _authService;
  final SecureStorage _secureStorage;
  bool _isLoggedIn = false;
  bool _isLoading = false;
  String? _errorMessage;
  User? _currentUser;

  String? get errorMessage => _errorMessage;
  bool get isLoading => _isLoading;
  User? get currentUser => _currentUser;
  bool get isLoggedIn => _isLoggedIn;

  set isLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  AuthProvider(this._authService, this._secureStorage) {
    checkLoginStatus();
  }

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

  //The is called when the user if first trying to sign up
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

  //This is called when the user is trying to complete the sign up process by asking the server to verify the user's email address
  Future<void> completeSignUp(String username, String email, String password, String verificationCode) async {
    _isLoading = true;
    try {
      User user = await _authService.registerVerify(username, email, password, verificationCode);
      _isLoggedIn = true;
      await saveUserData(user);
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> saveUserData(User user) async {
    _currentUser = user;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('username', user.username);
    await prefs.setString('email', user.email);
    notifyListeners();
  }

  Future<void> loadUserData() async {
    final prefs = await SharedPreferences.getInstance();
    String? username = prefs.getString('username');
    String? email = prefs.getString('email');
    if (username != null && email != null) {
      _currentUser = User.usernameAndEmail(username: username, email: email);
    }
    notifyListeners();
  }

  Future<void> logout() async {
    await _authService.logout();
    _isLoggedIn = false;
    _currentUser = null;
    await _authService.logout();
    await checkLoginStatus();
  }

  // Add additional authentication logic as needed
}
