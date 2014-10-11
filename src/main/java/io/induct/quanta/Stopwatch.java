package io.induct.quanta;

import java.util.concurrent.TimeUnit;

/**
* @author Esko Suomi <suomi.esko@gmail.com>
* @since 11.10.2014
*/
public class Stopwatch {
    private final long start;
    private final String name;
    private final String variant;

    public Stopwatch(String name, String variant) {
        this.name = name;
        this.variant = variant;
        this.start = System.nanoTime();
    }
    public void stop() {
        long runtime = System.nanoTime() - start;
        System.out.println("\t" + name + " " + variant + " ran for " + TimeUnit.MILLISECONDS.convert(runtime, TimeUnit.NANOSECONDS) + "ms");
    }
}
