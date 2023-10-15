import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

class FundsPage extends StatelessWidget {
  FundsPage({super.key});
  final List<Fund> _fundList = [
    Fund(id: 1, balance: 200.0, name: "school", creationDate: DateTime(2002, 1, 3), type: 'Personal'),
    Fund(id: 2, balance: 400.0, name: "work", creationDate: DateTime(2005, 5, 15), type: 'Shared')
  ];

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    List<Widget> fundsCards = _fundList.map((funds) => FundCard(funds, true)).toList();
    return Scaffold(
      body: ListView(padding: EdgeInsets.all(20.0), children: fundsCards),
    );
  }
}
