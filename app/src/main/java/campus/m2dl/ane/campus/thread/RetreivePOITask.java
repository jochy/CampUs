package campus.m2dl.ane.campus.thread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
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

import campus.m2dl.ane.campus.AppConfiguration;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;
import campus.m2dl.ane.campus.service.IUpdateMarkerServiceConsumer;
import campus.m2dl.ane.campus.service.UpdateMarkersService;

/**
 * Created by Nabil on 23/01/16.
 */

public class RetreivePOITask extends AsyncTask<String, Void, String> {

    private IUpdateMarkerServiceConsumer consumer;
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private EditText filterText;
    private GoogleMap map;

    private List<POI> poiList;

    public RetreivePOITask(IUpdateMarkerServiceConsumer consumer, EditText filterText, GoogleMap map) {
        this.consumer = consumer;
        this.filterText = filterText;
        this.map = map;
    }

    @Override
    protected String doInBackground(String... urls) {
        while (!isCancelled()) {
            poiList = new ArrayList<>();
            HttpClient httpclient = new DefaultHttpClient();
            try {
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

                        POI poi = new POI();
                        poi.sender = null; // Fixme : affect it
                        poi.description = lignePoi.optString("description");
                        Date startDate;
                        try {
                            startDate = df.parse(lignePoi.optString("date"));
                        } catch (ParseException e) {
                            startDate = new Date();
                            e.printStackTrace();
                        }
                        poi.date = startDate;
                        poi.poiId = lignePoi.optInt("ID");
                        poi.image = null; // Fixme : affect it
                        poi.tagImg = TagImg.valueOf(lignePoi.optString("type"));
                        poi.position = new LatLng(lignePoi.optDouble("latitude"), lignePoi.optDouble("longitude"));
                        poi.tags = Arrays.asList(lignePoi.optString("tags").split("\\s+"));;
                        poiList.add(poi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RetrievePOITask", e.getMessage());
            }

            for(POI p : poiList){
                boolean found = false;
                for(POI p2 : consumer.getPOIList()){
                    if(p2.poiId == p.poiId){
                        found = true;
                    }
                }

                if(!found){
                    consumer.getPOIList().add(p);
                }
            }

            consumer.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateMarkersService.getInstance().updateMarkers(consumer, consumer.getPOIList(), map, filterText.getText().toString());
                }
            });

            try {
                Thread.sleep(AppConfiguration.POI_REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String success) {
        // Nothing
    }

    public StringBuilder inputStreamToString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(is));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("erreur de flux d'entree", e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }
}




