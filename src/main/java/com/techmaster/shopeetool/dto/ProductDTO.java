package com.techmaster.shopeetool.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String username;
    private String productName;
    private String shopDistributor;
    private int ratting;
    private boolean isShoppeMall;
}
