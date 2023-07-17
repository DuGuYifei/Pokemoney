// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, prefer_final_fields

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/pages/NavBar.dart';
import 'package:pokemoney/constants/AppColors.dart';

import 'package:pokemoney/pages/barrel.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _selectedIndex = 0;
  static List<Widget> _widgetsOptions = <Widget>[
    HomePage(),
    FundsPage(),
    LedgerBooksPage(),
    AccountsPage(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
      Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => _widgetsOptions[_selectedIndex],
          ));
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      appBar: AppBar(
          iconTheme: IconThemeData(color: AppColors.textPrimary),
          backgroundColor: AppColors.surfaceContainer,
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
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        backgroundColor: AppColors.surfaceContainer,
        selectedItemColor: AppColors.textPrimary,
        unselectedItemColor: AppColors.textSecondary,
        selectedFontSize: 14,
        unselectedFontSize: 14,
        currentIndex: _selectedIndex,
        onTap: _onItemTapped,
        items: [
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
