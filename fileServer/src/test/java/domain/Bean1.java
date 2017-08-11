package domain;

/**
 * Created by scream on 2017/8/3.
 */
public class Bean1 {
    private String id;
    private Student student;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "id='" + id + '\'' +
                ", student=" + student +
                '}';
    }
}
