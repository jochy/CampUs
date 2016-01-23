package campus.m2dl.ane.campus.service;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.thread.UpdateMarkersTask;

/**
 * Created by Alexandre on 23/01/2016.
 */
public class UpdateMarkersService {
    private static final UpdateMarkersService instance = new UpdateMarkersService();

    private Object lock;
    private UpdateMarkersTask updateMarkersTask;

    private UpdateMarkersService(){
        this.lock = new Object();
    }

    public void updateMarkers(IUpdateMarkerServiceConsumer consumer,List<POI> poiList, GoogleMap map, String query) {
        synchronized (lock) {
            if (updateMarkersTask != null) {
                updateMarkersTask.cancel(true);
            }

            updateMarkersTask = new UpdateMarkersTask(consumer, poiList, query, map);
            updateMarkersTask.execute();
        }
    }

    public static UpdateMarkersService getInstance(){
        return instance;
    }
}
