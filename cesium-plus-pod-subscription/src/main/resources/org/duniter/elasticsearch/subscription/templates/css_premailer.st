css_premailer() ::= <<
   body,table,td,a{
       -webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;
   }
   table,td{
       mso-table-lspace:0pt;mso-table-rspace:0pt;
   }
   img{
       -ms-interpolation-mode:bicubic
   }
   a[x-apple-data-detectors]{
       color:inherit !important;
       text-decoration:none !important;
       font-size:inherit !important;
       font-family:inherit !important;
       font-weight:inherit !important;
       line-height:inherit !important
   }
   @media only screen and (max-width: 639px){
       body, #body{
           min-width:320px !important
       }
   }
>>