package campus.m2dl.ane.campus.model.mock;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;

/**
 * Created by Alexandre on 21/01/2016.
 */
public final class POImock {

    private static int poiId = 0;

    public static List<POI> getPoiList() {
        List<POI> poiList = new ArrayList<>();

        List<String> tags1 = new ArrayList<>();
        tags1.add("dégradation");
        tags1.add("central");
        tags1.add("tag");

        List<String> tag2 = new ArrayList<>();
        tag2.add("query");
        tag2.add("Android");
        tag2.add("SFS");

        poiList.add(createPOI(new LatLng(43.562932, 1.466117), "Ceci est le batiment administratif", TagImg.OTHER, tags1));
        poiList.add(createPOI(new LatLng(43.560219, 1.470301), "Ceci est le batiment U1", TagImg.WATER, new ArrayList<String>()));
        poiList.add(createPOI(new LatLng(43.566994, 1.470158), "Ceci est le batiment du stand de tir à l'arc", TagImg.DEGRADATION, tag2));
        poiList.add(createPOI(new LatLng(43.568724, 1.462178), "Ceci est le batiment de la fac de pharma", TagImg.RECYLCE, new ArrayList<String>()));
        poiList.add(createPOI(new LatLng(43.570032, 1.467095), "Ceci est le batiment de l'INSA", TagImg.OTHER, tags1));
        poiList.add(createPOI(new LatLng(43.557593, 1.469441), "Ceci est le batiment 4TP1", TagImg.OTHER, tag2));

        return poiList;
    }

    private static POI createPOI(LatLng latLng, String desc, TagImg tagImg, List<String> tags) {
        POI poi = new POI();

        poi.sender = null;
        poi.description = desc;
        poi.date = new Date();
        poi.poiId = poiId++;
        poi.image = null;
        poi.tagImg = tagImg;
        poi.position = latLng;
        poi.tags = tags;

        return poi;
    }
}
