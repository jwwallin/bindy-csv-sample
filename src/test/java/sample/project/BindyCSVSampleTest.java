package sample.project;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static sample.project.BindyCSVSample.createBasicRoute;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BindyCSVSampleTest extends CamelTestSupport {


    @Test
    void should_support_standalone_mode() throws Exception {
        adviceWith(context, "sample-route", a -> {
            a.weaveAddLast().to("mock:end");
        });

        MockEndpoint end = context.getEndpoint("mock:end", MockEndpoint.class);

        context.start();

        NotifyBuilder notify = new NotifyBuilder(context).from("timer:foo").whenCompleted(1).create();
        assertTrue(
            notify.matches(20, TimeUnit.SECONDS), "1 message should be completed"
        );

        assertEquals("val1,,,val4,val5,,,,val9\n", end.getReceivedExchanges().get(0).getIn().getBody(String.class));
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() {
        return createBasicRoute();
    }
}
