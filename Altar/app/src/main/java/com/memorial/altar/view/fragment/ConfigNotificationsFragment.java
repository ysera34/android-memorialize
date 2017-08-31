package com.memorial.altar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 8. 29..
 */

public class ConfigNotificationsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ConfigNotificationsFragment.class.getSimpleName();

    public static ConfigNotificationsFragment newInstance() {

        Bundle args = new Bundle();

        ConfigNotificationsFragment fragment = new ConfigNotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mRegisterLayout;
    private RelativeLayout mAltarCommentLayout;
    private RelativeLayout mObituaryLayout;
    private CheckBox mRegisterCheckBox;
    private CheckBox mAltarCommentCheckBox;
    private CheckBox mObituaryCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_notifications, container, false);
        mRegisterLayout = view.findViewById(R.id.register_layout);
        mRegisterLayout.setOnClickListener(this);
        mAltarCommentLayout = view.findViewById(R.id.altar_comment_layout);
        mAltarCommentLayout.setOnClickListener(this);
        mObituaryLayout = view.findViewById(R.id.obituary_layout);
        mObituaryLayout.setOnClickListener(this);
        mRegisterCheckBox = view.findViewById(R.id.register_check_box);
        mAltarCommentCheckBox = view.findViewById(R.id.altar_comment_check_box);
        mObituaryCheckBox = view.findViewById(R.id.obituary_check_box);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_layout:
                mRegisterCheckBox.setChecked(!mRegisterCheckBox.isChecked());
                break;
            case R.id.altar_comment_layout:
                mAltarCommentCheckBox.toggle();
                break;
            case R.id.obituary_layout:
                mObituaryCheckBox.performClick();
                break;
        }
    }
}
