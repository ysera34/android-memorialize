package com.memorial.altar.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;
import com.memorial.altar.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 9. 16..
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignInActivity.class);
        return intent;
    }

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextView mForgetUserInfoTextView;
    private TextView mSignInButtonTextView;
    private TextView mSignUpButtonTextView;
    private AlertDialog mDialog;
    private ProgressDialog mProgressDialog;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mEmailEditText = (EditText) findViewById(R.id.sign_in_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_password_edit_text);
        mForgetUserInfoTextView = (TextView) findViewById(R.id.forget_user_info_text_view);
        mForgetUserInfoTextView.setOnClickListener(this);
        mSignInButtonTextView = (TextView) findViewById(R.id.sign_in_button_text_view);
        mSignInButtonTextView.setOnClickListener(this);
        mSignUpButtonTextView = (TextView) findViewById(R.id.sign_up_button_text_view);
        mSignUpButtonTextView.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(SignInActivity.this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_user_info_text_view:
                forgetUserInfoShowDialog();
                break;
            case R.id.find_forgotten_email_text_view:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    findEmailShowDialog();
                }
                break;
            case R.id.find_forgotten_password_text_view:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    findPasswordShowDialog();
                }
                break;

            case R.id.sign_in_button_text_view:
                if (mEmailEditText.getText().length() > 0 && mPasswordEditText.getText().length() > 0) {
                    new SignInTask().execute(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.check_the_input_window, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_up_button_text_view:
                startActivity(SignUpActivity.newIntent(getApplicationContext()));
                finish();
                break;
        }
    }

    private void forgetUserInfoShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_forget_user_info, null);

        TextView findForgottenEmailTextView = view.findViewById(R.id.find_forgotten_email_text_view);
        findForgottenEmailTextView.setOnClickListener(this);
        TextView findForgottenPasswordTextView = view.findViewById(R.id.find_forgotten_password_text_view);
        findForgottenPasswordTextView.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title_forget_user_info);
        mDialog = builder.create();
        mDialog.show();
    }

    private void findEmailShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_find_email, null);

        TextInputEditText findEmailTextInputEditText = view.findViewById(R.id.find_email_text_input_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle(R.string.dialog_title_find_email);
        builder.setMessage(R.string.dialog_message_find_email);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        mDialog = builder.create();
        mDialog.show();
    }

    private void findPasswordShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_find_password, null);

        TextInputEditText findPasswordTextInputEditText = view.findViewById(R.id.find_password_text_input_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle(R.string.dialog_title_find_password);
        builder.setMessage(R.string.dialog_message_find_password);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        mDialog = builder.create();
        mDialog.show();
    }

    private class SignInTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            mProgressDialog.setMessage(getString(R.string.dialog_message_please_wait));
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... strings) {
            return requestSignIn(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            Log.i(TAG, "onPostExecute: s : " + s);
            if (s.equals("success")) {
                if (mUser == null) {
                    startActivity(HomeReadyActivity.newIntent(getApplicationContext()));
                } else {
                    startActivity(HomeActivity.newIntent(getApplicationContext(), mUser));
                }
                finish();

            } else {
                Toast.makeText(getApplicationContext(), R.string.sign_in_fail_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String requestSignIn(String email, String password) {

        String url = URL_HOST + "api" + File.separator + "users" + File.separator + "signin";
        StringBuilder result = new StringBuilder();
        String resultMessage = null;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept","application/json");
            connection.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", email);
            jsonParam.put("password", password);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
//            dos.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            dos.writeBytes(jsonParam.toString());

            dos.flush();
            dos.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "requestSignIn: response message : " + connection.getResponseMessage());

                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.i(TAG, "requestAltarUser: result to string " + result.toString());
                reader.close();
                resultMessage = parseSignIn(result.toString());
            }
            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "requestSignIn: e: " + e.getMessage());
            Log.e(TAG, "requestSignIn: e: " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultMessage;
    }

    private String parseSignIn(String responseObject) {

        String resultMessage = null;
        try {
            JSONObject jsonObject = new JSONObject(responseObject);
            String message = jsonObject.getString("message");
            if (message.equals("success")) {
                resultMessage = "success";

                JSONObject userJSONObject = jsonObject.getJSONObject("data");

                if (!userJSONObject.getString("name").equals("null")) {
                    mUser = new User();
                    mUser.setId(userJSONObject.getInt("id"));
                    mUser.setName(userJSONObject.getString("name"));
                    mUser.setBirth(userJSONObject.getString("birth"));
                    mUser.setImagePath(userJSONObject.getString("imagepath"));
                    mUser.setGender(userJSONObject.getString("gender"));
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
                    mUser.setGroupNames(groupNames);
                    mUser.setPublicLastWillMessage(userJSONObject.getString("lastwill"));
                }
            } else {
                resultMessage = "fail";
            }
        } catch (Exception e) {
            Log.e(TAG, "parseSignIn: e: " +e.getMessage());
        }
        return resultMessage;
    }
}
