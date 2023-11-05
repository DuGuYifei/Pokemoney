import 'package:flutter/material.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/dataExample.dart';

class AccountsPage extends StatelessWidget {
  const AccountsPage({super.key});

  @override
  Widget build(BuildContext context) {
  
    List<Widget> accountsCards = accountsList.map((accounts) => AccountCard(accounts)).toList();
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        const Padding(
          padding: EdgeInsets.fromLTRB(43, 19, 10, 1),
          child: Text(
            "Accounts",
            style: TextStyle(
              fontSize: 26,
              fontFamily: 'Roboto',
              fontWeight: FontWeight.w700,
            ),
          ),
        ),
        Expanded(
          // Wrap ListView with Expanded
          child: ListView(padding: const EdgeInsets.all(20.0), children: accountsCards),
        ),
      ],
    );
  }
}
