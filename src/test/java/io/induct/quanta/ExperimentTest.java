package io.induct.quanta;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExperimentTest {

    private static final boolean ALWAYS = true;

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
        });
        names.run();

    }
}