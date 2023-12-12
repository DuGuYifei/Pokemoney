class Alert {
  final int id;          // unique identifier for the alert
  final DateTime timestamp; // when the alert was generated or created
  final String type;        // type of the alert (e.g., 'notification', 'warning', 'reminder', etc.)
  final String content;     // the main content or message of the alert
  bool isRead;              // whether the alert has been read or not

  Alert({
    required this.id,
    required this.timestamp,
    required this.type,
    required this.content,
    this.isRead = false,
  });
}
