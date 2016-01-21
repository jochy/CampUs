package campus.m2dl.ane.campus.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexandre on 21/01/2016.
 */
public class POI {
    public Marker marker;
    public Bitmap image;
    public String description;
    public List<String> tags = new ArrayList<>();
    public User sender;
    public Date date;
    public int poiId;
    public LatLng position;
    public TagImg tagImg = TagImg.OTHER;

    public POI(){

    }
}
