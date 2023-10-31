import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/pages/screens/ledgerBook/LedgerBookProvider.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<LedgerBookProvider>(
      create: (_) => LedgerBookProvider(),
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
