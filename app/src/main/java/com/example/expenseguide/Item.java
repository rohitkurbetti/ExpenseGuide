package com.example.expenseguide;

import java.io.Serializable;

public class Item implements Serializable {

    private String expenseCategory;
    private Integer expenseTotal;

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Integer getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Integer expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public Item() {

    }

    public Item(String expenseCategory, Integer expenseTotal) {
        this.expenseCategory = expenseCategory;
        this.expenseTotal = expenseTotal;
    }

    @Override
    public String toString() {
        return "Item{" +
                "expenseCategory='" + expenseCategory + '\'' +
                ", expenseTotal=" + expenseTotal +
                '}';
    }
}
