package campus.m2dl.ane.campus.thread;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import campus.m2dl.ane.campus.activity.TagCreationActivity;
import campus.m2dl.ane.campus.model.POI;

/**
 * Created by edouard on 23/01/16.
 */
public class SendPoiToDBTask extends AsyncTask<POI, Void, String> {

    String response = "";
    private TagCreationActivity mActivity;

    public SendPoiToDBTask(TagCreationActivity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(POI... pois) {

        POI poi = pois[0];
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/insert_poi.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            /** TODO  **/
            nameValuePairs.add(new BasicNameValuePair("longitude",String.valueOf(poi.position.longitude)));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(poi.position.latitude)));
            nameValuePairs.add(new BasicNameValuePair("description", poi.description));
            nameValuePairs.add(new BasicNameValuePair("user", poi.sender.getUsername()));
            nameValuePairs.add(new BasicNameValuePair("type", ""));
            nameValuePairs.add(new BasicNameValuePair("tags", poi.tags.toString()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);

        } catch (Exception e) {
            Toast.makeText(mActivity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            response = "error";
            return "error";
        }


        return null;
    }

    @Override
    protected void onPostExecute(String success) {

        if (!response.trim().equals("error")) {
            Toast.makeText(mActivity.getApplicationContext(), "Le tag a été ajouté !", Toast.LENGTH_LONG).show();
            mActivity.finish();
        }
        else {
            Toast.makeText(mActivity.getApplicationContext(), "Une erreur est survenue lors de la création du tag", Toast.LENGTH_LONG).show();
        }

    }
}