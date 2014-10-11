package io.induct.quanta.experiment;

/**
 * Thrown whenever the experiment is deemed invalid, usually due to incorrect configuration.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
class InvalidExperimentException extends IllegalArgumentException {
    public InvalidExperimentException(String message) {
        super(message);
    }
}
