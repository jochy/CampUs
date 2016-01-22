package campus.m2dl.ane.campus.model;

import campus.m2dl.ane.campus.R;

/**
 * Created by Alexandre on 21/01/2016.
 */
public enum TagImg {
    WATER("Fuite d'eau", R.drawable.mpipe),
    DEGRADATION("Dégradation", R.drawable.mrepaire),
    RECYLCE("Recyclage", R.drawable.mtrash),
    FOOD("Restauration", R.drawable.mfood),
    BATTERY("Batterie", R.drawable.mbattery),
    CARTBOARD("Carton", R.drawable.mcartboard),
    GLASS("Verre", R.drawable.mglass),
    TOILETS("Toilettes", R.drawable.mtoilets),
    OTHER("Divers", R.drawable.munknow);

    public String text;
    public int resourceId;

    TagImg(String text, int resourceId) {
        this.text = text;
        this.resourceId = resourceId;
    }


    public static TagImg getValueOf(String s){
        for(TagImg t : values()){
            if(t.text.equals(s)){
                return t;
            }
        }

        return null;
    }
}
