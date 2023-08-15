import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/barrel.dart';

class AccountDetailsPage extends StatelessWidget {
  final Account account;
  const AccountDetailsPage(this.account, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          account.accountName,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        backgroundColor: AppColors.surfaceContainer,
      ),
      backgroundColor: AppColors.surface,
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            children: [
              HeaderAccountCard(account),
              LedgerOwnedCard(account),
            ],
          ),
        ),
      ),
    );
  }
}
            // ListView(
            //   children: <Widget>[
            //     //Image.asset(account.headerPicture),
            //     SizedBox(height: 20.0),
            //     Text("Account Name: ${account.accountName}"),
            //     SizedBox(height: 10.0),
            //     Text("Type: ${account.type}"),
            //     SizedBox(height: 10.0),
            //     ...account.ledgerBooks.map((ledgerBook) => Padding(
            //           padding: const EdgeInsets.symmetric(vertical: 8.0),
            //           child: Column(
            //             crossAxisAlignment: CrossAxisAlignment.start,
            //             children: [
            //               Text("Title: ${ledgerBook.title}"),
            //               Text("Description: ${ledgerBook.description}"),
            //               Text("Balance: \$${ledgerBook.balance}"),
            //             ],
            //           ),
            //         )),
            //     // Similarly, you can loop through `alerts` once you've defined it.
            //   ],
            // ),