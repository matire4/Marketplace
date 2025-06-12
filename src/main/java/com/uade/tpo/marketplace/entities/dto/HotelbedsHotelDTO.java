package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class HotelbedsHotelDTO {
    private int code;
    private Name name;
    private String description;
    private Address address;
    private City city;
    private Country country;
    private List<HotelbedsRoomDTO> rooms;

    @Data
    public static class Name {
        private String content;
    }

    @Data
    public static class Address {
        private String content;
    }

    @Data
    public static class City {
        private String content;
    }

    @Data
    public static class Country {
        private String content;
    }
}

