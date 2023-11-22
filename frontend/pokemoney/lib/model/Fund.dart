class Fund {
  int id;
  String name;
  double balance;
  DateTime creationDate;
  String type;
  int owner; // New field
  String editors; // New field
  DateTime updateAt; // New field
  int delFlag; // New field

  Fund({
    required this.id,
    required this.name,
    required this.balance,
    required this.creationDate,
    required this.type,
    required this.owner,
    required this.editors,
    required this.updateAt,
    required this.delFlag,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'balance': balance,
      'creationDate': creationDate.toIso8601String(),
      'owner': owner,
      'editors': editors,
      'updateAt': updateAt.toIso8601String(),
      'delFlag': delFlag,
    };
  }

  static Fund fromMap(Map<String, dynamic> map) {
    return Fund(
      id: map['id'],
      name: map['name'],
      balance: map['balance'],
      creationDate: DateTime.parse(map['creationDate']),
      type: map['type'],
      owner: map['owner'],
      editors: map['editors'],
      updateAt: DateTime.parse(map['updateAt']),
      delFlag: map['delFlag'],
    );
  }
}
