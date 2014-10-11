package io.induct.quanta.experiment;

import com.google.common.collect.ImmutableMap;
import io.induct.quanta.experiment.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ExperimentTest {

    private static final boolean ALWAYS = true;

    private Publisher testPublisher;
    private Report publishedReport;
    private Methodology<Boolean> alwaysTrueMethodology = (e) -> {
        e.control   = ()     -> true;
        e.candidate = ()     -> true;
        e.enabled   = (name) -> true;
        e.publish   = testPublisher;
    };

    @Before
    public void setUp() throws Exception {
        testPublisher = (report) -> {
            publishedReport = report;
        };
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveNonNullName() throws Exception {
        Experiment<Boolean> misconfigured = new Experiment<>(null, alwaysTrueMethodology);
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveNonEmptyName() throws Exception {
        Experiment<Boolean> misconfigured = new Experiment<>("", alwaysTrueMethodology);
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveNonNullMethodology() throws Exception {
        Experiment<Boolean> misconfigured = new Experiment<>("invalid-methodology", null);
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveBothControlAndCandidateSet() throws Exception {
        Experiment<String> misconfigured = new Experiment<>("not-like-this", (e) -> {
            e.control   = null;
            e.candidate = null;
        });
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveControlSet() throws Exception {
        Experiment<String> misconfigured = new Experiment<>("not-like-this", (e) -> {
            e.control   = null;
            e.candidate = () -> "candidate";
        });
    }

    @Test(expected = InvalidExperimentException.class)
    public void mustHaveCandidateSet() throws Exception {
        Experiment<String> misconfigured = new Experiment<>("not-like-this", (e) -> {
            e.control   = () -> "control";
            e.candidate = null;
        });
    }

    boolean controlTriggered = false;
    boolean candidateTriggered = false;
    @Test
    public void runsBothControlAndCandidateWhenEnabledIsTrue() throws Exception {
        Experiment<Boolean> exp = new Experiment<>("forced-enabled", (e) -> {
            e.control   = ()     -> controlTriggered = true;
            e.candidate = ()     -> candidateTriggered = true;
            e.enabled   = (name) -> ALWAYS;
        });
        exp.run();
        assertEquals("control variant was not triggered", true, controlTriggered);
        assertEquals("candidate variant was not triggered", true, candidateTriggered);
    }

    @Test
    public void canOverrideResultMatchComparison() throws Exception {
        Experiment<String> names = new Experiment<>("names", (e) -> {
            e.control   = ()     -> "John";
            e.candidate = ()     -> "Jack";
            e.match     = (a,b)  -> a.charAt(0) == b.charAt(0);
            e.enabled   = (name) -> ALWAYS;
            e.publish   = testPublisher;
        });
        names.run();
        assertTrue("Experiment should not have failed", publishedReport.resultsMatch());
    }

    @Test
    public void canBeGivenExtraContextForReport() throws Exception {
        Experiment<Integer> numbers = new Experiment<>("numbers", (e) -> {
            e.control   = ()     -> 1;
            e.candidate = ()     -> 2;
            e.enabled   = (name) -> ALWAYS;
            e.context   = ()     -> ImmutableMap.of("description", "experiment on even/odd numbers");
            e.publish   = testPublisher;
        });
        numbers.run();
        assertThat("description was not in published report payload",
                publishedReport.getPayload().get("description"),
                is("experiment on even/odd numbers"));
    }

    /*
         TODO: to reach feature parity...
            *) e.cleaner - Kind of against this since Maps in Java are legendarily terrible...
            *) feature flag helper
            *) dat-analysis equivalent?
          */
}