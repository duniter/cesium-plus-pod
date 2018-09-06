html_email_content(issuerPubkey, issuerName, senderPubkey, senderName, events, url) ::= <<
<table cellspacing="0" cellpadding="0" width="100%"
  style="font-size:12px;font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;border-spacing:0px;border-collapse:collapse;max-width:600px!important;">
    <tr>
        <td>
            <div style="background:#1a237e;width:100%;text-align:center;border-radius:4px;min-height:35px;">

                $cesium_logo(url, true)$

                <p style="margin:0px;padding:8px 0px;text-align:center;color:white;font-size:14px;">
                    $i18n_args("duniter4j.es.subscription.email.html.hello", issuerName)$
                </p>
            </div>
        </td>
    </tr>

    <tr>
        <td>
            <table cellspacing="0" cellpadding="0" width="100%" >
                <tr>
                    <td>
                        <p style="margin:0px;padding:16px;font-size: 12px;">
                            $i18n_args("duniter4j.es.subscription.email.html.unreadCount", {$length(events)$} )$
                            $if(issuerPubkey)$
                            <br/>
                            <span style="font-size:12px;color:grey !important;">
                                $i18n_args("duniter4j.es.subscription.email.html.pubkey", [{$[url, "/#/app/wot/", issuerPubkey, "/"]; separator=""$}, {$issuerPubkey; format="pubkey"$}])$
                            </span>
                            $endif$
                        </p>

                    </td>
                    <td>
                        <p style="margin:0px;width:100%;text-align:right;min-height: 64px;padding: 16px 0px;">
                            <a style="overflow:hidden!important;background-color:#387ef5;border-color:transparent;border-radius:2px;border-shadow: 2px 2px rgba(50,50,50,0.32);box-sizing: border-box;color:white;display:inline-block;font-size:14px;font-weight: 500;height: 47px;letter-spacing: 0.5px;line-height:42px;margin:0;min-height:47px;min-width:52px;padding-bottom:0px;padding-left:24px;padding-right:24px;padding-top:0px;text-align:center;text-decoration:none;text-transform:uppercase;"
                               href="$url$">$i18n("duniter4j.es.subscription.email.openCesium")$ &gt;&gt;</a>
                        </p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            <div style="background-color:#f5f5f5;border: 0;box-sizing: border-box; color: rgba(0, 0, 0, 0.54);font-size: 14px;font-weight: 700;height: 48px;line-height: 48px;min-height: 48px;padding-bottom: 8px;padding-left: 16px;padding-right: 16px;padding-top: 8px;vertical-align: baseline;">
                $i18n("duniter4j.es.subscription.email.notificationsDivider")$
            </div>
        </td>
    </tr>

    $events:{e|$html_event_item(e)$}$

    <tr>
        <td>
            <div style="width:100%;text-align:center;min-height:32px;padding:8px;">

            </div>
        </td>
    </tr>

    <tr>
        <td>
            <div style="background-color: rgb(236, 240, 247) !important;border-color: rgb(221, 223, 226) !important;width:100%;text-align:center;border-radius:4px;">
                <p style="margin:0px;padding:8px 0px;text-align:center;color:grey !important;text-decoration:none !important;">
                   $i18n_args("duniter4j.es.subscription.email.html.footer.sendBy", [{$[url, "/#/app/wot/", senderPubkey, "/"]; separator=""$}, senderName])$
                   <br/>
                   <small>
                       $i18n_args("duniter4j.es.subscription.email.html.footer.disableHelp", {$[url, "/#/app/wallet/subscriptions"]; separator=""$})$
                   </small>
                </p>
            </div>
        </td>
    </tr>
</table>
>>
