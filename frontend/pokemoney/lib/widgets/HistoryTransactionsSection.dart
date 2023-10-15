import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

class HistoryTransactionsSection extends StatelessWidget {
  final List<Transaction> transactions;

  HistoryTransactionsSection({required this.transactions});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8.0),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 2,
            blurRadius: 3,
            offset: Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'History transactions',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 18.0,
            ),
          ),
          SizedBox(height: 16.0),
          DataTable(
            columnSpacing: 25,
            columns: [
              DataColumn(label: Text('INVOICE')),
              DataColumn(label: Text('VENDOR')),
              DataColumn(label: Text('DATE')),
              DataColumn(label: Text('AMOUNT')),
            ],
            rows: transactions.map((transaction) {
              return DataRow(cells: [
                DataCell(Text(transaction.invoiceNumber)),
                DataCell(Text(transaction.vendor)),
                DataCell(Text(transaction.billingDate)),
                DataCell(Text('\$${transaction.amount.toStringAsFixed(2)}')),
              ]);
            }).toList(),
          ),
          TextButton(
            onPressed: () {
              // Add action for adding a team member
            },
            child: const Text(
              'Add team member',
              style: TextStyle(color: AppColors.textClickable, fontSize: 16.0, fontWeight: FontWeight.bold),
            ),
          )
        ],
      ),
    );
  }
}
