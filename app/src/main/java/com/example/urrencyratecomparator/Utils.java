package com.example.urrencyratecomparator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Utils {
    public static String getResponseFromURL(URL url) throws IOException {
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

    public static String cutTo3AfterPoint(String string) {
        String[] stringArr = string.split("\\p{Punct}");
        if (stringArr.length > 1){
            StringBuilder afterPoint = new StringBuilder(stringArr[1]);

            if (afterPoint.length() > 3) {
                return stringArr[0] + "." + afterPoint.substring(0, 3);

            } else if (afterPoint.length() < 3) {
                while (afterPoint.length() < 3) {
                    afterPoint.append("0");
                }
                return stringArr[0] + "." + afterPoint.toString();

            } else return string;

        } else return string + ".000";
    }
}
