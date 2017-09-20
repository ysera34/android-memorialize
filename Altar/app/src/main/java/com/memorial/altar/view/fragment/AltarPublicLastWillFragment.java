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
import com.memorial.altar.util.UserSharedPreferences;
import com.memorial.altar.view.activity.BillingStarActivity;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class AltarPublicLastWillFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{

    private static final String TAG = AltarPublicLastWillFragment.class.getSimpleName();

    private static final String ARG_PUBLIC_LAST_WILL_MESSAGE = "public_last_will_message";

    public static AltarPublicLastWillFragment newInstance() {

        Bundle args = new Bundle();

        AltarPublicLastWillFragment fragment = new AltarPublicLastWillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AltarPublicLastWillFragment newInstance(String publicLastWillMessage) {

        Bundle args = new Bundle();
        args.putString(ARG_PUBLIC_LAST_WILL_MESSAGE, publicLastWillMessage);

        AltarPublicLastWillFragment fragment = new AltarPublicLastWillFragment();
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
    private ImageView mAltarPublicLastWillCompleteImageView;
    private TextView mAltarPublicLastWillLimitTextView;
    private TextView mAltarPublicLastWillCountTextView;
    private AltarPublicLastWillEditTextWatcher mEditTextWatcher;
    private EditText mAltarPublicLastWillEditText;
    private TextView mAltarPublicLastWillConfirmButtonTextView;

    private String mAltarPublicLastWillMessage;
    private int mStarCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAltarPublicLastWillMessage = getArguments().getString(ARG_PUBLIC_LAST_WILL_MESSAGE, null);
        mStarCount = UserSharedPreferences.getStoredStar(getActivity());
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.fragment_dialog_altar_public_last_will, null);
        mAltarPublicLastWillCompleteImageView = view.findViewById(R.id.altar_public_last_will_complete_image_view);
        mAltarPublicLastWillCompleteImageView.setOnClickListener(this);
        mAltarPublicLastWillLimitTextView = view.findViewById(R.id.altar_public_last_will_limit_text_view);
        mAltarPublicLastWillLimitTextView.setOnClickListener(this);
        mAltarPublicLastWillCountTextView = view.findViewById(R.id.altar_public_last_will_text_count_text_view);
        mAltarPublicLastWillEditText = view.findViewById(R.id.altar_public_last_will_edit_text);
        mAltarPublicLastWillConfirmButtonTextView = view.findViewById(R.id.altar_public_last_will_confirm_button_text_view);
        mAltarPublicLastWillConfirmButtonTextView.setOnClickListener(this);

        dialog.setContentView(view);
        setPeekScreenHeight(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAltarPublicLastWillEditText.setText(mAltarPublicLastWillMessage);
        setAltarPublicLastWillCountTextView();

        if (mEditTextWatcher == null) {
            mEditTextWatcher = new AltarPublicLastWillEditTextWatcher();
        }
        mAltarPublicLastWillEditText.addTextChangedListener(mEditTextWatcher);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_public_last_will_limit_text_view:
                characterCountLimitShowDialog();
                break;
            case R.id.altar_public_last_will_confirm_button_text_view:
                publicLastWillConfirmShowDialog();
                break;
            case R.id.altar_public_last_will_complete_image_view:
                mInputMethodManager.hideSoftInputFromWindow(mAltarPublicLastWillEditText.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mEditTextWatcher != null) {
            mAltarPublicLastWillEditText.removeTextChangedListener(mEditTextWatcher);
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

    private void publicLastWillConfirmShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_public_last_will_confirm_info_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mOnAltarPublicLastWillDialogDismissListener.onAltarPublicLastWillDialogDismissed(
                        mAltarPublicLastWillEditText.getText().toString());
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

    private class AltarPublicLastWillEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setAltarPublicLastWillCountTextView();
            if (charSequence.length() == 200) {
                characterCountLimitShowDialog();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void setAltarPublicLastWillCountTextView() {
        mAltarPublicLastWillCountTextView.setText(getString(R.string.altar_last_will_count_format,
                String.valueOf(mAltarPublicLastWillEditText.getText().toString().length()),
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

    OnAltarPublicLastWillDialogDismissListener mOnAltarPublicLastWillDialogDismissListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnAltarPublicLastWillDialogDismissListener = (OnAltarPublicLastWillDialogDismissListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnAltarPublicLastWillDialogDismissListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnAltarPublicLastWillDialogDismissListener = null;
    }

    public interface OnAltarPublicLastWillDialogDismissListener {
        void onAltarPublicLastWillDialogDismissed(String publicLastWillMessage);
    }
}
