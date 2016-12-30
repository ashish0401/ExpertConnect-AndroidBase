package com.expertconnect.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.expertconnect.app.R;
import com.expertconnect.app.models.Category;
import com.expertconnect.app.utils.Utils;

import java.util.ArrayList;


public class MainCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Category> mAlMainCat=new ArrayList<>();

    public MainCategoryAdapter(Context c,ArrayList<Category> main) {
        mContext = c;
        this.mAlMainCat = main;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAlMainCat.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.maincategory_grid_row, null);


        } else {
            grid = (View) convertView;
        }

        TextView textView = (TextView) grid.findViewById(R.id.tv_main);
        textView.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
        textView.setText(mAlMainCat.get(position).getCategoryName());
        return grid;
    }
}
