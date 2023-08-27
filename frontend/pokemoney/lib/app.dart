// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, prefer_final_fields

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';

import 'package:pokemoney/pages/barrel.dart';

class App extends StatefulWidget {
  final int initialIndex;
  const App({Key? key, this.initialIndex = 0}) : super(key: key);

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  int _currentIndex = 0;

  @override
  void initState() {
    super.initState();
    _currentIndex = widget.initialIndex;
  }

  final List<Widget> _routers = [
    /// List of tab page widgets
    HomePage(),
    FundsPage(),
    LedgerBooksPage(),
    AccountsPage(),
  ];

  final List<String> _titles = [
    'Home',
    'Funds',
    'Ledger Books',
    'Accounts',
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
          centerTitle: true,
          title: Text(
            _titles[_currentIndex],
            style: TextStyle(
              fontWeight: FontWeight.bold, // Apply bold style here
            ),
          ),
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
      body: _routers[_currentIndex],
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
