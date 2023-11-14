import 'package:flutter/material.dart';
import 'package:pokemoney/app.dart';
import 'package:pokemoney/pages/barrel.dart';

class RouteGenerator {
  static const String app = '/';
  static const String homePage = '/home';
  static const String fundsPage = '/funds';
  static const String ledgerBooksPage = '/ledgerBooks';
  static const String accountsPage = '/accounts';
  static const String loginPage = '/login';
  static const String signUpPage = '/signUp';
  static const String forgotPasswordPage = '/forgotPassword';
  static const String settingsPage = '/settings';
  static const String statsPage = '/stats';
  static const String stylePage = '/style';
  static const String privacySecurityPage = '/privacySecurity';
  static const String helpPage = '/help';

  RouteGenerator._();

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case app:
        return MaterialPageRoute(
          builder: (_) => const App(),
        );
      case homePage:
        return MaterialPageRoute(
          builder: (_) => const HomePage(),
        );
      case fundsPage:
        return MaterialPageRoute(
          builder: (_) => const FundsPage(),
        );
      case ledgerBooksPage:
        return MaterialPageRoute(
          builder: (_) => const LedgerBooksPage(),
        );
      case accountsPage:
        return MaterialPageRoute(
          builder: (_) => const AccountsPage(),
        );
      case loginPage:
        return MaterialPageRoute(
          builder: (_) => LoginPage(),
        );
      case signUpPage:
        return MaterialPageRoute(
          builder: (_) => const SignUpPage(),
        );
      case forgotPasswordPage:
        return MaterialPageRoute(
          builder: (_) => ForgotPasswordPage(),
        );
      case settingsPage:
        return MaterialPageRoute(
          builder: (_) => const SettingsPage(),
        );
      case statsPage:
        return MaterialPageRoute(
          builder: (_) => const StatsPage(),
        );
      case stylePage:
        return MaterialPageRoute(
          builder: (_) => const StylePage(),
        );
      case privacySecurityPage:
        return MaterialPageRoute(
          builder: (_) => const PrivacySecurityPage(),
        );
      case helpPage:
        return MaterialPageRoute(
          builder: (_) => const HelpPage(),
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
