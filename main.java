import java.util.Scanner;
import java.sql.*;
import java.io.*;

class Utility{
    public static String choiceQuestionString = "What kinds of operations would you like to perform?";
    public static String enterChoice = "Enter Your Choice: ";
    public static boolean choice_error_condition(String userOption, int choiceNo){
        for(int i = 1; i<= choiceNo; i++){
            if(userOption.equals(String.valueOf(i))){
                return false;
            }
        }
        return true;
    }
    public static void print_choice_selection_error_message(int choiceNo){
        String baseString = "[Error]: Please enter either ";
        for(int i = 1; i<=choiceNo; i++){
            if (i == choiceNo){
                baseString += " " +String.valueOf(i)+"!";
            }
            else if (i == 1){
                baseString += String.valueOf(i)+",";
            }
            else if (i == choiceNo - 1){
                baseString += " "+String.valueOf(i)+" or";
            }
            else{
                baseString += " "+String.valueOf(i)+",";
            }
        }
        System.out.println(baseString);
    }
}

class Schema{
    public static String create_user_category = "create table if not exists user_category("
        + "ucid SMALLINT UNSIGNED NOT NULL,"
        + "max SMALLINT UNSIGNED NOT NULL,"
        + "period SMALLINT UNSIGNED NOT NULL,"
        + "PRIMARY KEY(ucid)"
        + ");";
    public static String create_lib_user = "create table if not exists libuser("
        + "libuid CHAR(10) NOT NULL,"  
        + "name VARCHAR(25) NOT NULL,"
        + "age SMALLINT UNSIGNED NOT NULL,"
        + "address VARCHAR(100) NOT NULL,"
        + "ucid SMALLINT UNSIGNED NOT NULL,"
        + "PRIMARY KEY (libuid),"
        + "FOREIGN KEY (ucid) REFERENCES user_category(ucid) ON UPDATE CASCADE ON DELETE CASCADE"
        + ");";
    public static String create_book_category = "create table if not exists book_category("
        + "bcid SMALLINT UNSIGNED NOT NULL,"
        + "bcname VARCHAR(30) NOT NULL,"
        + "PRIMARY KEY (bcid)"
        + ");";
    public static String create_book = "create table if not exists book("
        + "callnum CHAR(8) NOT NULL,"
        + "title VARCHAR(30) NOT NULL,"
        + "publish CHAR(10) NOT NULL,"
        + "rating REAL UNSIGNED DEFAULT NULL,"
        + "tborrowed SMALLINT UNSIGNED NOT NULL,"
        + "bcid SMALLINT UNSIGNED NOT NULL,"
        + "PRIMARY KEY (callnum),"
        + "FOREIGN KEY(bcid) REFERENCES book_category(bcid) ON UPDATE CASCADE ON DELETE CASCADE"
        + ");";
    public static String create_authorship = "create table if not exists authorship("
        + "aname VARCHAR(255) NOT NULL,"
        + "callnum CHAR(8) NOT NULL,"
        + "PRIMARY KEY (aname, callnum),"
        + "FOREIGN KEY (callnum) REFERENCES book(callnum)"
        + ");";
    public static String create_copy = "create table if not exists copy("
        + "copynum SMALLINT UNSIGNED NOT NULL,"
        + "callnum CHAR(8) NOT NULL,"
        + "PRIMARY KEY (copynum, callnum),"
        + "FOREIGN KEY (callnum) REFERENCES book(callnum) ON UPDATE CASCADE ON DELETE CASCADE" 
        + ");";
    public static String create_borrow = "create table if not exists borrow("
        + "libuid CHAR(10) NOT NULL,"
        + "callnum CHAR(8) NOT NULL,"
        + "copynum SMALLINT UNSIGNED NOT NULL,"
        + "checkout CHAR(10) NOT NULL,"
        + "`return` CHAR(10) DEFAULT NULL,"
        + "PRIMARY KEY (callnum, copynum, libuid, checkout),"
        + "FOREIGN KEY(libuid) REFERENCES libuser(libuid) ON UPDATE CASCADE ON DELETE CASCADE," 
        + "FOREIGN KEY(callnum) REFERENCES book(callnum),"
        + "FOREIGN KEY(copynum, callnum) REFERENCES copy(copynum, callnum) ON UPDATE CASCADE ON DELETE CASCADE"
        + ");";
}

class SQLConnection{
    public static Connection connect_to_sql(String dbAddress, String dbUsername, String dbPassword){
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }
}

class SQLQuery{
    public static void sql_operation(Connection con, String query){
        try{
            Statement myStatement = con.createStatement();

            myStatement.executeUpdate(query);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void sql_count(Connection con, String table_name){
        try{
            Statement stmt = con.createStatement();
            String sqlQuery = "select count(*) from " + table_name + ";";

            ResultSet rs = stmt.executeQuery(sqlQuery);
            
            rs.next();
            System.out.println(table_name + ": " + rs.getInt(1));
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void search_book(Connection con, String sqlQuery){
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else while(rs.next()){
                System.out.print("|"+rs.getString("callnum"));
                System.out.print("|"+rs.getString("title"));
                System.out.print("|"+rs.getString("bcname"));
                System.out.print("|"+rs.getString("aname"));
                System.out.print("|"+rs.getString("rating")+"|\n");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void search_user_record(Connection con, String sqlQuery){
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else while(rs.next()){
                System.out.print("|"+rs.getString("callnum"));
                System.out.print("|"+rs.getString("title"));
                System.out.print("|"+rs.getString("copynum"));
                System.out.print("|"+rs.getString("aname"));
                System.out.print("|"+rs.getString("checkout"));
                System.out.print("|"+rs.getString("returned")+"|\n");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void read_data(Connection con, String folderPath){
        try{
            File file = new File(folderPath + "/user_category.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt = con.prepareStatement("insert into user_category values (?,?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                int ucid = Integer.parseInt(split[0].trim());
                String max = split[1].trim();
                int period = Integer.parseInt(split[2].trim());
                stmt.setInt(1, ucid);
                stmt.setString(2, max);
                stmt.setInt(3, period);
                stmt.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            File file = new File(folderPath + "/user.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt = con.prepareStatement("insert into libuser values (?,?,?,?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                String libuid = split[0].trim();
                String name = split[1].trim();
                int age = Integer.parseInt(split[2].trim());
                String address = split[3].trim();
                int ucid = Integer.parseInt(split[4].trim());
                stmt.setString(1, libuid);
                stmt.setString(2, name);
                stmt.setInt(3, age);
                stmt.setString(4, address);
                stmt.setInt(5, ucid);
                stmt.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            File file = new File(folderPath + "/book_category.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt = con.prepareStatement("insert into book_category values (?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                int bcid = Integer.parseInt(split[0].trim());
                String bcname = split[1].trim();
                stmt.setInt(1, bcid);
                stmt.setString(2, bcname);
                stmt.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            File file = new File(folderPath + "/book.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt1 = con.prepareStatement("insert into book values (?,?,?,?,?,?)");
            PreparedStatement stmt2 = con.prepareStatement("insert into authorship values (?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                String callnum = split[0].trim();
                int num_of_copies = Integer.parseInt(split[1].trim()); //where is this in the schema??
                String title = split[2].trim();
                String aname = split[3].trim();
                String publish = split[4].trim();
                if (!split[5].trim().equals("null")){
                    double rating = Double.parseDouble(split[5].trim());
                    stmt1.setDouble(4, rating);
                } else {
                    stmt1.setNull(4, Types.DOUBLE);
                }
                int tborrowed = Integer.parseInt(split[6].trim());
                int bcid = Integer.parseInt(split[7].trim());

                stmt1.setString(1, callnum);
                stmt1.setString(2, title);
                stmt1.setString(3, publish);
                stmt1.setInt(5, tborrowed);
                stmt1.setInt(6, bcid);
                stmt1.executeUpdate();

                stmt2.setString(1, aname);
                stmt2.setString(2, callnum);
                stmt2.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            File file = new File(folderPath + "/check_out.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt1 = con.prepareStatement("insert into copy values (?,?)");
            PreparedStatement stmt2 = con.prepareStatement("insert into borrow values (?,?,?,?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                String callnum = split[0].trim();
                int copynum = Integer.parseInt(split[1].trim());
                stmt1.setInt(1, copynum);
                stmt1.setString(2, callnum);
                stmt1.executeUpdate();

                String libuid = split[2].trim();
                String checkout = split[3].trim();
                if (!split[4].trim().equals("null")){
                    String return_date = split[4].trim();
                    stmt2.setString(5, return_date);
                } else {
                    stmt2.setNull(5, Types.CHAR);
                }
                stmt2.setString(1, libuid);
                stmt2.setString(2, callnum);
                stmt2.setInt(3, copynum);
                stmt2.setString(4, checkout);
                stmt2.executeUpdate();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

class Main{
    private static int choiceNo = 4;
    private static void print_greeting_strings(){
        String _greetings= "Welcome to Library Inquiry System!\n";
        System.out.print(_greetings);
    }
    private static void print_separator(){
        String _separator = "\n-----Main menu-----\n";
        System.out.print(_separator);
    }
    private static void print_choice_strings(){
        String _choice1 = "1. Operations for Administrator\n";
        String _choice2 = "2. Operations for Library User\n";
        String _choice3 = "3. Operations for Librarian\n";
        String _choice4 = "4. Exit this program\n";
        System.out.print(_choice1 + _choice2 + _choice3 + _choice4);
    }
    private static void initialization(String dbChoiceQuestionString, String dbmsEnterChoice){
        print_separator();
        System.out.println(dbChoiceQuestionString);
        print_choice_strings();
        System.out.print(dbmsEnterChoice);
    }
    private static boolean do_operation(String userOption, Connection con){
        if(userOption.equals("1")){
            Administrator.main(con);
            return true;
        }
        else if (userOption.equals("2")){
            LibraryUser.main(con);
            return true;
        }
        else if(userOption.equals("3")){
            Librarian.main();
            return true;
        }
        else{
            System.out.println("Thank you for using this Database Management System!");
            return false;
        }

    }
    public static void main(String [] args){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db42";
        String dbUsername = "Group42";
        String dbPassword = "physicsisawesome";

        Connection con = SQLConnection.connect_to_sql(dbAddress, dbUsername, dbPassword);
        
        print_greeting_strings();
        boolean continueFlag = true;
        try{
            do{
                Scanner inputScanner = new Scanner(System.in);
                initialization(Utility.choiceQuestionString, Utility.enterChoice);
                String userOption = inputScanner.next();
                if (Utility.choice_error_condition(userOption, Main.choiceNo)) {
                    do {
                        Utility.print_choice_selection_error_message(Main.choiceNo);
                        System.out.print(Utility.enterChoice);
                        userOption = inputScanner.next();
                    } while (Utility.choice_error_condition(userOption, Main.choiceNo));
                }
                continueFlag = do_operation(userOption, con);
            }while (continueFlag);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

class Administrator {
    private static int choiceNo = 5;
    private static void print_separator() {
        String _separator = "\n-----Operations for administrator menu-----\n";
        System.out.print(_separator);
    }

    private static void print_choice_strings() {
        String _choice1 = "1. Create all tables\n";
        String _choice2 = "2. Delete all tables\n";
        String _choice3 = "3. Load from datafile\n";
        String _choice4 = "4. Show number of records in each table\n";
        String _choice5 = "5. Return to the main menu\n";
        System.out.print(_choice1 + _choice2 + _choice3 + _choice4 + _choice5);
    }

    private static void initialization(String dbChoiceQuestionString, String dbmsEnterChoice) {
        print_separator();
        System.out.println(dbChoiceQuestionString);
        print_choice_strings();
        System.out.print(dbmsEnterChoice);
    }

    private static boolean do_operation(String userOption, Connection con) {
        if (userOption.equals("1")) {
            administrator_choice_1(con);
            return true;
        } else if (userOption.equals("2")) {
            administrator_choice_2(con);
            return true;
        } else if (userOption.equals("3")) {
            administrator_choice_3(con);
            return true;
        } else if (userOption.equals("4")) {
            administrator_choice_4(con);
            return true;
        }else {
            return false;
        }
    }
    private static void administrator_choice_1(Connection con){
        SQLQuery.sql_operation(con, Schema.create_user_category);
        SQLQuery.sql_operation(con, Schema.create_lib_user);
        SQLQuery.sql_operation(con, Schema.create_book_category);
        SQLQuery.sql_operation(con, Schema.create_book);
        SQLQuery.sql_operation(con, Schema.create_authorship);
        SQLQuery.sql_operation(con, Schema.create_copy);
        SQLQuery.sql_operation(con, Schema.create_borrow);
        System.out.println("Processing...Done. Database is initialized.");
    }
    
    private static void administrator_choice_2(Connection con) {
        String drop_all_tables = "drop tables borrow, copy, authorship, book, book_category, libuser, user_category;";
        SQLQuery.sql_operation(con, drop_all_tables);
        System.out.println("Processing...Done. Database is removed.");
    }

    private static void administrator_choice_3(Connection con) {
        System.out.print("\nType in the Source Data Folder Path: ");
        Scanner inputScanner = new Scanner(System.in);
        String folderPath = inputScanner.next();
        SQLQuery.read_data(con, folderPath);
        System.out.println("Processing...Done. Database is inputted to the database.");
    }

    private static void administrator_choice_4(Connection con) {
        System.out.print("Numer of records in each table:\n");
        SQLQuery.sql_count(con, "user_category");
        SQLQuery.sql_count(con, "libuser");
        SQLQuery.sql_count(con, "book_category");
        SQLQuery.sql_count(con, "book");
        SQLQuery.sql_count(con, "authorship");
        SQLQuery.sql_count(con, "copy");
        SQLQuery.sql_count(con, "borrow");
    }

    public static void main(Connection con) {
        try {
            boolean continueFlag = true;
            do{
                Scanner inputScanner = new Scanner(System.in);
                initialization(Utility.choiceQuestionString, Utility.enterChoice);
                String userOption = inputScanner.next();
                if (Utility.choice_error_condition(userOption, Administrator.choiceNo)) {
                    do {
                        Utility.print_choice_selection_error_message(Administrator.choiceNo);
                        System.out.print(Utility.enterChoice);
                        userOption = inputScanner.next();
                    } while (Utility.choice_error_condition(userOption, Administrator.choiceNo));
                }
                continueFlag = do_operation(userOption, con);
            }while(continueFlag);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class LibraryUser{
    private static int choiceNo = 3;
    private static void print_separator() {
        String _separator = "\n-----Operations for library user menu-----\n";
        System.out.print(_separator);
    }

    private static void print_choice_strings() {
        String _choice1 = "1. Search for Books\n";
        String _choice2 = "2. Show loan record of a user\n";
        String _choice3 = "3. Return to the main menu\n";
        System.out.print(_choice1 + _choice2 + _choice3);
    }

    private static void initialization(String dbChoiceQuestionString, String dbmsEnterChoice) {
        print_separator();
        System.out.println(dbChoiceQuestionString);
        print_choice_strings();
        System.out.print(dbmsEnterChoice);
    }
    
    private static boolean do_operation(String userOption, Connection con) {
        if (userOption.equals("1")) {
            libraryuser_choice_1(con);
            return true;
        } else if (userOption.equals("2")) {
            libraryuser_choice_2(con);
            return true;
        } else {
            return false;
        }
    }

    private static void libraryuser_choice_1(Connection con){
        System.out.println("Choose the search criterion:");
        System.out.println("1. call number");
        System.out.println("2. title");
        System.out.println("3. author");
        System.out.print("Choose the search criterion: ");

        Scanner inputScanner = new Scanner(System.in);
        String searchOption = inputScanner.next();
        if (Utility.choice_error_condition(searchOption, 3)) {
            do {
                Utility.print_choice_selection_error_message(3);
                System.out.print("Choose the search criterion: ");
                searchOption = inputScanner.next();
            }while (Utility.choice_error_condition(searchOption, 3));
        }
        search_operation(searchOption, con);
    }

    private static void libraryuser_choice_2(Connection con) {
        System.out.print("Enter the User ID: ");
        Scanner inputScanner = new Scanner(System.in);
        String userID = inputScanner.next();
        
        String sqlQuery = "select book.callnum, title, copynum, aname, checkout, 'Yes' as returned "
            + "from book, borrow, authorship, libuser "
            + "where libuser.libuid = '" + userID + "' and book.callnum=authorship.callnum "
            + "and book.callnum=borrow.callnum and checkout is not null and borrow.libuid = libuser.libuid;";
        System.out.println("|CallNum|CopyNum|Title|Author|Check-out|Returned?|");
        SQLQuery.search_user_record(con, sqlQuery);
        System.out.println("End of Query");
    }

    private static void search_operation(String searchOption, Connection con){
        System.out.print("Type in the Search Keyword: ");
        Scanner inputScanner = new Scanner(System.in);

        if (searchOption.equals("1")) {
            String callnum = inputScanner.next();
            String sqlQuery = "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where book.callnum = '" + callnum + "' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum;";
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            SQLQuery.search_book(con, sqlQuery);
            System.out.println("End of Query");
        } else if (searchOption.equals("2")) {
            String title = inputScanner.next();
            String sqlQuery = "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where book.title like '%" + title + "%' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum;";
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            SQLQuery.search_book(con, sqlQuery);
            System.out.println("End of Query");
        } else if (searchOption.equals("3")) {
            String author = inputScanner.next();
            String sqlQuery = "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where aname like '%" + author + "%' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum;";
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            SQLQuery.search_book(con, sqlQuery);
            System.out.println("End of Query");
        }
    }

    public static void main(Connection con){
        boolean continueFlag = true;
        try {
            do{
                Scanner inputScanner = new Scanner(System.in);
                initialization(Utility.choiceQuestionString, Utility.enterChoice);
                String userOption = inputScanner.next();
                if (Utility.choice_error_condition(userOption, LibraryUser.choiceNo)) {
                    do {
                        Utility.print_choice_selection_error_message(LibraryUser.choiceNo);
                        System.out.print(Utility.enterChoice);
                        userOption = inputScanner.next();
                    } while (Utility.choice_error_condition(userOption, LibraryUser.choiceNo));
                }
                continueFlag = do_operation(userOption, con);
            }while(continueFlag);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class Librarian {
    private static int choiceNo = 4;

    private static void print_separator() {
        String _libraryUserGreeting1 = "\n-----Operations for librarian menu-----\n";
        System.out.print(_libraryUserGreeting1);
    }

    private static void print_choice_strings() {
        String _choice1 = "1. Book Borrowing\n";
        String _choice2 = "2. Book Returning\n";
        String _choice3 = "3. List all un-returned book copies which are checked-out within a period\n";
        String _choice4 = "4. Return to the main menu\n";
        System.out.print(_choice1 + _choice2 + _choice3 + _choice4);
    }

    private static void initialization(String dbChoiceQuestionString, String dbmsEnterChoice) {
        print_separator();
        System.out.println(dbChoiceQuestionString);
        print_choice_strings();
        System.out.print(dbmsEnterChoice);
    }

    private static boolean do_operation(String userOption) {
        if (userOption.equals("1")) {
            ;
            return true;
        } else if (userOption.equals("2")) {
            ;
            return true;
        } else if (userOption.equals("3")) {
            ;
            return true;
        } else {
            return false;
        }
    }
    public static void main() {
        boolean continueFlag = true;
        try {
            do{
                Scanner inputScanner = new Scanner(System.in);
                initialization(Utility.choiceQuestionString, Utility.enterChoice);
                String userOption = inputScanner.next();
                if (Utility.choice_error_condition(userOption, Librarian.choiceNo)) {
                    do {
                        Utility.print_choice_selection_error_message(Librarian.choiceNo);
                        System.out.print(Utility.enterChoice);
                        userOption = inputScanner.next();
                    } while (Utility.choice_error_condition(userOption, Librarian.choiceNo));
                }
                continueFlag = do_operation(userOption);
            }while(continueFlag);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
