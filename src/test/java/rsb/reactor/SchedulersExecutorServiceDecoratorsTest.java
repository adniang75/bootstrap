package rsb.reactor;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class SchedulersExecutorServiceDecoratorsTest {

    private final AtomicInteger methodInvocationCounts = new AtomicInteger();

    private final String rsb = "rsb";

    @BeforeEach
    void setUp() {
        Schedulers.resetFactory();
        Schedulers.addExecutorServiceDecorator(this.rsb,
                (scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService));
    }

    private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
        try {
            var pfb = new ProxyFactoryBean();
            pfb.setProxyInterfaces(new Class[]{ScheduledExecutorService.class});
            pfb.addAdvice((MethodInterceptor) methodInvocation -> {
                var methodName = methodInvocation.getMethod().getName().toLowerCase();
                this.methodInvocationCounts.incrementAndGet();
                log.info("methodName: {} incrementing", methodName);
                return methodInvocation.proceed();
            });
            pfb.setSingleton(true);
            pfb.setTarget(executorService);
            return (ScheduledExecutorService) pfb.getObject();
        } catch (Exception exception) {
            log.error(exception);
        }
        return null;
    }

    @AfterEach
    void tearDown() {
        Schedulers.resetFactory();
        Schedulers.removeExecutorServiceDecorator(this.rsb);
    }

    @Test
    @DisplayName("Change Default Decorator")
    void changeDefaultDecorator() {
        Flux<Integer> integerFlux = Flux
                .just(1)
                .delayElements(Duration.ofMillis(1));
        StepVerifier
                .create(integerFlux)
                .thenAwait(Duration.ofMillis(10))
                .expectNextCount(1)
                .verifyComplete();
        assertEquals(1, this.methodInvocationCounts.get());
    }

}
