package org.duniter.elasticsearch.user.dao;

/*
 * #%L
 * Duniter4j :: Core API
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


import org.duniter.elasticsearch.dao.IndexTypeDao;

/**
 * Created by Benoit on 30/03/2015.
 */
public interface RecordDao<T extends RecordDao> extends IndexTypeDao<T> {

    String TYPE = "record";

    String PROPERTY_AVATAR = "avatar";

    String create(final String json);

    void update(final String id, final String json);

    void checkSameDocumentIssuer(String id, String expectedIssuer);

}
