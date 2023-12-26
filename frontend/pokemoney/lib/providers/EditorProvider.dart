// creating the editors provider
import 'package:flutter/material.dart';
import 'package:pokemoney/services/EditorService.dart';
import 'package:pokemoney/model/Editor.dart';

class EditorProvider with ChangeNotifier {
  final EditorService _editorService = EditorService();
  Map<int, String> _editorNamesCache = {};

  List<Editor> _editors = [];
  List<Editor> get editors => _editors;

  Future<void> fetchAllEditors() async {
    _editors = await _editorService.getAllEditors();
    notifyListeners();
  }

//get and editor with id
  Future<Editor> getEditorById(int editorId) async {
    var editor = await _editorService.getEditorById(editorId);
    return editor;
  }

  Future<void> addEditor(Editor editor) async {
    await _editorService.addEditor(editor);
    fetchAllEditors();
    print("Editor added");
  }

  Future<void> deleteEditor(int id) async {
    await _editorService.deleteEditor(id);
    fetchAllEditors();
  }

  Future<void> updateEditor(Editor editor) async {
    await _editorService.updateEditor(editor);
    fetchAllEditors(); // Re-fetch the list of editors after updating
  }

  Future<void> cacheEditorNames() async {
    _editors = await _editorService.getAllEditors();
    _editorNamesCache = {for (var editor in _editors) editor.userId: editor.name};
    notifyListeners();
  }

Future<String> getEditorNameById(int editorId) async {
    try {
      Editor editor = await _editorService.getEditorById(editorId);
      return editor.name;
    } catch (e) {
      return 'Unknown'; // If the editor is not found, return 'Unknown'
    }
  }

  // String getEditorNameById(int editorId) {
  
  //   return _editorNamesCache[editorId] ?? 'Unknown';
  // }

  // Future<String> getEditorNameById(int editorId) async {
  //   try {
  //     Editor editor = await _editorService.getEditorById(editorId);
  //     return editor.userName;
  //   } catch (e) {
  //     return 'Unknown'; // If the editor is not found, return 'Unknown'
  //   }
  // }
}
