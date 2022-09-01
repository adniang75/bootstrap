package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class HotStream3Test {

    private final List<Integer> one = new ArrayList<>();
    private final List<Integer> two = new ArrayList<>();
    private final List<Integer> three = new ArrayList<>();

    private Consumer<Integer> subscribe(List<Integer> list) {
        return list::add;
    }

    @Test
    @DisplayName("publish")
    void publish() {
        Flux<Integer> pileOn = Flux
                .just(1, 2, 3)
                .publish()
                .autoConnect(3)
                .subscribeOn(Schedulers.immediate());
        // force the subscription on the same thread,
        // so we can observe the interactions
        pileOn.subscribe(subscribe(one));
        assertEquals(0, this.one.size());
        pileOn.subscribe(subscribe(two));
        assertEquals(0, this.two.size());
        pileOn.subscribe(subscribe(three));
        assertEquals(3, this.one.size());
        assertEquals(3, this.two.size());
        assertEquals(3, this.three.size());
    }

}
