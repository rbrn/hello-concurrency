package minimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.helloworld.HelloService;
import org.springframework.integration.samples.helloworld.HelloWorldApp;
import org.springframework.messaging.MessageChannel;

import java.net.URL;

/**
 * Trivial test class. Demonstrates the syntax of JUnit4.
 * Important: Do NOT inherit this class from TestCase() or JUnit3.x is enforced
 *
 * @author Sascha Tayefeh
 */
public class DoSomeActionTest {
    @Test
    public void testIsThisReallyTrue() throws Exception {
        HelloService action = new HelloService();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String contents = action.downloadContents(new URL("http://www.example.com"));
        final String contents1 = action.downloadContents(new URL("http://www.example.com"));
        final String contents2 = action.downloadContents(new URL("http://www.example.com"));
        final String contents3 = action.downloadContents(new URL("http://www.example.com"));
        final String contents4 = action.downloadContents(new URL("http://www.example.com"));

        stopWatch.stop();
        System.out.print("Blocking:\n");
        System.out.print(stopWatch);
        System.out.print("\n");
    }

    @Test
    public void testIsThisReallyTrueAsync() throws Exception {

        HelloService action = new HelloService();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String contents = action.downloadContentsWithFuture(new URL("http://www.example.com")).get();
        final String contents1 = action.downloadContentsWithFuture(new URL("http://www.example.com")).get();
        final String contents2 = action.downloadContentsWithFuture(new URL("http://www.example.com")).get();
        final String contents3 = action.downloadContentsWithFuture(new URL("http://www.example.com")).get();
        final String contents4 = action.downloadContentsWithFuture(new URL("http://www.example.com")).get();

        stopWatch.stop();

        System.out.print("NonBlocking:\n");
        System.out.print(stopWatch);
        System.out.print("\n");
    }
    
    @Test
    public void testAsync() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/helloWorldDemo.xml", HelloWorldApp.class);
        HelloService action = context.getBean("helloService", HelloService.class);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String contents = action.downloadContentsWithFutureAsync(new URL("http://www.example.com")).get();
        final String contents1 = action.downloadContentsWithFutureAsync(new URL("http://www.example.com")).get();
        final String contents2 = action.downloadContentsWithFutureAsync(new URL("http://www.example.com")).get();
        final String contents3 = action.downloadContentsWithFutureAsync(new URL("http://www.example.com")).get();
        final String contents4 = action.downloadContentsWithFutureAsync(new URL("http://www.example.com")).get();

        stopWatch.stop();

        System.out.print("NonBlocking:\n");
        System.out.print(stopWatch);
        System.out.print("\n");
    }


    @Test
    public void testAsyncGuava() throws Exception{
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/helloWorldDemo.xml", HelloWorldApp.class);
        HelloService action = context.getBean("helloService", HelloService.class);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
         action.doGuavaDownloadWithChainedThreads(new URL("http://www.example.com"));
        stopWatch.stop();

        System.out.print("NonBlocking guava:\n");
        System.out.print(stopWatch);
        System.out.print("\n");
    }
}
