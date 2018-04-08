package com.example.newbies.desktopappweight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NewBies
 * @date 2018/4/7
 */

public class ListViewService extends RemoteViewsService {

    public static final String INITENT_DATA = "extra_data";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
         return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context context;

        private List<String> list = new ArrayList<>();

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            list.add("一");
            list.add("二");
            list.add("三");
            list.add("四");
            list.add("五");
            list.add("六");
            list.add("七");
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            list.clear();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, "item:" + list.get(position));

            Bundle extras = new Bundle();
            extras.putInt(ListViewService.INITENT_DATA, position);
            Intent changeIntent = new Intent();
            changeIntent.setAction(MyWidgetProvider.CHANGE_IMAGE);
            changeIntent.putExtras(extras);

            /*
             * android.R.layout.simple_list_item_1 --- id --- text1
             * listView的item click：将 changeIntent 发送，
             * changeIntent 它默认的就有action 是provider中使用 setPendingIntentTemplate 设置的action
             */
            views.setOnClickFillInIntent(android.R.id.text1, changeIntent);
            return views;
        }

        /**
         * 在更新界面的时候如果耗时就会显示 正在加载... 的默认字样，但是你可以更改这个界面
         * 如果返回null 显示默认界面
         * 否则 加载自定义的，返回RemoteViews
         * @return
         */
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
            return false;
        }
    }
}
