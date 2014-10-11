package io.induct.quanta;

/**
* @author Esko Suomi <suomi.esko@gmail.com>
* @since 11.10.2014
*/
public interface Methodology<I> {
    void call(Exp<I> i);
}
