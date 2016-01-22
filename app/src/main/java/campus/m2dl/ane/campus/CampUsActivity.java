package campus.m2dl.ane.campus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import campus.m2dl.ane.campus.listener.CampUsLocationListener;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.mock.POImock;
import campus.m2dl.ane.campus.service.MessageService;
import campus.m2dl.ane.campus.thread.UpdateMarkersTask;

public class CampUsActivity extends AppCompatActivity implements TextWatcher, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private List<POI> poiList = new ArrayList<>();
    private EditText tags;
    private GoogleMap map;
    private UpdateMarkersTask updateMarkersTask;
    private final Object lock = new Object();
    private File photo;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);

        try {
            tags = (EditText) findViewById(R.id.adresseMap);
            tags.addTextChangedListener(this);

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            setBackgroundMap(map);
            map.setOnInfoWindowClickListener(this);
            map.setOnMarkerClickListener(this);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new CampUsLocationListener(this, map, findViewById(R.id.takePicture));
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

            // Fixme: comment that line !
            showDebugMarker();

            updateMarkers();
        }catch(Exception e){
            // If no GooglePlay service, then let google display its message
            findViewById(R.id.adresseMap).setVisibility(View.INVISIBLE);
            findViewById(R.id.validerLong).setVisibility(View.INVISIBLE);
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
        poiList = POImock.getPoiList();
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
                Toast.makeText(this, Uri.fromFile(photo).toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateMarkers();
    }

    private void updateMarkers(){
        synchronized (lock) {
            if (updateMarkersTask != null) {
                updateMarkersTask.cancel(true);
            }

            updateMarkersTask = new UpdateMarkersTask(this, poiList, tags.getText().toString(), map);
            updateMarkersTask.execute();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Nothing
    }

    public void updatePoiList(List<POI> poiList){
        this.poiList = poiList;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for(POI p : poiList){
            if(marker.equals(p.marker)){
                Intent intent = new Intent(this, MarkerInfoActivity.class);
                MessageService.message = p;

                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getBaseContext(), "Appuyez sur la description pour plus de détails", Toast.LENGTH_SHORT).show();
        return false;
    }
}
