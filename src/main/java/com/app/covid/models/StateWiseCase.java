package com.app.covid.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StateWiseCase {

    private Long active;
    private Long confirmed;
    private Long deaths;
    private Long deltaConfirmed;
    private Long deltaDeaths;
    private Long deltaRecovered;
    private Long migratedOther;
    private Long recovered;
    private String state;
    private String stateCode;
    private String stateNotes;
    private OffsetDateTime lastUpdatedTime;
}
