package org.example.service;

import org.example.Member;
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

        else return memberDao.join(conn, loginId, loginPw, name);
    }


    public Member login(Connection conn, String loginId, String loginPw) {

        Member member = memberDao.findByLoginId(conn, loginId);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        if (!member.getUserPw().equals(loginPw)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    public Member memberProfile(Connection conn, String loginId)
    {
        Member member = memberDao.findByLoginId(conn, loginId);

        return member;
    }

}
