import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class CustomHoverableListTile extends StatefulWidget {
  final VoidCallback onTap;
  final String title;
  final IconData icon;

  const CustomHoverableListTile({
    Key? key,
    required this.onTap,
    required this.title,
    required this.icon,
  }) : super(key: key);

  @override
  _CustomHoverableListTileState createState() =>
      _CustomHoverableListTileState();
}

class _CustomHoverableListTileState extends State<CustomHoverableListTile> {
  bool _isHovered = false;

  @override
  Widget build(BuildContext context) {
    return MouseRegion(
      onEnter: (_) => setState(() => _isHovered = true),
      onExit: (_) => setState(() => _isHovered = false),
      child: ListTile(
        tileColor: _isHovered ? Colors.transparent : null,
        leading: Icon(
          widget.icon,
          color: _isHovered ? Colors.blue : AppColors.tileLists,
        ),
        title: Text(
          widget.title,
          style: TextStyle(
            fontWeight: FontWeight.normal,
            color: _isHovered ? Colors.white : AppColors.tileLists,
          ),
        ),
        onTap: widget.onTap,
      ),
    );
  }
}
