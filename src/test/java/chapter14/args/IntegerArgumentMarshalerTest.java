package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class IntegerArgumentMarshalerTest {

    @DisplayName("Integer Argument 값을 파싱할 수 없는 경우")
    @Test
    void invalidInteger() {
        //given
        Iterator<String> argument = List.of("Forty two").iterator();

        //when
        ArgumentMarshaler am = new IntegerArgumentMarshaler();
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
        ArgumentMarshaler am = new IntegerArgumentMarshaler();
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_INTEGER);
    }

    @Test
    void getValueNotPresent() {
        //given
        ArgumentMarshaler am = new IntegerArgumentMarshaler();

        //when, then
        assertThat(IntegerArgumentMarshaler.getValue(am)).isZero();
    }

    @Test
    void getValuePresent() throws ArgsException {
        //given
        Iterator<String> list = List.of("42").iterator();

        //when
        ArgumentMarshaler am = new IntegerArgumentMarshaler();
        am.set(list);

        //then
        assertThat(IntegerArgumentMarshaler.getValue(am)).isEqualTo(42);
    }

    @Test
    void getValueNotEqualsType() throws ArgsException {
        //given
        Iterator<String> list = List.of("param").iterator();

        //when
        ArgumentMarshaler otherType = new StringArgumentMarshaler();
        otherType.set(list);

        //then
        assertThat(IntegerArgumentMarshaler.getValue(otherType)).isZero();
    }

    @Test
    void getValueNull() {
        assertThat(IntegerArgumentMarshaler.getValue(null)).isZero();
    }
}