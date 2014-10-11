package io.induct.quanta.experiment;

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
}
