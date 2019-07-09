package com.example.semiprojectsample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.bean.MemoBean;

public class FragmentModifyWrite extends Fragment {
    private EditText medtMemo;
    private Intent intent;
    private MemoBean memoBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_write, container, false);

        intent = getActivity().getIntent();

        memoBean = (MemoBean) intent.getSerializableExtra("MEMOBEAN");

        medtMemo = view.findViewById(R.id.edtModifyMemo);
        medtMemo.setText(memoBean.memo);

        return view;
    }
}
