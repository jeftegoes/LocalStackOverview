package com.example.clients.responses;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class RandomUserResponse {

    private List<RandomUser> results;
    private Info info;

    @Data
    public static class RandomUser {
        private String gender;
        private Name name;
        private Location location;
        private String email;
        private Login login;
        private Dob dob;
        private Registered registered;
        private String phone;
        private String cell;
        private Id id;
        private Picture picture;
        private String nat;
    }

    @Data
    public static class Name {
        private String title;
        private String first;
        private String last;
    }

    @Data
    public static class Location {
        private Street street;
        private String city;
        private String state;
        private String country;
        private String postcode;
        private Coordinates coordinates;
        private Timezone timezone;
    }

    @Data
    public static class Street {
        private int number;
        private String name;
    }

    @Data
    public static class Coordinates {
        private String latitude;
        private String longitude;
    }

    @Data
    public static class Timezone {
        private String offset;
        private String description;
    }

    @Data
    public static class Login {
        private String uuid;
        private String username;
        private String password;
        private String salt;
        private String md5;
        private String sha1;
        private String sha256;
    }

    @Data
    public static class Dob {
        private Instant date;
        private int age;
    }

    @Data
    public static class Registered {
        private Instant date;
        private int age;
    }

    @Data
    public static class Id {
        private String name;
        private String value;
    }

    @Data
    public static class Picture {
        private String large;
        private String medium;
        private String thumbnail;
    }

    @Data
    public static class Info {
        private String seed;
        private int results;
        private int page;
        private String version;
    }
}