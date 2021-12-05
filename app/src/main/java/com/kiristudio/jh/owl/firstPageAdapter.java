package com.kiristudio.jh.owl;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class firstPageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mInflater;

    public firstPageAdapter(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = inflater;

    }


    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        View vs = null;

        if (position == 0) {
            vs = mInflater.inflate(R.layout.first1, null);


            container.addView(vs);

        } else if (position == 1) {

            vs = mInflater.inflate(R.layout.first2, null);
            container.addView(vs);

        } else if (position == 2) {

            vs = mInflater.inflate(R.layout.first3, null);
            container.addView(vs);
        } else {

            Log.e("error", "pager is over");
        }


        return vs;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}


