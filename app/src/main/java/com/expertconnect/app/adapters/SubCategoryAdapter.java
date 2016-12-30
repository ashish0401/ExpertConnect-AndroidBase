package com.expertconnect.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.expertconnect.app.R;
import com.expertconnect.app.activities.SubCategoryList;
import com.expertconnect.app.fragments.BrowseEnquiry;
import com.expertconnect.app.models.SubCategory;
import com.expertconnect.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by chinar on 25/10/16.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private ArrayList<SubCategory> mAlSubList= new ArrayList<>();
    private Context mContext;
    private SubCategoryList mListener;

    public void setOnItemClickListener(SubCategoryList listener) {
        this.mListener = listener;
    }

    public SubCategoryAdapter(Context mContext, ArrayList<SubCategory> videolist) {

        this.mContext=mContext;
        this.mAlSubList=videolist;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent
                .getContext());
        final View sView = mInflater.inflate(R.layout.sub_category_list, parent,
                false);

        return    new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvSubList.setText(mAlSubList.get(position).getSub_category_name());
        holder.recyclerviewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, position,mAlSubList);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mAlSubList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvSubList;
        LinearLayout recyclerviewMain;
        public ViewHolder(View itemView) {

            super(itemView);

            tvSubList=(TextView) itemView
                    .findViewById(R.id.tv_sublist);
            recyclerviewMain=(LinearLayout) itemView
                    .findViewById(R.id.ll_rvmain);
            tvSubList.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

        }

        @Override
        public void onClick(View v) {

        }
    }
}

