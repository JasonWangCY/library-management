
import java.sql.*;

class Insert{
    public static void main(String [] args){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db42";
        String dbUsername = "Group42";
        String dbPassword = "physicsisawesome";
        String driver = "com.mysql.jdbc.Driver";

        Connection con = null;
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            Statement myStatement = con.createStatement();

            //insertion query
            String sqlQuery1 = "insert into S1 (sid, name, year, age) values (1002, 'Peter', 2018, 19);";
            myStatement.executeUpdate(sqlQuery1);

            System.out.println("Insertion Completed");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
