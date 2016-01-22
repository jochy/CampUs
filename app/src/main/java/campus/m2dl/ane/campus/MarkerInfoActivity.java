package campus.m2dl.ane.campus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import campus.m2dl.ane.campus.model.POI;
import campus.m2dl.ane.campus.service.MessageService;

public class MarkerInfoActivity extends AppCompatActivity {

    private POI poi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poi = (POI) MessageService.message;

        setContentView(R.layout.activity_marker_information);
        updateView();
    }

    private void updateView() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if (poi.image != null) {
            imageView.setImageBitmap(poi.image);
        }

        TextView textView1 = (TextView) findViewById(R.id.usernameField);
        if (poi.sender != null) {
            textView1.setText(textView1.getText() + " " + poi.sender.getFirstname() + " " + poi.sender.getLastname());
        }

        TextView textView2 = (TextView) findViewById(R.id.dateField);
        if (poi.date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            textView2.setText(textView2.getText() + " " + sdf.format(poi.date));
        }

        ListView listView = (ListView) findViewById(R.id.tagListView);
        if (poi.tags != null) {
            ListAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.list_adapter,
                    poi.tags);

            listView.setAdapter(adapter);
        }

        TextView textView3 = (TextView) findViewById(R.id.descriptionTV);
        if (poi.description != null) {
            textView3.setText(poi.description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_marker_information, menu);
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
}
