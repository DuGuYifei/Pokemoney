import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/AppColors.dart';

/// `HeaderAccountCard` Widget.
///
/// This widget is designed to showcase user account details in an attractive card format.
/// It contains a header image, an avatar image, and text information about the account.
class HeaderAccountCard extends StatelessWidget {
  /// Represents the account details.
  /// It requires an instance of `Account`.
  final User account;

  /// Constructor for `HeaderAccountCard`.
  ///
  /// - `account`: The account data to display.
  /// - `key`: An optional key that uniquely identifies the widget.
  const HeaderAccountCard(this.account, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      surfaceTintColor: AppColors.cardBackgorund,
      child: Stack(
        alignment: Alignment.topCenter,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildHeaderImage(),
              _buildAccountInfo(),
            ],
          ),
          _buildAccountAvatar(),
        ],
      ),
    );
  }

  /// Generates the header image for the account card.
  ClipRRect _buildHeaderImage() {
    return ClipRRect(
      borderRadius: const BorderRadius.only(
        topLeft: Radius.circular(8.0),
        topRight: Radius.circular(8.0),
      ),
      child: Image.asset(
        account.headerPicture,
        fit: BoxFit.cover,
        width: double.infinity,
      ),
    );
  }

  /// Generates the account avatar positioned over the header image.
  Positioned _buildAccountAvatar() {
    return Positioned(
      top: 90,
      left: 20,
      child: CircleAvatar(
        backgroundColor: AppColors.cardBackgorund,
        radius: 38,
        child: CircleAvatar(
          radius: 35,
          backgroundImage: AssetImage(account.pictureUrl),
        ),
      ),
    );
  }

  /// Generates the textual account information.
  Padding _buildAccountInfo() {
    return Padding(
      padding:
          const EdgeInsets.only(left: 110.0, top: 25, bottom: 20, right: 15),
      child: Row(
        children: [
          _accountTextInfo(),
          const Expanded(child: SizedBox.shrink()),
          IconButton(
            icon: const Icon(Icons.edit_outlined),
            onPressed: () {
              // Placeholder for possible actions, such as editing account details.
            },
          ),
        ],
      ),
    );
  }

  /// Returns the column widget containing account name and type.
  Column _accountTextInfo() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(account.accountName,
            style:
                const TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold)),
        const SizedBox(height: 3.0),
        Text(account.email,
            style:
                const TextStyle(fontSize: 14.0, fontWeight: FontWeight.w600)),
      ],
    );
  }
}
