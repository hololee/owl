package com.kiristudio.jh.owl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Calendar;


public class bootOnReceiver extends BroadcastReceiver {

    private static int VER = 1;
    private static String DATABASE_NAME = "database";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("warring", "start APP");


        Intent startIntent = new Intent(context, OnOffservice.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(startIntent);








        //OS 켜질때 스크린 온과 같은 작업 실행 // startday 등 저장하기
        //추가할 경우 및에 스위치 작업은 중복이므로 삭제 할것.
        //반대로 꺼질때에는 스크린 오프와 같은 작업을 실행 //db 저장등

        SharedPreferences UsedTimePre = context.getSharedPreferences("UsedTime", Context.MODE_PRIVATE);




        //스크린 온 카운트 횟수 1 추가
        SharedPreferences prev = context.getSharedPreferences("Count", Context.MODE_PRIVATE);
        int ScreenCount = prev.getInt("Count", 0);

        ScreenCount++;

        SharedPreferences.Editor edittt = prev.edit();
        edittt.putInt("Count", ScreenCount);
        edittt.commit();


        //시작..
        Calendar calstart = Calendar.getInstance();
        long StartTime = calstart.getTimeInMillis();

        int ththmonth = calstart.get(Calendar.MONTH);
        int ththDAte = calstart.get(Calendar.DATE);

        String StartDate = (ththmonth + 1) + "/" + ththDAte;


        //1= 일 , 2= 월.....
        int day = calstart.get(Calendar.DAY_OF_WEEK);

        //켜졌을때 날짜가 바뀌어 있으면 기존 시간 합을 전날로 저장 그리고 초기화;

        SharedPreferences sharestop = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        int changeDay = sharestop.getInt("StopDay", day);

        //날짜가 바뀌면
        if (changeDay != day) {

            //초기화
            SharedPreferences.Editor useTimeedite = UsedTimePre.edit();
            useTimeedite.putLong("UsedTime", 0);
            useTimeedite.commit();

            //count 초기화

            SharedPreferences prevv = context.getSharedPreferences("Count", Context.MODE_PRIVATE);

            SharedPreferences.Editor editote = prevv.edit();
            editote.putInt("Count", 0);
            editote.commit();


        }

        Log.e("warring", "screen ON");

        //시작점 측정
        SharedPreferences Preference = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Preference.edit();
        editor.putLong("StartTime", StartTime);
        editor.putInt("StartDay", day);
        editor.putString("StartDate", StartDate);
        editor.commit();








        //notification 설정
        SharedPreferences prevSelect = context.getSharedPreferences("switch", Context.MODE_PRIVATE);


        if (prevSelect.getBoolean("switch", false)) {

            UseDataBaseHelper   helper = new UseDataBaseHelper(context, DATABASE_NAME, null, VER);
            SQLiteDatabase db = helper.getWritableDatabase();


            Calendar cal2 = Calendar.getInstance();
            int day2 = cal2.get(Calendar.DAY_OF_WEEK);
            //day2 는 현재 날짜
            String[] args = {"" + day2};

            Cursor cur1;
            String TABLE_NAME = helper.TABLE_NAME;
            cur1 = db.rawQuery("select Day, Date, UsedTime from " + TABLE_NAME + " where Day = ?", args);
            //현재 날짜의 정보 불러오기0
            //Day ,Date , UsedTime 순으로 0,1,2

            cur1.moveToNext();


            long totalTime = cur1.getLong(2);

            long totalHour = totalTime / 3600000;
            long totalMinuite = (totalTime / 60000) - (60 * totalHour);

            String content = totalHour + context.getString(R.string.title3) + totalMinuite +context.getString(R.string.title4) ;

            SharedPreferences prevv = context.getSharedPreferences("Count", Context.MODE_PRIVATE);
            int Counts = prevv.getInt("Count", 0);


            NotificationManager noti = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder notifyBuilder = new Notification.Builder(context);
            notifyBuilder.setSmallIcon(R.drawable.small);
            Resources res = context.getResources();
            notifyBuilder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nomal));
            notifyBuilder.setPriority(2);
            notifyBuilder.setOngoing(true);

            Intent intents = new Intent(context,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,2001,intents,PendingIntent.FLAG_UPDATE_CURRENT);
            notifyBuilder.setContentIntent(pendingIntent);

            notifyBuilder.setWhen(0);
            notifyBuilder.setContentInfo(context.getString(R.string.Count) + " : " +Counts);
            notifyBuilder.setContentTitle(context.getString(R.string.title2));
            notifyBuilder.setContentText(content + " " + context.getString(R.string.title5));
            Notification notify = notifyBuilder.build();


            noti.notify(00700, notify);
            //   Log.e("Notify", " notify 활성화 ");


        }


    }
}
