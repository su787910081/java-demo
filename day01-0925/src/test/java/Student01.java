

public class Student01 {
    private static volatile Student01 student = null;
    private Student01() {
    }

    public static synchronized Student01 getInstance() {
        if (student == null) {
            student = new Student01();
        }
        return student;
    }
}
