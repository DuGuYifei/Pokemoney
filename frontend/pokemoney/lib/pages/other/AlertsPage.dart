import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class AlertsPage extends StatelessWidget {
  final User user;

  const AlertsPage({super.key, required this.user});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('All Alerts'),
      ),
      body: SingleChildScrollView(
        child: AlertsContainer(user),
      ), // set showAll to true to display all alerts
    );
  }
}
