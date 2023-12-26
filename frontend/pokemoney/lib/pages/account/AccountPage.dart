import 'package:flutter/material.dart';
import 'package:pokemoney/RouteGenerator.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/AuthProvider.dart';
import 'package:pokemoney/pages/register/LoginPage.dart';
import 'package:pokemoney/pages/register/SignUpPage.dart';

class AccountPage extends StatelessWidget {
  const AccountPage({super.key});

  @override
  Widget build(BuildContext context) {
    final authProvider = context.watch<AuthProvider>();

    return Scaffold(
      body: Center(
        child:
            authProvider.isLoggedIn ? _buildUserDetails(authProvider, context) : _buildLoginPrompt(context),
      ),
    );
  }

  Widget _buildUserDetails(AuthProvider authProvider, BuildContext context) {
    final user = authProvider.currentUser;

    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Spacer(),
        Text('Welcome, ${user?.username ?? 'User'}'),
        Text('Email: ${user?.email ?? 'Not available'}'),
        // Add more user details here
        const Spacer(),
        ElevatedButton.icon(
          icon: const Icon(Icons.logout),
          style: ButtonStyle(
            // make the size of the button bigger and add a login out icon to the left
            minimumSize: MaterialStateProperty.all(const Size(200, 50)),
          ),
          onPressed: () {
            authProvider.logout();
            Navigator.of(context).pushNamed(RouteGenerator.loginPage);
          },
          label: const Text('Log Out'),
        ),
        const SizedBox(height: 20)
      ],
    );
  }

  Widget _buildLoginPrompt(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Spacer(),
        const Text('Your are not logged in', style: TextStyle(fontSize: 20)),
        const Spacer(),
        ElevatedButton(
          key: const Key('loginButton'),
          style: ButtonStyle(
            // make the size of the button bigger
            minimumSize: MaterialStateProperty.all(const Size(150, 40)),
          ),
          onPressed: () {
            Navigator.of(context).push(MaterialPageRoute(builder: (context) => const LoginPage()));
          },
          child: const Text('Login', style: TextStyle(fontSize: 16)),
        ),
        ElevatedButton(
          key: const Key('registerButton'),
          style: ButtonStyle(
            // make the size of the button bigger
            minimumSize: MaterialStateProperty.all(const Size(150, 40)),
          ),
          onPressed: () {
            Navigator.of(context).push(MaterialPageRoute(builder: (context) => const SignUpPage()));
          },
          child: const Text('Register', style: TextStyle(fontSize: 16)),
        ),
        const SizedBox(height: 10),
      ],
    );
  }
}

    //   List<Widget> accountsCards = accountsList.map((accounts) => AccountCard(accounts)).toList();
    //   return Column(
    //     crossAxisAlignment: CrossAxisAlignment.start,
    //     children: <Widget>[
    //       const Padding(
    //         padding: EdgeInsets.fromLTRB(43, 19, 10, 1),
    //         child: Text(
    //           "Accounts",
    //           style: TextStyle(
    //             fontSize: 26,
    //             fontFamily: 'Roboto',
    //             fontWeight: FontWeight.w700,
    //           ),
    //         ),
    //       ),
    //       Expanded(
    //         // Wrap ListView with Expanded
    //         child: ListView(padding: const EdgeInsets.all(20.0), children: accountsCards),
    //       ),
    //     ],
    //   );