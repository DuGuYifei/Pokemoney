import 'package:flutter/material.dart';
import 'package:pokemoney/dataExample.dart';
import 'package:pokemoney/widgets/barrel.dart';

class FundsPage extends StatelessWidget {
  const FundsPage({super.key});

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    List<Widget> fundsCards =
        accountsList.expand((account) => account.funds.map((fund) => FundCard(fund, true))).toList();
    return Scaffold(
      body: ListView(padding: const EdgeInsets.all(20.0), children: fundsCards),
    );
  }
}
