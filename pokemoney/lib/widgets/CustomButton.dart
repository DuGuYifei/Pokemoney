import 'package:flutter/material.dart';

/// A custom button widget with customizable width, height, and other properties.
class CustomButton extends StatelessWidget {
  final Function()? onTap;
  final double borderRadius;
  final Color textColor;
  final Color backGroundColor;
  final String textButton;
  final double fontSize;
  final FontWeight fontWeight;
  final double width;
  final double height;

  /// Creates a custom button widget.
  ///
  /// The [onTap] parameter defines the callback when the button is pressed.
  /// The [borderRadius] parameter controls the rounded corners of the button.
  /// The [textColor] parameter defines the color of the button text.
  /// The [backGroundColor] parameter defines the background color of the button.
  /// The [textButton] parameter provides the text content of the button.
  /// The [fontSize] parameter sets the font size of the button text.
  /// The [fontWeight] parameter sets the font weight of the button text.
  /// The [width] parameter sets the width of the button.
  /// The [height] parameter sets the height of the button.
  const CustomButton({
    Key? key,
    required this.onTap,
    this.borderRadius = 30.0,
    this.backGroundColor = const Color(0xFF347662),
    this.textColor = Colors.white,
    required this.textButton,
    this.fontSize = 16.0,
    this.fontWeight = FontWeight.bold,
    this.width = 312.0,
    this.height = 45.0,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          color: backGroundColor,
          borderRadius: BorderRadius.circular(borderRadius),
        ),
        child: Center(
          child: Text(
            textButton,
            style: TextStyle(
              color: textColor,
              fontWeight: fontWeight,
              fontSize: fontSize,
            ),
          ),
        ),
      ),
    );
  }
}

extension CustomButtonExtension on CustomButton {
  /// Creates a custom button with a predefined size.
  ///
  /// The [size] parameter sets the width and height of the button.
  CustomButton withSize(double size) {
    return CustomButton(
      onTap: onTap,
      borderRadius: borderRadius,
      backGroundColor: backGroundColor,
      textColor: textColor,
      textButton: textButton,
      fontSize: fontSize,
      fontWeight: fontWeight,
      width: size,
      height: size,
    );
  }
}
