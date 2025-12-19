package org.example.controller;

import org.example.Article;
import org.example.service.ArticleService;
import org.example.util.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ArticleController {

    private Scanner sc;
    private ArticleService articleService;
    private Session session;

    public ArticleController(Scanner sc, ArticleService articleService, Session session)
    {
        this.sc = sc;
        this.articleService = articleService;
        this.session = session;

    }

    public void insert(Connection conn) throws SQLException
    {
        if(!session.isLoggedIn())
        {
            System.out.println("로그인한 사용자만 작성할 수 있습니다.");
            return;
        }
        String writer = session.getLoginMember().getName();
        int writerId = session.getLoginMember().getId();

        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = articleService.insert(conn, title, body, writer, writerId);

        System.out.println(id + "번 글이 생성되었습니다.");
    }

    public void findAll(Connection conn) throws SQLException
    {
        System.out.println("==목록==");

        List<Article> articles = articleService.findAll(conn);

        if (articles.isEmpty()) {
            System.out.println("게시글이 없습니다");
        }
        System.out.println(Ansi.PURPLE+"  번호  /   제목                 /  작성자"+Ansi.RESET);

        for (Article article : articles) {
            //System.out.printf("   %-4d /   %-10s   / %-10s\n", article.getId(), article.getTitle(), article.getWriter());
            System.out.printf(" %4d ",article.getId());
            System.out.printf("  /  "+Padding.padRight(article.getTitle(), 20));
            System.out.println("  /  "+Padding.padRight(article.getWriter(), 10));
        }
    }

    public void update(Connection conn, int id) throws SQLException
    {
        if (!session.isLoggedIn()) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        Article article = articleService.findById(conn, id);

        if (article == null) {
            System.out.println("존재하지 않는 게시글입니다.");
            return;
        }

        if (article.getWriterId() != session.getLoginMember().getId()) {
            System.out.println("본인 글만 수정할 수 있습니다.");
            return;
        }
        System.out.println("==수정==");
        System.out.print("새 제목 : ");
        String title = sc.nextLine().trim();
        System.out.print("새 내용 : ");
        String body = sc.nextLine().trim();

        articleService.update(conn, title, body, id);

        System.out.println(id + "번 글이 수정되었습니다.");
    }

    public void delete(Connection conn, int id) throws SQLException
    {
        if (!session.isLoggedIn()) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        //대충 arti.writerId == getId인지 봐야함,,,
        Article article = articleService.findById(conn, id);

        if (article == null) {
            System.out.println("존재하지 않는 게시글입니다.");
            return;
        }

        if ( article.getWriterId() != session.getLoginMember().getId()) {
            System.out.println("본인 글만 삭제할 수 있습니다.");
            return;
        }
        articleService.delete(conn, id);

        System.out.println(id + "번 글이 삭제 되었습니다.");
    }

    public void detail(Connection conn, String cmd) throws SQLException
    {

        int id = Integer.parseInt(cmd.replace("article detail ", ""));

        Article article = articleService.findById(conn, id);

        if(article == null) {
            System.out.println("게시글이 조회되지 않음. 게시글 id : " + id);
            return;
        }

        System.out.println("번호     : " + article.getId());
        System.out.println("작성자   : " + article.getWriter());
        System.out.println("작성날짜 :  " + article.getRegDate());
        System.out.println("수정날짜 : " + article.getUpdateDate());
        System.out.println("제목     : " + article.getTitle());
        System.out.println("내용     : " + article.getBody());
    }

}

class Padding {

    public static int displayWidth(String s) {

        if (s == null) {
            return 0;
        }

        int width = 0;
        for (
                int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c <= 0x007F) {              // ASCII
                width += 1;
            } else if (c >= 0xAC00 && c <= 0xD7A3) { // 한글
                width += 2;
            } else {
                width += 1; // 기타 문자
            }
        }
        return width;
    }

    public static String padRight(String s, int totalWidth) {
        int currentWidth = displayWidth(s);
        int pad = totalWidth - currentWidth;

        if (pad <= 0) {
            return s;
        }
        return s + " ".repeat(pad);
    }

    public static String padLeft(String s, int totalWidth) {
        int pad = totalWidth - displayWidth(s);
        return " ".repeat(Math.max(0, pad)) + s;
    }
}

class Ansi{

    public static final String RESET  = "\u001B[0m";

    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

        }
