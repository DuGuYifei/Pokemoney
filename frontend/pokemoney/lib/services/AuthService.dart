import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:pokemoney/services/SecureStorage.dart';

class AuthService {
  final String baseUrl;
  final SecureStorage secureStorage;

  AuthService(this.baseUrl, this.secureStorage);

  // Constants for API endpoints
  static const String _loginEndpoint = "/login";
  static const String _registerTry = "/api/v1/user/register-try";
  static const String _registerVerify = "/api/v1/user/register-verify";
  // Add other endpoints as constants...

  Future<void> login(String email, String password) async {
    final url = Uri.parse('$baseUrl$_loginEndpoint');
    try {
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'email': email, 'password': password}),
      );

      if (response.statusCode == 200) {
        String? token = response.headers['authorization'];
        await secureStorage.saveToken(token!);
        // var responseData = json.decode(response.body);
        // String token = responseData['token'];
        // await secureStorage.saveToken(token);
      } else {
        // Handle different status codes appropriately
        throw Exception('Failed to login. Status code: ${response.statusCode}');
      }
    } catch (e) {
      // Handle exceptions for network issues, etc.
      throw Exception('Login failed due to an error: $e');
    }
  }

  Future<void> registerTry(String username, String email, String password) async {
    //final url = Uri.parse('$baseUrl/signup'); // Use your actual sign-up endpoint
    final url = Uri.parse('$baseUrl$_registerTry'); // Use your actual sign-up endpoint

    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({'username': username, 'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      // Assuming the server returns a token upon successful registration
      var responseData = json.decode(response.body);
      if (responseData['status'] > 0) {
        // if everything is ok
        // Registration try successful, handle as needed
      } else {
        throw Exception(responseData['message']);
      }
    }
  }

  Future<void> registerVerify(String username, String email, String password, String verificationCode) async {
    final url = Uri.parse('$baseUrl$_registerVerify');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'username': username,
        'email': email,
        'password': password,
        'verificationCode': verificationCode,
      }),
    );

    if (response.statusCode == 200) {
      final responseData = json.decode(response.body);
      
      
      if (responseData['status'] > 0) {
        String? token = response.headers['authorization'];
        await secureStorage.saveToken(token!);

        //String token = responseData['token'];
        //await secureStorage.saveToken(token); // Save the token securely
      } else {
        throw Exception('Failed to sign up: ${response.reasonPhrase}');
      }
    }
  }

  Future<void> logout() async {
    await secureStorage.deleteToken();
  }
  // Additional methods...
}
