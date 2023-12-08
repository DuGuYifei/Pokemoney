import 'package:flutter/material.dart';

class WelcomeScreen extends StatefulWidget {
  const WelcomeScreen({super.key});

  @override
  _WelcomeScreenState createState() => _WelcomeScreenState();
}

class _WelcomeScreenState extends State<WelcomeScreen> {
  int _currentPage = 0;
  final PageController _pageController = PageController(initialPage: 0);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        controller: _pageController,
        onPageChanged: (int page) {
          setState(() {
            _currentPage = page;
          });
        },
        children: <Widget>[
          createPage(
            title: "Welcome to Pokemoney",
            content: "Image for Page 1",
            buttonText: "Next",
            showButton: true,
          ),
          createPage(
            title: "Make your expenses recorded and know where your money goes",
            content: "Image for Page 2",
            buttonText: "Next",
            showButton: true,
          ),
          createPage(
            title: "To get started, choose to login or register your account",
            content: "Image for Page 3",
            buttonText: "Login/Register",
            showButton: false,
          ),
        ],
      ),
    );
  }

  Widget createPage({required title,required String content, required buttonText, required bool showButton}) {
    return Container(
      padding: const EdgeInsets.all(20),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          Text(
            title,
            textAlign: TextAlign.center,
            style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 20),
          Image.asset(content), // Replace with Image.asset or Image.network based on your image source
          const SizedBox(height: 20),
          showButton ? ElevatedButton(
            child: Text(buttonText),
            onPressed: () {
              if (_currentPage < 2) {
                _pageController.animateToPage(
                  _currentPage + 1,
                  duration: const Duration(milliseconds: 400),
                  curve: Curves.easeInOut,
                );
              } else {
                // Handle Login/Register navigation
              }
            },
          ) : Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                child: const Text("Login"),
                onPressed: () {
                  // Handle Login
                },
              ),
              ElevatedButton(
                child: const Text("Register"),
                onPressed: () {
                  // Handle Register
                },
              ),
            ],
          ),
        ],
      ),
    );
  }
}