import java.util.*;

public class PharmacyInventory {

    public static void main(String[] args) {

        HashMap<String, Integer> inventory = new HashMap<>();

        inventory.put("Paracetamol", 100);
        inventory.put("Amoxicillin", 50);
        inventory.put("VitaminC", 80);

        System.out.println("Pharmacy Inventory:");

        for (Map.Entry<String, Integer> entry
                : inventory.entrySet()) {

            System.out.println(entry.getKey()
                    + " : " + entry.getValue());
        }

        System.out.println("\nParacetamol Stock: "
                + inventory.get("Paracetamol"));
    }
}

Map Comparison

Map

Ordering

Average Lookup

HashMap

No guaranteed order

O(1)

TreeMap

Sorted by key

O(log n)

LinkedHashMap

Insertion order

O(1)

Decision

HashMap is suitable when fast lookup is the main requirement.

