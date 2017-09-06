package com.memorial.altar.view.fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.Contact;
import com.memorial.altar.util.UserSharedPreferences;
import com.memorial.altar.view.activity.BillingStarActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class AltarContactFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{

    private static final String TAG = AltarContactFragment.class.getSimpleName();

    public static AltarContactFragment newInstance() {

        Bundle args = new Bundle();

        AltarContactFragment fragment = new AltarContactFragment();
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

    private TextView mAltarContactCountTextView;
    private TextView mAltarContactConfirmButtonTextView;
    private int mAltarContactSelectedCount;
    private ProgressBar mAltarContactProgressBar;
    private RecyclerView mAltarContactRecyclerView;
    private AltarContactAdapter mAltarContactAdapter;
    private ArrayList<Contact> mContacts;
    private int mStarCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStarCount = UserSharedPreferences.getStoredStar(getActivity());
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.fragment_dialog_altar_contact, null);
        mAltarContactCountTextView = view.findViewById(R.id.altar_contact_count_text_view);
        mAltarContactCountTextView.setOnClickListener(this);
        mAltarContactConfirmButtonTextView = view.findViewById(R.id.altar_contact_confirm_button_text_view);
        mAltarContactConfirmButtonTextView.setOnClickListener(this);
        mAltarContactProgressBar = view.findViewById(R.id.altar_contact_progress_bar);
        mAltarContactRecyclerView = view.findViewById(R.id.altar_contact_recycler_view);
        mAltarContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dialog.setContentView(view);
        setPeekScreenHeight(view);
        new AltarContactTask().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_contact_count_text_view:
                startActivity(BillingStarActivity.newIntent(getActivity()));
                break;
            case R.id.altar_contact_confirm_button_text_view:
                contactConfirmShowDialog();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private class AltarContactAdapter extends RecyclerView.Adapter<AltarContactViewHolder> {

        private ArrayList<Contact> mContacts;

        public AltarContactAdapter(ArrayList<Contact> contacts) {
            mContacts = contacts;
        }

        @Override
        public AltarContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_altar_contact, parent, false);
            return new AltarContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AltarContactViewHolder holder, int position) {
            holder.bindAltarContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    private class AltarContactViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Contact mContact;

        private ImageView mStarImageView;
        private TextView mNameTextView;
        private TextView mPhoneNumberTextView;


        public AltarContactViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mStarImageView = itemView.findViewById(R.id.list_item_altar_contact_star_image_view);
            mNameTextView = itemView.findViewById(R.id.list_item_altar_contact_name_text_view);
            mPhoneNumberTextView = itemView.findViewById(R.id.list_item_altar_contact_phone_number_text_view);
        }

        public void bindAltarContact(Contact contact) {
            mContact = contact;
            updateStar();
            mNameTextView.setText(String.valueOf(mContact.getName()));
            mPhoneNumberTextView.setText(String.valueOf(mContact.getPhoneNumber()));
        }

        @Override
        public void onClick(View view) {
//            mContact.setSelected(!mContact.isSelected());

            if (mContact.isSelected()) {
                mContact.setSelected(false);
                mAltarContactSelectedCount--;
            } else {
                mContact.setSelected(true);
                mAltarContactSelectedCount++;
            }
            if (mAltarContactSelectedCount > mStarCount) {
                mContact.setSelected(!mContact.isSelected());
                mAltarContactSelectedCount--;
                promoteBillingStarShowDialog();
            } else {
                updateStar();
            }
        }

        private void updateStar() {
            if (mContact.isSelected()) {
                mStarImageView.setImageResource(R.drawable.ic_small_star);
            } else {
                mStarImageView.setImageResource(R.drawable.ic_small_star_border);
            }
            mAltarContactCountTextView.setText(getString(R.string.altar_contact_count_format,
                    String.valueOf(mAltarContactSelectedCount), String.valueOf(mStarCount)));
        }
    }

    private class AltarContactTask extends AsyncTask<Void, Void, ArrayList<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAltarContactProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            return getContacts();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            mContacts = contacts;
            mAltarContactProgressBar.setVisibility(View.GONE);
            mAltarContactAdapter = new AltarContactAdapter(mContacts);
            mAltarContactRecyclerView.setAdapter(mAltarContactAdapter);
            int selectedCount = 0;
            for (Contact c : mContacts) {
                selectedCount = c.isSelected() ? selectedCount + 1 : selectedCount;
            }
            mAltarContactSelectedCount = selectedCount;
            mAltarContactCountTextView.setText(getString(R.string.altar_contact_count_format,
                    String.valueOf(mAltarContactSelectedCount), String.valueOf(mStarCount)));
        }
    }

    private ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Contact contact = new Contact();
//            contact.setName(" i = " + i);
//            contact.setPhoneNumber("000 - 0000 - 000");
//            if (i < 5)
//            contact.setSelected(true);
//            contacts.add(contact);
//        }

        ContentResolver contentResolver = getActivity().getContentResolver();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setName(cursor.getString(displayNameIndex));
                    contact.setPhoneNumber(cursor.getString(phoneNumberIndex));
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return contacts;
    }

    private void promoteBillingStarShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_promote_billing_starinfo_message));
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

    private void contactConfirmShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_contact_confirm_info_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Contact c : mContacts) {
                    if (c.isSelected()) {
                        stringBuilder.append(c.getName());
                        stringBuilder.append(" ");
                    }
                }
                mOnAltarContactDialogDismissListener.onAltarContactDialogDismissed(stringBuilder.toString());
                getDialog().dismiss();
            }
        });
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

    OnAltarContactDialogDismissListener mOnAltarContactDialogDismissListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnAltarContactDialogDismissListener = (OnAltarContactDialogDismissListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnAltarContactDialogDismissListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnAltarContactDialogDismissListener = null;
    }

    public interface OnAltarContactDialogDismissListener {
        void onAltarContactDialogDismissed(String contactName);
    }
}
