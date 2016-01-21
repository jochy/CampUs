package campus.m2dl.ane.campus.listener;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.MessageFormat;

import campus.m2dl.ane.campus.CampUsActivity;
import campus.m2dl.ane.campus.R;

/**
 * Created by Alexandre on 08/01/2016.
 */
public class CampUsLocationListener implements LocationListener {

    private GoogleMap map;
    private Marker position;
    private Activity activity;

    public CampUsLocationListener(Activity activity, GoogleMap map) {
        this.activity = activity;
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (position == null) {
            position = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .flat(true));
        }
        else {
            position.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(activity.getBaseContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
        position.remove();
        position = null;
    }
}
