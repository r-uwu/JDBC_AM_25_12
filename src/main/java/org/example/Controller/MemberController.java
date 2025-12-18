package org.example.Controller;

import org.example.service.MemberService;

import java.sql.Connection;
import java.util.Scanner;

public class MemberController {

    Scanner sc;
    MemberService memberService;

    public MemberController(Scanner sc, MemberService memberService) {
        this.sc = sc;
        this.memberService = memberService;
    }

    public void join(Connection conn)
    {
        String loginId = null;
        String loginPw = null;
        String loginPwConfirm = null;
        String name = null;

        //이 아래로 멤버 컨트롤러로 이송예정

        System.out.println("==회원가입==");
        while (true) {
            System.out.print("로그인 아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.isEmpty() || loginId.contains(" ")) {
                System.out.println("아이디 똑바로 써");
                continue;
            }

            while (true) {
                System.out.print("비밀번호 : ");
                loginPw = sc.nextLine().trim();

                if (loginPw.length() == 0 || loginPw.contains(" ")) {
                    System.out.println("비밀번호 똑바로 써");
                    continue;
                }

                boolean loginCheckPw = true;

                while (true) {
                    System.out.print("비번 확인 : ");
                    loginPwConfirm = sc.nextLine().trim();

                    if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                        System.out.println("비밀번호 확인 똑바로 써");
                        continue;
                    }

                    if (loginPw.equals(loginPwConfirm) == false) {
                        System.out.println("비번이 일치하지 않아");
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
                    System.out.println("이름 똑바로 써");
                    continue;
                }
                break;
            }
            break;
        }

        int id = memberService.join(conn, loginId, loginPw, name);

        //위에까지 멤버 컨트롤러로
        System.out.println(id + "번 회원 가입함");

    }

}
