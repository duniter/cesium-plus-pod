package org.duniter.elasticsearch.dao;

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

import org.duniter.elasticsearch.dao.handler.StringReaderHandler;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.Map;

/**
 * Created by blavenie on 03/04/17.
 */
public interface IndexTypeDao<T extends IndexTypeDao> extends IndexDao<T> {

    T createIndexIfNotExists();

    T deleteIndex();

    String getIndex();

    boolean existsIndex();

    XContentBuilder createTypeMapping();

    String getType();

    boolean isExists(String docId);

    Object getFieldById(String docId, String fieldName);

    Map<String, Object> getFieldsById(String docId, String... fieldNames);

    <B> B getTypedFieldById(String docId, String fieldName);

    Map<String, Object> getMandatoryFieldsById(String docId, String... fieldNames);

    void bulkFromClasspathFile(String classpathFile);

    void bulkFromClasspathFile(String classpathFile, StringReaderHandler handler);
}
