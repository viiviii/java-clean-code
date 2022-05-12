package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class StringArgumentMarshalerTest {
    private ArgumentMarshaler am = new StringArgumentMarshaler();

    @DisplayName("Argument 값이 없는 경우")
    @Test
    void missingString() {
        //given
        Iterator<String> argument = Collections.emptyIterator();

        //when
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_STRING);
    }
}