package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TakeTest {

    @Test
    void take() {
        var count = 10;
        Flux<Integer> take = range()
                .takeUntil(i -> i == (count - 1));
        StepVerifier
                .create(take)
                .expectNextCount(count)
                .verifyComplete();
    }

    private Flux<Integer> range() {
        return Flux.range(0, 1000);
    }

}
