class EBike extends Vehicle {

    EBike(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 15;
    }

    public double chargingFee() {
        return 20;
    }
}
