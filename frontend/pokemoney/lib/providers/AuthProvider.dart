import 'package:flutter/material.dart';
import 'dart:async';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/services/AuthService.dart';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:pokemoney/services/database_helper.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:pokemoney/services/EditorService.dart';

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

  AuthProvider(this._authService, this._secureStorage) {
    _initialize();
  }

  Future<void> _initialize() async {
    _isLoading = true;
    try {
      await _checkLoginStatus();
    } catch (e) {
      _errorMessage = 'Initialization error: ${e.toString()}';
    } finally {
      _isLoading = false;
    }
  }

  Future<void> _checkLoginStatus() async {
    String? token = await _secureStorage.getToken();
    if (token != null && token.isNotEmpty) {
      _isLoggedIn = true;
      await _loadUserData();
    } else {
      _isLoggedIn = false;
      _currentUser = null;
    }
    notifyListeners();
  }

  Future<void> login(String email, String password) async {
    _updateLoadingStatus(true);
    _errorMessage = null;

    try {
      _currentUser = await _authService.login(email, password);
      await _checkLoginStatus();
      if (_isLoggedIn) {
        await _saveUserData(_currentUser!);
        await initializeDatabaseForUser(_currentUser!.id!); // Initialize DB for logged-in user
      }
    } catch (error) {
      // Set the error message here
      _errorMessage = 'Login error: ${error.toString()}';
    } finally {
      _updateLoadingStatus(false);
    }
  }

  Future<void> startSignUp(String username, String email, String password) async {
    _updateLoadingStatus(true);
    _errorMessage = null;

    try {
      await _authService.registerTry(username, email, password);
    } catch (e) {
      _errorMessage = 'Signup error: ${e.toString()}';
    } finally {
      _updateLoadingStatus(false);
    }
  }

  Future<void> completeSignUp(String username, String email, String password, String verificationCode) async {
    _updateLoadingStatus(true);
    _errorMessage = null;

    try {
      User user = await _authService.registerVerify(username, email, password, verificationCode);
      _isLoggedIn = true;
      await _saveUserData(user);
      await initializeDatabaseForUser(_currentUser!.id!); // Initialize DB for logged-in user
    } catch (e) {
      _errorMessage = 'Signup completion error: ${e.toString()}';
    } finally {
      _updateLoadingStatus(false);
    }
  }

  Future<void> initializeDatabaseForUser(int userId) async {
    DBHelper.setUserId(userId); // Pass the user ID to DBHelper
    await DBHelper().initDb(); // Initialize the database for this user
  }

  Future<void> _saveUserData(User user) async {
    _currentUser = user;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt('id', user.id!);
    await prefs.setString('username', user.username);
    await prefs.setString('email', user.email);

    //added the user to the editor table
    Editor editor = Editor(userId: user.id!, email: user.email, name: user.username);
    await EditorService().upsertEditor(editor);

    notifyListeners();
  }

  Future<void> _loadUserData() async {
    final prefs = await SharedPreferences.getInstance();
    int? id = prefs.getInt('id');
    String? username = prefs.getString('username');
    String? email = prefs.getString('email');
    if (username != null && email != null && id != null) {
      _currentUser = User.idAndUsernameAndEmail(id: id, username: username, email: email);
    }
    notifyListeners();
  }

  Future<void> logout() async {
    _updateLoadingStatus(true);

    try {
      await _authService.logout();
      _isLoggedIn = false;
      _currentUser = null;
      await _secureStorage.deleteToken();
      await _clearUserData();
      DBHelper.resetUserId(); // You'll need to add this method to DBHelper
    } catch (e) {
      _errorMessage = 'Logout error: ${e.toString()}';
    } finally {
      _updateLoadingStatus(false);
    }
  }

  Future<void> _clearUserData() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('id');
    await prefs.remove('username');
    await prefs.remove('email');
    notifyListeners();
  }

  void _updateLoadingStatus(bool isLoading) {
    _isLoading = isLoading;
    notifyListeners();
  }
}
