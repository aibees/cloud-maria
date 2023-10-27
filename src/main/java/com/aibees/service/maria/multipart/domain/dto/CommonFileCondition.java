package com.aibees.service.maria.multipart.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonFileCondition extends FileCondition {
    private String alias;
    private String searchType;
    private String searchValue;
}
