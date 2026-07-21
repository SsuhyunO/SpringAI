package com.example.ch04.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelDTO {
    private String city;
    private List<String> names;
}
