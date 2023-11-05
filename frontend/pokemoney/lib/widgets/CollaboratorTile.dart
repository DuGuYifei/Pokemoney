import 'package:flutter/material.dart';

class CollaboratorTile extends StatelessWidget {
  final String imageUrl;
  final String name;
  final String role;

  const CollaboratorTile({super.key, required this.imageUrl, required this.name, required this.role});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: CircleAvatar(
        backgroundImage: AssetImage(imageUrl),
      ),
      title: Text(name),
      subtitle: Text(role),
    );
  }
}
