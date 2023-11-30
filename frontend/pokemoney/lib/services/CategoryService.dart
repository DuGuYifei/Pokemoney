import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class CategoryService {
  final DBHelper _dbHelper = DBHelper();

  Future<List<Category>> getAllCategory() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_categories_unsync");
    return result.map((map) => Category.fromMap(map)).toList();
  }

  Future<int> addCategory(Category category) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_categories_unsync", category.toMap());
    return res;
  }

  Future<Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map<String, dynamic>> maps = await dbClient.query(
      't_categories_unsync', // The name of the t_categories_unsync table
      where: 'id = ?',
      whereArgs: [categoryId],
    );

    if (maps.isNotEmpty) {
      // If a category is found, return it as a Category object
      return Category.fromMap(maps.first);
    } else {
      // If no category is found, throw an exception or handle it accordingly
      throw Exception('Category not found for id $categoryId');
    }
  }

  Future<int> updateCategory(Category category) async {
    var dbClient = await _dbHelper.db;
    return await dbClient
        .update("t_categories_unsync", category.toMap(), where: "id = ?", whereArgs: [category.id]);
  }

  Future<int> deleteCategory(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("t_categories_unsync", where: "id = ?", whereArgs: [id]);
  }
}
