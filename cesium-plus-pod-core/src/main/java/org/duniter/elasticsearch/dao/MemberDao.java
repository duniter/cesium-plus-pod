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

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.local.Identity;
import org.duniter.core.client.model.local.Member;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by blavenie on 30/01/19.
 */
public interface MemberDao extends Bean, TypeDao<MemberDao>{

    String TYPE = "member";

    List<Member> getMembers(String currencyId);

    boolean isExists(String currencyId, String pubkey);

    Identity create(Identity identity);

    Identity update(Identity identity);

    Set<String> getMemberPubkeys(String currency);

    void save(String currencyId, List<Member> members);

    void updateAsWasMember(String currency, Collection<String> wasMemberPubkeys);
}
