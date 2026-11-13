class Vehicle {

    protected double ratePerHour() {
        return 20;
    }

    @Override
    public String toString() {
        return "Vehicle";
    }
}

class TwoWheeler extends Vehicle {

    @Override
    protected double ratePerHour() {
        return 10;
    }

    @Override
    public String toString() {
        return "TwoWheeler";
    }
}

class Car extends Vehicle {

    @Override
    protected double ratePerHour() {
        return 30;
    }

    @Override
    public String toString() {
        return "Car";
    }
}

public class VehicleOverride {

    public static void main(String[] args) {

        Vehicle v1 = new TwoWheeler();
        Vehicle v2 = new Car();

        System.out.println(v1 + " Rate: " +
                v1.ratePerHour());

        System.out.println(v2 + " Rate: " +
                v2.ratePerHour());
    }
}
