package org.duniter.elasticsearch.user.model;

/*
 * #%L
 * Duniter4j :: Core Client API
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import org.duniter.core.client.model.elasticsearch.Record;

/**
 * Created by blavenie on 01/03/16.
 */
public class UserProfile extends Record {

    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_DESCRIPTION="description";
    public static final String PROPERTY_EMAIL="email";
    public static final String PROPERTY_LOCALE="locale";
    public static final String PROPERTY_AVATAR="avatar";
    public static final String PROPERTY_ADDRESS="address";
    public static final String PROPERTY_CITY="city";

    private String title;
    private String description;
    private String email;
    private String locale;
    private String address;
    private String city;
    private Attachment avatar;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Attachment getAvatar() {
        return avatar;
    }

    public void setAvatar(Attachment avatar) {
        this.avatar = avatar;
    }
}
