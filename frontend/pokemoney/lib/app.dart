// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, prefer_final_fields

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/services/sync/SyncManager.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/services/sync/GraphQLClientService.dart';
import 'package:pokemoney/services/DatabaseService.dart';

class App extends StatefulWidget {
  final int initialIndex;
  const App({Key? key, this.initialIndex = 0}) : super(key: key);

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  int _currentIndex = 0;
  final DatabaseService databaseProvider = DatabaseService();
  late final GraphQLClientService graphqlClientService;
  SyncManager? syncManager;

  @override
  void initState() {
    super.initState();
    _currentIndex = widget.initialIndex;
    _initializeSyncManager();
  }

  Future<void> _initializeSyncManager() async {
    graphqlClientService = await GraphQLClientService.initialize();
    syncManager = SyncManager(databaseProvider, graphqlClientService);
  }

  final List<Widget> _routers = [
    /// List of tab page widgets
    HomePage(),
    FundsPage(),
    LedgerBooksPage(),
    AccountPage(),
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

  void _onDrawerItemTapped(int index) {
    Navigator.of(context).pop(); // Close the drawer
    setState(() {
      _currentIndex = index;
    });
  }

  void _onSyncButtonPressed() async {
    if (syncManager == null) {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('Sync Manager not initialized')));
      return;
    }

    try {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Syncing...')));
      await syncManager!.syncData(context); // Call syncData from SyncManager
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Sync Complete')));
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Sync Failed: $e')));
    }
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
            Padding(
              padding: const EdgeInsets.only(right: 10.0),
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  foregroundColor: AppColors.whiteBackgorund,
                  backgroundColor: AppColors.primaryColor,
                ),
                onPressed: _onSyncButtonPressed,
                child: Text('Sync'),
              ),
            ),
          ]),
      drawer: CustomNavBar(onItemTapped: _onDrawerItemTapped),
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
            label: 'Books',
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
