package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.BillingStar;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 5..
 */

public class BillingStarActivity extends AppCompatActivity {

    private static final String TAG = BillingStarActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, BillingStarActivity.class);
        return intent;
    }

    private Toolbar mBillingStarToolbar;
    private RecyclerView mBillingStarRecyclerView;
    private BillingStarAdapter mBillingStarAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_star);

        mBillingStarToolbar = (Toolbar) findViewById(R.id.billing_star_toolbar);
        setSupportActionBar(mBillingStarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.billing_star_title));

        mBillingStarRecyclerView = (RecyclerView) findViewById(R.id.billing_star_recycler_view);
        mBillingStarRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        mBillingStarAdapter = new BillingStarAdapter(getBillingStars());
        mBillingStarRecyclerView.setAdapter(mBillingStarAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    private ArrayList<BillingStar> getBillingStars() {
        ArrayList<BillingStar> billingStars = new ArrayList<>();

        BillingStar billingStar0 = new BillingStar();
        billingStar0.setCount(30);
        billingStar0.setPrice(4500);
        billingStars.add(billingStar0);

        BillingStar billingStar1 = new BillingStar();
        billingStar1.setCount(85);
        billingStar1.setPrice(12500);
        billingStar1.setExtraCount(10);
        billingStars.add(billingStar1);

        BillingStar billingStar2 = new BillingStar();
        billingStar2.setCount(115);
        billingStar2.setPrice(21500);
        billingStar2.setExtraCount(15);
        billingStars.add(billingStar2);

        BillingStar billingStar3 = new BillingStar();
        billingStar3.setCount(300);
        billingStar3.setPrice(42500);
        billingStar3.setExtraCount(40);
        billingStars.add(billingStar3);

        BillingStar billingStar4 = new BillingStar();
        billingStar4.setCount(600);
        billingStar4.setPrice(77000);
        billingStar4.setExtraCount(100);
        billingStars.add(billingStar4);

        return billingStars;
    }

    private class BillingStarAdapter extends RecyclerView.Adapter<BillingStarViewHolder> {

        private ArrayList<BillingStar> mBillingStars;

        public BillingStarAdapter(ArrayList<BillingStar> billingStars) {
            mBillingStars = billingStars;
        }

        @Override
        public BillingStarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_billing_star, parent, false);
            return new BillingStarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BillingStarViewHolder holder, int position) {
            holder.bindBillingStar(mBillingStars.get(position));
        }

        @Override
        public int getItemCount() {
            return mBillingStars.size();
        }
    }

    private class BillingStarViewHolder extends RecyclerView.ViewHolder {

        private BillingStar mBillingStar;

        private TextView mBillingStarExtraCountTextView;
        private TextView mBillingStarCountTextView;
        private TextView mBillingStarPriceTextView;

        public BillingStarViewHolder(View itemView) {
            super(itemView);
            mBillingStarExtraCountTextView = itemView.findViewById(R.id.list_item_billing_star_extra_count_text_view);
            mBillingStarCountTextView = itemView.findViewById(R.id.list_item_billing_star_count_text_view);
            mBillingStarPriceTextView = itemView.findViewById(R.id.list_item_billing_star_price_text_view);
        }

        public void bindBillingStar(BillingStar billingStar) {
            mBillingStar = billingStar;
            if (mBillingStar.getExtraCount() > 0) {
                mBillingStarExtraCountTextView.setText(getString(R.string.billing_star_extra_star_format,
                        String.valueOf(mBillingStar.getExtraCount())));
            }
            mBillingStarCountTextView.setText(getString(R.string.billing_star_count_format,
                    String.valueOf(mBillingStar.getCount())));
            mBillingStarPriceTextView.setText(getString(R.string.billing_star_price_format,
                    String.valueOf(mBillingStar.getPrice())));

        }
    }
}
