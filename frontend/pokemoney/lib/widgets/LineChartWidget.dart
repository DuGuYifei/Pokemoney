import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:pokemoney/model/LincePrice.dart';

class LineChartWidget extends StatelessWidget {
  final List<PricePoint> points;

  const LineChartWidget(this.points, {super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8.0),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 2,
            blurRadius: 3,
            offset: Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        children: [
          const Text(
            'Line chart',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 18.0,
            ),
          ),
          const SizedBox(height: 16.0),
          AspectRatio(
            aspectRatio: 2,
            child: LineChart(LineChartData(lineBarsData: [
              LineChartBarData(
                  spots: points.map((point) => FlSpot(point.x, point.y)).toList(),
                  isCurved: false,
                  dotData: FlDotData(show: true))
            ])),
          ),
        ],
      ),
    );
  }
}
