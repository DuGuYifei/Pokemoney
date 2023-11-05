import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

/// A custom button widget with customizable width, height, and other properties.
class CustomButton extends StatelessWidget {
  final VoidCallback? onPressed;
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
  /// The [onPressed] parameter defines the callback when the button is pressed.
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
    this.onPressed,
    this.borderRadius = 30.0,
    this.backGroundColor = AppColors.buttonPrimary,
    this.textColor = Colors.white,
    required this.textButton,
    this.fontSize = 20.0,
    this.fontWeight = FontWeight.bold,
    this.width = 312.0,
    this.height = 45.0,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: width,
      height: height,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: backGroundColor,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(borderRadius),
          ),
        ),
        child: Text(
          textButton,
          style: TextStyle(
            color: textColor,
            fontWeight: fontWeight,
            fontSize: fontSize,
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
      onPressed: onPressed,
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
