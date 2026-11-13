public class Main {

    public static void main(String[] args) {

        EBike bike =
                new EBike("TN10EB1234");

        DeliveryVan van =
                new DeliveryVan("TN20DV5678");

        OneDayVisitorPass pass =
                new OneDayVisitorPass(true);

        System.out.println(
                "E-Bike Parking Fee: Rs "
                + bike.ratePerHour());

        System.out.println(
                "E-Bike Charging Fee: Rs "
                + bike.chargingFee());

        System.out.println(
                "Delivery Van Peak Fee: Rs "
                + van.peakHourFee(120));

        System.out.println(
                "Visitor One-Day Pass Valid: "
                + pass.isValid());
    }
}
