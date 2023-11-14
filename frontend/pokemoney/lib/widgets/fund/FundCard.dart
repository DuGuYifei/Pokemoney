import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:pokemoney/constants/AppColors.dart';
import 'package:pokemoney/model/barrel.dart';
import 'package:pokemoney/pages/barrel.dart';

class FundCard extends StatelessWidget {
  final Fund _fund;
  final bool isClickable;
  const FundCard(this._fund, this.isClickable, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: isClickable
          ? () => Navigator.of(context).push(MaterialPageRoute(builder: (context) => FundDetailsPage(_fund)))
          : null,
      child: Align(
        alignment: const AlignmentDirectional(0.0, 0.0),
        child: SizedBox(
          width: 403.0,
          height: 220.0,
          child: Stack(
            alignment: const AlignmentDirectional(0.0, 0.0),
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(25.0), // Adjust this value to your preference
                child: SvgPicture.asset(
                  'assets/backgorund_credit/card_1.svg',
                  width: 403.0,
                  height: 250.0,
                  fit: BoxFit.fill,
                ),
              ),
              Padding(
                padding: const EdgeInsetsDirectional.fromSTEB(27.0, 0.0, 27.0, 0.0),
                child: Column(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Padding(
                      padding: const EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 0.0, 30.0),
                      child: Row(
                        children: [
                          Column(
                            mainAxisSize: MainAxisSize.max,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'Balance:',
                                style: GoogleFonts.getFont(
                                  'Readex Pro',
                                  color: AppColors.textInfoCard,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 15.0,
                                ),
                              ),
                              Text(
                                '\$${_fund.balance.toString()}',
                                style: GoogleFonts.getFont(
                                  'Readex Pro',
                                  color: AppColors.textInfoCard,
                                  fontWeight: FontWeight.w600,
                                  fontSize: 20.0,
                                ),
                              ),
                            ],
                          ),
                          Expanded(
                            child: Align(
                              alignment: const AlignmentDirectional(1.0, 0.0),
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(8.0),
                                child: Image.asset(
                                  'assets/chip_2.png',
                                  width: 40.0,
                                  height: 40.0,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    Row(
                      mainAxisSize: MainAxisSize.max,
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Padding(
                          padding: const EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 50.0, 0.0),
                          child: Column(
                            mainAxisSize: MainAxisSize.max,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'Creation date:',
                                style: GoogleFonts.getFont(
                                  'Readex Pro',
                                  color: AppColors.textInfoCard,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 15.0,
                                ),
                              ),
                              Text(
                                '${_fund.creationDate.year}-${_fund.creationDate.month}-${_fund.creationDate.day}',
                                style: GoogleFonts.getFont(
                                  'Readex Pro',
                                  color: AppColors.textInfoCard,
                                  fontWeight: FontWeight.w600,
                                  fontSize: 20.0,
                                ),
                              ),
                            ],
                          ),
                        ),
                        Column(
                          mainAxisSize: MainAxisSize.max,
                          children: [
                            Column(
                              mainAxisSize: MainAxisSize.max,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'Funding name:',
                                  style: GoogleFonts.getFont(
                                    'Readex Pro',
                                    color: AppColors.textInfoCard,
                                    fontWeight: FontWeight.w400,
                                    fontSize: 15.0,
                                  ),
                                ),
                                Text(
                                  _fund.name,
                                  style: GoogleFonts.getFont(
                                    'Readex Pro',
                                    color: AppColors.textInfoCard,
                                    fontWeight: FontWeight.w600,
                                    fontSize: 20.0,
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ],
                    ),
                    Align(
                      alignment: const AlignmentDirectional(1.0, 0.0),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(0.0),
                        child: Image.asset(
                          'assets/visa.png',
                          width: 55.0,
                          height: 30.0,
                          fit: BoxFit.contain,
                        ),
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
