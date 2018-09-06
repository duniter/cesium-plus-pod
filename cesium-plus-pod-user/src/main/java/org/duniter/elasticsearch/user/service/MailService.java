package org.duniter.elasticsearch.user.service;

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


import org.duniter.core.exception.TechnicalException;
import org.duniter.core.model.SmtpConfig;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.user.PluginSettings;
import org.elasticsearch.common.inject.Inject;

/**
 * Created by Benoit on 30/03/2015.
 */
public class MailService extends AbstractService {

    private final org.duniter.core.service.MailService delegate;

    private final boolean enable;

    @Inject
    public MailService(final Duniter4jClient client,
                       final PluginSettings pluginSettings,
                       final CryptoService cryptoService,
                       final org.duniter.core.service.MailService delegate) {
        super("duniter.mail", client, pluginSettings, cryptoService);
        this.delegate = delegate;
        this.enable = pluginSettings.getMailEnable();
        // Init delegated service
        if (this.enable) {
            delegate.setSmtpConfig(createConfig(pluginSettings));
        }
    }

    /**
     * Send email
     */
    public void sendTextEmail(String subject, String textContent, String... recipients) {
        if (!this.enable) return;

        try {
            delegate.sendTextEmail(subject, textContent, recipients);
        }
        catch(TechnicalException e) {
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            }
            else {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Send email
     */
    public void sendHtmlEmail(String subject, String htmlContent, String... recipients) {
        if (!this.enable) return;

        try {
            delegate.sendHtmlEmail(subject, htmlContent, recipients);
        }
        catch(TechnicalException e) {
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            }
            else {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Send email
     */
    public void sendHtmlEmailWithText(String subject, String textContent, String htmlContent, String... recipients) {
        if (!this.enable) return;

        try {
            delegate.sendHtmlEmailWithText(subject, textContent, htmlContent, recipients);
        }
        catch(TechnicalException e) {
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            }
            else {
                logger.error(e.getMessage());
            }
        }
    }

    /* -- internal methods -- */

    protected SmtpConfig createConfig(PluginSettings pluginSettings) {
        SmtpConfig config = new SmtpConfig();
        config.setSmtpHost(pluginSettings.getMailSmtpHost());
        config.setSmtpPort(pluginSettings.getMailSmtpPort());
        config.setSmtpUsername(pluginSettings.getMailSmtpUsername());
        config.setSmtpPassword(pluginSettings.getMailSmtpPassword());
        config.setSenderAddress(pluginSettings.getMailFrom());
        config.setStartTLS(pluginSettings.isMailSmtpStartTLS());
        config.setUseSsl(pluginSettings.isMailSmtpUseSSL());
        return config;
    }

}
