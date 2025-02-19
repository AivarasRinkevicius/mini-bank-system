package com.bank.accounts.common;

import java.util.function.Consumer;

public class Utils {
    public static <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}
