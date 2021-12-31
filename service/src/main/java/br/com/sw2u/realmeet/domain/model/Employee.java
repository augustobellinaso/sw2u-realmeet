package br.com.sw2u.realmeet.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Employee {

    @Column(name = "employee_name")
    private String name;

    @Column(name = "employee_email")
    private String email;

    public Employee() {
    }

    private Employee(EmployeeBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name) && Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    @Override
    public String toString() {
        return "Employee{" +
               "name='" + name + '\'' +
               ", email='" + email + '\'' +
               '}';
    }

    public static EmployeeBuilder newEmployee() {
        return new EmployeeBuilder();
    }

    public static final class EmployeeBuilder {
        private String name;
        private String email;

        private EmployeeBuilder() {
        }

        public EmployeeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EmployeeBuilder email(String email) {
            this.email = email;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
