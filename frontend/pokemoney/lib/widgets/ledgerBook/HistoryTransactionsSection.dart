import 'package:flutter/material.dart';
import 'package:pokemoney/constants/ApiConstants.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/pages/ledgerBook/TransactionsPage.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:provider/provider.dart';

class HistoryTransactionsSection extends StatelessWidget {
  final List<Transaction> transactions;
  final LedgerBook ledgerBook;
  final int limit; // New parameter to control the number of transactions shown

  const HistoryTransactionsSection({
    super.key,
    required this.transactions,
    required this.ledgerBook,
    this.limit = 8,
  });

  @override
  Widget build(BuildContext context) {
// Sort the transactions by billingDate
    List<Transaction> sortedTransactions = List.from(transactions)
      ..sort((a, b) => b.billingDate.compareTo(a.billingDate)); // Descending order

    // If there are more transactions than the limit, only take up to the limit
    final visibleTransactions =
        sortedTransactions.length > limit ? sortedTransactions.sublist(0, limit) : sortedTransactions;
    final transactionProvider = Provider.of<TransactionProvider>(context, listen: false);

    return Container(
      padding: const EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8.0),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 2,
            blurRadius: 3,
            offset: const Offset(0, 3),
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
            columnSpacing: 10,
            columns: const [
              DataColumn(label: Text('INVOICE')),
              DataColumn(label: Text('CATEGORY')),
              DataColumn(label: Text('DATE')),
              DataColumn(label: Text('AMOUNT')),
            ],
            rows: visibleTransactions.map((transaction) {
              final formattedDate = DateFormat('yyyy-MM-dd').format(transaction.billingDate);
              final category = transactionProvider.getCategoryForTransaction(transaction);
              String lastFourDigits = transaction.invoiceNumber
                  .toString()
                  .padLeft(4, '0')
                  .substring(transaction.invoiceNumber.toString().length - 4);

              return DataRow(cells: [
                DataCell(Text(lastFourDigits)),
                DataCell(Text(category?.name.toUpperCase() ?? 'Unknown')),
                DataCell(Text(formattedDate)),
                DataCell(
                  Container(
                    padding: const EdgeInsets.symmetric(vertical: 3.0, horizontal: 8.0),
                    decoration: BoxDecoration(
                      color: transaction.type == transactionTypeCodes['income']
                          ? Colors.green[100]
                          : Colors.red[100],
                      borderRadius: BorderRadius.circular(4.0),
                      border: Border.all(
                        color: transaction.type == transactionTypeCodes['income'] ? Colors.green : Colors.red,
                        width: 1.0,
                      ),
                    ),
                    child: Text(
                      '\$${transaction.amount.toStringAsFixed(2)}',
                      style: TextStyle(
                        color: transaction.type == transactionTypeCodes['income'] ? Colors.green : Colors.red,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ]);
            }).toList(),
          ),
          TextButton(
            onPressed: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) => TransactionsPage(
                    ledgerBook: ledgerBook,
                  ),
                ),
              );
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
