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

  // Fetches all ledger books from the provider and updates the loading state
  void fetchLedgerBooks() async {
    await context.read<LedgerBookProvider>().fetchAllLedgerBooks();
    setState(() => _isLoading = false);
  }

  // Shows a form dialog for adding a new ledger book
  Future<void> _showForm(BuildContext context) async {
    final formKey = GlobalKey<FormState>();
    final titleController = TextEditingController();
    final budgetController = TextEditingController()..text = '1000000';

    await showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: const Text('Add ledger book'),
            content: SingleChildScrollView(
              // Wrap content in SingleChildScrollView
              child: ConstrainedBox(
                constraints:
                    const BoxConstraints(maxWidth: 400), // Removed maxHeight to let content define the height
                child: _buildForm(formKey, titleController, budgetController),
              ),
            ),
            actions: _buildFormActions(context, formKey, titleController, budgetController),
          );
        });
  }

  // Builds the form for adding a new ledger book
  Widget _buildForm(GlobalKey<FormState> formKey, TextEditingController titleController,
      TextEditingController budgetController) {
    return Form(
      key: formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          TextFormField(
            controller: titleController,
            decoration: const InputDecoration(labelText: "Title"),
            validator: (value) => value == null || value.isEmpty ? 'The title can\'t be null' : null,
          ),
          TextFormField(
            controller: budgetController,
            decoration: const InputDecoration(labelText: "Budget"),
            keyboardType: TextInputType.number,
            validator: (value) => value == null || value.isEmpty
                ? 'Budget is required'
                : (double.tryParse(value) == null ? 'Budget has to be a number' : null),
          ),
        ],
      ),
    );
  }

  // Builds actions for the form dialog
  List<Widget> _buildFormActions(BuildContext context, GlobalKey<FormState> formKey,
      TextEditingController titleController, TextEditingController budgetController) {
    return [
      TextButton(
        onPressed: () => Navigator.of(context).pop(),
        child: const Text('Cancel'),
      ),
      ElevatedButton(
        onPressed: () {
          if (formKey.currentState!.validate()) {
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
    ];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : Consumer<LedgerBookProvider>(
              builder: (context, ledgerProvider, child) => ledgerProvider.ledgerBooks.isEmpty
                  ? const Center(child: Text('No ledger books yet.'))
                  : _buildLedgerBooksGrid(ledgerProvider),
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showForm(context),
        child: const Icon(Icons.add),
      ),
    );
  }

  // Builds the grid of ledger books
  Widget _buildLedgerBooksGrid(LedgerBookProvider ledgerProvider) {
    return GridView.builder(
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        childAspectRatio: 0.7,
        mainAxisSpacing: 20,
        crossAxisSpacing: 20,
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
            _buildDeleteButton(context, ledgerProvider, ledgerBook),
          ],
        );
      },
    );
  }

  // Builds a delete button for each ledger book
  Widget _buildDeleteButton(BuildContext context, LedgerBookProvider ledgerProvider, LedgerBook ledgerBook) {
    return TextButton(
      onPressed: () {
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            title: const Text("Delete Confirmation"),
            content: const Text("Are you sure you want to delete this ledger book?"),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(ctx).pop(),
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
    );
  }
}
