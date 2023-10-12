import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class HelpPage extends StatelessWidget {
  const HelpPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Help"),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
    );
  }
}
