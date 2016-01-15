package campus.m2dl.ane.campus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import campus.m2dl.ane.campus.listener.CampUsLocationListener;

public class CampUsActivity extends AppCompatActivity {

    public static final int POS_SIZE = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new CampUsLocationListener(this);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(43.573503,
                        1.470537));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.moveCamera(center);
        map.animateCamera(zoom);
        //System.out.println(map.getCameraPosition().bearing);

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.over))
                .position(new LatLng(43.564646, 1.467138), 2500f, 2500f);

        map.addGroundOverlay(newarkMap);
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
}
