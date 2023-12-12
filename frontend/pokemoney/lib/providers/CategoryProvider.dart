import 'package:flutter/foundation.dart';
import 'package:pokemoney/services/CategoryService.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

class CategoryProvider with ChangeNotifier {
  final CategoryService _categoryService = CategoryService();
  List<pokemoney.Category> _category = [];

  List<pokemoney.Category> get categories => _category;

  fetchAllCategory() async {
    _category = await _categoryService.getAllCategory();
    notifyListeners();
  }

  Future<void> fetchAllCategoryFromSyncAndUnsync() async {
    _category = await _categoryService.getAllCategoryFromSyncAndUnsync();
    notifyListeners();
  }

  addCategory(pokemoney.Category category) async {
    await _categoryService.addCategory(category);
    fetchAllCategoryFromSyncAndUnsync();
    print("Category added");
  }

  deleteCategory(int id) async {
    await _categoryService.deleteCategory(id);
    fetchAllCategoryFromSyncAndUnsync();
  }

  updateCategory(pokemoney.Category category) async {
    await _categoryService.updateCategory(category);
    fetchAllCategoryFromSyncAndUnsync(); // Re-fetch the list of categry after updating
  }
}