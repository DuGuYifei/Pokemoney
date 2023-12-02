import 'dart:ffi';

class Fund {
  int? id;
  String name;
  double balance;
  DateTime creationDate;
  int owner; // New field
  String editors; // New field
  DateTime updateAt; // New field
  int delFlag; // New field

  Fund({
    this.id,
    required this.name,
    required this.balance,
    required this.creationDate,
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
      owner: map['owner'],
      editors: map['editors'],
      updateAt: DateTime.parse(map['updateAt']),
      delFlag: map['delFlag'],
    );
  }

  // void test(String editors) {
  //   List<int> editorList = editors.split(',').map((e) => int.parse(e)).toList();
  //   for (int i = 0; i < editorList.length; i++) {
  //     'select * from t_users where id = ?'
  //     if null unknown
  //   }
  //   'select * from t_users where id in (${editorList.map((e) => '?').join(',')})';
  // }
}
