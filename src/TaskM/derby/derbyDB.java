package TaskM.derby;

import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Justin Haupt
 *
 * This class manages the Apache derby database. The database files for all users
 * are located in the main executable's directory. The database is used for
 * storing/retrieving user account information and user task management data.
 * This object also keeps track of the ID of the current logged in user.
 */


public class derbyDB {

    private int totalUsers;
    private int currentUserID;    // the ID of the logged in user

    private final String protocol = "jdbc:derby:";   // protocol
    private final String dbName = "derbyDB";         // the name of the database

    private Connection conn;
    private ArrayList<Statement> statements;  // list of Statements and PreparedStatements
    private PreparedStatement insertUser;
    private PreparedStatement insertTask;
    private Statement s;
    private ResultSet res;


    /*
     * The default constructor initiates the database. Establishes a
     * connection, statement, and prepared statements. Creates new
     * tables for user accounts and data if they do not already exist.
     */
    public derbyDB() {

        this.totalUsers = this.currentUserID = 0;
        this.conn = null;
        this.statements = new ArrayList<>();
        this.insertUser = this.insertTask = null;
        this.s = null;
        this.res = null;

        try {
            // connect the database
            conn = DriverManager.getConnection(protocol + dbName
                    + ";create=true");

            conn.setAutoCommit(false);   // turn off autocommit to false

            s = conn.createStatement();  // create a statement object to run SQL commands
            statements.add(s);           // add object to the list of statement objects


            // Get database metadata to check if there are existing user accounts
            DatabaseMetaData meta = conn.getMetaData();
            res = meta.getTables(null, null, "ACCOUNTS", null);
            if (res.next()) {

                // count the total number of users
                try {
                    res = s.executeQuery("SELECT id FROM ACCOUNTS");
                    while (res.next()) {
                        ++totalUsers;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                // create a table for accounts
                s.execute("CREATE TABLE ACCOUNTS(" +
                        "id INTEGER NOT NULL PRIMARY KEY," +
                        "username varchar(15) NOT NULL," +
                        "password varchar(15) NOT NULL)");

                // create a table for user data
                s.execute("CREATE TABLE USERDATA(" +
                        "userID INTEGER NOT NULL, " +
                        "tType INTEGER NOT NULL, " +
                        "task varchar(30), " +
                        "CONSTRAINT f_key FOREIGN KEY(userID) REFERENCES ACCOUNTS(id))");
            }

            // Make prepared statements for inserting into both tables
            insertUser = conn.prepareStatement("INSERT INTO ACCOUNTS VALUES (?, ?, ?)");
            statements.add(insertUser);

            insertTask = conn.prepareStatement("INSERT INTO USERDATA VALUES(?, ?, ?)");
            statements.add(insertTask);

            System.out.println("Successfully initialized database");
        } catch (SQLException e) {
            System.out.println("Error initializing the database!");
            System.out.println(e.getMessage());
        }
    }


    /*
     * Checks if a username is already in the database. Return true
     * if the username has not been found, and false otherwise.
     */
    public boolean isValidUsername(String username) {

        try {
            res = s.executeQuery("SELECT username FROM ACCOUNTS");

            // Check if the username is already in the database
            while (res.next()) {
                String tableName = res.getString(1);
                if (username.equals(tableName)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }


    /*
     * Checks if the account information in the login text fields match
     * any account in the database.
     */
    public boolean isValidLogin(String username, String password) {

        try {
            this.res = s.executeQuery("SELECT * FROM ACCOUNTS");

            // Finds a match for the username and password
            while (res.next()) {
                int userID = res.getInt(1);
                String tableName = res.getString(2);
                String tablePassword = res.getString(3);
                if (username.equals(tableName) && password.equals(tablePassword)) {
                    this.currentUserID = userID;
                    return true;     // there is a match
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;   // no match found
    }


    /*
     * Adds a new user to the database.
     */
    public void addNewUser(String username, String password) {

        ++this.totalUsers;
        this.currentUserID = totalUsers;

        try {
            insertUser.setInt(1, totalUsers);   //primary key
            insertUser.setString(2, username);
            insertUser.setString(3, password);
            insertUser.executeUpdate();
            System.out.println("User inserted");
        }catch (SQLException e) {
             System.out.println("Error inserting user!");
             System.out.println(e.getMessage());
        }
    }


    /*
     * Returns one list type for a user upon login. Must be called three times
     * in order to load all three lists.
     */
    public List<String> retrieveList(int type) {

        List<String> list = new ArrayList<>();

        try {
            String command = "SELECT task FROM USERDATA WHERE tType = " + type + " AND userID = " + currentUserID;
            res = s.executeQuery(command);

            while (res.next())
                list.add(res.getString(1));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


    /*
     * Saves user data in the event of logging out or closing the window.
     * It deletes all of the old data before adding the new tasks.
     */
    public boolean save(ObservableList<String> list1, ObservableList<String> list2, ObservableList<String> list3) {
        try {

            // delete the old task data
            String command = "DELETE FROM USERDATA WHERE userID = " + currentUserID;
            s.executeUpdate(command);

            // add the updated task data for each list
            for(String item : list1) {
                insertTask.setInt(1, currentUserID);
                insertTask.setInt(2, 1);
                insertTask.setString(3, item);
                insertTask.executeUpdate();
            }
            for(String item : list2) {
                insertTask.setInt(1, currentUserID);
                insertTask.setInt(2, 2);
                insertTask.setString(3, item);
                insertTask.executeUpdate();
            }
            for(String item : list3) {
                insertTask.setInt(1, currentUserID);
                insertTask.setInt(2, 3);
                insertTask.setString(3, item);
                insertTask.executeUpdate();
            }
            System.out.println("Data saved");

        } catch (SQLException e) {
            System.out.println("Error saving data!");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    /*
     * Shuts the database down by committing all changes and closing
     * the connection. Also releases all open resources to avoid memory leaks.
     */
    public void shutdown() {

        try {
            conn.commit();  // commit changes to the database

            // Shutdown the database
            try {
                // the shutdown=true attribute shuts down Derby
                DriverManager.getConnection("jdbc:derby:;shutdown=true");

            } catch (SQLException e) {
                if (((e.getErrorCode() == 50000)
                        && ("XJ015".equals(e.getSQLState())))) {
                    System.out.println("Derby shut down normally");
                } else {
                    System.err.println("Derby did not shut down normally");
                    System.out.println(e.getMessage());
                }
            }

            // ResultSet
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
            } catch (SQLException e) {
                System.out.println("Error closing the result set");
                System.out.println(e.getMessage());
            }


            // Statements and PreparedStatements
            for(Statement st : statements) {

                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                    statements.remove(st);
                } catch (SQLException e) {
                    System.out.println("Error closing statements");
                    System.out.println(e.getMessage());
                }
            }


            // Close the connection
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection");
                System.out.println(e.getMessage());
            }

        }catch(SQLException e) {
            System.out.println("Error shutting down the database!");
        }
    }
}



