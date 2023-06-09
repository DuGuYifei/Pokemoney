import 'package:flutter/material.dart';

/// A custom square tile widget that displays an image with rounded corners.
class CustomSquareTile extends StatelessWidget {
  final String imagePath;
  final double borderRadius;
  final double imageHeight;
  final double paddingImage;

  /// Creates a custom square tile widget.
  ///
  /// The [imagePath] parameter specifies the path of the image asset.
  /// The [borderRadius] parameter controls the rounded corners of the tile and the image.
  /// The [imageHeight] parameter determines the height of the image.
  /// The [paddingImage] parameter determines the space or distance between the tile and the image
  CustomSquareTile({
    Key? key,
    required this.imagePath,
    this.borderRadius = 16.0,
    this.imageHeight = 40.0,
    this.paddingImage = 20,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(paddingImage),
      decoration: BoxDecoration(
        border: Border.all(color: Colors.white),
        borderRadius: BorderRadius.circular(borderRadius),
        color: Colors.grey[200],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(borderRadius),
        child: Image.asset(
          imagePath,
          height: imageHeight,
        ),
      ),
    );
  }
}
