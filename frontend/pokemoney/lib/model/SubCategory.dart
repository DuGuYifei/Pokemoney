class SubCategory {
  final int? id;
  final int categoryId;
  String name;
  final String? iconPath;
  final DateTime? updateAt; // Updated field name
  final int delFlag; // New field

  SubCategory({
    this.id,
    required this.categoryId,
    required this.name,
    this.iconPath, // Add a comma here
    this.updateAt, // Nullable
    this.delFlag = 0, // Default value or required
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'categoryId': categoryId,
      'name': name,
      'iconPath': iconPath,
      'updateAt': updateAt?.toIso8601String(),
      'delFlag': delFlag,
    };
  }

  static SubCategory fromMap(Map<String, dynamic> map) {
    return SubCategory(
      id: map['id'],
      categoryId: map['categoryId'], // Corrected field name
      name: map['name'],
      iconPath: map['iconPath'],
      updateAt: map['updateAt'] != null ? DateTime.parse(map['updateAt']) : null,
      delFlag: map['delFlag'],
    );
  }

  //create for me a fromJson method
  static SubCategory fromJson(Map<String, dynamic> json) {
    return SubCategory(
      id: json['id'],
      categoryId: json['categoryId'], // Corrected field name
      name: json['name'],
      iconPath: json['iconPath'],
      updateAt: json['updateAt'] != null ? DateTime.parse(json['updateAt']) : null,
      delFlag: json['delFlag'],
    );
  }
}
