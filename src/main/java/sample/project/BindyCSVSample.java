package sample.project;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

public final class BindyCSVSample {

    public static void main(String[] args) throws Exception {
        // create a CamelContext
        try (CamelContext camel = new DefaultCamelContext()) {
            camel.addRoutes(createBasicRoute());
            camel.start();

            Thread.sleep(10_000);
        }
    }

    static RouteBuilder createBasicRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("timer:foo")
                        .routeId("sample-route")
                        .setBody(ex -> {
                            Model model = new Model();
                            model.setField1("val1");
                            model.setField4("val4");
                            model.setField5("val5");
                            model.setField9("val9");
                            return model;
                        })
                        .marshal(new BindyCsvDataFormat(Model.class))
                        .log("Marshalled: ${body}");
            }
        };
    }

    @CsvRecord(separator = ",")
    public static class Model {
        @DataField(pos = 1)
        private String field1;
        @DataField(pos = 4)
        private String field4;
        @DataField(pos = 5)
        private String field5;
        @DataField(pos = 9)
        private String field9;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField4() {
            return field4;
        }

        public void setField4(String field4) {
            this.field4 = field4;
        }

        public String getField5() {
            return field5;
        }

        public void setField5(String field5) {
            this.field5 = field5;
        }

        public String getField9() {
            return field9;
        }

        public void setField9(String field9) {
            this.field9 = field9;
        }
    }
}
