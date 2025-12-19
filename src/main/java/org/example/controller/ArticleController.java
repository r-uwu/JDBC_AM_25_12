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
        System.out.println("  번호  /   제목   ");

        for (Article article : articles) {
            System.out.printf("   %-4d /   %-10s   \n", article.getId(), article.getTitle());
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
