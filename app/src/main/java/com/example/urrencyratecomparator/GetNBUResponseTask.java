package com.example.urrencyratecomparator;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;

public class GetNBUResponseTask extends AsyncTask<URL, Void, String> {
    private MainActivity context;

    public GetNBUResponseTask(MainActivity context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(URL... urls) {
        publishProgress();

        String response = null;
        try {
            response = Utils.getResponseFromURL(urls[0]);
        } catch (IOException e) { e.printStackTrace(); }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            boolean isEven = false;
            JSONArray jsonResponse = new JSONArray(response);
            TableLayout table = context.findViewById(R.id.table_2);

            for (int i = 0; i < jsonResponse.length(); i++) {
                TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_2_row_template, null);
                double rateVal = jsonResponse.getJSONObject(i).getDouble("rate");
                short units = 1;

                while (rateVal < 1) {
                    rateVal *= 10;
                    units *= 10;
                }

                String rate = rateVal + "";
                rate = Utils.cutTo3AfterPoint(rate);
                String cc = units + " " + jsonResponse.getJSONObject(i).getString("cc");

                ((TextView) row.findViewById(R.id.table_2_currency)).setText(jsonResponse.getJSONObject(i).getString("txt"));
                ((TextView) row.findViewById(R.id.table_2_amount)).setText(rate);
                ((TextView) row.findViewById(R.id.table_2_units)).setText(cc);

                if (!isEven) {
                    row.setBackgroundColor(Color.WHITE);
                } else {
                    row.setBackgroundResource(R.color.greenLight);
                }

                int id = View.generateViewId();
                row.setId(id);
                table.addView(row);
                context.table2RowList.add((TableRow) table.findViewById(id));
                isEven = !isEven;
            }

            MainActivity.progressBar2.setVisibility(View.INVISIBLE);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        MainActivity.progressBar2.setVisibility(View.VISIBLE);
    }
}
