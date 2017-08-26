package com.memorial.altar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mToolbarStarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mToolbarStarTextView = mToolbar.findViewById(R.id.toolbar_star_text_view);
        mToolbarStarTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_star_text_view:
                Toast.makeText(getApplicationContext(), "star_text_view", Toast.LENGTH_SHORT).show();
                mToolbarStarTextView.setText(String.valueOf(20));
                break;
        }
    }
}
