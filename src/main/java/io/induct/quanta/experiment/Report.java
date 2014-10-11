package io.induct.quanta.experiment;

import com.google.common.base.Stopwatch;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Describe execution of the experiment.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 * @param <V> Result value of the experiment path.
 */
public final class Report<V> {

    /**
     * Variant report details.
     * @param <V> Result value of the experiment path.
     */
    final static class Details<V> {

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
    private final LocalDateTime timestamp;

    Report(String experimentName, boolean match, Map<String, Object> payload, Details controlReport, Details candidateReport) {
        this.timestamp = LocalDateTime.now();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
