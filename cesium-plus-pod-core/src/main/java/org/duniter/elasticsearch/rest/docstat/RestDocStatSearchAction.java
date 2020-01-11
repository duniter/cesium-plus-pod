package org.duniter.elasticsearch.rest.docstat;

import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.dao.DocStatDao;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.search.RestSearchAction;
import org.elasticsearch.rest.action.support.RestStatusToXContentListener;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;

/**
 * /docstat/record has been replaced by /message/inbox
 * @deprecated
 */
@Deprecated
public class RestDocStatSearchAction extends BaseRestHandler {

    @Inject
    public RestDocStatSearchAction(Settings settings, RestController controller, Client client,
                                   PluginSettings pluginSettings,
                                   RestSecurityController securityController) {
        super(settings, controller, client);

        if (pluginSettings.enableDocStats()) {
            securityController.allow(GET, String.format("/%s/%s/_search", DocStatDao.OLD_INDEX, DocStatDao.OLD_TYPE));
            securityController.allow(POST, String.format("/%s/%s/_search", DocStatDao.OLD_INDEX, DocStatDao.OLD_TYPE));
            controller.registerHandler(GET, String.format("/%s/%s/_search", DocStatDao.OLD_INDEX, DocStatDao.OLD_TYPE), this);
            controller.registerHandler(POST, String.format("/%s/%s/_search", DocStatDao.OLD_INDEX, DocStatDao.OLD_TYPE), this);
        }
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel, final Client client) {
        SearchRequest searchRequest = new SearchRequest();
        RestSearchAction.parseSearchRequest(searchRequest, request, parseFieldMatcher, null);

        // Redirect to new index/type
        searchRequest.indices(DocStatDao.INDEX).types(DocStatDao.TYPE);

        client.search(searchRequest, new RestStatusToXContentListener<SearchResponse>(channel));
    }
}