package chapter14.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArgsTest {

    @DisplayName("scheme와 arguments가 없는 경우")
    @Test
    void noSchemaAndArguments() {
        //given
        String schema = "";
        String[] arguments = new String[0];

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isZero();
    }

    @DisplayName("scheme가 없고 argument가 1개 있는 경우")
    @Test
    void noSchemaButWithOneArgument() {
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

    @DisplayName("scheme가 없고 argument가 여러 개 있는 경우")
    @Test
    void noSchemaButWithMultipleArguments() {
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
    void simpleBooleanNotPresent() {
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
    void simpleBooleanPresent() {
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
    void simpleBooleanMultiplePresent() {
        //given
        String schema = "x,y";
        String[] arguments = new String[]{"-x", "-y"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isEqualTo(2);
        assertThat(args.getBoolean('x')).isTrue();
        assertThat(args.getBoolean('y')).isTrue();
    }

    @DisplayName("boolean 값이 여러 개 중 하나만 있는 경우")
    @Test
    void simpleBooleanOnlyOnePresent() {
        //given
        String schema = "x,y,z";
        String[] arguments = new String[]{"-z"};

        //when
        Args args = new Args(schema, arguments);

        //then
        assertThat(args.isValid()).isTrue();
        assertThat(args.cardinality()).isOne();
        assertThat(args.getBoolean('x')).isFalse();
        assertThat(args.getBoolean('y')).isFalse();
        assertThat(args.getBoolean('z')).isTrue();
    }
}