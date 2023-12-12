import 'package:flutter/material.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';

class CollaboratorSection extends StatelessWidget {
  const CollaboratorSection({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8.0),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 2,
            blurRadius: 3,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Collaborator',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 18.0,
            ),
          ),
          const SizedBox(height: 5.0),
          const CollaboratorTile(
            imageUrl: "assets/lion.jpg",
            name: 'Cameron Williamson',
            role: 'Product Designer',
          ),
          const CollaboratorTile(
            imageUrl: "assets/logo_login.png",
            name: 'Brooklyn Simmons',
            role: 'Software Engineer II',
          ),
          TextButton(
            onPressed: () {
              // Add action for adding a team member
            },
            child: const Text(
              'Add team member',
              style: TextStyle(color: AppColors.textClickable, fontSize: 16.0, fontWeight: FontWeight.bold),
            ),
          )
        ],
      ),
    );
  }
}
