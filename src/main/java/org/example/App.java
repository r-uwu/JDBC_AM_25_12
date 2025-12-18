package org.example;

import org.example.Controller.ArticleController;
import org.example.Controller.MemberController;
import org.example.dao.ArticleDao;
import org.example.service.ArticleService;
import org.example.service.MemberService;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    Scanner sc = new Scanner(System.in);
    MemberService memberService = new MemberService();
    MemberController memberController = new MemberController(sc, memberService);

    ArticleService articleService = new ArticleService();
    ArticleController articleController = new ArticleController(sc, articleService);

    public void run() {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner sc = new Scanner(System.in);
        int lastArticleId = 0;
        List<Article> articles = new ArrayList<>();


        System.out.println("==프로그램 시작==");


        while (true) {
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

        if (cmd.equals("member join")) {

            memberController.join(conn);

        } else if (cmd.equals("article write")) {

            articleController.insert(conn);
        }


        else if (cmd.equals("article list")) {
            System.out.println("==목록==");

            List<Article> articles = articleService.findAll(conn);

            if (articles.isEmpty()) {
                System.out.println("게시글이 없습니다");
                return 0;
            }
            System.out.println("  번호  /   제목  ");

            for (Article article : articles) {
                System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());

            }
        }



        else if (cmd.startsWith("article modify")) {

            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
            }

            articleController.update(conn, id);
        } else if (cmd.startsWith("article delete ")) {

            int id = Integer.parseInt(cmd.replace("article delete ", ""));

            articleController.delete(conn, id);
        }

        /*
        else if (cmd.startsWith("article detail ")) {

            int id = Integer.parseInt(cmd.replace("article detail ", ""));


            System.out.println("번호 : " + article.getId());
            System.out.println("작성날짜 :  " + article.getRegDate());
            System.out.println("수정날짜 : " + article.getUpdateDate());
            System.out.println("제목 : " + article.getTitle());
            System.out.println("내용 : " + article.getBody());



        }
         */
        return 1;
    }
}
