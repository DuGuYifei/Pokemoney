import 'package:flutter/material.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class LoginPage extends StatelessWidget {
  LoginPage({super.key});

  // text editing controllers
  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  Future<void> signUserIn(BuildContext context) async {
    var username = usernameController.text;
    var password = passwordController.text;

    try {
      var response = await http.post(
        Uri.parse('YOUR_BACKEND_LOGIN_ENDPOINT'),
        body: json.encode({'username': username, 'password': password}),
        headers: {'Content-Type': 'application/json'},
      );

      if (response.statusCode == 200) {
        // Handle successful login
        var data = json.decode(response.body);
        // Use the data as needed, e.g., navigate to home page, store user data, etc.
      } else {
        // Handle login failure
        // Show error message
      }
    } catch (e) {
      // Handle network or other errors
      // Show error message
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: AppColors.surface,
        body: SafeArea(
            child: Center(
                child: Column(children: [
          const SizedBox(height: 00),

          //logo
          const CustomSquareTile(
            imagePath: 'assets/logo_login.png',
            borderRadius: 60,
            imageHeight: 275,
            paddingImage: 0.0,
          ),

          const SizedBox(height: 5),

          const Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: EdgeInsets.only(left: 48.0), // Adjust the left padding as needed
              child: Column(
                children: [
                  Text(
                    'Login',
                    style: TextStyle(
                      color: AppColors.textPrimary,
                      fontSize: 30.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
            ),
          ),

          const SizedBox(height: 10),

          //username textfiled;
          CustomTextField(
            headerText: 'Email',
            controller: usernameController,
            labelText: 'Your email',
            obscureText: false,
          ),

          const SizedBox(height: 10),

          //password textfield
          CustomTextField(
            headerText: 'Password',
            controller: passwordController,
            labelText: 'Password',
            obscureText: true,
          ),

          const SizedBox(height: 10),

          //Forget password
          CustomClickableText(
            text: 'Forget password?',
            clickableText: 'Forget password?',
            onTap: () {
              // Navigate to another page or perform desired action
              Navigator.of(context).pushNamed(RouteGenerator.forgotPasswordPage);
            },
            clickableStyle: const TextStyle(
              decoration: TextDecoration.underline,
              fontWeight: FontWeight.w500,
              color: AppColors.textPrimary,
            ),
          ),

          const SizedBox(height: 15),

          //sign in button
          CustomButton(
            onPressed: () => signUserIn(context),
            textButton: 'Login',
          ),

          const SizedBox(height: 20),

          //Link to sing up
          CustomClickableText(
            text: 'Don\'t have an account? Sign-up',
            clickableText: 'Sign-up',
            onTap: () {
              // Navigate to another page or perform desired action
              Navigator.of(context).pushNamed(RouteGenerator.signUpPage);
            },
            textStyle: const TextStyle(
              fontWeight: FontWeight.w500,
              color: AppColors.textPrimary,
            ),
            clickableStyle: const TextStyle(
              decoration: TextDecoration.underline,
              fontWeight: FontWeight.w500,
              color: Colors.red,
            ),
          ),

          const SizedBox(height: 20),

          //or contine with
          const Padding(
            padding: EdgeInsets.symmetric(horizontal: 25.0),
            child: Row(
              children: [
                Expanded(
                  child: Divider(
                    thickness: 1,
                    color: Color(0xFF347662),
                  ),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 10.0),
                  child: Text(
                    'Or continue with',
                    style: TextStyle(color: AppColors.textPrimary, fontWeight: FontWeight.w500),
                  ),
                ),
                Expanded(
                  child: Divider(
                    thickness: 1,
                    color: Color(0xFF347662),
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 10),

          //login with google and apple
          const Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // google button
              CustomSquareTile(imagePath: 'assets/logo_google.png'),

              SizedBox(width: 25),

              // apple button
              CustomSquareTile(imagePath: 'assets/logo_apple.png')
            ],
          ),
        ]))));
  }
}
