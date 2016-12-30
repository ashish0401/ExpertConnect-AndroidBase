package com.expertconnect.app.fragments;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.expertconnect.app.R;
import com.expertconnect.app.adapters.NavigationDrawerAdapter;
import com.expertconnect.app.constants.AllConstants;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.models.NavigationDrawerItem;
import com.expertconnect.app.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private ImageView mImgProfile;
    private TextView mTvName;
    private static String[] titlesTopMenu = null;
    private static TypedArray imagesTopMenu = null;

    private FragmentDrawerListener drawerListener;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titlesTopMenu = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        imagesTopMenu = getActivity().getResources().obtainTypedArray(R.array.nav_drawer_images);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        initView(layout);
        setUserData();
        bindDataToAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        mImgProfile = (ImageView) view.findViewById(R.id.img_user_id);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
    }

    private void setUserData() {

        if (AllConstants.isFacebook) {
            // String profilePic = Utils.getSharedPrefString(Constants.PROFILE_PIC, getActivity());
            String firstname = Utils.getSharedPrefString(Constants.FIRST_NAME, getActivity());
            String lastname = Utils.getSharedPrefString(Constants.LAST_NAME, getActivity());
            String id = Utils.getSharedPrefString(Constants.FACEBOOK_ID, getActivity());
            Picasso.with(getActivity())
                    .load("https://graph.facebook.com/" + id + "/picture?type=large")
                    .into(mImgProfile);
            mTvName.setText(firstname + " " + lastname);
        } else {
            String profilePic = Utils.getSharedPrefString(Constants.PROFILE_PIC, getActivity());
            String firstname = Utils.getSharedPrefString(Constants.FIRST_NAME, getActivity());
            String lastname = Utils.getSharedPrefString(Constants.LAST_NAME, getActivity());
            if (!(profilePic.length() > 0)) {
                mImgProfile.setImageResource(R.drawable.default_profile_img);
            } else {
                Picasso.with(getActivity())
                        .load(profilePic)
                        .into(mImgProfile);

            }
            mTvName.setText(firstname + " " + lastname);
        }
    }


    private void bindDataToAdapter() {
        // Bind adapter to recycler view object
        recyclerView.setAdapter(new NavigationDrawerAdapter(getDrawerData(), getActivity()));
    }

    private List<NavigationDrawerItem> getDrawerData() {
        List<NavigationDrawerItem> items = new ArrayList<>();
        for (int i = 0; i < titlesTopMenu.length; i++) {
            items.add(new NavigationDrawerItem(titlesTopMenu[i], imagesTopMenu.getResourceId(i, -1)));
        }

        return items;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}
