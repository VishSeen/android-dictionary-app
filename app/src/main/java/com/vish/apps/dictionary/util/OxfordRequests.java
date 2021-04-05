package com.vish.apps.dictionary.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.XMLConstants;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OxfordRequests extends AsyncTask<String, Integer, String> {
    private static final String TAG = "OXFORD REQUEST";
    private Word mWord;

    public OxfordRequests(Word word){
        mWord = word;
    }

    @Override
    protected String doInBackground(String... params) {

        // replace with app id and app key
        final String app_id = "dd74a1c2";
        final String app_key = "6d767e1897c9cdf435102dcf0d77ad47";

        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String definition;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray results = jsonObject.getJSONArray("results");

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray lArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = lArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jObject = e.getJSONObject(0);
            JSONArray sensesArray = jObject.getJSONArray("senses");

            JSONObject jsonDef = sensesArray.getJSONObject(0);
            JSONArray arrayDef = jsonDef.getJSONArray("definitions");

            definition = arrayDef.getString(0);

            if (result.length() > 0) { // valid definitions were found
                mWord.setDefinition(definition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}