package com.techmaster.shopeetool.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseProductDTO {
    private String name;
    private String linkPhoto;
    private Map<Integer,String> choicesMap;
    private String price;
}
