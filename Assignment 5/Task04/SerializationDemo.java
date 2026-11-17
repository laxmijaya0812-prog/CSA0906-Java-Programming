import java.io.*;
import java.util.*;

class Resident implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String registerNo;
    private String password;

    // Live database resources should not be serialized.
    private transient ConnectionPlaceholder connection;

    Resident(String name, String registerNo, String password) {
        this.name = name;
        this.registerNo = registerNo;
        this.password = password;
    }

    public String toString() {
        return name + " - " + registerNo;
    }
}

class ConnectionPlaceholder implements Serializable {
    private static final long serialVersionUID = 1L;
}

class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    List<Resident> residents = new ArrayList<>();
}

class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;
    List<Room> rooms = new ArrayList<>();
}

public class SerializationDemo {
    public static void main(String[] args) throws Exception {
        Hostel hostel = new Hostel();
        Room room = new Room();
        room.residents.add(new Resident("Arun", "192311001", "hashedPassword"));
        hostel.rooms.add(room);

        try (ObjectOutputStream out =
                 new ObjectOutputStream(new FileOutputStream("hostel.ser"))) {
            out.writeObject(hostel);
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(new FileInputStream("hostel.ser"))) {
            Hostel restored = (Hostel) in.readObject();
            System.out.println("Residents restored: " +
                    restored.rooms.get(0).residents.size());
        }
    }
}
