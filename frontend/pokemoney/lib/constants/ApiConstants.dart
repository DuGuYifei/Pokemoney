const String apiBaseUrl = "http://43.131.33.18";


const Map<int, String> typeIDToTypeString = {
  1: "income",
  2: "expense",
  3: "receivable",
  4: "payable",
  5: "receivable_backs",
  6: "payable_backs",
};

//create a map from integer to a string of transaction type
const Map<String, int> typeStringToTypeID = {
  "income": 1,
  "expense": 2,
  "receivable": 3,
  "payable": 4,
  "receivable_backs": 5,
  "payable_backs": 6,
};

const listType = [
  "income",
  "expense",
  "receivable",
  "payable",
  "receivable_backs",
  "payable_backs",
];