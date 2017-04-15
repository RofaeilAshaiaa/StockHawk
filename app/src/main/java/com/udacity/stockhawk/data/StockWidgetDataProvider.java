package com.udacity.stockhawk.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.objects.OrganizationStock;
import com.udacity.stockhawk.ui.StockDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.stockhawk.data.Contract.Quote.POSITION_ABSOLUTE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.POSITION_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.POSITION_SYMBOL;

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
        if (cursor != null) {
            cursor.moveToFirst();
        }
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
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteView =
                new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote_widget);

        if (mCollection.get(position).getChange() < 0)
            remoteView.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);

        remoteView.setTextViewText(R.id.symbol, mCollection.get(position).getSymbolStock());
        remoteView.setTextViewText(R.id.price, Float.toString(mCollection.get(position).getPrice()));
        remoteView.setTextViewText(R.id.change, Float.toString(mCollection.get(position).getChange()));


        Intent intent = new Intent(mContext, StockDetailActivity.class);
        intent.putExtra(mContext.getString(R.string.intent_extra_symbol_key), mCollection.get(position).getSymbolStock());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        remoteView.setOnClickPendingIntent(R.id.stock_list_item, pendingIntent);

        return remoteView;


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

        if (cursor != null) {
            cursor.close();
        }

        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provider **/
        final long identityToken = Binder.clearCallingIdentity();

        cursor = mContext.getContentResolver()
                .query(Contract.Quote.URI,
                        null,
                        null, null, Contract.Quote.COLUMN_SYMBOL);

        Binder.restoreCallingIdentity(identityToken);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            OrganizationStock stock = new OrganizationStock();
            stock.setSymbolStock(cursor.getString(POSITION_SYMBOL));
            stock.setPrice(cursor.getFloat(POSITION_PRICE));
            stock.setChange(cursor.getFloat(POSITION_ABSOLUTE_CHANGE));
            mCollection.add(stock);

        }

    }

}
