package io.induct.quanta;

/**
 * Use to capture published report of the experiment run. Only called when candidate has also been executed.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
public interface Publisher {
    void publish(Report report);
}
