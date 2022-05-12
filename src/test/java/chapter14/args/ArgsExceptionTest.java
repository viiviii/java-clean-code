package chapter14.args;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArgsExceptionTest {

    @Test
    void unexpectedMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.UNEXPECTED_ARGUMENT;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument(s) -x unexpected.");
    }

    @Test
    void missingStringMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.MISSING_STRING;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find string parameter for -x.");
    }

    @Test
    void invalidIntegerMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.INVALID_INTEGER;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', "Forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x expects an integer but was 'Forty two'.");
    }

    @Test
    void missingIntegerMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.MISSING_INTEGER;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find integer parameter for -x.");
    }

    @Test
    void invalidDoubleMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.INVALID_DOUBLE;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', "Forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x expects an double but was 'Forty two'.");
    }

    @Test
    void missingDoubleMessage() throws Exception {
        //given
        ArgsException.ErrorCode errorCode = ArgsException.ErrorCode.MISSING_DOUBLE;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find double parameter for -x.");
    }
}