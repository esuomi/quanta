package io.induct.quanta;

import java.util.Map;

/**
 * @author Esko Suomi <suomi.esko@gmail.com>
 * @since 11.10.2014
 */
public class Report {
    private final String experimentName;
    private final boolean match;
    private final Map<String, Object> payload;

    public Report(String experimentName, boolean match, Map<String, Object> payload) {

        this.experimentName = experimentName;
        this.match = match;
        this.payload = payload;
    }

    public boolean resultsMatch() {
        return match;
    }
}
