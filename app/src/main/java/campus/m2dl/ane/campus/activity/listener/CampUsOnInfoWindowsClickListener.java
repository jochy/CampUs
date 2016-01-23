package campus.m2dl.ane.campus.activity.listener;

import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import campus.m2dl.ane.campus.activity.CampUsActivity;
import campus.m2dl.ane.campus.activity.MarkerInfoActivity;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.service.MessageService;

/**
 * Created by Alexandre on 23/01/2016.
 */
public class CampUsOnInfoWindowsClickListener implements GoogleMap.OnInfoWindowClickListener {

    private CampUsActivity campUsActivity;

    public CampUsOnInfoWindowsClickListener(CampUsActivity campUsActivity) {
        this.campUsActivity = campUsActivity;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (POI p : campUsActivity.getPoiList()) {
            if (marker.equals(p.marker)) {
                Intent intent = new Intent(campUsActivity, MarkerInfoActivity.class);
                MessageService.message = p;

                campUsActivity.startActivity(intent);
                break;
            }
        }
    }
}
