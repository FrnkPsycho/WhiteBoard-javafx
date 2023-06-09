package top.frnks.whiteboardjavafx.common;

import java.io.Serializable;

public class FileContent implements Serializable {
    private byte[] content;
    private String fileName;

    public FileContent(byte[] content, String name) {
        this.content = content;
    }
    public byte[] getContent() {
        return content;
    }
    public String getFileName() {
        return fileName;
    }
}
