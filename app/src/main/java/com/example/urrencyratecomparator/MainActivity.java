package com.example.urrencyratecomparator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private URL nbuRatURL;

    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private final Calendar TODAYS_DATE = Calendar.getInstance();
    private Calendar dateSet;
    private TextView tv_calendar_1;
    private TextView tv_calendar_2;
    private ArrayList<TableRow> table1RowList = new ArrayList<>();
    private TableRow table_1_row_1;
    private TableRow table_1_row_2;
    private TableRow table_1_row_3;
    private TableRow table_2_row_1;
    private TableRow table_2_row_2;
    private TableRow table_2_row_3;
    private TableRow table_2_row_4;
    private TableRow table_2_row_5;
    private TableRow table_2_row_6;
    private ScrollView scrollView_table_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.support_action_bar_layout);
        setContentView(R.layout.activity_main);

        tv_calendar_1 = findViewById(R.id.tv_calendar_1);
        tv_calendar_2 = findViewById(R.id.tv_calendar_2);
        table_1_row_1 = findViewById(R.id.table_1_row_1);
        table_1_row_2 = findViewById(R.id.table_1_row_2);
        table_1_row_3 = findViewById(R.id.table_1_row_3);
        table_2_row_1 = findViewById(R.id.table_2_row_1);
        table_2_row_2 = findViewById(R.id.table_2_row_2);
        table_2_row_3 = findViewById(R.id.table_2_row_3);
        table_2_row_4 = findViewById(R.id.table_2_row_4);
        table_2_row_5 = findViewById(R.id.table_2_row_5);
        table_2_row_6 = findViewById(R.id.table_2_row_6);
        TableLayout tableLayout = findViewById(R.id.table_1);
        scrollView_table_2 = findViewById(R.id.scrollView_table_2);

        tv_calendar_1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_calendar_2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        String chosenDate = dateFormat.format(TODAYS_DATE.getTime());
        tv_calendar_1.setText(chosenDate);
        tv_calendar_2.setText(chosenDate);

        try {
            pbRateURL = new URL("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
            nbuRatURL = new URL("https://bank.gov.ua/NBU_Exchange/exchange?json");
        } catch (MalformedURLException e) {e.printStackTrace();}

        new GetPBResponseTask().execute(pbRateURL);
        //new GetNBUResponseTask().execute(nbuRatURL);
    }



    public void onCalendarClick(View view){
        final ImageView imageView = (ImageView) view;

        if (view.getId() == R.id.iv_calendar_icon_1) {
            dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker viewDP, int year, int month, int dayOfMonth) {
                    dateSet = Calendar.getInstance();
                    dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateSet.set(Calendar.MONTH, ++month);
                    dateSet.set(Calendar.YEAR, year);
                    String chosenDate = dateFormat.format(dateSet.getTime());
                    tv_calendar_1.setText(chosenDate);
                    imageView.setBackgroundResource(R.drawable.calendar_icon);
                }
            };
        } else if (view.getId() == R.id.iv_calendar_icon_2) {
            dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dateSet = Calendar.getInstance();
                    dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateSet.set(Calendar.MONTH, ++month);
                    dateSet.set(Calendar.YEAR, year);
                    String chosenDate = dateFormat.format(dateSet.getTime());
                    tv_calendar_2.setText(chosenDate);
                    imageView.setBackgroundResource(R.drawable.calendar_icon);
                }
            };
        }

        imageView.setBackgroundResource(R.drawable.calendar_icon_pressed);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog calendarDialog = new DatePickerDialog(
                MainActivity.this,
                android.R.style.Theme_Holo_Light_NoActionBar,
                dateSetListener,
                year, month, day);

        calendarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        calendarDialog.show();
    }



    public void onCurrency1Click(View tableView) {
        resetRowColors();
        tableView.setBackgroundColor(Color.argb(25, 25, 25, 25));
        View scrollToView;
        switch (tableView.getId()) {
            case R.id.table_1_row_1 :
                scrollToView = findViewById(R.id.table_2_row_1);
                break;
            case R.id.table_1_row_2 :
                scrollToView = findViewById(R.id.table_2_row_2);
                break;
            case R.id.table_1_row_3 :
                scrollToView = findViewById(R.id.table_2_row_6);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tableView.getId());
        }

        scrollView_table_2.smoothScrollTo(0, scrollToView.getTop());
        scrollToView.setBackgroundColor(Color.argb(25, 25, 25, 25));
    }

    private void resetRowColors() {
        table_1_row_1.setBackgroundColor(Color.WHITE);
        table_1_row_2.setBackgroundColor(Color.WHITE);
        table_1_row_3.setBackgroundColor(Color.WHITE);
        table_2_row_1.setBackgroundColor(Color.WHITE);
        table_2_row_2.setBackgroundResource(R.color.greenLight);
        table_2_row_3.setBackgroundColor(Color.WHITE);
        table_2_row_4.setBackgroundResource(R.color.greenLight);
        table_2_row_5.setBackgroundColor(Color.WHITE);
        table_2_row_6.setBackgroundResource(R.color.greenLight);
    }



    class GetPBResponseTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONArray jsonResponse = new JSONArray(response);
                TableLayout table = MainActivity.this.findViewById(R.id.table_1);

                for (int i = 0; i < jsonResponse.length(); i++) {
                    TableRow tableRow = (TableRow) LayoutInflater.from(MainActivity.this).inflate(R.layout.row_template, null);
                    if (tableRow == null) {
                        System.out.println("NULL!");
                    }
                    ((TextView) tableRow.findViewById(R.id.table_1_currency)).setText(jsonResponse.getJSONObject(i).getString("ccy"));
                    ((TextView) tableRow.findViewById(R.id.table_1_buy)).setText(jsonResponse.getJSONObject(i).getString("buy"));
                    ((TextView) tableRow.findViewById(R.id.table_1_sale)).setText(jsonResponse.getJSONObject(i).getString("sale"));
                    table.addView(tableRow);
                }

                JSONObject row1 = jsonResponse.getJSONObject(0);
                JSONObject row2 = jsonResponse.getJSONObject(1);
                JSONObject row3 = jsonResponse.getJSONObject(2);

                TextView table_1_row_1_text_1 = findViewById(R.id.table_1_row_1_text_1);
                TextView table_1_row_1_text_2 = findViewById(R.id.table_1_row_1_text_2);
                TextView table_1_row_1_text_3 = findViewById(R.id.table_1_row_1_text_3);
                table_1_row_1_text_1.setText(row1.getString("ccy"));
                table_1_row_1_text_2.setText(row1.getString("buy").substring(0, 6));
                table_1_row_1_text_3.setText(row1.getString("sale").substring(0, 6));
            } catch (JSONException e) {e.printStackTrace();}
        }
    }

    class GetNBUResponseTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONArray jsonResponse = new JSONArray(response);
                JSONObject row1 = jsonResponse.getJSONObject(0);
                JSONObject row2 = jsonResponse.getJSONObject(1);
                JSONObject row3 = jsonResponse.getJSONObject(2);

                TextView table_1_row_1_text_1 = findViewById(R.id.table_1_row_1_text_1);
                TextView table_1_row_1_text_2 = findViewById(R.id.table_1_row_1_text_2);
                TextView table_1_row_1_text_3 = findViewById(R.id.table_1_row_1_text_3);
                table_1_row_1_text_1.setText(row1.getString("ccy"));
                table_1_row_1_text_2.setText(row1.getString("buy").substring(0, 6));
                table_1_row_1_text_3.setText(row1.getString("sale").substring(0, 6));
            } catch (JSONException e) {e.printStackTrace();}
        }
    }

    public String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        try {
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}