import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:pokemoney/model/barrel.dart';

class AuthService {
  final String baseUrl;
  final SecureStorage secureStorage;

  AuthService(this.baseUrl, this.secureStorage);

  // Constants for API endpoints
  static const String _loginEndpoint = "/api/v1/user/login";
  static const String _registerTry = "/api/v1/user/register-try";
  static const String _registerVerify = "/api/v1/user/register-verify";
  // Add other endpoints as constants...

  Future<User> login(String email, String password) async {
    final url = Uri.parse('$baseUrl$_loginEndpoint');
    try {
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode({'usernameOrEmail': email, 'password': password}),
      );

      if (response.statusCode == 200) {
        String? token = response.headers['authorization'];
        await secureStorage.saveToken(token!);
        var responseData = json.decode(response.body);
        print("reposne date:" + responseData.toString());

        User user = User.fromJson(responseData['data']);
        return user;
        // String token = responseData['token'];
        // await secureStorage.saveToken(token);
      } else {
        var responseData = json.decode(response.body);
        String serverErrorMessage = responseData['message'] ??
            'Unknown error occurred'; // Replace 'message' with the actual key used by your server
        return Future.error('Failed to login. Error: $serverErrorMessage');
      }
    } catch (e) {
      // Handle exceptions for network issues, etc.
      return Future.error('Login failed due to an error: $e');
    }
  }

  Future<void> registerTry(String username, String email, String password) async {
    //final url = Uri.parse('$baseUrl/signup'); // Use your actual sign-up endpoint
    final url = Uri.parse('$baseUrl$_registerTry'); // Use your actual sign-up endpoint

    try{
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
        return Future.error(responseData['message']);
      }
    }
    }catch(e){
      return Future.error('Failed to Register: ${e.toString()}');
    }
  }

  Future<User> registerVerify(String username, String email, String password, String verificationCode) async {
    final url = Uri.parse('$baseUrl$_registerVerify');
    try{
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
        if (token != null) {
          await secureStorage.saveToken(token);

          User user = User.fromJson(responseData['data']);
          return user;
        } else {
          return Future.error('Token not found in response');
        }
      } else {
        return Future.error('Failed to verify: ${responseData['message']}');
      }
    } else {
      return Future.error('Failed to sign up: ${response.reasonPhrase}');
    }
    }catch(e){
      return Future.error('Failed to sign up: ${e.toString()}');
    }
  }

  Future<void> logout() async {
    await secureStorage.deleteToken();
  }
  // Additional methods...
}
