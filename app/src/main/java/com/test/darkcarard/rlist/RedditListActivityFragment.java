package com.test.darkcarard.rlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class RedditListActivityFragment extends Fragment {

    private RedditItemAdapter redditsAdapter;

    public RedditListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reddit_list, container, false);
        redditsAdapter = new RedditItemAdapter(
                getActivity(),
                R.layout.list_item_reddit,
                new ArrayList<Reddit>()
        );

        ListView listView = (ListView) rootView.findViewById(R.id.listview_reddit);
        listView.setAdapter(redditsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Reddit reddit = redditsAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), RedditDetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, reddit);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateReddits() {
        FetchRedditTask redditTask = new FetchRedditTask();
        redditTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateReddits();
    }

    public class FetchRedditTask extends AsyncTask<Void, Void, Reddit[]> {

        private final String LOG_TAG = FetchRedditTask.class.getSimpleName();

        private String parseTimestampToString(double timestamp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd");
            return simpleDateFormat.format(timestamp);
        }

        private Reddit[] getRedditDataFromJson(String redditJsonString) throws JSONException {

            final String ARRAY = "children";                    //Nodo con el array de los Reddits que extraemos del JSON
            final String DATA = "data";                         //Objeto con los datos del Reddit
            final String DESCRIPTION = "public_description";    //Descripción de l Reddit
            final String SUBSCRIBERS = "subscribers";           //Cantidad de suscriptores
            final String CREATION_DATE = "created_utc";         //Fecha en que se creó el Reddit
            final String IMAGE = "icon_img";                    //URL con la imagen del Reddit
            final String IMAGE_SIZE = "icon_size";              //Tamaño de la imagen. Arreglo de 2
            final String IMAGE_HEADER = "header_img";
            final String CATEGORY = "advertiser_category";      //Categoría del Reddit
            final String URL = "url";                           //URL del Reddit
            final String LANGUAGE = "lang";                     //Idioma en el que está el Reddit
            final String OVER_18 = "over18";                    //Si el Reddit es para mayores de edad

            JSONObject redditJson = new JSONObject(redditJsonString);
            //Log.v(LOG_TAG, redditJson.toString());
            JSONArray redditArray = redditJson.getJSONObject("data").getJSONArray(ARRAY);

            Reddit[] resultReddits = new Reddit[redditArray.length() + 1];

            for (int i = 0; i < redditArray.length(); i++) {
                JSONObject redditJO = redditArray.getJSONObject(i);
                JSONObject data = redditJO.getJSONObject(DATA);

                Reddit reddit = new Reddit(
                        data.optString(DESCRIPTION),
                        data.optLong(SUBSCRIBERS),
                        parseTimestampToString(data.optDouble(CREATION_DATE)),
                        data.optString(IMAGE),
                        new int[]{!data.isNull(IMAGE_SIZE) ? data.getJSONArray(IMAGE_SIZE).getInt(0) : 0,
                                !data.isNull(IMAGE_SIZE) ? data.getJSONArray(IMAGE_SIZE).getInt(1) : 0},
                        data.optString(CATEGORY),
                        data.optString(URL),
                        data.optString(LANGUAGE),
                        data.optBoolean(OVER_18),
                        data.optString(IMAGE_HEADER)
                );
                resultReddits[i] = reddit;
            }
            return resultReddits;
        }

        @Override
        protected Reddit[] doInBackground(Void... params) {
            String redditJsonString = null;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean offlineMode = sharedPrefs.getBoolean(getString(R.string.pref_offlineMode_key), false);
            if (!offlineMode) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                redditJsonString = null;
                try {
                    final String REDDIT_BASE_URL = "https://www.reddit.com/subreddits.json";

                    Uri builtUri = Uri.parse(REDDIT_BASE_URL).buildUpon().build();
                    URL url = new URL(builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return null;
                    }




                    redditJsonString = buffer.toString();
                    JSonSaver.saveData(getActivity(), redditJsonString);

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error: ", e);
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error cerrando el stream: ", e);
                        }
                    }
                }
            } else {
                redditJsonString = JSonSaver.getData(getActivity());
            }
            try {
                return getRedditDataFromJson(redditJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Reddit[] reddits) {
            if (reddits != null) {
                redditsAdapter.clear();
                redditsAdapter.addAll(reddits);
            }
        }
    }
}
