package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class DoOnTest {

    @Test
    @DisplayName("Do on")
    void doOn() {
        var signals = new ArrayList<Signal<Integer>>();
        var nextValues = new ArrayList<Integer>();
        var subscriptions = new ArrayList<Subscription>();
        var exceptions = new ArrayList<Throwable>();
        var finallySignals = new ArrayList<SignalType>();
        Flux<Integer> on = Flux
                .<Integer>create(sink -> {
                    sink.next(1);
                    sink.next(2);
                    sink.next(3);
                    sink.error(new IllegalArgumentException("oops!"));
                    sink.complete();
                })
                .doOnNext(nextValues::add)
                .doOnEach(signals::add)
                .doOnSubscribe(subscriptions::add)
                .doOnError(IllegalArgumentException.class, exceptions::add)
                .doFinally(finallySignals::add);
        StepVerifier
                .create(on)
                .expectNext(1, 2, 3)
                .expectError(IllegalArgumentException.class)
                .verify();
        signals.forEach(log::info);
        assertEquals(4, signals.size());
        finallySignals.forEach(log::info);
        assertEquals(1, finallySignals.size());
        subscriptions.forEach(log::info);
        assertEquals(1, subscriptions.size());
        exceptions.forEach(log::info);
        assertEquals(1, exceptions.size());
        assertTrue(exceptions.get(0) instanceof IllegalArgumentException);
        nextValues.forEach(log::info);
        assertEquals(Arrays.asList(1, 2, 3), nextValues);
    }

}
