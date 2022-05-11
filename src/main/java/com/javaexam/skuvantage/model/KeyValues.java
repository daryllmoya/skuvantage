package com.javaexam.skuvantage.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KeyValues {

    private String key;
    private List<String> values;

}
