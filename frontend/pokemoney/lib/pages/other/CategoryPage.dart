import 'package:flutter/material.dart';
import 'package:pokemoney/providers/SubCategoryProvider.dart';
import 'package:pokemoney/providers/CategoryProvider.dart';
import 'package:provider/provider.dart';
import 'package:flutter_svg/svg.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/constants/barrel.dart';

class CategoryPage extends StatefulWidget {
  const CategoryPage({super.key});

  @override
  State<CategoryPage> createState() => _CategoryPageState();
}

class _CategoryPageState extends State<CategoryPage> {
  List<Category> categories = [];
  List<SubCategory> subCategories = [];

  @override
  void initState() {
    super.initState();
    // fetch all categories
    Provider.of<CategoryProvider>(context, listen: false).fetchAllCategoryFromSyncAndUnsync();
    Provider.of<SubCategoryProvider>(context, listen: false).fetchAllSubCategoriesFromSyncAndUnsync();

    //categories = Provider.of.CategoryProvider(context, listen: false).categories;
  }

  void _showAddSubCategoryDialog(BuildContext context, int categoryId) {
    TextEditingController _subCategoryNameController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Add Subcategory'),
          content: TextField(
            controller: _subCategoryNameController,
            decoration: const InputDecoration(hintText: "Subcategory Name"),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text('Cancel'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: const Text('Add'),
              onPressed: () {
                // Add logic to add subcategory
                var newSubCategory = SubCategory(
                  // You need to generate an ID for the subcategory or handle it in your database logic
                  categoryId: categoryId,
                  name: _subCategoryNameController.text,
                  updateAt: DateTime.now(),
                  delFlag: 0,
                  iconPath: 'assets/icons/custom_icon.svg',
                );
                Provider.of<SubCategoryProvider>(context, listen: false).addSubCategory(newSubCategory);
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  void _showEditSubCategoryDialog(BuildContext context, SubCategory subCategory) {
    TextEditingController subCategoryNameController = TextEditingController();
    subCategoryNameController.text = subCategory.name;

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Edit Subcategory'),
          content: TextField(
            controller: subCategoryNameController,
            decoration: const InputDecoration(hintText: "Subcategory Name"),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text('Cancel'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: const Text('Update'),
              onPressed: () {
                subCategory.name = subCategoryNameController.text;
                Provider.of<SubCategoryProvider>(context, listen: false).updateSubCategory(subCategory);
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Categories'),
        backgroundColor: AppColors.surfaceContainer,
      ),
      body: Consumer2<CategoryProvider, SubCategoryProvider>(
        builder: (context, categoryProvider, subCategoryProvider, child) {
          return ListView.builder(
            itemCount: categoryProvider.categories.length,
            itemBuilder: (context, index) {
              var category = categoryProvider.categories[index];
              var subCategories = subCategoryProvider.subCategories[category.id] ?? [];

              return ExpansionTile(
                title: Text(category.name),
                leading: ClipRRect(
                  borderRadius: BorderRadius.circular(10), // Adjust the radius as needed
                  child: SvgPicture.asset(
                    category.iconPath,
                    width: 45,
                    height: 45,
                  ),
                ), // Replace with your icon
                children: [
                  ...subCategories
                      .map((subCategory) => ListTile(
                            title: Text(subCategory.name),
                            leading: Padding(
                              padding: const EdgeInsets.only(left: 20),
                              child: subCategory.iconPath != null
                                  ? ClipRRect(
                                      borderRadius: BorderRadius.circular(10), // Adjust the radius as needed
                                      child: SvgPicture.asset(
                                        subCategory.iconPath!,
                                        width: 45,
                                        height: 45,
                                      ),
                                    )
                                  : const Icon(Icons.subdirectory_arrow_right),
                            ),
                            trailing: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                IconButton(
                                  icon: const Icon(Icons.edit),
                                  onPressed: () {
                                    // Implement edit functionality
                                    _showEditSubCategoryDialog(context, subCategory);
                                  },
                                ),
                                // IconButton(
                                //   icon: const Icon(
                                //     Icons.delete,
                                //     color: Colors.red,
                                //   ),
                                //   onPressed: () {
                                //     Provider.of<SubCategoryProvider>(context, listen: false)
                                //         .deleteSubCategory(subCategory.id!);
                                //   },
                                // ),
                              ],
                            ),
                          ))
                      .toList(),
                  ListTile(
                    leading: const Icon(Icons.add),
                    title: const Text('Add Subcategory'),
                    onTap: () => _showAddSubCategoryDialog(context, category.id!),
                  ),
                ],
              );
            },
          );
        },
      ),
    );
  }
}
