package org.example;

import org.example.controller.ArticleController;
import org.example.controller.MemberController;
import org.example.service.ArticleService;
import org.example.service.MemberService;
import org.example.util.Session;
import org.example.util.Ansi;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    Scanner sc = new Scanner(System.in);
    Session session = new Session();

    MemberService memberService = new MemberService();
    MemberController memberController = new MemberController(sc, memberService, session);

    ArticleService articleService = new ArticleService();
    ArticleController articleController = new ArticleController(sc, articleService, session);


    private boolean isLoggedIn = false;

    public void run() {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner sc = new Scanner(System.in);
        int lastArticleId = 0;
        List<Article> articles = new ArrayList<>();

        System.out.println("==프로그램 시작==");

        helper();

        while (true) {

            System.out.println();
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }

            Connection conn = null;
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/JDBC_AM_25_12?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = doAction(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("==프로그램 종료==");
                    sc.close();
                    break;
                }

            } catch (SQLException e) {
                System.out.println("에러 1 : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("==프로그램 종료==");
        sc.close();
    }

    private int doAction(Connection conn, Scanner sc, String cmd) throws SQLException {

        System.out.println();

        if (cmd.equals("login")) {
            memberController.login(conn);

        }
        else if(cmd.equals("logout")) {
            memberController.logout();
        }
        else if(cmd.equals("member profile")) {
            memberController.showProfile(conn);
        }

        else if (cmd.equals("member join")) {

            memberController.join(conn);

        } else if (cmd.equals("article write")) {

            articleController.insert(conn);
        }


        else if (cmd.equals("article list all")) {

            articleController.findAll(conn);

        }

        else if (cmd.startsWith("article list")) {

            int findPage = 0;

            try {
                findPage = Integer.parseInt(cmd.replace("article list ", ""));
            } catch (Exception e) {

            }
            articleController.findList(conn, findPage);
        }

        else if (cmd.startsWith("article modify")) {
            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해주세요.");
            }
            articleController.update(conn, id);
        }

        else if (cmd.startsWith("article delete ")) {

            int id = Integer.parseInt(cmd.replace("article delete ", ""));

            articleController.delete(conn, id);
        }

        else if (cmd.startsWith("article detail ")) {

            articleController.detail(conn, cmd);
        }

        else if(cmd.startsWith("도움")||cmd.startsWith("help")) {
            helper();
        }

        else
            System.out.println("명령어가 잘못 입력되었어요. 명령어를 볼려면 [help] 입력");

        return 1;
        //이 위에있는 리턴 어떢함?
    }

    void helper()
    {
        System.out.println(Ansi.PURPLE + "명령어 모음을 알려드릴게요.\n");

        System.out.println(Ansi.BLUE+"회원가입 [ member join ]");
        System.out.println("내 프로필 조회 [ member profile ]\n");

        System.out.println("로그인 [ login ]");
        System.out.println("로그아웃 [ logout ]\n");

        System.out.println("게시글 작성 [ article write ]");
        System.out.println("게시글 목록 [ article list (공백 혹은 페이지 번호) ]");
        System.out.println("게시글 모두 조회 [ article list all ]");
        System.out.println("게시글 수정 [ article modify (게시글 번호) ]");
        System.out.println("게시글 삭제 [ article delete (게시글 번호) ]"+Ansi.RESET);
    }
}
