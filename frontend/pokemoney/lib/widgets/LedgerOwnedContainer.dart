import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';

/// A widget that displays a list of ledger books associated with an account.
///
/// It showcases the ledger book details, and provides a "View all" option
/// if there are more ledger books than the set limit to display.
class LedgerOwnedContainer extends StatelessWidget {
  final Account account;

  // Number of ledger books to display.
  static const int NUMBER_OF_LEDGER_TO_SHOW = 3;

  // Height of the container.
  static const double HEIGHT_OF_CONTAINER = 180.0;

  LedgerOwnedContainer(this.account, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: AppColors.cardBackgorund,
        borderRadius: BorderRadius.circular(AppLayout.borderRadiusForContainers),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 2,
            blurRadius: 3,
            offset: Offset(0, 3),
          ),
        ],
      ),
      child: Column(children: [
        _buildHeader(),
        SizedBox(
          height: HEIGHT_OF_CONTAINER,
          child: ListView.builder(
            physics: const BouncingScrollPhysics(),
            itemCount: NUMBER_OF_LEDGER_TO_SHOW + 1,
            itemBuilder: (context, index) {
              if (index == NUMBER_OF_LEDGER_TO_SHOW) {
                return _buildSpecialTile();
              }
              // Otherwise, return the alert item
              return _buildLedgerItem(account.ledgerBooks[index], context);
            },
          ),
        )
      ]),
    );
  }

  /// Builds the header for the ledger books list.
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
          text: "View all",
          onTap: () {},
          clickableText: "View all",
        ),
        const SizedBox(
          width: 20,
        )
      ]),
    );
  }

  /// Constructs and returns a widget to display a single ledger book.
  ///
  /// [ledgerBook]: The [LedgerBook] object to be displayed.
  /// [context]: The build context.
  Widget _buildLedgerItem(LedgerBook ledgerBook, BuildContext context) {
    final String firstLetter = ledgerBook.title.isNotEmpty
        ? ledgerBook.title[0].toUpperCase()
        : '?'; // Default to '?' if title is empty.

    return ListTile(
      leading: Container(
        width: 25,
        height: 25,
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

  /// Constructs and returns a special tile indicating the remaining ledger books.

  Widget _buildSpecialTile() {
    int remainingLedgerBooks = account.ledgerBooks.length - NUMBER_OF_LEDGER_TO_SHOW;
    if (remainingLedgerBooks < 0) remainingLedgerBooks = 0;

    return ListTile(
      title: Text("+ $remainingLedgerBooks more Ledger books"),
      onTap: () {
        // Implement your action here
      },
    );
  }
}
