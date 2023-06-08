package top.frnks.whiteboardjavafx;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerClientIO {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ServerClientIO(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }
}
