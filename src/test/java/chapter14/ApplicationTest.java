package chapter14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends MainMethodTest {

    @Test
    void validBooleanArgs() {
        //given
        String arg = "-l";

        //when
        runMain(arg);

        //then
        assertThat(output()).contains("logging: true");
    }

    @Test
    void invalidBooleanArgs() {
        //given
        String notStartWithHyphen = "l";

        //when
        runMain(notStartWithHyphen);

        //then
        assertThat(output()).contains("logging: false");
    }

    @Override
    protected void runMain(String... args) {
        Application.main(args);
    }
}