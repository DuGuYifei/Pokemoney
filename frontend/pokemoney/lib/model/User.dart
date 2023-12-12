class User {
  int? id;
  String username;
  String email;
  String? pictureUrl;
  String? headerPicture;

  User({
    required this.id,
    required this.username,
    required this.email,
    required this.pictureUrl,
    required this.headerPicture,
  });

  User.usernameAndEmail({
    required this.username,
    required this.email,
  });

  User.idAndUsernameAndEmail({
    required this.id,
    required this.username,
    required this.email,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'username': username,
      'email': email,
    };
  }

  //create for me a fromJson method
  static User fromJson(Map<String, dynamic> json) {
    return User.idAndUsernameAndEmail(
      id: json['id'],
      username: json['username'],
      email: json['email'],
    );
  }

  //   factory User.fromJson(Map<String, dynamic> json) {
  //   return User.usernameAndEmail(
  //     username: json['username'],
  //     email: json['email'],
  //   );
  // }

  Map<String, dynamic> toJson() {
    return {
      'username': username,
      'email': email,
    };
  }
}
