package com.udacity.stockhawk.data;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class StocksWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetDataProvider(this.getApplicationContext(), intent);
    }
}
