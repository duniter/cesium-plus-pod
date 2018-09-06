package org.duniter.elasticsearch.rest.share;

import org.apache.http.entity.ContentType;
import org.duniter.core.exception.BusinessException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.util.opengraph.OGData;
import org.duniter.elasticsearch.util.springtemplate.STUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.nuiton.i18n.I18n;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Locale;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public abstract class AbstractRestShareLinkAction extends BaseRestHandler {

    protected final ESLogger log;

    public interface OGDataResolver {
        OGData resolve(String id) throws DuniterElasticsearchException, BusinessException;
    }

    private OGDataResolver resolver;
    private STGroup templates;
    private String urlPattern;

    public AbstractRestShareLinkAction(Settings settings, RestController controller, Client client,
                                       String indexName,
                                       String typeName,
                                       String shareBaseUrl,
                                       OGDataResolver resolver
                                        ) {
        super(settings, controller, client);
        log = Loggers.getLogger("duniter.rest." + indexName, settings, String.format("[%s]", indexName));

        String pathPattern = String.format("/%s/%s/%s/_share", indexName, typeName, "%s");
        controller.registerHandler(GET,
                String.format(pathPattern, "{id}"),
                this);
        this.urlPattern = (shareBaseUrl != null ? shareBaseUrl : "") + pathPattern;
        this.resolver = resolver;

        // Configure springtemplate engine
        this.templates = STUtils.newSTGroup("org/duniter/elasticsearch/templates");
        Preconditions.checkNotNull(this.templates.getInstanceOf("html_share"), "Unable to load ST template for share page");
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel restChannel, Client client) throws Exception {
        String id = request.param("id");

        try {

            OGData data = resolver.resolve(id);
            Preconditions.checkNotNull(data);
            Preconditions.checkNotNull(data.title);

            // Compute HTML content
            ST template = templates.getInstanceOf("html_share");
            template.add("type", data.type);
            template.add("title", data.title);
            template.add("summary", StringUtils.truncate(data.description, 500));
            template.add("description", data.description);
            template.add("siteName", data.siteName);
            template.add("image", data.image);
            template.add("url", String.format(urlPattern, id));
            template.add("redirectUrl", data.url);
            template.add("locale", data.locale);
            template.add("imageHeight", data.imageHeight);
            template.add("imageWidth", data.imageWidth);
            if (StringUtils.isNotBlank(data.url)) {
                Locale locale = data.locale != null ? new Locale(data.locale) : I18n.getDefaultLocale();
                template.add("redirectMessage", I18n.l(locale, "duniter4j.share.redirection.help"));
            }

            String html = template.render();

            restChannel.sendResponse(new BytesRestResponse(OK, ContentType.TEXT_HTML.getMimeType(), html));
        }
        catch(DuniterElasticsearchException | BusinessException e) {
            log.error(e.getMessage(), e);
            restChannel.sendResponse(new XContentThrowableRestResponse(request, e));
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
