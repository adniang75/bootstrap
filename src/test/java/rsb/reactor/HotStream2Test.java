package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class HotStream2Test {

    @Test
    @DisplayName("hot")
    void hot() throws Exception {
        int factor = 10;
        log.info("start");
        var cdl = new CountDownLatch(2);
        Flux<Integer> live = Flux
                .range(0, 10)
                .delayElements(Duration.ofMillis(factor))
                .share();
        var one = new ArrayList<Integer>();
        var two = new ArrayList<Integer>();
        live
                .doFinally(signalTypeConsumer(cdl))
                .subscribe(collect(one));
        Thread.sleep(factor * 2);
        live
                .doFinally(signalTypeConsumer(cdl))
                .subscribe(collect(two));
        cdl.await(5, TimeUnit.SECONDS);
        assertTrue(one.size() > two.size());
        log.info("stop");
    }

    private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
        return signal -> {
            if (signal.equals(SignalType.ON_COMPLETE)) {
                try {
                    cdl.countDown();
                    log.info("await()...");
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
    }

    private Consumer<Integer> collect(List<Integer> ints) {
        return ints::add;
    }

}
