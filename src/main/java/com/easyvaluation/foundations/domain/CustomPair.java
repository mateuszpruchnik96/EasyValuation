package com.easyvaluation.foundations.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomPair<T> {
    private T field;
}
