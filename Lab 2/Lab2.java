import java.util.Scanner;

public class Lab2 {
    static Scanner sc = new Scanner(System.in);

    class Student {
        public String name;
        public int rollNo;
        private String email;
        private String classs;
        private String school;

        void collect_details() {
            System.out.print("Enter student's Name: ");
            name = sc.next();
            System.out.print("Enter student's rollNo: ");
            rollNo = sc.nextInt();
            System.out.print("Enter student's Email: ");
            email = sc.next();
            System.out.print("Enter student's Class: ");
            classs = sc.next();
            System.out.print("Enter student's School: ");
            school = sc.next();
        }

        public String getEmail() {
            return email;
        }

        public String getClasss() {
            return classs;
        }

        public String getSchool() {
            return school;
        }

        void show_details() {
            System.out.println("The name of the Student: " + name);
            System.out.println("The Roll Number of the Student: " + rollNo);
        }

        void setEmail(String e) {
            this.email = e;
        }

        void setClass(String c) {
            this.classs = c;
        }

        void setSchool(String s) {
            this.school = s;
        }

        void clearEv() {
            name = null;
            rollNo = 0;
            email = null;
            classs = null;
            school = null;
        }
    }

    public static int len(Student[] studentArray) {
        int count = 0;
        for (Student s : studentArray) {
            if (s != null) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Student[] s = new Student[5];
        int n;
        while (true) {
            System.out.println("\n----Select the required option----");
            System.out.println("1. Add Details");
            System.out.println("2. View Details");
            System.out.println("3. Search");
            System.out.println("4. Update Details");
            System.out.println("5. Clear Memory");
            System.out.println("6. Exit");

            n = sc.nextInt();

            switch (n) {
                case 1:
                    int x = len(s);
                    if (x >= 5) {
                        System.out.println("All student detials are entered!!!");
                    } else {
                        for (int i = x; i < 5; i++) {
                            s[i].collect_details();
                        }
                    }
                    break;

                case 2:
                    if (len(s) == 0) {
                        System.out.println("Nothing to show!");
                    } else {
                        for (int i = 0; i < len(s); i++) {
                            s[i].show_details();
                            System.out.println("The Email of the student: " + s[i].getEmail());
                            System.out.println("The Class of the student: " + s[i].getClasss());
                            System.out.println("The School of the student: " + s[i].getSchool());
                        }
                    }
                    break;
                case 3:
                    if (len(s) == 0) {
                        System.out.println("There is nothing to search");
                    } else {
                        System.out.print("Enter the Roll Number of the student you want to search: ");
                        int roll = sc.nextInt();
                        for (int i = 0; i < len(s); i++) {
                            if (s[i].rollNo == roll) {
                                s[i].show_details();
                                System.out.println("The Email of the student: " + s[i].getEmail());
                                System.out.println("The Class of the student: " + s[i].getClasss());
                                System.out.println("The School of the student: " + s[i].getSchool());
                            }
                        }
                    }
                    break;
                case 4:
                    if (len(s) == 0) {
                        System.out.println("There is nothing to edit!!. PLease fill the details");
                    } else {
                        System.out.print("Enter the Roll Number of the student you want to Edit: ");
                        int roll = sc.nextInt();
                        int studentFound = -1;
                        for (int i = 0; i < len(s); i++) {
                            if (s[i].rollNo == roll) {
                                studentFound = i;
                            }
                        }
                        if (studentFound == -1) {
                            System.out.println("No such roll number found.");
                        } else {
                            while (true) {
                                System.out.println("---Enter the option you want to edit---");
                                System.out.println("1. Email");
                                System.out.println("2. Class");
                                System.out.println("3. School");
                                System.out.print("4. Exit");

                                int choice = sc.nextInt();

                                switch (choice) {
                                    case 1:
                                        System.out.print("Enter the new Email: ");
                                        String newEmail = sc.next();
                                        s[studentFound].setEmail(newEmail);
                                        System.out.println("Email updated Successfully!");
                                        break;
                                    case 2:
                                        System.out.print("Enter the new Class: ");
                                        String newclass = sc.next();
                                        s[studentFound].setClass(newclass);
                                        System.out.println("Email updated Successfully!");
                                        break;
                                    case 3:
                                        System.out.print("Enter the new School: ");
                                        String newSchool = sc.next();
                                        s[studentFound].setSchool(newSchool);
                                        System.out.println("Email updated Successfully!");
                                        break;
                                    case 4:
                                        return;
                                    default:
                                        System.out.println("Invalid Option");
                                }

                            }
                        }
                        break;
                    }
                case 5:
                    for (int i = 0; i < len(s); i++) {
                        s[i].clearEv();
                    }
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }

    }

}
