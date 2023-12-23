import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';

import 'package:pokemoney/main.dart' as app;
import 'package:pokemoney/pages/barrel.dart';
import 'package:pokemoney/pages/register/VerificationPage.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/services/AuthService.dart';
import 'package:pokemoney/services/SecureStorage.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/services/database_helper.dart';

import 'mock_authProvider.dart';

void main() {
// Initialize sqflite for FFI support
  sqfliteFfiInit();
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  const String apiBaseUrl = "http://43.131.33.18";
  
  // Use an in-memory database for tests
  databaseFactory = databaseFactoryFfi;

  //DBHelper().useInMemoryDatabase = true;

// Create a mock instance
  var mockAuthProvider = MockAuthProvider();

  // Custom widget for testing
  Widget createTestWidgetSignUp() {
    return MaterialApp(
      home: ChangeNotifierProvider<AuthProvider>.value(
        value: mockAuthProvider,
        child: const SignUpPage(),
      ),
    );
  }

  group('User Registration Tests', () {
    testWidgets('Successful user registration', (WidgetTester tester) async {
      await tester.pumpWidget(createTestWidgetSignUp());
      await tester.pumpAndSettle();

      // Fill in the registration details
      await tester.enterText(find.byKey(Key('usernameField')), 'testuser');
      await tester.enterText(find.byKey(Key('emailField')), 'testuser@example.com');
      await tester.enterText(find.byKey(Key('passwordField')), 'password123');
      await tester.enterText(find.byKey(Key('confirmPasswordField')), 'password123');

      // Tap on the 'agree to terms' checkbox
      await tester.ensureVisible(find.byKey(Key('termsCheckbox')));
      await tester.tap(find.byKey(Key('termsCheckbox')));
      await tester.pumpAndSettle();

      // Submit the form
      await tester.tap(find.byKey(Key('signUpButton')));
      await tester.pumpAndSettle();


      // Check for navigation to the VerificationPage or a success message
      expect(find.byType(VerificationPage), findsOneWidget);
    });

    // Additional test cases can be added here
  });
}
