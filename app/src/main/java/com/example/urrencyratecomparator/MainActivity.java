package com.example.urrencyratecomparator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private URL pbRateURL;
    private URL nbuRateURL;

    private DatePickerDialog calendarDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private boolean calendarDialogIsShown = false;
    private DateFormat dateFormatView = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private DateFormat dateFormatPB = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private DateFormat dateFormatNBU = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private final Calendar TODAYS_DATE = Calendar.getInstance();
    private Calendar dateSet;
    private TextView tv_calendar_1;
    private TextView tv_calendar_2;
    private String chosenDateView;
    private String chosenDatePB;
    private String chosenDateNBU;
    private Toast wrongYear;

    public ArrayList<TableRow> table1RowList = new ArrayList<>();
    public ArrayList<TableRow> table2RowList = new ArrayList<>();
    private ScrollView scrollView_table_2;

    public RelativeLayout mainLayout;
    public static ProgressBar progressBar1;
    public static ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.support_action_bar_layout);
        setContentView(R.layout.activity_main);

        tv_calendar_1 = findViewById(R.id.tv_calendar_1);
        tv_calendar_2 = findViewById(R.id.tv_calendar_2);
        scrollView_table_2 = findViewById(R.id.scrollView_table_2);

        tv_calendar_1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_calendar_2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        chosenDateView = dateFormatView.format(TODAYS_DATE.getTime());
        tv_calendar_1.setText(chosenDateView);
        tv_calendar_2.setText(chosenDateView);
        chosenDatePB = dateFormatPB.format(TODAYS_DATE.getTime());
        chosenDateNBU = dateFormatNBU.format(TODAYS_DATE.getTime());
        System.out.println(chosenDatePB + "\n" + chosenDateNBU);

        progressBar1 = findViewById(R.id.table_1_progress_bar);
        progressBar2 = findViewById(R.id.table_2_progress_bar);

        try {
            pbRateURL = new URL("https://api.privatbank.ua/p24api/exchange_rates?json&date=" + chosenDatePB);
            nbuRateURL = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=" + chosenDateNBU + "&json");
        } catch (MalformedURLException e) {e.printStackTrace();}

        new GetPBResponseTask(this).execute(pbRateURL);
        new GetNBUResponseTask(this).execute(nbuRateURL);
    }





    public void onCalendarClick(View view){
        final ImageView imageView = (ImageView) view;

        if (!calendarDialogIsShown){
            calendarDialogIsShown = true;
            if (view.getId() == R.id.iv_calendar_icon_1) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker viewDP, int year, int month, int dayOfMonth) {
                        if (checkYear(year, month, dayOfMonth)) {
                            chosenDateView = dateFormatView.format(dateSet.getTime());
                            chosenDatePB = dateFormatPB.format(dateSet.getTime());

                            tv_calendar_1.setText(chosenDateView);

                            TableLayout table1 = findViewById(R.id.table_1);
                            table1.removeViews(1, table1.getChildCount()-1);

                            try {
                                pbRateURL = new URL("https://api.privatbank.ua/p24api/exchange_rates?json&date=" + chosenDatePB);
                            } catch (MalformedURLException e) {e.printStackTrace();}
                            new GetPBResponseTask(MainActivity.this).execute(pbRateURL);

                            imageView.setBackgroundResource(R.drawable.calendar_icon);
                            calendarDialogIsShown = false;
                        } else {
                            showDataRejection();
                            calendarDialogIsShown = false;
                        }
                    }
                };
            } else if (view.getId() == R.id.iv_calendar_icon_2) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (checkYear(year, month, dayOfMonth)) {
                            chosenDateView = dateFormatView.format(dateSet.getTime());
                            chosenDateNBU = dateFormatNBU.format(dateSet.getTime());

                            tv_calendar_2.setText(chosenDateView);

                            TableLayout table2 = findViewById(R.id.table_2);
                            table2.removeViews(0, table2.getChildCount());

                            try {
                                nbuRateURL = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=" + chosenDateNBU + "&json");
                            } catch (MalformedURLException e) {e.printStackTrace();}
                            new GetNBUResponseTask(MainActivity.this).execute(nbuRateURL);

                            imageView.setBackgroundResource(R.drawable.calendar_icon);
                            calendarDialogIsShown = false;
                        } else {
                            showDataRejection();
                            calendarDialogIsShown = false;
                        }
                    }
                };
            }

            imageView.setBackgroundResource(R.drawable.calendar_icon_pressed);

            calendarDialog = new DatePickerDialog(
                    MainActivity.this,
                    android.R.style.Theme_Holo_Light_NoActionBar,
                    dateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            calendarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            calendarDialog.setCancelable(true);
            calendarDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    imageView.setBackgroundResource(R.drawable.calendar_icon);
                    calendarDialogIsShown = false;
                }
            });
            calendarDialog.show();
        }
    }

    public void onCurrency1Click(View tableView) {
        resetRowColors();
        tableView.setBackgroundColor(Color.argb(25, 25, 25, 25));
        View scrollToView = null;

        for (int i = 0; i < table2RowList.size(); i++) {
            String tableViewCurrency = ((TextView) tableView.findViewById(R.id.table_1_currency)).getText().toString();
            String table2RowCurrency = ((TextView) table2RowList.get(i).findViewById(R.id.table_2_units)).getText().toString();
            if (tableViewCurrency.equals("PLZ"))
                tableViewCurrency = "PLN";
            if (tableViewCurrency.equals(table2RowCurrency.substring(table2RowCurrency.length()-3))) {
                scrollToView = table2RowList.get(i);
            }
        }

        scrollView_table_2.smoothScrollTo(0, scrollToView.getTop());
        scrollToView.setBackgroundColor(Color.argb(25, 25, 25, 25));
    }

    private void resetRowColors() {
        for (int i = 0; i < table1RowList.size(); i++){
            table1RowList.get(i).setBackgroundColor(Color.WHITE);
        }

        for (int i = 0; i < table2RowList.size(); i++) {
            if (i % 2 == 0) {
                table2RowList.get(i).setBackgroundColor(Color.WHITE);
            } else {
                table2RowList.get(i).setBackgroundResource(R.color.greenLight);
            }
        }
    }

    public boolean checkYear (int year, int month, int day) {
        dateSet = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        dateSet.set(year, month, day);
        minDate.set(Calendar.getInstance().get(Calendar.YEAR) - 4, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1);
        maxDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);

        return dateSet.after(minDate) && dateSet.before(maxDate);
    }

    private void showDataRejection() {
        if (dateSet.before(Calendar.getInstance())) {
            wrongYear = Toast.makeText(MainActivity.this, R.string.year_before, Toast.LENGTH_LONG);
        } else {
            wrongYear = Toast.makeText(MainActivity.this, R.string.year_after, Toast.LENGTH_LONG);
        }
        wrongYear.show();
    }
}