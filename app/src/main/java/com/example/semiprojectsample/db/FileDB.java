package com.example.semiprojectsample.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.semiprojectsample.bean.MemberBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FileDB {
    private static final String FILE_DB = "FileDB";
    private static Gson mGson = new Gson();

    private static SharedPreferences getSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_DB, Context.MODE_PRIVATE);
        return sp;
    }

    // 새로운 멤버 추가. 리스트 자체를 저장
    public static void addMember(Context context, MemberBean memberBean) {
        // 1. 기존의 멤버 리스트를 불러온다.
        List<MemberBean> memberList = getMemberList(context);
        // 2. 기존의 멤버 리스트에 추가한다.
        memberList.add(memberBean);
        // 3. 멤버 리스트를 저장한다.
        String listStr = mGson.toJson(memberList); // 그냥 저장하면 덮어쓰기밖에 안 됨
        // 4. 저장한다.
        SharedPreferences.Editor editor = getSp(context).edit().putString("memberList", listStr);
        editor.putString("memberList", listStr);
        editor.commit(); // 커밋을 해 줘야 저장됨!!
    }

    // 리스트 가져오기
    public static List<MemberBean> getMemberList(Context context) {
        String listStr = getSp(context).getString("memberList", null); // Key : memberList, default : null

        // 저장된 리스트가 없는 경우 새로운 리스트를 리턴한다.
        if (listStr == null) {
            return new ArrayList<MemberBean>();
        }
        // 저장된 리스트가 있는 경우 Gson으로 변환한다
        List<MemberBean> memberList = mGson.fromJson(listStr, new TypeToken<List<MemberBean>>(){}.getType());
        return memberList; // JSON 형태가 다시 List 형태로 바뀌어 넘어감
    }

    // 멤버 체크
    public static MemberBean getFindMember(Context context, String memId) {
        // 1. 멤버 리스트를 가져온다.
        List<MemberBean> memberList = getMemberList(context);
        // 2. for 문 돌면서 해당 아이디를 찾는다.
        for(MemberBean bean : memberList) {
            if(TextUtils.equals(bean.memId, memId)) { // 아이디가 같다.
                // 3. 찾았을 경우는 해당 MemberBean을 리턴한다.
                return bean;
            }
        }
        // 3-2. 못찾았을 경우는 null 리턴
        return null;
        // aa라는 유저가 가입하고 다시 aa가 가입하려고 하면 "이미 가입된 회원입니다."
    }

    // 로그인한 MemberBean을 저장한다.
    public static void setLoginMember(Context context, MemberBean bean) {
        if(bean != null) {
            String str = mGson.toJson(bean);
            SharedPreferences.Editor editor = getSp(context).edit();
            editor.putString("loginMemberBean", str);
            editor.commit(); // 저장
        }
    }

    // 로그인한 MemberBean을 취득한다.
    public static MemberBean getLoginMember(Context context) {
        String str = getSp(context).getString("loginMemberBean", null);
        if(str == null) return null;
        MemberBean memberBean = mGson.fromJson(str, MemberBean.class);
        return memberBean;
    }
    /* 저장하는 키 값이 다르기 때문에 겹치지 않음 */

}
