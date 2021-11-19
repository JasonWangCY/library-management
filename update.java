
import java.sql.*;

class Update{
    public static void main(String [] args){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db42";
        String dbUsername = "Group42";
        String dbPassword = "physicsisawesome";

        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);

            Statement myStatement = con.createStatement();

            //update query
            String sqlQuery2 = "update S1 set year=2020 where sid=1002;";

            myStatement.executeUpdate(sqlQuery2);
            System.out.println("Update Completed");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
