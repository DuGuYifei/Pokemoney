import 'package:pokemoney/services/database_helper.dart';
import 'package:pokemoney/model/barrel.dart' as pokemoney;

enum TimeGrouping { day, week, month }

class TransactionService {
  final DBHelper _dbHelper = DBHelper();

  Future<int> addTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    int res = await dbClient.insert("transactions", transaction.toMap());
    return res;
  }

  Future<List<pokemoney.Transaction>> getAllTransactions() async {
    var dbClient = await _dbHelper.db;
    var result = await dbClient.query("transactions");
    return result.map((map) => pokemoney.Transaction.fromMap(map)).toList();
  }

  Future<int> updateTransaction(pokemoney.Transaction transaction) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.update(
      "transactions",
      transaction.toMap(),
      where: "id = ?",
      whereArgs: [transaction.id],
    );
  }

  Future<int> deleteTransaction(int id) async {
    var dbClient = await _dbHelper.db;
    return await dbClient.delete("transactions", where: "id = ?", whereArgs: [id]);
  }

  Future<pokemoney.Category> getCategoryById(int categoryId) async {
    var dbClient = await _dbHelper.db;
    List<Map> maps = await dbClient.query(
      "categories",
      columns: ["id", "name", "iconPath"],
      where: "id = ?",
      whereArgs: [categoryId],
    );
    if (maps.isNotEmpty) {
      // Cast the map to Map<String, dynamic> before passing it to fromMap
      return pokemoney.Category.fromMap(maps.first.cast<String, dynamic>());
    } else {
      throw Exception('Category not found for id $categoryId');
    }
  }

  Future<Map<DateTime, double>> getTransactionsSumByGrouping(TimeGrouping grouping) async {
    var dbClient = await _dbHelper.db;

    // This query will vary depending on the database schema and the grouping required.
    // The example below is a template to be adjusted based on actual table and column names.
    
    String groupByClause;
    switch (grouping) {
      case TimeGrouping.day:
        groupByClause = "strftime('%Y-%m-%d', billingDate)";
        break;
      case TimeGrouping.week:
        groupByClause = "strftime('%Y-%W', billingDate)";
        break;
      case TimeGrouping.month:
        groupByClause = "strftime('%Y-%m', billingDate)";
        break;
      default:
        throw Exception('Unsupported time grouping');
    }

    String query = '''
      SELECT $groupByClause as period, SUM(amount) as total
      FROM transactions
      GROUP BY period
      ORDER BY period;
    ''';

    List<Map<String, dynamic>> queryResult = await dbClient.rawQuery(query);
    Map<DateTime, double> aggregatedTransactions = {};

    for (var row in queryResult) {
      DateTime period;
      double total = row['total'] as double;

      switch (grouping) {
        case TimeGrouping.day:
          period = DateTime.parse(row['period'] + "T00:00:00");
          break;
        case TimeGrouping.week:
          // Use the first day of the week (Monday) for the period's date
          List<String> yearWeek = row['period'].split('-');
          int year = int.parse(yearWeek[0]);
          int week = int.parse(yearWeek[1]);
          period = DateUtils.getFirstDayOfWeek(year, week);
          break;
        case TimeGrouping.month:
          period = DateTime.parse(row['period'] + "-01T00:00:00");
          break;
        default:
          continue;
      }
      aggregatedTransactions[period] = total;
    }

    return aggregatedTransactions;
  }
}

// Utility function to get the first day of the week given a year and week number
class DateUtils {
  static DateTime getFirstDayOfWeek(int year, int week) {
    // DateTime.utc creates a date in the UTC timezone
    DateTime jan1 = DateTime.utc(year);
    // Calculate the number of days to add to get to the first week
    // ISO 8601 specifies the first week as the one with the first Thursday
    int daysToAdd = (week - 1) * 7 - (jan1.weekday - DateTime.monday);
    // Adjust to the start of the week (Monday)
    return jan1.add(Duration(days: daysToAdd));
  }
}
