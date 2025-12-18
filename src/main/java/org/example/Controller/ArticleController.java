package org.example.Controller;

import org.example.dao.ArticleDao;

public class ArticleController {

    public write()
    {
        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = ArticleDao.insert(conn, title, body);

        System.out.println(id + "번 글이 생성되었습니다.");
        //System.out.println(sql.toString());
    }
}
