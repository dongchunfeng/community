package com.nowcoder.community.util;


import java.io.BufferedReader;
import java.sql.*;


import java.io.FileReader;

import java.io.IOException;


public class JdbcStudy {

    public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]\\d{4,8}$";

    public static boolean regexMobile(String s){
        return s.matches(REGEX_MOBILE);
    }

    private String url = "jdbc:mysql://localhost:3306/test01?characterEncoding=utf-8&useSSL=false&serverTimezone=Hongkong";

    private String user = "root";

    private String password = "xdcf3233";

    BufferedReader br;

    Connection con;
    PreparedStatement ps;

    public void test(String tableName1) throws SQLException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(new FileReader("D:/" + tableName1 + ".txt"));

        String str = null;
        String s[] = new String[2];
        String s1 = br.readLine();
        String[] tableName = s1.split(",");
        String checkTable = "show tables like \"" + tableName1 + "\"";
        String createBrandDatabase = "create table "
                + tableName1
                +
                "(" + tableName[0] + " int(10) NOT NULL," + tableName[1] + " varchar(10) NOT NULL)"
                + " DEFAULT CHARSET=utf8;";
        Statement stmt = (Statement) con.createStatement();
        ResultSet resultSet = stmt.executeQuery(checkTable);
        if (resultSet.next()) {
            System.out.println("table exist!");
        } else {
            if (stmt.executeUpdate(createBrandDatabase) == 0) {
                System.out.println("create table success!");
            }
        }
        ps = con.prepareStatement("insert into " + tableName1 + " values(?,?)");

        while ((str = br.readLine()) != null) {
            s = str.split(",");

            ps.setInt(1, Integer.parseInt(s[0]));

            ps.setString(2, s[1]);


            ps.executeUpdate();

        }


        br.close();

        ps.close();

        con.close();

    }

    public static void main(String[] args) {
        JdbcStudy JS = new JdbcStudy();
        try {
            //JS.test("Classes");
            JS.test("Student_Classes");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


}

