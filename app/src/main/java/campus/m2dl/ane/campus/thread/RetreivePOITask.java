package campus.m2dl.ane.campus.thread;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;

/**
 * Created by Nabil on 23/01/16.
 */

    public class RetreivePOITask extends AsyncTask<String, Void, String> {

        POI poi ;
        List<POI> poiList ;
        Context context ;

    public RetreivePOITask( List<POI> poiList , Context context) {

        this.poiList = poiList;
        this.context = context ;
    }


        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            try {
                poiList.clear();
                HttpPost httpPost = new HttpPost(urls[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpresponse = httpclient.execute(httpPost);
                HttpEntity httpentity = httpresponse.getEntity();

                if (httpentity != null) {
                    JSONObject jso = new JSONObject(inputStreamToString(
                            httpentity.getContent()).toString());
                    JSONArray tabPOI = jso.optJSONArray("POI");

                    for (int i = 0; i < tabPOI.length(); i++) {

                        JSONObject lignePoi = tabPOI.getJSONObject(i);
                        poi = new POI();
                        poi.sender = null;
                        poi.description = lignePoi.optString("description");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = new Date();
                        try {
                            startDate = df.parse(lignePoi.optString("date"));
                            String newDateString = df.format(startDate);
                            System.out.println(newDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        poi.date = startDate ;
                        poi.poiId = lignePoi.optInt("ID");
                        poi.image = null;
                        poi.tagImg = TagImg.valueOf(lignePoi.optString("type"));
                        poi.position = new LatLng(lignePoi.optDouble("latitude"),lignePoi.optDouble("longitude"));
                        List<String> items = new ArrayList<String>(Arrays.asList(lignePoi.optString("tags").split("\\s+")));
                        poi.tags = items ;
                        poiList.add(poi);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String success) {


        }




        public StringBuilder inputStreamToString(InputStream is) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                br = new BufferedReader(new InputStreamReader(is));
                String ligne = br.readLine();
                while (ligne != null) {
                    sb.append(ligne);
                    ligne = br.readLine();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("erreur de flux d'entree", e.getMessage());
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return sb;
        }



    }




