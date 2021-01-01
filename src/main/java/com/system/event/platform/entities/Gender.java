package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author mark ortiz
 */
public enum Gender {
    @JsonProperty("Male")
    Male("Male"),
    @JsonProperty("Female")
    Female("Female");

    public final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Gender{" +
                "gender='" + gender + '\'' +
                '}';
    }
}
