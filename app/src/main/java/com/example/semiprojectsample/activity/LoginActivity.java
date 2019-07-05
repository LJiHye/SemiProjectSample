package com.example.semiprojectsample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.db.FileDB;

public class LoginActivity extends AppCompatActivity {

    //멤버변수 자리
    private EditText mEditId, mEditPw; // m을 붙이는 이유 : 멤버변수임을 명시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditId = findViewById(R.id.edtId);
        mEditPw = findViewById(R.id.edtPw);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnJoin = findViewById(R.id.btnJoin);

        btnLogin.setOnClickListener(mBtnLoginClick); // 리스너 연결
        btnJoin.setOnClickListener(mBtnJoinClick);

    } // End onCreate()

    // 로그인 버튼 클릭 이벤트 (클릭 리스너를 바깥에 뺐음)
    private View.OnClickListener mBtnLoginClick = new View.OnClickListener() { // Ctrl+Space 자동완성
        // 익명 클래스. 실시간으로 클래스가 만들어 진 것. (interface를 new 해서 클래스로 구현?)
        // 클래스의 객체로 구현받은 인스턴스
        // 여기서 this를 쓰면 에러남. LoginActivity.this
        @Override
        public void onClick(View view) {
            String memId = mEditId.getText().toString();
            String memPwd = mEditPw.getText().toString();

            MemberBean memberBean = FileDB.getFindMember(LoginActivity.this, memId);
            if(memberBean == null) {
                Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.equals(memberBean.memPw, memPwd)) {
                // 비밀번호 일치
                FileDB.setLoginMember(LoginActivity.this, memberBean); // 디비에 저장
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

    // 회원가입 버튼 클릭 이벤트
    private View.OnClickListener mBtnJoinClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(LoginActivity.this, CameraCapture2Activity.class); // 왼쪽 : Context, 오른쪽 : 갈 곳
            startActivity(i);
        }
   };
}
