package campus.m2dl.ane.campus.activity.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import campus.m2dl.ane.campus.activity.CampUsActivity;
import campus.m2dl.ane.campus.service.MessageService;

/**
 * Created by Alexandre on 08/01/2016.
 */
public class CampUsLocationListener implements LocationListener {

    private GoogleMap map;
    private Marker position;
    private CampUsActivity activity;
    private View takePicture;

    public CampUsLocationListener(CampUsActivity activity, GoogleMap map, View takePicture) {
        this.activity = activity;
        this.map = map;
        this.takePicture = takePicture;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (position == null) {
            position = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .flat(false));
        }
        else {
            position.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        MessageService.currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        takePicture.setVisibility(View.VISIBLE);
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
        Toast.makeText(activity.getBaseContext(), "GPS désactivé", Toast.LENGTH_SHORT).show();
        if (position != null) {
            position.remove();
            position = null;
        }
        MessageService.currentPosition = null;
        takePicture.setVisibility(View.INVISIBLE);
    }
}
