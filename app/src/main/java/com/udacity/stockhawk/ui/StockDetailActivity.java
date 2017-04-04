package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.stockhawk.R;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent() ;
        String symbolExtra = getString(R.string.intent_extra_symbol_key) ;

        if(intent.hasExtra(symbolExtra)){

            String symbol = intent.getStringExtra(symbolExtra) ;


            Bundle bundle = new Bundle();
            bundle.putString(symbolExtra , symbol );

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            StockDetailFragment fragment = new StockDetailFragment() ;
            fragment.setArguments(bundle);

            ft.replace(R.id.detail_fragment_container,fragment );
            ft.commit();

        }


    }
}
