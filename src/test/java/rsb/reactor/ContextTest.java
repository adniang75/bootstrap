package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("deprecation")
@Log4j2
class ContextTest {

    @Test
    @DisplayName("context")
    void context() throws InterruptedException {
        var observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();
        var max = 3;
        var key = "key1";
        var cdl = new CountDownLatch(max);
        Context context = Context.of(key, "value1");
        Flux<Integer> just = Flux
                .range(0, max)
                .delayElements(Duration.ofMillis(1))
                .doOnEach((Signal<Integer> integerSignal) -> {
                    Context currentContext = integerSignal.getContext();
                    if (integerSignal.getType().equals(SignalType.ON_NEXT)) {
                        String key1 = currentContext.get(key);
                        assertNotNull(key1);
                        assertEquals("value1", key1);
                        observedContextValues
                                .computeIfAbsent("key1", k -> new AtomicInteger(0))
                                .incrementAndGet();
                    }
                }).subscriberContext(context);
        just.subscribe(integer -> {
            log.info("integer: {}", integer);
            cdl.countDown();
        });
        cdl.await();
        assertEquals(max, observedContextValues.get(key).get());
    }

}
