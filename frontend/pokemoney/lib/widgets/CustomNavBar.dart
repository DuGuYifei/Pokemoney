import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/widgets/CustomHoverableListTile.dart';
import 'package:url_launcher/url_launcher.dart';

class CustomNavBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: Color.fromRGBO(4, 11, 20, 1),
      child: LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constraints) {
          return Stack(
            children: [
              ListView(
                padding: EdgeInsets.zero,
                children: [
                  SizedBox(
                    height: 284,
                    child: Column(
                      children: [
                        SizedBox(height: 10),
                        GestureDetector(
                          onTap: () {
                            showDialog(
                                context: context,
                                builder: (BuildContext context) {
                                  return Dialog(
                                    child: Container(
                                      width:
                                          MediaQuery.of(context).size.width / 2,
                                      height:
                                          MediaQuery.of(context).size.height /
                                              0.5,
                                      decoration: BoxDecoration(
                                        image: DecorationImage(
                                          image: AssetImage('assets/lion.jpg'),
                                          fit: BoxFit.cover,
                                        ),
                                      ),
                                    ),
                                  );
                                });
                          },
                          child: CircleAvatar(
                            radius: 90.0,
                            backgroundImage: AssetImage('assets/lion.jpg'),
                            backgroundColor: AppColors.buttonSecondary,
                          ),
                        ),
                        SizedBox(height: 10),
                        Text(
                          'Osamah Taresh',
                          style: TextStyle(
                              fontSize: 24,
                              color: Colors.white,
                              fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  ),
                  Divider(
                    height: 20,
                    color: Colors.transparent,
                  ),
                  CustomHoverableListTile(
                    icon: FontAwesomeIcons.book,
                    title: 'Ledger books',
                    onTap: () => Navigator.pushNamed(context, '/homa_page'),
                  ),
                  Divider(
                    height: 15,
                    color: Colors.transparent,
                  ),
                  CustomHoverableListTile(
                    icon: FontAwesomeIcons.creditCard,
                    title: 'Funds',
                    onTap: () => Navigator.pushNamed(context, '/about'),
                  ),
                  Divider(
                    height: 15,
                    color: Colors.transparent,
                  ),
                  CustomHoverableListTile(
                    icon: FontAwesomeIcons.user,
                    title: 'Accoutnts',
                    onTap: () => Navigator.pushNamed(context, '/resume'),
                  ),
                  Divider(
                    height: 15,
                    color: Colors.transparent,
                  ),
                  CustomHoverableListTile(
                    icon: FontAwesomeIcons.pieChart,
                    title: 'Stats',
                    onTap: () => Navigator.pushNamed(context, '/projects'),
                  ),
                  Divider(
                    height: 15,
                    color: Colors.transparent,
                  ),
                  CustomHoverableListTile(
                    icon: FontAwesomeIcons.phone,
                    title: 'Contact',
                    onTap: () => Navigator.pushNamed(context, '/contact'),
                  ),
                  Expanded(child: Container()),
                ],
              ),
              Positioned(
                bottom: 0,
                child: Container(
                  width: constraints.maxWidth,
                  child: ListTile(
                    title: Text(
                      '\u00a9 Copywrite Osamah 2023',
                      style: TextStyle(
                        fontSize: 14,
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        decoration: TextDecoration.none,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}

// class NavBar extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) {
//     return Drawer(
//       backgroundColor: Color.fromRGBO(4, 11, 20, 1),
//       child: ListView(
//         padding: EdgeInsets.zero,
// children: [
//   SizedBox(
//     height: 284,
//     child: Column(
//       children: [
//         SizedBox(height: 10),
//         CircleAvatar(
//           radius: 90.0,
//           backgroundImage: AssetImage('assets/images/osama_krakow.jpg'),
//           backgroundColor: Colors.transparent,
//         ),
//         SizedBox(height: 10),
//         Text(
//           'Osamah Taresh',
//           style: TextStyle(
//               fontSize: 24,
//               color: Colors.white,
//               fontWeight: FontWeight.bold),
//         ),
//         SizedBox(
//           height: 10,
//         ),
//         Row(
//           mainAxisAlignment: MainAxisAlignment.center,
//           children: [
//             IconButton(
//               onPressed: () async {
//                 await goToWebPage("https://github.com/Osama-Nasr");
//               },
//               icon: Icon(FontAwesomeIcons.github, color: Colors.white),
//               hoverColor: Colors.blue,
//               iconSize: 20,
//               style: IconButton.styleFrom(
//                   backgroundColor: Color.fromRGBO(33, 36, 49, 1)),
//             ),
//             SizedBox(
//               width: 15,
//             ),
//             IconButton(
//               onPressed: () async {
//                 await goToWebPage(
//                     "https://www.linkedin.com/in/osamah-taresh-6584b5233/");
//               },
//               icon:
//                   Icon(FontAwesomeIcons.linkedin, color: Colors.white),
//               hoverColor: Colors.blue,
//               iconSize: 20,
//               style: IconButton.styleFrom(
//                   backgroundColor: Color.fromRGBO(33, 36, 49, 1)),
//             )
//           ],
//         )
//       ],
//     ),
//   ),
//   Divider(
//     height: 20,
//     color: Colors.transparent,
//   ),
//   HoverableListTile(
//     icon: FontAwesomeIcons.home,
//     title: 'Home',
//     onTap: () => Navigator.pushNamed(context, '/homa_page'),
//   ),
//   Divider(
//     height: 15,
//     color: Colors.transparent,
//   ),
//   HoverableListTile(
//     icon: FontAwesomeIcons.user,
//     title: 'About',
//     onTap: () => Navigator.pushNamed(context, '/about'),
//   ),
//   Divider(
//     height: 15,
//     color: Colors.transparent,
//   ),
//   HoverableListTile(
//     icon: FontAwesomeIcons.file,
//     title: 'Resume',
//     onTap: () => Navigator.pushNamed(context, '/resume'),
//   ),
//   Divider(
//     height: 15,
//     color: Colors.transparent,
//   ),
//   HoverableListTile(
//     icon: FontAwesomeIcons.server,
//     title: 'Projects',
//     onTap: () => Navigator.pushNamed(context, '/projects'),
//   ),
//   Divider(
//     height: 15,
//     color: Colors.transparent,
//   ),
//   HoverableListTile(
//     icon: FontAwesomeIcons.phone,
//     title: 'Contact',
//     onTap: () => Navigator.pushNamed(context, '/contact'),
//   ),
//   Expanded(child: Container()),
//   ListTile(
//       title: Text(
//     '\u00a9 Copywrite Osamah 2023',
//     style: TextStyle(
//       fontSize: 14,
//       color: Colors.white,
//       fontWeight: FontWeight.bold,
//       decoration: TextDecoration.none,
//     ),
//   )),
// ],
//       ),
//     );
//   }
// }

Future<void> goToWebPage(String urlString) async {
  final Uri _url = Uri.parse(urlString);
  if (!await launchUrl(_url)) {
    throw 'Could not launch $_url';
  }
}
