package org.duniter.elasticsearch.user.rest.message.compat;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import org.duniter.elasticsearch.rest.AbstractRestPostMarkAsReadAction;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.user.service.MessageService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestController;

/**
 * /message/record has been replaced by /message/inbox
 * @deprecated
 */
@Deprecated
public class RestMessageRecordMarkAsReadAction extends AbstractRestPostMarkAsReadAction {

    @Inject
    public RestMessageRecordMarkAsReadAction(Settings settings, RestController controller, Client client,
                                             RestSecurityController securityController,
                                             MessageService messageService) {
        super(settings, controller, client, securityController, MessageService.INDEX, MessageService.RECORD_TYPE,
                (id, signature) -> {
                    messageService.markMessageAsRead(id, signature);
                });
    }
}