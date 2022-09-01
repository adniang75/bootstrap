package rsb.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
class HotStream1Test {

    @Test
    @DisplayName("hot")
    void hot() {
        var first = new ArrayList<Integer>();
        var second = new ArrayList<Integer>();
        EmitterProcessor<Integer> emitter = EmitterProcessor.create(2);
        FluxSink<Integer> sink = emitter.sink();
        emitter.subscribe(collect(first));
        sink.next(1);
        sink.next(2);
        emitter.subscribe(collect(second));
        sink.next(3);
        sink.complete();
        assertTrue(first.size() > second.size());
    }

    private Consumer<Integer> collect(List<Integer> collection) {
        return collection::add;
    }

}
