package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class StringArgumentMarshalerTest {

    @DisplayName("Argument 값이 없는 경우")
    @Test
    void missingString() {
        //given
        Iterator<String> argument = Collections.emptyIterator();

        //when
        ArgumentMarshaler am = new StringArgumentMarshaler();
        Exception exception = catchException(() -> am.set(argument));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_STRING);
    }


    @Test
    void getValueNotPresent() {
        //given
        ArgumentMarshaler am = new StringArgumentMarshaler();

        //when, then
        assertThat(StringArgumentMarshaler.getValue(am)).isEmpty();
    }

    @Test
    void getValuePresent() throws ArgsException {
        //given
        Iterator<String> list = List.of("param").iterator();

        //when
        ArgumentMarshaler am = new StringArgumentMarshaler();
        am.set(list);

        //then
        assertThat(StringArgumentMarshaler.getValue(am)).isEqualTo("param");
    }

    @Test
    void getValueNotEqualsType() throws ArgsException {
        //given
        Iterator<String> list = List.of("42").iterator();

        //when
        ArgumentMarshaler otherType = new IntegerArgumentMarshaler();
        otherType.set(list);

        //then
        assertThat(StringArgumentMarshaler.getValue(otherType)).isEmpty();
    }


    @Test
    void getValueNull() {
        assertThat(StringArgumentMarshaler.getValue(null)).isEmpty();
    }
}