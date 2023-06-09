import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

/// CustomClickableText is a widget that displays text with a clickable word or phrase.
/// Tapping the clickable text triggers an action, such as navigating to another page.
/// The non-clickable and clickable text can be customized with different styles.

class CustomClickableText extends StatelessWidget {
  /// The complete text content.
  final String text;

  /// The callback function that will be executed when the clickable text is tapped.
  final Function onTap;

  /// The style for the non-clickable text.
  final TextStyle textStyle;

  /// The style for the clickable text.
  final TextStyle clickableStyle;

  /// The word or phrase within the text that will be clickable.
  final String clickableText;

  /// Creates a CustomClickableText widget.
  ///
  /// The [text] parameter specifies the complete text content.
  /// The [onTap] parameter is a callback function that will be executed when the clickable text is tapped.
  /// The [textStyle] parameter allows you to customize the style of the non-clickable text.
  /// The [clickableStyle] parameter allows you to customize the style of the clickable text.
  /// The [clickableText] parameter lets you specify the clickable word or phrase within the text.

  const CustomClickableText({
    Key? key,
    required this.text,
    required this.onTap,
    this.textStyle = const TextStyle(
      color: Colors.black,
    ),
    this.clickableStyle = const TextStyle(
      decoration: TextDecoration.underline,
      color: Colors.blue,
    ),
    required this.clickableText,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final textParts = text.split(clickableText);
    final List<TextSpan> spans = [];

    for (var i = 0; i < textParts.length; i++) {
      spans.add(TextSpan(
        text: textParts[i],
        style: textStyle,
      ));

      if (i < textParts.length - 1) {
        spans.add(TextSpan(
          text: clickableText,
          style: clickableStyle,
          recognizer: TapGestureRecognizer()..onTap = onTap as void Function()?,
        ));
      }
    }

    return RichText(
      text: TextSpan(children: spans),
    );
  }
}
