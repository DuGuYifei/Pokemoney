import 'package:flutter/material.dart';
import 'package:pokemoney/app.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  // text editing controllers
  final usernameController = TextEditingController();
  final passwordController = TextEditingController();

  bool isPasswordVisible = false;

  Future<void> signUserIn(BuildContext context) async {
    var username = usernameController.text;
    var password = passwordController.text;

    final authProvider = Provider.of<AuthProvider>(context, listen: false);

    // checking the password or email not empty
    if (username.isEmpty || password.isEmpty) {
      _showDialog('Please fill in all fields');
      return;
    }

    //checking the password that is less than 8 characters
    if (password.length < 8) {
      _showDialog('Passwords is too short should be atleast 8 characters');
      return;
    }

    try {
      await authProvider.login(username, password);

      // Check if there is an error message set by AuthProvider
      if (authProvider.errorMessage != null) {
        // Show the error message in a dialog
        _showDialog(authProvider.errorMessage!);
      } else {
        // On successful login, navigate to another page
        Navigator.of(context).pushReplacement(MaterialPageRoute(
          builder: (context) => const App(),
        ));
      }
    } catch (error) {
      _showDialog('Login failed: ${error.toString()}');
    }
  }

  // Helper function to show dialog
  void _showDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Login Failed'),
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
    final authProvider = Provider.of<AuthProvider>(context);

    bool isLoading = authProvider.isLoading;

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
                  headerText: 'Email or Username',
                  controller: usernameController,
                  labelText: 'Your email or username',
                  obscureText: false,
                ),

                const SizedBox(height: 10),

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

                //if (errorMessage != null) Text(errorMessage, style: TextStyle(color: Colors.red)),

                //sign in button
                CustomButton(
                  onPressed: isLoading != true ? () => signUserIn(context) : null,
                  textButton: 'Login',
                ),

                if (isLoading) const CircularProgressIndicator(), // Display loading indicator when logging in

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

                // //or contine with
                // const Padding(
                //   padding: EdgeInsets.symmetric(horizontal: 25.0),
                //   child: Row(
                //     children: [
                //       Expanded(
                //         child: Divider(
                //           thickness: 1,
                //           color: Color(0xFF347662),
                //         ),
                //       ),
                //       Padding(
                //         padding: EdgeInsets.symmetric(horizontal: 10.0),
                //         child: Text(
                //           'Or continue with',
                //           style: TextStyle(color: AppColors.textPrimary, fontWeight: FontWeight.w500),
                //         ),
                //       ),
                //       Expanded(
                //         child: Divider(
                //           thickness: 1,
                //           color: Color(0xFF347662),
                //         ),
                //       ),
                //     ],
                //   ),
                // ),

                // const SizedBox(height: 10),

                // //login with google and apple
                // const Row(
                //   mainAxisAlignment: MainAxisAlignment.center,
                //   children: [
                //     // google button
                //     CustomSquareTile(imagePath: 'assets/logo_google.png'),

                //     SizedBox(width: 25),

                //     // apple button
                //     CustomSquareTile(imagePath: 'assets/logo_apple.png')
                //   ],
                // ),
              ]),
            )),
          )),
        ));
  }
}
