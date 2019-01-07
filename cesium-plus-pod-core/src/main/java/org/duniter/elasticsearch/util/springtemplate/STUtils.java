package org.duniter.elasticsearch.util.springtemplate;

import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.Date;

public class STUtils {

    private STUtils() {
        /*help class*/
    }

    public static STGroup newSTGroup(String dirName) {
        // Configure springtemplate engine
        STGroup templates = new STGroupDir(dirName, '$', '$');
        templates.registerRenderer(Date.class, new DateRenderer());
        templates.registerRenderer(String.class, new StringRenderer());
        return templates;
    }


}
