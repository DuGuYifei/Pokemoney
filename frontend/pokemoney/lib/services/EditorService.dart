import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/Editor.dart';

class EditorService {
  final DBHelper _dbHelper = DBHelper();

  Future<List<Editor>> getAllEditors() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("t_editors");
    return result.map((map) => Editor.fromMap(map)).toList();
  }

  Future<int> addEditor(Editor editor) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("t_editors", editor.toMap());
    return res;
  }

  Future<void> upsertEditor(Editor editor) async {
    var dbClient = await _dbHelper.db;
    var existingEditor = await dbClient.query(
      't_editors',
      where: 'userId = ?',
      whereArgs: [editor.userId],
    );

    if (existingEditor.isEmpty) {
      // If editor doesn't exist, insert new editor
      await dbClient.insert('t_editors', editor.toMap());
    } else {
      // If editor exists, update it
      await dbClient.update(
        't_editors',
        editor.toMap(),
        where: 'userId = ?',
        whereArgs: [editor.userId],
      );
    }
  }

  Future<Editor> getEditorById(int editorId) async {
    var dbClient = await _dbHelper.db;
    List<Map<String, dynamic>> maps = await dbClient.query(
      't_editors', // The name of the t_editors table
      where: 'userId = ?',
      whereArgs: [editorId],
    );

    if (maps.isNotEmpty) {
      // If a editor is found, return it as a Editor object
      return Editor.fromMap(maps.first);
    } else {
      // If no editor is found, throw an exception or handle it accordingly
      throw Exception('Editor not found for id $editorId');
    }
  }

  Future<int> updateEditor(Editor editor) async {
    var dbClient = await _dbHelper.db;
    return await dbClient
        .update("t_editors", editor.toMap(), where: "userId = ?", whereArgs: [editor.userId]);
  }

  Future<int> deleteEditor(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("t_editors", where: "userId = ?", whereArgs: [id]);
  }
}
