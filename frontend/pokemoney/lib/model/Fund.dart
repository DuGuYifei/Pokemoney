// ignore_for_file: public_member_api_docs, sort_constructors_first
class Fund {
  int id;
  double balance;
  String name;
  DateTime creationDate;
  String type;
  
  Fund({
    required this.id,
    required this.balance,
    required this.name,
    required this.creationDate,
    required this.type,
  });
}
