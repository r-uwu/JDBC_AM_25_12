package org.example.service;

import org.example.dao.MemberDao;

import java.sql.Connection;

public class MemberService {
    private MemberDao memberDao;

    public MemberService()
    {
        memberDao = new MemberDao();
    }

    public int join(Connection conn, String loginId, String loginPw, String name){

        if (memberDao.isLoginIdDup(conn, loginId)){
            System.out.println("이미 로그인이 되어있음");
            return -1;
        }

        else return memberDao.insert(conn, loginId, loginPw, name);
    }

}
