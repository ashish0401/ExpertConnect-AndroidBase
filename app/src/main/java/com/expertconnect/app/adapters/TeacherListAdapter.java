package com.expertconnect.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.expertconnect.app.R;
import com.expertconnect.app.activities.TeacherList;
import com.expertconnect.app.fragments.BrowseExpert;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chinar on 16/11/16.
 */
public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.ViewHolder> {

    private ArrayList<TeacherCategory> mAlBrowse= new ArrayList<>();
    private Context mContext;
    private TeacherList mListener;

    public void setOnItemClickListener(TeacherList listener) {
        this.mListener = listener;
    }

    public TeacherListAdapter(Context mContext, ArrayList<TeacherCategory> alBrowseEnq) {

        this.mContext=mContext;
        this.mAlBrowse=alBrowseEnq;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent
                .getContext());
        final View sView = mInflater.inflate(R.layout.teacher_adapter_list, parent,
                false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String s=null;
        holder.tvRequirement.setText(mAlBrowse.get(position).getFirstname() +" "+ mAlBrowse.get(position).getLastname());
        holder.tvSubCatname.setText(mAlBrowse.get(position).getSub_category());
        holder.tvFee.setText(mAlBrowse.get(position).getRate()+" AU$");
        holder.tvPlace.setText(mAlBrowse.get(position).getLocation());
        int size=mAlBrowse.get(position).getCoaching_details().size();
        if(size!=0){
            s=mAlBrowse.get(position).getCoaching_details().get(0).getVenue();
            for(int i=1;i<mAlBrowse.get(position).getCoaching_details().size();i++){
                s+=","+mAlBrowse.get(position).getCoaching_details().get(i).getVenue();
            }
            holder.tvPreference.setText(s);
        }
        if(mAlBrowse.get(position).getReg_type().equals("2")){
           String id=mAlBrowse.get(position).getSocial_id();
            Picasso.with(mContext)
                    .load("https://graph.facebook.com/" + id + "/picture?type=large")
                    .into(holder.mProfile);
        }
        else {
            if(!(mAlBrowse.get(position).getProfile_pic().length()>0)){
                holder.mProfile.setImageResource(R.drawable.default_profile_img);
            }

            else {
                Picasso.with(mContext)
                        .load(mAlBrowse.get(position).getProfile_pic())
                        .into(holder.mProfile);
            }
        }

        if(mAlBrowse.get(position).getFlag()==1){
            mAlBrowse.get(position).setTag(true);
            holder.mBtnSendoffer.setBackgroundColor(mContext.getResources().getColor(R.color.request_gray));
        }

        holder.mBtnSendoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, position,mAlBrowse , "request");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAlBrowse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRequirement;
        TextView tvFee;
        TextView tvExpertize;
        TextView tvPlace;
        TextView tvPreference;
        TextView tvFeeDetail;
        TextView tvLocation;
        TextView tvCoaching;
        ImageView mProfile;
        Button mBtnSendoffer;
        TextView tvSubCatname;

        public ViewHolder(View itemView) {

            super(itemView);

            tvRequirement=(TextView) itemView
                    .findViewById(R.id.tv_requirement);
            tvFee=(TextView) itemView
                    .findViewById(R.id.tv_fee);
            tvExpertize=(TextView) itemView
                    .findViewById(R.id.tv_expertise);
            tvSubCatname=(TextView) itemView
                    .findViewById(R.id.tv_subcatname);
            tvFeeDetail=(TextView) itemView
                    .findViewById(R.id.tv_feeDetail);
            tvLocation=(TextView) itemView
                    .findViewById(R.id.tv_expert_location);
            tvCoaching=(TextView) itemView
                    .findViewById(R.id.tv_fee_coaching);
            tvPlace=(TextView) itemView
                    .findViewById(R.id.tv_place);
            tvPreference=(TextView) itemView
                    .findViewById(R.id.tv_preference);
            mProfile=(ImageView) itemView
                    .findViewById(R.id.img_profile_image);
            mBtnSendoffer=(Button) itemView
                    .findViewById(R.id.btn_sendoffer);

            tvRequirement.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPreference.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvFee.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPlace.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvExpertize.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvFeeDetail.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvLocation.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvCoaching.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnSendoffer.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvSubCatname.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}



