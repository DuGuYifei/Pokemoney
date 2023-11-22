// ignore_for_file: public_member_api_docs, sort_constructors_first
class Fund {
  int id;
  String name;
  double balance  ;
  DateTime creationDate;
  String type;
  
  Fund({
    required this.id,
    required this.name,
    required this.balance,
    required this.creationDate,
    required this.type,
  });
}
