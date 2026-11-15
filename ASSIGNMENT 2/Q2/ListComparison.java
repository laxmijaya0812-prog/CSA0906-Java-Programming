import java.util.*;

public class ListComparison {

    public static void main(String[] args) {

        // ArrayList
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Student1");
        arrayList.add("Student2");
        arrayList.add("Student3");

        System.out.println("ArrayList:");
        System.out.println(arrayList);
        System.out.println("Element at index 1: "
                + arrayList.get(1));

        // LinkedList
        LinkedList<String> linkedList = new LinkedList<>();

        linkedList.add("Student1");
        linkedList.add("Student2");
        linkedList.add("Student3");

        linkedList.addFirst("Student0");

        System.out.println("\nLinkedList:");
        System.out.println(linkedList);

        // ArrayDeque
        ArrayDeque<String> queue = new ArrayDeque<>();

        queue.add("Student1");
        queue.add("Student2");
        queue.add("Student3");

        queue.addFirst("Student0");
        queue.addLast("Student4");

        System.out.println("\nArrayDeque:");
        System.out.println(queue);

        System.out.println("Removed first: "
                + queue.removeFirst());

        System.out.println("Removed last: "
                + queue.removeLast());

        System.out.println("Final ArrayDeque: " + queue);
    }
}

Key Points

Collection

Main Advantage

ArrayList

Fast random access

LinkedList

Efficient insertion/removal at ends

ArrayDeque

Efficient queue/deque operations
