package org.example.util;

import org.example.Member;

//로그인 관리할거임
public class Session {

    private Member loginMember;

    public boolean isLoggedIn() {
        return loginMember != null;
    }

    public void login(Member member) {
        this.loginMember = member;
    }

    public void logout() {
        this.loginMember = null;
    }

    public Member getLoginMember() {
        return loginMember;
    }
}
