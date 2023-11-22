import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';

/// A stateless widget that provides a container to display a list of alerts.
///
/// The container presents alerts associated with an [Account] and provides
/// functionality to view individual alerts. It also has a special tile to indicate
/// alerts that aren't directly shown due to space constraints.
///
/// This container shows a maximum of [NUMBER_OF_ALERTS_TO_SHOW] alerts and provides
/// a mechanism to view all the alerts if required.
class AlertsContainer extends StatelessWidget {
  /// The [User] associated with this widget.
  final User account;

  /// A constant that defines the number of alerts to display.
  static const int NUMBER_OF_ALERTS_TO_SHOW = 5;

  /// A constant that defines the height of the alerts container.
  static const double HIGHT_OF_CONTAINER = 300.0;

  /// Constructs the [AlertsContainer] widget.
  ///
  /// Takes in an [Account] and an optional [Key].
  const AlertsContainer(this.account, {Key? key}) : super(key: key);

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
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        children: [
          _buildHeader(),
          // SizedBox(
          //   height: HIGHT_OF_CONTAINER,
          //   child: ListView.builder(
          //     physics: const BouncingScrollPhysics(),
          //     itemCount: NUMBER_OF_ALERTS_TO_SHOW + 1, // Add 1 for the special tile
          //     itemBuilder: (context, index) {
          //       // If it's the last item, return the special tile
          //       if (index == NUMBER_OF_ALERTS_TO_SHOW) {
          //         return _buildSpecialTile();
          //       }
          //       // Otherwise, return the alert item
          //       return _buildAlertItem(account.alerts[index]);
          //     },
          //   ),
          // ),
        ],
      ),
    );
  }

  /// Builds and returns the header of the alerts container.
  ///
  /// The header comprises the title "Alerts" and a clickable "View all" text.
  Widget _buildHeader() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Row(children: [
        const SizedBox(
          width: 15,
        ),
        const Text("Alerts",
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

  /// Builds and returns an individual alert item.
  ///
  /// The alert item consists of an icon indicating its read status, the alert content,
  /// its type, and a timestamp.
  ///
  /// [alert] - The [Alert] object to be displayed.
  Widget _buildAlertItem(Alert alert) {
    return ListTile(
      leading: Icon(
        alert.isRead ? Icons.markunread_outlined : Icons.mark_email_unread_outlined,
      ),
      title: Text(alert.content),
      subtitle: Text(alert.type),
      trailing: Text(DateFormat('yMMMd').format(alert.timestamp)),
      onTap: () {},
    );
  }

  /// Builds and returns a special tile to indicate the number of alerts that aren't
  /// directly shown due to space constraints.
  ///
  /// If there are more alerts in the account than [NUMBER_OF_ALERTS_TO_SHOW],
  /// this tile indicates how many more alerts are present.
  // Widget _buildSpecialTile() {
  //   int remainingAlerts = account.alerts.length - NUMBER_OF_ALERTS_TO_SHOW;
  //   if (remainingAlerts < 0) remainingAlerts = 0;

  //   return ListTile(
  //     title: Text("+ $remainingAlerts more alerts"),
  //     onTap: () {
  //       // Implement your action here
  //     },
  //   );
  // }
}
