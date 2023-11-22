import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class CategoryService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addCategory(Category category) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_categories", category.toMap());
    return res;
  }

  Future<List<Category>> getAllCategory() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_categories");
    return result.map((map) => Category.fromMap(map)).toList();
  }

  Future<Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map<String, dynamic>> maps = await dbClient.query(
      't_categories', // The name of the t_categories table
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
    return await dbClient.update("t_categories", category.toMap(), where: "id = ?", whereArgs: [category.id]);
  }

  Future<int> deleteCategory(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("t_categories", where: "id = ?", whereArgs: [id]);
  }
}
