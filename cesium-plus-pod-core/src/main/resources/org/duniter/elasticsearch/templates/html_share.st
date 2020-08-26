html_share(type, title, summary, description, image, imageHeight, imageWidth, siteName, locale, url, redirectUrl, redirectMessage) ::= <<
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" prefix="og: http://ogp.me/ns#" >
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- bootstrap -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    $if(siteName)$
        <title>$siteName$ | $title$</title>
    $else$
        <title>$title$</title>
    $endif$

        <meta name="twitter:card" content="summary"/>

    $if(type)$
        <meta property="og:type" content="$type$" />
    $else$
        <meta property="og:type" content="website" />
    $endif$

        <meta property="og:title" content="$title$"/>
        <meta name="twitter:title" content="$title$"/>

    $if(summary)$
        <meta property="og:description" content="$summary$" />
        <meta name="twitter:description" content="$summary$" />
    $else$
        <meta property="og:description" content="$description$" />
        <meta name="twitter:description" content="$description$" />
    $endif$

    $if(siteName)$
        <meta property="og:site_name" content="$siteName$" />
    $endif$

    $if(image)$
        <meta property="og:image" content="$image$" />
        <meta name="twitter:image" content="$image$"/>
    $endif$
    $if(imageHeight)$
        <meta property="og:image:height" content="$imageHeight$" />
    $endif$
    $if(imageWidth)$
        <meta property="og:image:width" content="$imageWidth$" />
    $endif$

    $if(locale)$
        <meta property="og:locale" content="$locale$" />
    $endif$

    $if(url)$
        <meta property="og:url" content="$url$"/>
        <meta name="twitter:site" content="$url$">
    $endif$

    $if(redirectUrl)$
        <script type="text/javascript">
            setTimeout("window.location.href=\"$redirectUrl$\";", 2000);
        </script>
    $endif$
    </head>
    <body style="padding-top: 26px;">
        <div class="container">

            <div class="row">

                <div class="col-12">
                    <h1>$title$</h1>
                </div>
            </div>

            <div class="row">

                $if(image)$
                <div class="col-md-auto">
                    <img src="$image$"/>
                </div>
                $endif$

                <div class="col-md">
                    <p>$description$</p>
                </div>
            </div>


            $if(redirectUrl)$
            <div class="row">
                <div class="col-12">
                    <small>$redirectMessage$
                        <a class="btn btn-primary active"
                           href='$redirectUrl$'
                           role="button">$title$ &gt;&gt;
                        </a>.
                    </small>
                </div>
            </div>
            $endif$

        </div>
    </body>
</html>
>>
