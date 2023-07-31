// ignore_for_file: prefer_const_constructors

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/pages/HelpPage.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';

void switchAccount() {
  // Implement your switch account logic here
  print("Switch account clicked");
}

class CustomNavBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: AppColors.primaryColor,
      child: Column(children: <Widget>[
        // ignore: prefer_const_constructors
        UserAccountsDrawerHeader(
          decoration: BoxDecoration(color: AppColors.primaryColor),
          accountName: Text('Username'),
          accountEmail: GestureDetector(
            onTap: switchAccount,
            child: Text(
              'Switch Account',
              style: TextStyle(
                decoration: TextDecoration.underline,
              ),
            ),
          ),
          currentAccountPicture: CircleAvatar(
            backgroundColor: Colors.white,
            child: FlutterLogo(size: 42.0),
          ),
        ),
        CustomHoverableListTile(
          icon: FontAwesomeIcons.book,
          title: 'Ledger books',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.ledgerBooksPage),
        ),
        CustomHoverableListTile(
          icon: FontAwesomeIcons.creditCard,
          title: 'Funds',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.fundsPage),
        ),
        CustomHoverableListTile(
          icon: Icons.person,
          title: 'Accoutnts',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.accountsPage),
        ),
        CustomHoverableListTile(
          icon: FontAwesomeIcons.pieChart,
          title: 'Stats',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.statsPage),
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
            onTap: () =>
                Navigator.of(context)?.pushNamed(RouteGenerator.stylePage)),
        CustomHoverableListTile(
            icon: Icons.privacy_tip_outlined,
            title: 'Privacy and Security',
            onTap: () => Navigator.of(context)
                ?.pushNamed(RouteGenerator.privacySecurityPage)),
        CustomHoverableListTile(
          icon: Icons.info,
          title: 'Help',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.helpPage),
        ),
        CustomHoverableListTile(
          icon: Icons.settings_sharp,
          title: 'Settings',
          onTap: () =>
              Navigator.of(context)?.pushNamed(RouteGenerator.settingsPage),
        ),
      ]),
    );
  }
}
