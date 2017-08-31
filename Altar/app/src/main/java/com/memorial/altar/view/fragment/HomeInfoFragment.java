package com.memorial.altar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 8. 31..
 */

public class HomeInfoFragment extends Fragment {

    private static final String TAG = HomeInfoFragment.class.getSimpleName();

    private static final String ARG_INFO_PAGE = "info_page";

    public static HomeInfoFragment newInstance(int infoPage) {

        Bundle args = new Bundle();
        args.putInt(ARG_INFO_PAGE, infoPage);

        HomeInfoFragment fragment = new HomeInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mInfoPage;
    private int[] mInfoPageResId = {
            R.drawable.img_home_info_page_1, R.drawable.img_home_info_page_2,
            R.drawable.img_home_info_page_3, R.drawable.img_home_info_page_4,
    };
    private int[] mInfoImageViewResId = {
            R.id.info_page_image_view_1, R.id.info_page_image_view_2,
            R.id.info_page_image_view_3, R.id.info_page_image_view_4,
    };
    private ImageView mInfoPageImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfoPage = getArguments().getInt(ARG_INFO_PAGE, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_info, container, false);
        mInfoPageImageView = view.findViewById(mInfoImageViewResId[mInfoPage]);
        mInfoPageImageView.setImageResource(mInfoPageResId[mInfoPage]);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
