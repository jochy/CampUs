package campus.m2dl.ane.campus;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import campus.m2dl.ane.campus.listener.CampUsLocationListener;

public class CampUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        setBackgroundMap(map);
        showDebugMarker(map);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new CampUsLocationListener(this, map);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Unable to start GPS", Toast.LENGTH_LONG).show();
        }

        // Default Zoom + center on the Admin building
        CameraUpdate center = CameraUpdateFactory.newLatLng(MapConfiguration.CENTER_CAMERA);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(MapConfiguration.DEFAULT_ZOOM);
        map.moveCamera(center);
        map.animateCamera(zoom);


    }

    private void post(){
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://camp-us.net16.net/createuser.php").openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("");
            writer.flush();
            writer.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBackgroundMap(GoogleMap map) {
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mapover))
                .position(MapConfiguration.CENTER_MAP_GPS, MapConfiguration.MAP_WIDTH, MapConfiguration.MAP_HEIGHT);

        map.addGroundOverlay(newarkMap);
    }

    private void showDebugMarker(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.562932, 1.466117))
                .title("Administration"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.560219, 1.470301))
                .title("U1"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.566994, 1.470158))
                .title("Stand de tir à l'arc"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.568724, 1.462178))
                .title("FAC pharma"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.570032, 1.467095))
                .title("INSA"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.557593, 1.469441))
                .title("4TP1"));
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

    public void takePicture(View view)
    {
        Intent intent = new Intent(this, TagCreationActivity.class);
        startActivity(intent);
    }
}
