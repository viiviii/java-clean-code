package chapter14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends MainMethodTest {

    @Test
    void booleanArguments() {
        //given
        String[] args = new String[]{"-l"};

        //when
        runMain(args);

        //then
        assertThat(output()).contains("true");
    }

    @Test
    void stringArguments() {
        //given
        String[] args = new String[]{"-d", "root"};

        //when
        runMain(args);

        //then
        assertThat(output()).contains("root");
    }

    @Test
    void allArgumentsPresent() {
        //given
        String[] args = new String[]{"-l", "-d", "user"};

        //when
        runMain(args);

        //then
        assertThat(output()).isEqualTo("logging: true, directory: user");
    }

    @Test
    void noArguments() {
        //given
        String[] args = new String[0];

        //when
        runMain(args);

        //then
        assertThat(output()).isEqualTo("logging: false, directory:");
    }

    @Override
    protected void runMain(String... args) {
        Application.main(args);
    }
}