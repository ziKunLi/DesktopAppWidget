package com.example.newbies.desktopappweight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 *
 * @author NewBies
 * @date 2018/fourth/seventh
 */

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.example.newbies.desktop.app.widget.CLICK";
    public static final String CHANGE_IMAGE = "com.example.newbies.desktop.app.widget.CHANGE.IMAGE";

    private RemoteViews remoteViews;
    private ComponentName componentName;
    private int[] images = new int[]{
            R.drawable.ic_add,
            R.drawable.ic_cencle,
            R.drawable.ic_add,
            R.drawable.ic_cencle,
            R.drawable.ic_add,
            R.drawable.ic_cencle,
            R.drawable.ic_add
    };

    /**
     * 到达指定的更新时间或者当用户向桌面添加AppWidget时被调用
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds 桌面上所有的widget都会被分配一个唯一的ID标识，这个数组就是他们的列表
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        //first、创建一个 RemoteViews 对象，这个对象加载时指定了桌面小部件的界面布局文件
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.jump,"点击跳转到Activity");
        Intent jumpIntent = new Intent(context,ConfigActivity.class);
        PendingIntent jumpPendingIntent = PendingIntent.getActivity(context,2,jumpIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.jump,jumpPendingIntent);

        // 设置 ListView 的adapter。
        // (01) intent: 对应启动 ListViewService(RemoteViewsService) 的intent
        // (02) setRemoteAdapter: 设置 ListView 的适配器
        // 通过setRemoteAdapter将 ListView 和ListViewService关联起来，
        // 以达到通过 GridWidgetService 更新 gridview 的目的
        Intent listViewIntent = new Intent(context, ListViewService.class);
        remoteViews.setRemoteAdapter(R.id.listView, listViewIntent);
        remoteViews.setEmptyView(R.id.listView,android.R.id.empty);

        // 设置响应 ListView 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
        // 它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        // (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        // (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”

        /*
         * setPendingIntentTemplate 设置pendingIntent 模板
         * setOnClickFillInIntent 可以将fillInIntent 添加到pendingIntent中
         */
        Intent toIntent = new Intent(CHANGE_IMAGE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 20, toIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.listView, pendingIntent);


        componentName = new ComponentName(context, MyWidgetProvider.class);

        //可能有多个小部件处于活动状态，因此请更新所有小部件
        //fourth、调用 AppWidgetManager 更新桌面小部件
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (CLICK_ACTION.equals(intent.getAction())) {
            Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        }
        //更换图片
        else if(TextUtils.equals(CHANGE_IMAGE,intent.getAction())){
            Bundle extras = intent.getExtras();
            int position = extras.getInt(ListViewService.INITENT_DATA);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setImageViewResource(R.id.img, images[position]);
            componentName = new ComponentName(context, MyWidgetProvider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
