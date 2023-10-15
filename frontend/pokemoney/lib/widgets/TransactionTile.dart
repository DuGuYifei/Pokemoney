import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';

class TransactionTile extends StatelessWidget {
  final Transaction transaction;

  TransactionTile({required this.transaction});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(transaction.vendor),
      subtitle: Text('Invoice: ${transaction.invoiceNumber}  |  Date: ${transaction.billingDate}'),
      trailing: Text('\$${transaction.amount.toStringAsFixed(2)}'),
    );
  }
}