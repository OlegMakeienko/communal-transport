package com.makeienko.communaltransport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualServiceDto {
    private Long id;
    private String transportType;
    private String start;
    private String end;
    private int distance;
    private String weather;
    private int time;
}
