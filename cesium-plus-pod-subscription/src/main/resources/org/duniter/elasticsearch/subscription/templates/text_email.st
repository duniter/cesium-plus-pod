text_email(issuerPubkey, issuerName, senderPubkey, senderName, events, url) ::= <<
$i18n_args("duniter4j.es.subscription.email.hello", issuerName)$
$i18n_args("duniter4j.es.subscription.email.unreadCount", {$length(events)$} )$

$i18n("duniter4j.es.subscription.email.notificationsDivider")$
$events:{e|$text_event_item(e)$}$

$i18n("duniter4j.es.subscription.email.openCesium")$ : $url$
$if(issuerPubkey)$$i18n_args("duniter4j.es.subscription.email.pubkey", [{$[url, "/#/app/wot/", issuerPubkey, "/"]; separator=""$}, {$issuerPubkey; format="pubkey"$}])$$endif$

-----------------------------------------------
$i18n_args("duniter4j.es.subscription.email.footer.sendBy", [{$[url, "/#/app/wot/", senderPubkey, "/"]; separator=""$}, senderName])$
$i18n_args("duniter4j.es.subscription.email.footer.disableHelp", {$[url, "/#/app/wallet/subscriptions"]; separator=""$})$

>>