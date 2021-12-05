package com.kiristudio.jh.owl;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

/**
 * Created by 종혁 on 2015-08-14.
 */
public class FristPage extends Activity {
    ViewPager ppager;
    PagerAdapter padapter;
    Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ppager = (ViewPager) findViewById(R.id.ppager);
        padapter = new firstPageAdapter(getApplicationContext());
        ppager.setAdapter(padapter);
        ppager.setCurrentItem(0);

        ppager.addOnPageChangeListener(new pageChangeListener2());

        Button button = (Button) findViewById(R.id.sssss);
        button.setText("NEXT");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ppager.setCurrentItem(1);

            }
        });



    }

    private class pageChangeListener2 implements ViewPager.OnPageChangeListener {


        public pageChangeListener2() {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0: {

                    Button button = (Button) findViewById(R.id.sssss);
                    button.setText("NEXT");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          ppager.setCurrentItem(1);

                        }
                    });


                    break;
                }

                case 1: {

                    Button button = (Button) findViewById(R.id.sssss);
                    button.setText("NEXT");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ppager.setCurrentItem(2);

                        }
                    });

                    break;
                }

                case 2: {


                    Button button = (Button) findViewById(R.id.sssss);
                    button.setText(R.string.button3);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finish();

                        }
                    });

                    break;
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
