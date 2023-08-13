import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

class LedgerOwnedCard extends StatelessWidget {
  Account account;

  LedgerOwnedCard(this.account, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      surfaceTintColor: AppColors.cardBackgorund,
      child: Column(children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(children: [
            SizedBox(
              width: 15,
            ),
            Text("Ledger Owned",
                style: TextStyle(
                    color: Color(0xFF24292E),
                    fontSize: 22,
                    fontFamily: 'Manrope',
                    fontWeight: FontWeight.w500)),
            Expanded(child: Container()),
            CustomClickableText(
              text: "View All",
              onTap: () {},
              clickableText: "View All",
            ),
            SizedBox(
              width: 20,
            )
          ]),
        ),
        ListView(
          children: <Widget>[
            ...account.ledgerBooks.map((ledgerBook) => Padding(
                  padding: const EdgeInsets.symmetric(vertical: 8.0),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      CircleAvatar(
                        radius: 25,
                        backgroundImage: AssetImage(account.pictureUrl),
                      ),
                      Text("Title: ${ledgerBook.title}"),
                    ],
                  ),
                )),
            // Similarly, you can loop through `alerts` once you've defined it.
          ],
        ),
      ]),
    );
  }
}
