// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, prefer_final_fields

import 'package:flutter/material.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/widgets/ledgerBook/LedgerBookCard.dart';
import 'package:pokemoney/widgets/HistoryTransactionsAll.dart';
import 'package:pokemoney/model/LedgerBook.dart';
import 'package:pokemoney/constants/AppColors.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  bool _isLeading = true;

  @override
  void initState() {
    super.initState();
    // Fetch data from providers
    final ledgerBookProvider = context.read<LedgerBookProvider>();
    final transactionProvider = context.read<TransactionProvider>();
    ledgerBookProvider.fetchAllLedgerBooksFromSyncAndUnsync();
    transactionProvider.fetchAllTransactions();
    setState(() {
      _isLeading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final transactionProvider = context.watch<TransactionProvider>();
    final ledgerBookProvider = context.watch<LedgerBookProvider>();
    final fundProvider = context.watch<FundProvider>();

    double totalIncome = transactionProvider.getTotalIncome();
    double totalExpense = transactionProvider.getTotalExpense();
    double totalBalance = totalIncome - totalExpense;

    return Scaffold(
      backgroundColor: AppColors.surface,
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSummary(totalIncome, totalExpense,
                totalBalance), // You can create separate builder methods for each section
            const SizedBox(height: 16.0),
            HistoryTransactionsAll(transactions: transactionProvider.transactions),
            const SizedBox(height: 16.0),
            _buildLedgerBooks(ledgerBookProvider.ledgerBooks),
          ],
        ),
      ),
    );
  }

  // Builds the summary section which contains of rows and in the first row there is a column with two text widgets, the second row contains an svg picture, the last row contains of a column with two text widgets
  Widget _buildSummary(double totalIncome, double totalExpense, double totalBalance) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Column(
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    'Income',
                    style: TextStyle(
                      color: Color(0xFF64748B),
                      fontSize: 13.85,
                      fontFamily: 'Manrope',
                      fontWeight: FontWeight.w400,
                      height: 0,
                    ),
                  ),
                  Text(
                    '\$${totalIncome.toStringAsFixed(1)}',
                    style: TextStyle(
                      color: Color(0xFF191D23),
                      fontSize: 20,
                      fontFamily: 'Manrope',
                      fontWeight: FontWeight.w700,
                      height: 0,
                    ),
                  )
                ],
              ),
              const SizedBox(height: 20.0),
              Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    'Expense',
                    style: TextStyle(
                      color: Color(0xFF64748B),
                      fontSize: 13.85,
                      fontFamily: 'Manrope',
                      fontWeight: FontWeight.w400,
                      height: 0,
                    ),
                  ),
                  Text(
                    '\$${totalExpense.toStringAsFixed(1)}',
                    style: TextStyle(
                      color: Color(0xFF191D23),
                      fontSize: 20,
                      fontFamily: 'Manrope',
                      fontWeight: FontWeight.w700,
                      height: 0,
                    ),
                  ),
                ],
              ),
            ],
          ),
          ClipRRect(
              borderRadius: BorderRadius.circular(10), // Adjust the radius as needed
              child: Image(
                image: AssetImage('assets/Brand icon.png'),
                width: 100,
                height: 100,
              )),
          Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Text(
                'Total Balance',
                style: TextStyle(
                  color: Color(0xFF64748B),
                  fontSize: 16.0,
                  fontFamily: 'Manrope',
                  fontWeight: FontWeight.w400,
                  height: 0,
                ),
              ),
              Text(
                '\$${totalBalance.toStringAsFixed(2)}',
                style: TextStyle(
                  fontFamily: 'Manrope',
                  fontSize: 24.0,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

// build legerBook cards that is a list viws and the direction of teh scrooling is horizentaly
  Widget _buildLedgerBooks(List<LedgerBook> ledgerBooks) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(left: 16.0),
          child: Text(
            'Ledger Books',
            style: const TextStyle(
              fontFamily: 'Manrope',
              fontSize: 24.0,
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
        const SizedBox(height: 16.0),
        SizedBox(
          height: 230.0,
          child: ledgerBooks.isEmpty
              ? const Center(
                  child: Text(
                    "No Ledger book found",
                    style: TextStyle(
                      fontSize: 18.0,
                      color: Colors.grey,
                    ),
                  ),
                )
              : ListView.builder(
                  scrollDirection: Axis.horizontal,
                  itemCount: ledgerBooks.length,
                  itemBuilder: (context, index) {
                    final ledgerBook = ledgerBooks[index];
                    return Padding(
                      padding: const EdgeInsets.only(left: 5.0),
                      child: LedgerBookCard(
                        ledgerBook,
                        'assets/backgorund_credit/small_background_creditcard.png',
                        true,
                      ),
                    );
                  },
                ),
        ),
      ],
    );
  }
}
