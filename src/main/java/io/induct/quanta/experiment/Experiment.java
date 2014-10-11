package io.induct.quanta.experiment;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

/**
 * Use Experiment to test critical paths of your in-progress refactoring. Experiment is not an A/B test as it always
 * returns the control result, Experiment merely tests whether the old and the new path results are equivalent and what
 * is their performance.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 * @param <V> Result value of the experiment path.
 *
 * @see Methodology
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
        methodology.describe(exp);

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
        Report.Details<V> controlReport = runVariant(exp.control);
        if (exp.enabled.enabled(name)) {
            Report.Details<V> candidateReport = runVariant(exp.candidate);
            boolean match = exp.match.apply(controlReport.getResult(), candidateReport.getResult());
            // TODO: Hook https://dropwizard.github.io/ lib here for automatic result publishing
            if (match) {
                // publish match
            } else {
                // publish mismatch
            }

            Report<V> report = new Report<>(name, match, exp.context.provide(), controlReport, candidateReport);
            exp.publish.publish(report);
        }

        if (controlReport.getEx() != null) throw controlReport.getEx();
        return controlReport.getResult();
    }

    private Report.Details<V> runVariant(Variant<V> variant) {
        Stopwatch variantTimer = Stopwatch.createStarted();
        V variantResult = null;
        RuntimeException variantEx = null;
        try {
            variantResult = variant.call();
        } catch (RuntimeException candidateE) {
            variantEx = candidateE;
        } finally {
            variantTimer.stop();
        }
        return new Report.Details<>(variantTimer, variantResult, variantEx);
    }

}
