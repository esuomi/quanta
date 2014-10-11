package io.induct.quanta;

/**
 * Control enabling of the experiment. Experiment name is provided for additional lookups such as feature flag based
 * checks.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
public interface Enabler {
    boolean enabled(String experimentName);
}
