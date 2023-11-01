import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';
import 'package:intl/intl.dart';

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
          const Text(
            'History transactions',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 18.0,
            ),
          ),
          const SizedBox(height: 16.0),
          DataTable(
            columnSpacing: 15,
            columns: const [
              DataColumn(label: Text('INVOICE')),
              DataColumn(label: Text('CATEGORY')),
              DataColumn(label: Text('DATE')),
              DataColumn(label: Text('AMOUNT')),
            ],
            rows: transactions.map((transaction) {
              final formattedDate = DateFormat('yyyy-MM-dd').format(transaction.billingDate);
              return DataRow(cells: [
                DataCell(Text(transaction.invoiceNumber)),
                DataCell(Text(transaction.category)),
                DataCell(Text(formattedDate)),
                DataCell(Text('\$${transaction.amount.toStringAsFixed(2)}')),
              ]);
            }).toList(),
          ),
          TextButton(
            onPressed: () {
              // Add action for adding a team member
            },
            child: const Text(
              'See all transactions',
              style: TextStyle(color: AppColors.textClickable, fontSize: 16.0, fontWeight: FontWeight.bold),
            ),
          )
        ],
      ),
    );
  }
}
