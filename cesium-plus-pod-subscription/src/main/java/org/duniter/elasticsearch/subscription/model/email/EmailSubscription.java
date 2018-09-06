package org.duniter.elasticsearch.subscription.model.email;

/*
 * #%L
 * Duniter4j :: ElasticSearch GChange plugin
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

import org.duniter.elasticsearch.subscription.model.SubscriptionRecord;

/**
 * Created by blavenie on 01/12/16.
 */
public class EmailSubscription extends SubscriptionRecord<EmailSubscription.Content> {

    public static final String TYPE = "email";

    public static Content newContent() {
        return new EmailSubscription.Content();
    }

    public enum Frequency {
        daily,
        weekly
    }

    public static class Content {

        public static final String PROPERTY_EMAIL = "email";
        public static final String PROPERTY_FREQUENCY = "frequency";
        public static final String PROPERTY_LOCALE = "locale";
        public static final String PROPERTY_INCLUDES = "includes";
        public static final String PROPERTY_EXCLUDES = "excludes";

        private String email;
        private String[] includes;
        private String[] excludes;
        private String locale;
        private Frequency frequency;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String[] getIncludes() {
            return includes;
        }

        public void setIncludes(String[] includes) {
            this.includes = includes;
        }

        public String[] getExcludes() {
            return excludes;
        }

        public void setExcludes(String[] excludes) {
            this.excludes = excludes;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public Frequency getFrequency() {
            return frequency;
        }

        public void setFrequency(Frequency frequency) {
            this.frequency = frequency;
        }
    }

}
