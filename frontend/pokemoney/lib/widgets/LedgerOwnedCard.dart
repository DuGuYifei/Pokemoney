import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

class LedgerOwnedCard extends StatelessWidget {
  final Account account;

  const LedgerOwnedCard(this.account, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      // Assuming 'surfaceTintColor' is a valid property for Card, if not, you might have to adjust it.
      surfaceTintColor: AppColors.cardBackgorund,
      child: Column(children: [
        _buildHeader(),
        ListView.builder(
          shrinkWrap: true,
          physics: BouncingScrollPhysics(),
          itemCount: account.ledgerBooks.length,
          itemBuilder: (context, index) {
            final ledgerBook = account.ledgerBooks[index];
            return _buildLedgerItem(ledgerBook, context);
          },
        )
      ]),
    );
  }

  Widget _buildHeader() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Row(children: [
        const SizedBox(
          width: 15,
        ),
        const Text("Ledger Owned",
            style: TextStyle(
                color: AppColors.textPrimary,
                fontSize: 22,
                fontFamily: 'Manrope',
                fontWeight: FontWeight.w500)),
        Expanded(child: Container()),
        CustomClickableText(
          text: "View All",
          onTap: () {},
          clickableText: "View All",
        ),
        const SizedBox(
          width: 20,
        )
      ]),
    );
  }

  Widget _buildLedgerItem(LedgerBook ledgerBook, BuildContext context) {
    final String firstLetter = ledgerBook.title.isNotEmpty
        ? ledgerBook.title[0].toUpperCase()
        : '?'; // Default to '?' if title is empty.

    return ListTile(
      leading: Container(
        width: 25, // Adjust as per your needs.
        height: 25, // Adjust as per your needs.
        decoration: const BoxDecoration(
          color: Color(0xFF24292E),
          shape: BoxShape.circle,
        ),
        child: Center(
          child: Text(
            firstLetter,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 18, // Adjust as per your needs.
              fontFamily: 'Manrope',
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      ),
      title: Text(
        ledgerBook.title,
        style: const TextStyle(fontWeight: FontWeight.w500),
      ),
      onTap: () {
        // Navigate to ledger book details or perform any other action.
      },
    );
  }
}

// ListView.builder(
//   itemCount: account.ledgerBooks.length,
//   itemBuilder: (context, index) {
//     final ledgerBook = account.ledgerBooks[index];
//     return Padding(
//       padding: const EdgeInsets.symmetric(vertical: 8.0),
//       child: Row(
//         crossAxisAlignment: CrossAxisAlignment.start,
//         children: [
//           CircleAvatar(
//             radius: 25,
//             backgroundImage: AssetImage(account.pictureUrl),
//           ),
//           Text("Title: ${ledgerBook.title}"),
//         ],
//       ),
//     );
//   },
// )
