package org.example.service;

import org.example.Article;
import org.example.dao.ArticleDao;

import java.sql.Connection;
import java.util.List;

public class ArticleService {

    private ArticleDao articleDao;

    public ArticleService() {

        articleDao = new ArticleDao();
    }

    public int insert(Connection conn, String title, String body) {

        return articleDao.insert(conn, title, body);
    }

    public List<Article> findAll(Connection conn) {

        return articleDao.findAll(conn);

    }

    public void update(Connection conn, String title, String body, int id)
    {
        int isUpdate = articleDao.update(conn, title, body, id);

        if(isUpdate == 0)
            throw new RuntimeException("글이 존재하지 않음");

    }
    public void delete(Connection conn, int id)
    {
        int isDelete = articleDao.delete(conn, id);

        if(isDelete == 0)
            throw new RuntimeException("글이 삭제되지 않았음");

    }

    public Article findById(Connection conn, int id)
    {

        Article article = articleDao.findById(conn, id);

        if(article == null) {
            throw new RuntimeException("글이 조회되지 않음");

            //throw new ArticleNotFoundException(id);
            //혹은
            //Optional (삼항연산자로 db에서부터 비었는지 확인해서 없으면 Optional.empty 반환 있으면 db값꺼내서반환하는거인듯?
            //근데 optional 메서드 어디서 생성하는지
            // 이거 귀찮아보이는데 하지말까
        }

        else return article;

    }
}
