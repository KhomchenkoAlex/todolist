package edu.test.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDto {
    private String taskName;
    private String description;
    private String status;
    private String priority;
}