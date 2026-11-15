public class StringComparison {

    public static void main(String[] args) {

        int tickets = 10000;
        int fields = 50;

        // Using String
        long startString = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            String ticket = "";
            for (int j = 0; j < fields; j++) {
                ticket += "Field" + j + " ";
            }
        }

        long endString = System.nanoTime();

        // Using StringBuilder
        long startBuilder = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            StringBuilder ticket = new StringBuilder();

            for (int j = 0; j < fields; j++) {
                ticket.append("Field").append(j).append(" ");
            }
        }

        long endBuilder = System.nanoTime();

        // Using StringBuffer
        long startBuffer = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            StringBuffer ticket = new StringBuffer();

            for (int j = 0; j < fields; j++) {
                ticket.append("Field").append(j).append(" ");
            }
        }

        long endBuffer = System.nanoTime();

        System.out.println("String Time       : "
                + (endString - startString) + " ns");

        System.out.println("StringBuilder Time: "
                + (endBuilder - startBuilder) + " ns");

        System.out.println("StringBuffer Time : "
                + (endBuffer - startBuffer) + " ns");
    }
}

Key Points

String is immutable.

StringBuilder is mutable and suitable for single-threaded applications.

StringBuffer is mutable and synchronized.

StringBuilder generally provides better performance than StringBuffer.

Repeated concatenation using String can create many intermediate objects.
