import 'package:flutter/material.dart';
import 'package:path/path.dart';
import 'package:pokemoney/RouteGenerator.dart';
import 'package:pokemoney/constants/AppColors.dart';

class WelcomeScreen extends StatefulWidget {
  @override
  _WelcomeScreenState createState() => _WelcomeScreenState();
}

class _WelcomeScreenState extends State<WelcomeScreen> {
  final List<Map<String, dynamic>> items = [
    {
      'image': 'assets/Brand icon.png',
      'header': 'Welcome to Pokemoney',
      'description': 'Pokemoney make your money tracking easy',
      'showButton': false,
    },
    {
      'image': 'assets/welcome_fox.png',
      'header': 'Track Your Expenses',
      'description': 'Be aware where your money goes',
      'showButton': false,
    },
    {
      'image': 'assets/pie_line.png',
      'header': 'Get Started',
      'description': 'Sign up to start tracking your expenses',
      'showButton': true, // Show button on the last page
    },
  ];

  double currentPage = 0.0;
  final PageController _pageViewController = PageController();

  @override
  void initState() {
    super.initState();
    _pageViewController.addListener(() {
      setState(() {
        currentPage = _pageViewController.page ?? 0;
      });
    });
  }

  List<Widget> get slides => items.map((item) {
        bool isLastItem = items.indexOf(item) == items.length - 1;

        return Builder(builder: (BuildContext innerContext) {
          return Container(
            padding: EdgeInsets.symmetric(horizontal: 18.0),
            child: Column(
              children: <Widget>[
                SizedBox(height: 150),
                ClipRRect(
                  borderRadius: BorderRadius.circular(24.0),
                  child: Image.asset(
                    item['image']!,
                    fit: BoxFit.fitWidth,
                    width: 200.0,
                    alignment: Alignment.bottomCenter,
                  ),
                ),
                SizedBox(height: 40.0),
                Flexible(
                  flex: 1,
                  fit: FlexFit.tight,
                  child: Container(
                    padding: EdgeInsets.symmetric(horizontal: 30.0),
                    child: Column(
                      children: <Widget>[
                        Text(
                          item['header']!,
                          textAlign: TextAlign.center,
                          style: const TextStyle(
                              fontSize: 40.0,
                              fontWeight: FontWeight.w500,
                              color: Color(0XFF3F3D56),
                              height: 1.5),
                        ),
                        SizedBox(height: 20.0),
                        Text(
                          item['description']!,
                          style: const TextStyle(
                              color: AppColors.textSecondary,
                              letterSpacing: 1.2,
                              fontSize: 16.0,
                              height: 1.3),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
                if (isLastItem && item['showButton']) ...[
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all<Color>(AppColors.primaryColor),
                      shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30.0),
                        ),
                      ),
                    ),
                    child: const Text('Sign Up', style: TextStyle(color: Colors.white)),
                    onPressed: () {
                      Navigator.of(innerContext).pushNamed(RouteGenerator.signUpPage);
                    },
                  ),
                  SizedBox(height: 10),
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all<Color>(AppColors.primaryColor),
                      shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30.0),
                        ),
                      ),
                    ),
                    child: const Text('Login', style: TextStyle(color: Colors.white)),
                    onPressed: () {
                      Navigator.of(innerContext).pushNamed(RouteGenerator.loginPage);
                    },
                  ),
                ],
                Spacer(),
              ],
            ),
          );
        });
      }).toList();

  List<Widget> indicator() => List<Widget>.generate(
        slides.length,
        (index) => Container(
          margin: const EdgeInsets.symmetric(horizontal: 3.0),
          height: 10.0,
          width: 10.0,
          decoration: BoxDecoration(
            color: currentPage.round() == index
                ? AppColors.primaryColor
                : AppColors.primaryColor.withOpacity(0.2),
            borderRadius: BorderRadius.circular(10.0),
          ),
        ),
      );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      body: Container(
        child: Stack(
          children: <Widget>[
            PageView.builder(
              controller: _pageViewController,
              itemCount: slides.length,
              itemBuilder: (BuildContext context, int index) {
                return slides[index];
              },
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                margin: EdgeInsets.only(top: 70.0),
                padding: EdgeInsets.symmetric(vertical: 40.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: indicator(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
