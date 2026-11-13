interface Chargeable {

    double chargingFee(int kWh);
}

abstract class Vehicle {

    protected String regNo;

    Vehicle(String regNo) {
        this.regNo = regNo;
    }

    abstract double ratePerHour();

    public void display() {
        System.out.println("Vehicle: " + regNo);
    }
}

class ElectricCar
        extends Vehicle
        implements Chargeable {

    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    double ratePerHour() {
        return 30;
    }

    @Override
    public double chargingFee(int kWh) {
        return kWh * 12;
    }
}

public class AbstractionDemo {

    public static void main(String[] args) {

        ElectricCar car =
                new ElectricCar("TN03C3");

        car.display();

        System.out.println(
                "Parking Rate: Rs "
                + car.ratePerHour());

        System.out.println(
                "Charging Fee: Rs "
                + car.chargingFee(10));
    }
}
