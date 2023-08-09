import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class AccountsPage extends StatelessWidget {
  AccountsPage({super.key});

  List<Account> _accountsList = [
    Account("Osamah Taresh", "Personal", "assets/default_account_picture.jpg"),
    Account("Osamah Taresh", "Personal", "assets/lion.jpg"),
    Account("Nasr Taresh", "Fmaily", "assets/logo_login.png"),
  ];
  @override
  Widget build(BuildContext context) {
    List<Widget> accountsCards =
        _accountsList.map((accounts) => AccountCard(accounts)).toList();
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Padding(
          padding: EdgeInsets.all(20.0),
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

// @override
// Widget build(BuildContext context) {
// List<Widget> newsCards = _newsList.map((news) => NewsCard(news)).toList();
// return new Scaffold(
// appBar: new AppBar(
// title: new Text("News Feed"),
// ),
// body: new ListView(padding: EdgeInsets.all(20.0), children:
// newsCards));
