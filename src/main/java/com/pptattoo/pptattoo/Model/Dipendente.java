package com.pptattoo.pptattoo.Model;

public class Dipendente {

    private String id;
    private String name;
    private String lastName;
    private String role;
    private String salary;

    public Dipendente(String id, String name, String lastName, String role, String salary) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.role = role;
        this.salary = salary;
    }


    //setters - supposed to update in db also
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(String role) { this.role = role; }
    public void setSalary(String salary) {this.salary = salary; }


    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public String getSalary() { return salary; }
}
