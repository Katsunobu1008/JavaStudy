package com.picsy.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MatrixRowUpdateRequest(
        @Min(0) int rowIndex,
        @NotNull @Size(min = 1) List<Double> values
) {}
