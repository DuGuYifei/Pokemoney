import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/widgets/CustomNavBar.dart';
import 'package:pokemoney/constants/AppColors.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
        onTap: (value) {
          // Respond to item press.
        },
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
