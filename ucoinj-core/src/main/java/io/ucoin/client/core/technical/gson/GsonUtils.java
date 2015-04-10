package io.ucoin.client.core.technical.gson;

/*
 * #%L
 * UCoin Java Client :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
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


import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import io.ucoin.client.core.model.Identity;
import io.ucoin.client.core.model.Member;

public class GsonUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    public static GsonBuilder newBuilder() {
        return new GsonBuilder()
                // make sure date will be serialized
                .setDateFormat(DATE_PATTERN)
                // REgister Multimap adapter
                .registerTypeAdapter(Multimap.class, new MultimapTypeAdapter())
                // Register identity adapter
                .registerTypeAdapter(Identity.class, new IdentityTypeAdapter())
                .registerTypeAdapter(Member.class, new MemberTypeAdapter())
                ;
    }
}
