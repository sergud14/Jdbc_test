import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.*;
public class Tests {
        @Test
        public void testDeleteDepartment(){
            Department d =new Department(1,"Accounting");
            int countDep=0;
            int countEmpDep=0;
            String url = "jdbc:h2:.\\Office";
            Statement statement = null;
            ResultSet rs;
            try (Connection con = DriverManager.getConnection(url)) {
                if (con != null) {
                    System.out.println("Connection opened");
                } else {
                    System.out.println("Failed to make connection");
                }
                Service.createDB();
                Service.removeDepartment(d);

                statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = statement.executeQuery("Select count(*) from DEPARTMENT where ID="+d.departmentID);
                rs.next();
                countDep = rs.getInt(1);
                rs = statement.executeQuery("Select count(*) from Employee where DEPARTMENTID="+d.departmentID);
                rs.next();
                countEmpDep = rs.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Assertions.assertEquals(0,countDep);
            Assertions.assertEquals(0,countEmpDep);
        }
}
