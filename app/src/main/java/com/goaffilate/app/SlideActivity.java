package com.goaffilate.app;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.goaffilate.app.adapter.IntroAdapter;
import com.goaffilate.app.utils.CarouselEffectTransformer;
import com.goaffilate.app.utils.ClickableViewPager;

import java.util.ArrayList;
import java.util.List;

public class SlideActivity extends AppCompatActivity {

    private ClickableViewPager view_pager_slide;
    private IntroAdapter introAdapter;
    private List<Integer> slideList = new ArrayList<>();
    private ViewPagerIndicator view_pager_indicator;
    private RelativeLayout relative_layout_slide;
    private LinearLayout linear_layout_skip;
    private LinearLayout linear_layout_next;
    private TextView text_view_next_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_slide);

        slideList.add(1);
        slideList.add(2);
        slideList.add(3);
        slideList.add(4);

        this.text_view_next_done = (TextView) findViewById(R.id.text_view_next_done);
        this.linear_layout_next = (LinearLayout) findViewById(R.id.linear_layout_next);
        this.linear_layout_skip = (LinearLayout) findViewById(R.id.linear_layout_skip);
        this.view_pager_indicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        this.view_pager_slide = (ClickableViewPager) findViewById(R.id.view_pager_slide);
        this.relative_layout_slide = (RelativeLayout) findViewById(R.id.relative_layout_slide);
        introAdapter = new IntroAdapter(getApplicationContext(), slideList);
        view_pager_slide.setAdapter(this.introAdapter);
        view_pager_slide.setOffscreenPageLimit(1);
        view_pager_slide.setPageTransformer(false, new CarouselEffectTransformer(SlideActivity.this)); // Set transformer


        view_pager_slide.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < 7) {
                    view_pager_slide.setCurrentItem(position + 1);
                }     else {
                }
            }
        });
        this.linear_layout_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view_pager_slide.getCurrentItem() < slideList.size()) {
                    view_pager_slide.setCurrentItem(view_pager_slide.getCurrentItem() + 1);
                }
                if (view_pager_slide.getCurrentItem() + 1 == slideList.size()) {
                    Intent intent = new Intent(SlideActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();


                }

            }
        });
        view_pager_slide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position + 1 == slideList.size()) {
                    text_view_next_done.setText("DONE");

                } else {
                    text_view_next_done.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        this.linear_layout_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent = new Intent(SlideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }

//            }
        });
        this.view_pager_slide.setClipToPadding(false);
        this.view_pager_slide.setPageMargin(0);
        view_pager_indicator.setupWithViewPager(view_pager_slide);
    }
}
