import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Settings"),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
    );
  }
}
