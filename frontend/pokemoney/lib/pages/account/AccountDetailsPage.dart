import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

/// [AccountDetailsPage] displays details related to a given [Account].
///
/// This page showcases the account's name in the app bar and provides
/// a detailed view with the `HeaderAccountCard`, `LedgerOwnedContainer`,
/// and `AlertsContainer` related to the account.
class AccountDetailsPage extends StatelessWidget {
  final User user;

  /// Creates an [AccountDetailsPage] with a given [user].
  ///
  /// [account]: The [Account] object whose details are to be displayed.
  const AccountDetailsPage(this.user, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          user.username,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.surface,
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            HeaderAccountCard(user),
            const SizedBox(height: 10),
            LedgerOwnedContainer(user),
            const SizedBox(height: 10),
            AlertsContainer(user),
          ],
        ),
      ),
    );
  }
}
