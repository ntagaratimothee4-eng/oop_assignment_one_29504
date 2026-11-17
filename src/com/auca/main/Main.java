package com.auca.main;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import com.auca.administrator.Administrator;
import com.auca.department.Department;
import com.auca.course.Course;
import com.auca.classroom.Classroom;
import com.auca.enrollment.Enrollment;
import com.auca.assessment.Assessment;
import com.auca.feepayment.FeePayment;
import com.auca.librarybook.LibraryBook;
import com.auca.bookloan.BookLoan;
import com.auca.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> payments = new ArrayList<>();
    private static final List<LibraryBook> books = new ArrayList<>();
    private static final List<BookLoan> loans = new ArrayList<>();

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            choice = readInt("Select an option: ");
            try {
                switch (choice) {
                    case 1 -> registerDepartment();
                    case 2 -> registerStudent();
                    case 3 -> registerTeacher();
                    case 4 -> registerAdministrator();
                    case 5 -> registerCourse();
                    case 6 -> registerClassroom();
                    case 7 -> enrollStudent();
                    case 8 -> recordAssessment();
                    case 9 -> recordFeePayment();
                    case 10 -> registerLibraryBook();
                    case 11 -> borrowBook();
                    case 12 -> returnBook();
                    case 13 -> viewAllRecords();
                    case 14 -> searchRecord();
                    case 15 -> updateRecord();
                    case 16 -> studentAcademicSummary();
                    case 17 -> studentPaymentHistory();
                    case 18 -> viewAvailableBooks();
                    case 19 -> demonstratePolymorphism();
                    case 0 -> System.out.println("Thank you for using AUCA_ID.");
                    default -> System.out.println("Invalid option. Choose between 0 and 19.");
                }
            } catch (ValidationException | DuplicateRecordException |
                     RecordNotFoundException | BookUnavailableException e) {
                System.out.println("\n!!! Operation failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n!!! Unexpected error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    private static void displayMenu() {
        System.out.println("\n========== AUCA_ID SCHOOL MANAGEMENT ==========");
        System.out.println("1. Register Department       11. Borrow Library Book");
        System.out.println("2. Register Student          12. Return Library Book");
        System.out.println("3. Register Teacher          13. View All Records");
        System.out.println("4. Register Administrator    14. Search Record by ID");
        System.out.println("5. Register Course           15. Update Existing Record");
        System.out.println("6. Register Classroom        16. Student Academic Summary");
        System.out.println("7. Enroll Student            17. Student Payment History");
        System.out.println("8. Record Assessment         18. View Available Books");
        System.out.println("9. Record Fee Payment        19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book");
        System.out.println("0. Exit");
        System.out.println("===============================================");
    }



    private static String readString(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private static int readInt(String message) {
        while (true) {
            try { return Integer.parseInt(readString(message)); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            try { return Double.parseDouble(readString(message)); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid number."); }
        }
    }

    private static BigDecimal readAmount(String message) {
        while (true) {
            try { return new BigDecimal(readString(message)); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid amount."); }
        }
    }

    private static LocalDate readDate(String message) {
        while (true) {
            try { return LocalDate.parse(readString(message)); }
            catch (DateTimeParseException e) { System.out.println("Invalid date. Use YYYY-MM-DD."); }
        }
    }

    private static boolean readYesNo(String message) {
        while (true) {
            String answer = readString(message + " (yes/no): ");
            if (answer.equalsIgnoreCase("yes")) return true;
            if (answer.equalsIgnoreCase("no")) return false;
            System.out.println("Please enter yes or no.");
        }
    }

    private static void checkDuplicate(String id) throws DuplicateRecordException {
        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments); records.addAll(students); records.addAll(teachers);
        records.addAll(administrators); records.addAll(courses); records.addAll(classrooms);
        records.addAll(enrollments); records.addAll(assessments); records.addAll(payments);
        records.addAll(books); records.addAll(loans);

        for (Identifiable record : records) {
            if (record.getId().equalsIgnoreCase(id)) {
                throw new DuplicateRecordException("The ID " + id + " already exists.");
            }
        }
    }

    private static <T extends Identifiable> T selectRecord(List<T> records, String type) {
        System.out.println("\nSelect a " + type + ":");
        for (int i = 0; i < records.size(); i++) {
            System.out.println((i + 1) + ". " + records.get(i).getId() + " - " + records.get(i).getDisplayName());
        }
        System.out.println("0. Cancel");
        while (true) {
            int choice = readInt("Your choice: ");
            if (choice == 0) return null;
            if (choice >= 1 && choice <= records.size()) return records.get(choice - 1);
            System.out.println("Invalid selection.");
        }
    }

   
    private static boolean confirmSave(Registrable record) {
        System.out.println("\n--- REVIEW RECORD ---");
        record.printDetails(); // This prints all fields of the object!
        System.out.println("1. Save  0. Cancel");
        return readInt("Select: ") == 1;
    }



    private static void registerDepartment() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER DEPARTMENT ---");
        String id = readString("Department ID: ");
        checkDuplicate(id);
        String name = readString("Department name: ");
        String code = readString("Department code: ");
        String location = readString("Office location: ");
        String email = readString("Contact email: ");

        Teacher headTeacher = null;
        if (!teachers.isEmpty() && readYesNo("Assign a head teacher")) {
            headTeacher = selectRecord(teachers, "teacher");
            if (headTeacher == null) return;
        }

        Department department = new Department(id, name, code, location, email, headTeacher);
        department.validate();

        if (confirmSave(department)) {
            departments.add(department);
            System.out.println("Department registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerStudent() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER STUDENT ---");
        if (departments.isEmpty()) {
            System.out.println("No Department exists. Register a Department first.");
            System.out.println("1. Register Department now  0. Cancel");
            if (readInt("Select: ") == 1) { registerDepartment(); if (departments.isEmpty()) return; } else return;
        }

        String personId = readString("Person ID: ");
        checkDuplicate(personId);
        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        String email = readString("Email: ");
        String phone = readString("Phone number: ");
        LocalDate dob = readDate("Date of birth (YYYY-MM-DD): ");
        String memberNumber = readString("Member number: ");
        Department department = selectRecord(departments, "department");
        if (department == null) return;
        LocalDate joinDate = readDate("Join date (YYYY-MM-DD): ");
        String status = readString("Academic status (ACTIVE/INACTIVE): ");
        String institutionalEmail = readString("Institutional email: ");
        String advisorName = readString("Advisor name: ");
        String studentNumber = readString("Student number: ");
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(studentNumber)) throw new DuplicateRecordException("Student number already exists.");
        }
        String programName = readString("Program name: ");
        LocalDate admissionDate = readDate("Admission date (YYYY-MM-DD): ");
        int studyYear = readInt("Study year (1-6): ");
        String guardianPhone = readString("Guardian phone: ");
        boolean scholarship = readYesNo("Scholarship");

        Student student = new Student(personId, firstName, lastName, email, phone, dob,
                memberNumber, department, joinDate, status, institutionalEmail, advisorName,
                studentNumber, programName, admissionDate, studyYear, guardianPhone, scholarship);
        student.validate();

        if (confirmSave(student)) {
            students.add(student);
            System.out.println("Student registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerTeacher() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER TEACHER ---");
        if (departments.isEmpty()) {
            System.out.println("No Department exists. Register a Department first.");
            System.out.println("1. Register Department now  0. Cancel");
            if (readInt("Select: ") == 1) { registerDepartment(); if (departments.isEmpty()) return; } else return;
        }

        String personId = readString("Person ID: ");
        checkDuplicate(personId);
        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        String email = readString("Email: ");
        String phone = readString("Phone number: ");
        LocalDate dob = readDate("Date of birth (YYYY-MM-DD): ");
        String memberNumber = readString("Member number: ");
        Department department = selectRecord(departments, "department");
        if (department == null) return;
        LocalDate joinDate = readDate("Join date (YYYY-MM-DD): ");
        String status = readString("Academic status (ACTIVE/INACTIVE): ");
        String institutionalEmail = readString("Institutional email: ");
        String advisorName = readString("Advisor name: ");
        String employeeNumber = readString("Employee number: ");
        for (Teacher t : teachers) {
            if (t.getEmployeeNumber().equalsIgnoreCase(employeeNumber)) throw new DuplicateRecordException("Employee number already exists.");
        }
        String specialization = readString("Specialization: ");
        LocalDate hireDate = readDate("Hire date (YYYY-MM-DD): ");
        BigDecimal salary = readAmount("Salary: ");
        String officeNumber = readString("Office number: ");
        String employmentType = readString("Employment type: ");

        Teacher teacher = new Teacher(personId, firstName, lastName, email, phone, dob,
                memberNumber, department, joinDate, status, institutionalEmail, advisorName,
                employeeNumber, specialization, hireDate, salary, officeNumber, employmentType);
        teacher.validate();

        if (confirmSave(teacher)) {
            teachers.add(teacher);
            System.out.println("Teacher registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerAdministrator() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER ADMINISTRATOR ---");
        String id = readString("Person ID: ");
        checkDuplicate(id);
        String first = readString("First name: ");
        String last = readString("Last name: ");
        String email = readString("Email: ");
        String phone = readString("Phone number: ");
        LocalDate dob = readDate("Date of birth (YYYY-MM-DD): ");
        String staffNumber = readString("Staff number: ");
        String jobTitle = readString("Job title: ");
        String office = readString("Assigned office: ");
        LocalDate employmentDate = readDate("Employment date: ");
        int accessLevel = readInt("Access level: ");
        boolean active = readYesNo("Is administrator active");

        Administrator admin = new Administrator(id, first, last, email, phone, dob,
                staffNumber, jobTitle, office, employmentDate, accessLevel, active);
        admin.validate();

        if (confirmSave(admin)) {
            administrators.add(admin);
            System.out.println("Administrator registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerCourse() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER COURSE ---");
        if (departments.isEmpty() || teachers.isEmpty()) {
            System.out.println("Missing prerequisites. Need a Department and a Teacher.");
            if (departments.isEmpty()) {
                System.out.println("1. Register Department now  0. Cancel");
                if (readInt("Select: ") == 1) { registerDepartment(); if (departments.isEmpty()) return; } else return;
            }
            if (teachers.isEmpty()) {
                System.out.println("1. Register Teacher now  0. Cancel");
                if (readInt("Select: ") == 1) { registerTeacher(); if (teachers.isEmpty()) return; } else return;
            }
        }

        String id = readString("Course ID: ");
        checkDuplicate(id);
        String code = readString("Course code: ");
        String title = readString("Course title: ");
        int creditHours = readInt("Credit hours (1-6): ");
        Department department = selectRecord(departments, "department");
        if (department == null) return;
        Teacher teacher = selectRecord(teachers, "teacher");
        if (teacher == null) return;

        Course course = new Course(id, code, title, creditHours, department, teacher);
        course.validate();

        if (confirmSave(course)) {
            courses.add(course);
            System.out.println("Course registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerClassroom() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER CLASSROOM ---");
        String id = readString("Classroom ID: ");
        checkDuplicate(id);
        String building = readString("Building name: ");
        String room = readString("Room number: ");
        int capacity = readInt("Capacity: ");
        int floor = readInt("Floor number: ");
        boolean available = readYesNo("Is classroom available");

        Classroom classroom = new Classroom(id, building, room, capacity, floor, available);
        classroom.validate();

        if (confirmSave(classroom)) {
            classrooms.add(classroom);
            System.out.println("Classroom registered successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void enrollStudent() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- ENROLL STUDENT ---");
        if (students.isEmpty() || courses.isEmpty()) {
            System.out.println("Missing prerequisites. Need a Student and a Course.");
            if (students.isEmpty()) {
                System.out.println("1. Register Student now  0. Cancel");
                if (readInt("Select: ") == 1) { registerStudent(); if (students.isEmpty()) return; } else return;
            }
            if (courses.isEmpty()) {
                System.out.println("1. Register Course now  0. Cancel");
                if (readInt("Select: ") == 1) { registerCourse(); if (courses.isEmpty()) return; } else return;
            }
        }

        Student student = selectRecord(students, "student");
        if (student == null) return;
        Course course = selectRecord(courses, "course");
        if (course == null) return;
        String semester = readString("Semester: ");

        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student) && e.getCourse().equals(course) && e.getSemester().equalsIgnoreCase(semester) && e.isActive()) {
                throw new DuplicateRecordException("Student already actively enrolled in this course for this semester.");
            }
        }

        String id = readString("Enrollment ID: ");
        checkDuplicate(id);
        LocalDate date = readDate("Enrollment date (YYYY-MM-DD): ");
        Enrollment enrollment = new Enrollment(id, student, course, date, semester, "ACTIVE");
        enrollment.validate();

        if (confirmSave(enrollment)) {
            enrollments.add(enrollment);
            System.out.println("Enrollment completed successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void recordAssessment() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- RECORD ASSESSMENT ---");
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments exist. You must enroll a student first.");
            System.out.println("1. Enroll now  0. Cancel");
            if (readInt("Select: ") == 1) { enrollStudent(); if (enrollments.isEmpty()) return; } else return;
        }

        Enrollment enrollment = selectRecord(enrollments, "enrollment");
        if (enrollment == null) return;
        if (!enrollment.isActive()) throw new ValidationException("Only active enrollments can be assessed.");

        String id = readString("Assessment ID: ");
        checkDuplicate(id);
        String type = readString("Assessment type: ");
        double maximum = readDouble("Maximum score: ");
        double obtained = readDouble("Obtained score: ");
        LocalDate date = readDate("Assessment date (YYYY-MM-DD): ");

        Assessment assessment = new Assessment(id, enrollment, type, maximum, obtained, date);
        assessment.validate();

        if (confirmSave(assessment)) {
            assessments.add(assessment);
            System.out.println("Assessment recorded successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void recordFeePayment() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- RECORD FEE PAYMENT ---");
        if (students.isEmpty()) {
            System.out.println("No students exist. Register a student first.");
            System.out.println("1. Register Student now  0. Cancel");
            if (readInt("Select: ") == 1) { registerStudent(); if (students.isEmpty()) return; } else return;
        }

        Student student = selectRecord(students, "student");
        if (student == null) return;
        String id = readString("Payment ID: ");
        checkDuplicate(id);
        BigDecimal amount = readAmount("Payment amount: ");
        LocalDate date = readDate("Payment date (YYYY-MM-DD): ");
        String method = readString("Payment method: ");
        String receipt = readString("Receipt number: ");

        for (FeePayment p : payments) {
            if (p.getReceiptNumber().equalsIgnoreCase(receipt)) throw new DuplicateRecordException("This receipt number already exists.");
        }

        FeePayment payment = new FeePayment(id, student, amount, date, method, receipt);
        payment.validate();

        if (confirmSave(payment)) {
            payments.add(payment);
            System.out.println("Payment recorded successfully.");
        } else System.out.println("Cancelled.");
    }

    private static void registerLibraryBook() throws ValidationException, DuplicateRecordException {
        System.out.println("\n--- REGISTER LIBRARY BOOK ---");
        String id = readString("Book ID: ");
        checkDuplicate(id);
        String isbn = readString("ISBN: ");
        String title = readString("Book title: ");
        String author = readString("Author: ");
        int total = readInt("Total copies: ");
        int available = readInt("Available copies: ");

        LibraryBook book = new LibraryBook(id, isbn, title, author, total, available);
        book.validate();

        if (confirmSave(book)) {
            books.add(book);
            System.out.println("Book registered successfully.");
        } else System.out.println("Cancelled.");
    }

    // ======================== LIBRARY OPERATIONS ========================

    private static void borrowBook() throws ValidationException, DuplicateRecordException, BookUnavailableException {
        System.out.println("\n--- BORROW LIBRARY BOOK ---");
        if (students.isEmpty() || books.isEmpty()) {
            System.out.println("Missing prerequisites. Need a Student and a Library Book.");
            if (students.isEmpty()) {
                System.out.println("1. Register Student now  0. Cancel");
                if (readInt("Select: ") == 1) { registerStudent(); if (students.isEmpty()) return; } else return;
            }
            if (books.isEmpty()) {
                System.out.println("1. Register Library Book now  0. Cancel");
                if (readInt("Select: ") == 1) { registerLibraryBook(); if (books.isEmpty()) return; } else return;
            }
        }

        Student student = selectRecord(students, "student");
        if (student == null) return;
        LibraryBook book = selectRecord(books, "book");
        if (book == null) return;
        if (book.getAvailableCopies() <= 0) throw new BookUnavailableException("There are no available copies of this book.");

        String id = readString("Loan ID: ");
        checkDuplicate(id);
        LocalDate borrowed = readDate("Borrowed date (YYYY-MM-DD): ");
        LocalDate due = readDate("Due date (YYYY-MM-DD): ");

        BookLoan loan = new BookLoan(id, student, book, borrowed, due, null);
        loan.validate();

        // Change stock only after successful validation.
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        loans.add(loan);
        System.out.println("Book borrowed successfully.");
    }

    private static void returnBook() throws ValidationException, RecordNotFoundException {
        System.out.println("\n--- RETURN LIBRARY BOOK ---");
        List<BookLoan> activeLoans = new ArrayList<>();
        for (BookLoan loan : loans) {
            if (loan.isActive()) activeLoans.add(loan);
        }

        if (activeLoans.isEmpty()) throw new RecordNotFoundException("There are no active book loans.");
        BookLoan loan = selectRecord(activeLoans, "active loan");
        if (loan == null) return;

        LocalDate returned = readDate("Return date (YYYY-MM-DD): ");
        if (returned.isBefore(loan.getBorrowedDate())) throw new ValidationException("Return date cannot be before the borrowed date.");

        loan.setReturnedDate(returned);
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        System.out.println("Book returned successfully.");
    }


    private static void viewAllRecords() {
        System.out.println("\n========== ALL RECORDS ==========");
        printList("DEPARTMENTS", departments);
        printList("STUDENTS", students);
        printList("TEACHERS", teachers);
        printList("ADMINISTRATORS", administrators);
        printList("COURSES", courses);
        printList("CLASSROOMS", classrooms);
        printList("ENROLLMENTS", enrollments);
        printList("ASSESSMENTS", assessments);
        printList("FEE PAYMENTS", payments);
        printList("LIBRARY BOOKS", books);
        printList("BOOK LOANS", loans);
    }

    private static <T> void printList(String title, List<T> records) {
        System.out.println("\n--- " + title + " ---");
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (T record : records) System.out.println(record);
    }

    private static void searchRecord() throws RecordNotFoundException {
        String id = readString("Enter record ID: ");
        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments); records.addAll(students); records.addAll(teachers);
        records.addAll(administrators); records.addAll(courses); records.addAll(classrooms);
        records.addAll(enrollments); records.addAll(assessments); records.addAll(payments);
        records.addAll(books); records.addAll(loans);

        for (Identifiable record : records) {
            if (record.getId().equalsIgnoreCase(id)) {
                System.out.println("Record found:");
                System.out.println(record);
                return;
            }
        }
        throw new RecordNotFoundException("No record found with ID: " + id);
    }

    private static void updateRecord() throws RecordNotFoundException, ValidationException {
        String id = readString("Enter the ID of the record to update: ");
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                System.out.println(student);
                String newStatus = readString("Enter new academic status (ACTIVE/INACTIVE): ");
                student.setAcademicStatus(newStatus);
                student.validate();
                System.out.println("Student updated successfully.");
                return;
            }
        }
        for (Classroom classroom : classrooms) {
            if (classroom.getId().equalsIgnoreCase(id)) {
                System.out.println(classroom);
                boolean available = readYesNo("Set classroom as available");
                classroom.setAvailable(available);
                classroom.validate();
                System.out.println("Classroom updated successfully.");
                return;
            }
        }
        throw new RecordNotFoundException("No editable student or classroom found with ID: " + id);
    }

    private static void studentAcademicSummary() {
        System.out.println("\n--- STUDENT ACADEMIC SUMMARY ---");
        if (students.isEmpty()) { System.out.println("No students registered yet."); return; }
        Student student = selectRecord(students, "student");
        if (student == null) return;

        student.printDetails();
        System.out.println("\nEnrollments:");
        boolean found = false;
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student)) { e.printDetails(); found = true; }
        }
        if (!found) System.out.println("No enrollments found.");

        System.out.println("\nAssessments:");
        found = false;
        for (Assessment a : assessments) {
            if (a.getEnrollment().getStudent().equals(student)) { a.printDetails(); found = true; }
        }
        if (!found) System.out.println("No assessments found.");
    }

    private static void studentPaymentHistory() {
        System.out.println("\n--- PAYMENT HISTORY ---");
        if (students.isEmpty()) { System.out.println("No students registered yet."); return; }
        Student student = selectRecord(students, "student");
        if (student == null) return;

        boolean found = false;
        BigDecimal total = BigDecimal.ZERO;
        for (FeePayment payment : payments) {
            if (payment.getStudent().equals(student)) {
                payment.printDetails();
                total = total.add(payment.getAmount());
                found = true;
            }
        }
        if (!found) System.out.println("No payments found for this student.");
        else System.out.println("Total paid: " + total);
    }

    private static void viewAvailableBooks() {
        System.out.println("\n--- AVAILABLE BOOKS ---");
        boolean found = false;
        for (LibraryBook book : books) {
            if (book.getAvailableCopies() > 0) { book.printDetails(); found = true; }
        }
        if (!found) System.out.println("No books are currently available.");
    }

    private static void demonstratePolymorphism() {
        System.out.println("\n========== POLYMORPHISM DEMONSTRATION ==========");
        List<com.auca.abstracts.Person> people = new ArrayList<>();
        people.addAll(students); people.addAll(teachers); people.addAll(administrators);

        System.out.println("\nSuperclass (Person) polymorphism:");
        if (people.isEmpty()) System.out.println("No people registered yet.");
        for (com.auca.abstracts.Person person : people) {
            System.out.println(person.getRoleDescription() + " -> " + person.getDisplayName());
        }

        System.out.println("\nInterface (Identifiable) polymorphism:");
        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments); records.addAll(students); records.addAll(teachers);
        records.addAll(courses); records.addAll(classrooms); records.addAll(books);
        for (Identifiable record : records) {
            System.out.println(record.getId() + " | " + record.getDisplayName() + " | Active: " + record.isActive());
        }
    }
}