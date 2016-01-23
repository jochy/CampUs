package campus.m2dl.ane.campus.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
import java.io.File;
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

import campus.m2dl.ane.campus.ExifUtils;
import campus.m2dl.ane.campus.MapConfiguration;
import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.activity.listener.CampUsLocationListener;
import campus.m2dl.ane.campus.activity.listener.CampUsOnInfoWindowsClickListener;
import campus.m2dl.ane.campus.activity.listener.CampUsTextWatcher;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;
import campus.m2dl.ane.campus.service.IUpdateMarkerServiceConsumer;
import campus.m2dl.ane.campus.service.MessageService;
import campus.m2dl.ane.campus.service.UpdateMarkersService;

public class CampUsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, IUpdateMarkerServiceConsumer {

    private List<POI> poiList = new ArrayList<>();
    private EditText tags;
    private GoogleMap map;
    private File photo;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);
        // Fixme: comment that line !
        showDebugMarker();

        try {
            tags = (EditText) findViewById(R.id.adresseMap);

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            setBackgroundMap(map);
            map.setOnInfoWindowClickListener(new CampUsOnInfoWindowsClickListener(this));
            map.setOnMarkerClickListener(this);
            CampUsTextWatcher campUsTextWatcher = new CampUsTextWatcher(this, map);
            tags.addTextChangedListener(campUsTextWatcher);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new CampUsLocationListener(this, map, findViewById(R.id.takePicture));
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Impossible de démarrer le GPS", Toast.LENGTH_LONG).show();
            }

            // Default Zoom + center on the Admin building
            CameraUpdate center = CameraUpdateFactory.newLatLng(MapConfiguration.CENTER_CAMERA);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(MapConfiguration.DEFAULT_ZOOM);
            map.moveCamera(center);
            map.animateCamera(zoom);

            UpdateMarkersService.getInstance().updateMarkers(this, getPoiList(), map, null);
        } catch (Exception e) {
            // If no GooglePlay service, then let google display its message
            findViewById(R.id.adresseMap).setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
    }

    private void setBackgroundMap(GoogleMap map) {
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mapover))
                .position(MapConfiguration.CENTER_MAP_GPS, MapConfiguration.MAP_WIDTH, MapConfiguration.MAP_HEIGHT);

        map.addGroundOverlay(newarkMap);
    }

    private void showDebugMarker() {
        // Fixme: don't use that method
        // poiList = POImock.getPoiList();
        new RetreivePOITask().execute("http://camp-us.net16.net/script_php/get_all_poi.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camp_us, menu);
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

    public void takePicture(View view) {
        // Création de l'intent de type ACTION_IMAGE_CAPTURE
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                try {
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(photo));

                    // Rotate it
                    bitmap = ExifUtils.rotateBitmap(photo.getAbsolutePath(), bitmap);

                    Intent intent = new Intent(this, TagCreationActivity.class);
                    MessageService.message = bitmap;
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void updatePoiList(List<POI> poiList) {
        this.poiList = poiList;
    }

    public List<POI> getPoiList() {
        return this.poiList;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getBaseContext(), "Appuyez sur la description pour plus de détails", Toast.LENGTH_SHORT).show();
        return false;
    }


    public class RetreivePOITask extends AsyncTask<String, Void, String> {

        POI poi;

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
                        poi.date = startDate;
                        poi.poiId = lignePoi.optInt("ID");
                        poi.image = null;
                        poi.tagImg = TagImg.valueOf(lignePoi.optString("type"));
                        poi.position = new LatLng(lignePoi.optDouble("latitude"), lignePoi.optDouble("longitude"));
                        List<String> items = new ArrayList<String>(Arrays.asList(lignePoi.optString("tags").split("\\s+")));
                        poi.tags = items;
                        poiList.add(poi);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String success) {


            /*for (int i = 0; i < poiList.size() ; i++) {

            }
            System.out.prinlnt(); */

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
                e.printStackTrace();
                Log.e("Erreur de flux d'entree", e.getMessage());
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


}
