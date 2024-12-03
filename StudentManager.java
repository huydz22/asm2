import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

class Student {
    private String id;
    private String name;
    private double marks;

    // Constructor khởi tạo đối tượng Student
    public Student(String id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // Phương thức trả về rank (hạng) của sinh viên dựa trên điểm
    public String getRank() {
        if (marks >= 9.0) return "Excellent";
        if (marks >= 7.5) return "Very Good";
        if (marks >= 6.5) return "Good";
        if (marks >= 5.0) return "Medium";
        return "Fail";
    }

    // Override phương thức toString để hiển thị thông tin sinh viên
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Marks: %.2f, Rank: %s", id, name, marks, getRank());
    }
}

public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Stack<Student> historyStack = new Stack<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // In menu cho người dùng lựa chọn hành động
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Undo Last Action");
            System.out.println("5. Sort Students by Marks");
            System.out.println("6. Search Student by ID");
            System.out.println("7. Display All Students");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addStudent();  // Thêm sinh viên
                case 2 -> editStudent(); // Chỉnh sửa thông tin sinh viên
                case 3 -> deleteStudent(); // Xóa sinh viên
                case 4 -> undoLastAction(); // Hoàn tác hành động gần nhất
                case 5 -> sortStudents(); // Sắp xếp sinh viên theo điểm
                case 6 -> searchStudent(); // Tìm kiếm sinh viên theo ID
                case 7 -> displayStudents(); // Hiển thị tất cả sinh viên
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Thêm sinh viên mới vào danh sách
    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Marks: ");
        double marks = Double.parseDouble(scanner.nextLine());

        Student student = new Student(id, name, marks);
        students.add(student);  // Thêm sinh viên vào danh sách
        historyStack.push(student); // Lưu sinh viên vừa thêm vào lịch sử để hoàn tác nếu cần
        System.out.println("Student added successfully.");
    }

    // Chỉnh sửa thông tin sinh viên
    private static void editStudent() {
        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                historyStack.push(new Student(student.getId(), student.getName(), student.getMarks())); // Lưu thông tin cũ của sinh viên
                System.out.print("Enter new Student Name: ");
                student.setName(scanner.nextLine());
                System.out.print("Enter new Student Marks: ");
                student.setMarks(Double.parseDouble(scanner.nextLine()));
                System.out.println("Student updated successfully.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    // Xóa sinh viên theo ID
    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                historyStack.push(student); // Lưu sinh viên đã xóa vào lịch sử để hoàn tác nếu cần
                students.remove(student);  // Xóa sinh viên khỏi danh sách
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    // Hoàn tác hành động gần nhất (thêm, xóa hoặc sửa)
    private static void undoLastAction() {
        if (!historyStack.isEmpty()) {
            Student lastAction = historyStack.pop(); // Lấy hành động gần nhất trong lịch sử
            if (!students.contains(lastAction)) {
                students.add(lastAction); // Nếu là hành động xóa, phục hồi sinh viên
                System.out.println("Undo successful: Restored " + lastAction);
            } else {
                students.remove(lastAction); // Nếu là hành động thêm, xóa sinh viên
                System.out.println("Undo successful: Removed " + lastAction);
            }
        } else {
            System.out.println("No actions to undo.");
        }
    }

    // Sắp xếp sinh viên theo điểm bằng thuật toán Merge Sort
    private static void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to sort.");
            return;
        }
        students = mergeSort(students); // Gọi phương thức Merge Sort để sắp xếp
        System.out.println("Students sorted by marks.");
    }

    // Thuật toán Merge Sort để sắp xếp sinh viên theo điểm
    private static ArrayList<Student> mergeSort(ArrayList<Student> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<Student> left = new ArrayList<>(list.subList(0, mid));  // Chia danh sách thành hai phần
        ArrayList<Student> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);  // Sắp xếp phần bên trái
        right = mergeSort(right);  // Sắp xếp phần bên phải

        return merge(left, right);  // Kết hợp hai phần đã sắp xếp
    }

    // Hàm kết hợp hai danh sách đã sắp xếp
    private static ArrayList<Student> merge(ArrayList<Student> left, ArrayList<Student> right) {
        ArrayList<Student> merged = new ArrayList<>();
        int i = 0, j = 0;

        // So sánh và kết hợp hai danh sách đã sắp xếp
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getMarks() >= right.get(j).getMarks()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        // Thêm phần còn lại của danh sách vào kết quả
        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        return merged;
    }

    // Tìm kiếm sinh viên theo ID
    private static void searchStudent() {
        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                System.out.println(student);  // Hiển thị thông tin sinh viên nếu tìm thấy
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // Hiển thị tất cả sinh viên
    private static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);  // In thông tin tất cả sinh viên
        }
    }
}
