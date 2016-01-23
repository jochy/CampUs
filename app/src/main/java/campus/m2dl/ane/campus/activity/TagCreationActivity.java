package campus.m2dl.ane.campus.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.activity.adapter.TagListAdapter;
import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.model.TagImg;
import campus.m2dl.ane.campus.service.MessageService;

public class TagCreationActivity extends AppCompatActivity {

    private LatLng currentPosition;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = (Bitmap) MessageService.message;
        setContentView(R.layout.activity_tag_creation);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new TagListAdapter(getBaseContext(), R.layout.list_adapter, Arrays.asList(TagImg.values())));
        spinner.setSelection(TagImg.values().length - 1);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewTag);
        imageView.setImageBitmap(bitmap);

        currentPosition = MessageService.currentPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_creation, menu);
        return true;
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

    public void sendNewPoi(View v) {
        POI poi = new POI();
        poi.image = bitmap;
        poi.position = currentPosition;
        poi.sender = null; // FIXME : current user !
        poi.description = ((EditText) findViewById(R.id.descriptionField)).getText().toString();
        poi.tags = new ArrayList<>();
        for (String s : ((EditText) findViewById(R.id.descriptionField)).getText().toString().split(" ")) {
            poi.tags.add(s);
        }
        poi.date = new Date();
        poi.tagImg = (TagImg) ((Spinner) findViewById(R.id.spinner)).getSelectedItem();

        // TODO
    }
}
