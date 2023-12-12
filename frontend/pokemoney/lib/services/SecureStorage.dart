import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SecureStorage {
  final _storage = const FlutterSecureStorage();

  Future<void> saveToken(String token) async {
    await _storage.write(key: 'auth_token', value: token);
  }

  Future<String?> getToken() async {
    return await _storage.read(key: 'auth_token');
  }

  Future<void> deleteToken() async {
    await _storage.delete(key: 'auth_token');
  }

  Future<void> saveUsername(String username) async {
    await _storage.write(key: 'username', value: username);
  }

  Future<String?> getUsername() async {
    return await _storage.read(key: 'auth_token');
  }

  Future<void> deleteUsername() async {
    await _storage.delete(key: 'auth_token');
  }

  // Add more methods as needed for different types of data
}
