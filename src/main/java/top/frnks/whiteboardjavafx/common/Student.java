package top.frnks.whiteboardjavafx.common;

import java.io.Serializable;

public class Student implements Serializable {
    public String name;
    public long id;
    public Student(String name, long id) {
        this.name = name;
        this.id = id;
    }
    public String getDisplayName() {
        return name + " <" + id + ">";
    }
}
