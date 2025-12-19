package org.example.dao;

import org.example.Article;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleDao {

    public int insert(Connection conn, String title, String body, String writer, int writerId) throws SQLException {

        SecSql sql = new SecSql();
        sql.append("INSERT INTO article");
        sql.append("SET");
        sql.append("writer = ?,", writer);
        sql.append("writerId = ?,", writerId);
        sql.append("regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("title = ?,", title);
        sql.append("body = ?", body);

        return DBUtil.insert(conn, sql);
    }

    public Article findById(Connection conn, int id) {
        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article");
        sql.append("WHERE id = ?", id);

        Map<String, Object> row = DBUtil.selectRow(conn, sql);

        if (row.isEmpty()) return null;

        return new Article(row);
    }

    public List<Article> findAll(Connection conn) {

        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article");
        sql.append("ORDER BY id Desc");

        List<Map<String, Object>> rows = DBUtil.selectRows(conn, sql);
        List<Article> articles = new ArrayList<Article>();
        for (Map<String, Object> row : rows) {
            articles.add(new Article(row));
        }

        return articles;
    }

    public int findLastPage(Connection conn, int pageSize)
    {
        SecSql findLastPage = new SecSql();
        findLastPage.append("SELECT Count(*) FROM article");
        int lastPage = DBUtil.selectRowIntValue(conn, findLastPage);
        lastPage = (lastPage / pageSize) + 1;

        return lastPage;
    }

    public List<Article> findPage(Connection conn, int pageSize, int findPage) { //찾는 페이지 번호 없으면 1페이지 보여주기

        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article");
        sql.append("ORDER BY id Desc");
        sql.append("LIMIT ? OFFSET ?", pageSize, findPage);

        List<Map<String, Object>> rows = DBUtil.selectRows(conn, sql);
        List<Article> articles = new ArrayList<Article>();
        for (Map<String, Object> row : rows) {
            articles.add(new Article(row));
        }

        return articles;
    }

    public int update(Connection conn, String title, String body, int id) {

        SecSql sql = new SecSql();
        sql.append("UPDATE article SET ");
        sql.append("updateDate = NOW(), ");
        sql.append("title = ?,", title);
        sql.append("body = ?", body);
        sql.append("WHERE id = ?", id);

        return DBUtil.update(conn, sql);
    }

    public int delete(Connection conn, int id) {

        SecSql sql = new SecSql();
        sql.append("DELETE FROM article");
        sql.append("WHERE  id = ?", id);

        return DBUtil.update(conn, sql);
    }

    public List<Article> searchByTitle(Connection conn, String searchKeyword) {

        SecSql sql = new SecSql();
        //sql.append("SELECT * FROM article WHERE title LIKE '%s'", title);
        sql.append("SELECT * FROM article WHERE title LIKE ? ", "%"+searchKeyword+"%");

        List<Map<String, Object>> rows = DBUtil.selectRows(conn, sql);
        List<Article> articles = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            articles.add(new Article(row));
        }
        return articles;


    }

}
