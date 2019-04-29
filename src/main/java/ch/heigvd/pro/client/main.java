package ch.heigvd.pro.client;

import ch.heigvd.pro.client.file.FileDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        FileDriver test = new FileDriver();
        File passwordDB = new File("");
        Safe safeTest = new Safe();

        while (true) {
            System.out.println("1-Load another file");
            System.out.println("2-Create an entry");
            System.out.println("3-Modify an entry");
            System.out.println("4-Show entries");
            System.out.println("5-Save");
            System.out.println("6-Load");
            System.out.println("7-Exit");

            Scanner menuInput = new Scanner(System.in);

            switch (menuInput.nextInt()) {
                case 1:
                    break;
                case 2:
                    Scanner userInput = new Scanner(System.in);

                    System.out.println("Enter a username :");
                    char[] username = userInput.nextLine().toCharArray();

                    System.out.println("Enter a target :");
                    char[] target = userInput.nextLine().toCharArray();

                    System.out.println("Enter an email :");
                    char[] email = userInput.nextLine().toCharArray();

                    System.out.println("Enter a password :");
                    char[] clearPassword = userInput.nextLine().toCharArray();

                    Entry newEntry = new Entry(username, target, clearPassword, email, new Date());

                    safeTest.addEntry(newEntry);
                    safeTest.encryptPassword();

                    break;
                case 3:
                    break;
                case 4:
                    for (Entry e : safeTest.getEntryList()) {
                        System.out.println("======= ENTRY =======");
                        System.out.println("Username : " + e.getUsername());
                        System.out.println("Target : " + e.getTarget());
                        System.out.println("Email : " + e.getEmail());

                        System.out.print("Password : ");
                        for (char c : e.getClearPassword()) {
                            System.out.print(c);
                        }

                        System.out.println();
                        System.out.println();
                    }
                    break;
                case 5:
                    test.saveSafe(safeTest, passwordDB);
                    break;
                case 6:
                    System.out.println("Enter the name of your password file :");

                    Scanner filenameInput = new Scanner(System.in);
                    String filename = filenameInput.nextLine();
                    passwordDB = new File(filename + ".json");
                    passwordDB.createNewFile();

                    safeTest = test.loadSafe(passwordDB);

                    System.out.println("Enter your password :");
                    Scanner passwordInput = new Scanner(System.in);
                    safeTest.setSafePassword(passwordInput.nextLine().toCharArray());

                    safeTest.decryptPassword();

                    break;
                case 7:
                    return;
                default:
                    break;
            }

        }

    }


}