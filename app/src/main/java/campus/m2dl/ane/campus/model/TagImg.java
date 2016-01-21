package campus.m2dl.ane.campus.model;

import campus.m2dl.ane.campus.R;

/**
 * Created by Alexandre on 21/01/2016.
 */
public enum TagImg {
    WATER("Fuite d'eau", R.drawable.mpipe),
    DEGRADATION("Dégradation", R.drawable.mrepaire),
    RECYLCE("Recyclage", R.drawable.mtrash),
    OTHER("Divers", R.drawable.munknow);

    public String text;
    public int resourceId;

    TagImg(String text, int resourceId) {
        this.text = text;
        this.resourceId = resourceId;
    }
}
