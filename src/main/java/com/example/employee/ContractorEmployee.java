package com.example.employee;

public class ContractorEmployee extends Employee {
    private double hourlyRate;
    private int maxHours;

    public ContractorEmployee(String name, double hourlyRate, int maxHours) {
        super(name);
        this.hourlyRate = hourlyRate;
        this.maxHours = maxHours;
    }

    public ContractorEmployee(String name) {
        super(name);
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * maxHours;
    }

    @Override
    public String getType() {
        return "Contractor";
    }
}
