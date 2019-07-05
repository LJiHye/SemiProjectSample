package com.example.semiprojectsample.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.db.FileDB;

import java.io.File;

public class FragmentMember extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        TextView txtMemId = view.findViewById(R.id.txtMemId);
        TextView txtMemPw = view.findViewById(R.id.txtMemPw);
        TextView txtMemName = view.findViewById(R.id.txtMemName);
        TextView txtMemDate = view.findViewById(R.id.txtMemDate);

        // 파일DB에서 가져온다. 근데 누가 로그인한지 모름..
        // FileDB 에 get,setLoginMember 메소드 추가
        MemberBean memberBean = FileDB.getLoginMember(getActivity());

        imgProfile.setImageURI(Uri.fromFile(new File(memberBean.photoPath)));
        txtMemId.setText(memberBean.memId);
        txtMemName.setText(memberBean.memName);
        txtMemPw.setText(memberBean.memPw);
        txtMemDate.setText(memberBean.memRegDate);

        return view;
    }
}
