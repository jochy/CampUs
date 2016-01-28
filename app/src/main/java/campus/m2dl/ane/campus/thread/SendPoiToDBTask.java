package campus.m2dl.ane.campus.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import campus.m2dl.ane.campus.model.POI;

import static campus.m2dl.ane.campus.service.Base64.encodeBytes;

/**
 * Created by edouard on 23/01/16.
 */
public class SendPoiToDBTask extends AsyncTask<POI, Void, String> {


    String response = "", response2 = "", response3 = "";
    private AppCompatActivity/*TagCreationActivity*/ mActivity;
    public SendPoiToDBTask(AppCompatActivity/*TagCreationActivity*/ activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(POI... pois) {

        POI poi = pois[0];
        // TODO : add in appConfig...
        // String url = "camp-us.net16.net/images/";
        //File file;

        // Send POI to BD
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/insert_poi.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            /** TODO  **/
            nameValuePairs.add(new BasicNameValuePair("longitude",String.valueOf(poi.position.longitude)));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(poi.position.latitude)));
            nameValuePairs.add(new BasicNameValuePair("sender", poi.sender.getUsername()));
            nameValuePairs.add(new BasicNameValuePair("description", poi.description));
            nameValuePairs.add(new BasicNameValuePair("tagImg", poi.tagImg.getBdType()));
            nameValuePairs.add(new BasicNameValuePair("tags", poi.tags.toString()));
            nameValuePairs.add(new BasicNameValuePair("date", poi.date.toString()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(mActivity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            response = "error";
            //return "error";
        }


        //get ID of the inserted POI
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/get_last_poi_id.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            /** TODO  **/
            nameValuePairs.add(new BasicNameValuePair("longitude",String.valueOf(poi.position.longitude)));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(poi.position.latitude)));
            nameValuePairs.add(new BasicNameValuePair("sender", poi.sender.getUsername()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response2  = httpclient.execute(httppost, responseHandler);
            if (!response2.trim().equals("error")) {
                poi.poiId = Integer.valueOf(response2.trim());

                pois[0] = poi;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(mActivity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            response2 = "error";
            //return "error";
        }

        //CacheService.getInstance().saveCacheFile(Integer.toString(poi.poiId), Integer.toString(poi.poiId), poi.image );
        /*file = new File(AppConfiguration.URI_CACHE, Integer.toString(poi.poiId) + ".png");
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            httpclient.execute(httppost);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(mActivity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            response3 = "error";
            return "error";
        }*/

        //CacheService.getInstance().saveCacheFile(Integer.toString(poi.poiId), Integer.toString(poi.poiId), poi.image );

        InputStream is;
        BitmapFactory.Options bfo;
        //Bitmap bitmapOrg;
        ByteArrayOutputStream bao ;
        bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        FileInputStream fis = null;
        bao = new ByteArrayOutputStream();
        poi.image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte [] ba = bao.toByteArray();
        String ba1 = encodeBytes(ba);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("image",ba1));
        nameValuePairs.add(new BasicNameValuePair("cmd",String.valueOf(poi.poiId)));

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/UploadImage.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            Log.v("log_tag", "In the try Loop");
        }catch(Exception e){
            Log.v("log_tag", "Error in http connection "+e.toString());
            //return "error";

        }


        return null;
    }

    @Override
    protected void onPostExecute(String success) {

        if (!response.trim().equals("error") && !response2.trim().equals("error") && !response3.trim().equals("error")) {
            Toast.makeText(mActivity.getApplicationContext(), "Le tag a été ajouté !", Toast.LENGTH_LONG).show();
            mActivity.finish();
        }
        else {
            Toast.makeText(mActivity.getApplicationContext(), "Une erreur est survenue lors de la création du tag", Toast.LENGTH_LONG).show();
        }

    }
}