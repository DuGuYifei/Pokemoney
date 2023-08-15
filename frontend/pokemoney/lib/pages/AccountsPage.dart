import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class AccountsPage extends StatelessWidget {
  AccountsPage({super.key});

  final List<Account> _accountsList = [
    Account(
      id: 1,
      accountName: "John's Account",
      type: "Savings",
      pictureUrl: "assets/default_account_picture.jpg",
      headerPicture: "assets/header_image_unsplash.jpg",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            title: "January",
            description: "January Expenses",
            balance: 1500.0),
        LedgerBook(
            id: 2,
            title: "February",
            description: "February Expenses",
            balance: 1200.0),
      ],
    ),
    Account(
      id: 2,
      accountName: "Oso Lensra",
      type: "Personal",
      pictureUrl: "assets/lion.jpg",
      headerPicture: "assets/header_image.png",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            title: "January",
            description: "January Expenses",
            balance: 1500.0),
        LedgerBook(
            id: 2,
            title: "February",
            description: "February Expenses",
            balance: 1200.0),
      ],
    ),
    Account(
      id: 3,
      accountName: "Nasr Taresh",
      type: "Fmaily",
      pictureUrl: "assets/logo_login.png",
      headerPicture: "assets/header_image.png",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            title: "Work",
            description: "work Expenses",
            balance: 1146.0),
        LedgerBook(
            id: 2, title: "Home", description: "Home Expenses", balance: 900.0),
      ],
    ),
  ];

  @override
  Widget build(BuildContext context) {
    List<Widget> accountsCards =
        _accountsList.map((accounts) => AccountCard(accounts)).toList();
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Padding(
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
          child:
              ListView(padding: EdgeInsets.all(20.0), children: accountsCards),
        ),
      ],
    );
  }
}
