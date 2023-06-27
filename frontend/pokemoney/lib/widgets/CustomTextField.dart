import 'package:flutter/material.dart';

/// A custom text field widget with customizable borders and border radius.
class CustomTextField extends StatelessWidget {
  final String? headerText;
  final TextEditingController controller;
  final String labelText;
  final bool obscureText;

  /// Creates a custom text field widget.
  ///
  /// The [controller] parameter is used to control the text field's content.
  /// The [labelText] parameter provides a hint text to guide the user's input.
  /// The [obscureText] parameter determines whether the text is obscured, such as for password input.
  CustomTextField({
    Key? key,
    this.headerText,
    required this.controller,
    required this.labelText,
    required this.obscureText,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (headerText != null) ...[
          Text(
            headerText!,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 20.0,
            ),
          ),
          const SizedBox(height: 15)
        ],
        Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(10),
          ),
          child: SizedBox(
            width: 312,
            height: 45,
            child: TextField(
              obscureText: obscureText,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                labelText: labelText,
                labelStyle: const TextStyle(
                  color: Colors.black,
                  fontSize: 14,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }
}
