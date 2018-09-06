html_event_item(e) ::= <<
    <tr>
        <td>
            <div style="border-bottom: solid 1px #ccc !important;color: rgb(68, 68, 68);display: block;font-size: 14px;font-weight: 400;line-height: 20px;margin-bottom: -1px;margin-left: -1px;margin-right: -1px;margin-top: -1px;padding-bottom: 16px;padding-left: 16px;padding-right: 16px;padding-top: 16px;white-space: normal;">
                <h3 style="color: rgb(0,0,0);font-family:-apple-system,Helvetica Neue,Helvetica,Roboto,Segoe UI,sans-serif;font-size: 14px;font-synthesis: weight style;font-weight: 500;line-height: 16.8px;margin-bottom: 4px;margin-left: 0px;margin-right: 0px;margin-top: 0px;padding: 0;">$e.description$</h3>
                <h4 style="color: grey !important;font-size: 12px;font-weight: 500;line-height: 14.4px;margin: 0;padding: 0;">$e.time; format="short"$</h4>
            </div>
        </td>
    </tr>
>>