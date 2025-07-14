package ru.dmitry.tgBot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private Long externalId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false,length = 400)
    private String address;

    public Client(Long id, Long externalId, String fullName, String phoneNumber, String address) {
        this.id = id;
        this.externalId = externalId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", externalId=" + externalId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Client() {
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id!=null && id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
