import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/model/LincePrice.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class FundDetailsPage extends StatelessWidget {
  final Fund fund;

  // final List<Transaction> sampleTransactions = [
  //   Transaction(
  //     ledgerBookId: 2,
  //     invoiceNumber: '51468465',
  //     category: 'Jane Cooper',
  //     billingDate: DateTime(2002,3,1),
  //     amount: 500.00,
  //     type: 'income',
  //   ),
  //   Transaction(
  //     ledgerBookId: 2,
  //     invoiceNumber: '51468465',
  //     category: 'Jane Cooper',
  //     billingDate: DateTime(2002,3,1),
  //     amount: 500.00,
  //     type: 'income',
  //   ),
  //   // Add more transactions as needed
  // ];

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
            const CollaboratorSection(),
            const SizedBox(height: 10),
            // HistoryTransactionsSection(
            //   transactions: sampleTransactions,
            // ),
            const SizedBox(height: 10),
            //LineChartWidget(pricePoint),
          ],
        ),
      ),
    );
  }
}
