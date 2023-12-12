class Category {
  final int? id;
  final String name;
  final String iconPath; // field to hold the icon name/path

  Category({this.id, required this.name, required this.iconPath});

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'iconPath': iconPath,
    };
  }

  static Category fromMap(Map<String, dynamic> map) {
    return Category(
      id: map['id'],
      name: map['name'],
      iconPath: map['iconPath'],
    );
  }

  //create for me a fromJson method
  static Category fromJson(Map<String, dynamic> json) {
    return Category(
      id: json['id'],
      name: json['name'],
      iconPath: json['iconPath'],
    );
  }
}