package campus.m2dl.ane.campus.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import java.text.MessageFormat;

import campus.m2dl.ane.campus.CampUsActivity;

/**
 * Created by Alexandre on 08/01/2016.
 */
public class CampUsLocationListener implements LocationListener {

    CampUsActivity campUsActivity;

    public CampUsLocationListener(CampUsActivity campUsActivity){
        this.campUsActivity = campUsActivity;
    }


    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Changed");
        campUsActivity.updateLocation(location.getLatitude(), location.getLongitude());
        //Toast.makeText(campUsActivity.getBaseContext(), MessageFormat.format("x: {0} --- y: {1} --- z: {2}",location.getLatitude(), location.getLongitude(), location.getAltitude()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
