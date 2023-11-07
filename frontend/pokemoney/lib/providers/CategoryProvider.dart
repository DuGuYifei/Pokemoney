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

  addCategory(pokemoney.Category category) async {
    await _categoryService.addCategory(category);
    fetchAllCategory();
    print("Category added");
  }

  deleteCategory(int id) async {
    await _categoryService.deleteCategory(id);
    fetchAllCategory();
  }

  updateCategory(pokemoney.Category category) async {
    await _categoryService.updateCategory(category);
    fetchAllCategory(); // Re-fetch the list of categry after updating
  }
}
