package campus.m2dl.ane.campus.service;

import java.util.List;

import campus.m2dl.ane.campus.model.POI;

/**
 * Created by Alexandre on 23/01/2016.
 */
public interface IUpdateMarkerServiceConsumer {
    public void updatePoiList(List<POI> poiList);

    public void runOnUiThread(Runnable runnable);
}
