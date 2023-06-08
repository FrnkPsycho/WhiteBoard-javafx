package top.frnks.whiteboardjavafx.common;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private Student fromStudent;
    private String content;
    private LocalDateTime sendTime;

    public Student getFromStudent() {
        return fromStudent;
    }

    public void setFromStudent(Student fromStudent) {
        this.fromStudent = fromStudent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
