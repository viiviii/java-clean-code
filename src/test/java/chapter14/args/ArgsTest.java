package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

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
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isZero();
    }

    @DisplayName("schema가 없고 argument가 1개 있는 경우")
    @Test
    void noSchemaButWithOneArgument() throws Exception {
        //given
        String schema = "";
        String[] arguments = new String[]{"-x"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isFalse();
        assertThat(args.cardinality()).isZero();
        assertThat(args.errorMessage()).isEqualTo("Argument(s) -x unexpected.");
    }

    @DisplayName("schema가 없고 argument가 여러 개 있는 경우")
    @Test
    void noSchemaButWithMultipleArguments() throws Exception {
        //given
        String schema = "";
        String[] arguments = new String[]{"-x", "-y"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isFalse();
        assertThat(args.cardinality()).isZero();
        assertThat(args.errorMessage()).isEqualTo("Argument(s) -xy unexpected.");
    }

    @DisplayName("boolean 값이 없는 경우")
    @Test
    void simpleBooleanNotPresent() throws Exception {
        //given
        String schema = "x";
        String[] arguments = new String[]{};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isZero();
        assertThat(args.getBoolean('x')).isFalse();
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
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isOne();
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
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isEqualTo(2);
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getBoolean('x')).isTrue();
        assertThat(args.getBoolean('y')).isTrue();
        assertThat(args.getBoolean('z')).isFalse();
    }

    @DisplayName("String 값이 없는 경우")
    @Test
    void simpleStringNotPresent() throws Exception {
        //given
        String schema = "x*";
        String[] arguments = new String[]{};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isZero();
        assertThat(args.has('x')).isFalse();
        assertThat(args.getString('x')).isEmpty();
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
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isOne();
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
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isEqualTo(2);
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
        assertThat(args.has('z')).isFalse();
        assertThat(args.getString('x')).isEqualTo("param1");
        assertThat(args.getString('y')).isEqualTo("param2");
        assertThat(args.getString('z')).isEmpty();
    }

    @DisplayName("String Argument 값이 없는 경우")
    @Test
    void missingStringArgument() throws Exception {
        //given
        String schema = "x*";
        String[] arguments = new String[]{"-x"}; // missing

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isFalse();
        assertThat(args.cardinality()).isOne();
        assertThat(args.has('x')).isTrue();
        assertThat(args.getString('x')).isEmpty();
        assertThat(args.errorMessage()).isEqualTo("Could not find string parameter for -x.");
    }

    @DisplayName("Schema의 ElementId가 문자가 아니면 ParseException이 발생한다")
    @Test
    void throwParseExceptionWhenInvalidSchemaElementId() {
        //given
        String containsNonLetterElementId = "l,d*,#";
        String[] arguments = new String[0];

        //when
        Exception exception = catchException(() -> new Args(containsNonLetterElementId, arguments));

        //then
        assertThat(exception)
                .isInstanceOf(ParseException.class)
                .hasMessage("Bad character: # in Args format: l,d*,#");
    }

    @DisplayName("유효한 args에서 errorMessage를 조회할 경우 Excpetion이 발생한다")
    @Test
    void errorMessageThrowExceptionWhenErrorCodeIsOK() throws Exception {
        //given
        Args args = new Args("x", new String[]{"-x"});
        assertThat(args.isValid()).isTrue();

        //when
        Exception exception = catchException(args::errorMessage);

        //then
        assertThat(exception)
                .isInstanceOf(Exception.class)
                .hasMessage("TILT: Should not get here.");
    }
}