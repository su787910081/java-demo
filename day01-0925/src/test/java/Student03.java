public class Student03 {
    private Student03() {

    }

    private static class InnerStudent {
        static Student03 INSTANCE = new Student03();
    }

    public static Student03 getInstance() {
        return InnerStudent.INSTANCE;
    }
}
