package org.duniter.core.client.service.local;

/*
 * #%L
 * UCoin Java :: Core Client API
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

import org.duniter.core.beans.Service;
import org.duniter.core.client.model.local.Peer;

import java.util.List;

/**
 * Created by eis on 07/02/15.
 */
public interface PeerService extends Service {

    Peer save(final Peer peer);

    /**
     * Return a (cached) active peer, by currency id
     * @param currencyId
     * @return
     */
    Peer getActivePeerByCurrencyId(String currencyId);

    /**
     * Return a (cached) peer list, by currency id
     * @param currencyId
     * @return
     */
    List<Peer> getPeersByCurrencyId(String currencyId);

    /**
     * Fill all cache need for currencies
     * @param context
     * @param accountId
     */
    void loadCache(long accountId);
}
