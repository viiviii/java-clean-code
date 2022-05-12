package chapter14.args;

import org.junit.jupiter.api.Test;

import static chapter14.args.ArgsException.ErrorCode;
import static chapter14.args.ArgsException.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;

class ArgsExceptionTest {

    @Test
    void unexpectedMessage() throws Exception {
        //given
        ErrorCode errorCode = UNEXPECTED_ARGUMENT;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument(s) -x unexpected.");
    }

    @Test
    void missingStringMessage() throws Exception {
        //given
        ErrorCode errorCode = MISSING_STRING;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find string parameter for -x.");
    }

    @Test
    void invalidIntegerMessage() throws Exception {
        //given
        ErrorCode errorCode = INVALID_INTEGER;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', "Forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x expects an integer but was 'Forty two'.");
    }

    @Test
    void missingIntegerMessage() throws Exception {
        //given
        ErrorCode errorCode = MISSING_INTEGER;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find integer parameter for -x.");
    }

    @Test
    void invalidDoubleMessage() throws Exception {
        //given
        ErrorCode errorCode = INVALID_DOUBLE;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', "Forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x expects an double but was 'Forty two'.");
    }

    @Test
    void missingDoubleMessage() throws Exception {
        //given
        ErrorCode errorCode = MISSING_DOUBLE;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("Could not find double parameter for -x.");
    }

    @Test
    void invalidArgumentNameMessage() throws Exception {
        //given
        ErrorCode errorCode = INVALID_ARGUMENT_NAME;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("'x' is not a valid argument name.");
    }

    @Test
    void invalidArgumentFormatMessage() throws Exception {
        //given
        ErrorCode errorCode = INVALID_ARGUMENT_FORMAT;

        //when
        ArgsException e = new ArgsException(errorCode, 'x', "f~");

        //then
        assertThat(e.errorMessage()).isEqualTo("'f~' is not a valid argument format.");
    }
}