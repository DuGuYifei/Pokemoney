import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:pokemoney/services/SecureStorage.dart';

class AuthService {
  final String baseUrl;
  final SecureStorage secureStorage;

  AuthService(this.baseUrl, this.secureStorage);

  Future<void> login(String email, String password) async {
    final url = Uri.parse('$baseUrl/login');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      var responseData = json.decode(response.body);
      String token = responseData['token'];
      await secureStorage.saveToken(token);  // Storing the token securely
    } else {
      throw Exception('Failed to login');
    }
  }

  Future<void> logout() async {
    await secureStorage.deleteToken();  // Clearing the token on logout
  }

  // Other methods...
}
