package org.example.Controller;

import org.example.service.ArticleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ArticleController {

    private Scanner sc;
    private ArticleService articleService;

    public ArticleController(Scanner sc, ArticleService articleService)
    {
        this.sc = sc;
        this.articleService = articleService;
    }

    public void insert(Connection conn) throws SQLException
    {
        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = articleService.insert(conn, title, body);

        System.out.println(id + "번 글이 생성되었습니다.");
    }

    public void update(Connection conn, int id) throws SQLException
    {
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
        articleService.delete(conn, id);

        System.out.println(id + "번 글이 삭제 되었습니다.");
    }
}
