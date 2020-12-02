
public class Student02 {
    private static volatile Student02 student = null;
    private Student02() {

    }

    public static Student02 getInstance() {
        if (student == null) {
            synchronized (Student02.class) {
                if (student == null) {
                    student = new Student02();
                }
            }
        }

        return student;
    }
}
