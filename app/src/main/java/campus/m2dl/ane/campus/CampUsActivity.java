package campus.m2dl.ane.campus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import campus.m2dl.ane.campus.listener.CampUsLocationListener;

public class CampUsActivity extends AppCompatActivity {

    public static final int POS_SIZE = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new CampUsLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        // updateLocation(43.56291, 1.46613);
        // (1242; 1390) -> (43,56291; 1,46613)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camp_us, menu);
        return true;
    }

    public void updateLocation(double lat, double lon) {
        // Load file resolution
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.map_50_bmp, options);
        int width = options.outWidth;
        int height = options.outHeight;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_50_bmp);

        //Point refPix = new Point(1242* bitmap.getWidth() / width, 1390 * bitmap.getHeight() / height);
        //Point refPix = new Point(904 * bitmap.getWidth() / width, 1013 * bitmap.getHeight() / height);
        Point refPix = new Point(443 * bitmap.getWidth() / width, 507 * bitmap.getHeight() / height);

        float x = (float) (lat * refPix.x / 43.56291);
        float y = (float) (lon * refPix.y / 1.46613);

        Toast.makeText(getBaseContext(), x + " - " + y, Toast.LENGTH_LONG).show();

        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        bmOverlay.setPixel((int) x, (int) y, Color.RED);
        Canvas canvas = new Canvas(bmOverlay);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawCircle(x + POS_SIZE / 2, y, POS_SIZE, paint);

        ImageView imageView = (ImageView) findViewById(R.id.map);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), bmOverlay));
        imageView.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
