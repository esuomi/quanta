package io.induct.quanta;

import java.util.*;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 9.10.2014
 */
public class Science {

    public static void main(String[] args) {
        /*
         TODO: to reach feature parity...
            *) e.cleaner - Kind of against this since Maps in Java are legendarily terrible...
            *) feature flag helper
            *) reports aren't even close to what dat-science has
            *) dat-analysis equivalent
          */
        Experiment<String> experiment = new Experiment<>("choose-name",
                (e) -> {
                    e.control   = ()     -> { sleep(300); return "John"; };
                    e.candidate = ()     -> { sleep(20); return "Jack"; };
                    e.equalTo   = (a, b) -> b.charAt(0) == b.charAt(0);
                    e.enabled   = (name) -> Math.random() > 0.5;
                    e.context   = () -> {
                        Map<String, Object> ctx = new HashMap<>();
                        ctx.put("in-spirit port of", "https://github.com/github/dat-science");
                        return ctx;
                    };
                    e.publish   = (name, payload) -> publish(name, payload);
                }
        );

        for (int i = 0; i < 3; i++) {
            System.out.println("result #" + i + " = " + experiment.run());
        }
    }

    private static void publish(String name, Map<String, Object> payload) {
        System.out.println("\tAdditional publish for '" + name + "' => " + payload);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
