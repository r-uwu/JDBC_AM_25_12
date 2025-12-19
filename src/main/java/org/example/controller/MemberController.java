package org.example.controller;

import org.example.Member;
import org.example.service.MemberService;
import org.example.util.Session;

import java.sql.Connection;
import java.util.Scanner;

public class MemberController {

    private Scanner sc;
    private MemberService memberService;
    private Session session;

    public MemberController(Scanner sc, MemberService memberService, Session session) {
        this.sc = sc;
        this.memberService = memberService;
        this.session = session;
    }

    public void login(Connection conn) {

        if (session.isLoggedIn()) {
            System.out.println("이미 로그인 상태입니다.");
            return;
        }

        int tryLoggedIn = 0;

        while(true) {
            if (tryLoggedIn >= 3) {
                System.out.println("3회 이상 로그인 오류. 잠시 후 다시 시도해주세요.");
                break;
            }

            tryLoggedIn++;
            System.out.println("== 로그인 ==");
            System.out.print("로그인 아이디 : ");
            String loginId = sc.nextLine().trim();
            System.out.print("비밀번호 : ");
            String loginPw = sc.nextLine().trim();

            try {
                Member member = memberService.login(conn, loginId, loginPw);

                session.login(member);
                System.out.println(member.getName() + "님 로그인 되었습니다.");

                break;

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void logout() {

        if (!session.isLoggedIn()) {
            System.out.println("이미 로그아웃 상태입니다.");
            return;
        }

        String name = session.getLoginMember().getName();
        session.logout();

        System.out.println(name + "님 로그아웃 되었습니다.");
    }

    public void join(Connection conn)
    {
        String loginId = null;
        String loginPw = null;
        String loginPwConfirm = null;
        String name = null;

        System.out.println("==회원가입==");
        while (true) {
            System.out.print("로그인 아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.isEmpty() || loginId.contains(" ")) {
                System.out.println("아이디가 입력되지 않았습니다.");
                continue;
            }

            while (true) {
                System.out.print("비밀번호 : ");
                loginPw = sc.nextLine().trim();

                if (loginPw.length() == 0 || loginPw.contains(" ")) {
                    System.out.println("비밀번호가 입력되지 않았습니다.");
                    continue;
                }

                boolean loginCheckPw = true;

                while (true) {
                    System.out.print("비번 확인 : ");
                    loginPwConfirm = sc.nextLine().trim();

                    if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                        System.out.println("비밀번호가 입력되지 않았습니다.");
                        continue;
                    }

                    if (loginPw.equals(loginPwConfirm) == false) {
                        System.out.println("처음 비밀번호와 일치하지 않습니다.");
                        loginCheckPw = false;
                    }
                    break;
                }
                if (loginCheckPw) {
                    break;
                }
            }

            while (true) {
                System.out.print("이름 : ");
                name = sc.nextLine().trim();

                if (name.length() == 0 || name.contains(" ")) {
                    System.out.println("이름을 작성하지 않았어요.");
                    continue;
                }
                break;
            }
            break;
        }

        int id = memberService.join(conn, loginId, loginPw, name);

        System.out.println(id + "번 회원 가입함");
    }

    public void showProfile(Connection conn)
    {
        if (!session.isLoggedIn()) {
            System.out.println("로그아웃 상태입니다. 로그인하고 시도해주세요.");
            return;
        }

        Member member = memberService.memberProfile(conn, session.getLoginMember().getUserId());

        System.out.println("== 나의 프로필 ==");
        System.out.println("ID       : " + member.getUserId());
        System.out.println("name     : " + member.getName());
        System.out.println("regDate  : "+member.getRegDate());

    }
}
