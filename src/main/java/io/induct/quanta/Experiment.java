package io.induct.quanta;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 * @param <V>
 */
public class Experiment<V> {
    private final String name;
    private final Exp<V> exp;

    public Experiment(String name, Methodology<V> methodology) {
        if (Strings.isNullOrEmpty(name)) {
            throw new InvalidExperimentException("Invalid name '" + name + "'. Experiment must have a non-null, non-empty name");
        }
        this.name = name;

        if (methodology == null) {
            throw new InvalidExperimentException("Methodology is null");
        }

        Exp<V> exp = new Exp<>();
        methodology.call(exp);

        if (exp.control == null) {
            if (exp.candidate == null) {
                throw new InvalidExperimentException("Both control and candidate variants are missing");
            } else {
                throw new InvalidExperimentException("Control variant is missing");
            }
        }
        if (exp.candidate == null) {
            throw new InvalidExperimentException("Candidate variant is missing");
        }
        this.exp = exp;
    }

    public V run() {
        Stopwatch controlTimer = Stopwatch.createStarted();
        V controlResult = null;
        RuntimeException controlEx = null;
        try {
            controlResult = exp.control.call();
        } catch (RuntimeException e) {
            controlEx = e;
        } finally {
            controlTimer.stop();
        }

        if (exp.enabled.call(name)) {
            Stopwatch candidateTimer = Stopwatch.createStarted();
            V candidateResult = null;
            RuntimeException candidateEx = null;
            try {
                candidateResult = exp.candidate.call();
            } catch (RuntimeException candidateE) {
                candidateEx = candidateE;
            } finally {
                candidateTimer.stop();
            }

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("context", exp.context.call());
            payload.put("control", controlResult);
            payload.put("candidate", candidateResult);

            boolean match = exp.match.apply(controlResult, candidateResult);
            // TODO: Hook https://dropwizard.github.io/ lib here for automatic result publishing
            if (match) {
                // publish match
            } else {
                // publish mismatch
            }
            Report.Details<V> controlReport = new Report.Details<>(controlTimer, controlResult, controlEx);
            Report.Details<V> candidateReport = new Report.Details<>(candidateTimer, candidateResult, candidateEx);
            Report<V> report = new Report<>(name, match, payload, controlReport, candidateReport);
            exp.publish.report(report);
        }

        if (controlEx != null) throw controlEx;
        return controlResult;
    }

}
