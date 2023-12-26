// create for me an editor class with the following fields id email and user name
// and a method to convert the editor to a map and a method to convert a map to an editor
// Compare this snippet from pokemoney/lib/model/Editors.dart:

class Editor {
  final int userId;
  final String email;
  final String name;

  Editor({
    required this.userId,
    required this.email,
    required this.name,
  });

  Map<String, dynamic> toMap() {
    return {
      'userId': userId,
      'email': email,
      'name': name,
    };
  }

  factory Editor.fromMap(Map<String, dynamic> map) {
    return Editor(
      userId: map['userId'],
      email: map['email'] ?? '',
      name: map['name'] ?? '',
    );
  }
}
