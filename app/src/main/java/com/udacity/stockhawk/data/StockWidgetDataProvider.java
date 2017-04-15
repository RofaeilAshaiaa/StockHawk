package com.udacity.stockhawk.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.objects.OrganizationStock;

import java.util.ArrayList;
import java.util.List;

public class StockWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private List<OrganizationStock> mCollection = new ArrayList<>();
    private Context mContext;
    private Intent intent;
    private Cursor cursor;

    public StockWidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
//        return cursor.getCount();
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteView = null;

//        if (mCollection.get(position).getChange() > 0)
//            remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote_widget);
//        else if (mCollection.get(position).getChange() < 0)
//            remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote_widget_red);
        remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote_widget);

        if (mCollection.get(position).getChange() < 0)
            remoteView.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);

        remoteView.setTextViewText(R.id.symbol, mCollection.get(position).getSymbolStock());
        remoteView.setTextViewText(R.id.price, Float.toString(mCollection.get(position).getPrice()));
        remoteView.setTextViewText(R.id.change, Float.toString(mCollection.get(position).getChange()));
        return remoteView;

//        RemoteViews view = new RemoteViews(mContext.getPackageName(),
//                android.R.layout.simple_list_item_1);
//        view.setTextViewText(android.R.id.text1,
//                mCollection.get(position).getSymbolStock());
//        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        mCollection.clear();

//        if (cursor != null) {
//            cursor.close();
//        }

//        cursor = mContext.getContentResolver()
//                .query(Contract.Quote.URI,
//                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
//                null, null, Contract.Quote.COLUMN_SYMBOL) ;
//
//        cursor.moveToFirst() ;
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToNext();
//            OrganizationStock stock = new OrganizationStock();
//            stock.setSymbolStock(cursor.getString(POSITION_SYMBOL));
//            stock.setPrice(cursor.getFloat(POSITION_PRICE));
//            stock.setChange(cursor.getFloat(POSITION_ABSOLUTE_CHANGE));
//            mCollection.add(stock);
//
//        }


        for (float i = 1; i <= 10; i++) {
            OrganizationStock stock = new OrganizationStock();
            stock.setSymbolStock("item " + i);
            stock.setPrice(i);
            if (i % 2 == 0)
                stock.setChange(-i);
            else
                stock.setChange(i);
            mCollection.add(stock);
        }


    }

}
