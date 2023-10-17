import 'package:flutter/material.dart';
import 'package:pokemoney/model/Account.dart';
import 'package:pokemoney/pages/screens/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

/// A widget that displays an account card.
///
/// This card showcases the account details including the account name, type,
/// and an associated image. It also provides a popup menu for additional actions
/// such as logging out or accessing settings.
class AccountCard extends StatelessWidget {
  /// The [Account] object associated with this widget.
  final Account _account;

  /// Constructs an [AccountCard] widget.
  ///
  /// [_account]: The [Account] object that the card will represent.
  /// [key]: An optional key for the widget.
  const AccountCard(this._account, {Key? key}) : super(key: key);

  /// Handles the menu item selection from the popup menu.
  ///
  /// This method can be further expanded to handle additional actions.
  ///
  /// [value]: The value associated with the selected menu item.
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

  /// Builds and returns a widget that displays the account details.
  ///
  /// This includes the account name and its type.
  Widget _buildAccountDetails() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(_account.accountName, style: const TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold)),
        Text(_account.type, style: const TextStyle(fontSize: 14.0, fontWeight: FontWeight.w500)),
      ],
    );
  }

  /// Constructs and returns the popup menu for the account card.
  ///
  /// This menu currently provides options for settings and logging out. More options
  /// can be added as necessary.
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
          child: ListTile(leading: Icon(Icons.settings), title: Text('Settings')),
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
      onTap: () =>
          Navigator.of(context).push(MaterialPageRoute(builder: (context) => AccountDetailsPage(_account))),
      child: Padding(
        padding: const EdgeInsets.only(bottom: 5.0),
        child: Card(
          surfaceTintColor: AppColors.tileLists,
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                CircleAvatar(radius: 25, backgroundImage: AssetImage(_account.pictureUrl)),
                Padding(padding: const EdgeInsets.only(left: 15), child: _buildAccountDetails()),
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
