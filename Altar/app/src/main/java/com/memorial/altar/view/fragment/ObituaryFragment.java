package com.memorial.altar.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.memorial.altar.R;
import com.memorial.altar.model.Friend;
import com.memorial.altar.util.ImageHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;
import static com.memorial.altar.common.Common.URL_HOST;
import static com.memorial.altar.view.fragment.PermissionHeadlessFragment.STORAGE_PERMISSION_REQUEST;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class ObituaryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ObituaryFragment.class.getSimpleName();

    public static final int OBITUARY_SUBMIT_STORAGE_PERMISSION_REQUEST = 3001;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 3100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 3101;

    public static ObituaryFragment newInstance() {

        Bundle args = new Bundle();

        ObituaryFragment fragment = new ObituaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mSearchedFriendRecyclerView;
    private SearchedFriendAdapter mSearchedFriendAdapter;
    private ArrayList<Friend> mSearchedFriends;
    private EditText mObituarySearchEditText;
    private AlertDialog mDialog;
    private ImageHandler mImageHandler;
    private String mImageStoragePath;
    private TextView mImagePathTextView;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchedFriends = new ArrayList<>();
//        mSearchedFriends = getSearchedFriends();
        mImageHandler = new ImageHandler(getActivity());
        mProgressDialog = new ProgressDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obituary, container, false);
        mObituarySearchEditText = view.findViewById(R.id.obituary_search_edit_text);
        mObituarySearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchedFriendAdapter.onFilterViewHolder(editable.toString());

            }
        });
        mSearchedFriendRecyclerView = view.findViewById(R.id.searched_friend_recycler_view);
        mSearchedFriendRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();
    }

    private void updateUI() {
        if (mSearchedFriendAdapter == null) {
            mSearchedFriendAdapter = new SearchedFriendAdapter(mSearchedFriends);
            mSearchedFriendRecyclerView.setAdapter(mSearchedFriendAdapter);
        } else {
            mSearchedFriendAdapter.notifyDataSetChanged();
        }
    }

    private class SearchedFriendAdapter extends RecyclerView.Adapter<SearchedFriendViewHolder> {

        private ArrayList<Friend> mSearchedFriends;

        public SearchedFriendAdapter(ArrayList<Friend> searchedFriends) {
            mSearchedFriends = searchedFriends;
        }

        public void setSearchedFriends(ArrayList<Friend> searchedFriends) {
            mSearchedFriends = searchedFriends;
        }

        @Override
        public SearchedFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_searched_friend, parent, false);
            return new SearchedFriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchedFriendViewHolder holder, int position) {
            holder.bindSearchedFriend(mSearchedFriends.get(position));
        }

        @Override
        public int getItemCount() {
            return mSearchedFriends.size();
        }

        public void onFilterViewHolder(String text) {
            ArrayList<Friend> tempFriends = new ArrayList<>();
            if (text.length() > 0) {
                for (Friend f : getSearchedFriends()) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (f.getName().contains(text)) {
                        tempFriends.add(f);
                    }
                }
            }
            setSearchedFriends(tempFriends);
            notifyDataSetChanged();
        }
    }

    private class SearchedFriendViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Friend mSearchedFriend;

        private ImageView mPhotoImageView;
        private TextView mBirthTextView;
        private TextView mNameTextView;

        public SearchedFriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPhotoImageView = itemView.findViewById(R.id.list_item_searched_friend_photo_image_view);
            mBirthTextView = itemView.findViewById(R.id.list_item_searched_friend_birth_text_view);
            mNameTextView = itemView.findViewById(R.id.list_item_searched_friend_name_text_view);
        }

        public void bindSearchedFriend(Friend searchedFriend) {
            mSearchedFriend = searchedFriend;
            Glide.with(getActivity()).load(URL_HOST + mSearchedFriend.getImagePath()).into(mPhotoImageView);
            mNameTextView.setText(String.valueOf(mSearchedFriend.getName()));
            mBirthTextView.setText(String.valueOf(mSearchedFriend.getBirth()));
        }

        @Override
        public void onClick(View view) {
            obituaryShowDialog();
        }
    }

    private void obituaryShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_obituary_submit);
        builder.setMessage(getString(R.string.obituary_submit));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                obituaryAdviceMessageShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituaryAdviceMessageShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setTitle(R.string.dialog_title_information);
        builder.setMessage(getString(R.string.obituary_advice_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                obituarySubmitShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituarySubmitShowDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_obituary_submit, null);
        TextInputEditText nameTextInputEditText = view.findViewById(R.id.obituary_submit_name_text_input_edit_text);
        TextInputEditText name2TextInputEditText = view.findViewById(R.id.obituary_submit_name_2_text_input_edit_text);
        TextInputEditText relationsTextInputEditText = view.findViewById(R.id.obituary_submit_relations_text_input_edit_text);
        TextInputEditText contactsTextInputEditText = view.findViewById(R.id.obituary_submit_contacts_text_input_edit_text);
        Button submitImageButton = view.findViewById(R.id.obituary_submit_image_button);
        mImagePathTextView = view.findViewById(R.id.obituary_submit_image_path_text_view);
        submitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(PermissionHeadlessFragment.newInstance(
                                OBITUARY_SUBMIT_STORAGE_PERMISSION_REQUEST,
                                STORAGE_PERMISSION_REQUEST),
                                PermissionHeadlessFragment.TAG)
                        .commit();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.obituary_submit_title);
        builder.setMessage(getString(R.string.obituary_advice_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (mImageStoragePath != null) {
                    new UploadImageTask().execute(mImageStoragePath);
                }
//            obituaryConfirmMessageShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituaryConfirmMessageShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setTitle(R.string.dialog_title_information);
        builder.setMessage(getString(R.string.obituary_confirm_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onPermissionCallback(int requestCode, int requestPermissionId, boolean isGranted) {
        switch (requestCode) {
            case OBITUARY_SUBMIT_STORAGE_PERMISSION_REQUEST:
                if (requestPermissionId == STORAGE_PERMISSION_REQUEST) {
                    if (isGranted) {
                        obituarySubmitImageShowDialog();
                    }
                }
                break;
        }
    }

    private void obituarySubmitImageShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_obituary_submit_image, null);

        TextView cameraTextView = view.findViewById(R.id.submit_image_take_a_picture);
        cameraTextView.setOnClickListener(this);
        TextView galleryTextView = view.findViewById(R.id.submit_image_select_photo_in_album);
        galleryTextView.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_image_take_a_picture:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (mImageHandler.hasCamera()) {
                    startActivityForResult(mImageHandler.dispatchTakePictureIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), R.string.camera_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.submit_image_select_photo_in_album:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                startActivityForResult(mImageHandler.pickGalleryPictureIntent(), GALLERY_IMAGE_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
//                    mImageHandler.handleCameraImage(mUserImageView);

                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
//                    mImageHandler.handleGalleryImage(data, mUserImageView);
                    Uri selectedImageUri = data.getData();
                    mImageStoragePath = mImageHandler.getRealPathFromURI(selectedImageUri);
                    mImagePathTextView.setText(getString(R.string.submit_image_path_format, mImageStoragePath));
                }
                break;
        }
    }

    private ArrayList<Friend> getSearchedFriends() {
        String[] obituarySampleFriendNameArr = getResources().getStringArray(R.array.obituary_sample_friend_name);
        String[] obituarySampleImagePathArr = getResources().getStringArray(R.array.obituary_sample_friend_image_path);
        String[] obituarySampleFriendBirthArr = getResources().getStringArray(R.array.obituary_sample_friend_birth);
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Friend friend = new Friend();
            friend.setName(obituarySampleFriendNameArr[i]);
            friend.setImagePath(obituarySampleImagePathArr[i]);
            friend.setBirth(obituarySampleFriendBirthArr[i]);
            friends.add(friend);
        }
        return friends;
    }

    private class UploadImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return requestUploadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            mImagePathTextView.setText(s);
        }
    }

    private String requestUploadImage(String imageStoragePath) {

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
        String tag = name + ".jpg";
        String fileName = imageStoragePath.replace(imageStoragePath, tag);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(imageStoragePath);
        String responseMessage = null;

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + mImageStoragePath);

//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    mImagePathTextView.setText(getString(
//                            R.string.submit_image_path_not_exist_format, mImageStoragePath));
//                }
//            });
            responseMessage = "Source File not exist";
            return responseMessage;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://211.108.3.4:3000/upload");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"img\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
//                                    +" C:/wamp/wamp/www/uploads";
//                            mImagePathTextView.setText(msg);
////                            Toast.makeText(MainActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    responseMessage = "File Upload Completed.";
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//                dialog.dismiss();
                ex.printStackTrace();

//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        mImagePathTextView.setText("MalformedURLException Exception : check script url.");
////                        Toast.makeText(MainActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
//                    }
//                });
                responseMessage = "MalformedURLException Exception : check script url.";
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//                dialog.dismiss();
                e.printStackTrace();

//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        mImagePathTextView.setText("Got Exception : see logcat ");
////                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
//                    }
//                });
                Log.e("Exception", "Exception : "  + e.getMessage(), e);
                responseMessage = "Got Exception";
            }
//            dialog.dismiss();
            return responseMessage;

        }
    }
}
