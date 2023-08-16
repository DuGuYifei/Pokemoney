// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, prefer_final_fields

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';

import 'package:pokemoney/pages/barrel.dart';
class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  int _currentIndex = 0;

final List<String> _titles = [
    'Home',
    'Accounts',
    'Ledger Books',
    'Funds',
  ];

  void _onItemTapped(int index) {
    setState(() {
      _currentIndex = index;
      //Navigator.of(context)?.pushNamed(_routes[_currentIndex]);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      appBar: AppBar(
          iconTheme: IconThemeData(color: AppColors.textPrimary),
          backgroundColor: AppColors.surfaceContainer,
          title: Text(_titles[_currentIndex]),
          actions: [
            IconButton(
              icon: const Icon(
                FontAwesomeIcons.bell,
                color: AppColors.textPrimary,
              ),
              tooltip: 'Show Snackbar',
              onPressed: () {
                ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('This is a notifications')));
              },
            ),
          ]),
      drawer: CustomNavBar(),
      body: [
        /// List of tab page widgets
        HomePage(),
        const AccountsPage(),
        const LedgerBooksPage(),
        const FundsPage(),
      ][_currentIndex],
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        backgroundColor: AppColors.surfaceContainer,
        selectedItemColor: AppColors.textPrimary,
        unselectedItemColor: AppColors.textSecondary,
        selectedFontSize: 14,
        unselectedFontSize: 14,
        currentIndex: _currentIndex,
        onTap: _onItemTapped,
        items: const [
          BottomNavigationBarItem(
            label: 'Home',
            icon: Icon(Icons.home),
          ),
          BottomNavigationBarItem(
            label: 'Funds',
            icon: Icon(Icons.credit_card),
          ),
          BottomNavigationBarItem(
            label: 'Bills',
            icon: Icon(FontAwesomeIcons.fileInvoiceDollar),
          ),
          BottomNavigationBarItem(
            label: 'Accounts',
            icon: Icon(Icons.person),
          ),
        ],
      ),
    );
  }
}