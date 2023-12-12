import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart';

class SubCategoryService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addSubCategory(SubCategory subCategory) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_subcategories_unsync", subCategory.toMap());
    return res;
  }

  Future<List<SubCategory>> getAllSubCategories() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_subcategories_unsync");
    return result.map((map) => SubCategory.fromMap(map)).toList();
  }
  
  Future<List<SubCategory>> getAllSubCategoriesFromSyncAndUnsync() async {
    var dbClient = await _dbHelper.db;

    // Fetching from unsync table
    var unsyncedResult = await dbClient.query("t_subcategories_unsync");
    var unsyncedSubCategories = unsyncedResult.map((map) => SubCategory.fromMap(map)).toList();

    // Fetching from sync table
    var syncedResult = await dbClient.query("t_subcategories_sync");
    var syncedSubCategories = syncedResult.map((map) => SubCategory.fromMap(map)).toList();

    // Combine both lists, ensuring unique entries (based on ID or other criteria)
    var combinedSubCategories = {...unsyncedSubCategories, ...syncedSubCategories}.toList();
    
    return combinedSubCategories;
  }

  Future<int> deleteSubCategory(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("t_subcategories_unsync", where: "id = ?", whereArgs: [id]);
  }

  Future<SubCategory> getSubCategoryById(int subCategoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      "t_subcategories_unsync",
      columns: ['id', 'categoryId', 'name', 'iconPath', 'updateAt', 'delFlag'],
      where: 'id = ?',
      whereArgs: [subCategoryId],
    );

    if (maps.isNotEmpty) {
      return SubCategory.fromMap(maps.first.cast<String, dynamic>());
    }

    throw Exception('SubCategory with id $subCategoryId not found');
  }

  Future<int> updateSubCategory(SubCategory subCategory) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update(
      "t_subcategories_unsync", 
      subCategory.toMap(), 
      where: "id = ?", 
      whereArgs: [subCategory.id]
    );
  }
}