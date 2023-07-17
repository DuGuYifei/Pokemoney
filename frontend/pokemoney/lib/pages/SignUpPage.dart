import 'package:flutter/material.dart';

import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class SignUpPage extends StatefulWidget {
  SignUpPage({super.key});

  @override
  State<SignUpPage> createState() => _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {
  // text editing controllers
  final usernameController = TextEditingController();

  final passwordController = TextEditingController();
  bool agree = false;

  void signUserUp() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => HomePage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor:  AppColors.surface,
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
              padding: EdgeInsets.only(
                  left: 48.0), // Adjust the left padding as needed
              child: Column(
                children: [
                  Text(
                    'Sign-Up',
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
            headerText: 'Username',
            controller: usernameController,
            labelText: 'Your username',
            obscureText: false,
          ),

          const SizedBox(height: 10),

          //Email textfield
          CustomTextField(
            headerText: 'Email',
            controller: usernameController,
            labelText: 'Your Email',
            obscureText: false,
          ),

          const SizedBox(height: 15),

          //password textfield
          CustomTextField(
            headerText: 'Password',
            controller: passwordController,
            labelText: 'Password',
            obscureText: true,
          ),

          const SizedBox(height: 10),

          //confirm password textfield
          CustomTextField(
            controller: passwordController,
            labelText: 'Confirm password',
            obscureText: true,
          ),

          const SizedBox(height: 10),

          Row(
            children: [
              const SizedBox(
                width: 10,
              ),
              Material(
                child: SizedBox(
                  height: 24.0,
                  width: 24.0,
                  child: Checkbox(
                    fillColor: MaterialStateProperty.all<Color>(
                        AppColors.primaryColor),
                    value: agree,
                    onChanged: (value) {
                      setState(() {
                        agree = value ?? false;
                      });
                    },
                  ),
                ),
              ),
              CustomClickableText(
                  text: 'I have read and accpet terms and conditions',
                  onTap: () {
                    // Navigate to another page or perform desired action
                  },
                  clickableText: 'terms and conditions')
            ],
          ),

          const SizedBox(height: 15),

          //sign in button
          CustomButton(
            onPressed: agree ? signUserUp : null,
            textButton: 'Sign-up',
          ),
        ]))));
  }
}
