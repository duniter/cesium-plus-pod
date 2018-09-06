package org.duniter.elasticsearch.user.rest.user;

import com.google.common.html.HtmlEscapers;
import org.duniter.core.exception.BusinessException;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.rest.attachment.RestImageAttachmentAction;
import org.duniter.elasticsearch.rest.share.AbstractRestShareLinkAction;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.duniter.elasticsearch.user.service.UserService;
import org.duniter.elasticsearch.util.opengraph.OGData;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestController;
import org.nuiton.i18n.I18n;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class RestUserShareLinkAction extends AbstractRestShareLinkAction {

    @Inject
    public RestUserShareLinkAction(final Settings settings, final RestController controller, final Client client,
                                   final PluginSettings pluginSettings,
                                   final UserService userService) {
        super(settings, controller, client, UserService.INDEX, UserService.PROFILE_TYPE,
                pluginSettings.getShareBaseUrl(),
                createResolver(pluginSettings, userService));

        if (StringUtils.isBlank(pluginSettings.getShareBaseUrl())) {
            log.warn(I18n.t("duniter4j.es.share.error.noBaseUrl", "duniter.share.base.url"));
        }
    }

    protected static AbstractRestShareLinkAction.OGDataResolver createResolver(
            final PluginSettings pluginSettings,
            final UserService userService) throws DuniterElasticsearchException, BusinessException {

        return (id) -> {
            try {
                UserProfile profile = userService.getUserProfileForSharing(id);

                OGData data = new OGData();

                if (profile != null) {

                    // og:locale
                    Locale locale;
                    if (StringUtils.isNotBlank(profile.getLocale())) {
                        locale = new Locale(profile.getLocale());
                        data.locale = profile.getLocale();
                    }
                    else {
                        locale = I18n.getDefaultLocale();
                    }
                    data.locale = locale.toString();

                    String pubkey = I18n.l(locale, "duniter.user.share.pubkey", id);

                    // og:title
                    if (StringUtils.isNotBlank(profile.getTitle())) {
                        data.title = profile.getTitle();
                        data.description = pubkey;
                    }
                    else {
                        data.title = pubkey;
                        data.description = "";
                    }

                    // og:description
                    if (StringUtils.isNotBlank(data.description)) data.description += " | ";
                    if (StringUtils.isNotBlank(profile.getDescription())) {
                        data.description += HtmlEscapers.htmlEscaper().escape(profile.getDescription());
                    }
                    else {
                        data.description += I18n.l(locale, "duniter.user.share.description");
                    }

                    // og:image
                    if (profile.getAvatar() != null && StringUtils.isNotBlank(profile.getAvatar().getContentType())) {
                        String baseUrl = pluginSettings.getShareBaseUrl();
                        data.image = StringUtils.isBlank(baseUrl) ? "" : baseUrl;
                        data.image += RestImageAttachmentAction.computeImageUrl(UserService.INDEX, UserService.PROFILE_TYPE, id, UserProfile.PROPERTY_AVATAR, profile.getAvatar().getContentType());
                        data.imageHeight = 100;
                        data.imageWidth = 100;
                    }

                    // og:url
                    data.url = String.format("%s/#/app/wot/%s/%s",
                            pluginSettings.getCesiumUrl(),
                            id,
                            URLEncoder.encode(profile.getTitle(), "UTF-8"));
                }
                else {

                    // og:title
                    String pubkey = I18n.t("duniter.user.share.pubkey", id);
                    data.title = pubkey;

                    // og:description
                    data.description = I18n.t("duniter.user.share.description");

                    // og:url
                    data.url = String.format("%s/#/app/wot/%s/%s",
                            pluginSettings.getCesiumUrl(),
                            id,
                            "");
                }

                // og:type
                data.type = "website";

                // og:site_name
                data.siteName = pluginSettings.getShareSiteName();

                // default og:image
                if (StringUtils.isBlank(data.image)) {
                    data.image = pluginSettings.getCesiumUrl() + "/img/logo_200px.png";
                    data.imageType = "image/png";
                    data.imageHeight = 200;
                    data.imageWidth = 200;
                }

                return data;
            }
            catch(UnsupportedEncodingException e) {
                throw new TechnicalException(e);
            }
        };
    }
}
