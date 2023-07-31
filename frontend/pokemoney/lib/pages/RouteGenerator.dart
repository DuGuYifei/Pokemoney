import 'package:flutter/material.dart';
import 'package:pokemoney/pages/barrel.dart';

class RouteGenerator {
  static const String homePage = '/';
  static const String loginPage = '/login';
  static const String signUpPage = '/signUp';
  static const String forgotPasswordPage = 'forgotPassword';
  static const String accountsPage = '/accounts';
  static const String ledgerBooksPage = '/ledgerBooks';
  static const String fundsPage = '/funds';
  static const String settingsPage = '/settings';
  static const String statsPage = '/stats';
  static const String stylePage = '/style';
  static const String privacySecurityPage = '/privacySecurity';
  static const String helpPage = '/help';

  RouteGenerator._() {}

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case homePage:
        return MaterialPageRoute(
          builder: (_) => const HomePage(),
        );
      case loginPage:
        return MaterialPageRoute(
          builder: (_) => LoginPage(),
        );
      case signUpPage:
        return MaterialPageRoute(
          builder: (_) => SignUpPage(),
        );
      case forgotPasswordPage:
        return MaterialPageRoute(
          builder: (_) => ForgotPasswordPage(),
        );
      case accountsPage:
        return MaterialPageRoute(
          builder: (_) => AccountsPage(),
        );
      case ledgerBooksPage:
        return MaterialPageRoute(
          builder: (_) => LedgerBooksPage(),
        );
      case fundsPage:
        return MaterialPageRoute(
          builder: (_) => FundsPage(),
        );
      case settingsPage:
        return MaterialPageRoute(
          builder: (_) => SettingsPage(),
        );
      case statsPage:
        return MaterialPageRoute(
          builder: (_) => StatsPage(),
        );
      case stylePage:
        return MaterialPageRoute(
          builder: (_) => StylePage(),
        );
      case privacySecurityPage:
        return MaterialPageRoute(
          builder: (_) => PrivacySecurityPage(),
        );
      case helpPage:
        return MaterialPageRoute(
          builder: (_) => HelpPage(),
        );
      default:
        throw const FormatException("Route not found");
    }
  }
}

// 5.
class RouteException implements Exception {
  final String message;
  const RouteException(this.message);
}
