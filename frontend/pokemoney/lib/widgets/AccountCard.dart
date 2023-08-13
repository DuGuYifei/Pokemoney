import 'package:flutter/material.dart';
import 'package:pokemoney/model/Account.dart';
import 'package:pokemoney/constants/AppColors.dart';

class AccountCard extends StatelessWidget {
  final Account _accounts;
  const AccountCard(this._accounts, {super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
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
                      backgroundImage: AssetImage(_accounts.pictureUrl),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 15),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(_accounts.accountName,
                              style: const TextStyle(
                                  fontSize: 20.0, fontWeight: FontWeight.bold)),
                          Text(_accounts.type,
                              style: const TextStyle(
                                  fontSize: 14.0, fontWeight: FontWeight.w500))
                        ],
                      ),
                    ),
                    Expanded(child: Container()),
                    PopupMenuButton<String>(
                      surfaceTintColor: Colors.white,
                      icon: const Icon(Icons.more_vert_outlined),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10.0),
                        side: BorderSide.none,
                      ),
                      itemBuilder: (BuildContext context) => [
                        const PopupMenuItem<String>(
                          value: 'settings',
                          child: Row(
                            children: [
                              Icon(Icons.settings),
                              SizedBox(width: 8.0),
                              Text('Settings'),
                            ],
                          ),
                        ),
                        const PopupMenuDivider(),
                        const PopupMenuItem<String>(
                          value: 'logout',
                          child: Row(
                            children: [
                              Icon(Icons.logout, color: Colors.red),
                              SizedBox(width: 8.0),
                              Text('Logout',
                                  style: TextStyle(color: Colors.red)),
                            ],
                          ),
                        ),
                      ],
                      onSelected: (String value) {
                        switch (value) {
                          case 'logout':
                            // Handle logout functionality here
                            break;
                          case 'settings':
                            // Handle settings functionality here
                            break;
                          // Handle other options here
                        }
                      },
                    ),
                  ],
                ))));
  }
}
