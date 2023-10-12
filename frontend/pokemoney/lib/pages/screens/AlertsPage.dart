import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class AlertsPage extends StatelessWidget {
  final Account account;

  AlertsPage({required this.account});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('All Alerts'),
      ),
      body: SingleChildScrollView(
        child: AlertsContainer(account),
      ), // set showAll to true to display all alerts
    );
  }
}
