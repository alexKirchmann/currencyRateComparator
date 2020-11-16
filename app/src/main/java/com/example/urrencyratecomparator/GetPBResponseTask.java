package com.example.urrencyratecomparator;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class GetPBResponseTask extends AsyncTask<URL, Void, String> {
    private MainActivity context;

    public GetPBResponseTask(MainActivity context) {
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
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonResponseArr = jsonResponse.getJSONArray("exchangeRate");
            TableLayout table = context.findViewById(R.id.table_1);
            String buy;
            String sale;

            for (int i = 1; i < jsonResponseArr.length(); i++) {
                try {
                    buy = Utils.cutTo3AfterPoint(jsonResponseArr.getJSONObject(i).getString("purchaseRate"));
                    sale = Utils.cutTo3AfterPoint(jsonResponseArr.getJSONObject(i).getString("saleRate"));

                    TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_1_row_template, null);
                    ((TextView) row.findViewById(R.id.table_1_currency)).setText(jsonResponseArr.getJSONObject(i).getString("currency"));
                    ((TextView) row.findViewById(R.id.table_1_buy)).setText(buy);
                    ((TextView) row.findViewById(R.id.table_1_sale)).setText(sale);

                    int id = View.generateViewId();
                    row.setId(id);
                    table.addView(row);
                    context.table1RowList.add((TableRow) table.findViewById(id));
                } catch (Exception e) { }
            }

            MainActivity.progressBar1.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {e.printStackTrace();}
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        MainActivity.progressBar1.setVisibility(View.VISIBLE);
    }
}
