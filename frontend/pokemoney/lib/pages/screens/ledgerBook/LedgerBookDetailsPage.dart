import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/dataExample.dart';

class LedgerBookDetailsPage extends StatelessWidget {
  final LedgerBook _ledgerBook;

  LedgerBookDetailsPage(this._ledgerBook, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          _ledgerBook.title,
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
            LedgerBookCard(_ledgerBook, 'assets/backgorund_credit/small_background_creditcard.png', false),
            const SizedBox(height: 10),
            CollaboratorSection(),
            const SizedBox(height: 10),
            HistoryTransactionsSection(
              transactions: accountsList[1].ledgerBooks[_ledgerBook.id!]!.transactions,
            ),
            const SizedBox(height: 10),
            //LineChartWidget(pricePoint),
          ],
        ),
      ),
    );
  }
}
