import 'package:flutter/material.dart';
import 'package:pokemoney/constants/AppColors.dart';

class CheckboxExample extends StatefulWidget {
  final String? clickableText;
  final TextStyle? clickableTextStyle;
  final Function(bool?)? onTapClickableText;

  const CheckboxExample({
    Key? key,
    this.clickableText,
    this.clickableTextStyle,
    this.onTapClickableText,
  }) : super(key: key);

  @override
  _CheckboxExampleState createState() => _CheckboxExampleState();
}

class _CheckboxExampleState extends State<CheckboxExample> {
  bool isChecked = false;

  @override
  Widget build(BuildContext context) {
    Color getColor(Set<MaterialState> states) {
      const Set<MaterialState> interactiveStates = <MaterialState>{
        MaterialState.pressed,
        MaterialState.hovered,
        MaterialState.focused,
      };
      if (states.any(interactiveStates.contains)) {
        return AppColors.primaryColor;
      }
      return AppColors.textPrimary;
    }

    final customClickableText = null; //= widget.clickableText != null
    //     ? CustomClickableText(
    //         text: 'I agree to the ${widget.clickableText}',
    //         onTap: widget.onTapClickableText != null
    //             ? (isChecked) => widget.onTapClickableText!(isChecked)
    //             : null,
    //         clickableStyle: widget.clickableTextStyle ??
    //             TextStyle(
    //               decoration: TextDecoration.underline,
    //               color: Colors.blue,
    //             ),
    //         clickableText: widget.clickableText!,
    //       )
    //     : null;

    return Row(
      children: [
        Checkbox(
          checkColor: Colors.white,
          fillColor: MaterialStateProperty.resolveWith(getColor),
          value: isChecked,
          onChanged: (bool? value) {
            setState(() {
              isChecked = value ?? false;
            });
          },
        ),
        if (customClickableText != null) customClickableText,
      ],
    );
  }
}
