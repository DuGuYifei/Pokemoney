// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/widgets/barrel.dart';
import 'package:provider/provider.dart';
import 'package:pokemoney/providers/FundProvider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class FundsPage extends StatefulWidget {
  const FundsPage({super.key});

  @override
  State<FundsPage> createState() => _FundsPageState();
}

class _FundsPageState extends State<FundsPage> {
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    fetchFunds();
  }

  // Fetches all funds from the provider and updates the loading state
  void fetchFunds() async {
    await context.read<FundProvider>().fetchAllFundsFromSyncAndUnsync();
    setState(() => _isLoading = false);
  }

  Future<void> _showForm(BuildContext context) async {
    final formKey = GlobalKey<FormState>();
    final titleController = TextEditingController();
    final balanceController = TextEditingController()..text = '2000';

    // Retrieve preferences asynchronously before showing the dialog
    final prefs = await SharedPreferences.getInstance();
    int? id = prefs.getInt('id');

    // Now pass the id to _buildFormActions
    List<Widget> actions = _buildFormActions(context, formKey, titleController, balanceController, id);

    await showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: const Text('Add Fund'),
            content: SingleChildScrollView(
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 400),
                child: _buildForm(formKey, titleController, balanceController),
              ),
            ),
            actions: actions,
          );
        });
  }

  // Builds actions for the form dialog
  List<Widget> _buildFormActions(BuildContext context, GlobalKey<FormState> formKey,
      TextEditingController titleController, TextEditingController balanceController, int? id) {
    return [
      TextButton(
        onPressed: () => Navigator.of(context).pop(),
        child: const Text('Cancel'),
      ),
      ElevatedButton(
        onPressed: () {
          if (formKey.currentState!.validate()) {
            // Use the saved values to create a new fund instance and add it
            var newFund = Fund(
              name: titleController.text,
              balance: double.parse(balanceController.text),
              creationDate: DateTime.now(),
              owner: id!,
              editors: id.toString(),
              updateAt: DateTime.now(),
              delFlag: 0,
            );
            context.read<FundProvider>().addFund(newFund);
            Navigator.of(context).pop();
          }
        },
        child: const Text('Add'),
      ),
    ];
  }

  // Builds the form for adding a new fund
  Widget _buildForm(GlobalKey<FormState> formKey, TextEditingController titleController,
      TextEditingController budgetController) {
    return Form(
      key: formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          TextFormField(
            controller: titleController,
            decoration: const InputDecoration(labelText: "Name"),
            validator: (value) => value == null || value.isEmpty ? 'The name can\'t be null' : null,
          ),
          TextFormField(
            controller: budgetController,
            decoration: const InputDecoration(labelText: "Balance"),
            keyboardType: TextInputType.number,
            validator: (value) => value == null || value.isEmpty
                ? 'Budget is required'
                : (double.tryParse(value) == null ? 'Budget has to be a number' : null),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : Consumer<FundProvider>(
              builder: (context, fundProvider, child) => fundProvider.funds.isEmpty
                  ? const Center(child: Text('No funds yet'))
                  : _buildFundGrid(fundProvider),
            ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showForm(context),
        icon: const Icon(Icons.add),
        label: const Text('Funds'),
      ),
    );
  }

  Widget _buildFundGrid(FundProvider fundProvider) {
    return GridView.builder(
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 1,
        childAspectRatio: 1.5,
        mainAxisSpacing: 5,
        crossAxisSpacing: 5,
      ),
      itemCount: fundProvider.funds.length,
      itemBuilder: (context, index) {
        final fund = fundProvider.funds[index];
        return Column(
          children: [
            FundCard(
              fund,
              true,
            ),
            _buildDeleteButton(context, fundProvider, fund),
          ],
        );
      },
    );
  }

  Widget _buildDeleteButton(BuildContext context, FundProvider fundProvider, Fund fund) {
    return TextButton(
      onPressed: () {
        showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            title: const Text("Delete Confirmation"),
            content: const Text("Are you sure you want to delete this fund?"),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(ctx).pop(),
                child: const Text("Cancel"),
              ),
              TextButton(
                onPressed: () {
                  fundProvider.deleteFund(fund.id!);
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
