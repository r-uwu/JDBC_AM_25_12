package org.example;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        System.out.println("==프로그램 시작==");

        Scanner sc = new Scanner(System.in);

        int lastArticleId = 0;

        List<Article> articles = new ArrayList<>();

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }
            if (cmd.startsWith("article write")) {
                System.out.println("==글쓰기==");

                int id = lastArticleId + 1;
                System.out.print("제목 : ");
                String title = sc.nextLine().trim();
                System.out.print("내용 : ");
                String body = sc.nextLine().trim();

                String sql = "INSERT INTO article (regDate, updateDate, title, `body`) VALUES(NOW(), NOW(), ?, ?)";

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC_AM_25_12?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

                    conn = DriverManager.getConnection(url, "root", "");
                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, title);
                    pstmt.setString(2, body);

                    int rows = pstmt.executeUpdate();
                    System.out.println(rows + "개 글이 추가되었습니다.");
                } catch (SQLException e) {
                    System.out.println("DB 입력 중 오류 발생! " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) {}
                    try { if (conn != null) conn.close(); } catch (Exception ignored) {}
                }

                //Article article = new Article(id, title, body);
                //articles.add(article);

                lastArticleId++;
                //System.out.println(article);

            } else if (cmd.equals("article list")) {

                articles.clear();

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC_AM_25_12?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");

                    String sql = "SELECT *";
                    sql += " FROM article";
                    sql += " ORDER BY id DESC";

                    System.out.println(sql);

                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String regDate = rs.getString("regDate");
                        String updateDate = rs.getString("updateDate");
                        String title = rs.getString("title");
                        String body = rs.getString("body");
                        Article article = new Article(id, regDate, updateDate, title, body);
                        articles.add(article);
                    }

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try { if (pstmt != null && !pstmt.isClosed()) { pstmt.close();}
                    } catch (SQLException e) { e.printStackTrace(); }
                    try { if (conn != null && !conn.isClosed()) { conn.close();}
                    } catch (SQLException e) { e.printStackTrace(); }
                }


                System.out.println("==목록==");
                if (articles.size() == 0) {
                    System.out.println("게시글 없음");
                    continue;
                }
                System.out.println("   번호    /    제목");
                for (Article article : articles) {
                    System.out.printf("   %d    /   %s\n", article.getId(), article.getTitle());
                }
            }
            else if(cmd.startsWith("article modify "))
            {

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC_AM_25_12?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");

                    int modifyId = Integer.parseInt(cmd.replace("article modify ", ""));

                    System.out.print("제목 : ");
                    String title = sc.nextLine().trim();
                    System.out.print("내용 : ");
                    String body = sc.nextLine().trim();

                    String sql = "UPDATE article ";
                    sql += "SET updateDate = NOW() ";
                    sql += ", title = '" + title+"' ";
                    sql += ", body = '" + body + "' ";
                    sql += " WHERE id = " + modifyId + ";";

                    pstmt = conn.prepareStatement(sql);
                    int rows = pstmt.executeUpdate();
                }
                catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try { if (pstmt != null && !pstmt.isClosed()) { pstmt.close();}
                    } catch (SQLException e) { e.printStackTrace(); }
                    try { if (conn != null && !conn.isClosed()) { conn.close();}
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            }
        }

        System.out.println("==프로그램 종료==");
        sc.close();
    }
}