package com.security.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Address(String street, String city, String state, String pincode, String country, String phone) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
        this.phone = phone;
    }
}
