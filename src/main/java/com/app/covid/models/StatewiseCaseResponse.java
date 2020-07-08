package com.app.covid.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatewiseCaseResponse {

    private String active;
    private String confirmed;
    private String deaths;
    private String deltaconfirmed;
    private String deltadeaths;
    private String deltarecovered;
    private String migratedother;
    private String recovered;
    private String state;
    private String statecode;
    private String statenotes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastupdatedtime;
}
