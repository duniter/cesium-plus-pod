package org.duniter.elasticsearch.user.model;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
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

/**
 * Created by blavenie on 29/11/16.
 */
public enum UserEventCodes {

    NODE_STARTED,
    NODE_BMA_UP,
    NODE_BMA_DOWN,

    // Membership state
    MEMBER_JOIN,
    MEMBER_LEAVE,
    MEMBER_ACTIVE,
    MEMBER_REVOKE,
    MEMBER_EXCLUDE,

    // TX
    TX_SENT,
    TX_RECEIVED,

    // CERTIFICATION
    CERT_SENT,
    CERT_RECEIVED,

    // Message
    MESSAGE_RECEIVED,

    // Invitation
    INVITATION_TO_CERTIFY,

    // Like
    LIKE_RECEIVED,
    STAR__RECEIVED,
    ABUSE_RECEIVED,
    MODERATION_RECEIVED,

    // Comments
    NEW_COMMENT,
    UPDATE_COMMENT,
    NEW_REPLY_COMMENT,
    UPDATE_REPLY_COMMENT,

    FOLLOW_NEW_COMMENT,
    FOLLOW_UPDATE_COMMENT,

    FOLLOW_NEW,
    FOLLOW_UPDATE,
    FOLLOW_CLOSE
}
