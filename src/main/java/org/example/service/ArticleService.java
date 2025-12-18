package org.example.service;

import org.example.Article;
import org.example.dao.ArticleDao;
import org.example.util.SecSql;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ArticleService {

    ArticleDao articleDao;

    public ArticleService(ArticleDao articleDao) {

        this.articleDao = articleDao;
    }

    public int write(Connection conn, String title, String body) {

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




}
