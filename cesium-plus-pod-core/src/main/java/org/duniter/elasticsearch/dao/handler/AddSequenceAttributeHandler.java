package org.duniter.elasticsearch.dao.handler;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
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

import java.util.regex.Pattern;

public class AddSequenceAttributeHandler implements StringReaderHandler {
        private int order;
        private final String attributeName;
        private final Pattern filterPattern;
        public AddSequenceAttributeHandler(String attributeName, String filterRegex, int startValue) {
            this.order = startValue;
            this.attributeName = attributeName;
            this.filterPattern = Pattern.compile(filterRegex);
        }

        @Override
        public String onReadLine(String line) {
            // add 'order' field into
            if (filterPattern.matcher(line).matches()) {
                return String.format("%s, \"%s\": %d}",
                        line.substring(0, line.length()-1),
                        attributeName,
                        order++);
            }
            return line;
        }
    }
