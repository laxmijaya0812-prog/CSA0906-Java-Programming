class DeliveryVan extends Vehicle {

    DeliveryVan(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 30;
    }

    public double peakHourFee(int minutes) {

        int hours = (minutes + 59) / 60;

        return hours * 60;
    }
}
