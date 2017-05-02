package org.duniter.elasticsearch.subscription.dao.execution;

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

import org.duniter.elasticsearch.subscription.dao.SubscriptionIndexTypeDao;
import org.duniter.elasticsearch.subscription.model.SubscriptionExecution;
import org.duniter.elasticsearch.subscription.model.SubscriptionRecord;

import java.util.List;

/**
 * Created by blavenie on 03/04/17.
 */
public interface SubscriptionExecutionDao<T extends SubscriptionIndexTypeDao> extends SubscriptionIndexTypeDao<T> {

    String TYPE = "execution";

    SubscriptionExecution getLastExecution(SubscriptionRecord record);

    SubscriptionExecution getLastExecution(String recipient, String subscriptionType, String recordId);

    Long getLastExecutionTime(String recipient, String subscriptionType, String recordId);

    Long getLastExecutionTime(SubscriptionRecord record);
}
