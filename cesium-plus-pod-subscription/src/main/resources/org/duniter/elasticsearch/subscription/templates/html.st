html(content, title, useCss) ::= <<
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <meta content="IE=edge" http-equiv="X-UA-Compatible" />
        <meta charset="UTF-8">
        <title>$title$</title>
        $if(useCss)$
        <style type="text/css">
            $css()$
        </style>
        $endif$
    </head>
    <body>
        <ion-content>
            $content$
        </ion-content>
    </body>
</html>
>>