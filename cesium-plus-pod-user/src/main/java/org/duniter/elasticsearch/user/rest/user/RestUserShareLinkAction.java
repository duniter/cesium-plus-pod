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
import org.elasticsearch.rest.RestController;
import org.nuiton.i18n.I18n;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class RestUserShareLinkAction extends AbstractRestShareLinkAction implements AbstractRestShareLinkAction.OGDataResolver{

    private PluginSettings pluginSettings;
    private UserService userService;

    @Inject
    public RestUserShareLinkAction(final PluginSettings pluginSettings, final RestController controller, final Client client,
                                   final UserService userService) {
        super(pluginSettings.getDelegate(), controller, client, UserService.INDEX, UserService.PROFILE_TYPE);
        setResolver(this);

        if (StringUtils.isBlank(pluginSettings.getClusterRemoteUrlOrNull())) {
            log.warn(String.format("The cluster address can not be published on the network. /\\!\\\\ Fill in the options [cluster.remote.xxx] in the configuration (recommended)."));
        }
        this.pluginSettings = pluginSettings;
        this.userService = userService;
    }

    @Override
    public OGData resolve(String id) throws DuniterElasticsearchException, BusinessException {
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

                String pubkeyMessage = I18n.l(locale, "duniter.user.share.pubkey", id);

                // og:title
                if (StringUtils.isNotBlank(profile.getTitle())) {
                    data.title = profile.getTitle();
                    data.description = pubkeyMessage;
                }
                else {
                    data.title = pubkeyMessage;
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
                    String baseUrl = pluginSettings.getClusterRemoteUrlOrNull();
                    data.image = StringUtils.isBlank(baseUrl) ? "" : baseUrl;
                    data.image += RestImageAttachmentAction.computeImageUrl(UserService.INDEX, UserService.PROFILE_TYPE, id, UserProfile.PROPERTY_AVATAR, profile.getAvatar().getContentType());
                    data.imageHeight = 200; // min size for Facebook
                    data.imageWidth = 200;
                }

                // og:url
                data.url = pluginSettings.getShareUserLinkUrl()
                        .replace("{pubkey}", id)
                        .replace("{title}", URLEncoder.encode(profile.getTitle(), "UTF-8"));
            }
            else {

                // og:title
                String pubkeyMessage = I18n.t("duniter.user.share.pubkey", id);
                data.title = pubkeyMessage;

                // og:description
                data.description = I18n.t("duniter.user.share.description");

                // og:url
                data.url = pluginSettings.getShareUserLinkUrl()
                        .replace("{pubkey}", id)
                        .replace("{title}", "");
            }

            // og:type
            data.type = "website";

            // og:site_name
            data.siteName = pluginSettings.getShareSiteName();

            // default og:image
            if (StringUtils.isBlank(data.image)) {
                data.image = pluginSettings.getShareDefaultImageUrl();
                data.imageType = "image/png";
                data.imageHeight = 200;
                data.imageWidth = 200;
            }

            return data;
        }
        catch(UnsupportedEncodingException e) {
            throw new TechnicalException(e);
        }
    }
}
