class Transaction {
  final String invoiceNumber;
  final String vendor;
  final String billingDate;
  final double amount;

  Transaction({
    required this.invoiceNumber,
    required this.vendor,
    required this.billingDate,
    required this.amount,
  });
}