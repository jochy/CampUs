package campus.m2dl.ane.campus.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import campus.m2dl.ane.campus.AppConfiguration;

public class CacheService {
    private static CacheService INSTANCE = new CacheService();
    private HashMap<String, String> cacheMap;
    private HashMap<String, Bitmap> bitmapMap;

    @SuppressWarnings("unchecked")
    private CacheService() {
        cacheMap = new HashMap<String, String>();
        bitmapMap = new HashMap<String, Bitmap>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),
                AppConfiguration.URI_CACHE);
        if (!fullCacheDir.exists()) {
            Log.i("CACHE", "Directory doesn't exist");
            cleanCacheStart();
            return;
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(fullCacheDir.toString(), AppConfiguration.CACHE_FILENAME))));
            cacheMap = (HashMap<String, String>) is.readObject();
            is.close();
        } catch (StreamCorruptedException e) {
            Log.i("CACHE", "Corrupted stream");
            cleanCacheStart();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "File not found");
            cleanCacheStart();
        } catch (IOException e) {
            Log.i("CACHE", "Input/Output error");
            cleanCacheStart();
        } catch (ClassNotFoundException e) {
            Log.i("CACHE", "Class not found");
            cleanCacheStart();
        }
    }

    private void cleanCacheStart() {
        cacheMap = new HashMap<String, String>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),
                AppConfiguration.URI_CACHE);
        fullCacheDir.mkdirs();
        File noMedia = new File(fullCacheDir.toString(), ".nomedia");
        try {
            noMedia.createNewFile();
            Log.i("CACHE", "Cache created");
        } catch (IOException e) {
            Log.i("CACHE", "Couldn't create .nomedia file");
            e.printStackTrace();
        }
    }

    public static CacheService getInstance() {
        return INSTANCE;
    }

    public void saveCacheFile(String fileName, String cacheUri, Bitmap image) {
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),
                AppConfiguration.URI_CACHE);
        String fileLocalName = fileName + ".PNG"; // new  SimpleDateFormat("ddMMyyhhmmssSSS").format(new java.util.Date())+".PNG";
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(fileUri);
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            cacheMap.put(cacheUri, fileLocalName);
            Log.i("CACHE", "Saved file " + cacheUri + " (which is now " + fileUri.toString() + ") correctly");
            bitmapMap.put(cacheUri, image);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(new File(fullCacheDir.toString(), AppConfiguration.CACHE_FILENAME))));
            os.writeObject(cacheMap);
            os.close();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "Error: File " + cacheUri + " was not found!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("CACHE", "Error: File could not be stuffed!");
            e.printStackTrace();
        }
    }

    public Bitmap getCacheFile(String cacheUri) {
        if (bitmapMap.containsKey(cacheUri)) return (Bitmap) bitmapMap.get(cacheUri);

        if (!cacheMap.containsKey(cacheUri)) return null;
        String fileLocalName = cacheMap.get(cacheUri).toString();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),
                AppConfiguration.URI_CACHE);
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        if (!fileUri.exists()) return null;

        Log.i("CACHE", "File " + cacheUri + " has been found in the Cache");
        Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
        bitmapMap.put(cacheUri, bm);
        return bm;
    }


}
