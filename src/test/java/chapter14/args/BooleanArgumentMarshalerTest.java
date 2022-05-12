package chapter14.args;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class BooleanArgumentMarshalerTest {

    @Test
    void getValueNotPresent() {
        //given
        ArgumentMarshaler am = new BooleanArgumentMarshaler();

        //when, then
        assertThat(BooleanArgumentMarshaler.getValue(am)).isFalse();
    }

    @Test
    void getValuePresent() throws ArgsException {
        //given
        Iterator<String> list = List.of("test").iterator();

        //when
        ArgumentMarshaler am = new BooleanArgumentMarshaler();
        am.set(list);

        //then
        assertThat(BooleanArgumentMarshaler.getValue(am)).isTrue();
    }

    @Test
    void getValueNotEqualsType() throws ArgsException {
        //given
        Iterator<String> list = List.of("param").iterator();

        //when
        ArgumentMarshaler otherType = new StringArgumentMarshaler();
        otherType.set(list);

        //then
        assertThat(BooleanArgumentMarshaler.getValue(otherType)).isFalse();
    }

    @Test
    void getValueNull() {
        assertThat(BooleanArgumentMarshaler.getValue(null)).isFalse();
    }
}