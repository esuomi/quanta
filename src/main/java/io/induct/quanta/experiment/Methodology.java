package io.induct.quanta.experiment;

import java.util.Collections;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Capturing container for {@link Experiment#Experiment(String, Methodology)} to create a readable DSL.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 * @param <V> Result value of the experiment path.
 * @see Exp
 */
public interface Methodology<V> {
    /**
     * Describe your methodology by providing values to method parameter.
     *
     * @param exp Experiment methodology descriptor
     */
    void describe(Exp<V> exp);

    /**
     * Describe your experiment by setting the applicable public fields of this class.
     *
     * @author Esko Suomi <suomi.esko@gmail.com>
     * @since 11.10.2014
     * @param <V> Result value of the experiment path.
     */
    final class Exp<V> {
        /**
         * Exp is not meant to be externally instantiable. If you want to share your experiment methodology, store a
         * reference to {@link io.induct.quanta.experiment.Methodology} instead.
         */
        Exp() {}

        /**
         * Control path. Result of this path will always be returned by the experiment. If control throws an exception, it
         * will be rethrown.
         */
        public Variant<V> control = null;
        /**
         * Candidate path. Will be executed as controlled by {@link #enabled}. Result will be compared with control result.
         * Exceptions will be swallowed but visible in report.
         */
        public Variant<V> candidate = null;
        /**
         * Compare the result of control and candidate. Override for bridging eg. different versions of service under
         * refactoring. Uses object equality by default.
         */
        public BiFunction<V, V, Boolean> match = Objects::equals;
        /**
         * Control experiment's execution. When enabled, candidate execution and comparison logic is run. Defaults to false.
         */
        public Enabler enabled = (name) -> false;
        /**
         * Additional report publishing. Use for immediate reaction in code or other non-default publishing of results.
         * Report will only be published when the candidate has been executed.
         */
        public Publisher publish = (report) -> {};
        /**
         * Additional context of the experiment, usually obtained beforehand. Will be included in report if candidate is
         * executed.
         */
        public Context context = () -> Collections.EMPTY_MAP;
    }
}
