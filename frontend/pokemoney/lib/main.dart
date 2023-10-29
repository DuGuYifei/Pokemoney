import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';

void main() {
  runApp(
    const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of the application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        useMaterial3: true,
      ),
      debugShowCheckedModeBanner: false,
      initialRoute: RouteGenerator.app,
      onGenerateRoute: RouteGenerator.generateRoute,
    );
  }
}
