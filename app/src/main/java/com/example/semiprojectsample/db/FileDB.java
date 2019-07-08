package com.example.semiprojectsample.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FileDB {
    private static final String FILE_DB = "FileDB";
    public final static String TBL_MEMO = "MEMO";
    private static Gson mGson = new Gson();

    //private static List<MemoBean> memos = null;

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
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("memberList", listStr);
        editor.commit(); // 커밋을 해 줘야 저장됨!!
    }

    // 기존 멤버 교체 (메모 수정할 때 쓰면 됨)
    public static void setMember(Context context, MemberBean memberBean) {
        // 전체 멤버 리스트를 취득한다.
        List<MemberBean> memberList = getMemberList(context);
        if(memberList.size() == 0) return;

        for(int i=0; i<memberList.size(); i++) { // for each 는 리스트 돌면서 bean만 받아 옴
            MemberBean bean = memberList.get(i);
            if(TextUtils.equals(bean.memId, memberBean.memId)) {
                // 같은 멤버ID를 찾았다.
                memberList.set(i, memberBean);
                break;
            }
        } // end for

        // 새롭게 업데이트 된 리스트를 저장한다.
        String jsonStr = mGson.toJson(memberList);

        // 멤버 리스트를 저장한다.
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("memberList", jsonStr);
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


/*    // memo 선두에 추가
    public static void addMemo(Context context, MemoBean memo, MemberBean member) { // i객체를 받아서 memos에 추가
        member.memoLIst.add(0, memo); // (memo) : 리스트의 뒤에 붙음.. (0, memo) : 최근 것이 위에 뜸
    }

    // memo 획득 (index에 해당하는)
    public static MemberBean getMemo(Context context, int index, MemberBean member) {
        return member.memoLIst.get(index);
    }

    // memo 변경 (index번 메모의 내용 변경)
    public static void setMemo(Context context, int index, MemoBean memo, MemberBean member) {
        member.memoLIst.set(index, memo); // 덮어 씌움
    }

    // memo 삭제
    public static void removeMemo(Context context, int index, MemberBean member) {
        member.memoLIst.remove(index);
    }

    // memo를 SharedPreferences에 저장
    public static void saveMemos(Context context, MemberBean member) {
        String memoString = new GsonBuilder().serializeNulls().create().toJson(member.memoLIst);

        // 저장
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString(TBL_MEMO, memoString);
        editor.commit();
    }

    // SharedPreferences에서 memos 획득
    public static MemoBean loadMemos(Context context, MemberBean member) {
        String memoString = getSp(context).getString(TBL_MEMO, "");
        MemoBean memos = null;

        if(!memoString.isEmpty()) {
            MemoBean[] memoArray = new Gson().fromJson(memoString, MemoBean[].class);

            memos = new ArrayList<>(Arrays.asList(memoArray));
        }
        return memos;
    }*/

    // 전체 메모 리스트를 가져오고, 저장 - 메인 화면에서 씀
    // 메모 리스트에서 특정 메모를 바꾸는 기능이 있어야 함 - 고유번호!

    // 메모를 추가하는 메서드
    public static void addMemo(Context context, String memId, MemoBean memoBean){
        // 해당되는 유저 찾기
        MemberBean findMember = getFindMember(context, memId);
        if(findMember == null) return;
        // findMember로부터 메모 리스트 뺌
        List<MemoBean> memoList = findMember.memoList;
        if(memoList == null) {
            memoList = new ArrayList<>();
        }

        // 고유 메모 ID를 생성해준다.
        memoBean.memoId = System.currentTimeMillis(); // 호출되는 시점의 시간. 1/1000 단위
        memoList.add(0, memoBean);
        findMember.memoList = memoList;
        // 저장
        setMember(context, findMember);
    }

    // 기존 메모 교체
    public static void setMemo(Context context, String memId, MemoBean memoBean) {
        // TODO
    }

    // 메모 삭제
    public static void delMemo(Context context, String memId, long memoId) {
        // TODO
        List<MemoBean> memoList = getMemoList(context, memId);

        for(int i=0; i<memoList.size(); i++) {
            MemoBean memoBean = memoList.get(i);
            if (memoBean.memoId == memoId) {
                memoList.remove(i);
                break;
            }
        }
        MemberBean findMember = getFindMember(context, memId);
        if (findMember == null) return;
        findMember.memoList = memoList;
        setMember(context, findMember);
    }

    // 메모 리스트 취득
    public static List<MemoBean> getMemoList(Context context, String memId) {
        MemberBean memberBean = getFindMember(context, memId);
        if(memberBean == null) return null;

        if(memberBean.memoList == null) {
            return new ArrayList<>();
        }
        else {
            return memberBean.memoList;
        }
    }

    // findMemo도 필요.. 수정하러 들어갔을 때 메모가 몇 번짼지?
}
