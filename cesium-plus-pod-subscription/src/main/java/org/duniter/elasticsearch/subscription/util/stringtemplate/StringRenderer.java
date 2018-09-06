package org.duniter.elasticsearch.subscription.util.stringtemplate;

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

import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.StringUtils;
import org.nuiton.i18n.I18n;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * Add format capabilities: i18n, pubkey
 * Created by blavenie on 10/04/17.
 */
public class StringRenderer extends org.stringtemplate.v4.StringRenderer{

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return formatString == null ? (String)o :
                (formatString.equals("pubkey") ? ModelUtils.minifyPubkey((String)o) :
                        (formatString.startsWith("i18n") ? toI18nString(o, formatString, locale) :
                            super.toString(o, formatString, locale)));
    }

    protected String toI18nString(Object key, String formatString, Locale locale) {
        String[] params = formatString.startsWith("i18n:") ? formatString.substring(5).split(",") : null;
        if (CollectionUtils.isNotEmpty(params)) {
            return I18n.l(locale, key.toString(), params);
        }
        return I18n.l(locale, key.toString());
    }
}
