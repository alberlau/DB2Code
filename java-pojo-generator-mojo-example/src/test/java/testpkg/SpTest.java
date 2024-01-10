package testpkg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.sp.NextPrime;

@SpringBootTest(classes = {App.class, NextPrime.class})
public class SpTest {
    private final NextPrime nextPrime;

    public SpTest(@Autowired NextPrime nextPrime) {
        this.nextPrime = nextPrime;
    }

    @Test
    public void test() {
        assertEquals("127", nextPrime.invoke("123"));
    }
}
