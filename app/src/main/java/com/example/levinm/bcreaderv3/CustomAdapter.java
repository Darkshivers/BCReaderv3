package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by levinm on 17/07/2017.
 */

public class CustomAdapter extends BaseAdapter {


    String[] result;
    Context context;

    int[] imageId;

    private static LayoutInflater inflater=null;

    public CustomAdapter(ScannedProduct scannedproduct, String[] wheelnamelist, int[] wheelimages ){


        result= wheelnamelist;
        context = scannedproduct;
        imageId=wheelimages;

        inflater = (LayoutInflater)context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));

    }


    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.wheel_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You Clicked " +result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }
}
