package com.example.shose.server.dto.request.bill;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CancelBillClientRequest {
    private String id;
    private String description;
}
