import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class LedgerBooksPage extends StatelessWidget {
  const LedgerBooksPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("Ledger Books"),
        backgroundColor: AppColors.surfaceContainer,
      ),
    );
  }
}
