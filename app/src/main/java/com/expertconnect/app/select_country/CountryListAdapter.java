package com.expertconnect.app.select_country;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.expertconnect.app.R;
import java.util.ArrayList;
import java.util.Locale;


public class CountryListAdapter extends BaseAdapter {
    ArrayList<CountryModel> mCountryList, mTempCountryList;
    Context context;


    public CountryListAdapter(Context context, ArrayList<CountryModel> countryNameNumberArrayList) {
        this.mCountryList = countryNameNumberArrayList;
        this.context = context;
//        this.mTempCountryList = countryNameNumberArrayList;
        mTempCountryList = new ArrayList<>();
        mTempCountryList.addAll(mCountryList);
    }

    /* get Items Count of arraylist */
    @Override
    public int getCount() {
        return mCountryList.size();
    }

    /* get object item id of current position */
    @Override
    public Object getItem(int position) {
        return mCountryList.get(position);
    }

    /*get item id of current position*/
    @Override
    public long getItemId(int position) {
        return position;
    }


    /* Add row layout *//* Add row layout */
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final CountryAdapterHolder holder;

        if (view == null) {

            holder = new CountryAdapterHolder();
            view = LayoutInflater.from(context).inflate(R.layout.flag_country_row_list, parent, false);
            holder.name = (TextView) view.findViewById(R.id.tvCountryName);
            holder.number = (TextView) view.findViewById(R.id.tvCountryCode);
            holder.ivFlag = (ImageView) view.findViewById(R.id.ivflag5);

        } else {
            holder = (CountryAdapterHolder) view.getTag();
        }
        view.setTag(holder);


        holder.name.setText(mCountryList.get(position).getName());
        holder.number.setText(mCountryList.get(position).getCountryCodeStr());
        holder.ivFlag.setImageResource(mCountryList.get(position).getResId());
//        holder.ivFlag.setBackgroundResource(mCountryList.get(position).getResId());

        return view;
    }

    /**
     * Filter for search
     */
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        mCountryList.clear();
        if (charText.length() == 0) {
            mCountryList.addAll(mTempCountryList);
        } else {
            for (CountryModel obj : mTempCountryList) {
                String s = "";
                s = obj.getName();
                String codeCountry = obj.getCountryCodeStr();
                if (s.toLowerCase(Locale.getDefault()).contains(charText) || codeCountry.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mCountryList.add(obj);
                }
            }

        }

        notifyDataSetChanged();
    }

}

class CountryAdapterHolder {
    TextView name, localName, number;
    ImageView ivFlag;
}
