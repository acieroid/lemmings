package util;

import java.util.ArrayList;
import java.util.Collections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private SQLite.Database db;
    private static PreparedStatement addScoreQuery, changeOptionQuery,
        addOptionQuery, optionExistsQuery, getOptionQuery, getScoresQuery;

    private static Database instance;

    public Database(String file) {
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite:/" + file);
            java.lang.reflect.Method m =
                connection.getClass().getMethod("getSQLiteDatabase");
            db = (SQLite.Database) m.invoke(connection);

            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Database get() {
        if (instance == null)
            instance = new Database("database.sqlite");
        return instance;
    }

    private void createTables() {
        try {
            Statement st = connection.createStatement();
            String options =
                "create table options(name varchar(100), value varchar(100))";
            String scores =
                "create table scores(map varchar(100), name varchar(100), score integer)";

            try {
                st.executeUpdate("select * from options");
            } catch (SQLException e) {
                /* Table does not exists */
                st.executeUpdate(options);
            }

            try {
                st.executeUpdate("select * from scores");
            } catch (SQLException e) {
                st.executeUpdate(scores);
            }

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void finalize()
        throws Throwable {
        /* TODO: close the prepared statements */
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.finalize();
    }

    public static void addScore(String map, String name, int score)
        throws LemmingsException {
        try {
            if (addScoreQuery == null) {
                String query = "insert into scores values (?, ?, ?)";
                addScoreQuery = get().connection.prepareStatement(query);
            }
            addScoreQuery.setString(1, map);
            addScoreQuery.setString(2, name);
            addScoreQuery.setInt(3, score);
            addScoreQuery.executeUpdate();
            get().connection.commit();
        } catch (SQLException e) {
            throw new LemmingsException("db",
                                        "Cannot alter database: " + e.getMessage());
        }
    }

    public static ArrayList<Score> getScores(String map)
        throws LemmingsException {
        ArrayList<Score> res = new ArrayList<Score>();
        try {
            if (getScoresQuery == null) {
                String query = "select name, score from scores where map = ?";
                getScoresQuery = get().connection.prepareStatement(query);
            }
            getScoresQuery.setString(1, map);
            ResultSet rs = getScoresQuery.executeQuery();

            if (rs.first()) {
                do {
                    res.add(new Score(rs.getString(1), rs.getInt(2)));
                } while (rs.next());
            }
            Collections.sort(res);
            return res;
        } catch (SQLException e) {
            throw new LemmingsException("db",
                                        "Cannot query database: " + e.getMessage());
        }
    }

    private static boolean optionExists(String name)
        throws LemmingsException {
        try {
            if (optionExistsQuery == null) {
                String query = "select 1 from options where name = ?";
                optionExistsQuery = get().connection.prepareStatement(query);
            }
            optionExistsQuery.setString(1, name);
            ResultSet rs = optionExistsQuery.executeQuery();
            if (rs.first()) {
                rs.close();
                return true;
            }
            else {
                rs.close();
                return false;
            }
        } catch (SQLException e) {
            throw new LemmingsException("db",
                                        "Cannot query database: " + e.getMessage());
        }
    }

    public static void changeOption(String name, String value)
        throws LemmingsException {
        try {
            if (optionExists(name)) {
                if (changeOptionQuery == null) {
                    String query = "update options set value = ? where name = ?";
                    changeOptionQuery = get().connection.prepareStatement(query);
                }
                changeOptionQuery.setString(1, value);
                changeOptionQuery.setString(2, name);
                changeOptionQuery.executeUpdate();
            }
            else {
                if (addOptionQuery == null) {
                    String query = "insert into options values (?, ?)";
                    addOptionQuery = get().connection.prepareStatement(query);
                }

                addOptionQuery.setString(1, name);
                addOptionQuery.setString(2, value);
                addOptionQuery.executeUpdate();
            }
            get().connection.commit();
        } catch (SQLException e) {
            throw new LemmingsException("db",
                                        "Cannot alter database: " + e.getMessage());
        }
    }

    public static void changeOptionInt(String name, int value)
        throws LemmingsException {
        changeOption(name, Integer.toString(value));
    }

    public static void changeOptionBoolean(String name, boolean value)
        throws LemmingsException {
        changeOption(name, Boolean.toString(value));
    }

    public static String getOption(String name, String def) {
        try {
            if (getOptionQuery == null) {
                String query = "select value from options where name = ?";
                getOptionQuery = get().connection.prepareStatement(query);
            }
            getOptionQuery.setString(1, name);
            ResultSet rs = getOptionQuery.executeQuery();

            String res = def;
            if (rs.first())
                res = rs.getString(1);

            rs.close();
            return res;
        } catch (SQLException e) {
            System.out.println("Cannot query database: " + e.getMessage());
            return def;
        }
    }

    public static int getOptiontInt(String name, int def) {
        String value = getOption(name, Integer.toString(def));
        return Integer.parseInt(value);
    }

    public static boolean getOptionBoolean(String name, boolean def) {
        String value = getOption(name, Boolean.toString(def));
        return Boolean.parseBoolean(value);
    }
}
