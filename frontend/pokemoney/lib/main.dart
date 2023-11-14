import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';
import 'package:pokemoney/providers/TransactionProvider.dart';
import 'package:pokemoney/providers/CategoryProvider.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<LedgerBookProvider>(
          create: (_) => LedgerBookProvider(),
        ),
        ChangeNotifierProvider<TransactionProvider>(
          create: (_) => TransactionProvider(),
        ),
        ChangeNotifierProvider<CategoryProvider>(
          create: (_) => CategoryProvider(),
        ),
      ],
      child: MaterialApp(
        theme: ThemeData(
          useMaterial3: true,
        ),
        debugShowCheckedModeBanner: false,
        initialRoute: RouteGenerator.app,
        onGenerateRoute: RouteGenerator.generateRoute,
      ),
    );
  }
}
