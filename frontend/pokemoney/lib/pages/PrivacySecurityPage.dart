import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class PrivacySecurityPage extends StatelessWidget {
  const PrivacySecurityPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Privacy and Security"),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
    );
  }
}
