package io.induct.quanta.experiment;

import java.util.Map;

/**
 * Additional report context provider.
 *
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
public interface Context {
    Map<String, Object> provide();
}
