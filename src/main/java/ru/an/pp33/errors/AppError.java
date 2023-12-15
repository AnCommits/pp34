package ru.an.pp33.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppError {
    private int code;
    private String message;
}
