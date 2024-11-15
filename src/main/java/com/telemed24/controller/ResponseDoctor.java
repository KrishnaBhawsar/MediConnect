package com.telemed24.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResponseDoctor {
    @JsonProperty("id")
    int id;
    @JsonProperty("specialization")
    String specialization;
    @JsonProperty("city")
    String city;
    @JsonProperty("name")
    String name;
}