package com.example.urrencyratecomparator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private final Calendar TODAYS_DATE = Calendar.getInstance();
    private Calendar dateSet;
    private TextView tv_calendar_1;
    private TextView tv_calendar_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.support_action_bar_layout);
        setContentView(R.layout.activity_main);

        tv_calendar_1 = findViewById(R.id.tv_calendar_1);
        tv_calendar_2 = findViewById(R.id.tv_calendar_2);
        tv_calendar_1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_calendar_2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        String chosenDate = dateFormat.format(TODAYS_DATE.getTime());
        tv_calendar_1.setText(chosenDate);
        tv_calendar_2.setText(chosenDate);
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
                    tv_calendar_1.setText(chosenDate);
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
}