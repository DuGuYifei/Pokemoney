package com.pokemoney.hadoop.hbase.enums;

/**
 * category of transaction
 */
public enum CategoryEnum {
    /**
     * Catering and Cuisine
     */
    CATERING_AND_CUISINE(1),
    /**
     * Apparel and Grooming
     */
    APPAREL_AND_GROOMING(2),
    /**
     * Rent and Utilities
     */
    RENT_AND_UTILITIES(3),
    /**
     * Transportation
     */
    TRANSPORTATION(4),
    /**
     * Grocery
     */
    GROCERY(5),
    /**
     * Travel and Hotel
     */
    TRAVEL_AND_HOTEL(6),
    /**
     * Entertainment
     */
    ENTERTAINMENT(7),
    /**
     * Hospital and Care
     */
    HOSPITAL_AND_CARE(8),
    /**
     * Education
     */
    EDUCATION(9),
    /**
     * Pet
     */
    PET(10),
    /**
     * Business and Invest
     */
    BUSINESS_AND_INVEST(11),
    /**
     * Charity
     */
    CHARITY(12),
    /**
     * Digital appliances
     */
    DIGITAL_APPLIANCES(13),
    /**
     * Salary
     */
    SALARY(14),
    /**
     * Bonus
     */
    BONUS(15),
    /**
     * Allowance
     */
    ALLOWANCE(16),
    /**
     * Money Transfer
     */
    MONEY_TRANSFER(17),
    /**
     * Gift
     */
    GIFT(18),
    /**
     * Other expense
     */
    OTHER_EXPENSE(101),
    /**
     * Other income
     */
    OTHER_INCOME(102);

    /**
     * value
     */
    private final int value;

    /**
     * constructor
     *
     * @param value value
     */
    CategoryEnum(int value) {
        this.value = value;
    }

    /**
     * get value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }
}
