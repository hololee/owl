package com.kiristudio.jh.owl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by jongH on 2015-08-16.
 */
public class bootOffReceiver extends BroadcastReceiver {


    private static int VER = 1;
    private static String DATABASE_NAME = "database";



    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences UsedTimePre = context.getSharedPreferences("UsedTime", Context.MODE_PRIVATE);
        Log.e("os Down", "true");

        // 스크린 off 와 동일


        Calendar cal22 = Calendar.getInstance();
        int dayof = cal22.get(Calendar.DAY_OF_WEEK);


        //요일 확인
        SharedPreferences prefe = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        int day = prefe.getInt("StartDay", dayof);

        //오늘 요일
        Calendar cal = Calendar.getInstance();
        long curTime = cal.getTimeInMillis();


        if (day == cal.get(Calendar.DAY_OF_WEEK)) {
            //날짜가 그대로 일때


            SharedPreferences startTimepre = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            long startTime = startTimepre.getLong("StartTime", curTime);


            int date = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH);

            String dAte = (month + 1) + "/" + date;

            long currentTime = cal.getTimeInMillis();
            long totalTime = (currentTime - startTime) + UsedTimePre.getLong("UsedTime", 0);


            SharedPreferences.Editor useTimeedit = UsedTimePre.edit();
            useTimeedit.putLong("UsedTime", totalTime);
            useTimeedit.commit();

            Log.e("Usage Time", "Time : " + (currentTime - startTime));
            Log.e("Usage Time", "Total Time : " + totalTime);

            //db에 당일 날짜  저장 totalTime, day,dAte

            UseDataBaseHelper helper = new UseDataBaseHelper(context, DATABASE_NAME, null, VER);
            SQLiteDatabase db = helper.getWritableDatabase();
            String tableName = helper.TABLE_NAME;

            db.execSQL("update " + tableName + " set Day = '" + day + "', Date = '" + dAte + "', UsedTime = '" + totalTime + "' where Day = '" + day + "'");



        } else if (day != cal.get(Calendar.DAY_OF_WEEK)) {
            //날짜가 바뀔때


            Calendar caltoday = Calendar.getInstance();
            int year = caltoday.get(Calendar.YEAR);
            int month = caltoday.get(Calendar.MONTH);
            int date = caltoday.get(Calendar.DATE);

            Calendar startcalDay = Calendar.getInstance();
            startcalDay.set(year, month, date, 00, 00, 00);
            long startinTime = startcalDay.getTimeInMillis();


            Calendar calcurrent = Calendar.getInstance();
            long currentinTime = calcurrent.getTimeInMillis();

            //count 초기화
            SharedPreferences prevv = context.getSharedPreferences("Count", Context.MODE_PRIVATE);

            SharedPreferences.Editor editote = prevv.edit();
            editote.putInt("Count", 0);
            editote.commit();


            //다음 날자 사용 시간
            long UsedNextDay = currentinTime - startinTime;


            SharedPreferences startPre = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            long startTime = startPre.getLong("StartTime", currentinTime);
            String StartDate = startPre.getString("StartDate", "error");

            //이전 일자 시용시간
            long UsedPrevDay = (currentinTime - startTime) - UsedNextDay;

            long totalTimed = UsedTimePre.getLong("UsedTime", 0) + UsedPrevDay;


            //그다음 db에다가  전날 시간(start day date등) 저장      totalTimed, day , StartDate

            UseDataBaseHelper helper = new UseDataBaseHelper(context, DATABASE_NAME, null, VER);
            SQLiteDatabase db = helper.getWritableDatabase();
            String tableName = helper.TABLE_NAME;

            db.execSQL("update " + tableName + " set Day = '" + day + "', Date = '" + StartDate + "', UsedTime = '" + totalTimed + "' where Day = '" + day + "'");


            //그후pre 초기화 한다음에 UsedNextDay 저장

            SharedPreferences.Editor edit = UsedTimePre.edit();
            edit.putLong("UsedTime", UsedNextDay);
            edit.commit();

            Log.e("Usage Time", "Time PrevDay : " + UsedPrevDay);
            Log.e("Usage Time", "Time NextDay : " + UsedNextDay);
            Log.e("Usage Time", "Total Time : ");

            //db에 당일 날짜 저장


        }



        //stop day 저장

        Calendar calCloseDay = Calendar.getInstance();
        int closeDay = calCloseDay.get(Calendar.DAY_OF_WEEK);

        SharedPreferences sharestop = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editute = sharestop.edit();
        editute.putInt("StopDay",closeDay);
        editute.commit();






    }
}
