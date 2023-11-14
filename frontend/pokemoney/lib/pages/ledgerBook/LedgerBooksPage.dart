import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/LedgerBookProvider.dart';

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

  _showForm(BuildContext context) async {
    final formKey = GlobalKey<FormState>();
    final titleController = TextEditingController();
    final descriptionController = TextEditingController();
    final balanceController = TextEditingController();
    final accountIdController = TextEditingController();

    await showDialog(
        context: context,
        builder: (BuildContext context) {
          return Dialog(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 400, maxHeight: 700), // Set your constraints here
              child: SingleChildScrollView(
                child: AlertDialog(
                  title: const Text('Add Transaction'),
                  content: Form(
                    key: formKey,
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        TextFormField(
                          controller: titleController,
                          decoration: const InputDecoration(labelText: "Title"),
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'The title can\'t be null';
                            }
                            return null;
                          },
                        ),
                        TextFormField(
                          controller: descriptionController,
                          decoration: const InputDecoration(labelText: "Description"),
                          // Add validation logic if needed
                        ),
                        TextFormField(
                          controller: balanceController,
                          decoration: const InputDecoration(labelText: "Balance"),
                          keyboardType: TextInputType.number,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Balance is required';
                            }
                            if (double.tryParse(value) == null) {
                              return 'Balance has to be a number';
                            }
                            return null;
                          },
                        ),
                        TextFormField(
                          controller: accountIdController,
                          decoration: const InputDecoration(labelText: "Account ID"),
                          keyboardType: TextInputType.number,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Account ID is required';
                            }
                            if (int.tryParse(value) == null) {
                              return 'Account ID has to be an integer';
                            }
                            return null;
                          },
                        ),
                      ],
                    ),
                  ),
                  actions: [
                    TextButton(
                      onPressed: () {
                        Navigator.of(context).pop(); // This will close the dialog without adding anything
                      },
                      child: const Text('Cancel'),
                    ),
                    ElevatedButton(
                      onPressed: () {
                        if (formKey.currentState!.validate()) {
                          formKey.currentState!.save();

                          // Use the saved values to create a new LedgerBook instance and add it
                          var newLedgerBook = LedgerBook(
                            accountId: int.parse(accountIdController.text),
                            title: titleController.text,
                            description: descriptionController.text,
                            balance: double.parse(balanceController.text),
                            creationDate: DateTime.now(),
                            transactions: [],
                          );

                          context.read<LedgerBookProvider>().addLedgerBook(newLedgerBook);
                          Navigator.of(context).pop();
                        }
                      },
                      child: const Text('Add'),
                    ),
                  ],
                ),
              ),
            ),
          );
        });
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
        onPressed: () => _showForm(context),
        child: const Icon(Icons.add),
      ),
    );
  }
}
