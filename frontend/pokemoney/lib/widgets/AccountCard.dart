import 'package:flutter/material.dart';
import 'package:pokemoney/model/Account.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

class AccountCard extends StatelessWidget {
  final Account _account;
  // Constructor to initialize the Account object
  const AccountCard(this._account, {Key? key}) : super(key: key);

  // Function to handle the menu selection
  void _onMenuSelected(String value) {
    switch (value) {
      case 'logout':
        // Handle logout functionality here
        break;
      case 'settings':
        // Handle settings functionality here
        break;
      // Handle other options here
    }
  }

  // Widget to create account details
  Widget _buildAccountDetails() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(_account.accountName,
            style:
                const TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold)),
        Text(_account.type,
            style:
                const TextStyle(fontSize: 14.0, fontWeight: FontWeight.w500)),
      ],
    );
  }

  // Widget to create popup menu
  Widget _buildPopupMenu() {
    return PopupMenuButton<String>(
      surfaceTintColor: Colors.white,
      icon: const Icon(Icons.more_vert_outlined),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10.0),
      ),
      itemBuilder: (BuildContext context) => [
        const PopupMenuItem<String>(
          value: 'settings',
          child:
              ListTile(leading: Icon(Icons.settings), title: Text('Settings')),
        ),
        const PopupMenuDivider(),
        const PopupMenuItem<String>(
          value: 'logout',
          child: ListTile(
            leading: Icon(Icons.logout, color: Colors.red),
            title: Text('Logout', style: TextStyle(color: Colors.red)),
          ),
        ),
      ],
      onSelected: _onMenuSelected,
    );
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () => Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => AccountDetailsPage(_account))),
      child: Padding(
        padding: const EdgeInsets.only(bottom: 5.0),
        child: Card(
          surfaceTintColor: AppColors.tileLists,
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                CircleAvatar(
                    radius: 25,
                    backgroundImage: AssetImage(_account.pictureUrl)),
                Padding(
                    padding: const EdgeInsets.only(left: 15),
                    child: _buildAccountDetails()),
                Expanded(child: Container()),
                _buildPopupMenu(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
