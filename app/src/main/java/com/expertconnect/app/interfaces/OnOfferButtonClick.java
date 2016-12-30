package com.expertconnect.app.interfaces;

import android.view.View;

import com.expertconnect.app.models.BrowseEnquiryResult;
import com.expertconnect.app.models.TeacherCategory;

import java.util.ArrayList;

/**
 * Created by chinar on 9/11/16.
 */
public interface OnOfferButtonClick {
    void onItemClick(View v, int position, ArrayList<TeacherCategory> browseResult, String tag);
}
