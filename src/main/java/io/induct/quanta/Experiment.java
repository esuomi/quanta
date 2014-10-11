package io.induct.quanta;

import java.util.LinkedHashMap;
import java.util.Map;

/**
* @author Esko Suomi <suomi.esko@gmail.com>
* @since 11.10.2014
*/
class Experiment<V> {
    private final String name;
    private final Exp<V> exp;

    public Experiment(String name, Variants<V> variants) {
        this.name = name;

        Exp<V> exp = new Exp<>();
        variants.call(exp);

        if (exp.control == null) {
            if (exp.candidate == null) {
                throw new InvalidExperimentException("Both control and candidate variants are missing");
            } else {
                throw new InvalidExperimentException("Control variant is missing");
            }
        }
        this.exp = exp;
    }

    public V run() {
        Stopwatch controlTimer = new Stopwatch(name, "control");
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
            Stopwatch candidateTimer = new Stopwatch(name, "candidate");
            V candidateResult = null;
            try {
                candidateResult = exp.candidate.call();
            } catch (RuntimeException candidateE) {
                // swallow
            } finally {
                candidateTimer.stop();
            }
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("context", exp.context.call());
            payload.put("control", controlResult);
            payload.put("candidate", candidateResult);

            if (exp.equalTo.apply(controlResult, candidateResult)) {
                // publish match
                System.out.println("\t'" + name + "' experiment match! control was " + controlResult + ", candidate was " + candidateResult);
            } else {
                // publish mismatch
                System.out.println("\t'" + name + "' experiment mismatch! control was " + controlResult + ", candidate was " + candidateResult);
            }
            exp.publish.call(name, payload);
        }

        if (controlEx != null) throw controlEx;
        return controlResult;
    }

    private static class InvalidExperimentException extends RuntimeException {
        public InvalidExperimentException(String message) {
            super(message);
        }
    }
}
