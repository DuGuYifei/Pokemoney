import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';
import 'package:pokemoney/providers/CategoryProvider.dart';
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';
import 'package:pokemoney/providers/SubCategoryProvider.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/services/AuthService.dart';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:pokemoney/constants/ApiConstants.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<FundProvider>(
          create: (_) => FundProvider(),
        ),
        ChangeNotifierProvider<LedgerBookProvider>(
          create: (_) => LedgerBookProvider(),
        ),
        ChangeNotifierProxyProvider2<FundProvider, LedgerBookProvider, TransactionProvider>(
          create: (_) => TransactionProvider(),
          update: (_, fundProvider, ledgerBookProvider, transactionProvider) =>
              transactionProvider!..updateProviders(fundProvider, ledgerBookProvider),
        ),
        ChangeNotifierProvider<CategoryProvider>(
          create: (_) => CategoryProvider(),
        ),
        ChangeNotifierProvider<AuthProvider>(
          create: (_) => AuthProvider(AuthService(apiBaseUrl, SecureStorage()), SecureStorage()),
        ),
        ChangeNotifierProvider<SubCategoryProvider>(
          create: (_) => SubCategoryProvider(),
        ),
      ],
      child: MaterialApp(
        theme: ThemeData(
          useMaterial3: true,
        ),
        debugShowCheckedModeBanner: false,
        initialRoute: RouteGenerator.welcomeScreens,
        onGenerateRoute: RouteGenerator.generateRoute,
      ),
    );
  }
}
