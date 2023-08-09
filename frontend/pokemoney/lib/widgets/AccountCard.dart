import 'package:flutter/material.dart';
import 'package:pokemoney/model/Account.dart';

class AccountCard extends StatelessWidget {
  Account _accounts;
  AccountCard(this._accounts);

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: EdgeInsets.only(bottom: 20.0),
        child: Card(
            child: Padding(
                padding: EdgeInsets.all(20.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Image.asset("${_accounts.pictureUrl}"),
                    Padding(
                        padding: EdgeInsets.only(top: 20.0, bottom: 10.0),
                        child: Text(
                          "${_accounts.accountName}",
                          style: TextStyle(
                              fontSize: 14.0, fontStyle: FontStyle.italic),
                        )),
                    Padding(
                        padding: EdgeInsets.only(bottom: 10.0),
                        child: Text("${_accounts.type}",
                            style: TextStyle(
                                fontSize: 20.0, fontWeight: FontWeight.bold))),
                    Text(
                      "${_accounts.pictureUrl}",
                      maxLines: 2,
                      style: TextStyle(fontSize: 10.0),
                      overflow: TextOverflow.fade,
                    ),
                    Row(children: [])
                  ],
                ))));
  }
}
