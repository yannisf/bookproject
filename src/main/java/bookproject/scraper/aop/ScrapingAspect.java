package bookproject.scraper.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.stream.Stream;

@Aspect
@Component
public class ScrapingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ScrapingAspect.class);

    @Pointcut("execution(public * search(..))")
    private void search() {
    }

    @Around("search()")
    public Object logSearchElapsedTime(ProceedingJoinPoint pjp) throws Throwable {
        Stream.of(pjp.getArgs()).forEach(a -> LOG.debug("Search argument [{}]", a));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object returnValue = pjp.proceed();
        stopWatch.stop();
        LOG.debug("Search elapsed time: [{}] seconds", stopWatch.getTotalTimeSeconds());
        return returnValue;
    }

}
