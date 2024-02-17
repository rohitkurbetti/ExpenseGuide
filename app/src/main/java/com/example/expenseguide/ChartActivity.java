package com.example.expenseguide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    // Create the object of TextView and PieChart class
    TextView tvHousingExpenses,tvTransportation,tvFood,tvHealthcare,tvDebtPayments,tvEntertainment,
            tvSavingsAndInvestments,tvClothingAndPersonalCare,tvEducation,tvCharityAndGifts,tvTravel,tvInsurance,
            tvChildcareAndEducation,tvMiscellaneous,tvTotal;
    PieChart pieChart;

    List<Item> itemsList;
    private Double grandTotal;
    Map<String, String> colorMap;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        itemsList = (List<Item>) intent.getSerializableExtra("myList");
        grandTotal = (Double) intent.getSerializableExtra("grandTotal");

        tvHousingExpenses = findViewById(R.id.tvHousingExpenses);
        tvTransportation = findViewById(R.id.tvTransportation);
        tvFood = findViewById(R.id.tvFood);
        tvHealthcare = findViewById(R.id.tvHealthcare);
        tvDebtPayments = findViewById(R.id.tvDebtPayments);
        tvEntertainment = findViewById(R.id.tvEntertainment);
        tvSavingsAndInvestments = findViewById(R.id.tvSavingsAndInvestments);
        tvClothingAndPersonalCare = findViewById(R.id.tvClothingAndPersonalCare);

        tvEducation = findViewById(R.id.tvEducation);
        tvCharityAndGifts = findViewById(R.id.tvCharityAndGifts);
        tvTravel = findViewById(R.id.tvTravel);

        tvInsurance = findViewById(R.id.tvInsurance);
        tvChildcareAndEducation = findViewById(R.id.tvChildcareAndEducation);
        tvMiscellaneous = findViewById(R.id.tvMiscellaneous);

        tvTotal = findViewById(R.id.tvTotal);


        pieChart = findViewById(R.id.piechart);

        updatecolorMap();

        updatePieChart(itemsList, pieChart);
        updateExpensePercentages();
        // To animate the pie chart
        pieChart.startAnimation();

        Log.d("MAP COLOR: ",colorMap.toString());

    }

    private void updatecolorMap() {
        colorMap = new HashMap<>();
        colorMap.put("Housing Expenses","#FFA726");
        colorMap.put("Transportation","#66BB6A");
        colorMap.put("Debt Payments","#B266FF");
        colorMap.put("Food","#EF5350");
        colorMap.put("Healthcare","#29B6F6");
        colorMap.put("Entertainment","#CCFFCC");
        colorMap.put("Clothing and Personal Care","#CC6600");
        colorMap.put("Education","#EC47E2");
        colorMap.put("Charity and Gifts","#0D69FF");
        colorMap.put("Travel","#0DFF14");
        colorMap.put("Insurance","#FEF309");
        colorMap.put("Clothing and Personal Care","#CC6600");
        colorMap.put("Miscellaneous","#73C6B6");
        colorMap.put("Savings and Investments","#FF66B2");
        colorMap.put("Childcare and Education","#FF0000");
    }

    private void updateExpensePercentages() {
        if(itemsList.size() > 0) {

            for (Item item : itemsList) {
                double percent = (item.getExpenseTotal() / grandTotal)*100;
                if(item.getExpenseTotal()==0){
                    updateExpensePercentage(item.getExpenseCategory(), item.getExpenseTotal() , "");
                } else {
                    updateExpensePercentage(item.getExpenseCategory(), item.getExpenseTotal() ,String.format("%.02f",percent));
                }
            }
            tvTotal.setText(String.valueOf(grandTotal));




        }
    }

    private void updateExpensePercentage(String expenseCategory, Integer expenseTotal, String percent) {

        switch (expenseCategory) {
            case "Housing Expenses":
                tvHousingExpenses.setText(expenseTotal+" "+ (percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Food":
                tvFood.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Travel":
                tvTravel.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Transportation":
                tvTransportation.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Healthcare":
                tvHealthcare.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Debt Payments":
                tvDebtPayments.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Entertainment":
                tvEntertainment.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Savings and Investments":
                tvSavingsAndInvestments.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Clothing and Personal Care":
                tvClothingAndPersonalCare.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Education":
                tvEducation.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Charity and Gifts":
                tvCharityAndGifts.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Insurance":
                tvInsurance.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Childcare and Education":
                tvChildcareAndEducation.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
            case "Miscellaneous":
                tvMiscellaneous.setText(expenseTotal+" "+(percent.isEmpty()?"":String.format("(%s%%)", percent)));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePieChart(List<Item> itemsList, PieChart pieChart) {
        for (Item currItem: itemsList) {
            pieChart.addPieSlice(
                    new PieModel(
                            currItem.getExpenseCategory(),
                            Integer.parseInt(currItem.getExpenseTotal().toString()),
                            Color.parseColor(colorMap.get(currItem.getExpenseCategory()))
                    )
            );
        }
    }
}