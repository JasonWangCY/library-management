import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

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
            // Statement myStatement = con.createStatement();


            //insertion query
            // String sqlQuery1 = "create table test (test_id integer);";
            // myStatement.executeUpdate(sqlQuery1);

            // System.out.println("Creation Completed");
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }
}

class CreateTable{
    public static void create_table(Connection con, String query){
        try{
            Statement myStatement = con.createStatement();

            myStatement.executeUpdate(query);
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
            LibraryUser.main();
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
            administrator_choice_2();
            return true;
        } else if (userOption.equals("3")) {
            administrator_choice_3();
            return true;
        } else if (userOption.equals("4")) {
            administrator_choice_4();
            return true;
        }else {
            return false;
        }
    }
    private static void administrator_choice_1(Connection con){
        CreateTable.create_table(con, Schema.create_user_category);
        CreateTable.create_table(con, Schema.create_lib_user);
        CreateTable.create_table(con, Schema.create_book_category);
        CreateTable.create_table(con, Schema.create_book);
        CreateTable.create_table(con, Schema.create_authorship);
        CreateTable.create_table(con, Schema.create_copy);
        CreateTable.create_table(con, Schema.create_borrow);
        System.out.println("Processing...Done. Database is initialized.");
    }
    
    private static void administrator_choice_2() {
        ;
        System.out.println("Processing...Done. Database is removed.");
    }

    private static void administrator_choice_3() {
        ;
        System.out.println("Processing...Done. Database is inputted to the database.");
    }

    private static void administrator_choice_4() {
        ;
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
    
    private static boolean do_operation(String userOption) {
        if (userOption.equals("1")) {
            ;
            return true;
        } else if (userOption.equals("2")) {
            ;
            return true;
        } else {
            return false;
        }
    }
    public static void main(){
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
                continueFlag = do_operation(userOption);
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
