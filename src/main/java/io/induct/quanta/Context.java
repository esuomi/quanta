package io.induct.quanta;

import java.util.Map;

/**
* @author Esko Suomi <suomi.esko@gmail.com>
* @since 11.10.2014
*/
public interface Context {
    Map<String, Object> call();
}
