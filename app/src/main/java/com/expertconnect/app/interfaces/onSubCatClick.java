package com.expertconnect.app.interfaces;

import android.view.View;


import com.expertconnect.app.models.SubCategory;

import java.util.ArrayList;

/**
 * Created by chinar on 14/11/16.
 */
public interface onSubCatClick {
    void onItemClick(View v, int position, ArrayList<SubCategory> browseResult);
}


