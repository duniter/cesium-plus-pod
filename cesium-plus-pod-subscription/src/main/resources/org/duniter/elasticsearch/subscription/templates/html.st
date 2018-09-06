html(content, useCss) ::= <<
    <!DOCTYPE html>
    <head>
        <meta charset="UTF-8">
        <title>Cesium+</title>
        $if(useCss)$
        <style>
            $css()$
            $css_logo()$
        </style>
        $endif$
    </head>
    <body class="platform-browser platform-linux platform-ready">
        <ion-content>
            $content$
        </ion-content>
    </body>
</html>
>>