package com.memorial.altar.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 9. 16..
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignUpActivity.class);
        return intent;
    }

    private EditText mPhoneNumberEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView mSignUpButtonTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mPhoneNumberEditText = (EditText) findViewById(R.id.sign_up_phone_number_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.sign_up_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.sign_up_password_edit_text);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.sign_up_confirm_password_edit_text);
        mSignUpButtonTextView = (TextView) findViewById(R.id.sign_up_button_text_view);
        mSignUpButtonTextView.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(SignUpActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_button_text_view:
                if (mPhoneNumberEditText.getText().length() > 0 && mEmailEditText.getText().length() > 0
                        && mPasswordEditText.getText().length() > 0 && mConfirmPasswordEditText.getText().length() > 0) {
                    new SignUpTask().execute(mPhoneNumberEditText.getText().toString(),
                            mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.check_the_input_window, Toast.LENGTH_SHORT).show();
                }
//                startActivity(HomeReadyActivity.newIntent(getApplicationContext()));
//                finish();
                break;
        }
    }

    private class SignUpTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            mProgressDialog.setMessage(getString(R.string.dialog_message_please_wait));
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            return requestSignUp(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            Log.i(TAG, "onPostExecute: s : " + s);
            if (s.equals("success")) {
                startActivity(HomeReadyActivity.newIntent(getApplicationContext()));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.sign_up_fail_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String requestSignUp(String phoneNumber, String email, String password) {

        String url = URL_HOST + "api" + File.separator + "users" + File.separator + "signup";
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
            jsonParam.put("phone", phoneNumber);
            jsonParam.put("email", email);
            jsonParam.put("password", password);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
//            dos.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            dos.writeBytes(jsonParam.toString());

            dos.flush();
            dos.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "requestSignUp: response message : " + connection.getResponseMessage());

                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.i(TAG, "requestAltarUser: result to string " + result.toString());
                reader.close();
                resultMessage = parseSignUp(result.toString());
            }
            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "requestSignUp: e: " + e.getMessage());
            Log.e(TAG, "requestSignUp: e: " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultMessage;
    }

    private String parseSignUp(String responseObject) {

        String resultMessage = null;
        try {
            JSONObject jsonObject = new JSONObject(responseObject);
            String message = jsonObject.getString("message");
            if (message.equals("success")) {
                resultMessage = "success";

//                JSONObject userJSONObject = jsonObject.getJSONObject("data");
//
//                if (!userJSONObject.getString("name").equals("null")) {
//                    mUser = new User();
//                    mUser.setId(userJSONObject.getInt("id"));
//                    mUser.setName(userJSONObject.getString("name"));
//                    mUser.setBirth(userJSONObject.getString("birth"));
//                    mUser.setImagePath(userJSONObject.getString("imagepath"));
//                    mUser.setGender(userJSONObject.getString("gender"));
//                    String[] schoolArr = userJSONObject.getString("school").split(", ");
//                    String[] companyArr = userJSONObject.getString("company").split(", ");
//                    String[] societyArr = userJSONObject.getString("society").split(", ");
//                    ArrayList<String> groupNames = new ArrayList<>();
//                    for (int i = 0; i < schoolArr.length; i++) {
//                        groupNames.add(schoolArr[i]);
//                    }
//                    for (int i = 0; i < companyArr.length; i++) {
//                        groupNames.add(companyArr[i]);
//                    }
//                    for (int i = 0; i < societyArr.length; i++) {
//                        groupNames.add(societyArr[i]);
//                    }
//                    mUser.setGroupNames(groupNames);
//                    mUser.setPublicLastWillMessage(userJSONObject.getString("lastwill"));
//                }
            } else {
                resultMessage = "fail";
            }
        } catch (Exception e) {
            Log.e(TAG, "parseSignUp: e: " +e.getMessage());
        }
        return resultMessage;
    }
}
