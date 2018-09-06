package org.duniter.elasticsearch.exception;

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


import org.duniter.core.exception.BusinessException;
import org.elasticsearch.rest.RestStatus;

/**
 *
 * Created by Benoit on 03/04/2015.
 */
public class DuplicateIndexIdException extends DuniterElasticsearchException{

    public DuplicateIndexIdException(Throwable cause) {
        super(cause);
    }

    public DuplicateIndexIdException(String msg, Object... args) {
        super(msg, args);
    }

    public DuplicateIndexIdException(String msg, Throwable cause, Object... args) {
        super(msg, args, cause);
    }


    @Override
    public RestStatus status() {
        return RestStatus.BAD_REQUEST;
    }
}
