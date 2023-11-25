import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/pages/register/VerificationPage.dart';

class SignUpPage extends StatefulWidget {
  const SignUpPage({super.key});

  @override
  State<SignUpPage> createState() => _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {
  final usernameController = TextEditingController();
  final emailController = TextEditingController();
  final passwordController = TextEditingController();
  final confirmPasswordController = TextEditingController();

  bool agree = false;

  void signUserUp() async {
    String username = usernameController.text;
    String email = emailController.text;
    String password = passwordController.text;
    String confirmPassword = confirmPasswordController.text;
    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    // Check if any field is empty
    if (username.isEmpty || email.isEmpty || password.isEmpty || confirmPassword.isEmpty) {
      _showDialog('Please fill in all fields');
      return;
    }

    // Validate email format (you can use more sophisticated validation here)
    if (!email.contains('@')) {
      _showDialog('Please enter a valid email');
      return;
    }

    // Check if passwords match
    if (password != confirmPassword) {
      _showDialog('Passwords do not match');
      return;
    }

    try {
      await authProvider.startSignUp(username, email, password);

      if (authProvider.errorMessage != null) {
        _showDialog(authProvider.errorMessage!);
      } else {
        Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => VerificationPage(
            username: username,
            email: email,
            password: password,
          ),
        ));
      }
    } catch (error) {
      _showDialog('Sign-up failed: ${error.toString()}');
    }
  }

  // Helper function to show dialog
  void _showDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Sign-Up Failed'),
        content: Text(message),
        actions: <Widget>[
          TextButton(
            child: const Text('OK'),
            onPressed: () {
              Navigator.of(context).pop();
            },
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final isLoading = Provider.of<AuthProvider>(context).isLoading;

    return Scaffold(
        backgroundColor: AppColors.surface,
        body: GestureDetector(
          onTap: () => FocusScope.of(context).unfocus(),
          child: SafeArea(
              child: SingleChildScrollView(
            child: Center(
                child: ConstrainedBox(
              constraints: BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.9),
              child: Column(children: [
                CustomTextField(
                  headerText: 'Username',
                  controller: usernameController,
                  labelText: 'Your username',
                  obscureText: false,
                ),
                CustomTextField(
                  headerText: 'Email',
                  controller: emailController,
                  labelText: 'Your Email',
                  obscureText: false,
                ),
                CustomTextField(
                  headerText: 'Password',
                  controller: passwordController,
                  labelText: 'Password',
                  obscureText: true,
                ),
                CustomTextField(
                  controller: confirmPasswordController,
                  labelText: 'Confirm password',
                  obscureText: true,
                ),
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
                          fillColor: MaterialStateProperty.all<Color>(AppColors.primaryColor),
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
                CustomButton(
                  onPressed: agree && !isLoading ? () => signUserUp() : null,
                  textButton: 'Sign-up',
                ),
              ]),
            )),
          )),
        ));
  }
}
