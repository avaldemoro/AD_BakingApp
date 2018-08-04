package co.asterv.ad_bakingapp.utils;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JsonUtils {
    public static URL buildBakingUrl() {
        Uri builtUri = Uri.parse (Constant.BAKING_JSON_URL);

        URL url = null;
        try {
            url = new URL (builtUri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection ();
        try {
            InputStream in = urlConnection.getInputStream ();

            Scanner scanner = new Scanner (in);
            scanner.useDelimiter ("\\A");

            boolean hasInput = scanner.hasNext ();
            if (hasInput) {
                return scanner.next ();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect ();
        }
    }
}

