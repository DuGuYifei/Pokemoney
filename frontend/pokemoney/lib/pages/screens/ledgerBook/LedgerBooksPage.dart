import 'package:flutter/material.dart';
import 'package:pokemoney/dataExample.dart';
import 'package:pokemoney/widgets/barrel.dart';

class LedgerBooksPage extends StatelessWidget {
  const LedgerBooksPage({super.key});

  @override
  Widget build(BuildContext context) {
    List<Widget> fundsCards = accountsList
        .expand((account) => account.ledgerBooks.map((ledgerBook) =>
            LedgerBookCard(ledgerBook, 'assets/backgorund_credit/small_background_creditcard.png', true)))
        .toList();
    return Scaffold(
      body: GridView(
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            mainAxisSpacing: 5.0,
            crossAxisSpacing: 0.0,
            childAspectRatio: 0.87,
          ),
          children: fundsCards),
    );
  }
}
