package io.induct.quanta;

/**
 * Variant describes critical code path being experimented on.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 * @param <V> Result value of the experiment path.
 */
public interface Variant<V> {
    V call();
}
