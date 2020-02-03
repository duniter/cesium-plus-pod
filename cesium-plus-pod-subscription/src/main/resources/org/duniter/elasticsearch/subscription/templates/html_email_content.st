html_email_content(title, issuerPubkey, issuerName, senderPubkey, senderName, events, url, linkName, locale) ::= <<
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" $if(locale)$lang="$locale$" xml:lang="$locale$"$endif$>
    <head>
        <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <meta content="IE=edge" http-equiv="X-UA-Compatible"/>
        <meta charset="UTF-8"/>
        <title>$title$</title>
        <style data-premailer="ignore" type="text/css">
            $css_premailer()$
        </style>
        <style type="text/css">
            $css()$
        </style>
    </head>
    <body id="body">
        <table cellspacing="0" cellpadding="0" width="100%"
          style="font-size:12px;font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;border-spacing:0;border-collapse:collapse;max-width:600px!important;">
              <tbody>
                <tr>
                    <td>
                        <div style="background:#1a237e;width:100%;text-align:center;border-radius:4px;min-height:35px;">

                            $app_logo(url, false)$

                            <p style="margin:0;padding:8px 0;text-align:center;color:white;font-size:14px;">
                                $i18n_args("duniter4j.es.subscription.email.html.hello", issuerName)$
                            </p>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <table cellspacing="0" cellpadding="0" width="100%">
                            <tbody>
                                <tr>
                                    <td>
                                        <p style="margin:0;padding:16px;font-size: 12px;">
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
                                        <p style="margin:0;width:100%;text-align:right;min-height: 64px;padding: 16px 0;">
                                            <a style="overflow:hidden!important;background-color:#387ef5;border-color:transparent;border-radius:2px;border-shadow: 2px 2px rgba(50,50,50,0.32);box-sizing: border-box;color:white;display:inline-block;font-size:14px;font-weight: 500;height: 47px;letter-spacing: 0.5px;line-height:42px;margin:0;min-height:47px;min-width:52px;padding-bottom:0;padding-left:24px;padding-right:24px;padding-top:0;text-align:center;text-decoration:none;text-transform:uppercase;"
                                               href="$url$">$i18n("duniter4j.es.subscription.email.open")$ $linkName$ &gt;&gt;</a>
                                        </p>
                                    </td>
                                </tr>
                            </tbody>
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
                            <p style="margin:0;padding:8px 0;text-align:center;color:grey !important;text-decoration:none !important;">
                               $i18n_args("duniter4j.es.subscription.email.html.footer.sendBy", [{$[url, "/#/app/wot/", senderPubkey, "/"]; separator=""$}, senderName, linkName])$
                               <br/>
                               <small>
                                   $i18n_args("duniter4j.es.subscription.email.html.footer.disableHelp", [{$[url, "/#/app/wallet/subscriptions"]; separator=""$}, linkName])$
                               </small>
                            </p>
                        </div>
                    </td>
                </tr>

            </tbody>
        </table>
    </body>
</html>
>>
