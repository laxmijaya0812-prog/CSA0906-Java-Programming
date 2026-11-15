import java.util.*;

public class PharmacyRedesign {

    public static void main(String[] args) {

        TreeMap<String, Integer> inventory =
                new TreeMap<>();

        inventory.put("VitaminC", 80);
        inventory.put("Paracetamol", 100);
        inventory.put("Amoxicillin", 50);
        inventory.put("Aspirin", 60);

        System.out.println("Pharmacy Inventory in Code Order:");

        for (Map.Entry<String, Integer> entry
                : inventory.entrySet()) {

            System.out.println(entry.getKey()
                    + " : " + entry.getValue());
        }

        System.out.println("\nStock of Paracetamol: "
                + inventory.get("Paracetamol"));
    }
}

Expected Output

Pharmacy Inventory in Code Order:
Amoxicillin : 50
Aspirin : 60
Paracetamol : 100
VitaminC : 80

Stock of Paracetamol: 100

Redesign Decision

The original HashMap approach provides fast average lookup but does not maintain sorted order.

If the pharmacy frequently requires reports in sorted order, TreeMap is a better choice because keys remain sorted automatically.
