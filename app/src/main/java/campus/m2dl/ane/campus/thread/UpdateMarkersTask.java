package campus.m2dl.ane.campus.thread;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.service.IUpdateMarkerServiceConsumer;

/**
 * Created by Alexandre on 21/01/2016.
 */
public class UpdateMarkersTask extends AsyncTask<String, Void, String> {

    private IUpdateMarkerServiceConsumer consumer;
    private List<POI> poiList;
    private String query;
    private GoogleMap map;

    public UpdateMarkersTask(IUpdateMarkerServiceConsumer consumer, List<POI> poiList, String query,
                             GoogleMap map) {
        this.consumer = consumer;
        this.poiList = poiList != null ? poiList : new ArrayList<POI>();
        this.query = query != null ? query.toLowerCase() : "";
        this.map = map;
    }

    @Override
    protected String doInBackground(String... params) {
        final List<POI> matchPOI = new ArrayList<>();

        if (poiList == null || query == null) {
            return null;
        }

        for (final POI p : poiList) {
            boolean match = false;

            for (String t : query.split(" ")) {
                for (String subTag : p.tags) {
                    match = match || subTag.toLowerCase().contains(t);
                }
            }

            if (match || (p.tags.size() == 0 && "".equals(query))) {
                matchPOI.add(p);
            }
        }

        consumer.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (POI p : consumer.getPOIList()) {
                    try {
                        p.marker.remove();
                    } catch (Exception e) {
                        // Nothing
                    }
                    p.marker = null;
                }

                for(POI p : consumer.getPOIList()){
                    if(matchPOI.contains(p)){
                        p.marker = map.addMarker(new MarkerOptions()
                                .flat(false)
                                .position(p.position)
                                .icon(BitmapDescriptorFactory.fromResource(p.tagImg.resourceId))
                                .title(p.tagImg.text)
                                .snippet(p.description));
                    }
                }
            }
        });

        return null;
    }
}

