import java.util.Scanner;

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
    private static boolean do_operation(String userOption){
        if(userOption.equals("1")){
            Administrator.main();
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
                continueFlag = do_operation(userOption);
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

    private static boolean do_operation(String userOption) {
        if (userOption.equals("1")) {
            administrator_choice_1();
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
    private static void administrator_choice_1(){
        ;
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

    public static void main() {
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
                continueFlag = do_operation(userOption);
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
    
    private static void do_operation(String userOption) {
        if (userOption.equals("1")) {
            ;
        } else if (userOption.equals("2")) {
            ;
        } else {
            return;
        }
    }
    public static void main(){
        try {
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
            do_operation(userOption);
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

    private static void do_operation(String userOption) {
        if (userOption.equals("1")) {
            ;
        } else if (userOption.equals("2")) {
            ;
        } else if (userOption.equals("3")) {
            ;
        } else {
            return;
        }
    }
    public static void main() {
        try {
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
            do_operation(userOption);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
