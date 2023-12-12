import 'package:flutter/material.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

class ForgotPasswordPage extends StatelessWidget {
  ForgotPasswordPage({super.key});

  // text editing controllers
  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  void UserForgotPassword() {}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: const Color(0xFFF2FCF6),
        body: SafeArea(
            child: Center(
                child: Column(children: [
          //logo
          const CustomSquareTile(
            imagePath: 'assets/logo_login.png',
            borderRadius: 60,
            imageHeight: 275,
            paddingImage: 0.0,
          ),

          const SizedBox(height: 15),

          const Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: EdgeInsets.only(left: 48.0), // Adjust the left padding as needed
              child: Column(
                children: [
                  Text(
                    'Forgot password',
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

          const SizedBox(height: 30),

          CustomButton(
            onPressed: UserForgotPassword,
            textButton: 'Submit',
          ),

          const SizedBox(height: 25),

          //Link to sing up
          CustomClickableText(
            text: 'Back to login',
            clickableText: 'Back to login',
            onTap: () {
              // Navigate to another page or perform desired action
              Navigator.of(context).pushNamed(RouteGenerator.loginPage);
            },
            clickableStyle: const TextStyle(
              decoration: TextDecoration.underline,
              fontWeight: FontWeight.w500,
              color: Colors.red,
            ),
          ),
        ]))));
  }
}
