package campus.m2dl.ane.campus.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.model.TagImg;

/**
 * Created by Alexandre on 22/01/2016.
 */
public class TagListAdapter extends ArrayAdapter<TagImg> {

    public TagListAdapter(Context context, int resource, List<TagImg> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_tag_adapter, null);
        }

        TagImg tag = getItem(position);

        if (tag != null) {
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView2);
            TextView textView = (TextView) v.findViewById(R.id.textViewTag);

            imageView.setImageResource(tag.resourceId);
            textView.setText(tag.text);
        }


        return v;
    }
}
