package campus.m2dl.ane.campus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import campus.m2dl.ane.campus.listener.CampUsLocationListener;

public class CampUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_us);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new CampUsLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        System.out.println("configured");

        // (1242; 1390) -> (43,56291; 1,46613)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camp_us, menu);
        return true;
    }

    public void updateLocation(double lat, double lon) {
        ImageView imageView = (ImageView) findViewById(R.id.map);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache().copy(imageView.getDrawingCache().getConfig(), true);
        // 1242  |
        // 43    | lat
        float x = (float) (lat * 1242 / 43.56291);
        float y = (float) (lon * 1390 / 1.46613);

        Toast.makeText(getBaseContext(),x + " - " + y,Toast.LENGTH_LONG).show();

        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        BitmapShader shader = new BitmapShader(bmOverlay,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        Canvas canvas = new Canvas(bmOverlay);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setShader(shader);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawCircle(x, y, 100, paint);

        imageView.setImageBitmap(bmOverlay);
        //imageView.buildDrawingCache();
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
