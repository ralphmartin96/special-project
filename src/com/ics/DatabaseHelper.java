package com.ics;

import java.sql.*;

class DatabaseHelper {

    private static final String DATABASE_NAME = "ICS-eLibrary.db";
    private static final String TABLE_PROJECTS = "special_projects";

    DatabaseHelper() {
        initialize();
    }

    private static void initialize(){
        createNewDatabase();
        createTableProjects();
    }

    private Connection connect() {

        String url = "jdbc:sqlite:C://sqlite/java/connect/" + DATABASE_NAME;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void createNewDatabase() {

        String url = "jdbc:sqlite:C:/sqlite/java/connect/" + DATABASE_NAME;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTableProjects() {

        String url = "jdbc:sqlite:C://sqlite//java/connect/" + DATABASE_NAME;

        String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE_PROJECTS+ " (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    title text NOT NULL,\n"
                + "    authors text NOT NULL,\n"
                + "    abstract text NOT NULL,\n"
                + "    adviser text,\n"
                + "    index_terms text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            System.out.println("Successfully create table "+TABLE_PROJECTS);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void insertIntoProjects(OriginalSP sp) {
        String sql = "INSERT INTO " +TABLE_PROJECTS+" (title,authors,abstract,adviser, index_terms) VALUES(?,?,?,?,?)";

        StringBuilder authors = new StringBuilder();
        StringBuilder index_terms = new StringBuilder();

        for(String author : sp.getAuthors())
            authors.append(author).append(";");

        for(String indexTerms : sp.getIndexTerms())
            index_terms.append(indexTerms).append(", ");

        try (Connection conn = this.connect();

        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sp.getTitle());
            pstmt.setString(2, authors.toString());
            pstmt.setString(3, sp.getAbstractText());
            pstmt.setString(4, sp.getAdviser());
            pstmt.setString(5, index_terms.toString());

            pstmt.executeUpdate();

            System.out.println("Successfully inserted into table "+TABLE_PROJECTS);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void getAllDataFromProjects(){
        String sql = "SELECT id, title, authors, abstract, adviser, index_terms FROM "+TABLE_PROJECTS;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {

                System.out.println("Project ID:\n" + rs.getInt("id") +"\n");
                System.out.println("==========================================\n");
                System.out.println("Title:\n" + rs.getString("title") +"\n");
                System.out.println("Authors:\n" + rs.getString("authors")+"\n");
                System.out.println("Adviser: \n" +  rs.getString("adviser")+"\n");
                System.out.println("Abstract: \n" + rs.getString("abstract")+"\n");
                System.out.println("Index Terms: \n"+ rs.getString("index_terms")+"\n");
                System.out.println("==========================================\n");

//                System.out.println(
//                        rs.getInt("id") +  "\n***\n" +
//                        rs.getString("title") + "\n***\n" +
//                        rs.getString("authors") + "\n***\n" +
//                        rs.getString("abstract") +"\n***\n" +
//                        rs.getString("adviser") +"\n***\n" +
//                        rs.getString("index_terms") +"\n***\n"
//                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void deleteTableProjects(){
        String url = "jdbc:sqlite:C://sqlite//java/connect/" + DATABASE_NAME;

        String sql = "DROP TABLE "+TABLE_PROJECTS;

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            System.out.println("Successfully deleted table "+TABLE_PROJECTS);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

