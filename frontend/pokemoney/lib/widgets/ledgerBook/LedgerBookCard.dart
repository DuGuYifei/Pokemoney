import 'package:flutter/material.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/pages/barrel.dart';

class LedgerBookCard extends StatelessWidget {
  final LedgerBook _ledgerBook;
  final String? bgImage;
  final bool isClickable;

  const LedgerBookCard(this._ledgerBook, this.bgImage, this.isClickable, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    String balanceText = _ledgerBook.currentBalance != null
        ? '\$${_ledgerBook.currentBalance.toStringAsFixed(2)}'
        : 'Loading...';
    return GestureDetector(
      onTap: isClickable
          ? () => Navigator.of(context)
              .push(MaterialPageRoute(builder: (context) => LedgerBookDetailsPage(_ledgerBook)))
          : null,
      child: Align(
        alignment: const AlignmentDirectional(0.0, 0.0),
        child: Container(
          width: 190.0,
          height: 230.0,
          decoration: BoxDecoration(
            image: DecorationImage(
              fit: BoxFit.cover,
              image: AssetImage(bgImage!),
            ),
            borderRadius: BorderRadius.circular(30.0),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.only(top: 16, left: 20, bottom: 10),
                child: Column(
                  children: [
                    const Text(
                      'Book Name',
                      style: TextStyle(
                        fontFamily: 'Cera Pro',
                        color: Color(0x7FFFFFFF),
                        fontSize: 15.0,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    Text(
                      _ledgerBook.title,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontFamily: 'Manrope',
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, bottom: 10),
                child: Column(
                  children: [
                    const Text(
                      'Balance',
                      style: TextStyle(
                        fontFamily: 'Cera Pro',
                        color: Color(0x7FFFFFFF),
                        fontSize: 15.0,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    Text(
                      balanceText,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontFamily: 'Manrope',
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20, bottom: 10),
                child: Column(
                  children: [
                    const Text(
                      'Initial Balance',
                      style: TextStyle(
                        fontFamily: 'Cera Pro',
                        color: Color(0x7FFFFFFF),
                        fontSize: 15.0,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    Text(
                      '\$${_ledgerBook.initialBalance.toStringAsFixed(2)}',
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontFamily: 'Manrope',
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 20),
                child: Column(
                  children: [
                    const Text(
                      'Creation Date',
                      style: TextStyle(
                        fontFamily: 'Cera Pro',
                        color: Color(0x7FFFFFFF),
                        fontSize: 15.0,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    Text(
                      '${_ledgerBook.creationDate.day}/${_ledgerBook.creationDate.month}/${_ledgerBook.creationDate.year}',
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontFamily: 'Manrope',
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
