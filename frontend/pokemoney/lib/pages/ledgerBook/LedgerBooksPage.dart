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
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    fetchLedgerBooks();
  }

  void fetchLedgerBooks() async {
    await context.read<LedgerBookProvider>().fetchAllLedgerBooks();
    setState(() => _isLoading = false);
  }

  _showForm(BuildContext context) async {
    final formKey = GlobalKey<FormState>();
    final titleController = TextEditingController();
    final budgetController = TextEditingController();

    budgetController.text = '1000000';

    await showDialog(
        context: context,
        builder: (BuildContext context) {
          return Dialog(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 400, maxHeight: 700), // Set your constraints here
              child: SingleChildScrollView(
                child: AlertDialog(
                  title: const Text('Add ledger book'),
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
                          controller: budgetController,
                          decoration: const InputDecoration(labelText: "Budget"),
                          keyboardType: TextInputType.number,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Budeget is required';
                            }
                            if (double.tryParse(value) == null) {
                              return 'Budeget has to be a number';
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
                            title: titleController.text,
                            budget: double.parse(budgetController.text),
                            creationDate: DateTime.now(),
                            owner: 1, // TODO: Change this to the logged in user's ID
                            editors: '1', // TODO: Change this to the logged in user's ID
                            updateAt: DateTime.now(),
                            delFlag: 0,
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
    return Scaffold(
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : Consumer<LedgerBookProvider>(
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
                        ),
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
