package model;

public class Vehicle {

    private String regNo;

    public Vehicle(String regNo) {

        if (regNo == null || regNo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Registration number cannot be empty");
        }

        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {

        if (regNo == null || regNo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid registration number");
        }

        this.regNo = regNo;
    }
}
