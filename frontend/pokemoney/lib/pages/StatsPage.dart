import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class StatsPage extends StatelessWidget {
  const StatsPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Stats"),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
    );
  }
}
