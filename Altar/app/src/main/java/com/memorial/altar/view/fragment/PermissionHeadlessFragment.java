package com.memorial.altar.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class PermissionHeadlessFragment extends Fragment {

    public static final String TAG = PermissionHeadlessFragment.class.getSimpleName();

    private static final String ARG_REQUEST_CODE = "request_code";
    private static final String ARG_REQUEST_PERMISSION_ID = "request_permission";

    public static PermissionHeadlessFragment newInstance(
            int requestCode, int requestPermissionId) {

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putInt(ARG_REQUEST_PERMISSION_ID, requestPermissionId);

        PermissionHeadlessFragment fragment = new PermissionHeadlessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final int CONTACT_PERMISSION_REQUEST = 1001;
    public static final int STORAGE_PERMISSION_REQUEST = 1002;
    private int mRequestCode;
    private int mRequestPermissionId;
    private String mRequestPermission;
    private PermissionCallbackListener mPermissionCallbackListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestCode = getArguments().getInt(ARG_REQUEST_CODE);
        mRequestPermissionId = getArguments().getInt(ARG_REQUEST_PERMISSION_ID);
        switch (mRequestPermissionId) {
            case CONTACT_PERMISSION_REQUEST:
                Log.i(TAG, "onCreate: request permission ID: " + "CONTACT_PERMISSION_REQUEST");
                mRequestPermission = Manifest.permission.READ_CONTACTS;
                break;
            case STORAGE_PERMISSION_REQUEST:
                Log.i(TAG, "onCreate: request permission ID: " + "STORAGE_PERMISSION_REQUEST");
                mRequestPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
                break;
        }
        checkRequestedPermission();
    }

    private void checkRequestedPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), mRequestPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mRequestPermission)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.permission_explanation);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(new String[]{mRequestPermission}, mRequestPermissionId);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    requestPermissions(new String[]{mRequestPermission}, mRequestPermissionId);
                }
            } else {
                mPermissionCallbackListener.onPermissionCallback(mRequestCode, mRequestPermissionId, true);
                onDetach();
            }
        } else {
            mPermissionCallbackListener.onPermissionCallback(mRequestCode, mRequestPermissionId, true);
            onDetach();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            mPermissionCallbackListener.onPermissionCallback(requestCode, true);
//        } else {
//            mPermissionCallbackListener.onPermissionCallback(requestCode, false);
//        }
        if (grantResults.length > 0) {
            mPermissionCallbackListener.onPermissionCallback(mRequestCode,
                    requestCode, grantResults[0] == PackageManager.PERMISSION_GRANTED);
            onDetach();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: PermissionHeadlessFragment");
        try {
            mPermissionCallbackListener = (PermissionCallbackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PermissionCallbackListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallbackListener = null;
        Log.i(TAG, "onDetach: PermissionHeadlessFragment");
    }

    public interface PermissionCallbackListener {
        void onPermissionCallback(int requestCode, int requestPermissionId, boolean isGranted);
    }
}
