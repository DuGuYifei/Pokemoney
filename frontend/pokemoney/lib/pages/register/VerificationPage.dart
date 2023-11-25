import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/app.dart';

class VerificationPage extends StatefulWidget {
  final String email;
  final String username;
  final String password;

  const VerificationPage({Key? key, required this.email, required this.username, required this.password})
      : super(key: key);

  @override
  _VerificationPageState createState() => _VerificationPageState();
}

class _VerificationPageState extends State<VerificationPage> {
  final codeController = TextEditingController();

  void verifyCode() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    try {
      await authProvider.completeSignUp(widget.username, widget.email, widget.password, codeController.text);

      if (authProvider.errorMessage == null) {
        Navigator.of(context).pushReplacement(MaterialPageRoute(
          builder: (context) => App(),
        ));
      } else {
        _showErrorDialog(authProvider.errorMessage!);
      }
    } catch (error) {
      _showErrorDialog(error.toString());
    }
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Verification Failed'),
        content: Text(message),
        actions: <Widget>[
          TextButton(
            child: Text('OK'),
            onPressed: () {
              Navigator.of(context).pop();
            },
          ),
        ],
      ),
    );
  }

  void resendCode() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    await authProvider.startSignUp(
        widget.username, widget.email, widget.password); // Resend verification code
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Verify Your Account')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Enter the verification code sent to ${widget.email}',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 16.0),
            ),
            SizedBox(height: 20),
            TextField(
              controller: codeController,
              keyboardType: TextInputType.number,
              maxLength: 6, // Limit to 6 digits
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'Verification Code',
              ),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: verifyCode,
              child: Text('Verify'),
            ),
            TextButton(
              onPressed: resendCode,
              child: Text('Resend Code'),
            ),
          ],
        ),
      ),
    );
  }
}
