package campus.m2dl.ane.campus.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.model.TagImg;

/**
 * Created by Alexandre on 22/01/2016.
 */
public class TagListAdapter extends ArrayAdapter<TagImg> {
    public TagListAdapter(Context context, int resource) {
        super(context, resource);
    }

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
            /*

            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt1 != null) {
                tt1.setText(p.getId());
            }

            if (tt2 != null) {
                tt2.setText(p.getCategory().getId());
            }

            if (tt3 != null) {
                tt3.setText(p.getDescription());
            }
             */
        }


        return v;
    }
}
