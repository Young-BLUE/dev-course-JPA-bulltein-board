package com.kdtpractice.young.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "customers")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Customer {
    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postcode", nullable = false, length = 6)
    private int postcode;

    @Column(name = "phone_number", nullable = false, length = 13) // 010-1234-5678 ('-' 포함 13)
    private String phoneNumber;

    @Column(name = "emain_address", nullable = false, length = 50)
    private String emailAddress;

    public void changeName(String name) {
        this.name = name;
    }
}
