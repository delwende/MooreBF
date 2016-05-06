package com.example.eliane.moore.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliane.moore.R;
import com.example.eliane.moore.model.Product;


import java.util.List;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class ListProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<Product> mProductList;
    private MediaPlayer mediaPlayer;
    private boolean playFirst = true;
    public ListProductAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.item_listview, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_product_name);

        TextView tvDescription = (TextView)v.findViewById(R.id.tv_product_description);
        tvName.setText(mProductList.get(position).getName());
       ImageButton imgbt=(ImageButton)v.findViewById(R.id.imbt);
        String m=mProductList.get(position).getMusic();
        mediaPlayer = MediaPlayer.create(mContext,R.raw.kalimba);
        Resources res = mContext.getResources();
        int soundId = res.getIdentifier("music1", "raw", mContext.getPackageName());
       // playSound(mediaPlayer, soundId);
        mediaPlayer = MediaPlayer.create(mContext,soundId);
       // ImageButton imbt =(ImageButton)viewg.findViewById(R.id.imbt);
        imgbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true == playFirst) {
                    mediaPlayer.start();
                    playFirst=false;
                }
                else {
                    mediaPlayer.stop();
                   // mediaPlayer.release();
                    playFirst=true;
                }
                }

        });

       // tvPrice.setText(String.valueOf(mProductList.get(position).getPrice()) + " $");
        tvDescription.setText(mProductList.get(position).getDescription());
        return v;
    }



}
