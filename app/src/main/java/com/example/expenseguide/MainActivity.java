package com.example.expenseguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.expenseguide.database.SqliteDatebaseHelper;
import com.example.expenseguide.utility.CustomSpinnerAdapter;
import com.example.expenseguide.utility.SpinnerItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editTextDateTime;

    TextView textView;
    EditText editTextCostIncurred, editTextSufficientCost, editTextParticulars;
    Spinner spinnerOptions, spinnerReport;
    Button buttonSave;

    SqliteDatebaseHelper dbHelper;
    private String dateStr;

    List<Item> itemsList = new ArrayList<>();
    private double constInc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dbHelper = new SqliteDatebaseHelper(this);
        editTextParticulars = findViewById(R.id.editTextParticulars);
        editTextCostIncurred = findViewById(R.id.editTextCostIncurred);
        editTextSufficientCost = findViewById(R.id.editTextSufficientCost);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        spinnerOptions = findViewById(R.id.spinnerOptions);
        buttonSave = findViewById(R.id.buttonSave);
        textView = findViewById(R.id.textView);
        spinnerReport = findViewById(R.id.spinnerReport);

        List<SpinnerItem> items = new ArrayList<>();
        items.add(new SpinnerItem("Select Options", 0));
        items.add(new SpinnerItem("Housing Expenses", R.drawable.house_to_rent_svgrepo_com));
        items.add(new SpinnerItem("Transportation", R.drawable.ground_transportation_svgrepo_com));
        items.add(new SpinnerItem("Food", R.drawable.meal_easter_svgrepo_com));
        items.add(new SpinnerItem("Healthcare", R.drawable.healthcare_hospital_medical_9_svgrepo_com));

        items.add(new SpinnerItem("Debt Payments", R.drawable.money_svgrepo_com__1_));
        items.add(new SpinnerItem("Entertainment", R.drawable.entertainment_svgrepo_com));
        items.add(new SpinnerItem("Savings and Investments", R.drawable.piggybank_pig_svgrepo_com));

        items.add(new SpinnerItem("Clothing and Personal Care", R.drawable.clothes_clothing_formal_wear_svgrepo_com));
        items.add(new SpinnerItem("Education", R.drawable.education_graduation_learning_school_study_svgrepo_com));
        items.add(new SpinnerItem("Charity and Gifts", R.drawable.loving_charity_svgrepo_com));
        items.add(new SpinnerItem("Travel", R.drawable.travel_svgrepo_com__1_));
        items.add(new SpinnerItem("Insurance", R.drawable.employee_svgrepo_com));
        items.add(new SpinnerItem("Childcare and Education", R.drawable.woman_pushing_stroller_svgrepo_com));
        items.add(new SpinnerItem("Miscellaneous", R.drawable.notebook_miscellaneous_svgrepo_com));

//        items.add(new SpinnerItem("Item 2", R.drawable.icon2));
        // Add more items as needed

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("myList", (Serializable) itemsList);
                intent.putExtra("grandTotal", constInc);
                startActivity(intent);

                return false;
            }
        });

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, items);
        spinnerOptions.setAdapter(adapter);

        editTextDateTime = findViewById(R.id.editTextDateTime);

        editTextDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date and time
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Open date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set selected date to the calendar instance
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                // Open time picker dialog after selecting a date
                                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Set selected time to the calendar instance
                                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                calendar.set(Calendar.MINUTE, minute);

                                                // Format date to "yyyy-MM-dd HH:mm"
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                                                String formattedDate = dateFormat.format(calendar.getTime());
                                                dateStr = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());

                                                // Update the EditText with the selected date and time
                                                editTextDateTime.setText(formattedDate);
                                            }
                                        }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerItem spinnerItem =  items.get(spinnerOptions.getSelectedItemPosition());
                String category = spinnerItem.getText();
                String constInc = String.valueOf(editTextCostIncurred.getText());
                String suffCost = String.valueOf(editTextSufficientCost.getText());

                double costIncurred = Double.parseDouble(constInc.isEmpty() ? "0.0" :constInc);
                double sufficientCost = Double.parseDouble(suffCost.isEmpty() ? "0.0" : suffCost);
                String dateTime = editTextDateTime.getText().toString();
                String particulars = String.valueOf(editTextParticulars.getText());
                if (!category.isEmpty() && !category.equalsIgnoreCase("Select Options")
                        && !category.equals("0") && costIncurred !=0.0d && sufficientCost != 0.0d
                        && costIncurred >= 0 && sufficientCost >= 0 && !dateTime.isEmpty()) {
                    double costDifference = sufficientCost - costIncurred;
                    boolean isInserted = dbHelper.insertData(category, particulars, costIncurred, sufficientCost, costDifference, dateTime, dateStr);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        resetFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });








        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String timeSpan = String.valueOf(adapterView.getSelectedItem());
                switch (timeSpan){
                    case "Last Month":
                        fetchReport("Last Month");
                        break;
                    case "Last 3 Months":
                        fetchReport("Last 3 Months");
                        break;
                    case "Last 6 Months":
                        fetchReport("Last 6 Months");
                        break;
                    case "Last 1 Year":
                        fetchReport("Last 1 Year");
                        break;
                    case "Select Option":
                        textView.setText(null);
                        break;
                    default:
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if(item.getItemId()== R.id.exportCsv){
                invokeExportCSV();
            } else {
                return super.onOptionsItemSelected(item);
            }
            return true;
    }

    private void invokeExportCSV() {
        File documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String dateFrmtted = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
        String fileName = "TestFile_"+dateFrmtted+".csv";
        // Create a new file in the Documents directory
        File csvFile = new File(documentsDirectory, fileName);

        try {
            // Create a FileWriter object with append mode set to false (overwrite existing content)
            FileWriter writer = new FileWriter(csvFile, false);

            writeHeaders(writer);
            writeBody(writer);

            // Close the FileWriter to release resources
            writer.close();
            Toast.makeText(this, "File saved in Documents", Toast.LENGTH_SHORT).show();

            // If needed, you can also notify the system that a new file has been created
            // MediaScannerConnection.scanFile(context, new String[]{csvFile.toString()}, null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Writer writeHeaders(FileWriter writer) throws IOException {
        String[] headers = {"Srno", "Category", "Amount", "Date"};

        // Write headers to the CSV file
        for (String header : headers) {
            if(header.equalsIgnoreCase("Date")){
                writer.append(header);
            } else {
                writer.append(header).append(",");
            }
        }
        writer.append("\n");
        return writer;
    }

    private FileWriter writeBody(FileWriter writer) throws IOException {

       Cursor cursor = dbHelper.getAllRecords();

       if(cursor.getCount() > 0){
           StringBuilder stringBuilder = new StringBuilder();
           while(cursor.moveToNext()){
               stringBuilder.append(cursor.getInt(0));
               stringBuilder.append(",");
               stringBuilder.append(cursor.getString(1));
               stringBuilder.append(",");
               stringBuilder.append(cursor.getInt(3));
               stringBuilder.append(",");
               stringBuilder.append(cursor.getString(7));
               stringBuilder.append("\n");
           }

           writer.write(stringBuilder.toString());
       }


        return writer;
    }

    private void fetchReport(String timeSpan) {

        if(timeSpan.equalsIgnoreCase("Last Month")){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String endDate = year +"/"+ (month+1)+"/"+ day;
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            String startDate = calendar.get(Calendar.YEAR) +"/"+ String.format("%02d", (calendar.get(Calendar.MONTH)+1))+"/"+
                    calendar.get(Calendar.DAY_OF_MONTH);

            getReports(startDate, endDate);
        }

        if(timeSpan.equalsIgnoreCase("Last 3 Months")){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String endDate = year +"/"+ (month+1)+"/"+ day;
            calendar.add(Calendar.DAY_OF_MONTH, -90);
            String startDate = calendar.get(Calendar.YEAR) +"/"+ String.format("%02d", (calendar.get(Calendar.MONTH)+1))+"/"+
                    calendar.get(Calendar.DAY_OF_MONTH);

            getReports(startDate, endDate);
        }

        if(timeSpan.equalsIgnoreCase("Last 6 Months")){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String endDate = year +"/"+ (month+1)+"/"+ day;
            calendar.add(Calendar.DAY_OF_MONTH, -180);
            String startDate = calendar.get(Calendar.YEAR) +"/"+ String.format("%02d", (calendar.get(Calendar.MONTH)+1))+"/"+
                    calendar.get(Calendar.DAY_OF_MONTH);

            getReports(startDate, endDate);
        }

        if(timeSpan.equalsIgnoreCase("Last 1 Year")){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String endDate = year +"/"+ (month+1)+"/"+ day;
            calendar.add(Calendar.DAY_OF_MONTH, -365);
            String startDate = calendar.get(Calendar.YEAR) +"/"+ String.format("%02d", (calendar.get(Calendar.MONTH)+1))+"/"+
                    calendar.get(Calendar.DAY_OF_MONTH);

            getReports(startDate, endDate);
        }


    }

    private void getReports(String startDate, String endDate) {
        constInc = 0.0d;
        double constDiff = 0.0d;
        Cursor list = dbHelper.fetchReportsDateRange(startDate, endDate);
        Integer food = 0,travel=0,healthcare=0,entertainment=0,debtPayments=0,savingsAndInvestments=0,
                clothingAndPersonalCare=0,education=0,charityAndGifts=0,insurance=0,
                childcareAndEducation=0,miscellaneous=0,transportation=0,housingExpenses=0;
        try {
            StringBuilder sb = new StringBuilder();
            while (list.moveToNext()){
                String row = list.getString(0)+" "+
                        list.getString(1)+" "+
                        list.getInt(3)+" "+
                        list.getInt(4)+" "+
                        list.getInt(5)+" "+
                        list.getString(7);
                if(list.getString(1).equalsIgnoreCase("food")){
                    food += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("travel")) {
                    travel += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("healthcare")) {
                    healthcare += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Transportation")) {
                    transportation += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Debt Payments")) {
                    debtPayments += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Savings and Investments")) {
                    savingsAndInvestments += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Clothing and Personal Care")) {
                    clothingAndPersonalCare += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Education")) {
                    education += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Charity and Gifts")) {
                    charityAndGifts += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Insurance")) {
                    insurance += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Childcare and Education")) {
                    childcareAndEducation += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Miscellaneous")) {
                    miscellaneous += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("entertainment")) {
                    entertainment += list.getInt(3);
                } else if (list.getString(1).equalsIgnoreCase("Housing Expenses")) {
                    housingExpenses += list.getInt(3);
                }
                constInc = constInc + list.getInt(3);
                constDiff = constDiff + list.getInt(5);
                sb.append(row);
                sb.append("\n");
            }
            itemsList.clear();

            itemsList.add(new Item("Food", food));
            itemsList.add(new Item("Travel", travel));
            itemsList.add(new Item("Healthcare", healthcare));
            itemsList.add(new Item("Entertainment", entertainment));
            itemsList.add(new Item("Transportation", transportation));
            itemsList.add(new Item("Debt Payments", debtPayments));
            itemsList.add(new Item("Savings and Investments", savingsAndInvestments));
            itemsList.add(new Item("Clothing and Personal Care", clothingAndPersonalCare));
            itemsList.add(new Item("Education", education));
            itemsList.add(new Item("Charity and Gifts", charityAndGifts));
            itemsList.add(new Item("Insurance", insurance));
            itemsList.add(new Item("Childcare and Education", childcareAndEducation));
            itemsList.add(new Item("Miscellaneous", miscellaneous));
            itemsList.add(new Item("Housing Expenses", housingExpenses));


            sb.append("\n Saved: "+ String.format("%.02f%%",(constDiff/constInc)*100)+"\n");
//            sb.append("\n\n--*--*--*--*--*--*--*--*--*--*--*--"
//                    +"\nExp Details\n\nFood: "+food+ " ("+String.format("%.02f",(food/constInc)*100)+"%)"
//                    +"\nHousing Expenses: "+housingExpenses + " ("+String.format("%.02f",(housingExpenses/constInc)*100)+"%)"
//                    +"\nTravel: "+travel + " ("+String.format("%.02f",(travel/constInc)*100)+"%)"
//                    +"\nHealthcare: "+healthcare+ " ("+String.format("%.02f",(healthcare/constInc)*100)+"%)"
//                    +"\nEntertainment: "+entertainment+ " ("+String.format("%.02f",(entertainment/constInc)*100)+"%)"
//                    +"\nTransportation: "+transportation+ " ("+String.format("%.02f",(transportation/constInc)*100)+"%)"
//                    +"\nDebt Payments: "+debtPayments+ " ("+String.format("%.02f",(debtPayments/constInc)*100)+"%)"
//                    +"\nSavings and Investments: "+savingsAndInvestments+ " ("+String.format("%.02f",(savingsAndInvestments/constInc)*100)+"%)"
//                    +"\nClothing and Personal Care: "+clothingAndPersonalCare+ " ("+String.format("%.02f",(clothingAndPersonalCare/constInc)*100)+"%)"
//                    +"\nEducation: "+education+ " ("+String.format("%.02f",(education/constInc)*100)+"%)"
//                    +"\nCharity and Gifts: "+charityAndGifts+ " ("+String.format("%.02f",(charityAndGifts/constInc)*100)+"%)"
//                    +"\nInsurance: "+insurance+ " ("+String.format("%.02f",(insurance/constInc)*100)+"%)"
//                    +"\nChildcare and Education: "+childcareAndEducation+ " ("+String.format("%.02f",(childcareAndEducation/constInc)*100)+"%)"
//                    +"\nMiscellaneous: "+miscellaneous+ " ("+String.format("%.02f",(miscellaneous/constInc)*100)+"%)"
//                    +"\n--*--*--*--*--*--*--*--*--*--*--*--*--\n\n");
            textView.setText(String.valueOf(sb));
        } finally {
            list.close();
        }


    }

    private void resetFields() {
        spinnerOptions.setSelection(0);
        editTextParticulars.setText(null);
        editTextCostIncurred.setText(null);
        editTextSufficientCost.setText(null);
        editTextDateTime.setText(null);

    }
}