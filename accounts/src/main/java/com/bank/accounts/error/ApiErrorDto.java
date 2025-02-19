package com.bank.accounts.error;

import lombok.NonNull;

public record ApiErrorDto(@NonNull String errorCode, @NonNull String message) {
}
