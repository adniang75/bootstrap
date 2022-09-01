package rsb.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.StepVerifier;

@SuppressWarnings("deprecation")
class ReplayProcessorTest {

    @Test
    void replayProcessor() throws Exception {
        ReplayProcessor<String> processor = ReplayProcessor.create(2, false);
        produce(processor.sink());
        consume(processor);
    }

    private void consume(Flux<String> publisher) {
        for (int i = 0; i < 5; i++) {
            StepVerifier.create(publisher)
                    .expectNext("2")
                    .expectNext("3")
                    .verifyComplete();
        }
    }

    private void produce(FluxSink<String> sink) {
        sink.next("1");
        sink.next("2");
        sink.next("3");
        sink.complete();
    }

}
