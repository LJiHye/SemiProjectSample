package com.example.semiprojectsample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;
import com.example.semiprojectsample.fragment.FragmentModifyCamera;
import com.example.semiprojectsample.fragment.FragmentModifyWrite;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ModifyMemoActivity extends AppCompatActivity {

    private TabLayout mTabLayoutModify;
    private ViewPager mViewPagerModify;
    private ViewPagerAdapter2 mViewPagerAdapter2;
    private Intent intent;
    private MemoBean memoBean;
    private MemberBean memberBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_memo);

        mTabLayoutModify = findViewById(R.id.tabLayoutModify);
        mViewPagerModify = findViewById(R.id.viewPagerModify);

        // 탭 생성
        mTabLayoutModify.addTab(mTabLayoutModify.newTab().setText("메모"));
        mTabLayoutModify.addTab(mTabLayoutModify.newTab().setText("사진"));
        mTabLayoutModify.setTabGravity(TabLayout.GRAVITY_FILL);

        // ViewPager 생성 (어댑터가 있어야 함 - ViewPagerAdapter)
        mViewPagerAdapter2 = new ViewPagerAdapter2(getSupportFragmentManager(), mTabLayoutModify.getTabCount());
        // 탭과 뷰페이저를 서로 연결
        mViewPagerModify.setAdapter(mViewPagerAdapter2);
        mViewPagerModify.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayoutModify));
        mTabLayoutModify.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPagerModify.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        findViewById(R.id.btnModWrite).setOnClickListener(mBtnClick);
        findViewById(R.id.btnModRemove).setOnClickListener(mBtnClick);
    } // end onCreate

    class ViewPagerAdapter2 extends FragmentPagerAdapter {
        private int tabCount;

        public ViewPagerAdapter2(FragmentManager fm, int count) {
            super(fm);
            this.tabCount = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentModifyWrite();
                case 1:
                    return new FragmentModifyCamera();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    private View.OnClickListener mBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 어떤 버튼이 클릭됐는지 구분
            switch (view.getId()) {
                case R.id.btnModWrite:
                    // 처리
                    modifyMemo();
                    break;

                case R.id.btnModRemove:
                    // 처리
                    removeMemo();
                    break;
            }
        }
    };

    private void modifyMemo() {
        FragmentModifyWrite f0 = (FragmentModifyWrite) mViewPagerAdapter2.instantiateItem(mViewPagerModify, 0);
        FragmentModifyCamera f1 = (FragmentModifyCamera) mViewPagerAdapter2.instantiateItem(mViewPagerModify, 1);

        EditText edtModifyMemo = f0.getView().findViewById(R.id.edtModifyMemo);
        String memoStr = edtModifyMemo.getText().toString();
        String photoPath = f1.mPhotoPath;

        intent = getIntent();
        memoBean = (MemoBean) intent.getSerializableExtra("MEMOBEAN");
        if(intent.getSerializableExtra("MEMOBEAN") == null) {
            Toast.makeText(this, "MEMOBEAN null", Toast.LENGTH_SHORT).show();
            return;
        }
        memberBean = FileDB.getLoginMember(this);

        memoBean.memo = memoStr;
        memoBean.memoPicPath = photoPath;

        FileDB.setMemo(this, memberBean.memId, memoBean);
        Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();

        //mViewPagerAdapter2.notifyDataSetChanged();
        finish();
    }

    private void removeMemo() {
        intent = getIntent();
        if(intent.getSerializableExtra("MEMOBEAN") == null) {
            Toast.makeText(this, "MEMOBEAN null", Toast.LENGTH_SHORT).show();
            return;
        }
        memoBean = (MemoBean) intent.getSerializableExtra("MEMOBEAN");
        memberBean = FileDB.getLoginMember(this);
        List<MemoBean> memoList = FileDB.getMemoList(this, memberBean.memId);

        FileDB.delMemo(this, memberBean.memId, memoBean.memoId);
        Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show();
        memoList = FileDB.getMemoList(this, memberBean.memId);

        //mViewPagerAdapter2.notifyDataSetChanged();
        finish();
    };
}

