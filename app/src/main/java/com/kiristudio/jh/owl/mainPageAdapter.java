package com.kiristudio.jh.owl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by lee on 2015-07-27.
 */
public class mainPageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mInflater;

    int h2,h358;


    //db
    private static int VER = 1;
    private static String DATABASE_NAME = "database";

    float degree;

    ImageView conner1, conner2, conner3, conner4, alpha1, alpha2, alpha3, alpha4;

    TextView percent;

    public mainPageAdapter(Context context) {

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = inflater;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v =null;

        if (position == 0) {
            v = mInflater.inflate(R.layout.page1, null);


            container.addView(v);

        } else if (position == 1) {

            v = mInflater.inflate(R.layout.page2, null);


            container.addView(v);

        } else if (position == 2) {


            v = mInflater.inflate(R.layout.page3, null);
            // invalidate 효과 복사
            TextView hour = (TextView) v.findViewById(R.id.hour);
            TextView minuite = (TextView) v.findViewById(R.id.minuite);

            h2 = (int) mContext.getResources().getDimension(R.dimen.h2);
            h358 = (int) mContext.getResources().getDimension(R.dimen.h358);

            ////////////////////////////////////////////////////////////////////////////////////////////



            FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout);
            alpha1 = (ImageView) v.findViewById(R.id.alpha1);
             alpha2 = (ImageView) v.findViewById(R.id.alpha2);
             alpha3 = (ImageView) v.findViewById(R.id.alpha3);
             alpha4 = (ImageView) v.findViewById(R.id.alpha4);
             conner1 = (ImageView)v.findViewById(R.id.conner1);
             conner2 = (ImageView)v.findViewById(R.id.conner2);
             conner3 = (ImageView)v.findViewById(R.id.conner3);
             conner4 = (ImageView)v.findViewById(R.id.conner4);

            percent = (TextView)v.findViewById(R.id.percent);

            frameLayout.setVisibility(View.VISIBLE);
            alpha1.setVisibility(View.VISIBLE);
            alpha2.setVisibility(View.VISIBLE);
            alpha3.setVisibility(View.VISIBLE);
            alpha4.setVisibility(View.VISIBLE);
            conner1.setVisibility(View.INVISIBLE);
            conner2.setVisibility(View.INVISIBLE);
            conner3.setVisibility(View.INVISIBLE);
            conner4.setVisibility(View.INVISIBLE);


            //보여질때 시간 누적(현재까지 이용시간)

            UseDataBaseHelper ahelper = new UseDataBaseHelper(mContext, DATABASE_NAME, null, VER);
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
            //시간 데이터 표시.



            long totalHour = totalTime / 3600000;

            Log.e("Hour", "" + totalHour);
            hour.setText("" + totalHour);


            long totalMinuite = (totalTime / 60000) - (60 * totalHour);
            Log.e("Minuite", "" + totalMinuite);
            minuite.setText("" + totalMinuite);


            //그래프 애니메이션,
            degree = (360 * (1000 * totalTime / 86400000)) / 1000;
            //degree = 340;
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
                Log.e("percent", "percent : " + percentage);

                percent.setText(String.valueOf(percentage) + "%");

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.intropercent);
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
                Animation Rotate2 = new RotateAnimation(from2, to2, 0, 0);//dp 값으로 바꾸기 120dp
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
                        Animation Rotate3 = new RotateAnimation(from3, to3, 0, 0);//dp 값으로 바꾸기 120dp
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

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.intropercent);
                percent.startAnimation(animation);



            } else if (degree >= 180 && degree < 270){

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
                Animation Rotate2 = new RotateAnimation(from2, to2, 0, 0);//dp 값으로 바꾸기 120dp
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

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.intropercent);
                percent.startAnimation(animation);



            }else if (degree >= 270){

                frameLayout.setVisibility(View.INVISIBLE);
                percent.setText("경고! 사용 시간이 너무 많습니다.");
                percent.setTextColor(Color.RED);
                percent.setTextSize(40);


            }





















            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            container.addView(v);

        } else if (position == 3) {

            v = mInflater.inflate(R.layout.page4, null);


            container.addView(v);

        } else if (position == 4) {
            v = mInflater.inflate(R.layout.page5, null);


            container.addView(v);
        } else {

            Log.e("error", "pager is over");
        }


        return v;

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View) object);
    }





}
