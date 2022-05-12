package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static chapter14.args.ArgsException.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class ArgsTest {

    @DisplayName("schema와 arguments가 없는 경우")
    @Test
    void noSchemaAndArguments() throws Exception {
        //given
        String schema = "";
        String[] arguments = new String[0];

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.nextArgument()).isZero();
    }

    @DisplayName("schema가 없고 argument가 1개 있는 경우")
    @Test
    void noSchemaButWithOneArgument() {
        //given
        String schema = "";
        String[] arguments = new String[]{"-x"};

        //when
        Exception exception = catchException(() -> new Args(schema, arguments));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(UNEXPECTED_ARGUMENT);
        assertThat(e.getErrorArgumentId()).isEqualTo('x');
    }

    @DisplayName("Schema가 있고, arugment가 없는 경우")
    @Test
    void multipleSchemasButNoArguments() throws Exception {
        //given
        String schema = "x,y#,z*";
        String[] arguments = new String[]{};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isFalse();
        assertThat(args.has('y')).isFalse();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getBoolean('x')).isFalse();
        assertThat(args.getInt('y')).isZero();
        assertThat(args.getString('z')).isEmpty();
    }

    @DisplayName("schema가 문자가 아닌 경우")
    @Test
    void nonLetterSchema() {
        //given
        String nonLetterSchemaElementId = "*";
        String[] arguments = new String[]{};

        //when
        Exception exception = catchException(() -> new Args(nonLetterSchemaElementId, arguments));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(INVALID_ARGUMENT_NAME);
        assertThat(e.getErrorArgumentId()).isEqualTo('*');
    }

    @DisplayName("schema format이 유효하지 않은 경우")
    @Test
    void invalidArgumentFormat() {
        //given
        String invalidSchemeFormat = "f~";
        String[] arguments = new String[]{};

        //when
        Exception exception = catchException(() -> new Args(invalidSchemeFormat, arguments));

        //then
        assertThat(exception).isInstanceOf(ArgsException.class);

        ArgsException e = (ArgsException) exception;
        assertThat(e.getErrorCode()).isEqualTo(INVALID_ARGUMENT_FORMAT);
        assertThat(e.getErrorArgumentId()).isEqualTo('f');
    }

    @DisplayName("Schema format에 스페이스가 있는 경우")
    @Test
    void spacesInFormat() throws Exception {
        //given
        String schema = "x, y";
        String[] arguments = new String[]{"-xy"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
    }

    @DisplayName("boolean 값이 있는 경우")
    @Test
    void simpleBooleanPresent() throws Exception {
        //given
        String schema = "x";
        String[] arguments = new String[]{"-x"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.getBoolean('x')).isTrue();
    }

    @DisplayName("boolean 값이 여러 개 있는 경우")
    @Test
    void simpleBooleanMultiplePresent() throws Exception {
        //given
        String schema = "x,y,z";
        String[] arguments = new String[]{"-x", "-y"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getBoolean('x')).isTrue();
        assertThat(args.getBoolean('y')).isTrue();
        assertThat(args.getBoolean('z')).isFalse();
    }

    @DisplayName("boolean 값이 없는 경우")
    @Test
    void simpleBooleanNotPresent() throws Exception {
        //given
        Args args = new Args("x", new String[]{});

        //when
        boolean actual = args.getBoolean('y');

        //then
        assertThat(actual).isFalse();
    }

    @DisplayName("String 값이 있는 경우")
    @Test
    void simpleStringPresent() throws Exception {
        //given
        String schema = "x*";
        String[] arguments = new String[]{"-x", "param"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.getString('x')).isEqualTo("param");
    }

    @DisplayName("String 값이 여러 개 있는 경우")
    @Test
    void simpleStringMultiplePresent() throws Exception {
        //given
        String schema = "x*,y*,z*";
        String[] arguments = new String[]{"-x", "param1", "-y", "param2"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getString('x')).isEqualTo("param1");
        assertThat(args.getString('y')).isEqualTo("param2");
        assertThat(args.getString('z')).isEmpty();
    }

    @DisplayName("String 값이 없는 경우")
    @Test
    void simpleStringNotPresent() throws Exception {
        //given
        Args args = new Args("x*", new String[]{});

        //when
        String actual = args.getString('y');

        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("int 값이 있는 경우")
    @Test
    void simpleIntPresent() throws Exception {
        //given
        String schema = "x#";
        String[] arguments = new String[]{"-x", "42"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.getInt('x')).isEqualTo(42);
    }

    @DisplayName("int 값이 여러 개 있는 경우")
    @Test
    void simpleIntMultiplePresent() throws Exception {
        //given
        String schema = "x#,y#,z#";
        String[] arguments = new String[]{"-x", "8001", "-y", "8002"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getInt('x')).isEqualTo(8001);
        assertThat(args.getInt('y')).isEqualTo(8002);
        assertThat(args.getInt('z')).isZero();
    }

    @DisplayName("int 값이 없는 경우")
    @Test
    void simpleIntNotPresent() throws Exception {
        //given
        Args args = new Args("x#", new String[]{});

        //when
        int actual = args.getInt('y');

        //then
        assertThat(actual).isZero();
    }

    @DisplayName("double 값이 있는 경우")
    @Test
    void simpleDoublePresent() throws Exception {
        //given
        String schema = "x##";
        String[] arguments = new String[]{"-x", "42.3"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.has('x')).isTrue();
        assertThat(args.getDouble('x')).isEqualTo(42.3);
    }

    @DisplayName("Double 값이 없는 경우")
    @Test
    void simpleDoubleNotPresent() throws Exception {
        //given
        Args args = new Args("x##", new String[]{});

        //when
        double actual = args.getDouble('y');

        //then
        assertThat(actual).isZero();
    }

    @DisplayName("잘못된 타입으로 호출한 경우")
    @Test
    void invalidType() throws Exception {
        //given
        String schema = "x#,y*";
        String[] arguments = new String[]{"-x", "42", "-y", "param"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.getBoolean('x')).isFalse(); // x is type int
        assertThat(args.getString('x')).isEmpty(); // x is type int
        assertThat(args.getInt('y')).isZero(); // y is type String
        assertThat(args.getDouble('y')).isZero(); // y is type String
    }
}