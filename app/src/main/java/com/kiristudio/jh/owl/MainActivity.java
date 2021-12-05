package com.kiristudio.jh.owl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends Activity {

    TextView textView;

    ImageButton homeButton, settingButton;

    ImageView index0, index1, index2, index3, index4;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;

    int height, h2, h358;

    //db
    private static int VER = 1;
    private static String DATABASE_NAME = "database";

    //pageViewr 리소스들
    ImageView selected;
    TextView hour, minuite, percent;
    ImageView conner1, conner2, conner3, conner4, alpha1, alpha2, alpha3, alpha4;
    FrameLayout frameLayout;
    Bitmap bitmapSelect1, bitmapSelect2;
    ImageView oowl;


    float degree;

    Thread thread1, thread2;
    Handler handler = new Handler();
    int count;

    ViewPager pager;
    mainPageAdapter adapter;

    int positionK;


    boolean isplaying = false;
    boolean isplaying2 = false;

    boolean isRunning = false;

    boolean isfirstStarting = true;


    @Override
    protected void onResume() {
        super.onResume();


        Log.e("booleanstart", "" + isfirstStarting);

        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.kiristudio.jh.owl.OnOffservice".equals(service.service.getClassName())) {
                isRunning = true;

            }

        }

        Intent i = new Intent(getApplicationContext(), OnOffservice.class);

        Log.d("screen", "isRunning  = " + isRunning);

        if (isRunning == false) {
            startService(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.maintitle);
        homeButton = (ImageButton) findViewById(R.id.owlbanner);
        settingButton = (ImageButton) findViewById(R.id.owlsettings);

        index0 = (ImageView) findViewById(R.id.index01);
        index1 = (ImageView) findViewById(R.id.index02);
        index2 = (ImageView) findViewById(R.id.index03);
        index3 = (ImageView) findViewById(R.id.index04);
        index4 = (ImageView) findViewById(R.id.index05);

        //인디케이터 타일 설정
        setTile();

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new mainPageAdapter(getApplicationContext());
        pager.setAdapter(adapter);
        pager.setCurrentItem(2);
        pager.addOnPageChangeListener(new pageChangeListener(getApplicationContext()));

        SharedPreferences prevz = getSharedPreferences("First", MODE_PRIVATE);
        isfirstStarting = prevz.getBoolean("First", isfirstStarting);

        //처음 활동 할것만 작동
        if (isfirstStarting) {


            isfirstStarting = false;
            SharedPreferences prevk = getSharedPreferences("First", MODE_PRIVATE);
            SharedPreferences.Editor eidtor = prevk.edit();
            eidtor.putBoolean("First", isfirstStarting);
            eidtor.commit();
            Log.e("boolean", "" + isfirstStarting);
            Intent intent = new Intent(getApplicationContext(), FristPage.class);
            startActivity(intent);


        }


        setPagerAnimation(pager);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pager.setCurrentItem(2);

            }
        });


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), settingPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.up, R.anim.up2);

            }
        });

    }


    //페이지 전환 효과
    public void setPagerAnimation(ViewPager pager0) {

        pager0.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                float positionValue = Math.abs(1 - Math.abs(position));

                page.setScaleX(positionValue);
                page.setScaleY(positionValue);

                page.setAlpha(positionValue);


            }
        });
    }


    //버튼색 설정
    public void setTile() {


        Resources res = getResources();
        bitmap1 = BitmapFactory.decodeResource(res, R.drawable.index01);
        bitmap2 = BitmapFactory.decodeResource(res, R.drawable.index02);
        bitmap3 = BitmapFactory.decodeResource(res, R.drawable.index03);
        bitmap4 = BitmapFactory.decodeResource(res, R.drawable.index04);
        bitmap5 = BitmapFactory.decodeResource(res, R.drawable.index05);

        Bitmap index11 = Bitmap.createScaledBitmap(bitmap1, 15, 15, false);
        index0.setImageBitmap(index11);
        Bitmap index22 = Bitmap.createScaledBitmap(bitmap2, 15, 15, false);
        index1.setImageBitmap(index22);
        Bitmap index33 = Bitmap.createScaledBitmap(bitmap3, 15, 15, false);
        index2.setImageBitmap(index33);
        Bitmap index44 = Bitmap.createScaledBitmap(bitmap4, 15, 15, false);
        index3.setImageBitmap(index44);
        Bitmap index55 = Bitmap.createScaledBitmap(bitmap5, 15, 15, false);
        index4.setImageBitmap(index55);

    }


    //-인디케이터 효과-
    class pageChangeListener implements ViewPager.OnPageChangeListener {
        int displaywidth;

        public pageChangeListener(Context context) {


            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();

            displaywidth = display.getWidth();

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //position은 0부터 시작 같음


            /*Log.e("getSize", "" + displaywidth);
            Log.e("positionOffset", "" + positionOffset);
            Log.e("positionOffsetPixels", positionOffsetPixels + "");
            Log.e("posiiton", position + "");
            */

            double size1 = positionOffsetPixels / (double) displaywidth;

            //우측으로 움직일때 사이즈 변화값, size1 과 곲하는 값은 확대 크기
            int smallingsize = 40 - ((int) (25 * (size1)));
            int largingsize = 15 + ((int) (25 * (size1)));
            //Log.e("size", "" + largingsize);

            //위치에 따른 인덱스 사이즈 변화
            switch (position) {

                case 0: {
                    Bitmap index11 = Bitmap.createScaledBitmap(bitmap1, smallingsize, smallingsize, false);
                    index0.setImageBitmap(index11);

                    Bitmap index22 = Bitmap.createScaledBitmap(bitmap2, largingsize, largingsize, false);
                    index1.setImageBitmap(index22);

                    break;
                }
                case 1: {
                    Bitmap index22 = Bitmap.createScaledBitmap(bitmap2, smallingsize, smallingsize, false);
                    index1.setImageBitmap(index22);

                    Bitmap index33 = Bitmap.createScaledBitmap(bitmap3, largingsize, largingsize, false);
                    index2.setImageBitmap(index33);

                    break;
                }

                case 2: {
                    Bitmap index33 = Bitmap.createScaledBitmap(bitmap3, smallingsize, smallingsize, false);
                    index2.setImageBitmap(index33);

                    Bitmap index44 = Bitmap.createScaledBitmap(bitmap4, largingsize, largingsize, false);
                    index3.setImageBitmap(index44);

                    break;
                }

                case 3: {
                    Bitmap index44 = Bitmap.createScaledBitmap(bitmap4, smallingsize, smallingsize, false);
                    index3.setImageBitmap(index44);

                    Bitmap index55 = Bitmap.createScaledBitmap(bitmap5, largingsize, largingsize, false);
                    index4.setImageBitmap(index55);

                    break;
                }
            }
        }

        //maintext 및 owl 애니메이션 관리
        @Override
        public void onPageSelected(int position) {

            Log.e("pager", "PageSelected : page " + position);

            positionK = position;


            //셋팅 아이콘 표시

            if (position == 4) {

                settingButton.setVisibility(View.VISIBLE);

            } else {

                settingButton.setVisibility(View.GONE);

            }


            switch (position) {
                case 0: {
                    textView.setText("OWL");


                    selected = (ImageView) pager.getRootView().findViewById(R.id.selected);
                    Resources res = getResources();

                    //usedtime 으로 조건 주기
                    // index로 모양 선택 if (index==1d일때 머시기 저시기)
                    //charactor.로 owl 선ㄴ택

                    //초기 설정
                    bitmapSelect1 = BitmapFactory.decodeResource(res, R.drawable.nomal);
                    bitmapSelect2 = BitmapFactory.decodeResource(res, R.drawable.nomal_down);

                    SharedPreferences UsedTimePre = getSharedPreferences("UsedTime", MODE_PRIVATE);
                    long UsedTimed = UsedTimePre.getLong("UsedTime", 0);

                    if (UsedTimed >= 21600000) {

                        bitmapSelect1 = BitmapFactory.decodeResource(res, R.drawable.nomal_hit);
                        bitmapSelect2 = BitmapFactory.decodeResource(res, R.drawable.nomal_hit);

                    }


                    isplaying = true;


                    if (thread1 == null) {

                        thread1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                count = 0;
                                while (true) {

                                    if (isplaying) {
                                        if (count == 0) {

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    //selected.setImageResource(R.drawable.owl);
                                                    selected.setImageBitmap(bitmapSelect1);
                                                }
                                            });
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (count == 1) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // selected.setImageResource(R.drawable.owldown);
                                                    selected.setImageBitmap(bitmapSelect2);
                                                }
                                            });

                                            try {
                                                Thread.sleep(200);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        count++;
                                        if (count == 2) {
                                            count = 0;
                                        }
                                    }
                                }
                            }
                        });

                        thread1.start();
                    }

                    break;
                }


                case 1: {
                    textView.setText("Week");


                    ImageView[] gage;
                    TextView[] gageState, dates;

                    gage = new ImageView[7];
                    gage[0] = (ImageView) pager.getRootView().findViewById(R.id.gage1);
                    gage[1] = (ImageView) pager.getRootView().findViewById(R.id.gage2);
                    gage[2] = (ImageView) pager.getRootView().findViewById(R.id.gage3);
                    gage[3] = (ImageView) pager.getRootView().findViewById(R.id.gage4);
                    gage[4] = (ImageView) pager.getRootView().findViewById(R.id.gage5);
                    gage[5] = (ImageView) pager.getRootView().findViewById(R.id.gage6);
                    gage[6] = (ImageView) pager.getRootView().findViewById(R.id.gage7);

                    gageState = new TextView[7];
                    gageState[0] = (TextView) pager.getRootView().findViewById(R.id.gageState1);
                    gageState[1] = (TextView) pager.getRootView().findViewById(R.id.gageState2);
                    gageState[2] = (TextView) pager.getRootView().findViewById(R.id.gageState3);
                    gageState[3] = (TextView) pager.getRootView().findViewById(R.id.gageState4);
                    gageState[4] = (TextView) pager.getRootView().findViewById(R.id.gageState5);
                    gageState[5] = (TextView) pager.getRootView().findViewById(R.id.gageState6);
                    gageState[6] = (TextView) pager.getRootView().findViewById(R.id.gageState7);

                    dates = new TextView[7];
                    dates[0] = (TextView) pager.getRootView().findViewById(R.id.date1);
                    dates[1] = (TextView) pager.getRootView().findViewById(R.id.date2);
                    dates[2] = (TextView) pager.getRootView().findViewById(R.id.date3);
                    dates[3] = (TextView) pager.getRootView().findViewById(R.id.date4);
                    dates[4] = (TextView) pager.getRootView().findViewById(R.id.date5);
                    dates[5] = (TextView) pager.getRootView().findViewById(R.id.date6);
                    dates[6] = (TextView) pager.getRootView().findViewById(R.id.date7);


                    height = (int) getResources().getDimension(R.dimen.graph_height);

                    UseDataBaseHelper helper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String[] args = {"0"};

                    Cursor cur1;
                    String TABLE_NAME = helper.TABLE_NAME;
                    cur1 = db.rawQuery("select Day, Date, UsedTime from " + TABLE_NAME + " where Day > ?", args);
                    //Day ,Date ,  UsedTime 순으로 0,1,2

                    int count = cur1.getCount();
                    Log.d("count", count + "");

                    //테스트ㅡ용
                    String Date[] = {"일", "월", "화", "수", "목", "금", "토"};
                    int[] Day = {1, 2, 3, 4, 5, 6, 7};
                    long[] UsedTime = {86400000, 4250009, 0, 29999999, 55456464, 11235335, 23343365};

                    for (int i = 0; i < count; i++) {
                        //i를 요일 기준으로 잡기
                        cur1.moveToNext();


                        Day[i] = cur1.getInt(0);
                        Date[i] = cur1.getString(1);
                        UsedTime[i] = cur1.getLong(2);


                        int index = Day[i] - 1;
                        //Log.d("index", "day = " + Day[i] + ",  index = " + index);

                        Animation anim = new TranslateAnimation(0, 0, height, (float) (height - ((1000 * 31.25 * UsedTime[i] / 3600000) / 1000)));


                        // Log.d("gage", UsedTime[i] / 3600000 + ":" + i);
                        anim.setDuration(400);
                        anim.setFillAfter(true);
                        anim.setFillEnabled(true);

                        gage[index].startAnimation(anim);

                        double used = (double) (1000 * UsedTime[i] / 3600000) / (double) 1000;
                        int use = (int) (used * 10);
                        gageState[i].setText("" + (double) use / (double) 10);
                        dates[i].setText(Date[i]);
                    }
                    cur1.close();


                    break;
                }


                case 2: {


                    textView.setText("Today");

                    hour = (TextView) pager.getRootView().findViewById(R.id.hour);
                    minuite = (TextView) pager.getRootView().findViewById(R.id.minuite);

                    conner1 = (ImageView) pager.getRootView().findViewById(R.id.conner1);
                    conner2 = (ImageView) pager.getRootView().findViewById(R.id.conner2);
                    conner3 = (ImageView) pager.getRootView().findViewById(R.id.conner3);
                    conner4 = (ImageView) pager.getRootView().findViewById(R.id.conner4);
                    alpha1 = (ImageView) pager.getRootView().findViewById(R.id.alpha1);
                    alpha2 = (ImageView) pager.getRootView().findViewById(R.id.alpha2);
                    alpha3 = (ImageView) pager.getRootView().findViewById(R.id.alpha3);
                    alpha4 = (ImageView) pager.getRootView().findViewById(R.id.alpha4);
                    percent = (TextView) pager.getRootView().findViewById(R.id.percent);
                    frameLayout = (FrameLayout) pager.getRootView().findViewById(R.id.frameLayout);


                    frameLayout.setVisibility(View.VISIBLE);
                    alpha1.setVisibility(View.VISIBLE);
                    alpha2.setVisibility(View.VISIBLE);
                    alpha3.setVisibility(View.VISIBLE);
                    alpha4.setVisibility(View.VISIBLE);
                    conner1.setVisibility(View.INVISIBLE);
                    conner2.setVisibility(View.INVISIBLE);
                    conner3.setVisibility(View.INVISIBLE);
                    conner4.setVisibility(View.INVISIBLE);

                    h2 = (int) getResources().getDimension(R.dimen.h2);
                    h358 = (int) getResources().getDimension(R.dimen.h358);


                    UseDataBaseHelper ahelper = new UseDataBaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
                    SQLiteDatabase db = ahelper.getWritableDatabase();

                    Calendar cal = Calendar.getInstance();
                    int day2 = cal.get(Calendar.DAY_OF_WEEK);
                    //day2 는 현재 날짜
                    String[] args = {"" + day2};

                    Cursor cur1;
                    String TABLE_NAME = ahelper.TABLE_NAME;
                    cur1 = db.rawQuery("select Day, Date, UsedTime from " + TABLE_NAME + " where Day = ?", args);
                    //현재 날짜의 정보 불러오기
                    //Day ,Date , UsedTime 순으로 0,1,2

                    cur1.moveToNext();


                    long totalTime = cur1.getLong(2);


                    long totalHour = totalTime / 3600000;

                    //Log.e("Hour", "" + totalHour);
                    hour.setText("" + totalHour);


                    long totalMinuite = (totalTime / 60000) - (60 * totalHour);
                    // Log.e("Minuite", "" + totalMinuite);
                    minuite.setText("" + totalMinuite);


                    //그래프 애니메이션,
                    degree = (360 * (1000 * totalTime / 86400000)) / 1000;

                    Log.e("totalTime", "" + totalTime);
                    Log.e("degree", "" + degree);
                    //conner1
                    if (degree >= 0 && degree < 90) {


                        alpha2.setVisibility(View.INVISIBLE);

                        conner2.setVisibility(View.VISIBLE);


                        float from = -90;
                        float to = -(90 - (degree));
                        Animation Rotate1 = new RotateAnimation(from, to, h2, h358);//dp 값으로 바꾸기 120dp

                        Rotate1.setDuration(150);
                        Rotate1.setFillEnabled(true);
                        Rotate1.setFillAfter(true);
                        Rotate1.setStartOffset(150);
                        conner2.startAnimation(Rotate1);


                        //퍼센테이지 텍스트 설정
                        int percentage = (int) (100 * (degree / 360));
                        // Log.e("percent", "percent : " + percentage);

                        percent.setText(String.valueOf(percentage) + "%");

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.intropercent);
                        percent.startAnimation(animation);


                    } else if (degree >= 90 && degree < 180) {


                        alpha2.setVisibility(View.INVISIBLE);
                        alpha3.setVisibility(View.INVISIBLE);

                        conner2.setVisibility(View.VISIBLE);
                        conner3.setVisibility(View.VISIBLE);


                        float from1 = -90;
                        float to1 = 0;
                        Animation Rotate1 = new RotateAnimation(from1, to1, h2, h358);//dp 값으로 바꾸기 120dp
                        Rotate1.setDuration(150);
                        Rotate1.setFillEnabled(true);
                        Rotate1.setFillAfter(true);
                        Rotate1.setStartOffset(150);
                        conner2.setAnimation(Rotate1);


                        float from2 = -180;
                        float to2 = -90;
                        Animation Rotate2 = new RotateAnimation(from2, to2, h2, h2);//dp 값으로 바꾸기 120dp

                        Rotate2.setDuration(150);
                        Rotate2.setFillEnabled(true);
                        Rotate2.setFillAfter(true);
                        Rotate1.setStartOffset(150);
                        conner3.startAnimation(Rotate2);

                        Rotate1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                                float from3 = -90;
                                float to3 = -(90 - (degree - 90));
                                Animation Rotate3 = new RotateAnimation(from3, to3, h2, h2);//dp 값으로 바꾸기 120dp

                                Rotate3.setDuration(150);
                                Rotate3.setFillEnabled(true);
                                Rotate3.setFillAfter(true);
                                conner3.startAnimation(Rotate3);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        //퍼센테이지 텍스트 설정
                        int percentage = (int) (100 * (degree / 360));
                        Log.e("percent", "percent : " + percentage);

                        percent.setText(String.valueOf(percentage) + "%");

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.intropercent);
                        percent.startAnimation(animation);


                    } else if (degree >= 180 && degree < 270) {

                        alpha2.setVisibility(View.INVISIBLE);
                        alpha3.setVisibility(View.INVISIBLE);
                        alpha4.setVisibility(View.INVISIBLE);

                        conner2.setVisibility(View.VISIBLE);
                        conner3.setVisibility(View.VISIBLE);
                        conner4.setVisibility(View.VISIBLE);


                        float from1 = -90;
                        float to1 = 0;
                        Animation Rotate1 = new RotateAnimation(from1, to1, h2, h358);//dp 값으로 바꾸기 120dp

                        Rotate1.setDuration(150);
                        Rotate1.setFillEnabled(true);
                        Rotate1.setFillAfter(true);
                        Rotate1.setStartOffset(150);
                        conner2.setAnimation(Rotate1);


                        float from2 = -180;
                        float to2 = -90;
                        Animation Rotate2 = new RotateAnimation(from2, to2, h2, h2);//dp 값으로 바꾸기 120dp
                        Rotate2.setDuration(150);
                        Rotate2.setFillEnabled(true);
                        Rotate2.setFillAfter(true);
                        Rotate2.setStartOffset(150);
                        conner3.startAnimation(Rotate2);


                        float from3 = -270;
                        float to3 = -180;
                        Animation Rotate3 = new RotateAnimation(from3, to3, 0, 0);//dp 값으로 바꾸기 120dp
                        Rotate3.setDuration(150);
                        Rotate3.setFillEnabled(true);
                        Rotate3.setFillAfter(true);
                        Rotate3.setStartOffset(150);
                        conner4.startAnimation(Rotate3);

                        Rotate1.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                float from2 = -90;
                                float to2 = 0;
                                Animation Rotate2 = new RotateAnimation(from2, to2, 0, 0);//dp 값으로 바꾸기 120dp
                                Rotate2.setDuration(150);
                                Rotate2.setFillEnabled(true);
                                Rotate2.setFillAfter(true);
                                Rotate2.setStartOffset(150);
                                conner3.startAnimation(Rotate2);


                                float from3 = -180;
                                float to3 = -90;
                                Animation Rotate3 = new RotateAnimation(from3, to3, h358, 0);//dp 값으로 바꾸기 120dp
                                Rotate3.setDuration(150);
                                Rotate3.setFillEnabled(true);
                                Rotate3.setFillAfter(true);
                                Rotate3.setStartOffset(150);
                                conner4.startAnimation(Rotate3);

                                Rotate2.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {


                                        float from3 = -90;
                                        float to3 = -(90 - (degree - 180));
                                        Animation Rotate3 = new RotateAnimation(from3, to3, h358, 0);//dp 값으로 바꾸기 120dp
                                        Rotate3.setDuration(150);
                                        Rotate3.setFillEnabled(true);
                                        Rotate3.setFillAfter(true);
                                        Rotate3.setStartOffset(150);

                                        conner4.startAnimation(Rotate3);


                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                        //퍼센테이지 텍스트 설정
                        int percentage = (int) (100 * (degree / 360));
                        Log.e("percent", "percent : " + percentage);

                        percent.setText(String.valueOf(percentage) + "%");

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.intropercent);
                        percent.startAnimation(animation);


                    } else if (degree >= 270) {

                        frameLayout.setVisibility(View.INVISIBLE);
                        percent.setText("경고! 사용 시간이 너무 많습니다.");
                        percent.setTextColor(Color.RED);
                        percent.setTextSize(40);


                    }

                    break;

                }
                case 3: {
                    textView.setText("Count");

                    ImageView countBack = (ImageView) pager.getRootView().findViewById(R.id.back);
                    ImageView countBack2 = (ImageView) pager.getRootView().findViewById(R.id.back2);
                    ImageView countBack3 = (ImageView) pager.getRootView().findViewById(R.id.back3);
                    ImageView countBack4 = (ImageView) pager.getRootView().findViewById(R.id.back4);


                    TextView count = (TextView) pager.getRootView().findViewById(R.id.count);

                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coount_rotate);
                    Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coount_rotate2);
                    Animation anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coount_rotate3);
                    Animation anim5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coount_rotate4);


                    countBack.startAnimation(anim);
                    countBack2.startAnimation(anim3);
                    countBack3.startAnimation(anim4);
                    countBack4.startAnimation(anim5);

                    //카운트 표시
                    SharedPreferences prevv = getSharedPreferences("Count", MODE_PRIVATE);
                    int Count = prevv.getInt("Count", 0);
                    count.setText("" + Count);

                    Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
                    count.startAnimation(anim2);


                    break;
                }
                case 4: {


                    //스클롤 뷰에 addView 방식으로 추가
                    textView.setText("About OWL");

                    oowl = (ImageView) pager.getRootView().findViewById(R.id.owlTitle);

                    //부엉이 움직임///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    isplaying2 = true;


                    if (thread2 == null) {

                        thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                count = 0;
                                while (true) {

                                    if (isplaying2) {
                                        if (count == 0) {

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {


                                                    oowl.setImageResource(R.drawable.about_title);
                                                }
                                            });
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (count == 1) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    oowl.setImageResource(R.drawable.about_title_down);
                                                }
                                            });

                                            try {
                                                Thread.sleep(200);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        count++;
                                        if (count == 2) {
                                            count = 0;
                                        }
                                    }
                                }
                            }
                        });

                        thread2.start();
                    }


                    //부엉이 움직임///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    break;
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {


            if (positionK > 0) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                    if (isplaying == true) {

                        //  try {
                        isplaying = false;
                        // thread1.join();

                        /// } catch (InterruptedException e) {
                        //     e.printStackTrace();
                        //}


                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        pager.setCurrentItem(2);
    }






}


