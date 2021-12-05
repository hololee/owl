package com.kiristudio.jh.owl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class settingPage extends Activity {

    TextView bar;

    UseDataBaseHelper helper;
    SQLiteDatabase db;

    private static int VER = 1;
    private static String DATABASE_NAME = "database";

    ImageButton track;
    ImageView marking, markingsub;

    Button Yes, No;

    boolean isSwitch1on = false;

    Animation Right, Left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


        bar = (TextView) findViewById(R.id.bar);
        Yes = (Button) findViewById(R.id.yes);
        No = (Button) findViewById(R.id.no);

        track = (ImageButton) findViewById(R.id.track);

        marking = (ImageView) findViewById(R.id.marking);
        markingsub = (ImageView) findViewById(R.id.markingsub);

        helper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);

        //isSwitch1on불러오기
        SharedPreferences prevSelect = getSharedPreferences("switch", MODE_PRIVATE);
        isSwitch1on = prevSelect.getBoolean("switch", false);

        if (isSwitch1on) {
            markingsub.setVisibility(View.VISIBLE);
            marking.setVisibility(View.GONE);
            track.setBackgroundResource(R.drawable.ontrack);
        }

        int h52 = (int) getResources().getDimension(R.dimen.h52);


        Right =new TranslateAnimation(0,h52,0,0);
        Right.setDuration(180);
        Left = new TranslateAnimation(h52,0,0,0);
        Left.setDuration(180);

        bar.setTypeface(Typeface.MONOSPACE);


        //스위치 a모션
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSwitch1on) {

                    //스위치가 켜져있을때 누르면
                    Left.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            marking.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    marking.startAnimation(Left);
                    markingsub.setVisibility(View.GONE);
                    track.setBackgroundResource(R.drawable.offtrack);

                    isSwitch1on = false;


                    //notify 삭제


                } else if (!isSwitch1on) {

                    ///스위치가 꺼져있을때 누르면
                    Right.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            markingsub.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    marking.startAnimation(Right);
                    marking.setVisibility(View.GONE);
                    track.setBackgroundResource(R.drawable.ontrack);

                    isSwitch1on = true;

                }

            }
        });


        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.down2, R.anim.down);
            }
        });


        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prevSelect = getSharedPreferences("switch", MODE_PRIVATE);
                SharedPreferences.Editor edit = prevSelect.edit();
                edit.putBoolean("switch", isSwitch1on);
                edit.commit();




                if (isSwitch1on) {


                    db = helper.getWritableDatabase();


                    Calendar cal = Calendar.getInstance();
                    int day2 = cal.get(Calendar.DAY_OF_WEEK);
                    //day2 는 현재 날짜
                    String[] args = {"" + day2};

                    Cursor cur1;
                    String TABLE_NAME = helper.TABLE_NAME;
                    cur1 = db.rawQuery("select Day, Date, UsedTime from " + TABLE_NAME + " where Day = ?", args);
                    //현재 날짜의 정보 불러오기
                    //Day ,Date , UsedTime 순으로 0,1,2

                    cur1.moveToNext();


                    long totalTime = cur1.getLong(2);

                    long totalHour = totalTime / 3600000;
                    long totalMinuite = (totalTime / 60000) - (60 * totalHour);

                    String content = totalHour + getString(R.string.title3) + totalMinuite +getString(R.string.title4) ;

                    SharedPreferences prevv = getSharedPreferences("Count", MODE_PRIVATE);
                    int Count = prevv.getInt("Count", 0);

                    NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder notifyBuilder = new Notification.Builder(getApplicationContext());
                    notifyBuilder.setSmallIcon(R.drawable.small);
                    Resources res = getResources();
                    notifyBuilder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nomal));
                    notifyBuilder.setOngoing(true);

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),2001,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notifyBuilder.setContentIntent(pendingIntent);

                    long[] vib = {1000,400};
                    notifyBuilder.setVibrate(vib);
                    notifyBuilder.setWhen(0);
                    notifyBuilder.setContentInfo(getString(R.string.Count) + " : " +Count);
                    notifyBuilder.setContentTitle(getString(R.string.title2));
                    notifyBuilder.setContentText(content + " " + getString(R.string.title5));
                    Notification notify = notifyBuilder.build();


                    noti.notify(00700, notify);

                  //  Log.e("Notify"," notify 활성화 ");

                } else if (!isSwitch1on) {


                    NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder notifyBuilder = new Notification.Builder(getApplicationContext());
                    Notification notify = notifyBuilder.build();


                    noti.cancel(00700);
                }


                finish();
                overridePendingTransition(R.anim.down2, R.anim.down);

                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                View toastView = View.inflate(getApplicationContext(), R.layout.toastview, null);
                toast.setView(toastView);
                toast.show();
            }
        });

        //isSwitch1on 로 스위치 켜짐 판별후 적용


    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.down2, R.anim.down);
    }
}
