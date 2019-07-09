package com.example.semiprojectsample.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiprojectsample.R;
import com.example.semiprojectsample.activity.ModifyMemoActivity;
import com.example.semiprojectsample.activity.NewMemoActivity;
import com.example.semiprojectsample.bean.MemberBean;
import com.example.semiprojectsample.bean.MemoBean;
import com.example.semiprojectsample.db.FileDB;

import java.util.ArrayList;
import java.util.List;

public class FragmentMemo extends Fragment {
    public static final int SAVE = 1001;
    public static final int MODIFY = 1002;
    public static final int DETAIL = 1003;
    public static final int REMOVEALL = 1004;
    public ListView mLstMemo; // MainActivity에서 접근할 수 있어야 하므로 public
/*
    List<MemoBean> memo = new ArrayList<>();;
*/
    ListAdapter adapter;
    MemberBean memberBean;

    Button btnModify, btnDetail, btnRemove;
    List<MemoBean> memos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        mLstMemo = view.findViewById(R.id.lstMemo);
        memberBean = FileDB.getLoginMember(getActivity());
        memos = FileDB.getMemoList(getActivity(), memberBean.memId);
        adapter = new ListAdapter(getActivity(), memos);
        mLstMemo.setAdapter(adapter);

        view.findViewById(R.id.btnNewMemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 새 메모 화면으로 이동
                Intent i = new Intent(getActivity(), NewMemoActivity.class); // 컨텍스트를 안 가지고 있기 때문
                startActivityForResult(i, SAVE);
            }
        });

        view.findViewById(R.id.btnRemoveAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메모 전체 삭제
                FileDB.delAllMemo(getActivity(), memberBean.memId);

                /*Intent i = new Intent(getActivity(), FragmentMemo.class); // 컨텍스트를 안 가지고 있기 때문
                startActivityForResult(i, REMOVEALL);*/

                adapter.setItems(new ArrayList<MemoBean>());
                adapter.notifyDataSetChanged();
            }
        });
    return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SAVE) {
            memos = FileDB.getMemoList(getContext(), memberBean.memId);
            adapter.setItems(memos);
            adapter.notifyDataSetChanged();
        }

        if(requestCode == DETAIL) {
            memos = FileDB.getMemoList(getContext(), memberBean.memId);
            adapter.setItems(memos);
            adapter.notifyDataSetChanged();
        }
        if(requestCode == MODIFY) {
            memos = FileDB.getMemoList(getContext(), memberBean.memId);
            adapter.setItems(memos);
            adapter.notifyDataSetChanged();
        }
        /*if(requestCode == REMOVEALL) {
            memos = FileDB.getMemoList(getContext(), memberBean.memId);
            adapter.setItems(memos);
            adapter.notifyDataSetChanged();
        }*/
    }

    class ListAdapter extends BaseAdapter {
        List<MemoBean> memoList;
        Context mContext;
        LayoutInflater inflater;

        public ListAdapter(Context context, List<MemoBean> memo) {
            this.memoList = memo;
            this.mContext = context;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<MemoBean> memo) {
            this.memoList = memo;
        }

        @Override
        public int getCount() {
            //return memos.size();
            return memoList.size();
        }

        @Override
        public Object getItem(int i) {
            return memoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.view_memo, null);

            ImageView memoImg = view.findViewById(R.id.imgMemo);
            TextView txtvMemo = view.findViewById(R.id.txtMemo);
            TextView txtvDate = view.findViewById(R.id.txtDate);

            memoList = FileDB.getMemoList(getActivity(), memberBean.memId);
            final MemoBean memoBean = memoList.get(i);

                if(memoBean.memoPicPath != null) {
                    memoImg.setImageURI(Uri.parse(memoBean.memoPicPath));
                } else
                    memoImg.setImageResource(R.drawable.ic_launcher_background);

                txtvMemo.setText(memoBean.memo);
                txtvDate.setText(memoBean.memoDate);


            btnModify = view.findViewById(R.id.btnModify);
            btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "수정..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ModifyMemoActivity.class);
                    intent.putExtra("MEMOBEAN", memoBean);
                    startActivityForResult(intent, MODIFY);
                }
            });
            btnDetail = view.findViewById(R.id.btnDetail);
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "상세..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ModifyMemoActivity.class);
                    intent.putExtra("MEMOBEAN", memoBean);
                    startActivityForResult(intent, DETAIL);
                }
            });
            btnRemove = view.findViewById(R.id.btnRemove);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(), "삭제..", Toast.LENGTH_SHORT).show();
                    FileDB.delMemo(getActivity(), memberBean.memId, memoBean.memoId);
                    Toast.makeText(getActivity(), "삭제", Toast.LENGTH_SHORT).show();
                    memoList = FileDB.getMemoList(getActivity(), memberBean.memId);
                    adapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
