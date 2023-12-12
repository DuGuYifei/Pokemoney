import 'package:flutter/material.dart';
import 'package:pokemoney/services/SubCategoryService.dart';
import 'package:pokemoney/model/barrel.dart';

class SubCategoryProvider with ChangeNotifier {
  final SubCategoryService _subCategoryService = SubCategoryService();
  Map<int, List<SubCategory>> _subCategories = {};

  Map<int, List<SubCategory>> get subCategories => _subCategories;

  fetchAllSubCategories() async {
    var allSubCategories = await _subCategoryService.getAllSubCategories();
    _subCategories = _groupByCategory(allSubCategories);
    notifyListeners();
  }
  
  Future<void> fetchAllSubCategoriesFromSyncAndUnsync() async {
    var allSubCategories = await _subCategoryService.getAllSubCategoriesFromSyncAndUnsync();
    _subCategories = _groupByCategory(allSubCategories);
    notifyListeners();
  }

  addSubCategory(SubCategory subCategory) async {
    await _subCategoryService.addSubCategory(subCategory);
    fetchAllSubCategoriesFromSyncAndUnsync();
  }

  Map<int, List<SubCategory>> _groupByCategory(List<SubCategory> subCategories) {
    return { for (var item in subCategories) item.categoryId : subCategories.where((subCategory) => subCategory.categoryId == item.categoryId).toList() };
  }

  //Implementation of the delete method
  deleteSubCategory(int subCategoryId) async {
    await _subCategoryService.deleteSubCategory(subCategoryId);
    fetchAllSubCategoriesFromSyncAndUnsync();
  }

  //Implementation of the update method
  updateSubCategory(SubCategory subCategory) async {
    await _subCategoryService.updateSubCategory(subCategory);
    fetchAllSubCategoriesFromSyncAndUnsync();
  }

  // Get sub category name by id
  String getCategoryNameById(int subCategoryId) {
    var subCategory = _subCategories.values.expand((element) => element).firstWhere((element) => element.id == subCategoryId);
    return subCategory.name;
  }
}
