import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionProvider.dart';
import 'package:pokemoney/widgets/TransactionsLineChart.dart';
import 'package:pokemoney/pages/screens/ledgerBook/TransactionService.dart';
import 'package:provider/provider.dart';

class TransactionsChartPage extends StatefulWidget {
  @override
  _TransactionsChartPageState createState() => _TransactionsChartPageState();
}

class _TransactionsChartPageState extends State<TransactionsChartPage> {
  TimeGrouping _selectedGrouping = TimeGrouping.day; // default selection

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Transaction Chart')),
      body: Column(
        children: [
          // Grouping selection controls
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: _buildGroupingSelection(),
          ),
          // Expanded chart area
          Expanded(
            child: Consumer<TransactionProvider>(
              builder: (context, provider, child) {
                // If the provider is currently fetching data (you could add a boolean flag for this in your provider)
                if (provider.isFetching) {
                  return Center(child: CircularProgressIndicator());
                } else if (provider.aggregatedTransactions.isEmpty) {
                  return Center(child: Text('No data available'));
                }
                // When data is ready, build the chart
                return TransactionsLineChart(
                  aggregatedTransactionsData: provider.aggregatedTransactions,
                  selectedGrouping: _selectedGrouping,
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildGroupingSelection() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: TimeGrouping.values.map((grouping) {
        return Padding(
          padding: const EdgeInsets.symmetric(horizontal: 4.0),
          child: ChoiceChip(
            label: Text(_describeGrouping(grouping)),
            selected: _selectedGrouping == grouping,
            onSelected: (bool selected) {
              setState(() {
                _selectedGrouping = grouping; // update the grouping state
              });
              if (selected) {
                // Fetch or recalculate the data based on the new grouping
                Provider.of<TransactionProvider>(context, listen: false)
                    .aggregateTransactions(grouping);
              }
            },
          ),
        );
      }).toList(),
    );
  }

  String _describeGrouping(TimeGrouping grouping) {
    switch (grouping) {
      case TimeGrouping.day:
        return 'Day';
      case TimeGrouping.week:
        return 'Week';
      case TimeGrouping.month:
        return 'Month';
      default:
        return '';
    }
  }
}

