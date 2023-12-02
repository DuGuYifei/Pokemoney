// ignore_for_file: prefer_const_constructors

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';

class CustomNavBar extends StatelessWidget {
  final Function(int) onItemTapped;

  const CustomNavBar({super.key, required this.onItemTapped});

  @override
  Widget build(BuildContext context) {
    // Accessing the AuthProvider
    final authProvider = Provider.of<AuthProvider>(context);

    return Drawer(
      backgroundColor: AppColors.primaryColor,
      child: Column(
        children: <Widget>[
          UserAccountsDrawerHeader(
            decoration: BoxDecoration(color: AppColors.primaryColor),
            accountName: Text(authProvider.currentUser?.username ?? 'Guest'),
            accountEmail: authProvider.isLoggedIn
                ? GestureDetector(
                    onTap: () {
                      // Implement the logic to switch accounts
                      print("Switch account clicked");  // TODO: replce with going ot the account page
                    },
                    child: Text(
                      'Switch Account',
                      style: TextStyle(
                        decoration: TextDecoration.underline,
                      ),
                    ),
                  )
                : GestureDetector(
                    onTap: () {
                      // Implement the logic for guest or login action
                      
                    },
                    child: Text(
                      'Login/Signup',
                      style: TextStyle(
                        decoration: TextDecoration.underline,  // TODO: going to the sign up page
                      ),
                    ),
                  ),
            currentAccountPicture: CircleAvatar(
              backgroundColor: Colors.white,
              child: authProvider.currentUser != null
                  ? Text(
                      authProvider.currentUser!.username[0], // First letter of the username
                      style: TextStyle(fontSize: 20.0),
                    )
                  : FlutterLogo(size: 42.0),
            ),
          ),
          CustomHoverableListTile(
              icon: FontAwesomeIcons.book, title: 'Ledger books', onTap: () => onItemTapped(2)),
          CustomHoverableListTile(
            icon: FontAwesomeIcons.creditCard,
            title: 'Funds',
            onTap: () => onItemTapped(1),
          ),
          CustomHoverableListTile(icon: Icons.person, title: 'Accoutnts', onTap: () => onItemTapped(3)),
          CustomHoverableListTile(
            icon: Icons.bar_chart_outlined,
            title: 'Stats',
            onTap: () => Navigator.of(context).pushNamed(RouteGenerator.statsPage),
          ),
          CustomHoverableListTile(
            icon: FontAwesomeIcons.pieChart,
            title: 'Category',
            onTap: () => Navigator.of(context).pushNamed(RouteGenerator.categoryPage),
          ),
          Spacer(),
          Divider(
            color: Colors.white,
            thickness: 2.5,
            indent: 10.0,
            endIndent: 13.0,
          ),
          CustomHoverableListTile(
              icon: Icons.brush_outlined,
              title: 'Style',
              onTap: () => Navigator.of(context).pushNamed(RouteGenerator.stylePage)),
          CustomHoverableListTile(
              icon: Icons.privacy_tip_outlined,
              title: 'Privacy and Security',
              onTap: () => Navigator.of(context).pushNamed(RouteGenerator.privacySecurityPage)),
          CustomHoverableListTile(
            icon: Icons.info,
            title: 'Help',
            onTap: () => Navigator.of(context).pushNamed(RouteGenerator.helpPage),
          ),
          CustomHoverableListTile(
            icon: Icons.settings_sharp,
            title: 'Settings',
            onTap: () => Navigator.of(context).pushNamed(RouteGenerator.settingsPage),
          ),
        ],
      ),
    );
  }
}
