package com.vish.apps.dictionary.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.XMLConstants;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.service.controls.Control;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import com.vish.apps.dictionary.Interfaces.AsyncResponse;
import com.vish.apps.dictionary.adapters.DefinitionsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Oxford extends AsyncTask<String, Integer, String> {
    private static final String TAG = "OXFORD REQUEST";
    private Word mWord;

    public Oxford(Word word) {
        mWord = word;
    }

    /** code that run in the background */
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
                Log.d(TAG, "doInBackground: STRING" + stringBuilder);
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    /** Before anything executes */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    /** Receive progress updates from doInBackground */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


    /** Update the UI after background processes completes */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String title;
        String definition;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray results = jsonObject.getJSONArray("results");

            JSONObject idEntries = results.getJSONObject(0);
            JSONArray idArray = idEntries.getJSONArray("id");

            title = idArray.getString(0);

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray lArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = lArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jObject = e.getJSONObject(0);
            JSONArray sensesArray = jObject.getJSONArray("senses");

            JSONObject jsonDef = sensesArray.getJSONObject(0);
            JSONArray arrayDef = jsonDef.getJSONArray("definitions");

            definition = arrayDef.getString(0);

            mWord.setTitle(title);
            mWord.setDefinition(definition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Word convertJson(JSONObject jsonObj) throws JSONException {
        String title;
        String definition;

        JSONObject jsonObject = jsonObj;
        JSONArray results = jsonObject.getJSONArray("results");

        JSONObject idEntries = results.getJSONObject(0);
        JSONArray idArray = idEntries.getJSONArray("id");
        title = idArray.getString(0);

        JSONObject lEntries = results.getJSONObject(0);
        JSONArray lArray = lEntries.getJSONArray("lexicalEntries");

        JSONObject entries = lArray.getJSONObject(0);
        JSONArray e = entries.getJSONArray("entries");

        JSONObject jObject = e.getJSONObject(0);
        JSONArray sensesArray = jObject.getJSONArray("senses");

        JSONObject jsonDef = sensesArray.getJSONObject(0);
        JSONArray arrayDef = jsonDef.getJSONArray("definitions");

        definition = arrayDef.getString(0);

        return new Word(title, definition);
    }
}