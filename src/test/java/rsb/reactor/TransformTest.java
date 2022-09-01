package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.test.util.AssertionErrors.assertTrue;

class TransformTest {

    @Test
    void transform() {
        var finished = new AtomicBoolean();
        var letters = Flux
                .just("A", "B", "C")
                .transform(stringFlux -> stringFlux.doFinally(signal -> finished.set(true)));
        StepVerifier
                .create(letters)
                .expectNextCount(3)
                .verifyComplete();
        assertTrue("the finished Boolean must be true.", finished.get());
    }

}
