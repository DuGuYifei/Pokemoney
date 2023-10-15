import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

/// [FundDetailsPage] displays details related to a given [Account].
///
/// This page showcases the account's name in the app bar and provides
/// a detailed view with the `HeaderAccountCard`, `LedgerOwnedContainer`,
/// and `AlertsContainer` related to the account.
class FundDetailsPage extends StatelessWidget {
  final Fund fund;

  final List<Transaction> sampleTransactions = [
    Transaction(
      invoiceNumber: '51468465',
      vendor: 'Jane Cooper',
      billingDate: '2/19/21',
      amount: 500.00,
    ),
    Transaction(
      invoiceNumber: '54673198',
      vendor: 'Wade Warren',
      billingDate: '5/7/16',
      amount: 235.34,
    ),
    // Add more transactions as needed
  ];

  /// Creates an [FundDetailsPage] with a given [fund].
  ///
  /// [account]: The [Account] object whose details are to be displayed.
  FundDetailsPage(this.fund, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          fund.name,
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
            FundCard(fund, false),
            const SizedBox(height: 10),
            CollaboratorSection(),
            const SizedBox(height: 10),
            HistoryTransactionsSection(
              transactions: sampleTransactions,
            ),
            // AlertsContainer(account),
          ],
        ),
      ),
    );
  }
}
