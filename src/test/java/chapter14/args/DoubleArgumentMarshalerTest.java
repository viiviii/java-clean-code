package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class DoubleArgumentMarshalerTest {

    @DisplayName("값을 파싱할 수 없는 경우")
    @Test
    void invalidDouble() {
        //given
        Iterator<String> argument = List.of("Forty two").iterator();

        //when
        ArgumentMarshaler am = new DoubleArgumentMarshaler();
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
        ArgumentMarshaler am = new DoubleArgumentMarshaler();
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_DOUBLE);
    }

    @Test
    void getValueNotPresent() {
        //given
        ArgumentMarshaler am = new DoubleArgumentMarshaler();

        //when, then
        assertThat(DoubleArgumentMarshaler.getValue(am)).isZero();
    }

    @Test
    void getValuePresent() throws ArgsException {
        //given
        Iterator<String> list = List.of("42.3").iterator();

        //when
        ArgumentMarshaler am = new DoubleArgumentMarshaler();
        am.set(list);

        //then
        assertThat(DoubleArgumentMarshaler.getValue(am)).isEqualTo(42.3);
    }

    @Test
    void getValueNotEqualsType() throws ArgsException {
        //given
        Iterator<String> list = List.of("param").iterator();

        //when
        ArgumentMarshaler otherType = new StringArgumentMarshaler();
        otherType.set(list);

        //then
        assertThat(DoubleArgumentMarshaler.getValue(otherType)).isZero();
    }

    @Test
    void getValueNull() {
        assertThat(DoubleArgumentMarshaler.getValue(null)).isZero();
    }
}