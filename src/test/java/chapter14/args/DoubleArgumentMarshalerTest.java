package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class DoubleArgumentMarshalerTest {
    private ArgumentMarshaler am = new DoubleArgumentMarshaler();

    @DisplayName("값을 파싱할 수 없는 경우")
    @Test
    void invalidDouble() {
        //given
        Iterator<String> argument = List.of("Forty two").iterator();

        //when
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_DOUBLE);
        assertThat(e.getErrorParameter()).isEqualTo("Forty two");
    }

    @DisplayName("Argument 값이 없는 경우")
    @Test
    void missingDouble() {
        //given
        Iterator<String> argument = Collections.emptyIterator();

        //when
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_DOUBLE);
    }
}