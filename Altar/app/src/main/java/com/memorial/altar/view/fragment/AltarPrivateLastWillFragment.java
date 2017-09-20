package com.memorial.altar.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.LastWill;
import com.memorial.altar.util.UserSharedPreferences;
import com.memorial.altar.view.activity.BillingStarActivity;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class AltarPrivateLastWillFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{

    private static final String TAG = AltarPrivateLastWillFragment.class.getSimpleName();

    private static final String ARG_PRIVATE_LAST_WILL = "private_last_will";
    private static final String ARG_PRIVATE_LAST_WILL_SEND_TO = "private_last_will_send_to";
    private static final String ARG_PRIVATE_LAST_WILL_MESSAGE = "private_last_will_message";

    public static AltarPrivateLastWillFragment newInstance() {

        Bundle args = new Bundle();

        AltarPrivateLastWillFragment fragment = new AltarPrivateLastWillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AltarPrivateLastWillFragment newInstance(LastWill lastWill) {

        Bundle args = new Bundle();
//        args.putString(ARG_PRIVATE_LAST_WILL_SEND_TO, sendTo);
//        args.putString(ARG_PRIVATE_LAST_WILL_MESSAGE, privateLastWillMessage);
        args.putSerializable(ARG_PRIVATE_LAST_WILL, lastWill);

        AltarPrivateLastWillFragment fragment = new AltarPrivateLastWillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    private InputMethodManager mInputMethodManager;
    private ImageView mAltarPrivateLastWillCompleteImageView;
    private TextView mAltarPrivateLastWillLimitTextView;
    private TextView mAltarPrivateLastWillCountTextView;
    private AltarPrivateLastWillEditTextWatcher mEditTextWatcher;
    private TextView mAltarPrivateLastWillSendToTextView;
    private EditText mAltarPrivateLastWillEditText;
    private TextView mAltarPrivateLastWillConfirmButtonTextView;

    private LastWill mLastWill;
    private String mAltarPrivateLastWillSendTo;
    private String mAltarPrivateLastWillMessage;
    private int mStarCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mAltarPrivateLastWillSendTo = getArguments().getString(ARG_PRIVATE_LAST_WILL_SEND_TO, null);
//        mAltarPrivateLastWillMessage = getArguments().getString(ARG_PRIVATE_LAST_WILL_MESSAGE, null);
        mLastWill = (LastWill) getArguments().getSerializable(ARG_PRIVATE_LAST_WILL);
        mAltarPrivateLastWillSendTo = mLastWill.getSendTo();
        mAltarPrivateLastWillMessage = mLastWill.getMessage();

        mStarCount = UserSharedPreferences.getStoredStar(getActivity());
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.fragment_dialog_altar_private_last_will, null);
        mAltarPrivateLastWillCompleteImageView = view.findViewById(R.id.altar_private_last_will_complete_image_view);
        mAltarPrivateLastWillCompleteImageView.setOnClickListener(this);
        mAltarPrivateLastWillLimitTextView = view.findViewById(R.id.altar_private_last_will_limit_text_view);
        mAltarPrivateLastWillLimitTextView.setOnClickListener(this);
        mAltarPrivateLastWillCountTextView = view.findViewById(R.id.altar_private_last_will_text_count_text_view);
        mAltarPrivateLastWillSendToTextView = view.findViewById(R.id.altar_private_last_will_send_to_text_view);
        mAltarPrivateLastWillEditText = view.findViewById(R.id.altar_private_last_will_edit_text);
        mAltarPrivateLastWillConfirmButtonTextView = view.findViewById(R.id.altar_private_last_will_confirm_button_text_view);
        mAltarPrivateLastWillConfirmButtonTextView.setOnClickListener(this);

        dialog.setContentView(view);
        setPeekScreenHeight(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAltarPrivateLastWillSendToTextView.setText(mAltarPrivateLastWillSendTo);
        mAltarPrivateLastWillEditText.setText(mAltarPrivateLastWillMessage);
        if (mAltarPrivateLastWillMessage != null) {
            mAltarPrivateLastWillEditText.setSelection(mAltarPrivateLastWillMessage.length());
        }
        setAltarPrivateLastWillCountTextView();

        if (mEditTextWatcher == null) {
            mEditTextWatcher = new AltarPrivateLastWillEditTextWatcher();
        }
        mAltarPrivateLastWillEditText.addTextChangedListener(mEditTextWatcher);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_private_last_will_limit_text_view:
                characterCountLimitShowDialog();
                break;
            case R.id.altar_private_last_will_confirm_button_text_view:
                privateLastWillConfirmShowDialog();
                break;
            case R.id.altar_private_last_will_complete_image_view:
                mInputMethodManager.hideSoftInputFromWindow(mAltarPrivateLastWillEditText.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mEditTextWatcher != null) {
            mAltarPrivateLastWillEditText.removeTextChangedListener(mEditTextWatcher);
        }
        super.onDismiss(dialog);
    }

    private void promoteBillingStarShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_promote_billing_star_info_message));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.billing_star, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(BillingStarActivity.newIntent(getActivity()));
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void privateLastWillConfirmShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_public_last_will_confirm_info_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mAltarPrivateLastWillEditText.getText().toString().length() > 0) {
                    mLastWill.setMessage(mAltarPrivateLastWillEditText.getText().toString());
                }
                mOnAltarPrivateLastWillDialogDismissListener.onAltarPrivateLastWillDialogDismissed(mLastWill);
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void characterCountLimitShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_message_character_count_limit));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.billing_star, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(BillingStarActivity.newIntent(getActivity()));
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class AltarPrivateLastWillEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setAltarPrivateLastWillCountTextView();
            if (charSequence.length() == 200) {
                characterCountLimitShowDialog();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void setAltarPrivateLastWillCountTextView() {
        mAltarPrivateLastWillCountTextView.setText(getString(R.string.altar_last_will_count_format,
                String.valueOf(mAltarPrivateLastWillEditText.getText().toString().length()),
                String.valueOf(String.valueOf(200))));
    }

    private void setPeekScreenHeight(View view) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight - statusBarHeight;
        parent.setLayoutParams(params);
    }

    OnAltarPrivateLastWillDialogDismissListener mOnAltarPrivateLastWillDialogDismissListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnAltarPrivateLastWillDialogDismissListener = (OnAltarPrivateLastWillDialogDismissListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnAltarPrivateLastWillDialogDismissListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnAltarPrivateLastWillDialogDismissListener = null;
    }

    public interface OnAltarPrivateLastWillDialogDismissListener {
        void onAltarPrivateLastWillDialogDismissed(LastWill lastWill);
    }
}
