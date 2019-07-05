package com.example.semiprojectsample.bean;

import java.io.Serializable;
import java.util.List;

// 회원정보 Bean
public class MemberBean implements Serializable {
    public String photoPath;
    public String memId;
    public String memName;
    public String memPw;
    public String memRegDate;
    public List<MemberBean> memoLIst;

}
