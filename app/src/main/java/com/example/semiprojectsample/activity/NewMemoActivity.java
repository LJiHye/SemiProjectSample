package com.example.semiprojectsample.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;
import com.example.semiprojectsample.dialog.DialogUtil;
import com.example.semiprojectsample.fragment.FragmentCamera;
import com.example.semiprojectsample.fragment.FragmentMemoWrite;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMemoActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    MemberBean memberBean;
    MemoBean memoBean;
    // 클릭 이벤트가 들어왔을 때 여기에 있는 값을 써야 해서 멤버 변수로 올려 줌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  //커스텀한 액션바로 보여주기
        getSupportActionBar().setCustomView(R.layout.custom_bar);  //커스텀한 액션바 가져오기

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        // 탭 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("메모"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // 뷰페이저 생성
         mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        // 탭과 뷰페이저를 서로 연결
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        findViewById(R.id.btnCancel).setOnClickListener(mBtnClick);
        findViewById(R.id.btnSave).setOnClickListener(mBtnClick);
    } // end onCreate

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private int tabCount;

        public ViewPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.tabCount = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new FragmentMemoWrite();
                case 1:
                    return new FragmentCamera();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    } //end class ViewPagerAdapter

    private View.OnClickListener mBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 어떤 버튼이 클릭됐는지 구분
            switch (view.getId()) {
                case R.id.btnCancel:
                    // 처리
                    finish();
                    break;

                case R.id.btnSave:
                    // 처리
                    saveProc();
                    break;
            }
        }
    };

    // 저장버튼 저장처리
    private void saveProc() {
        // 1. 첫 번째 프래그먼트의 EditText 값을 받아온다.
        FragmentMemoWrite f0 = (FragmentMemoWrite) mViewPagerAdapter.instantiateItem(mViewPager, 0); // 인덱스 0
        // 2. 두 번째 프래그먼트의 mPhotoPath 값을 가져온다
        FragmentCamera f1 = (FragmentCamera) mViewPagerAdapter.instantiateItem(mViewPager, 1); // 인덱스 0

        EditText edtWriteMemo = f0.getView().findViewById(R.id.edtWriteMemo);
        String memoStr = edtWriteMemo.getText().toString();

     /*   Log.e("SEMI", "MemoStr = " + memoStr + ", photoPath = " + photoPath);
        Toast.makeText(this, "MemoStr = " + memoStr + ", photoPath = " + photoPath, Toast.LENGTH_LONG).show();*/
        // TODO 파일 DB에 저장 처리
        memberBean = FileDB.getLoginMember(this);
        memoBean = new MemoBean();
        if(edtWriteMemo != null)
            memoBean.memo = edtWriteMemo.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy.MM.dd");
        Date currentTime = new Date();
        String dTime = sdf.format(currentTime);
        memoBean.memoDate = dTime;
        String photoPath = f1.mPhotoPath;
        if(f1.mPhotoPath != null) {
            memoBean.memoPicPath = photoPath;
        }

        //메모가 공백인지 체크한다.
        if( TextUtils.isEmpty(memoStr) ){
            Toast.makeText(this, "메모를 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }

     if(photoPath == null) {
         DialogUtil.showDialog(this, "Memo", "이미지를 추가하지 않았습니다. 메모를 저장하시겠습니까?",
                 "예", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         FileDB.addMemo(getApplicationContext(), memberBean.memId, memoBean);
                         //Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
                         finish();
                     }
                 },
                 "아니오", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         finish();
                     }
                 });
     }

        FileDB.addMemo(getApplicationContext(), memberBean.memId, memoBean);
        finish();

        /*// TODO 파일 DB에 저장 처리
        MemberBean memberBean = FileDB.getLoginMember(this);
        MemoBean memoBean = new MemoBean();
        memoBean.memo = memoStr;
        if(edtWriteMemo != null)
            memoBean.memo = edtWriteMemo.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy.MM.dd");
        Date currentTime = new Date();
        String dTime = sdf.format(currentTime);
        memoBean.memoDate = dTime;

        if(f1.mPhotoPath != null) {
            photoPath = f1.mPhotoPath;
            memoBean.memoPicPath = photoPath;
        }

        FileDB.addMemo(this, memberBean.memId, memoBean);
        Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();

        finish();
    }*/
    }
}
