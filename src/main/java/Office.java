import java.sql.*;
import java.util.Scanner;
import java.security.Provider;

public class Office {

    public static void main(String[] args) throws SQLException {

        Option opt = Option.AddDepartment;
        Scanner sc = new Scanner(System.in);

        while (!opt.equals(Option.EXIT)) {
            System.out.println("Введите число:");
            for (Option o : Option.values()) System.out.println(o.getText());
            opt = Option.values()[sc.nextInt()];
            opt.action();
        }

        checkEmployee();
    }

    public static void checkEmployee() {
        Service.createDB();
        String url = "jdbc:h2:.\\Office";
        Statement statement = null;
        ResultSet rs;
        try (Connection con = DriverManager.getConnection(url)) {
            if (con != null) {
                System.out.println("Connection opened");
            } else {
                System.out.println("Failed to make connection");
            }
            //Найдите ID сотрудника с именем Ann. Если такой сотрудник только один, то установите его департамент в HR.
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery("Select * from Employee");

            while (rs.next()) {
                System.out.println("Имя сотрудника: " + rs.getString("Name") + "\t" + "ID отдела: " + rs.getString("DEPARTMENTID"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
        public static void updateDB()
        {
            Service.createDB();
            String url = "jdbc:h2:.\\Office";
            Statement statement = null;
            ResultSet rs;
            try (Connection con = DriverManager.getConnection(url)) {
                if (con != null) {
                    System.out.println("Connection opened");
                } else {
                    System.out.println("Failed to make connection");
                }
                //Найдите ID сотрудника с именем Ann. Если такой сотрудник только один, то установите его департамент в HR.
                statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = statement.executeQuery("Select count(*) from Employee where NAME='Ann'");
                rs.next();
                if (rs.getInt(1) == 1) {
                    rs = statement.executeQuery("Select ID from DEPARTMENT where NAME='HR'");
                    rs.next();
                    String depId = rs.getString("ID");
                    //System.out.println(depId);
                    rs = statement.executeQuery("Select * from Employee where NAME='Ann'");
                    rs.next();
                    //System.out.println(rs.getInt("DEPARTMENTID") + "\t" + rs.getString("Name"));
                    rs.updateString("DEPARTMENTID", depId);
                    rs.updateRow();
                    //System.out.println(rs.getInt("DEPARTMENTID") + "\t" + rs.getString("Name"));
                    rs = statement.executeQuery("Select * from Employee where NAME='Ann'");
                    rs.next();
                    System.out.println("Id отдела: "+ rs.getInt("DEPARTMENTID") + "\t" +"Имя сотрудника: "+ rs.getString("Name"));
                }

                //Проверьте имена всех сотрудников. Если чьё-то имя написано с маленькой буквы, исправьте её на большую. Выведите на экран количество исправленных имён.
                rs = statement.executeQuery("Select count(*) from Employee");
                rs.next();
                int countEmp = rs.getInt(1);
                int countNameInLowerCase = 0;
                for (int i = 1; i <= countEmp; i++) {
                    rs = statement.executeQuery("Select NAME from Employee where ID=" + i);
                    rs.next();
                    String lower = rs.getString(1).toLowerCase();
                    if (rs.getString(1).equals(lower)) {
                        countNameInLowerCase++;
                        String s = rs.getString(1).substring(0,1);
                        String s2 = rs.getString(1).substring(1, rs.getString(1).length());
                        String s3 = s+s2;
                        rs = statement.executeQuery("Select * from Employee where ID=" + i);
                        rs.next();
                        rs.updateString("NAME", s3);
                        rs.updateRow();
                    }
                }
                System.out.println("Количество исправленных имен: "+countNameInLowerCase);

                //Выведите на экран количество сотрудников в IT-отделе
                rs = statement.executeQuery("Select ID from DEPARTMENT where NAME='IT'");
                rs.next();
                String depId = rs.getString("ID");
                rs = statement.executeQuery("Select count(*) as Total from Employee where DEPARTMENTID="+depId);
                rs.next();
                System.out.println("Количество сотрудников в IT-отделе: "+rs.getInt(1));
            }
            catch (SQLException ex) {
                System.out.println(ex);
            }
        }
}


