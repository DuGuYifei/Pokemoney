import 'package:pokemoney/model/barrel.dart';

final List<Account> accountsList = [
  Account(
      id: 1,
      accountName: "John's Account",
      type: "Savings",
      pictureUrl: "assets/default_account_picture.jpg",
      headerPicture: "assets/header_image_unsplash.jpg",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "January",
            description: "January Expenses",
            initialBalance: 1500.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "February",
            description: "February Expenses",
            initialBalance: 1200.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
      ],
      alerts: [],
      funds: [
        Fund(id: 1, balance: 200.0, name: "school", creationDate: DateTime(2002, 1, 3), type: 'Personal'),
        Fund(id: 2, balance: 200.0, name: "work", creationDate: DateTime(2005, 5, 15), type: 'Shared')
      ]),
  Account(
      id: 2,
      accountName: "Oso Lensra",
      type: "Personal",
      pictureUrl: "assets/lion.jpg",
      headerPicture: "assets/header_image.png",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "January",
            description: "January Expenses",
            initialBalance: 1500.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "February",
            description: "February Expenses",
            initialBalance: 1200.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "January",
            description: "January Expenses",
            initialBalance: 1500.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "February",
            description: "February Expenses",
            initialBalance: 1200.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "January",
            description: "January Expenses",
            initialBalance: 1500.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "February",
            description: "February Expenses",
            initialBalance: 1200.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "January",
            description: "January Expenses",
            initialBalance: 1500.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "February",
            description: "February Expenses",
            initialBalance: 1200.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
      ],
      alerts: [
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'notification',
            content: 'This is a new notification alert!'),
        Alert(id: 2, timestamp: DateTime.now(), type: 'notification', content: 'You passed the limit!'),
      ],
      funds: [
        Fund(id: 1, balance: 200.0, name: "school", creationDate: DateTime(2002, 1, 3), type: 'Personal'),
        Fund(id: 2, balance: 200.0, name: "work", creationDate: DateTime(2005, 5, 15), type: 'Shared')
      ]),
  Account(
      id: 3,
      accountName: "Nasr Taresh",
      type: "Fmaily",
      pictureUrl: "assets/logo_login.png",
      headerPicture: "assets/header_image.png",
      ledgerBooks: [
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "Work",
            description: "work Expenses",
            initialBalance: 1146.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "Home",
            description: "Home Expenses",
            initialBalance: 900.0,
            creationDate: DateTime(2005, 5, 15),
            // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 1,
            accountId: 1,
            title: "Work",
            description: "work Expenses",
            initialBalance: 1146.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
        LedgerBook(
            id: 2,
            accountId: 1,
            title: "Home",
            description: "Home Expenses",
            initialBalance: 900.0,
            creationDate: DateTime(2005, 5, 15),
                        // transactions: [
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   Transaction(
            //     ledgerBookId: 2,
            //     invoiceNumber: '51468465',
            //     categoryId: 1,
            //     billingDate: DateTime(2002, 3, 1),
            //     amount: 500.00,
            //     type: 'income',
            //   ),
            //   // Add more transactions as needed
            // ]),
        ),
      ],
      alerts: [
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'Saving',
            content: 'You passed the limit!',
            isRead: false),
        Alert(
            id: 2,
            timestamp: DateTime.now(),
            type: ' Fmaily',
            content: 'People added an expense!',
            isRead: true),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'Saving',
            content: 'You passed the limit!',
            isRead: false),
        Alert(
            id: 2,
            timestamp: DateTime.now(),
            type: ' Fmaily',
            content: 'People added an expense!',
            isRead: true),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'Saving',
            content: 'You passed the limit!',
            isRead: false),
        Alert(
            id: 2,
            timestamp: DateTime.now(),
            type: ' Fmaily',
            content: 'People added an expense!',
            isRead: true),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'Saving',
            content: 'You passed the limit!',
            isRead: false),
        Alert(
            id: 2,
            timestamp: DateTime.now(),
            type: ' Fmaily',
            content: 'People added an expense!',
            isRead: true),
        Alert(
            id: 1,
            timestamp: DateTime.now(),
            type: 'Saving',
            content: 'You passed the limit!',
            isRead: false),
        Alert(
            id: 2,
            timestamp: DateTime.now(),
            type: ' Fmaily',
            content: 'People added an expense!',
            isRead: true),
      ],
      funds: [
        Fund(id: 1, balance: 200.0, name: "school", creationDate: DateTime(2002, 1, 3), type: 'Personal'),
        Fund(id: 2, balance: 200.0, name: "work", creationDate: DateTime(2005, 5, 15), type: 'Shared')
      ]),
];
