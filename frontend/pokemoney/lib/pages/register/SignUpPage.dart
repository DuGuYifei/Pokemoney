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
  bool isPasswordVisible = false;

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

    // Check username length
    if (username.length < 4) {
      _showDialog('Username must be at least 4 characters long');
      return;
    }

    // Validate email format (you can use more sophisticated validation here)
    if (!email.contains('@')) {
      _showDialog('Please enter a valid email');
      return;
    }

    // Check if passwords match
    if (password.length < 8) {
      _showDialog('Passwords is too short should be atleast 8 characters');
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
                  controller: emailController,
                  labelText: 'Your Email',
                  obscureText: false,
                ),

                const SizedBox(height: 15),

                //password textfield
                CustomTextField(
                  headerText: 'Password',
                  controller: passwordController,
                  labelText: 'Password',
                  obscureText: !isPasswordVisible,
                  suffixIcon: IconButton(
                    icon: Icon(
                      isPasswordVisible ? Icons.visibility : Icons.visibility_off,
                    ),
                    onPressed: () {
                      setState(() {
                        isPasswordVisible = !isPasswordVisible;
                      });
                    },
                  ),
                ),

                const SizedBox(height: 10),

                //confirm password textfield
                CustomTextField(
                  controller: confirmPasswordController,
                  labelText: 'Confirm password',
                  obscureText: !isPasswordVisible,
                  suffixIcon: IconButton(
                    icon: Icon(
                      isPasswordVisible ? Icons.visibility : Icons.visibility_off,
                    ),
                    onPressed: () {
                      setState(() {
                        isPasswordVisible = !isPasswordVisible;
                      });
                    },
                  ),
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

                const SizedBox(height: 15),
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
