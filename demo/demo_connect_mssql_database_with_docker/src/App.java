import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("Cuong*0907359621");
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("TestDB");

        try (Connection conn = ds.getConnection()) {
            System.out.println("Connection success!");
            System.out.println(conn.getMetaData());
        } catch (SQLServerException err) {
            err.printStackTrace();
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }
}
