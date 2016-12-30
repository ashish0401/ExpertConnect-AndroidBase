package com.expertconnect.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.expertconnect.app.R;
import com.expertconnect.app.activities.Notification;
import com.expertconnect.app.fragments.BrowseEnquiry;
import com.expertconnect.app.models.BrowseEnquiryResult;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.utils.Utils;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by chinar on 9/11/16.
 */
public class BrowseEnquiryAdapter extends RecyclerView.Adapter<BrowseEnquiryAdapter.ViewHolder> {

    private ArrayList<TeacherCategory> mAlBrowse= new ArrayList<>();
    private Context mContext;
    private BrowseEnquiry mListener;
    private Notification mListenerNotify;
    private boolean isSent=false;

    public void setOnItemClickListener(BrowseEnquiry listener) {
        this.mListener = listener;
    }

    public void setOnItemClickListenerNotifiy(Notification listener) {
        this.mListenerNotify = listener;
    }

    public BrowseEnquiryAdapter(Context mContext, ArrayList<TeacherCategory> alBrowseEnq, boolean b) {

        this.mContext=mContext;
        this.mAlBrowse=alBrowseEnq;
        this.isSent=b;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent
                .getContext());
        final View sView = mInflater.inflate(R.layout.browse_enquiry_list, parent,
                false);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String s=null;

        holder.tvRequirement.setText(mAlBrowse.get(position).getFirstname()+" "+mAlBrowse.get(position).getLastname());
        holder.tvSubcat.setText(mAlBrowse.get(position).getSub_category());
        holder.tvPlace.setText(mAlBrowse.get(position).getLocation());
        int size=mAlBrowse.get(position).getCoaching_details().size();
        int rate=mAlBrowse.get(position).getRate_details().size();
        if(size!=0){
            s=mAlBrowse.get(position).getCoaching_details().get(0).getVenue();
            for(int i=1;i<mAlBrowse.get(position).getCoaching_details().size();i++){
                s+=","+mAlBrowse.get(position).getCoaching_details().get(i).getVenue();
            }
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

        if(isSent){
            mAlBrowse.get(position).setTag(true);
            holder.tvCoaching.setText(R.string.fee);
            if(rate!=0){
                holder.tvPreference.setText(mAlBrowse.get(position).getRate_details().get(0).getRate()+" AU$");
            }
            else {
                holder.tvPreference.setText("0 AU$");
            }

            holder.mBtnRequest.setVisibility(View.VISIBLE);
            holder.mLinearJoin.setVisibility(View.GONE);
            holder.mBtnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, position,mAlBrowse , "request");
                    }
                }
            });
        }
        else {

            if(mAlBrowse.get(position).getType().equals("accept")){
                mAlBrowse.get(position).setAccept_tag(true);
            }

            if (mAlBrowse.get(position).getType().equals("reject")) {
                mAlBrowse.get(position).setAccept_reject_tag(true);
            }

            if(mAlBrowse.get(position).isAccept_tag()){
                holder.mLinearJoin.setVisibility(View.GONE);
                holder.mBtnRequest.setVisibility(View.GONE);
                holder.mBtnAcceptReject.setVisibility(View.VISIBLE);
                holder.mBtnAcceptReject.setText("Accepted");

                holder.mBtnAcceptReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "accepted");
                        }
                    }
                });

            }
            else if(mAlBrowse.get(position).isAccept_reject_tag()){
                holder.mLinearJoin.setVisibility(View.GONE);
                holder.mBtnRequest.setVisibility(View.GONE);
                holder.mBtnAcceptReject.setVisibility(View.VISIBLE);
                holder.mBtnAcceptReject.setText("Rejected");
                holder.mBtnAcceptReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "rejected");
                        }
                    }
                });
            }

            else {
                holder.mLinearJoin.setVisibility(View.VISIBLE);
                holder.mBtnRequest.setVisibility(View.GONE);
                holder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "accept");
                        }
                    }
                });
                holder.mBtnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "reject");
                        }
                    }
                });
            }
            holder.tvCoaching.setText(R.string.coaching1);
            holder.tvPreference.setText(s);

        }
    }

    @Override
    public int getItemCount() {
        return mAlBrowse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRequirement;
        TextView tvSubcat;
        TextView tvPlace;
        TextView tvPreference;
        ImageView mProfile;
        Button mBtnAccept;
        Button mBtnReject;
        Button mBtnRequest;
        Button mBtnAcceptReject;
        TextView tvCoaching;
        TextView tvLocation;
        LinearLayout mLinearJoin;

        public ViewHolder(View itemView) {

            super(itemView);

            tvRequirement=(TextView) itemView
                    .findViewById(R.id.tv_requirement);
            tvSubcat=(TextView) itemView
                    .findViewById(R.id.tv_subcat);
            tvPlace=(TextView) itemView
                    .findViewById(R.id.tv_place);
            tvPreference=(TextView) itemView
                    .findViewById(R.id.tv_preference);
            mProfile=(ImageView) itemView
                    .findViewById(R.id.img_profile_image);
            mBtnAccept=(Button) itemView
                    .findViewById(R.id.btn_accept);
            mBtnAcceptReject=(Button) itemView
                    .findViewById(R.id.btn_accepted_rejected);
            mBtnReject=(Button) itemView
                    .findViewById(R.id.btn_reject);
            mBtnRequest=(Button) itemView
                    .findViewById(R.id.btn_request);
            tvCoaching=(TextView) itemView
                    .findViewById(R.id.tv_coaching);
            tvLocation=(TextView) itemView
                    .findViewById(R.id.tv_expert_location);
            mLinearJoin=(LinearLayout) itemView
                    .findViewById(R.id.ll_joinbuttons);

            tvRequirement.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvSubcat.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPlace.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPreference.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvCoaching.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvLocation.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnAccept.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnReject.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnRequest.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnAcceptReject.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}


