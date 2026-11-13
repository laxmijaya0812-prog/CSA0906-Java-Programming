package app;

import model.Car;
import service.ParkingFacility;

public class Main {

    public static void main(String[] args) {

        Car car = new Car("TN01AB1111");

        System.out.println(
                "Registration Number: "
                + car.getRegNo());

        ParkingFacility facility =
                new ParkingFacility();

        facility.displayMessage();
    }
}
