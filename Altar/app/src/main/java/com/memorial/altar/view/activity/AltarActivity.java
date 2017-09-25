package com.memorial.altar.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.memorial.altar.R;
import com.memorial.altar.model.User;
import com.memorial.altar.view.fragment.AltarPreviewFragment;
import com.memorial.altar.view.fragment.AltarReadFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.memorial.altar.common.Common.ALTAR_PREVIEW_RESULT;
import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarActivity extends AppCompatActivity {

    private static final String TAG = AltarActivity.class.getSimpleName();

    private static final String EXTRA_FRIEND_ID = "com.memorial.altar.friend_id";
    private static final String EXTRA_ALTAR_USER = "com.memorial.altar.user";

    public static Intent newIntent(Context packageContext, int friendId) {
        Intent intent = new Intent(packageContext, AltarActivity.class);
        intent.putExtra(EXTRA_FRIEND_ID, friendId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, AltarActivity.class);
        intent.putExtra(EXTRA_ALTAR_USER, user);
        return intent;
    }

    private int mFriendId;
    private User mUser;
    private Toolbar mAltarToolbar;
    private Fragment mAltarFragment;
    private FragmentManager mAltarFragmentManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendId = getIntent().getIntExtra(EXTRA_FRIEND_ID, -1);
        mUser = (User) getIntent().getSerializableExtra(EXTRA_ALTAR_USER);
        setContentView(R.layout.activity_altar);
        mProgressDialog = new ProgressDialog(AltarActivity.this);
        mAltarFragmentManager = getSupportFragmentManager();
        mAltarToolbar = (Toolbar) findViewById(R.id.altar_toolbar);
        setSupportActionBar(mAltarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestFriend();

    }

    private void requestFriend() {

        if (mUser == null) {
            setTitle(getString(R.string.friend_altar));
            new AltarReadTask().execute(mFriendId);
//            mAltarFragment = AltarReadFragment.newInstance(mFriendId);
//            mAltarFragmentManager.beginTransaction()
//                    .add(R.id.altar_container, mAltarFragment)
//                    .commit();
        } else {
            setTitle(getString(R.string.altar_create_preview));
            mAltarFragment = AltarPreviewFragment.newInstance(mUser);
            mAltarFragmentManager.beginTransaction()
                    .add(R.id.altar_container, mAltarFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    public void previewConfirm() {
        Intent intent = new Intent();
        intent.putExtra(ALTAR_PREVIEW_RESULT, mUser);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class AltarReadTask extends AsyncTask<Integer, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected User doInBackground(Integer... integers) {
            return requestAltarUser(integers[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            mProgressDialog.dismiss();

            mAltarFragment = AltarReadFragment.newInstance(user);
            mAltarFragmentManager.beginTransaction()
                    .add(R.id.altar_container, mAltarFragment)
                    .commit();
        }
    }

    private User requestAltarUser(int userId) {

        String url = URL_HOST + "api" + File.separator + "users" + File.separator + userId;
        User user = new User();
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.i(TAG, "requestAltarUser: result to string " + result.toString());
            reader.close();
            user = parseAltarUser(result.toString());

        } catch (Exception e) {
            Log.e(TAG, "requestAltarUser: e: " + e.getMessage());
            Log.e(TAG, "requestAltarUser: e: " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return user;
    }

    private User parseAltarUser(String responseObject) {

        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(responseObject);
            String message = jsonObject.getString("message");
            if (message.equals("success")) {
                JSONObject userJSONObject = jsonObject.getJSONObject("data");
                user.setId(userJSONObject.getInt("id"));
                user.setName(userJSONObject.getString("name"));
                user.setBirth(userJSONObject.getString("birth"));
                user.setImagePath(userJSONObject.getString("imagepath"));
                user.setGender(userJSONObject.getString("gender"));
                String[] schoolArr = userJSONObject.getString("school").split(", ");
                String[] companyArr = userJSONObject.getString("company").split(", ");
                String[] societyArr = userJSONObject.getString("society").split(", ");
                ArrayList<String> groupNames = new ArrayList<>();
                for (int i = 0; i < schoolArr.length; i++) {
                    groupNames.add(schoolArr[i]);
                }
                for (int i = 0; i < companyArr.length; i++) {
                    groupNames.add(companyArr[i]);
                }
                for (int i = 0; i < societyArr.length; i++) {
                    groupNames.add(societyArr[i]);
                }
                user.setGroupNames(groupNames);
                user.setPublicLastWillMessage(userJSONObject.getString("lastwill"));
            }
        } catch (Exception e) {
            Log.e(TAG, "parseAltarUser: e" + e.getMessage());
        }
        return user;

    }
}
/*
try {
                HttpURLConnection connection =
                        (HttpURLConnection) new URL(params[0]).openConnection();
                connection.setRequestMethod("POST");

                StringBuffer sb = new StringBuffer();
                sb.append("title=").append(URLEncoder.encode(params[1], "UTF-8"));
                sb.append("&");
                sb.append("director=").append(URLEncoder.encode(params[2], "UTF-8"));

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(sb.toString().length()));
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(sb.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                return responseCode;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
* */