package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    int getTodayNewMember(String today);


    int getTotalMember();
    /*。。。*/
    int getThisWeekAndMonthNewMember(String weekMonday);


    Integer findMemberCountBeforeDate(String lastDayOfMonth);
}
