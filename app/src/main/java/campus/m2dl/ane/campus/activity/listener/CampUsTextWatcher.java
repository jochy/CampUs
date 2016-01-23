package campus.m2dl.ane.campus.activity.listener;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.gms.maps.GoogleMap;

import campus.m2dl.ane.campus.activity.CampUsActivity;
import campus.m2dl.ane.campus.service.UpdateMarkersService;

/**
 * Created by Alexandre on 23/01/2016.
 */
public class CampUsTextWatcher implements TextWatcher {

    private CampUsActivity campUsActivity;
    private GoogleMap map;

    public CampUsTextWatcher(CampUsActivity campUsActivity, GoogleMap map) {
        this.campUsActivity = campUsActivity;
        this.map = map;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        UpdateMarkersService.getInstance().updateMarkers(campUsActivity, campUsActivity.getPoiList(), map, s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Nothing
    }
}
