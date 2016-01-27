package campus.m2dl.ane.campus.model;

import campus.m2dl.ane.campus.R;

/**
 * Created by Alexandre on 21/01/2016.
 */
public enum TagImg {
    WATER("Eau", R.drawable.mpipe, "WATER"),
    DEGRADATION("Dégradation", R.drawable.mrepaire, "DEGRADATION"),
    RECYLCE("Recyclage", R.drawable.mtrash, "RECYCLE"),
    FOOD("Restauration", R.drawable.mfood, "FOOD"),
    BATTERY("Batterie", R.drawable.mbattery, "BATTERY"),
    CARTBOARD("Carton", R.drawable.mcartboard, "CARTBOARD"),
    GLASS("Verre", R.drawable.mglass, "GLASS"),
    TOILETS("Toilettes", R.drawable.mtoilets, "TOILETS"),
    PAINT("Peinture", R.drawable.mpaint, "PAINT"),
    CONSTRUCTION("Travaux", R.drawable.mconstruction, "CONSTRUCTION"),
    OTHER("Divers", R.drawable.munknow, "OTHER");

    public String text;
    public int resourceId;
    public String bdType;

    TagImg(String text, int resourceId, String dbType) {
        this.text = text;
        this.resourceId = resourceId;
        this.bdType = dbType;
    }

    public static TagImg getValueOf(String s){
        for(TagImg t : values()){
            if(t.text.equals(s)){
                return t;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public String getBdType(){
        return this.bdType;
    }
}
