import 'package:flutter/material.dart';
import 'package:pokemoney/app.dart';
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/pages/other/WelcomePage.dart';
import 'package:pokemoney/pages/other/WelcomeScreen.dart';
import 'package:pokemoney/pages/register/VerificationPage.dart';

class RouteGenerator {
  static const String app = '/';
  static const String welcomePage = '/welcome';
  static const String welcomeScreens = '/welcomeScreens';
  static const String homePage = '/home';
  static const String fundsPage = '/funds';
  static const String ledgerBooksPage = '/ledgerBooks';
  static const String accountPage = '/account';
  static const String loginPage = '/login';
  static const String signUpPage = '/signUp';
  static const String varificationPage = '/varification';
  static const String forgotPasswordPage = '/forgotPassword';
  static const String settingsPage = '/settings';
  static const String statsPage = '/stats';
  static const String stylePage = '/style';
  static const String categoryPage = '/category';
  static const String privacySecurityPage = '/privacySecurity';
  static const String helpPage = '/help';

  RouteGenerator._();

  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case app:
        return MaterialPageRoute(
          builder: (_) => const App(),
        );
      case welcomePage:
        return MaterialPageRoute(
          builder: (_) => WelcomePage(),
        );
      case welcomeScreens:
        return MaterialPageRoute(
          builder: (_) => WelcomeScreen(),
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
      case accountPage:
        return MaterialPageRoute(
          builder: (_) => const AccountPage(),
        );
      case loginPage:
        return MaterialPageRoute(
          builder: (_) => const LoginPage(),
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
      case categoryPage:
        return MaterialPageRoute(
          builder: (_) => const CategoryPage(),
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
