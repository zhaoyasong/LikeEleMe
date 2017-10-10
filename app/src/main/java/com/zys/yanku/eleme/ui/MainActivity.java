package com.zys.yanku.eleme.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zys.yanku.eleme.CtrlLinearLayoutManager;
import com.zys.yanku.eleme.MarginConfig;
import com.zys.yanku.eleme.R;
import com.zys.yanku.eleme.ZoomHeaderView;
import com.zys.yanku.eleme.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ZoomHeaderView mZoomHeader;
    private ViewPager mViewPager;
    private RelativeLayout mBottomView;

    //判断是不是第一次进入应用
    private boolean isFirst = true;
    private int mBottomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initWidget();
    }

    private void initWidget() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mZoomHeader = (ZoomHeaderView) findViewById(R.id.zoomHeader);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new Adapter());
        mViewPager.setOffscreenPageLimit(4);
        CtrlLinearLayoutManager layoutManager = new CtrlLinearLayoutManager(this);
        //未展开禁止滑动
        layoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new HomeAdapter());
        mRecyclerView.setAlpha(0);
        mBottomView = (RelativeLayout) findViewById(R.id.rv_bottom);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            for (int i = 0; i < mViewPager.getChildCount(); i++) {
                View v = mViewPager.getChildAt(i).findViewById(R.id.ll_bottom);
                v.setY(mViewPager.getChildAt(i).findViewById(R.id.imageView).getHeight());
                v.setX(MarginConfig.MARGIN_LEFT_RIGHT);

                //触发一次dependency变化 让按钮归位
                mZoomHeader.setY(mZoomHeader.getY() - 1);
                isFirst = false;
            }
        }

        //隐藏底部状态栏
        mBottomY = (int) mBottomView.getY();
        mBottomView.setTranslationY(mBottomView.getY() + mBottomView.getHeight());
        mZoomHeader.setBottomView(mBottomView, mBottomY);
    }

    class Adapter extends PagerAdapter {

        private List<View> mViews;

        public Adapter() {
            mViews = new ArrayList<>();
            mViews.add(View.inflate(MainActivity.this, R.layout.item_img, null));
            mViews.get(0).findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "buy", Toast.LENGTH_SHORT).show();
                }
            });

            mViews.add(View.inflate(MainActivity.this, R.layout.item_img, null));
            mViews.add(View.inflate(MainActivity.this, R.layout.item_img, null));
        }

        private int[] imgs = {R.drawable.pizza, R.drawable.pic2, R.drawable.pic3};

        @Override
        public int getCount() {
            return mViews.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mViews.get(position).findViewById(R.id.imageView).setBackgroundResource(imgs[position]);
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mZoomHeader.isExpand()) {
            mZoomHeader.restore(mZoomHeader.getY());
        } else {
            finish();
        }
    }
}
