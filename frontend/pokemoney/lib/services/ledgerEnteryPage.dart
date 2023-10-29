
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/services/ledger_entry_provider.dart';
import 'package:pokemoney/services/LedgerEntry.dart';

class LedgerEntryListPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Ledger Entries'),
      ),
      body: Consumer<LedgerEntryProvider>(
        builder: (context, ledgerProvider, child) {
          if (ledgerProvider.ledgerEntries.isEmpty) {
            return Center(child: Text('No ledger entries yet.'));
          }

          return ListView.builder(
            itemCount: ledgerProvider.ledgerEntries.length,
            itemBuilder: (context, index) {
              final entry = ledgerProvider.ledgerEntries[index];
              return ListTile(
                title: Text(entry.description),
                subtitle: Text(entry.amount.toString()),
                // Add more UI elements as necessary
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Implement addition of a new LedgerEntry, and it'll automatically update the UI
          var newEntry = LedgerEntry(
            amount: 100.0,
            description: 'Test Description',
            category: 'Test Category',
            timestamp: DateTime.now(),
          );
          context.read<LedgerEntryProvider>().addLedgerEntry(newEntry);
        },
        child: Icon(Icons.add),
      ),
    );
  }
}
