package interfaces;

import java.io.Serializable;

/**
 * An object of this class is used in RMI to transfer objects by value between Java virtual machines.
 * The class is a component JavaBeans.
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name, surname, organization, report, email;

    public Participant() {
        this.name = "Valentyn";
        this.surname = "Syniuk";
        this.organization = "KhNU";
        this.report = "Report";
        this.email = "planet.sv01@gmail.com";
    }

    public Participant(String name, String surname, String organization, String report, String email) {
        this.name = name;
        this.surname = surname;
        this.organization = organization;
        this.report = report;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + "; Surname: " + surname + "; Organization: " + organization + "; " + "Report: " + report + "; Email: " + email;
    }

}
