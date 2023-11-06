class Category {
  final int? id;
  final String name;
  final String iconName; // field to hold the icon name/path

  Category({this.id, required this.name, required this.iconName});

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'iconName': iconName,
    };
  }

  static Category fromMap(Map<String, dynamic> map) {
    return Category(
      id: map['id'],
      name: map['name'],
      iconName: map['iconName'],
    );
  }
}