import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
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
        String baseString = Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Please enter either ";
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
    public static String parse_date_format(String date){
        String[] date_array = date.split("/");
        String year = date_array[2];
        String month = date_array[1];
        String day = date_array[0];
        return year+"-"+month+"-"+day;
    }
    public static String get_today_date(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
    public static String get_today_date_format(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db42";
    private static String dbUsername = "Group42";
    private static String dbPassword = "physicsisawesome";

    private static Connection connect_to_sql(String dbAddress, String dbUsername, String dbPassword) {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + ": Java MySQL DB Driver not found!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }

    public static Connection get_connection(){
        return connect_to_sql(Utility.dbAddress, Utility.dbUsername, Utility.dbPassword);
    }
    
    public static final String ANSI_BOLD = "\033[1m";
    public static final String ANSI_BOLD_RESET = "\033[0m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    // Underline with white 
    public static final String ANSI_WHITE_UNDERLINED = "\033[4;37m";
    // Underline with black
    public static final String ANSI_BLACK_UNDERLINED = "\033[4;30m";
    // Reset underline
    public static final String ANSI_UNDERLINE_RESET = "\033[0;0m";
    public static final long GLOBAL_SLEEP_TIME = 500;
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
        + "publish DATE NOT NULL,"
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
        + "checkout DATE NOT NULL,"
        + "`return` DATE DEFAULT NULL,"
        + "PRIMARY KEY (callnum, copynum, libuid, checkout),"
        + "FOREIGN KEY(libuid) REFERENCES libuser(libuid) ON UPDATE CASCADE ON DELETE CASCADE," 
        + "FOREIGN KEY(callnum) REFERENCES book(callnum),"
        + "FOREIGN KEY(copynum, callnum) REFERENCES copy(copynum, callnum) ON UPDATE CASCADE ON DELETE CASCADE"
        + ");";
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
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " No such book exists in the library.");
            else {
                System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy|");
                while(rs.next()){
                System.out.print("|"+rs.getString("callnum"));
                System.out.print("|"+rs.getString("title"));
                System.out.print("|"+rs.getString("bcname"));
                System.out.print("|"+rs.getString("aname"));
                System.out.print("|"+rs.getString("rating"));
                System.out.print("|"+rs.getString("num_of_copies")+"|\n");
                }
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET
                    + Utility.ANSI_BOLD_RESET +" End of Query");
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
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " User ID does not exist.");
            else {
                System.out.println("|CallNum|CopyNum|Title|Author|Check-out|Returned?|");
                while(rs.next()){
                System.out.print("|"+rs.getString("callnum"));
                System.out.print("|"+rs.getString("copynum"));
                System.out.print("|"+rs.getString("title"));
                System.out.print("|"+rs.getString("aname"));
                System.out.print("|"+rs.getString("checkout"));
                System.out.print("|"+rs.getString("returned")+"|\n");
                }
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET
                        + Utility.ANSI_BOLD_RESET +" End of Query");
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
            PreparedStatement stmt3 = con.prepareStatement("insert into copy values (?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                String callnum = split[0].trim();
                int num_of_copies = Integer.parseInt(split[1].trim());
                String title = split[2].trim();
                String aname = split[3].trim();
                String publish = split[4].trim();
                publish = Utility.parse_date_format(publish);
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

                for (int i=1; i<=num_of_copies; i++) {
                    stmt3.setInt(1, i);
                    stmt3.setString(2, callnum);
                    stmt3.executeUpdate();
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            File file = new File(folderPath + "/check_out.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PreparedStatement stmt = con.prepareStatement("insert into borrow values (?,?,?,?,?)");

            String st = null;
            while ((st = br.readLine()) != null){
                String[] split = st.split("\t");
                String callnum = split[0].trim();
                int copynum = Integer.parseInt(split[1].trim());

                String libuid = split[2].trim();
                String checkout = split[3].trim();
                checkout = Utility.parse_date_format(checkout);
                if (!split[4].trim().equals("null")){
                    String return_date = split[4].trim();
                    return_date = Utility.parse_date_format(return_date);
                    stmt.setString(5, return_date);
                } else {
                    stmt.setNull(5, Types.CHAR);
                }
                stmt.setString(1, libuid);
                stmt.setString(2, callnum);
                stmt.setInt(3, copynum);
                stmt.setString(4, checkout);
                stmt.executeUpdate();
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
            Librarian.main(con);
            return true;
        }
        else{
            System.out.println("Thank you for using this Database Management System!");
            return false;
        }

    }
    public static void main(String [] args){

        Connection con = Utility.get_connection();
        
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
                TimeUnit.MILLISECONDS.sleep(Utility.GLOBAL_SLEEP_TIME);
                continueFlag = do_operation(userOption, con);
                TimeUnit.MILLISECONDS.sleep(Utility.GLOBAL_SLEEP_TIME);
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
        System.out.println( Utility.ANSI_BOLD + Utility.ANSI_GREEN +"[Success]"+Utility.ANSI_RESET + Utility.ANSI_BOLD_RESET +" Processing... Done. Database is "
                + Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD + "initialized" + Utility.ANSI_BOLD_RESET+ Utility.ANSI_UNDERLINE_RESET + ".");
    }
    
    private static void administrator_choice_2(Connection con) {
        String drop_all_tables = "drop tables borrow, copy, authorship, book, book_category, libuser, user_category;";
        SQLQuery.sql_operation(con, drop_all_tables);
        System.out.println(
                Utility.ANSI_BOLD + Utility.ANSI_GREEN +"[Success]"+Utility.ANSI_RESET + Utility.ANSI_BOLD_RESET  + " Processing... Done. Database is "+ Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD
                        + "removed" + Utility.ANSI_BOLD_RESET+ Utility.ANSI_UNDERLINE_RESET+".");
    }

    private static void administrator_choice_3(Connection con) {
        System.out.print("\nType in the Source Data Folder Path: ");
        Scanner inputScanner = new Scanner(System.in);
        String folderPath = inputScanner.next();
        SQLQuery.read_data(con, folderPath);
        System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN +"[Success]"+Utility.ANSI_RESET + Utility.ANSI_BOLD_RESET 
                + " Processing... Done. Database is " + Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD
                + "input to the database" + Utility.ANSI_BOLD_RESET
                + Utility.ANSI_UNDERLINE_RESET + ".");
    }

    private static void administrator_choice_4(Connection con) {
        System.out.print(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET
                + Utility.ANSI_BOLD_RESET + " Number of records in each table:\n");
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
                TimeUnit.MILLISECONDS.sleep(Utility.GLOBAL_SLEEP_TIME);
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
        
        String sqlQuery = "select book.callnum, copynum, title, aname, checkout, 'Yes' as returned "
            + "from book, borrow, authorship, libuser "
            + "where libuser.libuid = '" + userID + "' and book.callnum=authorship.callnum "
            + "and book.callnum=borrow.callnum and checkout is not null and borrow.libuid = libuser.libuid "
            + "order by checkout desc;";
        SQLQuery.search_user_record(con, sqlQuery);
    }

    private static void search_operation(String searchOption, Connection con){
        System.out.print("Type in the Search Keyword: ");
        Scanner inputScanner = new Scanner(System.in);

        if (searchOption.equals("1")) {
            String callnum = inputScanner.next();
            String sqlQuery = "select copy.callnum, title, bcname, aname, rating, count(copy.callnum) as num_of_copies "
                + "from copy, ( "
                + "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where book.callnum = '" + callnum + "' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum"
                + ") as T1 "
                + "where copy.callnum = T1.callnum "
                + "group by copy.callnum "
                + "order by copy.callnum;";
            SQLQuery.search_book(con, sqlQuery);
        } else if (searchOption.equals("2")) {
            String title = inputScanner.next();
            String sqlQuery = "select copy.callnum, title, bcname, aname, rating, count(copy.callnum) as num_of_copies "
                + "from copy, ( "
                + "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where book.title like '%" + title + "%' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum"
                + ") as T1 "
                + "where copy.callnum = T1.callnum "
                + "group by copy.callnum "
                + "order by copy.callnum;";
            SQLQuery.search_book(con, sqlQuery);
        } else if (searchOption.equals("3")) {
            String author = inputScanner.next();
            String sqlQuery = "select copy.callnum, title, bcname, aname, rating, count(copy.callnum) as num_of_copies "
                + "from copy, ( "
                + "select book.callnum, title, bcname, aname, rating "
                + "from book, book_category, authorship where aname like '%" + author + "%' "
                + "and book.bcid=book_category.bcid and book.callnum=authorship.callnum"
                + ") as T1 "
                + "where copy.callnum = T1.callnum "
                + "group by copy.callnum "
                + "order by copy.callnum;";
            SQLQuery.search_book(con, sqlQuery);
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
                TimeUnit.MILLISECONDS.sleep(Utility.GLOBAL_SLEEP_TIME);
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

    private static boolean do_operation(String userOption, Connection con) {
        if (userOption.equals("1")) {
            librarian_choice_1(con);
            return true;
        } else if (userOption.equals("2")) {
            librarian_choice_2(con);
            return true;
        } else if (userOption.equals("3")) {
            librarian_choice_3(con);
            return true;
        } else {
            return false;
        }
    }

    // a boolean check_userID_available function that inputs an userID through
    // connection con and check whether
    // the userID exists in the table libuser where libuid = userID. If exists,
    // return true, else return false.
    private static boolean check_userID_available(String userID, Connection con) {
        String sql = "SELECT * FROM libuser WHERE libuid = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    // a boolean check_callNumber_available function that inputs a callNumber through
    // connection con and check whether
    // the callNumber exists in the table book where callnum = callNumber. If exists,
    // return true, else return false.
    private static boolean check_callNumber_available(String callNumber, Connection con) {
        String sql = "SELECT * FROM book WHERE callnum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    // a boolean check_copyNumber_availble function that inputs a string callNumber and a int copyNumber through
    // connection con and check whether
    // the copyNumber of book callNumber exists in the table copy where callnum = callNumber and copynum = userID. If exists,
    // return true, else return false.
    private static boolean check_copyNumber_available(String callNumber, int copyNumber, Connection con) {
        String sql = "SELECT * FROM copy WHERE callnum = (?) and copynum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            pstmt.setInt(2, copyNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    //a boolean _check_date_format_correct function that has two parts.
    // It inputs a string date. The first part is to check whether it is
    // in the format of dd/mm/yyyy.
    // Another part is to check if the date is in between 1900-01-01 and today through Utility.get_today_date(). If not, return false,
    // else return true.
    private static boolean check_date_format_correct(String date) {
        if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] dateArray = date.split("/");
            int day = Integer.parseInt(dateArray[0]);
            int month = Integer.parseInt(dateArray[1]);
            int year = Integer.parseInt(dateArray[2]);
            String todayDate = Utility.get_today_date();
            String[] todayDateArray = todayDate.split("/");
            int todayDay = Integer.parseInt(todayDateArray[0]);
            int todayMonth = Integer.parseInt(todayDateArray[1]);
            int todayYear = Integer.parseInt(todayDateArray[2]);
            //compare input date is in the range of 01/01/1900 and today date
            if (year >= 1900 && year <= todayYear && month >= 1 && month <= 12 && day >= 1 && day <= 31) {
                if (year == todayYear) {
                    if (month == todayMonth) {
                        if (day <= todayDay) {
                            return true;
                        }
                    } else if (month < todayMonth) {
                        return true;
                    }
                } else if (year < todayYear) {
                    return true;
                }
            }
        }
        return false;
    }

    //a check_rating_format_correct function that inputs a float rating
    // and check whether it is in the range of 0 to 10 (inclusive).
    // if correct, return true, else return false.
    private static boolean check_rating_format_correct(float rating) {
        if (rating >= 0 && rating <= 10) {
            return true;
        }
        return false;
    }

    
    private static String enter_user_id(Connection con){
        String userID;
        boolean userIDErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try{
                System.out.print("Enter the user ID: ");
                userID = inputScanner.next();
                userIDErrorFlag = check_userID_available(userID, con);
                if (!userIDErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " User ID \""+userID+"\" does not exist.");
                }
            }finally{
                //inputScanner.close();
            }
        } while (!userIDErrorFlag);
        return userID;
    }

    private static String enter_call_number(Connection con){
        String callNumber;
        boolean callNumberErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try{
                System.out.print("Enter the call number: ");
                callNumber = inputScanner.next();
                callNumberErrorFlag = check_callNumber_available(callNumber, con);
                if (!callNumberErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Call number \""+callNumber+"\" does not exist.");
                }
            }finally{
                //inputScanner.close();
            }
        } while (!callNumberErrorFlag);
        return callNumber;
    }

    private static int enter_copy_number(String callNumber, Connection con){
        int copyNumber;
        boolean copyNumberErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try{
                System.out.print("Enter the copy number: ");
                copyNumber = inputScanner.nextInt();
                copyNumberErrorFlag = check_copyNumber_available(callNumber, copyNumber, con);
                if (!copyNumberErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Copy number \""+copyNumber+"\" does not exist.");
                }
            }finally{
                //inputScanner.close();
            }
        } while (!copyNumberErrorFlag);
        return copyNumber;
    }

    private static float enter_rating(){
        float rating;
        boolean ratingErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try{
                System.out.print("Enter the rating (0-10): ");
                rating = inputScanner.nextInt();
                ratingErrorFlag = check_rating_format_correct(rating);
                if (!ratingErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Expected input a value between 0 and 10!");
                }
            }finally{
                //inputScanner.close();
            }
        } while (!ratingErrorFlag);
        return rating;
    }

    private static String enter_start_date(){
        String startDate;
        boolean startDateErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try{
                System.out.print("Type in the starting date [dd/mm/yyyy]: ");
                startDate = inputScanner.next();
                startDateErrorFlag = check_date_format_correct(startDate);
                if (!startDateErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Please enter a date before or on today ("+ Utility.get_today_date() + ")");
                }
            }
            finally{
                //inputScanner.close();
            }
        }while(!startDateErrorFlag);
        return Utility.parse_date_format(startDate);
    }

    private static String enter_end_date() {
        String endDate;
        boolean endDateErrorFlag;
        do {
            Scanner inputScanner = new Scanner(System.in);
            try {
                System.out.print("Type in the ending date [dd/mm/yyyy]: ");
                endDate = inputScanner.next();
                endDateErrorFlag = check_date_format_correct(endDate);
                if (!endDateErrorFlag) {
                    System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Please enter a date before or on today ("+ Utility.get_today_date() +")");
                }
            } finally {
                //inputScanner.close();
            }
        } while (!endDateErrorFlag);
        return Utility.parse_date_format(endDate);
    }

    // check if a book copy is already borrowed
    // if the copy is currently available (i.e. not borrowed), return true
    // otherwise, return false
    private static boolean book_status(String callNumber, int copyNumber, Connection con) {
        String sql = "SELECT * FROM borrow WHERE callnum = (?) and copynum = (?) and borrow.return is not NULL";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            pstmt.setInt(2, copyNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    // a borrow_book function that inputs string userID, string callNumber, int copyNumber, string todayDate through Connection con and insert 
    // into the table borrow with field libuid, callnum, copynum, checkout, and leave field return null
    private static void borrow_book(String userID, String callNumber, int copyNumber, String todayDate, Connection con) {
        String sql = "INSERT INTO borrow (libuid, callnum, copynum, checkout) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, callNumber);
            pstmt.setInt(3, copyNumber);
            pstmt.setDate(4, java.sql.Date.valueOf(todayDate));
            pstmt.executeUpdate();
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET +" Book "+Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD
                + "borrowing" + Utility.ANSI_BOLD_RESET
                + Utility.ANSI_UNDERLINE_RESET +" performed successfully.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // an update_book_time_borrowed function that inputs string callNumber through Connection con and 
    // increases the value tborrowed by 1 in the table book where callnum is equal to callNumber
    private static void update_book_time_borrowed(String callNumber, Connection con) {
        String sql = "UPDATE book SET tborrowed = tborrowed + 1 WHERE callnum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // a userID_callNumber_copyNumber_exists function that inputs string userID, string callNumber, int copyNumber through Connection con and get number of rows
    //  this unique combination of userID, callNumber, and copyNumber is available in the table borrow with fields libuid, callnum, copynum
    //  if the number of rows is greater than 0, return true, otherwise return false
    private static boolean _userID_callNumber_copyNumber_exists(String userID, String callNumber, int copyNumber, Connection con) {
        String sql = "SELECT * FROM borrow WHERE libuid = (?) AND callnum = (?) AND copynum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, callNumber);
            pstmt.setInt(3, copyNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    // a return_book function that inputs string userID, string callNumber, int copyNumber, string todayDate through Connection con and update
    // the table borrow with field return = todayDate where libuid = userID and callnum = callNumber and copynum = copyNumber, and output error
    // if the userID, callNumber and copyNumber are not found in the table borrow
    private static void return_book(String userID, String callNumber, int copyNumber, String todayDate, Float bookRating, Connection con) {
        boolean userIDCallNumberCopyNumberExists = _userID_callNumber_copyNumber_exists(userID, callNumber, copyNumber, con);
        if (userIDCallNumberCopyNumberExists) {
            String sql = "UPDATE borrow SET borrow.return = (?) WHERE libuid = (?) AND callnum = (?) AND copynum = (?)";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setDate(1, java.sql.Date.valueOf(todayDate));
                pstmt.setString(2, userID);
                pstmt.setString(3, callNumber);
                pstmt.setInt(4, copyNumber);
                pstmt.executeUpdate();
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET +" Book "+Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD
                + "returning" + Utility.ANSI_BOLD_RESET
                + Utility.ANSI_UNDERLINE_RESET+ " performed successfully.");
                update_book_rating(callNumber, bookRating, con);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Record of user \""+userID +"\" borrowing book \""+ callNumber +"\" copy \"" + copyNumber +"\" is not found in the database.");
        }
    }

    //a float _get_book_rating function that inputs string callNumber through Connection con and get the rating of the book
    // from table book with field callnum and return the rating. If not found book, return -1.
    private static float _get_book_rating(String callNumber, Connection con) {
        String sql = "SELECT rating FROM book WHERE callnum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("rating");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    //an int _get_num_borrows function that inputs string callNumber through Connection con and get the number of borrows of the book
    // from table borrow with field callnum and return the number of borrows. If not found book, return -1.
    private static int _get_num_borrows(String callNumber, Connection con) {
        String sql = "SELECT tborrowed FROM book WHERE callnum = (?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, callNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    //an update_book_rating function that get float originalRating through _get_book_rating function, int numBorrows through _get_num_borrows function,
    // input string callNumber and float newRating through Connection con and update the table book with field rating = (originalRating * numBorrows + newRating) / (numBorrows + 1)
    // and output sql error if originalRating and numBorrows are -1.
    private static void update_book_rating(String callNumber, float newRating, Connection con) {
        float originalRating = _get_book_rating(callNumber, con);
        int numBorrows = _get_num_borrows(callNumber, con);
        if (originalRating != -1 && numBorrows != -1) {
            String sql = "UPDATE book SET rating = (?) WHERE callnum = (?)";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setFloat(1, (originalRating * numBorrows + newRating) / (numBorrows+1));
                pstmt.setString(2, callNumber);
                pstmt.executeUpdate();
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_GREEN + "[Success]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET +" Book rating "+Utility.ANSI_WHITE_UNDERLINED + Utility.ANSI_BOLD
                + "updated" + Utility.ANSI_BOLD_RESET
                + Utility.ANSI_UNDERLINE_RESET+" successfully.");
                update_book_time_borrowed(callNumber, con);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Cannot update the book's rating.");
        }
    }
    
    //a void get_all_unreturned_books function that get the string startDate and string endDate through connection con and
    // find all the rows with return is null and checkout (borrowed date) is between startDate and endDate,
    // print out all the rows with fields libuid, callnum, copynum, checkout with header
    // |LibUID|CallNum|CopyNum|Checkout|
    private static void get_all_unreturned_books(String startDate, String endDate, Connection con) {
        String sql = "SELECT * FROM borrow WHERE borrow.return IS NULL AND checkout BETWEEN (?) AND (?) ORDER BY borrow.checkout DESC";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("|LibUID|CallNum|CopyNum|Checkout|");
            while (rs.next()) {
                System.out.println("|" + rs.getString("libuid") + "|" + rs.getString("callnum") + "|" + rs.getInt("copynum") + "|" + rs.getString("checkout") + "|");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static void librarian_choice_1(Connection con) {
        boolean transactionErrorFlag = false;
        String userID = enter_user_id(con);
        String callNumber = enter_call_number(con);
        int copyNumber = enter_copy_number(callNumber, con);
        String todayDate = Utility.get_today_date_format();
        try{
            if(book_status(callNumber, copyNumber, con)){
                borrow_book(userID, callNumber, copyNumber, todayDate, con);
            }
            else {
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Book copy is not available.");
            }
        } catch (Exception e) {
            transactionErrorFlag = true;
            System.out.println(e);
        }
        if (transactionErrorFlag) {
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Book borrowing failed.");
        }
    }

    private static void librarian_choice_2(Connection con) {
        boolean transactionErrorFlag = false;
        String userID = enter_user_id(con);
        String callNumber = enter_call_number(con);
        int copyNumber = enter_copy_number(callNumber, con);
        float bookRating = enter_rating();
        String todayDate = Utility.get_today_date_format();
        try{
            if(!book_status(callNumber, copyNumber, con)) {
                return_book(userID, callNumber, copyNumber,todayDate, bookRating, con);
            } else {
                System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Book copy is already returned!");
            }
        }catch (Exception e){
            transactionErrorFlag = true;
            System.out.println(e);
        }
        if (transactionErrorFlag) {
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Book returning failed.");
        }
    }

    private static void librarian_choice_3(Connection con) {
        boolean transactionErrorFlag = false;
        String startDate = enter_start_date();
        String endDate = enter_end_date();
        try{
            get_all_unreturned_books(startDate, endDate, con);
        }
        catch (Exception e){
            transactionErrorFlag = true;
            System.out.println(e);
        }
        if (transactionErrorFlag) {
            System.out.println(Utility.ANSI_BOLD + Utility.ANSI_RED + "[Error]" + Utility.ANSI_RESET+ Utility.ANSI_BOLD_RESET + " Cannot fetch the unreturned books.");
        }
    }

    public static void main(Connection con) {
        boolean continueFlag = true;
        try {
            do{
                Scanner inputScanner = new Scanner(System.in);
                try{
                    initialization(Utility.choiceQuestionString, Utility.enterChoice);
                    String userOption = inputScanner.next();
                    if (Utility.choice_error_condition(userOption, Librarian.choiceNo)) {
                        do {
                            Utility.print_choice_selection_error_message(Librarian.choiceNo);
                            System.out.print(Utility.enterChoice);
                            userOption = inputScanner.next();
                        } while (Utility.choice_error_condition(userOption, Librarian.choiceNo));
                    }
                    continueFlag = do_operation(userOption, con);
                    TimeUnit.MILLISECONDS.sleep(Utility.GLOBAL_SLEEP_TIME);
                }
                finally{
                    //inputScanner.close();
                }
            }while(continueFlag);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
