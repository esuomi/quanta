package io.induct.quanta;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 9.10.2014
 */
public class Science {

    public static void main(String[] args) {
        /*
         TODO: to reach feature parity...
            *) e.cleaner - Kind of against this since Maps in Java are legendarily terrible...
            *) feature flag helper
            *) reports aren't even close to what dat-science has
            *) dat-analysis equivalent
          */
        Experiment<String> experiment = new Experiment<>("choose-name",
                (e) -> {
                    e.control   = ()     -> { sleep(300); return "John"; };
                    e.candidate = ()     -> { sleep(20); return "Jack"; };
                    e.equalTo   = (a, b) -> b.charAt(0) == b.charAt(0);
                    e.enabled   = (name) -> Math.random() > 0.5;
                    e.context   = () -> {
                        Map<String, Object> ctx = new HashMap<>();
                        ctx.put("in-spirit port of", "https://github.com/github/dat-science");
                        return ctx;
                    };
                    e.publish   = (name, payload) -> publish(name, payload);
                }
        );

        for (int i = 0; i < 3; i++) {
            System.out.println("result #" + i + " = " + experiment.run());
        }
    }

    private static void publish(String name, Map<String, Object> payload) {
        System.out.println("\tAdditional publish for '" + name + "' => " + payload);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Experiment<V> {
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

    private static interface Variants<I> {
        void call(Exp<I> i);
    }

    private static class Exp<V> {
        public Variant<V> control = null;
        public Variant<V> candidate = null;
        public BiFunction<V, V, Boolean> equalTo = Objects::equals;
        public Enabler enabled = (name) -> false;
        public Publisher publish = (name, payload) -> {};
        public Context context = () -> Collections.EMPTY_MAP;
    }

    private static interface Variant<V> {
        V call();
    }
    private static interface Context {
        Map<String, Object> call();
    }
    private static interface Enabler {
        boolean call(String experimentName);
    }
    private static interface Publisher {
        void call(String experimentName, Map<String, Object> payload);
    }

    private static class Stopwatch {
        private final long start;
        private final String name;
        private final String variant;

        public Stopwatch(String name, String variant) {
            this.name = name;
            this.variant = variant;
            this.start = System.nanoTime();
        }
        public void stop() {
            long runtime = System.nanoTime() - start;
            System.out.println("\t" + name + " " + variant + " ran for " + TimeUnit.MILLISECONDS.convert(runtime, TimeUnit.NANOSECONDS) + "ms");
        }
    }
}
