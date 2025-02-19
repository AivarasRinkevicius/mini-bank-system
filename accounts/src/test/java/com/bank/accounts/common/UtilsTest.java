package com.bank.accounts.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilsTest {
    @Test
    void shouldUpdateIfNotNull() {
        String newValue = "newValue";
        Consumer<String> setter = mock(Consumer.class);

        Utils.updateIfNotNull(newValue, setter);

        verify(setter, times(1)).accept(newValue);
    }

    @Test
    void ushouldNotCallSetter_WhenNewValueIsNull() {
        Consumer<String> setter = mock(Consumer.class);

        Utils.updateIfNotNull(null, setter);

        verify(setter, never()).accept(any());
    }

}
