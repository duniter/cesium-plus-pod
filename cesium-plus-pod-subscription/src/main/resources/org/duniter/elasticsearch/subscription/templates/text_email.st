text_email(issuerPubkey, issuerName, senderPubkey, senderName, events, url, linkName) ::= <<
$i18n_args("duniter4j.es.subscription.email.hello", issuerName)$
$i18n_args("duniter4j.es.subscription.email.unreadCount", {$length(events)$} )$

$i18n("duniter4j.es.subscription.email.notificationsDivider")$
$events:{e|$text_event_item(e)$}$

$i18n("duniter4j.es.subscription.email.open")$ $linkName$ : $url$
$if(issuerPubkey)$$i18n_args("duniter4j.es.subscription.email.pubkey", [{$[url, "/#/app/wot/", issuerPubkey, "/"]; separator=""$}, {$issuerPubkey; format="pubkey"$}, linkName])$$endif$

-----------------------------------------------
$i18n_args("duniter4j.es.subscription.email.footer.sendBy", [{$[url, "/#/app/wot/", senderPubkey, "/"]; separator=""$}, senderName, linkName])$
$i18n_args("duniter4j.es.subscription.email.footer.disableHelp", [{$[url, "/#/app/wallet/subscriptions"]; separator=""$}, linkName])$

>>