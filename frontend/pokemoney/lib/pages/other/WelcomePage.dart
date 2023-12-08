import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';

class WelcomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        alignment: Alignment.center,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            const Text(
              'Welcome to Pokemoney!',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 32,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'Were your ',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 18,
              ),
            ),
            const SizedBox(height: 40),
            ElevatedButton(
              child: const Text('Sign Up'),
              onPressed: () {
                // Navigate to the Sign Up Page
                Navigator.of(context).pushNamed(RouteGenerator.signUpPage);
              },
            ),
            TextButton(
              child: const Text('Log In'),
              onPressed: () {
                // Navigate to the Log In Page
                Navigator.of(context).pushNamed(RouteGenerator.loginPage);
              },
            ),
          ],
        ),
      ),
    );
  }
}
