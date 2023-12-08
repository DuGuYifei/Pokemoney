import 'package:mockito/mockito.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/model/User.dart';  // Import if you have a User model

class MockAuthProvider extends Mock implements AuthProvider {
  @override
  bool isLoading = false;  // Simulate isLoading state
  @override
  String? errorMessage;  // Simulate error message
  @override
  User? currentUser;  // Simulate current user, if applicable

  @override
  Future<void> startSignUp(String username, String email, String password) async {
    // Mock the behavior of startSignUp method
    // You can set isLoading, errorMessage, currentUser as needed to simulate different scenarios

    // Example: Simulating successful signup
    isLoading = true;
    try {
      // Simulate network delay
      await Future.delayed(Duration(seconds: 1));
      // Simulate successful signup (set currentUser, reset errorMessage)
      currentUser = User.usernameAndEmail(username: username, email: email); // Assuming you have a User model
      errorMessage = null;
    } catch (e) {
      // Simulate signup error
      errorMessage = 'Signup error: ${e.toString()}';
    } finally {
      isLoading = false;
    }
  }

  // ... override other necessary methods and properties ...
}
