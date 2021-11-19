
import java.sql.*;

class Delete{
    public static void main(String [] args){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db42";
        String dbUsername = "Group42";
        String dbPassword = "physicsisawesome";

        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);

            Statement myStatement = con.createStatement();

            //delete query
            String sqlQuery3 = "delete from S1 where sid=1001;";

            myStatement.executeUpdate(sqlQuery3);
            System.out.println("Deletion Completed");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
