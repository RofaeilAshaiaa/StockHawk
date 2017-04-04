package com.udacity.stockhawk.ui;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.objects.OrganizationStock;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import static com.udacity.stockhawk.data.Contract.Quote.POSITION_HISTORY;

public class StockDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_LOADER_SYMBOL = 1;
    @BindView(R.id.chart_container)
    FrameLayout chartContainer;
    @BindView(R.id.tv_stock_symbol)
    TextView stockName;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.constraint_detail_fragment)
    ConstraintLayout constraintLayout;
    private Context mContext;
    private OrganizationStock mOrganizationStock;
    private StockDetailActivity mParentActivity;
    private Stock stock;
    private List<HistoricalQuote> quote;


    public StockDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mParentActivity = (StockDetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_detail, container, false);
        ButterKnife.bind(this, view);
        mOrganizationStock = new OrganizationStock();
        mOrganizationStock.setSymbolStock(getArguments().getString(mContext.getString(R.string.intent_extra_symbol)));

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            progressBar.setVisibility(View.VISIBLE);
            sentStockRequest();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, getString(R.string.error_no_connection), Toast.LENGTH_SHORT).show();
        }

//        mParentActivity.getSupportLoaderManager().initLoader(STOCK_LOADER_SYMBOL, null, this);

        return view;
    }

    private void sentStockRequest() {

        AsyncTaskLoader loader = new AsyncTaskLoader(mContext) {
            @Override
            public Object loadInBackground() {

                try {
                    stock = YahooFinance.get(mOrganizationStock.getSymbolStock());
                    mOrganizationStock.setNameStock(stock.getName());
                    quote = stock.getHistory();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(Object o) {
                super.deliverResult(o);

                stockName.setText(mOrganizationStock.getNameStock());
                mParentActivity.getSupportActionBar().setTitle(mOrganizationStock.getSymbolStock());

                BarChart chart = new BarChart(mContext);
                chartContainer.addView(chart);
                chart.animateXY(2000, 2000);

                List<BarEntry> entries1 = new ArrayList<>();
                ArrayList<String> strings = new ArrayList<>(quote.size());

                for (int i = 0, j = quote.size() - 1; i < quote.size(); i++, j--) {
                    entries1.add(new BarEntry(i, quote.get(j).getClose().floatValue()));
                    strings.add(i, getMonthForInt(quote.get(j).getDate().get(Calendar.MONTH)));
                }

                BarDataSet dataSet = new BarDataSet(entries1, "Months");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                BarData data = new BarData(dataSet);

                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(strings));

                chart.setData(data);
                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                DisplayMetrics displaymetrics = new DisplayMetrics();
                mParentActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int heightPixels = displaymetrics.heightPixels;
                int widthPixels = displaymetrics.widthPixels;
                int targetHeight = (heightPixels / 4) * 3;
//                int targetWidth= ( widthPixels/6 ) *5 ;
                chart.setLayoutParams(new FrameLayout.LayoutParams(widthPixels, targetHeight));
                chart.setPadding(32, 32, 32, 32);
                chart.invalidate();

                progressBar.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);

            }
        };
        loader.forceLoad();


    }

    private void extractStockData(Cursor cursor) {

        cursor.moveToFirst();
        String history = cursor.getString(POSITION_HISTORY);
        extractStockHistory(history);
//        mOrganizationStock.setSymbolStock(cursor.getString(POSITION_SYMBOL));

    }

    private void extractStockHistory(String history) {

    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                Contract.Quote.makeUriForStock(mOrganizationStock.getSymbolStock()),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.d("", cursor.toString());

        extractStockData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
