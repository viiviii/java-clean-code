package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class IntegerArgumentMarshalerTest {
    private ArgumentMarshaler am = new IntegerArgumentMarshaler();

    @DisplayName("Integer Argument 값을 파싱할 수 없는 경우")
    @Test
    void invalidInteger() {
        //given
        Iterator<String> argument = List.of("Forty two").iterator();

        //when
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_INTEGER);
        assertThat(e.getErrorParameter()).isEqualTo("Forty two");
    }

    @DisplayName("Argument 값이 없는 경우")
    @Test
    void missingInteger() {
        //given
        Iterator<String> argument = Collections.emptyIterator();

        //when
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_INTEGER);
    }
}