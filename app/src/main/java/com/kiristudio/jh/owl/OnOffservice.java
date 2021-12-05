package com.kiristudio.jh.owl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;


public class OnOffservice extends Service {


    private static int VER = 1;
    private static String DATABASE_NAME = "database";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "start Service ");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");


        BroadcastReceiver OnOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //그날 총 사용 시간 저장
                SharedPreferences UsedTimePre = getSharedPreferences("UsedTime", MODE_PRIVATE);


                //스크린 켜질떄
                if (intent.getAction() == Intent.ACTION_SCREEN_ON) {

                    //스크린 온 카운트 횟수 1 추가
                    SharedPreferences prev = getSharedPreferences("Count", MODE_PRIVATE);
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

                    SharedPreferences sharestop = getSharedPreferences("time", MODE_PRIVATE);
                    int changeDay = sharestop.getInt("StopDay", day);

                    //날짜가 바뀌면
                    if (changeDay != day) {

                        //초기화
                        SharedPreferences.Editor useTimeedite = UsedTimePre.edit();
                        useTimeedite.putLong("UsedTime", 0);
                        useTimeedite.commit();

                        //count 초기화

                        SharedPreferences prevv = getSharedPreferences("Count", MODE_PRIVATE);

                        SharedPreferences.Editor editote = prevv.edit();
                        editote.putInt("Count", 0);
                        editote.commit();


                    }

                    Log.e("warring", "screen ON");

                    //시작점 측정
                    SharedPreferences Preference = getSharedPreferences("time", MODE_PRIVATE);
                    SharedPreferences.Editor editor = Preference.edit();
                    editor.putLong("StartTime", StartTime);
                    editor.putInt("StartDay", day);
                    editor.putString("StartDate", StartDate);
                    editor.commit();








                    //notification 설정
                    SharedPreferences prevSelect = getSharedPreferences("switch", MODE_PRIVATE);


                    if (prevSelect.getBoolean("switch", false)) {

                        UseDataBaseHelper   helper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
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

                        String content = totalHour + getString(R.string.title3) + totalMinuite +getString(R.string.title4) ;

                        SharedPreferences prevv = getSharedPreferences("Count", MODE_PRIVATE);
                        int Counts = prevv.getInt("Count", 0);


                        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification.Builder notifyBuilder = new Notification.Builder(getApplicationContext());
                        notifyBuilder.setSmallIcon(R.drawable.small);
                        Resources res = getResources();
                        notifyBuilder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nomal));
                        notifyBuilder.setPriority(2);
                        notifyBuilder.setOngoing(true);

                        Intent intents = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),2001,intents,PendingIntent.FLAG_UPDATE_CURRENT);
                        notifyBuilder.setContentIntent(pendingIntent);

                        notifyBuilder.setWhen(0);
                        notifyBuilder.setContentInfo(getString(R.string.Count) + " : " +Counts);
                        notifyBuilder.setContentTitle(getString(R.string.title2));
                        notifyBuilder.setContentText(content + " " + getString(R.string.title5));
                        Notification notify = notifyBuilder.build();


                        noti.notify(00700, notify);
                        //   Log.e("Notify", " notify 활성화 ");


                    }













                    //스크린 꺼질때
                } else if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {

                    Log.e("warring", "screen OFF");

                    Calendar cal22 = Calendar.getInstance();
                    int dayof = cal22.get(Calendar.DAY_OF_WEEK);


                    //요일 확인
                    SharedPreferences prefe = getSharedPreferences("time", MODE_PRIVATE);
                    int day = prefe.getInt("StartDay", dayof);

                    //오늘 요일
                    Calendar cal = Calendar.getInstance();
                    long curTime = cal.getTimeInMillis();


                    if (day == cal.get(Calendar.DAY_OF_WEEK)) {
                        //날짜가 그대로 일때


                        SharedPreferences startTimepre = getSharedPreferences("time", MODE_PRIVATE);
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

                        UseDataBaseHelper helper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
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
                        SharedPreferences prevv = getSharedPreferences("Count", MODE_PRIVATE);

                        SharedPreferences.Editor editote = prevv.edit();
                        editote.putInt("Count", 0);
                        editote.commit();


                        //다음 날자 사용 시간
                        long UsedNextDay = currentinTime - startinTime;


                        SharedPreferences startPre = getSharedPreferences("time", MODE_PRIVATE);
                        long startTime = startPre.getLong("StartTime", currentinTime);
                        String StartDate = startPre.getString("StartDate", "error");

                        //이전 일자 시용시간
                        long UsedPrevDay = (currentinTime - startTime) - UsedNextDay;

                        long totalTimed = UsedTimePre.getLong("UsedTime", 0) + UsedPrevDay;


                        //그다음 db에다가  전날 시간(start day date등) 저장      totalTimed, day , StartDate

                        UseDataBaseHelper helper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String tableName = helper.TABLE_NAME;

                        db.execSQL("update " + tableName + " set Day = '" + day + "', Date = '" + StartDate + "', UsedTime = '" + totalTimed + "' where Day = '" + day + "'");


                        //그후pre 초기화 한다음에 UsedNextDay 저장

                        SharedPreferences.Editor edit = UsedTimePre.edit();
                        edit.putLong("UsedTime", UsedNextDay);
                        edit.commit();

                        long totalNextTime = UsedTimePre.getLong("UsedTime",0);


                        //db에 당일 날짜 저장   //수정 요망


                    }



                    //stop day 저장

                    Calendar calCloseDay = Calendar.getInstance();
                    int closeDay = calCloseDay.get(Calendar.DAY_OF_WEEK);

                    SharedPreferences sharestop = getSharedPreferences("time", MODE_PRIVATE);
                    SharedPreferences.Editor editute = sharestop.edit();
                    editute.putInt("StopDay",closeDay);
                    editute.commit();



                }

            }
        };

        getApplicationContext().registerReceiver(OnOffReceiver, filter);


        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
