package com.pokemoney.hadoop.hbase.enums;

import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;

import java.util.List;

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
    OTHER_EXPENSE(98),
    /**
     * Other income
     */
    OTHER_INCOME(99);

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

    /**
     * get category dto list
     *
     * @return category dto list
     */
    public static List<CategoryDto> getInitCategoryDtoList() {
        return List.of(
                new CategoryDto(1, "Catering and Cuisine"),
                new CategoryDto(2, "Apparel and Grooming"),
                new CategoryDto(3, "Rent and Utilities"),
                new CategoryDto(4, "Transportation"),
                new CategoryDto(5, "Grocery"),
                new CategoryDto(6, "Travel and Hotel"),
                new CategoryDto(7, "Entertainment"),
                new CategoryDto(8, "Hospital and Care"),
                new CategoryDto(9, "Education"),
                new CategoryDto(10, "Pet"),
                new CategoryDto(11, "Business and Invest"),
                new CategoryDto(12, "Charity"),
                new CategoryDto(13, "Digital appliances"),
                new CategoryDto(14, "Salary"),
                new CategoryDto(15, "Bonus"),
                new CategoryDto(16, "Allowance"),
                new CategoryDto(17, "Money Transfer"),
                new CategoryDto(18, "Gift"),
                new CategoryDto(98, "Other expense"),
                new CategoryDto(99, "Other income")
        );
    }

    /**
     * get subcategory dto list
     *
     * @return subcategory dto list
     */
    public static List<SubcategoryDto> getInitSubcategoryList () {
        return List.of(
                new SubcategoryDto(1L, 1, "Catering and Cuisine", 0L, 0),
                new SubcategoryDto(2L, 2, "Apparel and Grooming", 0L, 0),
                new SubcategoryDto(3L, 3, "Rent and Utilities", 0L, 0),
                new SubcategoryDto(4L, 4, "Transportation", 0L, 0),
                new SubcategoryDto(5L, 5, "Grocery", 0L, 0),
                new SubcategoryDto(6L, 6, "Travel and Hotel", 0L, 0),
                new SubcategoryDto(7L, 7, "Entertainment", 0L, 0),
                new SubcategoryDto(8L, 8, "Hospital and Care", 0L, 0),
                new SubcategoryDto(9L, 9, "Education", 0L, 0),
                new SubcategoryDto(10L, 10, "Pet", 0L, 0),
                new SubcategoryDto(11L, 11, "Business and Invest", 0L, 0),
                new SubcategoryDto(12L, 12, "Charity", 0L, 0),
                new SubcategoryDto(13L, 13, "Digital appliances", 0L, 0),
                new SubcategoryDto(14L, 14, "Salary", 0L, 0),
                new SubcategoryDto(15L, 15, "Bonus", 0L, 0),
                new SubcategoryDto(16L, 16, "Allowance", 0L, 0),
                new SubcategoryDto(17L, 17, "Money Transfer", 0L, 0),
                new SubcategoryDto(18L, 18, "Gift", 0L, 0),
                new SubcategoryDto(98L, 98, "Other expense", 0L, 0),
                new SubcategoryDto(99L, 99, "Other income", 0L, 0)
        );
    }
}
