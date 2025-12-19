package org.example.controller;

import org.example.Article;
import org.example.service.ArticleService;
import org.example.util.Ansi;
import org.example.util.Padding;
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
            System.out.println("로그인한 사용자만 작성할 수 있습니다. [login]");
            return;
        }
        String writer = session.getLoginMember().getName();
        int writerId = session.getLoginMember().getId();

        System.out.println(Ansi.PURPLE+"글쓰기 ==>"+Ansi.RESET);
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = articleService.insert(conn, title, body, writer, writerId);

        System.out.println(id + "번 글이 생성되었습니다.");
    }

    //게시글 색인 전체
    public void findAll(Connection conn) throws SQLException
    {
        System.out.println(Ansi.PURPLE+ "목록 =="+Ansi.RESET);

        List<Article> articles = articleService.findAll(conn);

        if (articles.isEmpty()) {
            System.out.println("게시글이 없습니다");
        }
        System.out.println(Ansi.PURPLE+"  번호  /   제목                 /  작성자"+Ansi.RESET);

        for (Article article : articles) {
            //System.out.printf("   %-4d /   %-10s   / %-10s\n", article.getId(), article.getTitle(), article.getWriter());
            System.out.printf(" %4d ",article.getId());
            System.out.printf("  /  "+ Padding.padRight(article.getTitle(), 20));
            System.out.println("  /  "+Padding.padRight(article.getWriter(), 10));
        }
    }

    //게시글 목록(페이지 단위)
    public void findList(Connection conn, int findPage) throws SQLException {
        System.out.println(Ansi.PURPLE+ "목록 ==>" + Ansi.RESET);
        //findPage = (findPage - 1) * 5;
        while (true) {
            List<Article> articles = articleService.findPage(conn, findPage);

            if (articles.isEmpty()) {
                System.out.println("게시글이 없습니다");
            }

            else {
                System.out.println(Ansi.YELLOW + "  번호  /   제목                 /  작성자" + Ansi.RESET);

                for (Article article : articles) {
                    //System.out.printf("   %-4d /   %-10s   / %-10s\n", article.getId(), article.getTitle(), article.getWriter());
                    System.out.printf(" %4d ", article.getId());
                    System.out.printf("  /  " + Padding.padRight(article.getTitle(), 20));
                    System.out.println("  /  " + Padding.padRight(article.getWriter(), 10));
                }

                System.out.printf("\n현재 페이지 (%d 중 %d)\n",findPage, articleService.findLastPage(conn));
            }

            while(true) {
                System.out.println("이전 [prev] / 다음 [next] / 페이지 넘버 이동 [페이지 넘버] / 종료 [undo]\n");
                String cmd = sc.nextLine().trim();
                if (cmd.equals("undo"))
                    return;
                else if (cmd.equals("next")) {
                    findPage ++;
                    break;
                } else if (cmd.equals("prev")) {
                    if (findPage <= 1)
                        System.out.println("이전 페이지가 존재하지 않습니다.");
                    else{findPage --; break;}
                } else {
                    try {

                        if(Integer.parseInt((cmd)) <= 0) {
                            System.out.println("1 이상의 페이지를 입력해주세요.");
                        }
                        else{
                            findPage = Integer.parseInt(cmd);
                            break;
                        }

                    } catch (NumberFormatException ex) {
                        System.out.println(" next, prev, 페이지 넘버만 입력 가능합니다.");
                    }
                }
            }
        }
    }

    //수정
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
        System.out.println(Ansi.PURPLE + "수정 ==>"+Ansi.RESET);
        System.out.print("새 제목 : ");
        String title = sc.nextLine().trim();
        System.out.print("새 내용 : ");
        String body = sc.nextLine().trim();

        articleService.update(conn, title, body, id);

        System.out.println(id + "번 글이 수정되었습니다.");
    }

    //삭제
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

        System.out.println(Ansi.YELLOW+ "번호     : " + article.getId());
        System.out.println("작성자   : " + article.getWriter());
        System.out.println("작성날짜 :  " + article.getRegDate());
        System.out.println("수정날짜 : " + article.getUpdateDate());
        System.out.println("제목     : " + article.getTitle());
        System.out.println("내용     : " + article.getBody()+Ansi.RESET);
    }

    public void search(Connection conn, String cmd) throws SQLException
    {
        String searchKeyword = cmd.replace("article search ", "");

        if (searchKeyword.isBlank())
        {
            System.out.println("검색할 키워드가 입력되지 않았습니다.");
            return;
        }
        List<Article> articles = articleService.searchByTitle(conn, searchKeyword);

        if (articles.isEmpty())
        {
            System.out.println("검색 결과가 없습니다. 검색 키워드 : "+searchKeyword);
            return;
        }

        System.out.println(Ansi.PURPLE+ "목록 - 검색어 ["+searchKeyword+"] =="+Ansi.RESET);
        System.out.println(Ansi.YELLOW + "  번호  /   제목                 /  작성자" + Ansi.RESET);
        for (Article article : articles) {
            System.out.printf(" %4d ", article.getId());
            System.out.printf("  /  " + Padding.padRight(article.getTitle(), 20));
            System.out.println("  /  " + Padding.padRight(article.getWriter(), 10));
        }
    }
}

