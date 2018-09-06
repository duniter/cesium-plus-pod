package org.duniter.elasticsearch.util.springtemplate;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Subscription plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.stringtemplate.v4.AttributeRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateRenderer implements AttributeRenderer {

    public DateRenderer() {
    }

    public String toString(Object o, String formatString, Locale locale) {
        if(formatString == null) {
            formatString = "short";
        }

        Date d;
        if(o instanceof Calendar) {
            d = ((Calendar)o).getTime();
        } else {
            d = (Date)o;
        }

        Integer styleI = (Integer)org.stringtemplate.v4.DateRenderer.formatToInt.get(formatString);
        Object f;
        if(styleI == null) {
            f = new SimpleDateFormat(formatString, locale);
        } else {
            int style = styleI.intValue();
            if(formatString.startsWith("date:")) {
                f = DateFormat.getDateInstance(style, locale);
            } else if(formatString.startsWith("time:")) {
                f = DateFormat.getTimeInstance(style, locale);
            } else {
                f = DateFormat.getDateTimeInstance(style, style, locale);
            }
        }

        return ((DateFormat)f).format(d);
    }
}