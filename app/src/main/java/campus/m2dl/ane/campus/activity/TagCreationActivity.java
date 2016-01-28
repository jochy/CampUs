package campus.m2dl.ane.campus.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.activity.adapter.TagListAdapter;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;
import campus.m2dl.ane.campus.service.MessageService;

import static campus.m2dl.ane.campus.service.Base64.encodeBytes;

public class TagCreationActivity extends AppCompatActivity {

    private LatLng currentPosition;
    private Bitmap bitmap;
    private POI poi ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = (Bitmap) MessageService.message;
        setContentView(R.layout.activity_tag_creation);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new TagListAdapter(getBaseContext(), R.layout.list_adapter, Arrays.asList(TagImg.values())));
        spinner.setSelection(TagImg.values().length - 1);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewTag);
        imageView.setImageBitmap(bitmap);

        currentPosition = MessageService.currentPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_creation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNewPoi(View view) {
        Toast.makeText(getApplicationContext(),"button clicked",Toast.LENGTH_LONG).show();
        poi = new POI();
        poi.image = bitmap;
        poi.position = currentPosition;
        poi.sender = MessageService.currentUser;
        poi.description = ((EditText) findViewById(R.id.descriptionField)).getText().toString();
        poi.tags = new ArrayList<>();
        for (String s : ((EditText) findViewById(R.id.tagList)).getText().toString().split(" ")) {
            poi.tags.add(s);
        }
        poi.date = new Date();
        poi.tagImg = (TagImg) ((Spinner) findViewById(R.id.spinner)).getSelectedItem();

        new SendPoiToDBTask3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public class SendPoiToDBTask3 extends AsyncTask<String, Void, String> {

        InputStream is;

        @Override
        protected String doInBackground(String... urls) {

            try {
                //Toast.makeText(context, "RESP 1", Toast.LENGTH_LONG).show();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/insert_poi.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                /** TODO  **/
                nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(poi.position.longitude)));
                nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(poi.position.latitude)));
                nameValuePairs.add(new BasicNameValuePair("sender", poi.sender.getUsername()));
                nameValuePairs.add(new BasicNameValuePair("description", poi.description));
                nameValuePairs.add(new BasicNameValuePair("tagImg", poi.tagImg.getBdType()));
                nameValuePairs.add(new BasicNameValuePair("tags", poi.tags.toString()));
                nameValuePairs.add(new BasicNameValuePair("date", poi.date.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);
                //Toast.makeText(context, response, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/get_last_poi_id.php");
                //Toast.makeText(context, "RESP 2", Toast.LENGTH_LONG).show();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                /** TODO  **/
                nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(poi.position.longitude)));
                nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(poi.position.latitude)));
                nameValuePairs.add(new BasicNameValuePair("sender", poi.sender.getUsername()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response2 = httpclient.execute(httppost, responseHandler);
                if (!response2.trim().equals("error")) {
                    poi.poiId = Integer.valueOf(response2.trim());
                    //Toast.makeText(context, "id" + poi.poiId, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }


            InputStream is;
            BitmapFactory.Options bfo;
            //Bitmap bitmapOrg;
            ByteArrayOutputStream bao;
            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;
            FileInputStream fis = null;
            bao = new ByteArrayOutputStream();
            poi.image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = encodeBytes(ba);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", ba1));
            nameValuePairs.add(new BasicNameValuePair("cmd", "testImage"));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://camp-us.net16.net/script_php/UploadImage.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                Log.v("log_tag", "In the try Loop");
            } catch (Exception e) {
                e.printStackTrace();

                //Toast.makeText(context, "Error in http connection " + e.toString(), Toast.LENGTH_LONG).show();
                //return "error";
            }
            return null ;

        }

        @Override
        protected void onPostExecute(String success) {
            // Nothing
        }



    }







}
