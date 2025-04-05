package com.spring.basicapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private Long user_id;
    private String description;
    private Boolean status;
    private Long value;

}
