// create for me an editor class with the following fields id email and user name 
// and a method to convert the editor to a map and a method to convert a map to an editor
// Compare this snippet from pokemoney/lib/model/Editors.dart:

class Editor {
  final int id;
  final String email;
  final String userName;

  Editor({
    required this.id,
    required this.email,
    required this.userName,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'email': email,
      'userName': userName,
    };
  }

  factory Editor.fromMap(Map<String, dynamic> map) {
    return Editor(
      id: map['id'],
      email: map['email'],
      userName: map['userName'],
    );
  }
}