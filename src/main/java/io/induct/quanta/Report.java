package io.induct.quanta;

import com.google.common.base.Stopwatch;

import java.util.Map;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
public class Report<V> {

    public static class Details<V> {

        private final Stopwatch duration;
        private final V result;
        private final RuntimeException ex;

        public Details(Stopwatch duration, V result, RuntimeException ex) {
            this.duration = duration;
            this.result = result;
            this.ex = ex;
        }

        public Stopwatch getDuration() {
            return duration;
        }

        public V getResult() {
            return result;
        }

        public RuntimeException getEx() {
            return ex;
        }
    }

    private final String experiment;
    private final boolean match;
    private final Map<String, Object> payload;
    private final Details<V> control;
    private final Details<V> candidate;

    public Report(String experimentName, boolean match, Map<String, Object> payload, Details controlReport, Details candidateReport) {
        this.experiment = experimentName;
        this.match = match;
        this.payload = payload;
        this.control = controlReport;
        this.candidate = candidateReport;
    }

    public boolean resultsMatch() {
        return match;
    }

    public String getExperiment() {
        return experiment;
    }

    public boolean isMatch() {
        return match;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public Details<V> getControl() {
        return control;
    }

    public Details<V> getCandidate() {
        return candidate;
    }
}
