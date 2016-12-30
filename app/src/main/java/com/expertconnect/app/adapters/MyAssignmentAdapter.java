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
import com.expertconnect.app.fragments.BrowseExpert;
import com.expertconnect.app.fragments.MyAssignment;
import com.expertconnect.app.models.TeacherCategory;
import com.expertconnect.app.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chinar on 22/11/16.
 */
public class MyAssignmentAdapter extends RecyclerView.Adapter<MyAssignmentAdapter.ViewHolder> {

    private ArrayList<TeacherCategory> mAlBrowse= new ArrayList<>();
    private Context mContext;
    OnItemClickListener mItemClickListener;
    private MyAssignment mListener;

    public void setOnItemClickListener(MyAssignment listener) {
        this.mListener = listener;
    }


    public MyAssignmentAdapter(Context mContext, ArrayList<TeacherCategory> alBrowseEnq) {

        this.mContext=mContext;
        this.mAlBrowse=alBrowseEnq;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent
                .getContext());
        final View sView = mInflater.inflate(R.layout.my_assignment_list, parent,
                false);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String s=null;
        holder.tvRequirement.setText(mAlBrowse.get(position).getFirstname() +" "+ mAlBrowse.get(position).getLastname());
        holder.tvStatus.setText("IN PROGRESS");
        holder.tvSubjectName.setText(mAlBrowse.get(position).getSub_category());
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

        holder.tvContactNo.setText(mAlBrowse.get(position).getCountry_code()+" "+mAlBrowse.get(position).getMobile_no());


        if(mAlBrowse.get(position).getType().equals("confirm")){
            holder.mBtnComplete.setVisibility(View.VISIBLE);
            holder.mLinearJoin.setVisibility(View.GONE);
            holder.mBtnConfirmReject.setVisibility(View.GONE);
            holder.mBtnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, position,mAlBrowse , "complete");
                    }
                }
            });
        }

        else if(mAlBrowse.get(position).getType().equals("accept") && !(mAlBrowse.get(position).isConfirm_tag()) &&  !(mAlBrowse.get(position).isConfirm_reject_tag())){
            holder.mBtnComplete.setVisibility(View.GONE);
            holder.mBtnConfirmReject.setVisibility(View.GONE);
            holder.mLinearJoin.setVisibility(View.VISIBLE);
            holder.mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, position,mAlBrowse , "confirm");
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
        else {
            if(mAlBrowse.get(position).isConfirm_tag()){
                holder.mLinearJoin.setVisibility(View.GONE);
                holder.mBtnComplete.setVisibility(View.GONE);
                holder.mBtnConfirmReject.setVisibility(View.VISIBLE);
                holder.mBtnConfirmReject.setText("Confirmed");
                holder.mBtnConfirmReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "confirmed");
                        }
                    }
                });
            }

            else {
                holder.mLinearJoin.setVisibility(View.GONE);
                holder.mBtnComplete.setVisibility(View.GONE);
                holder.mBtnConfirmReject.setVisibility(View.VISIBLE);
                holder.mBtnConfirmReject.setText("Rejected");
                holder.mBtnConfirmReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, position,mAlBrowse , "rejected");
                        }
                    }
                });
            }

        }


    }



    @Override
    public int getItemCount() {
        return mAlBrowse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRequirement;
        TextView tvStatus;
        TextView tvSubject;
        TextView tvSubjectName;
        TextView tvPlace;
        TextView tvLocation;
        TextView tvCoaching;
        TextView tvPreference;
        TextView tvContact;
        TextView tvContactNo;
        ImageView mProfile;
        Button mBtnComplete;
        Button mBtnConfirm;
        Button mBtnReject;
        Button mBtnConfirmReject;
        LinearLayout mLinearJoin;

        public ViewHolder(View itemView) {

            super(itemView);

            tvRequirement=(TextView) itemView
                    .findViewById(R.id.tv_requirement);
            tvStatus=(TextView) itemView
                    .findViewById(R.id.tv_status);
            tvSubject=(TextView) itemView
                    .findViewById(R.id.tv_Subject);
            tvSubjectName=(TextView) itemView
                    .findViewById(R.id.tv_subname);
            tvLocation=(TextView) itemView
                    .findViewById(R.id.tv_expert_location);
            tvPlace=(TextView) itemView
                    .findViewById(R.id.tv_place);
            tvCoaching=(TextView) itemView
                    .findViewById(R.id.tv_fee_coaching);
            tvPreference=(TextView) itemView
                    .findViewById(R.id.tv_preference);
            tvContact=(TextView) itemView
                    .findViewById(R.id.tv_contact);
            tvContactNo=(TextView) itemView
                    .findViewById(R.id.tv_contactnum);
            mProfile=(ImageView) itemView
                    .findViewById(R.id.img_profile_image);
            mBtnComplete=(Button) itemView
                    .findViewById(R.id.btn_complete);
            mBtnConfirm=(Button) itemView
                    .findViewById(R.id.btn_confirm);
            mBtnReject=(Button) itemView
                    .findViewById(R.id.btn_reject);
            mLinearJoin=(LinearLayout) itemView
                    .findViewById(R.id.ll_joinbuttons);
            mBtnConfirmReject=(Button) itemView
                    .findViewById(R.id.btn_confirmed_rejected);

            tvRequirement.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPreference.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvStatus.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvPlace.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvSubject.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvSubjectName.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvLocation.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvCoaching.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvContact.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            tvContactNo.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnComplete.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnConfirm.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnReject.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));
            mBtnConfirmReject.setTypeface(Utils.setFonts(mContext, "fonts/AvenirLTStd-Light.otf"));




            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,getAdapterPosition());
            }

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view,int position);
    }

    public void SetOnItemClickListener(
            final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}




