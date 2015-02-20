package nz.ac.auckland.scriptella.driver.http;

import nz.ac.auckland.morc.MorcTestBuilder;
import nz.ac.auckland.morc.TestBean;
import scriptella.configuration.ConfigurationEl;
import scriptella.configuration.ConfigurationFactory;
import scriptella.execution.EtlExecutor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Jack W Finlay - jfin404@aucklanduni.ac.nz
 */
public class IntegrationTest extends MorcTestBuilder {

    public void configure() {

        syncTest("Integration test", new TestBean() {
            @Override
            public void run() throws Exception {
                final EtlExecutor se = newEtlExecutor();
                se.execute();
            }
        }).addExpectation(syncExpectation("jetty:http://localhost:8080")
                .expectedHeaders(headers(header("abc", "123"), header("def", "456"), header("ghi", "789"))))
                .addExpectation(syncExpectation("jetty:http://localhost:8080")
                        .expectedHeaders(headers(header("abc", "jkl"), header("def", "mno"), header("ghi", "pqr"))))
                .addExpectation(syncExpectation("jetty:http://localhost:8080")
                        .expectedHeaders(headers(header("abc", "stu"), header("def", "vwx"), header("ghi", "yz"))));
    }

    protected EtlExecutor newEtlExecutor() {
        return new EtlExecutor(loadConfiguration("IntegrationTest.xml"));
    }

    protected ConfigurationEl loadConfiguration(final String path) {
        ConfigurationFactory cf = new ConfigurationFactory();
        //final URL resource = getClass().getResource(path);
        URL resource = null;
        try {
            resource = new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (resource == null) {
            throw new IllegalStateException("Resource " + path + " not found");

        }

        cf.setResourceURL(resource);

        return cf.createConfiguration();


    }

}
