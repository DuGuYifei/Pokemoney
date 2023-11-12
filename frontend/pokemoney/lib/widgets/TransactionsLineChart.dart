import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart'; // for date formatting
import 'package:pokemoney/pages/screens/ledgerBook/TransactionService.dart';
class TransactionsLineChart extends StatelessWidget {
  final Map<DateTime, double> aggregatedTransactionsData;
  final TimeGrouping selectedGrouping;

  TransactionsLineChart({
    Key? key,
    required this.aggregatedTransactionsData,
    required this.selectedGrouping,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Sort dates to ensure the spots are in order
    final sortedDates = aggregatedTransactionsData.keys.toList()..sort();
    final List<FlSpot> spots = sortedDates.map((date) {
      final x = date.millisecondsSinceEpoch.toDouble(); // X-axis as time in milliseconds
      final y = aggregatedTransactionsData[date]!; // Y-axis as total transaction amount
      return FlSpot(x, y);
    }).toList();

    return LineChart(
      LineChartData(
        minX: spots.first.x, // Define min and max values for the x-axis to avoid auto scaling
        maxX: spots.last.x,
        minY: 0, // Assuming transaction amount should not be negative, start at 0
        // maxY: determined by the max transaction amount or can be set to a fixed value
        lineBarsData: [LineChartBarData(spots: spots)],
        titlesData: FlTitlesData(show: true), // Add titles data if you want to show axis titles
        gridData: FlGridData(show: true), // Show grid lines for better readability
        borderData: FlBorderData(show: true), // Show the border or not
      ),
    );
  }
}
