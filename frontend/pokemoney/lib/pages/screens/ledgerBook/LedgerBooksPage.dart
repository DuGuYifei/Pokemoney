import 'package:flutter/material.dart';
import 'package:pokemoney/dataExample.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/pages/screens/ledgerBook/LedgerBookProvider.dart';

class LedgerBooksPage extends StatefulWidget {
  const LedgerBooksPage({Key? key}) : super(key: key);

  @override
  _LedgerBooksPageState createState() => _LedgerBooksPageState();
}

class _LedgerBooksPageState extends State<LedgerBooksPage> {
  @override
  void initState() {
    super.initState();
    // Fetch ledger entries when the page is initialized
    context.read<LedgerBookProvider>().fetchAllLedgerBooks();
  }

  @override
  Widget build(BuildContext context) {
    // List<Widget> fundsCards = accountsList
    //     .expand((account) => account.ledgerBooks.map((ledgerBook) =>
    //         LedgerBookCard(ledgerBook, 'assets/backgorund_credit/small_background_creditcard.png', true)))
    //     .toList();
    return Scaffold(
      body: Consumer<LedgerBookProvider>(
        builder: (context, ledgerProvider, child) {
          if (ledgerProvider.ledgerBooks.isEmpty) {
            return const Center(child: Text('No ledger books yet.'));
          }

          return GridView.builder(
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2, // Specifies the number of columns
              childAspectRatio: 0.7, // You might need to adjust this to fit your cards properly
              mainAxisSpacing: 20, // Adjust spacing as needed
              crossAxisSpacing: 20, // Adjust spacing as needed
            ),
            itemCount: ledgerProvider.ledgerBooks.length,
            itemBuilder: (context, index) {
              final ledgerBook = ledgerProvider.ledgerBooks[index];
              return Column(
                children: [
                  LedgerBookCard(
                    ledgerBook,
                    'assets/backgorund_credit/small_background_creditcard.png',
                    true,
                  ),
                  // Optional: Add a delete button under each card.
                  TextButton(
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (ctx) => AlertDialog(
                          title: const Text("Delete Confirmation"),
                          content: const Text("Are you sure you want to delete this ledger book?"),
                          actions: [
                            TextButton(
                              onPressed: () {
                                Navigator.of(ctx).pop();
                              },
                              child: const Text("Cancel"),
                            ),
                            TextButton(
                              onPressed: () {
                                ledgerProvider.deleteLedgerBook(ledgerBook.id!);
                                Navigator.of(ctx).pop();
                              },
                              child: const Text("Delete"),
                            ),
                          ],
                        ),
                      );
                    },
                    child: const Text('Delete'),
                  )
                ],
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          var newLedgerBook = LedgerBook(
              accountId: 2,
              title: "wew",
              description: "description",
              balance: 23,
              creationDate: DateTime(2002, 12, 1),
              transactions: []);
          context.read<LedgerBookProvider>().addLedgerBook(newLedgerBook);
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
