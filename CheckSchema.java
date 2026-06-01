import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class CheckSchema {
    public static void main(String[] args) {
        String URL = "jdbc:postgresql://localhost:5432/collabWhiteboard";
        String USER = "postgres";
        String PASSWORD = "m0ss13";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "draw_events", null);
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME") + " - " + rs.getString("TYPE_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
