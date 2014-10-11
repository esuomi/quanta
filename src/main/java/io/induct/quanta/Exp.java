package io.induct.quanta;

import java.util.Collections;
import java.util.Objects;
import java.util.function.BiFunction;

/**
* @author Esko Suomi <suomi.esko@gmail.com>
* @since 11.10.2014
*/
public class Exp<V> {
    public Variant<V> control = null;
    public Variant<V> candidate = null;
    public BiFunction<V, V, Boolean> equalTo = Objects::equals;
    public Enabler enabled = (name) -> false;
    public Publisher publish = (name, payload) -> {};
    public Context context = () -> Collections.EMPTY_MAP;
}
