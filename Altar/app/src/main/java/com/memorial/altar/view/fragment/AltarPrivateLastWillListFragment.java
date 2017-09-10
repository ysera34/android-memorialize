package com.memorial.altar.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.GroupChild;
import com.memorial.altar.model.LastWill;
import com.memorial.altar.view.activity.BillingStarActivity;

import java.util.ArrayList;

import static com.memorial.altar.view.fragment.PermissionHeadlessFragment.CONTACT_PERMISSION_REQUEST;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class AltarPrivateLastWillListFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{

    private static final String TAG = AltarPrivateLastWillListFragment.class.getSimpleName();

    public static final int ALTAR_PRIVATE_LAST_WILL_CONTACT_PERMISSION_REQUEST = 1003;

    public static AltarPrivateLastWillListFragment newInstance() {

        Bundle args = new Bundle();

        AltarPrivateLastWillListFragment fragment = new AltarPrivateLastWillListFragment();
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

    private Toolbar mAltarPrivateLastWillListToolbar;
    private RecyclerView mAltarPrivateLastWillRecyclerView;
    private PrivateLastWillAdapter mPrivateLastWillAdapter;
    private ArrayList<LastWill> mLastWills;
    private TextView mAltarPrivateLastWillEmptyTextView;
    private TextView mAltarPrivateLastWillCreateButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLastWills = new ArrayList<>();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.fragment_dialog_altar_private_last_will_list, null);
        mAltarPrivateLastWillListToolbar = view.findViewById(R.id.altar_private_last_will_list_toolbar);
        mAltarPrivateLastWillListToolbar.setTitle(getString(R.string.altar_private_last_will_list));
        mAltarPrivateLastWillRecyclerView = view.findViewById(R.id.altar_private_last_will_recycler_view);
        mAltarPrivateLastWillRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPrivateLastWillAdapter = new PrivateLastWillAdapter(mLastWills);
        mAltarPrivateLastWillRecyclerView.setAdapter(mPrivateLastWillAdapter);
        mAltarPrivateLastWillEmptyTextView = view.findViewById(R.id.altar_private_last_will_empty_text_view);
        mAltarPrivateLastWillCreateButtonTextView = view.findViewById(R.id.altar_private_last_will_create_button_text_view);
        mAltarPrivateLastWillCreateButtonTextView.setOnClickListener(this);

        dialog.setContentView(view);
        setPeekScreenHeight(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_private_last_will_create_button_text_view:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(PermissionHeadlessFragment.newInstance(
                                ALTAR_PRIVATE_LAST_WILL_CONTACT_PERMISSION_REQUEST,
                                CONTACT_PERMISSION_REQUEST),
                                PermissionHeadlessFragment.TAG)
                        .commit();
                break;
        }
    }

    public void onPermissionCallback(int requestCode, int requestPermissionId, boolean isGranted) {
        switch (requestCode) {
            case ALTAR_PRIVATE_LAST_WILL_CONTACT_PERMISSION_REQUEST:
                if (requestPermissionId == CONTACT_PERMISSION_REQUEST) {
                    if (isGranted /*&& isResumed()*/) {
                        BottomSheetDialogFragment altarContactFragment =
                                AltarContactFragment.newInstance();
//                        altarContactFragment.show(getChildFragmentManager(), "altar_contact");
                        getChildFragmentManager().beginTransaction()
                                .add(altarContactFragment, "altar_contact")
                                .commitAllowingStateLoss();
                    }
                }
                break;
        }
    }

    public void onAltarContactDialogDismissed(String contactName) {
        Log.i(TAG, getString(R.string.altar_private_last_will_send_to_format, contactName));
        String sendTo = getString(R.string.altar_private_last_will_send_to_format, contactName);
        LastWill lastWill = new LastWill();
        lastWill.setSendTo(sendTo);

        BottomSheetDialogFragment altarContactFragment = AltarPrivateLastWillFragment.newInstance(lastWill);
        getChildFragmentManager().beginTransaction()
                .add(altarContactFragment, "altar_private_last_will")
                .commitAllowingStateLoss();
    }

    public void onAltarPrivateLastWillDialogDismissed(LastWill lastWill) {
        Log.i(TAG, "onAltarPrivateLastWillDialogDismissed: sendTo : " + lastWill.getSendTo());
        Log.i(TAG, "onAltarPrivateLastWillDialogDismissed: Message: " + lastWill.getMessage());
        if (lastWill.isUpdate()) {
            lastWill.setUpdate(false);
        } else {
            mLastWills.add(lastWill);
        }
        updateUI();
    }

    private void updateUI() {
        if (mLastWills.size() > 0) {
            mAltarPrivateLastWillEmptyTextView.setVisibility(View.GONE);
        }
        mPrivateLastWillAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
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
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removePrivateLastWillShowDialog(final LastWill lastWill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mLastWills.contains(lastWill)) {
                    mLastWills.remove(lastWill);
                    updateUI();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
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

    private class PrivateLastWillAdapter extends RecyclerView.Adapter<PrivateLastWillViewHolder> {

        private ArrayList<LastWill> mLastWills;

        public PrivateLastWillAdapter(ArrayList<LastWill> lastWills) {
            mLastWills = lastWills;
        }

        @Override
        public PrivateLastWillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_private_last_will, parent, false);
            return new PrivateLastWillViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PrivateLastWillViewHolder holder, int position) {
            holder.bindPrivateLastWill(mLastWills.get(position));
        }

        @Override
        public int getItemCount() {
            return mLastWills.size();
        }
    }

    private class PrivateLastWillViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private LastWill mLastWill;

        private TextView mContactNamesTextView;
        private TextView mMessageTextView;

//        private StringBuilder mContactNamesBuilder;

        public PrivateLastWillViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
//            mContactNamesBuilder = new StringBuilder();

            mContactNamesTextView = itemView.findViewById(R.id.list_item_altar_private_last_will_contact_names_text_view);
            mMessageTextView = itemView.findViewById(R.id.list_item_altar_private_last_will_message_text_view);
        }

        public void bindPrivateLastWill(LastWill lastWill) {
            mLastWill = lastWill;

//            mContactNamesBuilder.setLength(0);
//            for (int i = 0; i < mLastWill.getContacts().size(); i++) {
//                if (i != 0) {
//                    mContactNamesBuilder.append(", ");
//                }
//                mContactNamesBuilder.append(mLastWill.getContacts().get(i).getName());
//            }
//            if (mContactNamesBuilder.length() > 0) {
//                mContactNamesTextView.setText(mContactNamesBuilder.toString());
//            }
            mContactNamesTextView.setText(String.valueOf(mLastWill.getSendTo()));
            mMessageTextView.setText(String.valueOf(mLastWill.getMessage()));
        }

        @Override
        public void onClick(View view) {
            mLastWill.setUpdate(true);
            BottomSheetDialogFragment altarContactFragment =
                    AltarPrivateLastWillFragment.newInstance(mLastWill);
            getChildFragmentManager().beginTransaction()
                    .add(altarContactFragment, "altar_private_last_will")
                    .commitAllowingStateLoss();
        }

        @Override
        public boolean onLongClick(View view) {
            removePrivateLastWillShowDialog(mLastWill);
            return true;
        }
    }

//    OnAltarPublicLastWillDialogDismissListener mOnAltarPublicLastWillDialogDismissListener;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mOnAltarPublicLastWillDialogDismissListener = (OnAltarPublicLastWillDialogDismissListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implements OnAltarPublicLastWillDialogDismissListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mOnAltarPublicLastWillDialogDismissListener = null;
//    }
//
//    public interface OnAltarPublicLastWillDialogDismissListener {
//        void onAltarPublicLastWillDialogDismissed(String publicLastWillMessage);
//    }
}
