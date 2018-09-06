css() ::= <<
    html, body, div, span, applet, object, iframe,
    h1, h2, h3, h4, h5, h6, p, blockquote, pre,
    a, abbr, acronym, address, big, cite, code,
    del, dfn, em, img, ins, kbd, q, s, samp,
    small, strike, strong, sub, sup, tt, var,
    b, i, u, center,
    dl, dt, dd, ol, ul, li,
    fieldset, form, label, legend,
    table, caption, tbody, tfoot, thead, tr, th, td,
    article, aside, canvas, details, embed, fieldset,
    figure, figcaption, footer, header, hgroup,
    menu, nav, output, ruby, section, summary,
    time, mark, audio, video {
      margin: 0;
      padding: 0;
      border: 0;
      vertical-align: baseline;
      font: inherit;
      font-size: 100%; }

    /**
     * Hide the `template` element in IE, Safari, and Firefox < 22.
     */
    [hidden],
    template {
      display: none; }

    script {
      display: none !important; }

    /* ==========================================================================
       Base
       ========================================================================== */
    /**
     * 1. Set default font family to sans-serif.
     * 2. Prevent iOS text size adjust after orientation change, without disabling
     *  user zoom.
     */
    html {
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      font-family: sans-serif;
      /* 1 */
      -webkit-text-size-adjust: 100%;
      -ms-text-size-adjust: 100%;
      /* 2 */
      -webkit-text-size-adjust: 100%;
      /* 2 */ }

    /**
     * Remove default margin.
     */
    body {
      margin: 0;
      line-height: 1; }

    /**
     * Remove default outlines.
     */
    a,
    button,
    :focus,
    a:focus,
    button:focus,
    a:active,
    a:hover {
      outline: 0; }

    /* *
     * Remove tap highlight color
     */
    a {
      -webkit-user-drag: none;
      -webkit-tap-highlight-color: transparent;
      -webkit-tap-highlight-color: transparent; }
      a[href]:hover {
        cursor: pointer; }

    /* ==========================================================================
       Typography
       ========================================================================== */
    /**
     * Address style set to `bolder` in Firefox 4+, Safari 5, and Chrome.
     */
    b,
    strong {
      font-weight: bold; }

    /**
     * Address styling not present in Safari 5 and Chrome.
     */
    dfn {
      font-style: italic; }

    /**
     * Address differences between Firefox and other browsers.
     */
    hr {
      -moz-box-sizing: content-box;
      box-sizing: content-box;
      height: 0; }

    /**
     * Correct font family set oddly in Safari 5 and Chrome.
     */
    code,
    kbd,
    pre,
    samp {
      font-size: 1em;
      font-family: monospace, serif; }

    /**
     * Improve readability of pre-formatted text in all browsers.
     */
    pre {
      white-space: pre-wrap; }

    /**
     * Set consistent quote types.
     */
    q {
      quotes: "\201C" "\201D" "\2018" "\2019"; }

    /**
     * Address inconsistent and variable font size in all browsers.
     */
    small {
      font-size: 80%; }

    /**
     * Prevent `sub` and `sup` affecting `line-height` in all browsers.
     */
    sub,
    sup {
      position: relative;
      vertical-align: baseline;
      font-size: 75%;
      line-height: 0; }

    sup {
      top: -0.5em; }

    sub {
      bottom: -0.25em; }

    /**
     * Define consistent border, margin, and padding.
     */
    fieldset {
      margin: 0 2px;
      padding: 0.35em 0.625em 0.75em;
      border: 1px solid #c0c0c0; }

    /**
     * 1. Correct `color` not being inherited in IE 8/9.
     * 2. Remove padding so people aren't caught out if they zero out fieldsets.
     */
    legend {
      padding: 0;
      /* 2 */
      border: 0;
      /* 1 */ }

    /**
     * 1. Correct font family not being inherited in all browsers.
     * 2. Correct font size not being inherited in all browsers.
     * 3. Address margins set differently in Firefox 4+, Safari 5, and Chrome.
     * 4. Remove any default :focus styles
     * 5. Make sure webkit font smoothing is being inherited
     * 6. Remove default gradient in Android Firefox / FirefoxOS
     */
    button,
    input,
    select,
    textarea {
      margin: 0;
      /* 3 */
      font-size: 100%;
      /* 2 */
      font-family: inherit;
      /* 1 */
      outline-offset: 0;
      /* 4 */
      outline-style: none;
      /* 4 */
      outline-width: 0;
      /* 4 */
      -webkit-font-smoothing: inherit;
      /* 5 */
      background-image: none;
      /* 6 */ }

    /**
     * Address Firefox 4+ setting `line-height` on `input` using `importnt` in
     * the UA stylesheet.
     */
    button,
    input {
      line-height: normal; }

    /**
     * Address inconsistent `text-transform` inheritance for `button` and `select`.
     * All other form control elements do not inherit `text-transform` values.
     * Correct `button` style inheritance in Chrome, Safari 5+, and IE 8+.
     * Correct `select` style inheritance in Firefox 4+ and Opera.
     */
    button,
    select {
      text-transform: none; }

    /**
     * 1. Avoid the WebKit bug in Android 4.0.* where (2) destroys native `audio`
     *  and `video` controls.
     * 2. Correct inability to style clickable `input` types in iOS.
     * 3. Improve usability and consistency of cursor style between image-type
     *  `input` and others.
     */
    button,
    html input[type="button"],
    input[type="reset"],
    input[type="submit"] {
      cursor: pointer;
      /* 3 */
      -webkit-appearance: button;
      /* 2 */ }

    /**
     * Re-set default cursor for disabled elements.
     */
    button[disabled],
    html input[disabled] {
      cursor: default; }

    /**
     * Remove inner padding and border in Firefox 4+.
     */
    button::-moz-focus-inner,
    input::-moz-focus-inner {
      padding: 0;
      border: 0; }

    img {
      -webkit-user-drag: none; }

    /**
     * Scaffolding
     * --------------------------------------------------
     */
    *,
    *:before,
    *:after {
      -webkit-box-sizing: border-box;
      -moz-box-sizing: border-box;
      box-sizing: border-box; }

    html {
      overflow: hidden;
      -ms-touch-action: pan-y;
      touch-action: pan-y; }

    body,
    .ionic-body {
      -webkit-touch-callout: none;
      -webkit-font-smoothing: antialiased;
      font-smoothing: antialiased;
      -webkit-text-size-adjust: none;
      -moz-text-size-adjust: none;
      text-size-adjust: none;
      -webkit-tap-highlight-color: transparent;
      -webkit-tap-highlight-color: transparent;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      overflow: hidden;
      margin: 0;
      padding: 0;
      color: #000;
      word-wrap: break-word;
      font-size: 14px;
      font-family: -apple-system;
      font-family: "-apple-system", "Helvetica Neue", "Roboto", "Segoe UI", sans-serif;
      line-height: 20px;
      text-rendering: optimizeLegibility;
      -webkit-backface-visibility: hidden;
      -webkit-user-drag: none;
      -ms-content-zooming: none; }

    .content {
      position: relative; }

    /* If you change these, change platform.scss as well */
    .has-header {
      top: 44px; }

    .no-header {
      top: 0; }

    .has-subheader {
      top: 88px; }

    .has-tabs-top {
      top: 93px; }

    .has-header.has-subheader.has-tabs-top {
      top: 137px; }

    .has-footer {
      bottom: 44px; }

    .has-subfooter {
      bottom: 88px; }

    .has-tabs,
    .bar-footer.has-tabs {
      bottom: 49px; }
      .has-tabs.pane,
      .bar-footer.has-tabs.pane {
        bottom: 49px;
        height: auto; }

    .bar-subfooter.has-tabs {
      bottom: 93px; }

    .has-footer.has-tabs {
      bottom: 93px; }

    /**
     * Typography
     * --------------------------------------------------
     */
    p {
      margin: 0 0 10px; }

    small {
      font-size: 85%; }

    cite {
      font-style: normal; }

    .text-left {
      text-align: left; }

    .text-right {
      text-align: right; }

    .text-center, .item.large-button-bar {
      text-align: center; }

    h1, h2, h3, h4, h5, h6,
    .h1, .h2, .h3, .h4, .h5, .h6 {
      color: #000;
      font-weight: 500;
      font-family: "-apple-system", "Helvetica Neue", "Roboto", "Segoe UI", sans-serif;
      line-height: 1.2; }
      h1 small, h2 small, h3 small, h4 small, h5 small, h6 small,
      .h1 small, .h2 small, .h3 small, .h4 small, .h5 small, .h6 small {
        font-weight: normal;
        line-height: 1; }

    h1, .h1,
    h2, .h2,
    h3, .h3 {
      margin-top: 20px;
      margin-bottom: 10px; }
      h1:first-child, .h1:first-child,
      h2:first-child, .h2:first-child,
      h3:first-child, .h3:first-child {
        margin-top: 0; }
      h1 + h1, h1 + .h1,
      h1 + h2, h1 + .h2,
      h1 + h3, h1 + .h3, .h1 + h1, .h1 + .h1,
      .h1 + h2, .h1 + .h2,
      .h1 + h3, .h1 + .h3,
      h2 + h1,
      h2 + .h1,
      h2 + h2,
      h2 + .h2,
      h2 + h3,
      h2 + .h3, .h2 + h1, .h2 + .h1,
      .h2 + h2, .h2 + .h2,
      .h2 + h3, .h2 + .h3,
      h3 + h1,
      h3 + .h1,
      h3 + h2,
      h3 + .h2,
      h3 + h3,
      h3 + .h3, .h3 + h1, .h3 + .h1,
      .h3 + h2, .h3 + .h2,
      .h3 + h3, .h3 + .h3 {
        margin-top: 10px; }

    h4, .h4,
    h5, .h5,
    h6, .h6 {
      margin-top: 10px;
      margin-bottom: 10px; }

    h1, .h1 {
      font-size: 36px; }

    h2, .h2 {
      font-size: 30px; }

    h3, .h3 {
      font-size: 24px; }

    h4, .h4 {
      font-size: 18px; }

    h5, .h5 {
      font-size: 14px; }

    h6, .h6 {
      font-size: 12px; }

    h1 small, .h1 small {
      font-size: 24px; }

    h2 small, .h2 small {
      font-size: 18px; }

    h3 small, .h3 small,
    h4 small, .h4 small {
      font-size: 14px; }

    dl {
      margin-bottom: 20px; }

    dt,
    dd {
      line-height: 1.42857; }

    dt {
      font-weight: bold; }

    blockquote {
      margin: 0 0 20px;
      padding: 10px 20px;
      border-left: 5px solid gray; }
      blockquote p {
        font-weight: 300;
        font-size: 17.5px;
        line-height: 1.25; }
      blockquote p:last-child {
        margin-bottom: 0; }
      blockquote small {
        display: block;
        line-height: 1.42857; }
        blockquote small:before {
          content: '\2014 \00A0'; }

    q:before,
    q:after,
    blockquote:before,
    blockquote:after {
      content: ""; }

    address {
      display: block;
      margin-bottom: 20px;
      font-style: normal;
      line-height: 1.42857; }

    a {
      color: #387ef5; }

    a.subdued {
      padding-right: 10px;
      color: #888;
      text-decoration: none; }
      a.subdued:hover {
        text-decoration: none; }
      a.subdued:last-child {
        padding-right: 0; }

    /**
     * Bar (Headers and Footers)
     * --------------------------------------------------
     */
    .bar {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      position: absolute;
      right: 0;
      left: 0;
      z-index: 9;
      -webkit-box-sizing: border-box;
      -moz-box-sizing: border-box;
      box-sizing: border-box;
      padding: 5px;
      width: 100%;
      height: 44px;
      border-width: 0;
      border-style: solid;
      border-top: 1px solid transparent;
      border-bottom: 1px solid #ddd;
      background-color: white;
      /* border-width: 1px will actually create 2 device pixels on retina */
      /* this nifty trick sets an actual 1px border on hi-res displays */
      background-size: 0; }
      @media (min--moz-device-pixel-ratio: 1.5), (-webkit-min-device-pixel-ratio: 1.5), (min-device-pixel-ratio: 1.5), (min-resolution: 144dpi), (min-resolution: 1.5dppx) {
        .bar {
          border: none;
          background-image: linear-gradient(0deg, #ddd, #ddd 50%, transparent 50%);
          background-position: bottom;
          background-size: 100% 1px;
          background-repeat: no-repeat; } }
      .bar.bar-clear {
        border: none;
        background: none;
        color: #fff; }
        .bar.bar-clear .button {
          color: #fff; }
        .bar.bar-clear .title {
          color: #fff; }
      .bar.item-input-inset .item-input-wrapper {
        margin-top: -1px; }
        .bar.item-input-inset .item-input-wrapper input {
          padding-left: 8px;
          width: 94%;
          height: 28px;
          background: transparent; }
      .bar.bar-light {
        border-color: #ddd;
        background-color: white;
        background-image: linear-gradient(0deg, #ddd, #ddd 50%, transparent 50%);
        color: #444; }
        .bar.bar-light .title {
          color: #444; }
        .bar.bar-light.bar-footer {
          background-image: linear-gradient(180deg, #ddd, #ddd 50%, transparent 50%); }
      .bar.bar-stable {
        border-color: #b2b2b2;
        background-color: #f8f8f8;
        background-image: linear-gradient(0deg, #b2b2b2, #b2b2b2 50%, transparent 50%);
        color: #444; }
        .bar.bar-stable .title {
          color: #444; }
        .bar.bar-stable.bar-footer {
          background-image: linear-gradient(180deg, #b2b2b2, #b2b2b2 50%, transparent 50%); }
      .bar.bar-positive {
        border-color: #0c60ee;
        background-color: #387ef5;
        background-image: linear-gradient(0deg, #0c60ee, #0c60ee 50%, transparent 50%);
        color: #fff; }
        .bar.bar-positive .title {
          color: #fff; }
        .bar.bar-positive.bar-footer {
          background-image: linear-gradient(180deg, #0c60ee, #0c60ee 50%, transparent 50%); }
      .bar.bar-calm {
        border-color: #0a9dc7;
        background-color: #11c1f3;
        background-image: linear-gradient(0deg, #0a9dc7, #0a9dc7 50%, transparent 50%);
        color: #fff; }
        .bar.bar-calm .title {
          color: #fff; }
        .bar.bar-calm.bar-footer {
          background-image: linear-gradient(180deg, #0a9dc7, #0a9dc7 50%, transparent 50%); }
      .bar.bar-assertive {
        border-color: #e42112;
        background-color: #ef473a;
        background-image: linear-gradient(0deg, #e42112, #e42112 50%, transparent 50%);
        color: #fff; }
        .bar.bar-assertive .title {
          color: #fff; }
        .bar.bar-assertive.bar-footer {
          background-image: linear-gradient(180deg, #e42112, #e42112 50%, transparent 50%); }
      .bar.bar-balanced {
        border-color: #28a54c;
        background-color: #33cd5f;
        background-image: linear-gradient(0deg, #28a54c, #28a54c 50%, transparent 50%);
        color: #fff; }
        .bar.bar-balanced .title {
          color: #fff; }
        .bar.bar-balanced.bar-footer {
          background-image: linear-gradient(180deg, #28a54c, #28a54c 50%, transparent 50%); }
      .bar.bar-energized {
        border-color: #e6b500;
        background-color: #ffc900;
        background-image: linear-gradient(0deg, #e6b500, #e6b500 50%, transparent 50%);
        color: #fff; }
        .bar.bar-energized .title {
          color: #fff; }
        .bar.bar-energized.bar-footer {
          background-image: linear-gradient(180deg, #e6b500, #e6b500 50%, transparent 50%); }
      .bar.bar-royal {
        border-color: #6b46e5;
        background-color: #886aea;
        background-image: linear-gradient(0deg, #6b46e5, #6b46e5 50%, transparent 50%);
        color: #fff; }
        .bar.bar-royal .title {
          color: #fff; }
        .bar.bar-royal.bar-footer {
          background-image: linear-gradient(180deg, #6b46e5, #6b46e5 50%, transparent 50%); }
      .bar.bar-dark {
        border-color: #111;
        background-color: #444444;
        background-image: linear-gradient(0deg, #111, #111 50%, transparent 50%);
        color: #fff; }
        .bar.bar-dark .title {
          color: #fff; }
        .bar.bar-dark.bar-footer {
          background-image: linear-gradient(180deg, #111, #111 50%, transparent 50%); }
      .bar .title {
        display: block;
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        z-index: 0;
        overflow: hidden;
        margin: 0 10px;
        min-width: 30px;
        height: 43px;
        text-align: center;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-size: 17px;
        font-weight: 500;
        line-height: 44px; }
        .bar .title.title-left {
          text-align: left; }
        .bar .title.title-right {
          text-align: right; }
      .bar .title a {
        color: inherit; }
      .bar .button, .bar button {
        z-index: 1;
        padding: 0 8px;
        min-width: initial;
        min-height: 31px;
        font-weight: 400;
        font-size: 13px;
        line-height: 32px; }
        .bar .button.button-icon:before,
        .bar .button .icon:before,
        .bar .button .icon-help:before,
        .bar .button .icon-alert:before,
        .bar .button #menu .footer .icon-help:before, #menu .footer
        .bar .button .icon-help:before, .bar .button.icon:before, .bar .button.icon-help:before, .bar .button.icon-alert:before, .bar #menu .footer .button.icon-help:before, #menu .footer .bar .button.icon-help:before, .bar .button.icon-left:before, .bar .button.icon-right:before, .bar button.button-icon:before,
        .bar button .icon:before,
        .bar button .icon-help:before,
        .bar button .icon-alert:before,
        .bar button #menu .footer .icon-help:before, #menu .footer
        .bar button .icon-help:before, .bar button.icon:before, .bar button.icon-help:before, .bar button.icon-alert:before, .bar #menu .footer button.icon-help:before, #menu .footer .bar button.icon-help:before, .bar button.icon-left:before, .bar button.icon-right:before {
          padding-right: 2px;
          padding-left: 2px;
          font-size: 20px;
          line-height: 32px; }
        .bar .button.button-icon, .bar button.button-icon {
          font-size: 17px; }
          .bar .button.button-icon .icon:before, .bar .button.button-icon .icon-help:before, .bar .button.button-icon .icon-alert:before, .bar .button.button-icon #menu .footer .icon-help:before, #menu .footer .bar .button.button-icon .icon-help:before, .bar .button.button-icon:before, .bar .button.button-icon.icon-left:before, .bar .button.button-icon.icon-right:before, .bar button.button-icon .icon:before, .bar button.button-icon .icon-help:before, .bar button.button-icon .icon-alert:before, .bar button.button-icon #menu .footer .icon-help:before, #menu .footer .bar button.button-icon .icon-help:before, .bar button.button-icon:before, .bar button.button-icon.icon-left:before, .bar button.button-icon.icon-right:before {
            vertical-align: top;
            font-size: 32px;
            line-height: 32px; }
        .bar .button.button-clear, .bar .button.button-text, .bar button.button-clear, .bar button.button-text {
          padding-right: 2px;
          padding-left: 2px;
          font-weight: 300;
          font-size: 17px; }
          .bar .button.button-clear .icon:before, .bar .button.button-text .icon:before, .bar .button.button-clear .icon-help:before, .bar .button.button-text .icon-help:before, .bar .button.button-clear .icon-alert:before, .bar .button.button-text .icon-alert:before, .bar .button.button-clear #menu .footer .icon-help:before, #menu .footer .bar .button.button-clear .icon-help:before, .bar .button.button-text #menu .footer .icon-help:before, #menu .footer .bar .button.button-text .icon-help:before, .bar .button.button-clear.icon:before, .bar .button.icon.button-text:before, .bar .button.button-text.icon-help:before, .bar .button.button-text.icon-alert:before, .bar #menu .footer .button.button-text.icon-help:before, #menu .footer .bar .button.button-text.icon-help:before, .bar .button.button-clear.icon-help:before, .bar .button.button-clear.icon-alert:before, .bar #menu .footer .button.button-clear.icon-help:before, #menu .footer .bar .button.button-clear.icon-help:before, .bar .button.button-clear.icon-left:before, .bar .button.icon-left.button-text:before, .bar .button.button-clear.icon-right:before, .bar .button.icon-right.button-text:before, .bar button.button-clear .icon:before, .bar button.button-text .icon:before, .bar button.button-clear .icon-help:before, .bar button.button-text .icon-help:before, .bar button.button-clear .icon-alert:before, .bar button.button-text .icon-alert:before, .bar button.button-clear #menu .footer .icon-help:before, #menu .footer .bar button.button-clear .icon-help:before, .bar button.button-text #menu .footer .icon-help:before, #menu .footer .bar button.button-text .icon-help:before, .bar button.button-clear.icon:before, .bar button.icon.button-text:before, .bar button.button-text.icon-help:before, .bar button.button-text.icon-alert:before, .bar #menu .footer button.button-text.icon-help:before, #menu .footer .bar button.button-text.icon-help:before, .bar button.button-clear.icon-help:before, .bar button.button-clear.icon-alert:before, .bar #menu .footer button.button-clear.icon-help:before, #menu .footer .bar button.button-clear.icon-help:before, .bar button.button-clear.icon-left:before, .bar button.icon-left.button-text:before, .bar button.button-clear.icon-right:before, .bar button.icon-right.button-text:before {
            font-size: 32px;
            line-height: 32px; }
        .bar .button.back-button, .bar button.back-button {
          display: block;
          margin-right: 5px;
          padding: 0;
          white-space: nowrap;
          font-weight: 400; }
        .bar .button.back-button.active, .bar .button.back-button.activated, .bar button.back-button.active, .bar button.back-button.activated {
          opacity: 0.2; }
      .bar .button-bar > .button,
      .bar .buttons > .button {
        min-height: 31px;
        line-height: 32px; }
      .bar .button-bar + .button,
      .bar .button + .button-bar {
        margin-left: 5px; }
      .bar .buttons,
      .bar .buttons.primary-buttons,
      .bar .buttons.secondary-buttons {
        display: inherit; }
      .bar .buttons span {
        display: inline-block; }
      .bar .buttons-left span {
        margin-right: 5px;
        display: inherit; }
      .bar .buttons-right span {
        margin-left: 5px;
        display: inherit; }
      .bar .title + .button:last-child,
      .bar > .button + .button:last-child,
      .bar > .button.pull-right, .popover-helptip
      .bar > .button.icon.icon-right, .popover-helptip
      .bar > .button.icon-right.icon-help, .popover-helptip
      .bar > .button.icon-right.icon-alert, .popover-helptip #menu .footer
      .bar > .button.icon-right.icon-help, #menu .footer .popover-helptip
      .bar > .button.icon-right.icon-help, .popover-helptip
      .bar > .button.icon.icon-center, .popover-helptip
      .bar > .button.icon-center.icon-help, .popover-helptip
      .bar > .button.icon-center.icon-alert, .popover-helptip #menu .footer
      .bar > .button.icon-center.icon-help, #menu .footer .popover-helptip
      .bar > .button.icon-center.icon-help, .popover-helptip
      .bar > .button.icon.icon-bottom-right, .popover-helptip
      .bar > .button.icon-bottom-right.icon-help, .popover-helptip
      .bar > .button.icon-bottom-right.icon-alert, .popover-helptip #menu .footer
      .bar > .button.icon-bottom-right.icon-help, #menu .footer .popover-helptip
      .bar > .button.icon-bottom-right.icon-help, .popover-helptip
      .bar > .button.icon.icon-bottom-center, .popover-helptip
      .bar > .button.icon-bottom-center.icon-help, .popover-helptip
      .bar > .button.icon-bottom-center.icon-alert, .popover-helptip #menu .footer
      .bar > .button.icon-bottom-center.icon-help, #menu .footer .popover-helptip
      .bar > .button.icon-bottom-center.icon-help,
      .bar .buttons.pull-right,
      .bar .popover-helptip .buttons.icon.icon-right, .popover-helptip
      .bar .buttons.icon.icon-right,
      .bar .popover-helptip .buttons.icon-right.icon-help, .popover-helptip
      .bar .buttons.icon-right.icon-help,
      .bar .popover-helptip .buttons.icon-right.icon-alert, .popover-helptip
      .bar .buttons.icon-right.icon-alert,
      .bar .popover-helptip #menu .footer .buttons.icon-right.icon-help, .popover-helptip #menu .footer
      .bar .buttons.icon-right.icon-help,
      .bar #menu .footer .popover-helptip .buttons.icon-right.icon-help, #menu .footer .popover-helptip
      .bar .buttons.icon-right.icon-help,
      .bar .popover-helptip .buttons.icon.icon-center, .popover-helptip
      .bar .buttons.icon.icon-center,
      .bar .popover-helptip .buttons.icon-center.icon-help, .popover-helptip
      .bar .buttons.icon-center.icon-help,
      .bar .popover-helptip .buttons.icon-center.icon-alert, .popover-helptip
      .bar .buttons.icon-center.icon-alert,
      .bar .popover-helptip #menu .footer .buttons.icon-center.icon-help, .popover-helptip #menu .footer
      .bar .buttons.icon-center.icon-help,
      .bar #menu .footer .popover-helptip .buttons.icon-center.icon-help, #menu .footer .popover-helptip
      .bar .buttons.icon-center.icon-help,
      .bar .popover-helptip .buttons.icon.icon-bottom-right, .popover-helptip
      .bar .buttons.icon.icon-bottom-right,
      .bar .popover-helptip .buttons.icon-bottom-right.icon-help, .popover-helptip
      .bar .buttons.icon-bottom-right.icon-help,
      .bar .popover-helptip .buttons.icon-bottom-right.icon-alert, .popover-helptip
      .bar .buttons.icon-bottom-right.icon-alert,
      .bar .popover-helptip #menu .footer .buttons.icon-bottom-right.icon-help, .popover-helptip #menu .footer
      .bar .buttons.icon-bottom-right.icon-help,
      .bar #menu .footer .popover-helptip .buttons.icon-bottom-right.icon-help, #menu .footer .popover-helptip
      .bar .buttons.icon-bottom-right.icon-help,
      .bar .popover-helptip .buttons.icon.icon-bottom-center, .popover-helptip
      .bar .buttons.icon.icon-bottom-center,
      .bar .popover-helptip .buttons.icon-bottom-center.icon-help, .popover-helptip
      .bar .buttons.icon-bottom-center.icon-help,
      .bar .popover-helptip .buttons.icon-bottom-center.icon-alert, .popover-helptip
      .bar .buttons.icon-bottom-center.icon-alert,
      .bar .popover-helptip #menu .footer .buttons.icon-bottom-center.icon-help, .popover-helptip #menu .footer
      .bar .buttons.icon-bottom-center.icon-help,
      .bar #menu .footer .popover-helptip .buttons.icon-bottom-center.icon-help, #menu .footer .popover-helptip
      .bar .buttons.icon-bottom-center.icon-help,
      .bar .title + .buttons {
        position: absolute;
        top: 5px;
        right: 5px;
        bottom: 5px; }

    .platform-android .nav-bar-has-subheader .bar {
      background-image: none; }

    .platform-android .bar .back-button .icon:before, .platform-android .bar .back-button .icon-help:before, .platform-android .bar .back-button .icon-alert:before, .platform-android .bar .back-button #menu .footer .icon-help:before, #menu .footer .platform-android .bar .back-button .icon-help:before {
      font-size: 24px; }

    .platform-android .bar .title {
      font-size: 19px;
      line-height: 44px; }

    .bar-light .button {
      border-color: #ddd;
      background-color: white;
      color: #444; }
      .bar-light .button:hover {
        color: #444;
        text-decoration: none; }
      .bar-light .button.active, .bar-light .button.activated {
        border-color: #ccc;
        background-color: #fafafa; }
      .bar-light .button.button-clear, .bar-light .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #444;
        font-size: 17px; }
      .bar-light .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-stable .button {
      border-color: #b2b2b2;
      background-color: #f8f8f8;
      color: #444; }
      .bar-stable .button:hover {
        color: #444;
        text-decoration: none; }
      .bar-stable .button.active, .bar-stable .button.activated {
        border-color: #a2a2a2;
        background-color: #e5e5e5; }
      .bar-stable .button.button-clear, .bar-stable .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #444;
        font-size: 17px; }
      .bar-stable .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-positive .button {
      border-color: #0c60ee;
      background-color: #387ef5;
      color: #fff; }
      .bar-positive .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-positive .button.active, .bar-positive .button.activated {
        border-color: #0c60ee;
        background-color: #0c60ee; }
      .bar-positive .button.button-clear, .bar-positive .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-positive .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-calm .button {
      border-color: #0a9dc7;
      background-color: #11c1f3;
      color: #fff; }
      .bar-calm .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-calm .button.active, .bar-calm .button.activated {
        border-color: #0a9dc7;
        background-color: #0a9dc7; }
      .bar-calm .button.button-clear, .bar-calm .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-calm .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-assertive .button {
      border-color: #e42112;
      background-color: #ef473a;
      color: #fff; }
      .bar-assertive .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-assertive .button.active, .bar-assertive .button.activated {
        border-color: #e42112;
        background-color: #e42112; }
      .bar-assertive .button.button-clear, .bar-assertive .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-assertive .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-balanced .button {
      border-color: #28a54c;
      background-color: #33cd5f;
      color: #fff; }
      .bar-balanced .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-balanced .button.active, .bar-balanced .button.activated {
        border-color: #28a54c;
        background-color: #28a54c; }
      .bar-balanced .button.button-clear, .bar-balanced .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-balanced .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-energized .button {
      border-color: #e6b500;
      background-color: #ffc900;
      color: #fff; }
      .bar-energized .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-energized .button.active, .bar-energized .button.activated {
        border-color: #e6b500;
        background-color: #e6b500; }
      .bar-energized .button.button-clear, .bar-energized .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-energized .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-royal .button {
      border-color: #6b46e5;
      background-color: #886aea;
      color: #fff; }
      .bar-royal .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-royal .button.active, .bar-royal .button.activated {
        border-color: #6b46e5;
        background-color: #6b46e5; }
      .bar-royal .button.button-clear, .bar-royal .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-royal .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-dark .button {
      border-color: #111;
      background-color: #444444;
      color: #fff; }
      .bar-dark .button:hover {
        color: #fff;
        text-decoration: none; }
      .bar-dark .button.active, .bar-dark .button.activated {
        border-color: #000;
        background-color: #262626; }
      .bar-dark .button.button-clear, .bar-dark .button.button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: #fff;
        font-size: 17px; }
      .bar-dark .button.button-icon {
        border-color: transparent;
        background: none; }

    .bar-header {
      top: 0;
      border-top-width: 0;
      border-bottom-width: 1px; }
      .bar-header.has-tabs-top {
        border-bottom-width: 0px;
        background-image: none; }

    .tabs-top .bar-header {
      border-bottom-width: 0px;
      background-image: none; }

    .bar-footer {
      bottom: 0;
      border-top-width: 1px;
      border-bottom-width: 0;
      background-position: top;
      height: 44px; }
      .bar-footer.item-input-inset {
        position: absolute; }
      .bar-footer .title {
        height: 43px;
        line-height: 44px; }

    .bar-tabs {
      padding: 0; }

    .bar-subheader {
      top: 44px;
      height: 44px; }
      .bar-subheader .title {
        height: 43px;
        line-height: 44px; }

    .bar-subfooter {
      bottom: 44px;
      height: 44px; }
      .bar-subfooter .title {
        height: 43px;
        line-height: 44px; }

    .nav-bar-block {
      position: absolute;
      top: 0;
      right: 0;
      left: 0;
      z-index: 9; }

    .bar .back-button.hide,
    .bar .buttons .hide {
      display: none; }

    .nav-bar-tabs-top .bar {
      background-image: none; }

    /**
     * Tabs
     * --------------------------------------------------
     * A navigation bar with any number of tab items supported.
     */
    .tabs {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-direction: normal;
      -webkit-box-orient: horizontal;
      -webkit-flex-direction: horizontal;
      -moz-flex-direction: horizontal;
      -ms-flex-direction: horizontal;
      flex-direction: horizontal;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      -webkit-justify-content: center;
      -moz-justify-content: center;
      justify-content: center;
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      border-color: #b2b2b2;
      background-color: #f8f8f8;
      background-image: linear-gradient(0deg, #b2b2b2, #b2b2b2 50%, transparent 50%);
      color: #444;
      position: absolute;
      bottom: 0;
      z-index: 5;
      width: 100%;
      height: 49px;
      border-style: solid;
      border-top-width: 1px;
      background-size: 0;
      line-height: 49px; }
      .tabs .tab-item .badge {
        background-color: #444;
        color: #f8f8f8; }
      @media (min--moz-device-pixel-ratio: 1.5), (-webkit-min-device-pixel-ratio: 1.5), (min-device-pixel-ratio: 1.5), (min-resolution: 144dpi), (min-resolution: 1.5dppx) {
        .tabs {
          padding-top: 2px;
          border-top: none !important;
          border-bottom: none;
          background-position: top;
          background-size: 100% 1px;
          background-repeat: no-repeat; } }

    /* Allow parent element of tabs to define color, or just the tab itself */
    .tabs-light > .tabs,
    .tabs.tabs-light {
      border-color: #ddd;
      background-color: #fff;
      background-image: linear-gradient(0deg, #ddd, #ddd 50%, transparent 50%);
      color: #444; }
      .tabs-light > .tabs .tab-item .badge,
      .tabs.tabs-light .tab-item .badge {
        background-color: #444;
        color: #fff; }

    .tabs-stable > .tabs,
    .tabs.tabs-stable {
      border-color: #b2b2b2;
      background-color: #f8f8f8;
      background-image: linear-gradient(0deg, #b2b2b2, #b2b2b2 50%, transparent 50%);
      color: #444; }
      .tabs-stable > .tabs .tab-item .badge,
      .tabs.tabs-stable .tab-item .badge {
        background-color: #444;
        color: #f8f8f8; }

    .tabs-positive > .tabs,
    .tabs.tabs-positive {
      border-color: #0c60ee;
      background-color: #387ef5;
      background-image: linear-gradient(0deg, #0c60ee, #0c60ee 50%, transparent 50%);
      color: #fff; }
      .tabs-positive > .tabs .tab-item .badge,
      .tabs.tabs-positive .tab-item .badge {
        background-color: #fff;
        color: #387ef5; }

    .tabs-calm > .tabs,
    .tabs.tabs-calm {
      border-color: #0a9dc7;
      background-color: #11c1f3;
      background-image: linear-gradient(0deg, #0a9dc7, #0a9dc7 50%, transparent 50%);
      color: #fff; }
      .tabs-calm > .tabs .tab-item .badge,
      .tabs.tabs-calm .tab-item .badge {
        background-color: #fff;
        color: #11c1f3; }

    .tabs-assertive > .tabs,
    .tabs.tabs-assertive {
      border-color: #e42112;
      background-color: #ef473a;
      background-image: linear-gradient(0deg, #e42112, #e42112 50%, transparent 50%);
      color: #fff; }
      .tabs-assertive > .tabs .tab-item .badge,
      .tabs.tabs-assertive .tab-item .badge {
        background-color: #fff;
        color: #ef473a; }

    .tabs-balanced > .tabs,
    .tabs.tabs-balanced {
      border-color: #28a54c;
      background-color: #33cd5f;
      background-image: linear-gradient(0deg, #28a54c, #28a54c 50%, transparent 50%);
      color: #fff; }
      .tabs-balanced > .tabs .tab-item .badge,
      .tabs.tabs-balanced .tab-item .badge {
        background-color: #fff;
        color: #33cd5f; }

    .tabs-energized > .tabs,
    .tabs.tabs-energized {
      border-color: #e6b500;
      background-color: #ffc900;
      background-image: linear-gradient(0deg, #e6b500, #e6b500 50%, transparent 50%);
      color: #fff; }
      .tabs-energized > .tabs .tab-item .badge,
      .tabs.tabs-energized .tab-item .badge {
        background-color: #fff;
        color: #ffc900; }

    .tabs-royal > .tabs,
    .tabs.tabs-royal {
      border-color: #6b46e5;
      background-color: #886aea;
      background-image: linear-gradient(0deg, #6b46e5, #6b46e5 50%, transparent 50%);
      color: #fff; }
      .tabs-royal > .tabs .tab-item .badge,
      .tabs.tabs-royal .tab-item .badge {
        background-color: #fff;
        color: #886aea; }

    .tabs-dark > .tabs,
    .tabs.tabs-dark {
      border-color: #111;
      background-color: #444;
      background-image: linear-gradient(0deg, #111, #111 50%, transparent 50%);
      color: #fff; }
      .tabs-dark > .tabs .tab-item .badge,
      .tabs.tabs-dark .tab-item .badge {
        background-color: #fff;
        color: #444; }

    .tabs-striped .tabs {
      background-color: white;
      background-image: none;
      border: none;
      border-bottom: 1px solid #ddd;
      padding-top: 2px; }

    .tabs-striped .tab-item.tab-item-active, .tabs-striped .tab-item.active, .tabs-striped .tab-item.activated {
      margin-top: -2px;
      border-style: solid;
      border-width: 2px 0 0 0;
      border-color: #444; }
      .tabs-striped .tab-item.tab-item-active .badge, .tabs-striped .tab-item.active .badge, .tabs-striped .tab-item.activated .badge {
        top: 2px;
        opacity: 1; }

    .tabs-striped.tabs-light .tabs {
      background-color: #fff; }

    .tabs-striped.tabs-light .tab-item {
      color: rgba(68, 68, 68, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-light .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-light .tab-item.tab-item-active, .tabs-striped.tabs-light .tab-item.active, .tabs-striped.tabs-light .tab-item.activated {
        margin-top: -2px;
        color: #444;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #444; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-stable .tabs {
      background-color: #f8f8f8; }

    .tabs-striped.tabs-stable .tab-item {
      color: rgba(68, 68, 68, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-stable .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-stable .tab-item.tab-item-active, .tabs-striped.tabs-stable .tab-item.active, .tabs-striped.tabs-stable .tab-item.activated {
        margin-top: -2px;
        color: #444;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #444; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-positive .tabs {
      background-color: #387ef5; }

    .tabs-striped.tabs-positive .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-positive .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-positive .tab-item.tab-item-active, .tabs-striped.tabs-positive .tab-item.active, .tabs-striped.tabs-positive .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-calm .tabs {
      background-color: #11c1f3; }

    .tabs-striped.tabs-calm .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-calm .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-calm .tab-item.tab-item-active, .tabs-striped.tabs-calm .tab-item.active, .tabs-striped.tabs-calm .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-assertive .tabs {
      background-color: #ef473a; }

    .tabs-striped.tabs-assertive .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-assertive .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-assertive .tab-item.tab-item-active, .tabs-striped.tabs-assertive .tab-item.active, .tabs-striped.tabs-assertive .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-balanced .tabs {
      background-color: #33cd5f; }

    .tabs-striped.tabs-balanced .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-balanced .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-balanced .tab-item.tab-item-active, .tabs-striped.tabs-balanced .tab-item.active, .tabs-striped.tabs-balanced .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-energized .tabs {
      background-color: #ffc900; }

    .tabs-striped.tabs-energized .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-energized .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-energized .tab-item.tab-item-active, .tabs-striped.tabs-energized .tab-item.active, .tabs-striped.tabs-energized .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-royal .tabs {
      background-color: #886aea; }

    .tabs-striped.tabs-royal .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-royal .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-royal .tab-item.tab-item-active, .tabs-striped.tabs-royal .tab-item.active, .tabs-striped.tabs-royal .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-dark .tabs {
      background-color: #444; }

    .tabs-striped.tabs-dark .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-dark .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-dark .tab-item.tab-item-active, .tabs-striped.tabs-dark .tab-item.active, .tabs-striped.tabs-dark .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border-style: solid;
        border-width: 2px 0 0 0;
        border-color: #fff; }

    .tabs-striped.tabs-top .tab-item.tab-item-active .badge, .tabs-striped.tabs-top .tab-item.active .badge, .tabs-striped.tabs-top .tab-item.activated .badge {
      top: 4%; }

    .tabs-striped.tabs-background-light .tabs {
      background-color: #fff;
      background-image: none; }

    .tabs-striped.tabs-background-stable .tabs {
      background-color: #f8f8f8;
      background-image: none; }

    .tabs-striped.tabs-background-positive .tabs {
      background-color: #387ef5;
      background-image: none; }

    .tabs-striped.tabs-background-calm .tabs {
      background-color: #11c1f3;
      background-image: none; }

    .tabs-striped.tabs-background-assertive .tabs {
      background-color: #ef473a;
      background-image: none; }

    .tabs-striped.tabs-background-balanced .tabs {
      background-color: #33cd5f;
      background-image: none; }

    .tabs-striped.tabs-background-energized .tabs {
      background-color: #ffc900;
      background-image: none; }

    .tabs-striped.tabs-background-royal .tabs {
      background-color: #886aea;
      background-image: none; }

    .tabs-striped.tabs-background-dark .tabs {
      background-color: #444;
      background-image: none; }

    .tabs-striped.tabs-color-light .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-light .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-light .tab-item.tab-item-active, .tabs-striped.tabs-color-light .tab-item.active, .tabs-striped.tabs-color-light .tab-item.activated {
        margin-top: -2px;
        color: #fff;
        border: 0 solid #fff;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-light .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-light .tab-item.active .badge, .tabs-striped.tabs-color-light .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-stable .tab-item {
      color: rgba(248, 248, 248, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-stable .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-stable .tab-item.tab-item-active, .tabs-striped.tabs-color-stable .tab-item.active, .tabs-striped.tabs-color-stable .tab-item.activated {
        margin-top: -2px;
        color: #f8f8f8;
        border: 0 solid #f8f8f8;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-stable .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-stable .tab-item.active .badge, .tabs-striped.tabs-color-stable .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-positive .tab-item {
      color: rgba(56, 126, 245, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-positive .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-positive .tab-item.tab-item-active, .tabs-striped.tabs-color-positive .tab-item.active, .tabs-striped.tabs-color-positive .tab-item.activated {
        margin-top: -2px;
        color: #387ef5;
        border: 0 solid #387ef5;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-positive .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-positive .tab-item.active .badge, .tabs-striped.tabs-color-positive .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-calm .tab-item {
      color: rgba(17, 193, 243, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-calm .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-calm .tab-item.tab-item-active, .tabs-striped.tabs-color-calm .tab-item.active, .tabs-striped.tabs-color-calm .tab-item.activated {
        margin-top: -2px;
        color: #11c1f3;
        border: 0 solid #11c1f3;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-calm .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-calm .tab-item.active .badge, .tabs-striped.tabs-color-calm .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-assertive .tab-item {
      color: rgba(239, 71, 58, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-assertive .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-assertive .tab-item.tab-item-active, .tabs-striped.tabs-color-assertive .tab-item.active, .tabs-striped.tabs-color-assertive .tab-item.activated {
        margin-top: -2px;
        color: #ef473a;
        border: 0 solid #ef473a;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-assertive .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-assertive .tab-item.active .badge, .tabs-striped.tabs-color-assertive .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-balanced .tab-item {
      color: rgba(51, 205, 95, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-balanced .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-balanced .tab-item.tab-item-active, .tabs-striped.tabs-color-balanced .tab-item.active, .tabs-striped.tabs-color-balanced .tab-item.activated {
        margin-top: -2px;
        color: #33cd5f;
        border: 0 solid #33cd5f;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-balanced .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-balanced .tab-item.active .badge, .tabs-striped.tabs-color-balanced .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-energized .tab-item {
      color: rgba(255, 201, 0, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-energized .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-energized .tab-item.tab-item-active, .tabs-striped.tabs-color-energized .tab-item.active, .tabs-striped.tabs-color-energized .tab-item.activated {
        margin-top: -2px;
        color: #ffc900;
        border: 0 solid #ffc900;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-energized .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-energized .tab-item.active .badge, .tabs-striped.tabs-color-energized .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-royal .tab-item {
      color: rgba(136, 106, 234, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-royal .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-royal .tab-item.tab-item-active, .tabs-striped.tabs-color-royal .tab-item.active, .tabs-striped.tabs-color-royal .tab-item.activated {
        margin-top: -2px;
        color: #886aea;
        border: 0 solid #886aea;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-royal .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-royal .tab-item.active .badge, .tabs-striped.tabs-color-royal .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-striped.tabs-color-dark .tab-item {
      color: rgba(68, 68, 68, 0.4);
      opacity: 1; }
      .tabs-striped.tabs-color-dark .tab-item .badge {
        opacity: 0.4; }
      .tabs-striped.tabs-color-dark .tab-item.tab-item-active, .tabs-striped.tabs-color-dark .tab-item.active, .tabs-striped.tabs-color-dark .tab-item.activated {
        margin-top: -2px;
        color: #444;
        border: 0 solid #444;
        border-top-width: 2px; }
        .tabs-striped.tabs-color-dark .tab-item.tab-item-active .badge, .tabs-striped.tabs-color-dark .tab-item.active .badge, .tabs-striped.tabs-color-dark .tab-item.activated .badge {
          top: 2px;
          opacity: 1; }

    .tabs-background-light .tabs,
    .tabs-background-light > .tabs {
      background-color: #fff;
      background-image: linear-gradient(0deg, #ddd, #ddd 50%, transparent 50%);
      border-color: #ddd; }

    .tabs-background-stable .tabs,
    .tabs-background-stable > .tabs {
      background-color: #f8f8f8;
      background-image: linear-gradient(0deg, #b2b2b2, #b2b2b2 50%, transparent 50%);
      border-color: #b2b2b2; }

    .tabs-background-positive .tabs,
    .tabs-background-positive > .tabs {
      background-color: #387ef5;
      background-image: linear-gradient(0deg, #0c60ee, #0c60ee 50%, transparent 50%);
      border-color: #0c60ee; }

    .tabs-background-calm .tabs,
    .tabs-background-calm > .tabs {
      background-color: #11c1f3;
      background-image: linear-gradient(0deg, #0a9dc7, #0a9dc7 50%, transparent 50%);
      border-color: #0a9dc7; }

    .tabs-background-assertive .tabs,
    .tabs-background-assertive > .tabs {
      background-color: #ef473a;
      background-image: linear-gradient(0deg, #e42112, #e42112 50%, transparent 50%);
      border-color: #e42112; }

    .tabs-background-balanced .tabs,
    .tabs-background-balanced > .tabs {
      background-color: #33cd5f;
      background-image: linear-gradient(0deg, #28a54c, #28a54c 50%, transparent 50%);
      border-color: #28a54c; }

    .tabs-background-energized .tabs,
    .tabs-background-energized > .tabs {
      background-color: #ffc900;
      background-image: linear-gradient(0deg, #e6b500, #e6b500 50%, transparent 50%);
      border-color: #e6b500; }

    .tabs-background-royal .tabs,
    .tabs-background-royal > .tabs {
      background-color: #886aea;
      background-image: linear-gradient(0deg, #6b46e5, #6b46e5 50%, transparent 50%);
      border-color: #6b46e5; }

    .tabs-background-dark .tabs,
    .tabs-background-dark > .tabs {
      background-color: #444;
      background-image: linear-gradient(0deg, #111, #111 50%, transparent 50%);
      border-color: #111; }

    .tabs-color-light .tab-item {
      color: rgba(255, 255, 255, 0.4);
      opacity: 1; }
      .tabs-color-light .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-light .tab-item.tab-item-active, .tabs-color-light .tab-item.active, .tabs-color-light .tab-item.activated {
        color: #fff;
        border: 0 solid #fff; }
        .tabs-color-light .tab-item.tab-item-active .badge, .tabs-color-light .tab-item.active .badge, .tabs-color-light .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-stable .tab-item {
      color: rgba(248, 248, 248, 0.4);
      opacity: 1; }
      .tabs-color-stable .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-stable .tab-item.tab-item-active, .tabs-color-stable .tab-item.active, .tabs-color-stable .tab-item.activated {
        color: #f8f8f8;
        border: 0 solid #f8f8f8; }
        .tabs-color-stable .tab-item.tab-item-active .badge, .tabs-color-stable .tab-item.active .badge, .tabs-color-stable .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-positive .tab-item {
      color: rgba(56, 126, 245, 0.4);
      opacity: 1; }
      .tabs-color-positive .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-positive .tab-item.tab-item-active, .tabs-color-positive .tab-item.active, .tabs-color-positive .tab-item.activated {
        color: #387ef5;
        border: 0 solid #387ef5; }
        .tabs-color-positive .tab-item.tab-item-active .badge, .tabs-color-positive .tab-item.active .badge, .tabs-color-positive .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-calm .tab-item {
      color: rgba(17, 193, 243, 0.4);
      opacity: 1; }
      .tabs-color-calm .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-calm .tab-item.tab-item-active, .tabs-color-calm .tab-item.active, .tabs-color-calm .tab-item.activated {
        color: #11c1f3;
        border: 0 solid #11c1f3; }
        .tabs-color-calm .tab-item.tab-item-active .badge, .tabs-color-calm .tab-item.active .badge, .tabs-color-calm .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-assertive .tab-item {
      color: rgba(239, 71, 58, 0.4);
      opacity: 1; }
      .tabs-color-assertive .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-assertive .tab-item.tab-item-active, .tabs-color-assertive .tab-item.active, .tabs-color-assertive .tab-item.activated {
        color: #ef473a;
        border: 0 solid #ef473a; }
        .tabs-color-assertive .tab-item.tab-item-active .badge, .tabs-color-assertive .tab-item.active .badge, .tabs-color-assertive .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-balanced .tab-item {
      color: rgba(51, 205, 95, 0.4);
      opacity: 1; }
      .tabs-color-balanced .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-balanced .tab-item.tab-item-active, .tabs-color-balanced .tab-item.active, .tabs-color-balanced .tab-item.activated {
        color: #33cd5f;
        border: 0 solid #33cd5f; }
        .tabs-color-balanced .tab-item.tab-item-active .badge, .tabs-color-balanced .tab-item.active .badge, .tabs-color-balanced .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-energized .tab-item {
      color: rgba(255, 201, 0, 0.4);
      opacity: 1; }
      .tabs-color-energized .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-energized .tab-item.tab-item-active, .tabs-color-energized .tab-item.active, .tabs-color-energized .tab-item.activated {
        color: #ffc900;
        border: 0 solid #ffc900; }
        .tabs-color-energized .tab-item.tab-item-active .badge, .tabs-color-energized .tab-item.active .badge, .tabs-color-energized .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-royal .tab-item {
      color: rgba(136, 106, 234, 0.4);
      opacity: 1; }
      .tabs-color-royal .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-royal .tab-item.tab-item-active, .tabs-color-royal .tab-item.active, .tabs-color-royal .tab-item.activated {
        color: #886aea;
        border: 0 solid #886aea; }
        .tabs-color-royal .tab-item.tab-item-active .badge, .tabs-color-royal .tab-item.active .badge, .tabs-color-royal .tab-item.activated .badge {
          opacity: 1; }

    .tabs-color-dark .tab-item {
      color: rgba(68, 68, 68, 0.4);
      opacity: 1; }
      .tabs-color-dark .tab-item .badge {
        opacity: 0.4; }
      .tabs-color-dark .tab-item.tab-item-active, .tabs-color-dark .tab-item.active, .tabs-color-dark .tab-item.activated {
        color: #444;
        border: 0 solid #444; }
        .tabs-color-dark .tab-item.tab-item-active .badge, .tabs-color-dark .tab-item.active .badge, .tabs-color-dark .tab-item.activated .badge {
          opacity: 1; }

    ion-tabs.tabs-color-active-light .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-light .tab-item.tab-item-active, ion-tabs.tabs-color-active-light .tab-item.active, ion-tabs.tabs-color-active-light .tab-item.activated {
        color: #fff; }

    ion-tabs.tabs-striped.tabs-color-active-light .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-light .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-light .tab-item.activated {
      border-color: #fff;
      color: #fff; }

    ion-tabs.tabs-color-active-stable .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-stable .tab-item.tab-item-active, ion-tabs.tabs-color-active-stable .tab-item.active, ion-tabs.tabs-color-active-stable .tab-item.activated {
        color: #f8f8f8; }

    ion-tabs.tabs-striped.tabs-color-active-stable .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-stable .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-stable .tab-item.activated {
      border-color: #f8f8f8;
      color: #f8f8f8; }

    ion-tabs.tabs-color-active-positive .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-positive .tab-item.tab-item-active, ion-tabs.tabs-color-active-positive .tab-item.active, ion-tabs.tabs-color-active-positive .tab-item.activated {
        color: #387ef5; }

    ion-tabs.tabs-striped.tabs-color-active-positive .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-positive .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-positive .tab-item.activated {
      border-color: #387ef5;
      color: #387ef5; }

    ion-tabs.tabs-color-active-calm .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-calm .tab-item.tab-item-active, ion-tabs.tabs-color-active-calm .tab-item.active, ion-tabs.tabs-color-active-calm .tab-item.activated {
        color: #11c1f3; }

    ion-tabs.tabs-striped.tabs-color-active-calm .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-calm .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-calm .tab-item.activated {
      border-color: #11c1f3;
      color: #11c1f3; }

    ion-tabs.tabs-color-active-assertive .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-assertive .tab-item.tab-item-active, ion-tabs.tabs-color-active-assertive .tab-item.active, ion-tabs.tabs-color-active-assertive .tab-item.activated {
        color: #ef473a; }

    ion-tabs.tabs-striped.tabs-color-active-assertive .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-assertive .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-assertive .tab-item.activated {
      border-color: #ef473a;
      color: #ef473a; }

    ion-tabs.tabs-color-active-balanced .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-balanced .tab-item.tab-item-active, ion-tabs.tabs-color-active-balanced .tab-item.active, ion-tabs.tabs-color-active-balanced .tab-item.activated {
        color: #33cd5f; }

    ion-tabs.tabs-striped.tabs-color-active-balanced .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-balanced .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-balanced .tab-item.activated {
      border-color: #33cd5f;
      color: #33cd5f; }

    ion-tabs.tabs-color-active-energized .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-energized .tab-item.tab-item-active, ion-tabs.tabs-color-active-energized .tab-item.active, ion-tabs.tabs-color-active-energized .tab-item.activated {
        color: #ffc900; }

    ion-tabs.tabs-striped.tabs-color-active-energized .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-energized .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-energized .tab-item.activated {
      border-color: #ffc900;
      color: #ffc900; }

    ion-tabs.tabs-color-active-royal .tab-item {
      color: #444; }
      ion-tabs.tabs-color-active-royal .tab-item.tab-item-active, ion-tabs.tabs-color-active-royal .tab-item.active, ion-tabs.tabs-color-active-royal .tab-item.activated {
        color: #886aea; }

    ion-tabs.tabs-striped.tabs-color-active-royal .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-royal .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-royal .tab-item.activated {
      border-color: #886aea;
      color: #886aea; }

    ion-tabs.tabs-color-active-dark .tab-item {
      color: #fff; }
      ion-tabs.tabs-color-active-dark .tab-item.tab-item-active, ion-tabs.tabs-color-active-dark .tab-item.active, ion-tabs.tabs-color-active-dark .tab-item.activated {
        color: #444; }

    ion-tabs.tabs-striped.tabs-color-active-dark .tab-item.tab-item-active, ion-tabs.tabs-striped.tabs-color-active-dark .tab-item.active, ion-tabs.tabs-striped.tabs-color-active-dark .tab-item.activated {
      border-color: #444;
      color: #444; }

    .tabs-top.tabs-striped {
      padding-bottom: 0; }
      .tabs-top.tabs-striped .tab-item {
        background: transparent;
        -webkit-transition: color .1s ease;
        -moz-transition: color .1s ease;
        -ms-transition: color .1s ease;
        -o-transition: color .1s ease;
        transition: color .1s ease; }
        .tabs-top.tabs-striped .tab-item.tab-item-active, .tabs-top.tabs-striped .tab-item.active, .tabs-top.tabs-striped .tab-item.activated {
          margin-top: 1px;
          border-width: 0px 0px 2px 0px !important;
          border-style: solid; }
          .tabs-top.tabs-striped .tab-item.tab-item-active > .badge, .tabs-top.tabs-striped .tab-item.tab-item-active > i, .tabs-top.tabs-striped .tab-item.active > .badge, .tabs-top.tabs-striped .tab-item.active > i, .tabs-top.tabs-striped .tab-item.activated > .badge, .tabs-top.tabs-striped .tab-item.activated > i {
            margin-top: -1px; }
        .tabs-top.tabs-striped .tab-item .badge {
          -webkit-transition: color .2s ease;
          -moz-transition: color .2s ease;
          -ms-transition: color .2s ease;
          -o-transition: color .2s ease;
          transition: color .2s ease; }
      .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.tab-item-active .tab-title, .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.tab-item-active i, .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.active .tab-title, .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.active i, .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.activated .tab-title, .tabs-top.tabs-striped:not(.tabs-icon-left):not(.tabs-icon-top) .tab-item.activated i {
        display: block;
        margin-top: -1px; }
      .tabs-top.tabs-striped.tabs-icon-left .tab-item {
        margin-top: 1px; }
        .tabs-top.tabs-striped.tabs-icon-left .tab-item.tab-item-active .tab-title, .tabs-top.tabs-striped.tabs-icon-left .tab-item.tab-item-active i, .tabs-top.tabs-striped.tabs-icon-left .tab-item.active .tab-title, .tabs-top.tabs-striped.tabs-icon-left .tab-item.active i, .tabs-top.tabs-striped.tabs-icon-left .tab-item.activated .tab-title, .tabs-top.tabs-striped.tabs-icon-left .tab-item.activated i {
          margin-top: -0.1em; }

    /* Allow parent element to have tabs-top */
    /* If you change this, change platform.scss as well */
    .tabs-top > .tabs,
    .tabs.tabs-top {
      top: 44px;
      padding-top: 0;
      background-position: bottom;
      border-top-width: 0;
      border-bottom-width: 1px; }
      .tabs-top > .tabs .tab-item.tab-item-active .badge, .tabs-top > .tabs .tab-item.active .badge, .tabs-top > .tabs .tab-item.activated .badge,
      .tabs.tabs-top .tab-item.tab-item-active .badge,
      .tabs.tabs-top .tab-item.active .badge,
      .tabs.tabs-top .tab-item.activated .badge {
        top: 4%; }

    .tabs-top ~ .bar-header {
      border-bottom-width: 0; }

    .tab-item {
      -webkit-box-flex: 1;
      -webkit-flex: 1;
      -moz-box-flex: 1;
      -moz-flex: 1;
      -ms-flex: 1;
      flex: 1;
      display: block;
      overflow: hidden;
      max-width: 150px;
      height: 100%;
      color: inherit;
      text-align: center;
      text-decoration: none;
      text-overflow: ellipsis;
      white-space: nowrap;
      font-weight: 400;
      font-size: 14px;
      font-family: "-apple-system", "Helvetica Neue", "Roboto", "Segoe UI", sans-serif;
      opacity: 0.7; }
      .tab-item:hover {
        cursor: pointer; }
      .tab-item.tab-hidden {
        display: none; }

    .tabs-item-hide > .tabs,
    .tabs.tabs-item-hide {
      display: none; }

    .tabs-icon-top > .tabs .tab-item,
    .tabs-icon-top.tabs .tab-item,
    .tabs-icon-bottom > .tabs .tab-item,
    .tabs-icon-bottom.tabs .tab-item {
      font-size: 10px;
      line-height: 14px; }

    .tab-item .icon, .tab-item .icon-help, .tab-item .icon-alert, .tab-item #menu .footer .icon-help, #menu .footer .tab-item .icon-help {
      display: block;
      margin: 0 auto;
      height: 32px;
      font-size: 32px; }

    .tabs-icon-left.tabs .tab-item,
    .tabs-icon-left > .tabs .tab-item,
    .tabs-icon-right.tabs .tab-item,
    .tabs-icon-right > .tabs .tab-item {
      font-size: 10px; }
      .tabs-icon-left.tabs .tab-item .icon, .tabs-icon-left.tabs .tab-item .icon-help, .tabs-icon-left.tabs .tab-item .icon-alert, .tabs-icon-left.tabs .tab-item #menu .footer .icon-help, #menu .footer .tabs-icon-left.tabs .tab-item .icon-help, .tabs-icon-left.tabs .tab-item .tab-title,
      .tabs-icon-left > .tabs .tab-item .icon,
      .tabs-icon-left > .tabs .tab-item .icon-help,
      .tabs-icon-left > .tabs .tab-item .icon-alert,
      .tabs-icon-left > .tabs .tab-item #menu .footer .icon-help, #menu .footer
      .tabs-icon-left > .tabs .tab-item .icon-help,
      .tabs-icon-left > .tabs .tab-item .tab-title,
      .tabs-icon-right.tabs .tab-item .icon,
      .tabs-icon-right.tabs .tab-item .icon-help,
      .tabs-icon-right.tabs .tab-item .icon-alert,
      .tabs-icon-right.tabs .tab-item #menu .footer .icon-help, #menu .footer
      .tabs-icon-right.tabs .tab-item .icon-help,
      .tabs-icon-right.tabs .tab-item .tab-title,
      .tabs-icon-right > .tabs .tab-item .icon,
      .tabs-icon-right > .tabs .tab-item .icon-help,
      .tabs-icon-right > .tabs .tab-item .icon-alert,
      .tabs-icon-right > .tabs .tab-item #menu .footer .icon-help, #menu .footer
      .tabs-icon-right > .tabs .tab-item .icon-help,
      .tabs-icon-right > .tabs .tab-item .tab-title {
        display: inline-block;
        vertical-align: top;
        margin-top: -.1em; }
        .tabs-icon-left.tabs .tab-item .icon:before, .tabs-icon-left.tabs .tab-item .icon-help:before, .tabs-icon-left.tabs .tab-item .icon-alert:before, .tabs-icon-left.tabs .tab-item #menu .footer .icon-help:before, #menu .footer .tabs-icon-left.tabs .tab-item .icon-help:before, .tabs-icon-left.tabs .tab-item .tab-title:before,
        .tabs-icon-left > .tabs .tab-item .icon:before,
        .tabs-icon-left > .tabs .tab-item .icon-help:before,
        .tabs-icon-left > .tabs .tab-item .icon-alert:before,
        .tabs-icon-left > .tabs .tab-item #menu .footer .icon-help:before, #menu .footer
        .tabs-icon-left > .tabs .tab-item .icon-help:before,
        .tabs-icon-left > .tabs .tab-item .tab-title:before,
        .tabs-icon-right.tabs .tab-item .icon:before,
        .tabs-icon-right.tabs .tab-item .icon-help:before,
        .tabs-icon-right.tabs .tab-item .icon-alert:before,
        .tabs-icon-right.tabs .tab-item #menu .footer .icon-help:before, #menu .footer
        .tabs-icon-right.tabs .tab-item .icon-help:before,
        .tabs-icon-right.tabs .tab-item .tab-title:before,
        .tabs-icon-right > .tabs .tab-item .icon:before,
        .tabs-icon-right > .tabs .tab-item .icon-help:before,
        .tabs-icon-right > .tabs .tab-item .icon-alert:before,
        .tabs-icon-right > .tabs .tab-item #menu .footer .icon-help:before, #menu .footer
        .tabs-icon-right > .tabs .tab-item .icon-help:before,
        .tabs-icon-right > .tabs .tab-item .tab-title:before {
          font-size: 24px;
          line-height: 49px; }

    .tabs-icon-left > .tabs .tab-item .icon, .tabs-icon-left > .tabs .tab-item .icon-help, .tabs-icon-left > .tabs .tab-item .icon-alert, .tabs-icon-left > .tabs .tab-item #menu .footer .icon-help, #menu .footer .tabs-icon-left > .tabs .tab-item .icon-help,
    .tabs-icon-left.tabs .tab-item .icon,
    .tabs-icon-left.tabs .tab-item .icon-help,
    .tabs-icon-left.tabs .tab-item .icon-alert,
    .tabs-icon-left.tabs .tab-item #menu .footer .icon-help, #menu .footer
    .tabs-icon-left.tabs .tab-item .icon-help {
      padding-right: 3px; }

    .tabs-icon-right > .tabs .tab-item .icon, .tabs-icon-right > .tabs .tab-item .icon-help, .tabs-icon-right > .tabs .tab-item .icon-alert, .tabs-icon-right > .tabs .tab-item #menu .footer .icon-help, #menu .footer .tabs-icon-right > .tabs .tab-item .icon-help,
    .tabs-icon-right.tabs .tab-item .icon,
    .tabs-icon-right.tabs .tab-item .icon-help,
    .tabs-icon-right.tabs .tab-item .icon-alert,
    .tabs-icon-right.tabs .tab-item #menu .footer .icon-help, #menu .footer
    .tabs-icon-right.tabs .tab-item .icon-help {
      padding-left: 3px; }

    .tabs-icon-only > .tabs .icon, .tabs-icon-only > .tabs .icon-help, .tabs-icon-only > .tabs .icon-alert, .tabs-icon-only > .tabs #menu .footer .icon-help, #menu .footer .tabs-icon-only > .tabs .icon-help,
    .tabs-icon-only.tabs .icon,
    .tabs-icon-only.tabs .icon-help,
    .tabs-icon-only.tabs .icon-alert,
    .tabs-icon-only.tabs #menu .footer .icon-help, #menu .footer
    .tabs-icon-only.tabs .icon-help {
      line-height: inherit; }

    .tab-item.has-badge {
      position: relative; }

    .tab-item .badge {
      position: absolute;
      top: 4%;
      right: 33%;
      right: calc(50% - 26px);
      padding: 1px 6px;
      height: auto;
      font-size: 12px;
      line-height: 16px; }

    /* Navigational tab */
    /* Active state for tab */
    .tab-item.tab-item-active,
    .tab-item.active,
    .tab-item.activated {
      opacity: 1; }
      .tab-item.tab-item-active.tab-item-light,
      .tab-item.active.tab-item-light,
      .tab-item.activated.tab-item-light {
        color: #fff; }
      .tab-item.tab-item-active.tab-item-stable,
      .tab-item.active.tab-item-stable,
      .tab-item.activated.tab-item-stable {
        color: #f8f8f8; }
      .tab-item.tab-item-active.tab-item-positive,
      .tab-item.active.tab-item-positive,
      .tab-item.activated.tab-item-positive {
        color: #387ef5; }
      .tab-item.tab-item-active.tab-item-calm,
      .tab-item.active.tab-item-calm,
      .tab-item.activated.tab-item-calm {
        color: #11c1f3; }
      .tab-item.tab-item-active.tab-item-assertive,
      .tab-item.active.tab-item-assertive,
      .tab-item.activated.tab-item-assertive {
        color: #ef473a; }
      .tab-item.tab-item-active.tab-item-balanced,
      .tab-item.active.tab-item-balanced,
      .tab-item.activated.tab-item-balanced {
        color: #33cd5f; }
      .tab-item.tab-item-active.tab-item-energized,
      .tab-item.active.tab-item-energized,
      .tab-item.activated.tab-item-energized {
        color: #ffc900; }
      .tab-item.tab-item-active.tab-item-royal,
      .tab-item.active.tab-item-royal,
      .tab-item.activated.tab-item-royal {
        color: #886aea; }
      .tab-item.tab-item-active.tab-item-dark,
      .tab-item.active.tab-item-dark,
      .tab-item.activated.tab-item-dark {
        color: #444; }

    .item.tabs {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      padding: 0; }
      .item.tabs .icon:before, .item.tabs .icon-help:before, .item.tabs .icon-alert:before, .item.tabs #menu .footer .icon-help:before, #menu .footer .item.tabs .icon-help:before {
        position: relative; }

    .tab-item.disabled,
    .tab-item[disabled] {
      opacity: .4;
      cursor: default;
      pointer-events: none; }

    .nav-bar-tabs-top.hide ~ .view-container .tabs-top .tabs {
      top: 0; }

    .pane[hide-nav-bar="true"] .has-tabs-top {
      top: 49px; }

    /**
     * Menus
     * --------------------------------------------------
     * Side panel structure
     */
    .menu {
      position: absolute;
      top: 0;
      bottom: 0;
      z-index: 0;
      overflow: hidden;
      min-height: 100%;
      max-height: 100%;
      width: 275px;
      background-color: #fff; }
      .menu .scroll-content {
        z-index: 10; }
      .menu .bar-header {
        z-index: 11; }

    .menu-content {
      -webkit-transform: none;
      transform: none;
      box-shadow: -1px 0px 2px rgba(0, 0, 0, 0.2), 1px 0px 2px rgba(0, 0, 0, 0.2); }

    .menu-open .menu-content .pane,
    .menu-open .menu-content .scroll-content {
      pointer-events: none; }

    .menu-open .menu-content .scroll-content .scroll {
      pointer-events: none; }

    .menu-open .menu-content .scroll-content:not(.overflow-scroll) {
      overflow: hidden; }

    .grade-b .menu-content,
    .grade-c .menu-content {
      -webkit-box-sizing: content-box;
      -moz-box-sizing: content-box;
      box-sizing: content-box;
      right: -1px;
      left: -1px;
      border-right: 1px solid #ccc;
      border-left: 1px solid #ccc;
      box-shadow: none; }

    .menu-left {
      left: 0; }

    .menu-right {
      right: 0; }

    .aside-open.aside-resizing .menu-right {
      display: none; }

    .menu-animated {
      -webkit-transition: -webkit-transform 200ms ease;
      transition: transform 200ms ease; }

    /**
     * Modals
     * --------------------------------------------------
     * Modals are independent windows that slide in from off-screen.
     */
    .modal-backdrop,
    .modal-backdrop-bg {
      position: fixed;
      top: 0;
      left: 0;
      z-index: 10;
      width: 100%;
      height: 100%; }

    .modal-backdrop-bg {
      pointer-events: none; }

    .modal {
      display: block;
      position: absolute;
      top: 0;
      z-index: 10;
      overflow: hidden;
      min-height: 100%;
      width: 100%;
      background-color: #fff; }

    @media (min-width: 680px) {
      .modal {
        top: 20%;
        right: 20%;
        bottom: 20%;
        left: 20%;
        min-height: 240px;
        width: 60%; }
      .modal.ng-leave-active {
        bottom: 0; }
      .platform-ios.platform-cordova .modal-wrapper .modal .bar-header:not(.bar-subheader) {
        height: 44px; }
        .platform-ios.platform-cordova .modal-wrapper .modal .bar-header:not(.bar-subheader) > * {
          margin-top: 0; }
      .platform-ios.platform-cordova .modal-wrapper .modal .tabs-top > .tabs,
      .platform-ios.platform-cordova .modal-wrapper .modal .tabs.tabs-top {
        top: 44px; }
      .platform-ios.platform-cordova .modal-wrapper .modal .has-header,
      .platform-ios.platform-cordova .modal-wrapper .modal .bar-subheader {
        top: 44px; }
      .platform-ios.platform-cordova .modal-wrapper .modal .has-subheader {
        top: 88px; }
      .platform-ios.platform-cordova .modal-wrapper .modal .has-header.has-tabs-top {
        top: 93px; }
      .platform-ios.platform-cordova .modal-wrapper .modal .has-header.has-subheader.has-tabs-top {
        top: 137px; }
      .modal-backdrop-bg {
        -webkit-transition: opacity 300ms ease-in-out;
        transition: opacity 300ms ease-in-out;
        background-color: #000;
        opacity: 0; }
      .active .modal-backdrop-bg {
        opacity: 0.5; } }

    .modal-open {
      pointer-events: none; }
      .modal-open .modal,
      .modal-open .modal-backdrop {
        pointer-events: auto; }
      .modal-open.loading-active .modal,
      .modal-open.loading-active .modal-backdrop {
        pointer-events: none; }

    /**
     * Popovers
     * --------------------------------------------------
     * Popovers are independent views which float over content
     */
    .popover-backdrop {
      position: fixed;
      top: 0;
      left: 0;
      z-index: 10;
      width: 100%;
      height: 100%;
      background-color: transparent; }
      .popover-backdrop.active {
        background-color: rgba(0, 0, 0, 0.1); }

    .popover {
      position: absolute;
      top: 25%;
      left: 50%;
      z-index: 10;
      display: block;
      margin-top: 12px;
      margin-left: -110px;
      height: 280px;
      width: 220px;
      background-color: #fff;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
      opacity: 0; }
      .popover .item:first-child {
        border-top: 0; }
      .popover .item:last-child {
        border-bottom: 0; }
      .popover.popover-bottom {
        margin-top: -12px; }

    .popover,
    .popover .bar-header {
      border-radius: 2px; }

    .popover .scroll-content {
      z-index: 1;
      margin: 2px 0; }

    .popover .bar-header {
      border-bottom-right-radius: 0;
      border-bottom-left-radius: 0; }

    .popover .has-header {
      border-top-right-radius: 0;
      border-top-left-radius: 0; }

    .popover-arrow {
      display: none; }

    .platform-ios .popover {
      box-shadow: 0 0 40px rgba(0, 0, 0, 0.08);
      border-radius: 10px; }

    .platform-ios .popover .bar-header {
      -webkit-border-top-right-radius: 10px;
      border-top-right-radius: 10px;
      -webkit-border-top-left-radius: 10px;
      border-top-left-radius: 10px; }

    .platform-ios .popover .scroll-content {
      margin: 8px 0;
      border-radius: 10px; }

    .platform-ios .popover .scroll-content.has-header {
      margin-top: 0; }

    .platform-ios .popover-arrow {
      position: absolute;
      display: block;
      top: -17px;
      width: 30px;
      height: 19px;
      overflow: hidden; }
      .platform-ios .popover-arrow:after {
        position: absolute;
        top: 12px;
        left: 5px;
        width: 20px;
        height: 20px;
        background-color: #fff;
        border-radius: 3px;
        content: '';
        -webkit-transform: rotate(-45deg);
        transform: rotate(-45deg); }

    .platform-ios .popover-bottom .popover-arrow {
      top: auto;
      bottom: -10px; }
      .platform-ios .popover-bottom .popover-arrow:after {
        top: -6px; }

    .platform-android .popover {
      margin-top: -32px;
      background-color: #fafafa;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.35); }
      .platform-android .popover .item {
        border-color: #fafafa;
        background-color: #fafafa;
        color: #4d4d4d; }
      .platform-android .popover.popover-bottom {
        margin-top: 32px; }

    .platform-android .popover-backdrop,
    .platform-android .popover-backdrop.active {
      background-color: transparent; }

    .popover-open {
      pointer-events: none; }
      .popover-open .popover,
      .popover-open .popover-backdrop {
        pointer-events: auto; }
      .popover-open.loading-active .popover,
      .popover-open.loading-active .popover-backdrop {
        pointer-events: none; }

    @media (min-width: 680px) {
      .popover {
        width: 360px;
        margin-left: -180px; } }

    /**
     * Popups
     * --------------------------------------------------
     */
    .popup-container {
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      right: 0;
      background: transparent;
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      -webkit-justify-content: center;
      -moz-justify-content: center;
      justify-content: center;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      z-index: 12;
      visibility: hidden; }
      .popup-container.popup-showing {
        visibility: visible; }
      .popup-container.popup-hidden .popup {
        -webkit-animation-name: scaleOut;
        animation-name: scaleOut;
        -webkit-animation-duration: 0.1s;
        animation-duration: 0.1s;
        -webkit-animation-timing-function: ease-in-out;
        animation-timing-function: ease-in-out;
        -webkit-animation-fill-mode: both;
        animation-fill-mode: both; }
      .popup-container.active .popup {
        -webkit-animation-name: superScaleIn;
        animation-name: superScaleIn;
        -webkit-animation-duration: 0.2s;
        animation-duration: 0.2s;
        -webkit-animation-timing-function: ease-in-out;
        animation-timing-function: ease-in-out;
        -webkit-animation-fill-mode: both;
        animation-fill-mode: both; }
      .popup-container .popup {
        width: 250px;
        max-width: 100%;
        max-height: 90%;
        border-radius: 0px;
        background-color: rgba(255, 255, 255, 0.9);
        display: -webkit-box;
        display: -webkit-flex;
        display: -moz-box;
        display: -moz-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-box-direction: normal;
        -webkit-box-orient: vertical;
        -webkit-flex-direction: column;
        -moz-flex-direction: column;
        -ms-flex-direction: column;
        flex-direction: column; }
      .popup-container input,
      .popup-container textarea {
        width: 100%; }

    .popup-head {
      padding: 15px 10px;
      border-bottom: 1px solid #eee;
      text-align: center; }

    .popup-title {
      margin: 0;
      padding: 0;
      font-size: 15px; }

    .popup-sub-title {
      margin: 5px 0 0 0;
      padding: 0;
      font-weight: normal;
      font-size: 11px; }

    .popup-body {
      padding: 10px;
      overflow: auto; }

    .popup-buttons {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-direction: normal;
      -webkit-box-orient: horizontal;
      -webkit-flex-direction: row;
      -moz-flex-direction: row;
      -ms-flex-direction: row;
      flex-direction: row;
      padding: 10px;
      min-height: 65px; }
      .popup-buttons .button {
        -webkit-box-flex: 1;
        -webkit-flex: 1;
        -moz-box-flex: 1;
        -moz-flex: 1;
        -ms-flex: 1;
        flex: 1;
        display: block;
        min-height: 45px;
        border-radius: 2px;
        line-height: 20px;
        margin-right: 5px; }
        .popup-buttons .button:last-child {
          margin-right: 0px; }

    .popup-open {
      pointer-events: none; }
      .popup-open.modal-open .modal {
        pointer-events: none; }
      .popup-open .popup-backdrop, .popup-open .popup {
        pointer-events: auto; }

    /**
     * Loading
     * --------------------------------------------------
     */
    .loading-container {
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
      z-index: 13;
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      -webkit-justify-content: center;
      -moz-justify-content: center;
      justify-content: center;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      -webkit-transition: 0.2s opacity linear;
      transition: 0.2s opacity linear;
      visibility: hidden;
      opacity: 0; }
      .loading-container:not(.visible) .icon, .loading-container:not(.visible) .icon-help, .loading-container:not(.visible) .icon-alert, .loading-container:not(.visible) #menu .footer .icon-help, #menu .footer .loading-container:not(.visible) .icon-help,
      .loading-container:not(.visible) .spinner {
        display: none; }
      .loading-container.visible {
        visibility: visible; }
      .loading-container.active {
        opacity: 1; }
      .loading-container .loading {
        padding: 20px;
        border-radius: 5px;
        background-color: rgba(0, 0, 0, 0.7);
        color: #fff;
        text-align: center;
        text-overflow: ellipsis;
        font-size: 15px; }
        .loading-container .loading h1, .loading-container .loading h2, .loading-container .loading h3, .loading-container .loading h4, .loading-container .loading h5, .loading-container .loading h6 {
          color: #fff; }

    /**
     * Items
     * --------------------------------------------------
     */
    .item {
      border-color: #ddd;
      background-color: #fff;
      color: #444;
      position: relative;
      z-index: 2;
      display: block;
      margin: -1px;
      padding: 16px;
      border-width: 1px;
      border-style: solid;
      font-size: 16px; }
      .item h2 {
        margin: 0 0 2px 0;
        font-size: 16px;
        font-weight: normal; }
      .item h3 {
        margin: 0 0 4px 0;
        font-size: 14px; }
      .item h4 {
        margin: 0 0 4px 0;
        font-size: 12px; }
      .item h5, .item h6 {
        margin: 0 0 3px 0;
        font-size: 10px; }
      .item p {
        color: #666;
        font-size: 14px;
        margin-bottom: 2px; }
      .item h1:last-child,
      .item h2:last-child,
      .item h3:last-child,
      .item h4:last-child,
      .item h5:last-child,
      .item h6:last-child,
      .item p:last-child {
        margin-bottom: 0; }
      .item .badge {
        display: -webkit-box;
        display: -webkit-flex;
        display: -moz-box;
        display: -moz-flex;
        display: -ms-flexbox;
        display: flex;
        position: absolute;
        top: 16px;
        right: 32px; }
      .item.item-button-right .badge {
        right: 67px; }
      .item.item-divider .badge {
        top: 8px; }
      .item .badge + .badge {
        margin-right: 5px; }
      .item.item-light {
        border-color: #ddd;
        background-color: #fff;
        color: #444; }
      .item.item-stable {
        border-color: #b2b2b2;
        background-color: #f8f8f8;
        color: #444; }
      .item.item-positive {
        border-color: #0c60ee;
        background-color: #387ef5;
        color: #fff; }
      .item.item-calm {
        border-color: #0a9dc7;
        background-color: #11c1f3;
        color: #fff; }
      .item.item-assertive {
        border-color: #e42112;
        background-color: #ef473a;
        color: #fff; }
      .item.item-balanced {
        border-color: #28a54c;
        background-color: #33cd5f;
        color: #fff; }
      .item.item-energized {
        border-color: #e6b500;
        background-color: #ffc900;
        color: #fff; }
      .item.item-royal {
        border-color: #6b46e5;
        background-color: #886aea;
        color: #fff; }
      .item.item-dark {
        border-color: #111;
        background-color: #444;
        color: #fff; }
      .item[ng-click]:hover {
        cursor: pointer; }

    .list-borderless .item,
    .item-borderless {
      border-width: 0; }

    .item.active,
    .item.activated,
    .item-complex.active .item-content,
    .item-complex.activated .item-content,
    .item .item-content.active,
    .item .item-content.activated {
      border-color: #ccc;
      background-color: #D9D9D9; }
      .item.active.item-complex > .item-content,
      .item.activated.item-complex > .item-content,
      .item-complex.active .item-content.item-complex > .item-content,
      .item-complex.activated .item-content.item-complex > .item-content,
      .item .item-content.active.item-complex > .item-content,
      .item .item-content.activated.item-complex > .item-content {
        border-color: #ccc;
        background-color: #D9D9D9; }
      .item.active.item-light,
      .item.activated.item-light,
      .item-complex.active .item-content.item-light,
      .item-complex.activated .item-content.item-light,
      .item .item-content.active.item-light,
      .item .item-content.activated.item-light {
        border-color: #ccc;
        background-color: #fafafa; }
        .item.active.item-light.item-complex > .item-content,
        .item.activated.item-light.item-complex > .item-content,
        .item-complex.active .item-content.item-light.item-complex > .item-content,
        .item-complex.activated .item-content.item-light.item-complex > .item-content,
        .item .item-content.active.item-light.item-complex > .item-content,
        .item .item-content.activated.item-light.item-complex > .item-content {
          border-color: #ccc;
          background-color: #fafafa; }
      .item.active.item-stable,
      .item.activated.item-stable,
      .item-complex.active .item-content.item-stable,
      .item-complex.activated .item-content.item-stable,
      .item .item-content.active.item-stable,
      .item .item-content.activated.item-stable {
        border-color: #a2a2a2;
        background-color: #e5e5e5; }
        .item.active.item-stable.item-complex > .item-content,
        .item.activated.item-stable.item-complex > .item-content,
        .item-complex.active .item-content.item-stable.item-complex > .item-content,
        .item-complex.activated .item-content.item-stable.item-complex > .item-content,
        .item .item-content.active.item-stable.item-complex > .item-content,
        .item .item-content.activated.item-stable.item-complex > .item-content {
          border-color: #a2a2a2;
          background-color: #e5e5e5; }
      .item.active.item-positive,
      .item.activated.item-positive,
      .item-complex.active .item-content.item-positive,
      .item-complex.activated .item-content.item-positive,
      .item .item-content.active.item-positive,
      .item .item-content.activated.item-positive {
        border-color: #0c60ee;
        background-color: #0c60ee; }
        .item.active.item-positive.item-complex > .item-content,
        .item.activated.item-positive.item-complex > .item-content,
        .item-complex.active .item-content.item-positive.item-complex > .item-content,
        .item-complex.activated .item-content.item-positive.item-complex > .item-content,
        .item .item-content.active.item-positive.item-complex > .item-content,
        .item .item-content.activated.item-positive.item-complex > .item-content {
          border-color: #0c60ee;
          background-color: #0c60ee; }
      .item.active.item-calm,
      .item.activated.item-calm,
      .item-complex.active .item-content.item-calm,
      .item-complex.activated .item-content.item-calm,
      .item .item-content.active.item-calm,
      .item .item-content.activated.item-calm {
        border-color: #0a9dc7;
        background-color: #0a9dc7; }
        .item.active.item-calm.item-complex > .item-content,
        .item.activated.item-calm.item-complex > .item-content,
        .item-complex.active .item-content.item-calm.item-complex > .item-content,
        .item-complex.activated .item-content.item-calm.item-complex > .item-content,
        .item .item-content.active.item-calm.item-complex > .item-content,
        .item .item-content.activated.item-calm.item-complex > .item-content {
          border-color: #0a9dc7;
          background-color: #0a9dc7; }
      .item.active.item-assertive,
      .item.activated.item-assertive,
      .item-complex.active .item-content.item-assertive,
      .item-complex.activated .item-content.item-assertive,
      .item .item-content.active.item-assertive,
      .item .item-content.activated.item-assertive {
        border-color: #e42112;
        background-color: #e42112; }
        .item.active.item-assertive.item-complex > .item-content,
        .item.activated.item-assertive.item-complex > .item-content,
        .item-complex.active .item-content.item-assertive.item-complex > .item-content,
        .item-complex.activated .item-content.item-assertive.item-complex > .item-content,
        .item .item-content.active.item-assertive.item-complex > .item-content,
        .item .item-content.activated.item-assertive.item-complex > .item-content {
          border-color: #e42112;
          background-color: #e42112; }
      .item.active.item-balanced,
      .item.activated.item-balanced,
      .item-complex.active .item-content.item-balanced,
      .item-complex.activated .item-content.item-balanced,
      .item .item-content.active.item-balanced,
      .item .item-content.activated.item-balanced {
        border-color: #28a54c;
        background-color: #28a54c; }
        .item.active.item-balanced.item-complex > .item-content,
        .item.activated.item-balanced.item-complex > .item-content,
        .item-complex.active .item-content.item-balanced.item-complex > .item-content,
        .item-complex.activated .item-content.item-balanced.item-complex > .item-content,
        .item .item-content.active.item-balanced.item-complex > .item-content,
        .item .item-content.activated.item-balanced.item-complex > .item-content {
          border-color: #28a54c;
          background-color: #28a54c; }
      .item.active.item-energized,
      .item.activated.item-energized,
      .item-complex.active .item-content.item-energized,
      .item-complex.activated .item-content.item-energized,
      .item .item-content.active.item-energized,
      .item .item-content.activated.item-energized {
        border-color: #e6b500;
        background-color: #e6b500; }
        .item.active.item-energized.item-complex > .item-content,
        .item.activated.item-energized.item-complex > .item-content,
        .item-complex.active .item-content.item-energized.item-complex > .item-content,
        .item-complex.activated .item-content.item-energized.item-complex > .item-content,
        .item .item-content.active.item-energized.item-complex > .item-content,
        .item .item-content.activated.item-energized.item-complex > .item-content {
          border-color: #e6b500;
          background-color: #e6b500; }
      .item.active.item-royal,
      .item.activated.item-royal,
      .item-complex.active .item-content.item-royal,
      .item-complex.activated .item-content.item-royal,
      .item .item-content.active.item-royal,
      .item .item-content.activated.item-royal {
        border-color: #6b46e5;
        background-color: #6b46e5; }
        .item.active.item-royal.item-complex > .item-content,
        .item.activated.item-royal.item-complex > .item-content,
        .item-complex.active .item-content.item-royal.item-complex > .item-content,
        .item-complex.activated .item-content.item-royal.item-complex > .item-content,
        .item .item-content.active.item-royal.item-complex > .item-content,
        .item .item-content.activated.item-royal.item-complex > .item-content {
          border-color: #6b46e5;
          background-color: #6b46e5; }
      .item.active.item-dark,
      .item.activated.item-dark,
      .item-complex.active .item-content.item-dark,
      .item-complex.activated .item-content.item-dark,
      .item .item-content.active.item-dark,
      .item .item-content.activated.item-dark {
        border-color: #000;
        background-color: #262626; }
        .item.active.item-dark.item-complex > .item-content,
        .item.activated.item-dark.item-complex > .item-content,
        .item-complex.active .item-content.item-dark.item-complex > .item-content,
        .item-complex.activated .item-content.item-dark.item-complex > .item-content,
        .item .item-content.active.item-dark.item-complex > .item-content,
        .item .item-content.activated.item-dark.item-complex > .item-content {
          border-color: #000;
          background-color: #262626; }

    .item,
    .item h1,
    .item h2,
    .item h3,
    .item h4,
    .item h5,
    .item h6,
    .item p,
    .item-content,
    .item-content h1,
    .item-content h2,
    .item-content h3,
    .item-content h4,
    .item-content h5,
    .item-content h6,
    .item-content p {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap; }

    a.item {
      color: inherit;
      text-decoration: none; }
      a.item:hover, a.item:focus {
        text-decoration: none; }

    /**
     * Complex Items
     * --------------------------------------------------
     * Adding .item-complex allows the .item to be slidable and
     * have options underneath the button, but also requires an
     * additional .item-content element inside .item.
     * Basically .item-complex removes any default settings which
     * .item added, so that .item-content looks them as just .item.
     */
    .item-complex,
    a.item.item-complex,
    button.item.item-complex {
      padding: 0; }

    .item-complex .item-content,
    .item-radio .item-content {
      position: relative;
      z-index: 2;
      padding: 16px 49px 16px 16px;
      border: none;
      background-color: #fff; }

    a.item-content {
      display: block;
      color: inherit;
      text-decoration: none; }

    .item-text-wrap .item,
    .item-text-wrap .item-content,
    .item-text-wrap,
    .item-text-wrap h1,
    .item-text-wrap h2,
    .item-text-wrap h3,
    .item-text-wrap h4,
    .item-text-wrap h5,
    .item-text-wrap h6,
    .item-text-wrap p,
    .item-complex.item-text-wrap .item-content,
    .item-body h1,
    .item-body h2,
    .item-body h3,
    .item-body h4,
    .item-body h5,
    .item-body h6,
    .item-body p {
      overflow: visible;
      white-space: normal; }

    .item-complex.item-text-wrap,
    .item-complex.item-text-wrap h1,
    .item-complex.item-text-wrap h2,
    .item-complex.item-text-wrap h3,
    .item-complex.item-text-wrap h4,
    .item-complex.item-text-wrap h5,
    .item-complex.item-text-wrap h6,
    .item-complex.item-text-wrap p {
      overflow: visible;
      white-space: normal; }

    .item-complex.item-light > .item-content {
      border-color: #ddd;
      background-color: #fff;
      color: #444; }
      .item-complex.item-light > .item-content.active, .item-complex.item-light > .item-content:active {
        border-color: #ccc;
        background-color: #fafafa; }
        .item-complex.item-light > .item-content.active.item-complex > .item-content, .item-complex.item-light > .item-content:active.item-complex > .item-content {
          border-color: #ccc;
          background-color: #fafafa; }

    .item-complex.item-stable > .item-content {
      border-color: #b2b2b2;
      background-color: #f8f8f8;
      color: #444; }
      .item-complex.item-stable > .item-content.active, .item-complex.item-stable > .item-content:active {
        border-color: #a2a2a2;
        background-color: #e5e5e5; }
        .item-complex.item-stable > .item-content.active.item-complex > .item-content, .item-complex.item-stable > .item-content:active.item-complex > .item-content {
          border-color: #a2a2a2;
          background-color: #e5e5e5; }

    .item-complex.item-positive > .item-content {
      border-color: #0c60ee;
      background-color: #387ef5;
      color: #fff; }
      .item-complex.item-positive > .item-content.active, .item-complex.item-positive > .item-content:active {
        border-color: #0c60ee;
        background-color: #0c60ee; }
        .item-complex.item-positive > .item-content.active.item-complex > .item-content, .item-complex.item-positive > .item-content:active.item-complex > .item-content {
          border-color: #0c60ee;
          background-color: #0c60ee; }

    .item-complex.item-calm > .item-content {
      border-color: #0a9dc7;
      background-color: #11c1f3;
      color: #fff; }
      .item-complex.item-calm > .item-content.active, .item-complex.item-calm > .item-content:active {
        border-color: #0a9dc7;
        background-color: #0a9dc7; }
        .item-complex.item-calm > .item-content.active.item-complex > .item-content, .item-complex.item-calm > .item-content:active.item-complex > .item-content {
          border-color: #0a9dc7;
          background-color: #0a9dc7; }

    .item-complex.item-assertive > .item-content {
      border-color: #e42112;
      background-color: #ef473a;
      color: #fff; }
      .item-complex.item-assertive > .item-content.active, .item-complex.item-assertive > .item-content:active {
        border-color: #e42112;
        background-color: #e42112; }
        .item-complex.item-assertive > .item-content.active.item-complex > .item-content, .item-complex.item-assertive > .item-content:active.item-complex > .item-content {
          border-color: #e42112;
          background-color: #e42112; }

    .item-complex.item-balanced > .item-content {
      border-color: #28a54c;
      background-color: #33cd5f;
      color: #fff; }
      .item-complex.item-balanced > .item-content.active, .item-complex.item-balanced > .item-content:active {
        border-color: #28a54c;
        background-color: #28a54c; }
        .item-complex.item-balanced > .item-content.active.item-complex > .item-content, .item-complex.item-balanced > .item-content:active.item-complex > .item-content {
          border-color: #28a54c;
          background-color: #28a54c; }

    .item-complex.item-energized > .item-content {
      border-color: #e6b500;
      background-color: #ffc900;
      color: #fff; }
      .item-complex.item-energized > .item-content.active, .item-complex.item-energized > .item-content:active {
        border-color: #e6b500;
        background-color: #e6b500; }
        .item-complex.item-energized > .item-content.active.item-complex > .item-content, .item-complex.item-energized > .item-content:active.item-complex > .item-content {
          border-color: #e6b500;
          background-color: #e6b500; }

    .item-complex.item-royal > .item-content {
      border-color: #6b46e5;
      background-color: #886aea;
      color: #fff; }
      .item-complex.item-royal > .item-content.active, .item-complex.item-royal > .item-content:active {
        border-color: #6b46e5;
        background-color: #6b46e5; }
        .item-complex.item-royal > .item-content.active.item-complex > .item-content, .item-complex.item-royal > .item-content:active.item-complex > .item-content {
          border-color: #6b46e5;
          background-color: #6b46e5; }

    .item-complex.item-dark > .item-content {
      border-color: #111;
      background-color: #444;
      color: #fff; }
      .item-complex.item-dark > .item-content.active, .item-complex.item-dark > .item-content:active {
        border-color: #000;
        background-color: #262626; }
        .item-complex.item-dark > .item-content.active.item-complex > .item-content, .item-complex.item-dark > .item-content:active.item-complex > .item-content {
          border-color: #000;
          background-color: #262626; }

    /**
     * Item Icons
     * --------------------------------------------------
     */
    .item-icon-left .icon, .item-icon-left .icon-help, .item-icon-left .icon-alert, .item-icon-left #menu .footer .icon-help, #menu .footer .item-icon-left .icon-help,
    .item-icon-right .icon,
    .item-icon-right .icon-help,
    .item-icon-right .icon-alert,
    .item-icon-right #menu .footer .icon-help, #menu .footer
    .item-icon-right .icon-help {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      position: absolute;
      top: 0;
      height: 100%;
      font-size: 32px; }
      .item-icon-left .icon:before, .item-icon-left .icon-help:before, .item-icon-left .icon-alert:before, .item-icon-left #menu .footer .icon-help:before, #menu .footer .item-icon-left .icon-help:before,
      .item-icon-right .icon:before,
      .item-icon-right .icon-help:before,
      .item-icon-right .icon-alert:before,
      .item-icon-right #menu .footer .icon-help:before, #menu .footer
      .item-icon-right .icon-help:before {
        display: block;
        width: 32px;
        text-align: center; }

    .item .fill-icon {
      min-width: 30px;
      min-height: 30px;
      font-size: 28px; }

    .item-icon-left {
      padding-left: 54px; }
      .item-icon-left .icon, .item-icon-left .icon-help, .item-icon-left .icon-alert, .item-icon-left #menu .footer .icon-help, #menu .footer .item-icon-left .icon-help {
        left: 11px; }

    .item-complex.item-icon-left {
      padding-left: 0; }
      .item-complex.item-icon-left .item-content {
        padding-left: 54px; }

    .item-icon-right {
      padding-right: 54px; }
      .item-icon-right .icon, .item-icon-right .icon-help, .item-icon-right .icon-alert, .item-icon-right #menu .footer .icon-help, #menu .footer .item-icon-right .icon-help {
        right: 11px; }

    .item-complex.item-icon-right {
      padding-right: 0; }
      .item-complex.item-icon-right .item-content {
        padding-right: 54px; }

    .item-icon-left.item-icon-right .icon:first-child, .item-icon-left.item-icon-right .icon-help:first-child, .item-icon-left.item-icon-right .icon-alert:first-child, .item-icon-left.item-icon-right #menu .footer .icon-help:first-child, #menu .footer .item-icon-left.item-icon-right .icon-help:first-child {
      right: auto; }

    .item-icon-left.item-icon-right .icon:last-child, .item-icon-left.item-icon-right .icon-help:last-child, .item-icon-left.item-icon-right .icon-alert:last-child, .item-icon-left.item-icon-right #menu .footer .icon-help:last-child, #menu .footer .item-icon-left.item-icon-right .icon-help:last-child,
    .item-icon-left .item-delete .icon,
    .item-icon-left .item-delete .icon-help,
    .item-icon-left .item-delete .icon-alert,
    .item-icon-left .item-delete #menu .footer .icon-help, #menu .footer
    .item-icon-left .item-delete .icon-help {
      left: auto; }

    .item-icon-left .icon-accessory,
    .item-icon-right .icon-accessory {
      color: #ccc;
      font-size: 16px; }

    .item-icon-left .icon-accessory {
      left: 3px; }

    .item-icon-right .icon-accessory {
      right: 3px; }

    /**
     * Item Button
     * --------------------------------------------------
     * An item button is a child button inside an .item (not the entire .item)
     */
    .item-button-left {
      padding-left: 72px; }

    .item-button-left > .button,
    .item-button-left .item-content > .button {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      position: absolute;
      top: 8px;
      left: 11px;
      min-width: 34px;
      min-height: 34px;
      font-size: 18px;
      line-height: 32px; }
      .item-button-left > .button .icon:before, .item-button-left > .button .icon-help:before, .item-button-left > .button .icon-alert:before, .item-button-left > .button #menu .footer .icon-help:before, #menu .footer .item-button-left > .button .icon-help:before,
      .item-button-left .item-content > .button .icon:before,
      .item-button-left .item-content > .button .icon-help:before,
      .item-button-left .item-content > .button .icon-alert:before,
      .item-button-left .item-content > .button #menu .footer .icon-help:before, #menu .footer
      .item-button-left .item-content > .button .icon-help:before {
        position: relative;
        left: auto;
        width: auto;
        line-height: 31px; }
      .item-button-left > .button > .button,
      .item-button-left .item-content > .button > .button {
        margin: 0px 2px;
        min-height: 34px;
        font-size: 18px;
        line-height: 32px; }

    .item-button-right,
    a.item.item-button-right,
    button.item.item-button-right {
      padding-right: 80px; }

    .item-button-right > .button,
    .item-button-right .item-content > .button,
    .item-button-right > .buttons,
    .item-button-right .item-content > .buttons {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      position: absolute;
      top: 8px;
      right: 16px;
      min-width: 34px;
      min-height: 34px;
      font-size: 18px;
      line-height: 32px; }
      .item-button-right > .button .icon:before, .item-button-right > .button .icon-help:before, .item-button-right > .button .icon-alert:before, .item-button-right > .button #menu .footer .icon-help:before, #menu .footer .item-button-right > .button .icon-help:before,
      .item-button-right .item-content > .button .icon:before,
      .item-button-right .item-content > .button .icon-help:before,
      .item-button-right .item-content > .button .icon-alert:before,
      .item-button-right .item-content > .button #menu .footer .icon-help:before, #menu .footer
      .item-button-right .item-content > .button .icon-help:before,
      .item-button-right > .buttons .icon:before,
      .item-button-right > .buttons .icon-help:before,
      .item-button-right > .buttons .icon-alert:before,
      .item-button-right > .buttons #menu .footer .icon-help:before, #menu .footer
      .item-button-right > .buttons .icon-help:before,
      .item-button-right .item-content > .buttons .icon:before,
      .item-button-right .item-content > .buttons .icon-help:before,
      .item-button-right .item-content > .buttons .icon-alert:before,
      .item-button-right .item-content > .buttons #menu .footer .icon-help:before, #menu .footer
      .item-button-right .item-content > .buttons .icon-help:before {
        position: relative;
        left: auto;
        width: auto;
        line-height: 31px; }
      .item-button-right > .button > .button,
      .item-button-right .item-content > .button > .button,
      .item-button-right > .buttons > .button,
      .item-button-right .item-content > .buttons > .button {
        margin: 0px 2px;
        min-width: 34px;
        min-height: 34px;
        font-size: 18px;
        line-height: 32px; }

    .item-button-left.item-button-right .button:first-child {
      right: auto; }

    .item-button-left.item-button-right .button:last-child {
      left: auto; }

    .item-avatar,
    .item-avatar .item-content,
    .item-avatar-left,
    .item-avatar-left .item-content {
      padding-left: 72px;
      min-height: 72px; }
      .item-avatar > img:first-child,
      .item-avatar .item-image,
      .item-avatar .item-content > img:first-child,
      .item-avatar .item-content .item-image,
      .item-avatar-left > img:first-child,
      .item-avatar-left .item-image,
      .item-avatar-left .item-content > img:first-child,
      .item-avatar-left .item-content .item-image {
        position: absolute;
        top: 16px;
        left: 16px;
        max-width: 40px;
        max-height: 40px;
        width: 100%;
        height: 100%;
        border-radius: 50%; }

    .item-avatar-right,
    .item-avatar-right .item-content {
      padding-right: 72px;
      min-height: 72px; }
      .item-avatar-right > img:first-child,
      .item-avatar-right .item-image,
      .item-avatar-right .item-content > img:first-child,
      .item-avatar-right .item-content .item-image {
        position: absolute;
        top: 16px;
        right: 16px;
        max-width: 40px;
        max-height: 40px;
        width: 100%;
        height: 100%;
        border-radius: 50%; }

    .item-thumbnail-left,
    .item-thumbnail-left .item-content {
      padding-top: 8px;
      padding-left: 106px;
      min-height: 100px; }
      .item-thumbnail-left > img:first-child,
      .item-thumbnail-left .item-image,
      .item-thumbnail-left .item-content > img:first-child,
      .item-thumbnail-left .item-content .item-image {
        position: absolute;
        top: 10px;
        left: 10px;
        max-width: 80px;
        max-height: 80px;
        width: 100%;
        height: 100%; }

    .item-avatar.item-complex,
    .item-avatar-left.item-complex,
    .item-thumbnail-left.item-complex {
      padding-top: 0;
      padding-left: 0; }

    .item-thumbnail-right,
    .item-thumbnail-right .item-content {
      padding-top: 8px;
      padding-right: 106px;
      min-height: 100px; }
      .item-thumbnail-right > img:first-child,
      .item-thumbnail-right .item-image,
      .item-thumbnail-right .item-content > img:first-child,
      .item-thumbnail-right .item-content .item-image {
        position: absolute;
        top: 10px;
        right: 10px;
        max-width: 80px;
        max-height: 80px;
        width: 100%;
        height: 100%; }

    .item-avatar-right.item-complex,
    .item-thumbnail-right.item-complex {
      padding-top: 0;
      padding-right: 0; }

    .item-image {
      padding: 0;
      text-align: center; }
      .item-image img:first-child, .item-image .list-img {
        width: 100%;
        vertical-align: middle; }

    .item-body {
      overflow: auto;
      padding: 16px;
      text-overflow: inherit;
      white-space: normal; }
      .item-body h1, .item-body h2, .item-body h3, .item-body h4, .item-body h5, .item-body h6, .item-body p {
        margin-top: 16px;
        margin-bottom: 16px; }

    .item-divider {
      padding-top: 8px;
      padding-bottom: 8px;
      min-height: 30px;
      background-color: #f5f5f5;
      color: #222;
      font-weight: 500; }

    .platform-ios .item-divider-platform,
    .item-divider-ios {
      padding-top: 26px;
      text-transform: uppercase;
      font-weight: 300;
      font-size: 13px;
      background-color: #efeff4;
      color: #555; }

    .platform-android .item-divider-platform,
    .item-divider-android {
      font-weight: 300;
      font-size: 13px; }

    .item-note {
      float: right;
      color: #aaa;
      font-size: 14px; }

    .item-left-editable .item-content,
    .item-right-editable .item-content {
      -webkit-transition-duration: 250ms;
      transition-duration: 250ms;
      -webkit-transition-timing-function: ease-in-out;
      transition-timing-function: ease-in-out;
      -webkit-transition-property: -webkit-transform;
      -moz-transition-property: -moz-transform;
      transition-property: transform; }

    .list-left-editing .item-left-editable .item-content,
    .item-left-editing.item-left-editable .item-content {
      -webkit-transform: translate3d(50px, 0, 0);
      transform: translate3d(50px, 0, 0); }

    .item-remove-animate.ng-leave {
      -webkit-transition-duration: 300ms;
      transition-duration: 300ms; }

    .item-remove-animate.ng-leave .item-content, .item-remove-animate.ng-leave:last-of-type {
      -webkit-transition-duration: 300ms;
      transition-duration: 300ms;
      -webkit-transition-timing-function: ease-in;
      transition-timing-function: ease-in;
      -webkit-transition-property: all;
      transition-property: all; }

    .item-remove-animate.ng-leave.ng-leave-active .item-content {
      opacity: 0;
      -webkit-transform: translate3d(-100%, 0, 0) !important;
      transform: translate3d(-100%, 0, 0) !important; }

    .item-remove-animate.ng-leave.ng-leave-active:last-of-type {
      opacity: 0; }

    .item-remove-animate.ng-leave.ng-leave-active ~ ion-item:not(.ng-leave) {
      -webkit-transform: translate3d(0, -webkit-calc(-100% + 1px), 0);
      transform: translate3d(0, calc(-100% + 1px), 0);
      -webkit-transition-duration: 300ms;
      transition-duration: 300ms;
      -webkit-transition-timing-function: cubic-bezier(0.25, 0.81, 0.24, 1);
      transition-timing-function: cubic-bezier(0.25, 0.81, 0.24, 1);
      -webkit-transition-property: all;
      transition-property: all; }

    .item-left-edit {
      -webkit-transition: all ease-in-out 125ms;
      transition: all ease-in-out 125ms;
      position: absolute;
      top: 0;
      left: 0;
      z-index: 0;
      width: 50px;
      height: 100%;
      line-height: 100%;
      display: none;
      opacity: 0;
      -webkit-transform: translate3d(-21px, 0, 0);
      transform: translate3d(-21px, 0, 0); }
      .item-left-edit .button {
        height: 100%; }
        .item-left-edit .button.icon, .item-left-edit .button.icon-help, .item-left-edit .button.icon-alert, .item-left-edit #menu .footer .button.icon-help, #menu .footer .item-left-edit .button.icon-help {
          display: -webkit-box;
          display: -webkit-flex;
          display: -moz-box;
          display: -moz-flex;
          display: -ms-flexbox;
          display: flex;
          -webkit-box-align: center;
          -ms-flex-align: center;
          -webkit-align-items: center;
          -moz-align-items: center;
          align-items: center;
          position: absolute;
          top: 0;
          height: 100%; }
      .item-left-edit.visible {
        display: block; }
        .item-left-edit.visible.active {
          opacity: 1;
          -webkit-transform: translate3d(8px, 0, 0);
          transform: translate3d(8px, 0, 0); }

    .list-left-editing .item-left-edit {
      -webkit-transition-delay: 125ms;
      transition-delay: 125ms; }

    .item-delete .button.icon, .item-delete .button.icon-help, .item-delete .button.icon-alert, .item-delete #menu .footer .button.icon-help, #menu .footer .item-delete .button.icon-help {
      color: #ef473a;
      font-size: 24px; }
      .item-delete .button.icon:hover, .item-delete .button.icon-help:hover, .item-delete .button.icon-alert:hover, .item-delete #menu .footer .button.icon-help:hover, #menu .footer .item-delete .button.icon-help:hover {
        opacity: .7; }

    .item-right-edit {
      -webkit-transition: all ease-in-out 250ms;
      transition: all ease-in-out 250ms;
      position: absolute;
      top: 0;
      right: 0;
      z-index: 3;
      width: 75px;
      height: 100%;
      background: inherit;
      padding-left: 20px;
      display: block;
      opacity: 0;
      -webkit-transform: translate3d(75px, 0, 0);
      transform: translate3d(75px, 0, 0); }
      .item-right-edit .button {
        min-width: 50px;
        height: 100%; }
        .item-right-edit .button.icon, .item-right-edit .button.icon-help, .item-right-edit .button.icon-alert, .item-right-edit #menu .footer .button.icon-help, #menu .footer .item-right-edit .button.icon-help {
          display: -webkit-box;
          display: -webkit-flex;
          display: -moz-box;
          display: -moz-flex;
          display: -ms-flexbox;
          display: flex;
          -webkit-box-align: center;
          -ms-flex-align: center;
          -webkit-align-items: center;
          -moz-align-items: center;
          align-items: center;
          position: absolute;
          top: 0;
          height: 100%;
          font-size: 32px; }
      .item-right-edit.visible {
        display: block; }
        .item-right-edit.visible.active {
          opacity: 1;
          -webkit-transform: translate3d(0, 0, 0);
          transform: translate3d(0, 0, 0); }

    .item-reorder .button.icon, .item-reorder .button.icon-help, .item-reorder .button.icon-alert, .item-reorder #menu .footer .button.icon-help, #menu .footer .item-reorder .button.icon-help {
      color: #444;
      font-size: 32px; }

    .item-reordering {
      position: absolute;
      left: 0;
      top: 0;
      z-index: 9;
      width: 100%;
      box-shadow: 0px 0px 10px 0px #aaa; }
      .item-reordering .item-reorder {
        z-index: 9; }

    .item-placeholder {
      opacity: 0.7; }

    /**
     * The hidden right-side buttons that can be exposed under a list item
     * with dragging.
     */
    .item-options {
      position: absolute;
      top: 0;
      right: 0;
      z-index: 1;
      height: 100%; }
      .item-options .button {
        height: 100%;
        border: none;
        border-radius: 0;
        display: -webkit-inline-box;
        display: -webkit-inline-flex;
        display: -moz-inline-flex;
        display: -ms-inline-flexbox;
        display: inline-flex;
        -webkit-box-align: center;
        -ms-flex-align: center;
        -webkit-align-items: center;
        -moz-align-items: center;
        align-items: center; }
        .item-options .button:before {
          margin: 0 auto; }

    /**
     * Lists
     * --------------------------------------------------
     */
    .list {
      position: relative;
      padding-top: 1px;
      padding-bottom: 1px;
      padding-left: 0;
      margin-bottom: 20px; }

    .list:last-child {
      margin-bottom: 0px; }
      .list:last-child.card {
        margin-bottom: 40px; }

    /**
     * List Header
     * --------------------------------------------------
     */
    .list-header {
      margin-top: 20px;
      padding: 5px 15px;
      background-color: transparent;
      color: #222;
      font-weight: bold; }

    .card.list .list-item {
      padding-right: 1px;
      padding-left: 1px; }

    /**
     * Cards and Inset Lists
     * --------------------------------------------------
     * A card and list-inset are close to the same thing, except a card as a box shadow.
     */
    .card,
    .list-inset {
      overflow: hidden;
      margin: 20px 10px;
      border-radius: 2px;
      background-color: #fff; }

    .card {
      padding-top: 1px;
      padding-bottom: 1px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3); }
      .card .item {
        border-left: 0;
        border-right: 0; }
      .card .item:first-child {
        border-top: 0; }
      .card .item:last-child {
        border-bottom: 0; }

    .padding .card, .item.large-button-bar .card, .padding .list-inset, .item.large-button-bar .list-inset {
      margin-left: 0;
      margin-right: 0; }

    .card .item:first-child,
    .list-inset .item:first-child,
    .padding > .list .item:first-child, .item.large-button-bar > .list .item:first-child {
      border-top-left-radius: 2px;
      border-top-right-radius: 2px; }
      .card .item:first-child .item-content,
      .list-inset .item:first-child .item-content,
      .padding > .list .item:first-child .item-content, .item.large-button-bar > .list .item:first-child .item-content {
        border-top-left-radius: 2px;
        border-top-right-radius: 2px; }

    .card .item:last-child,
    .list-inset .item:last-child,
    .padding > .list .item:last-child, .item.large-button-bar > .list .item:last-child {
      border-bottom-right-radius: 2px;
      border-bottom-left-radius: 2px; }
      .card .item:last-child .item-content,
      .list-inset .item:last-child .item-content,
      .padding > .list .item:last-child .item-content, .item.large-button-bar > .list .item:last-child .item-content {
        border-bottom-right-radius: 2px;
        border-bottom-left-radius: 2px; }

    .card .item:last-child,
    .list-inset .item:last-child {
      margin-bottom: -1px; }

    .card .item,
    .list-inset .item,
    .padding > .list .item, .item.large-button-bar > .list .item,
    .padding-horizontal > .list .item {
      margin-right: 0;
      margin-left: 0; }
      .card .item.item-input input,
      .list-inset .item.item-input input,
      .padding > .list .item.item-input input, .item.large-button-bar > .list .item.item-input input,
      .padding-horizontal > .list .item.item-input input {
        padding-right: 44px; }

    .padding-left > .list .item {
      margin-left: 0; }

    .padding-right > .list .item, .popover-share .bar-footer .button-close > .list .item {
      margin-right: 0; }

    /**
     * Badges
     * --------------------------------------------------
     */
    .badge {
      background-color: transparent;
      color: #AAAAAA;
      z-index: 1;
      display: inline-block;
      padding: 3px 8px;
      min-width: 10px;
      border-radius: 10px;
      vertical-align: baseline;
      text-align: center;
      white-space: nowrap;
      font-weight: bold;
      font-size: 14px;
      line-height: 16px; }
      .badge:empty {
        display: none; }

    .tabs .tab-item .badge.badge-light,
    .badge.badge-light {
      background-color: #fff;
      color: #444; }

    .tabs .tab-item .badge.badge-stable,
    .badge.badge-stable {
      background-color: #f8f8f8;
      color: #444; }

    .tabs .tab-item .badge.badge-positive,
    .badge.badge-positive {
      background-color: #387ef5;
      color: #fff; }

    .tabs .tab-item .badge.badge-calm,
    .badge.badge-calm {
      background-color: #11c1f3;
      color: #fff; }

    .tabs .tab-item .badge.badge-assertive, .tabs .tab-item .badge.badge-editable:hover,
    .badge.badge-assertive,
    .badge.badge-editable:hover {
      background-color: #ef473a;
      color: #fff; }

    .tabs .tab-item .badge.badge-balanced,
    .badge.badge-balanced {
      background-color: #33cd5f;
      color: #fff; }

    .tabs .tab-item .badge.badge-energized,
    .badge.badge-energized {
      background-color: #ffc900;
      color: #fff; }

    .tabs .tab-item .badge.badge-royal,
    .badge.badge-royal {
      background-color: #886aea;
      color: #fff; }

    .tabs .tab-item .badge.badge-dark,
    .badge.badge-dark {
      background-color: #444;
      color: #fff; }

    .button .badge {
      position: relative;
      top: -1px; }

    /**
     * Slide Box
     * --------------------------------------------------
     */
    .slider {
      position: relative;
      visibility: hidden;
      overflow: hidden; }

    .slider-slides {
      position: relative;
      height: 100%; }

    .slider-slide {
      position: relative;
      display: block;
      float: left;
      width: 100%;
      height: 100%;
      vertical-align: top; }

    .slider-slide-image > img {
      width: 100%; }

    .slider-pager {
      position: absolute;
      bottom: 20px;
      z-index: 1;
      width: 100%;
      height: 15px;
      text-align: center; }
      .slider-pager .slider-pager-page {
        display: inline-block;
        margin: 0px 3px;
        width: 15px;
        color: #000;
        text-decoration: none;
        opacity: 0.3; }
        .slider-pager .slider-pager-page.active {
          -webkit-transition: opacity 0.4s ease-in;
          transition: opacity 0.4s ease-in;
          opacity: 1; }

    .slider-slide.ng-enter, .slider-slide.ng-leave, .slider-slide.ng-animate,
    .slider-pager-page.ng-enter,
    .slider-pager-page.ng-leave,
    .slider-pager-page.ng-animate {
      -webkit-transition: none !important;
      transition: none !important; }

    .slider-slide.ng-animate,
    .slider-pager-page.ng-animate {
      -webkit-animation: none 0s;
      animation: none 0s; }

    /**
     * Swiper 3.2.7
     * Most modern mobile touch slider and framework with hardware accelerated transitions
     *
     * http://www.idangero.us/swiper/
     *
     * Copyright 2015, Vladimir Kharlampidi
     * The iDangero.us
     * http://www.idangero.us/
     *
     * Licensed under MIT
     *
     * Released on: December 7, 2015
     */
    .swiper-container {
      margin: 0 auto;
      position: relative;
      overflow: hidden;
      /* Fix of Webkit flickering */
      z-index: 1; }

    .swiper-container-no-flexbox .swiper-slide {
      float: left; }

    .swiper-container-vertical > .swiper-wrapper {
      -webkit-box-orient: vertical;
      -moz-box-orient: vertical;
      -ms-flex-direction: column;
      -webkit-flex-direction: column;
      flex-direction: column; }

    .swiper-wrapper {
      position: relative;
      width: 100%;
      height: 100%;
      z-index: 1;
      display: -webkit-box;
      display: -moz-box;
      display: -ms-flexbox;
      display: -webkit-flex;
      display: flex;
      -webkit-transition-property: -webkit-transform;
      -moz-transition-property: -moz-transform;
      -o-transition-property: -o-transform;
      -ms-transition-property: -ms-transform;
      transition-property: transform;
      -webkit-box-sizing: content-box;
      -moz-box-sizing: content-box;
      box-sizing: content-box; }

    .swiper-container-android .swiper-slide,
    .swiper-wrapper {
      -webkit-transform: translate3d(0px, 0, 0);
      -moz-transform: translate3d(0px, 0, 0);
      -o-transform: translate(0px, 0px);
      -ms-transform: translate3d(0px, 0, 0);
      transform: translate3d(0px, 0, 0); }

    .swiper-container-multirow > .swiper-wrapper {
      -webkit-box-lines: multiple;
      -moz-box-lines: multiple;
      -ms-flex-wrap: wrap;
      -webkit-flex-wrap: wrap;
      flex-wrap: wrap; }

    .swiper-container-free-mode > .swiper-wrapper {
      -webkit-transition-timing-function: ease-out;
      -moz-transition-timing-function: ease-out;
      -ms-transition-timing-function: ease-out;
      -o-transition-timing-function: ease-out;
      transition-timing-function: ease-out;
      margin: 0 auto; }

    .swiper-slide {
      display: block;
      -webkit-flex-shrink: 0;
      -ms-flex: 0 0 auto;
      flex-shrink: 0;
      width: 100%;
      height: 100%;
      position: relative; }

    /* Auto Height */
    .swiper-container-autoheight,
    .swiper-container-autoheight .swiper-slide {
      height: auto; }

    .swiper-container-autoheight .swiper-wrapper {
      -webkit-box-align: start;
      -ms-flex-align: start;
      -webkit-align-items: flex-start;
      align-items: flex-start;
      -webkit-transition-property: -webkit-transform, height;
      -moz-transition-property: -moz-transform;
      -o-transition-property: -o-transform;
      -ms-transition-property: -ms-transform;
      transition-property: transform, height; }

    /* a11y */
    .swiper-container .swiper-notification {
      position: absolute;
      left: 0;
      top: 0;
      pointer-events: none;
      opacity: 0;
      z-index: -1000; }

    /* IE10 Windows Phone 8 Fixes */
    .swiper-wp8-horizontal {
      -ms-touch-action: pan-y;
      touch-action: pan-y; }

    .swiper-wp8-vertical {
      -ms-touch-action: pan-x;
      touch-action: pan-x; }

    /* Arrows */
    .swiper-button-prev,
    .swiper-button-next {
      position: absolute;
      top: 50%;
      width: 27px;
      height: 44px;
      margin-top: -22px;
      z-index: 10;
      cursor: pointer;
      -moz-background-size: 27px 44px;
      -webkit-background-size: 27px 44px;
      background-size: 27px 44px;
      background-position: center;
      background-repeat: no-repeat; }

    .swiper-button-prev.swiper-button-disabled,
    .swiper-button-next.swiper-button-disabled {
      opacity: 0.35;
      cursor: auto;
      pointer-events: none; }

    .swiper-button-prev,
    .swiper-container-rtl .swiper-button-next {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23007aff'%2F%3E%3C%2Fsvg%3E");
      left: 10px;
      right: auto; }

    .swiper-button-prev.swiper-button-black,
    .swiper-container-rtl .swiper-button-next.swiper-button-black {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23000000'%2F%3E%3C%2Fsvg%3E"); }

    .swiper-button-prev.swiper-button-white,
    .swiper-container-rtl .swiper-button-next.swiper-button-white {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23ffffff'%2F%3E%3C%2Fsvg%3E"); }

    .swiper-button-next,
    .swiper-container-rtl .swiper-button-prev {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23007aff'%2F%3E%3C%2Fsvg%3E");
      right: 10px;
      left: auto; }

    .swiper-button-next.swiper-button-black,
    .swiper-container-rtl .swiper-button-prev.swiper-button-black {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23000000'%2F%3E%3C%2Fsvg%3E"); }

    .swiper-button-next.swiper-button-white,
    .swiper-container-rtl .swiper-button-prev.swiper-button-white {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23ffffff'%2F%3E%3C%2Fsvg%3E"); }

    /* Pagination Styles */
    .swiper-pagination {
      position: absolute;
      text-align: center;
      -webkit-transition: 300ms;
      -moz-transition: 300ms;
      -o-transition: 300ms;
      transition: 300ms;
      -webkit-transform: translate3d(0, 0, 0);
      -ms-transform: translate3d(0, 0, 0);
      -o-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      z-index: 10; }

    .swiper-pagination.swiper-pagination-hidden {
      opacity: 0; }

    .swiper-pagination-bullet {
      width: 8px;
      height: 8px;
      display: inline-block;
      border-radius: 100%;
      background: #000;
      opacity: 0.2; }

    button.swiper-pagination-bullet {
      border: none;
      margin: 0;
      padding: 0;
      box-shadow: none;
      -moz-appearance: none;
      -ms-appearance: none;
      -webkit-appearance: none;
      appearance: none; }

    .swiper-pagination-clickable .swiper-pagination-bullet {
      cursor: pointer; }

    .swiper-pagination-white .swiper-pagination-bullet {
      background: #fff; }

    .swiper-pagination-bullet-active {
      opacity: 1; }

    .swiper-pagination-white .swiper-pagination-bullet-active {
      background: #fff; }

    .swiper-pagination-black .swiper-pagination-bullet-active {
      background: #000; }

    .swiper-container-vertical > .swiper-pagination {
      right: 10px;
      top: 50%;
      -webkit-transform: translate3d(0px, -50%, 0);
      -moz-transform: translate3d(0px, -50%, 0);
      -o-transform: translate(0px, -50%);
      -ms-transform: translate3d(0px, -50%, 0);
      transform: translate3d(0px, -50%, 0); }

    .swiper-container-vertical > .swiper-pagination .swiper-pagination-bullet {
      margin: 5px 0;
      display: block; }

    .swiper-container-horizontal > .swiper-pagination {
      bottom: 10px;
      left: 0;
      width: 100%; }

    .swiper-container-horizontal > .swiper-pagination .swiper-pagination-bullet {
      margin: 0 5px; }

    /* 3D Container */
    .swiper-container-3d {
      -webkit-perspective: 1200px;
      -moz-perspective: 1200px;
      -o-perspective: 1200px;
      perspective: 1200px; }

    .swiper-container-3d .swiper-wrapper,
    .swiper-container-3d .swiper-slide,
    .swiper-container-3d .swiper-slide-shadow-left,
    .swiper-container-3d .swiper-slide-shadow-right,
    .swiper-container-3d .swiper-slide-shadow-top,
    .swiper-container-3d .swiper-slide-shadow-bottom,
    .swiper-container-3d .swiper-cube-shadow {
      -webkit-transform-style: preserve-3d;
      -moz-transform-style: preserve-3d;
      -ms-transform-style: preserve-3d;
      transform-style: preserve-3d; }

    .swiper-container-3d .swiper-slide-shadow-left,
    .swiper-container-3d .swiper-slide-shadow-right,
    .swiper-container-3d .swiper-slide-shadow-top,
    .swiper-container-3d .swiper-slide-shadow-bottom {
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      pointer-events: none;
      z-index: 10; }

    .swiper-container-3d .swiper-slide-shadow-left {
      background-image: -webkit-gradient(linear, left top, right top, from(rgba(0, 0, 0, 0.5)), to(transparent));
      /* Safari 4+, Chrome */
      background-image: -webkit-linear-gradient(right, rgba(0, 0, 0, 0.5), transparent);
      /* Chrome 10+, Safari 5.1+, iOS 5+ */
      background-image: -moz-linear-gradient(right, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 3.6-15 */
      background-image: -o-linear-gradient(right, rgba(0, 0, 0, 0.5), transparent);
      /* Opera 11.10-12.00 */
      background-image: linear-gradient(to left, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 16+, IE10, Opera 12.50+ */ }

    .swiper-container-3d .swiper-slide-shadow-right {
      background-image: -webkit-gradient(linear, right top, left top, from(rgba(0, 0, 0, 0.5)), to(transparent));
      /* Safari 4+, Chrome */
      background-image: -webkit-linear-gradient(left, rgba(0, 0, 0, 0.5), transparent);
      /* Chrome 10+, Safari 5.1+, iOS 5+ */
      background-image: -moz-linear-gradient(left, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 3.6-15 */
      background-image: -o-linear-gradient(left, rgba(0, 0, 0, 0.5), transparent);
      /* Opera 11.10-12.00 */
      background-image: linear-gradient(to right, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 16+, IE10, Opera 12.50+ */ }

    .swiper-container-3d .swiper-slide-shadow-top {
      background-image: -webkit-gradient(linear, left top, left bottom, from(rgba(0, 0, 0, 0.5)), to(transparent));
      /* Safari 4+, Chrome */
      background-image: -webkit-linear-gradient(bottom, rgba(0, 0, 0, 0.5), transparent);
      /* Chrome 10+, Safari 5.1+, iOS 5+ */
      background-image: -moz-linear-gradient(bottom, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 3.6-15 */
      background-image: -o-linear-gradient(bottom, rgba(0, 0, 0, 0.5), transparent);
      /* Opera 11.10-12.00 */
      background-image: linear-gradient(to top, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 16+, IE10, Opera 12.50+ */ }

    .swiper-container-3d .swiper-slide-shadow-bottom {
      background-image: -webkit-gradient(linear, left bottom, left top, from(rgba(0, 0, 0, 0.5)), to(transparent));
      /* Safari 4+, Chrome */
      background-image: -webkit-linear-gradient(top, rgba(0, 0, 0, 0.5), transparent);
      /* Chrome 10+, Safari 5.1+, iOS 5+ */
      background-image: -moz-linear-gradient(top, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 3.6-15 */
      background-image: -o-linear-gradient(top, rgba(0, 0, 0, 0.5), transparent);
      /* Opera 11.10-12.00 */
      background-image: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), transparent);
      /* Firefox 16+, IE10, Opera 12.50+ */ }

    /* Coverflow */
    .swiper-container-coverflow .swiper-wrapper {
      /* Windows 8 IE 10 fix */
      -ms-perspective: 1200px; }

    /* Fade */
    .swiper-container-fade.swiper-container-free-mode .swiper-slide {
      -webkit-transition-timing-function: ease-out;
      -moz-transition-timing-function: ease-out;
      -ms-transition-timing-function: ease-out;
      -o-transition-timing-function: ease-out;
      transition-timing-function: ease-out; }

    .swiper-container-fade .swiper-slide {
      pointer-events: none; }

    .swiper-container-fade .swiper-slide .swiper-slide {
      pointer-events: none; }

    .swiper-container-fade .swiper-slide-active,
    .swiper-container-fade .swiper-slide-active .swiper-slide-active {
      pointer-events: auto; }

    /* Cube */
    .swiper-container-cube {
      overflow: visible; }

    .swiper-container-cube .swiper-slide {
      pointer-events: none;
      visibility: hidden;
      -webkit-transform-origin: 0 0;
      -moz-transform-origin: 0 0;
      -ms-transform-origin: 0 0;
      transform-origin: 0 0;
      -webkit-backface-visibility: hidden;
      -moz-backface-visibility: hidden;
      -ms-backface-visibility: hidden;
      backface-visibility: hidden;
      width: 100%;
      height: 100%;
      z-index: 1; }

    .swiper-container-cube.swiper-container-rtl .swiper-slide {
      -webkit-transform-origin: 100% 0;
      -moz-transform-origin: 100% 0;
      -ms-transform-origin: 100% 0;
      transform-origin: 100% 0; }

    .swiper-container-cube .swiper-slide-active,
    .swiper-container-cube .swiper-slide-next,
    .swiper-container-cube .swiper-slide-prev,
    .swiper-container-cube .swiper-slide-next + .swiper-slide {
      pointer-events: auto;
      visibility: visible; }

    .swiper-container-cube .swiper-slide-shadow-top,
    .swiper-container-cube .swiper-slide-shadow-bottom,
    .swiper-container-cube .swiper-slide-shadow-left,
    .swiper-container-cube .swiper-slide-shadow-right {
      z-index: 0;
      -webkit-backface-visibility: hidden;
      -moz-backface-visibility: hidden;
      -ms-backface-visibility: hidden;
      backface-visibility: hidden; }

    .swiper-container-cube .swiper-cube-shadow {
      position: absolute;
      left: 0;
      bottom: 0px;
      width: 100%;
      height: 100%;
      background: #000;
      opacity: 0.6;
      -webkit-filter: blur(50px);
      filter: blur(50px);
      z-index: 0; }

    /* Scrollbar */
    .swiper-scrollbar {
      border-radius: 10px;
      position: relative;
      -ms-touch-action: none;
      background: rgba(0, 0, 0, 0.1); }

    .swiper-container-horizontal > .swiper-scrollbar {
      position: absolute;
      left: 1%;
      bottom: 3px;
      z-index: 50;
      height: 5px;
      width: 98%; }

    .swiper-container-vertical > .swiper-scrollbar {
      position: absolute;
      right: 3px;
      top: 1%;
      z-index: 50;
      width: 5px;
      height: 98%; }

    .swiper-scrollbar-drag {
      height: 100%;
      width: 100%;
      position: relative;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 10px;
      left: 0;
      top: 0; }

    .swiper-scrollbar-cursor-drag {
      cursor: move; }

    /* Preloader */
    .swiper-lazy-preloader {
      width: 42px;
      height: 42px;
      position: absolute;
      left: 50%;
      top: 50%;
      margin-left: -21px;
      margin-top: -21px;
      z-index: 10;
      -webkit-transform-origin: 50%;
      -moz-transform-origin: 50%;
      transform-origin: 50%;
      -webkit-animation: swiper-preloader-spin 1s steps(12, end) infinite;
      -moz-animation: swiper-preloader-spin 1s steps(12, end) infinite;
      animation: swiper-preloader-spin 1s steps(12, end) infinite; }

    .swiper-lazy-preloader:after {
      display: block;
      content: "";
      width: 100%;
      height: 100%;
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20viewBox%3D'0%200%20120%20120'%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20xmlns%3Axlink%3D'http%3A%2F%2Fwww.w3.org%2F1999%2Fxlink'%3E%3Cdefs%3E%3Cline%20id%3D'l'%20x1%3D'60'%20x2%3D'60'%20y1%3D'7'%20y2%3D'27'%20stroke%3D'%236c6c6c'%20stroke-width%3D'11'%20stroke-linecap%3D'round'%2F%3E%3C%2Fdefs%3E%3Cg%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(30%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(60%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(90%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(120%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(150%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.37'%20transform%3D'rotate(180%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.46'%20transform%3D'rotate(210%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.56'%20transform%3D'rotate(240%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.66'%20transform%3D'rotate(270%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.75'%20transform%3D'rotate(300%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.85'%20transform%3D'rotate(330%2060%2C60)'%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E");
      background-position: 50%;
      -webkit-background-size: 100%;
      background-size: 100%;
      background-repeat: no-repeat; }

    .swiper-lazy-preloader-white:after {
      background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20viewBox%3D'0%200%20120%20120'%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20xmlns%3Axlink%3D'http%3A%2F%2Fwww.w3.org%2F1999%2Fxlink'%3E%3Cdefs%3E%3Cline%20id%3D'l'%20x1%3D'60'%20x2%3D'60'%20y1%3D'7'%20y2%3D'27'%20stroke%3D'%23fff'%20stroke-width%3D'11'%20stroke-linecap%3D'round'%2F%3E%3C%2Fdefs%3E%3Cg%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(30%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(60%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(90%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(120%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(150%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.37'%20transform%3D'rotate(180%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.46'%20transform%3D'rotate(210%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.56'%20transform%3D'rotate(240%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.66'%20transform%3D'rotate(270%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.75'%20transform%3D'rotate(300%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.85'%20transform%3D'rotate(330%2060%2C60)'%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E"); }

    @-webkit-keyframes swiper-preloader-spin {
      100% {
        -webkit-transform: rotate(360deg); } }

    @keyframes swiper-preloader-spin {
      100% {
        transform: rotate(360deg); } }

    ion-slides {
      width: 100%;
      height: 100%;
      display: block; }

    .slide-zoom {
      display: block;
      width: 100%;
      text-align: center; }

    .swiper-container {
      width: 100%;
      height: 100%;
      padding: 0;
      overflow: hidden; }

    .swiper-wrapper {
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      padding: 0; }

    .swiper-slide {
      width: 100%;
      height: 100%;
      box-sizing: border-box;
      /* Center slide text vertically */ }
      .swiper-slide img {
        width: auto;
        height: auto;
        max-width: 100%;
        max-height: 100%; }

    .scroll-refresher {
      position: absolute;
      top: -60px;
      right: 0;
      left: 0;
      overflow: hidden;
      margin: auto;
      height: 60px; }
      .scroll-refresher .ionic-refresher-content {
        position: absolute;
        bottom: 15px;
        left: 0;
        width: 100%;
        color: #666666;
        text-align: center;
        font-size: 30px; }
        .scroll-refresher .ionic-refresher-content .text-refreshing,
        .scroll-refresher .ionic-refresher-content .text-pulling {
          font-size: 16px;
          line-height: 16px; }
        .scroll-refresher .ionic-refresher-content.ionic-refresher-with-text {
          bottom: 10px; }
      .scroll-refresher .icon-refreshing,
      .scroll-refresher .icon-pulling {
        width: 100%;
        -webkit-backface-visibility: hidden;
        backface-visibility: hidden;
        -webkit-transform-style: preserve-3d;
        transform-style: preserve-3d; }
      .scroll-refresher .icon-pulling {
        -webkit-animation-name: refresh-spin-back;
        animation-name: refresh-spin-back;
        -webkit-animation-duration: 200ms;
        animation-duration: 200ms;
        -webkit-animation-timing-function: linear;
        animation-timing-function: linear;
        -webkit-animation-fill-mode: none;
        animation-fill-mode: none;
        -webkit-transform: translate3d(0, 0, 0) rotate(0deg);
        transform: translate3d(0, 0, 0) rotate(0deg); }
      .scroll-refresher .icon-refreshing,
      .scroll-refresher .text-refreshing {
        display: none; }
      .scroll-refresher .icon-refreshing {
        -webkit-animation-duration: 1.5s;
        animation-duration: 1.5s; }
      .scroll-refresher.active .icon-pulling:not(.pulling-rotation-disabled) {
        -webkit-animation-name: refresh-spin;
        animation-name: refresh-spin;
        -webkit-transform: translate3d(0, 0, 0) rotate(-180deg);
        transform: translate3d(0, 0, 0) rotate(-180deg); }
      .scroll-refresher.active.refreshing {
        -webkit-transition: -webkit-transform 0.2s;
        transition: -webkit-transform 0.2s;
        -webkit-transition: transform 0.2s;
        transition: transform 0.2s;
        -webkit-transform: scale(1, 1);
        transform: scale(1, 1); }
        .scroll-refresher.active.refreshing .icon-pulling,
        .scroll-refresher.active.refreshing .text-pulling {
          display: none; }
        .scroll-refresher.active.refreshing .icon-refreshing,
        .scroll-refresher.active.refreshing .text-refreshing {
          display: block; }
        .scroll-refresher.active.refreshing.refreshing-tail {
          -webkit-transform: scale(0, 0);
          transform: scale(0, 0); }

    .overflow-scroll > .scroll {
      -webkit-overflow-scrolling: touch;
      width: 100%; }
      .overflow-scroll > .scroll.overscroll {
        position: fixed;
        right: 0;
        left: 0; }

    .overflow-scroll.padding > .scroll.overscroll, .overflow-scroll.item.large-button-bar > .scroll.overscroll {
      padding: 10px; }

    @-webkit-keyframes refresh-spin {
      0% {
        -webkit-transform: translate3d(0, 0, 0) rotate(0); }
      100% {
        -webkit-transform: translate3d(0, 0, 0) rotate(180deg); } }

    @keyframes refresh-spin {
      0% {
        transform: translate3d(0, 0, 0) rotate(0); }
      100% {
        transform: translate3d(0, 0, 0) rotate(180deg); } }

    @-webkit-keyframes refresh-spin-back {
      0% {
        -webkit-transform: translate3d(0, 0, 0) rotate(180deg); }
      100% {
        -webkit-transform: translate3d(0, 0, 0) rotate(0); } }

    @keyframes refresh-spin-back {
      0% {
        transform: translate3d(0, 0, 0) rotate(180deg); }
      100% {
        transform: translate3d(0, 0, 0) rotate(0); } }

    /**
     * Spinners
     * --------------------------------------------------
     */
    .spinner {
      stroke: #444;
      fill: #444; }
      .spinner svg {
        width: 28px;
        height: 28px; }
      .spinner.spinner-light {
        stroke: #fff;
        fill: #fff; }
      .spinner.spinner-stable {
        stroke: #f8f8f8;
        fill: #f8f8f8; }
      .spinner.spinner-positive {
        stroke: #387ef5;
        fill: #387ef5; }
      .spinner.spinner-calm {
        stroke: #11c1f3;
        fill: #11c1f3; }
      .spinner.spinner-balanced {
        stroke: #33cd5f;
        fill: #33cd5f; }
      .spinner.spinner-assertive {
        stroke: #ef473a;
        fill: #ef473a; }
      .spinner.spinner-energized {
        stroke: #ffc900;
        fill: #ffc900; }
      .spinner.spinner-royal {
        stroke: #886aea;
        fill: #886aea; }
      .spinner.spinner-dark {
        stroke: #444;
        fill: #444; }

    .spinner-android {
      stroke: #4b8bf4; }

    .spinner-ios,
    .spinner-ios-small {
      stroke: #69717d; }

    .spinner-spiral .stop1 {
      stop-color: #fff;
      stop-opacity: 0; }

    .spinner-spiral.spinner-light .stop1 {
      stop-color: #444; }

    .spinner-spiral.spinner-light .stop2 {
      stop-color: #fff; }

    .spinner-spiral.spinner-stable .stop2 {
      stop-color: #f8f8f8; }

    .spinner-spiral.spinner-positive .stop2 {
      stop-color: #387ef5; }

    .spinner-spiral.spinner-calm .stop2 {
      stop-color: #11c1f3; }

    .spinner-spiral.spinner-balanced .stop2 {
      stop-color: #33cd5f; }

    .spinner-spiral.spinner-assertive .stop2 {
      stop-color: #ef473a; }

    .spinner-spiral.spinner-energized .stop2 {
      stop-color: #ffc900; }

    .spinner-spiral.spinner-royal .stop2 {
      stop-color: #886aea; }

    .spinner-spiral.spinner-dark .stop2 {
      stop-color: #444; }

    /**
     * Forms
     * --------------------------------------------------
     */
    form {
      margin: 0 0 1.42857; }

    legend {
      display: block;
      margin-bottom: 1.42857;
      padding: 0;
      width: 100%;
      border: 1px solid #ddd;
      color: #444;
      font-size: 21px;
      line-height: 2.85714; }
      legend small {
        color: #f8f8f8;
        font-size: 1.07143; }

    label,
    input,
    button,
    select,
    textarea {
      font-weight: normal;
      font-size: 14px;
      line-height: 1.42857; }

    input,
    button,
    select,
    textarea {
      font-family: "-apple-system", "Helvetica Neue", "Roboto", "Segoe UI", sans-serif; }

    .item-input {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      position: relative;
      overflow: hidden;
      padding: 6px 0 5px 16px; }
      .item-input input {
        -webkit-border-radius: 0;
        border-radius: 0;
        -webkit-box-flex: 1;
        -webkit-flex: 1 220px;
        -moz-box-flex: 1;
        -moz-flex: 1 220px;
        -ms-flex: 1 220px;
        flex: 1 220px;
        -webkit-appearance: none;
        -moz-appearance: none;
        appearance: none;
        margin: 0;
        padding-right: 24px;
        background-color: transparent; }
      .item-input .button .icon, .item-input .button .icon-help, .item-input .button .icon-alert, .item-input .button #menu .footer .icon-help, #menu .footer .item-input .button .icon-help {
        -webkit-box-flex: 0;
        -webkit-flex: 0 0 24px;
        -moz-box-flex: 0;
        -moz-flex: 0 0 24px;
        -ms-flex: 0 0 24px;
        flex: 0 0 24px;
        position: static;
        display: inline-block;
        height: auto;
        text-align: center;
        font-size: 16px; }
      .item-input .button-bar {
        -webkit-border-radius: 0;
        border-radius: 0;
        -webkit-box-flex: 1;
        -webkit-flex: 1 0 220px;
        -moz-box-flex: 1;
        -moz-flex: 1 0 220px;
        -ms-flex: 1 0 220px;
        flex: 1 0 220px;
        -webkit-appearance: none;
        -moz-appearance: none;
        appearance: none; }
      .item-input .icon, .item-input .icon-help, .item-input .icon-alert, .item-input #menu .footer .icon-help, #menu .footer .item-input .icon-help {
        min-width: 14px; }

    .platform-windowsphone .item-input input {
      flex-shrink: 1; }

    .item-input-inset {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      position: relative;
      overflow: hidden;
      padding: 10.66667px; }

    .item-input-wrapper {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-flex: 1;
      -webkit-flex: 1 0;
      -moz-box-flex: 1;
      -moz-flex: 1 0;
      -ms-flex: 1 0;
      flex: 1 0;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
      -webkit-border-radius: 4px;
      border-radius: 4px;
      padding-right: 8px;
      padding-left: 8px;
      background: #eee; }

    .item-input-inset .item-input-wrapper input {
      padding-left: 4px;
      height: 29px;
      background: transparent;
      line-height: 18px; }

    .item-input-wrapper ~ .button {
      margin-left: 10.66667px; }

    .input-label {
      display: table;
      padding: 7px 10px 7px 0px;
      max-width: 200px;
      width: 35%;
      color: #444;
      font-size: 16px; }

    .placeholder-icon {
      color: #aaa; }
      .placeholder-icon:first-child {
        padding-right: 6px; }
      .placeholder-icon:last-child {
        padding-left: 6px; }

    .item-stacked-label {
      display: block;
      background-color: transparent;
      box-shadow: none; }
      .item-stacked-label .input-label, .item-stacked-label .icon, .item-stacked-label .icon-help, .item-stacked-label .icon-alert, .item-stacked-label #menu .footer .icon-help, #menu .footer .item-stacked-label .icon-help {
        display: inline-block;
        padding: 4px 0 0 0px;
        vertical-align: middle; }

    .item-stacked-label input,
    .item-stacked-label textarea {
      -webkit-border-radius: 2px;
      border-radius: 2px;
      padding: 4px 8px 3px 0;
      border: none;
      background-color: #fff; }

    .item-stacked-label input {
      overflow: hidden;
      height: 46px; }

    .item-select.item-stacked-label select {
      position: relative;
      padding: 0px;
      max-width: 90%;
      direction: ltr;
      white-space: pre-wrap;
      margin: -3px; }

    .item-floating-label {
      display: block;
      background-color: transparent;
      box-shadow: none; }
      .item-floating-label .input-label {
        position: relative;
        padding: 5px 0 0 0;
        opacity: 0;
        top: 10px;
        -webkit-transition: opacity 0.15s ease-in, top 0.2s linear;
        transition: opacity 0.15s ease-in, top 0.2s linear; }
        .item-floating-label .input-label.has-input {
          opacity: 1;
          top: 0;
          -webkit-transition: opacity 0.15s ease-in, top 0.2s linear;
          transition: opacity 0.15s ease-in, top 0.2s linear; }

    textarea,
    input[type="text"],
    input[type="password"],
    input[type="datetime"],
    input[type="datetime-local"],
    input[type="date"],
    input[type="month"],
    input[type="time"],
    input[type="week"],
    input[type="number"],
    input[type="email"],
    input[type="url"],
    input[type="search"],
    input[type="tel"],
    input[type="color"] {
      display: block;
      padding-top: 2px;
      padding-left: 0;
      height: 34px;
      color: #111;
      vertical-align: middle;
      font-size: 14px;
      line-height: 16px; }

    .platform-ios input[type="datetime-local"],
    .platform-ios input[type="date"],
    .platform-ios input[type="month"],
    .platform-ios input[type="time"],
    .platform-ios input[type="week"],
    .platform-android input[type="datetime-local"],
    .platform-android input[type="date"],
    .platform-android input[type="month"],
    .platform-android input[type="time"],
    .platform-android input[type="week"] {
      padding-top: 8px; }

    .item-input input,
    .item-input textarea {
      width: 100%; }

    textarea {
      padding-left: 0; }
      textarea::-moz-placeholder {
        color: #aaaaaa; }
      textarea:-ms-input-placeholder {
        color: #aaaaaa; }
      textarea::-webkit-input-placeholder {
        color: #aaaaaa;
        text-indent: -3px; }

    textarea {
      height: auto; }

    textarea,
    input[type="text"],
    input[type="password"],
    input[type="datetime"],
    input[type="datetime-local"],
    input[type="date"],
    input[type="month"],
    input[type="time"],
    input[type="week"],
    input[type="number"],
    input[type="email"],
    input[type="url"],
    input[type="search"],
    input[type="tel"],
    input[type="color"] {
      border: 0; }

    input[type="radio"],
    input[type="checkbox"] {
      margin: 0;
      line-height: normal; }

    .item-input input[type="file"],
    .item-input input[type="image"],
    .item-input input[type="submit"],
    .item-input input[type="reset"],
    .item-input input[type="button"],
    .item-input input[type="radio"],
    .item-input input[type="checkbox"] {
      width: auto; }

    input[type="file"] {
      line-height: 34px; }

    .previous-input-focus,
    .cloned-text-input + input,
    .cloned-text-input + textarea {
      position: absolute !important;
      left: -9999px;
      width: 200px; }

    input::-moz-placeholder,
    textarea::-moz-placeholder {
      color: #aaaaaa; }

    input:-ms-input-placeholder,
    textarea:-ms-input-placeholder {
      color: #aaaaaa; }

    input::-webkit-input-placeholder,
    textarea::-webkit-input-placeholder {
      color: #aaaaaa;
      text-indent: 0; }

    input[disabled],
    select[disabled],
    textarea[disabled],
    input[readonly]:not(.cloned-text-input),
    textarea[readonly]:not(.cloned-text-input),
    select[readonly] {
      background-color: #f8f8f8;
      cursor: not-allowed; }

    input[type="radio"][disabled],
    input[type="checkbox"][disabled],
    input[type="radio"][readonly],
    input[type="checkbox"][readonly] {
      background-color: transparent; }

    /**
     * Checkbox
     * --------------------------------------------------
     */
    .checkbox {
      position: relative;
      display: inline-block;
      padding: 7px 7px;
      cursor: pointer; }
      .checkbox input:before,
      .checkbox .checkbox-icon:before {
        border-color: #ddd; }
      .checkbox input:checked:before,
      .checkbox input:checked + .checkbox-icon:before {
        background: #387ef5;
        border-color: #387ef5; }

    .checkbox-light input:before,
    .checkbox-light .checkbox-icon:before {
      border-color: #ddd; }

    .checkbox-light input:checked:before,
    .checkbox-light input:checked + .checkbox-icon:before {
      background: #ddd;
      border-color: #ddd; }

    .checkbox-stable input:before,
    .checkbox-stable .checkbox-icon:before {
      border-color: #b2b2b2; }

    .checkbox-stable input:checked:before,
    .checkbox-stable input:checked + .checkbox-icon:before {
      background: #b2b2b2;
      border-color: #b2b2b2; }

    .checkbox-positive input:before,
    .checkbox-positive .checkbox-icon:before {
      border-color: #387ef5; }

    .checkbox-positive input:checked:before,
    .checkbox-positive input:checked + .checkbox-icon:before {
      background: #387ef5;
      border-color: #387ef5; }

    .checkbox-calm input:before,
    .checkbox-calm .checkbox-icon:before {
      border-color: #11c1f3; }

    .checkbox-calm input:checked:before,
    .checkbox-calm input:checked + .checkbox-icon:before {
      background: #11c1f3;
      border-color: #11c1f3; }

    .checkbox-assertive input:before,
    .checkbox-assertive .checkbox-icon:before {
      border-color: #ef473a; }

    .checkbox-assertive input:checked:before,
    .checkbox-assertive input:checked + .checkbox-icon:before {
      background: #ef473a;
      border-color: #ef473a; }

    .checkbox-balanced input:before,
    .checkbox-balanced .checkbox-icon:before {
      border-color: #33cd5f; }

    .checkbox-balanced input:checked:before,
    .checkbox-balanced input:checked + .checkbox-icon:before {
      background: #33cd5f;
      border-color: #33cd5f; }

    .checkbox-energized input:before,
    .checkbox-energized .checkbox-icon:before {
      border-color: #ffc900; }

    .checkbox-energized input:checked:before,
    .checkbox-energized input:checked + .checkbox-icon:before {
      background: #ffc900;
      border-color: #ffc900; }

    .checkbox-royal input:before,
    .checkbox-royal .checkbox-icon:before {
      border-color: #886aea; }

    .checkbox-royal input:checked:before,
    .checkbox-royal input:checked + .checkbox-icon:before {
      background: #886aea;
      border-color: #886aea; }

    .checkbox-dark input:before,
    .checkbox-dark .checkbox-icon:before {
      border-color: #444; }

    .checkbox-dark input:checked:before,
    .checkbox-dark input:checked + .checkbox-icon:before {
      background: #444;
      border-color: #444; }

    .checkbox input:disabled:before,
    .checkbox input:disabled + .checkbox-icon:before {
      border-color: #ddd; }

    .checkbox input:disabled:checked:before,
    .checkbox input:disabled:checked + .checkbox-icon:before {
      background: #ddd; }

    .checkbox.checkbox-input-hidden input {
      display: none !important; }

    .checkbox input,
    .checkbox-icon {
      position: relative;
      width: 28px;
      height: 28px;
      display: block;
      border: 0;
      background: transparent;
      cursor: pointer;
      -webkit-appearance: none; }
      .checkbox input:before,
      .checkbox-icon:before {
        display: table;
        width: 100%;
        height: 100%;
        border-width: 1px;
        border-style: solid;
        border-radius: 28px;
        background: #fff;
        content: ' ';
        -webkit-transition: background-color 20ms ease-in-out;
        transition: background-color 20ms ease-in-out; }

    .checkbox input:checked:before,
    input:checked + .checkbox-icon:before {
      border-width: 2px; }

    .checkbox input:after,
    .checkbox-icon:after {
      -webkit-transition: opacity 0.05s ease-in-out;
      transition: opacity 0.05s ease-in-out;
      -webkit-transform: rotate(-45deg);
      transform: rotate(-45deg);
      position: absolute;
      top: 33%;
      left: 25%;
      display: table;
      width: 14px;
      height: 6px;
      border: 1px solid #fff;
      border-top: 0;
      border-right: 0;
      content: ' ';
      opacity: 0; }

    .platform-android .checkbox-platform input:before,
    .platform-android .checkbox-platform .checkbox-icon:before,
    .checkbox-square input:before,
    .checkbox-square .checkbox-icon:before {
      border-radius: 2px;
      width: 72%;
      height: 72%;
      margin-top: 14%;
      margin-left: 14%;
      border-width: 2px; }

    .platform-android .checkbox-platform input:after,
    .platform-android .checkbox-platform .checkbox-icon:after,
    .checkbox-square input:after,
    .checkbox-square .checkbox-icon:after {
      border-width: 2px;
      top: 19%;
      left: 25%;
      width: 13px;
      height: 7px; }

    .platform-android .item-checkbox-right .checkbox-square .checkbox-icon::after {
      top: 31%; }

    .grade-c .checkbox input:after,
    .grade-c .checkbox-icon:after {
      -webkit-transform: rotate(0);
      transform: rotate(0);
      top: 3px;
      left: 4px;
      border: none;
      color: #fff;
      content: '\2713';
      font-weight: bold;
      font-size: 20px; }

    .checkbox input:checked:after,
    input:checked + .checkbox-icon:after {
      opacity: 1; }

    .item-checkbox {
      padding-left: 60px; }
      .item-checkbox.active {
        box-shadow: none; }

    .item-checkbox .checkbox {
      position: absolute;
      top: 50%;
      right: 8px;
      left: 8px;
      z-index: 3;
      margin-top: -21px; }

    .item-checkbox.item-checkbox-right {
      padding-right: 60px;
      padding-left: 16px; }

    .item-checkbox-right .checkbox input,
    .item-checkbox-right .checkbox-icon {
      float: right; }

    /**
     * Toggle
     * --------------------------------------------------
     */
    .item-toggle {
      pointer-events: none; }

    .toggle {
      position: relative;
      display: inline-block;
      pointer-events: auto;
      margin: -5px;
      padding: 5px; }
      .toggle input:checked + .track {
        border-color: #4cd964;
        background-color: #4cd964; }
      .toggle.dragging .handle {
        background-color: #f2f2f2 !important; }

    .toggle.toggle-light input:checked + .track {
      border-color: #ddd;
      background-color: #ddd; }

    .toggle.toggle-stable input:checked + .track {
      border-color: #b2b2b2;
      background-color: #b2b2b2; }

    .toggle.toggle-positive input:checked + .track {
      border-color: #387ef5;
      background-color: #387ef5; }

    .toggle.toggle-calm input:checked + .track {
      border-color: #11c1f3;
      background-color: #11c1f3; }

    .toggle.toggle-assertive input:checked + .track {
      border-color: #ef473a;
      background-color: #ef473a; }

    .toggle.toggle-balanced input:checked + .track {
      border-color: #33cd5f;
      background-color: #33cd5f; }

    .toggle.toggle-energized input:checked + .track {
      border-color: #ffc900;
      background-color: #ffc900; }

    .toggle.toggle-royal input:checked + .track {
      border-color: #886aea;
      background-color: #886aea; }

    .toggle.toggle-dark input:checked + .track {
      border-color: #444;
      background-color: #444; }

    .toggle input {
      display: none; }

    /* the track appearance when the toggle is "off" */
    .toggle .track {
      -webkit-transition-timing-function: ease-in-out;
      transition-timing-function: ease-in-out;
      -webkit-transition-duration: 0.3s;
      transition-duration: 0.3s;
      -webkit-transition-property: background-color, border;
      transition-property: background-color, border;
      display: inline-block;
      box-sizing: border-box;
      width: 51px;
      height: 31px;
      border: solid 2px #e6e6e6;
      border-radius: 20px;
      background-color: #fff;
      content: ' ';
      cursor: pointer;
      pointer-events: none; }

    /* Fix to avoid background color bleeding */
    /* (occurred on (at least) Android 4.2, Asus MeMO Pad HD7 ME173X) */
    .platform-android4_2 .toggle .track {
      -webkit-background-clip: padding-box; }

    /* the handle (circle) thats inside the toggle's track area */
    /* also the handle's appearance when it is "off" */
    .toggle .handle {
      -webkit-transition: 0.3s cubic-bezier(0, 1.1, 1, 1.1);
      transition: 0.3s cubic-bezier(0, 1.1, 1, 1.1);
      -webkit-transition-property: background-color, transform;
      transition-property: background-color, transform;
      position: absolute;
      display: block;
      width: 27px;
      height: 27px;
      border-radius: 27px;
      background-color: #fff;
      top: 7px;
      left: 7px;
      box-shadow: 0 2px 7px rgba(0, 0, 0, 0.35), 0 1px 1px rgba(0, 0, 0, 0.15); }
      .toggle .handle:before {
        position: absolute;
        top: -4px;
        left: -21.5px;
        padding: 18.5px 34px;
        content: " "; }

    .toggle input:checked + .track .handle {
      -webkit-transform: translate3d(20px, 0, 0);
      transform: translate3d(20px, 0, 0);
      background-color: #fff; }

    .item-toggle.active {
      box-shadow: none; }

    .item-toggle,
    .item-toggle.item-complex .item-content {
      padding-right: 99px; }

    .item-toggle.item-complex {
      padding-right: 0; }

    .item-toggle .toggle {
      position: absolute;
      top: 10px;
      right: 16px;
      z-index: 3; }

    .toggle input:disabled + .track {
      opacity: .6; }

    .toggle-small .track {
      border: 0;
      width: 34px;
      height: 15px;
      background: #9e9e9e; }

    .toggle-small input:checked + .track {
      background: rgba(0, 150, 137, 0.5); }

    .toggle-small .handle {
      top: 2px;
      left: 4px;
      width: 21px;
      height: 21px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.25); }

    .toggle-small input:checked + .track .handle {
      -webkit-transform: translate3d(16px, 0, 0);
      transform: translate3d(16px, 0, 0);
      background: #009689; }

    .toggle-small.item-toggle .toggle {
      top: 19px; }

    .toggle-small .toggle-light input:checked + .track {
      background-color: rgba(221, 221, 221, 0.5); }

    .toggle-small .toggle-light input:checked + .track .handle {
      background-color: #ddd; }

    .toggle-small .toggle-stable input:checked + .track {
      background-color: rgba(178, 178, 178, 0.5); }

    .toggle-small .toggle-stable input:checked + .track .handle {
      background-color: #b2b2b2; }

    .toggle-small .toggle-positive input:checked + .track {
      background-color: rgba(56, 126, 245, 0.5); }

    .toggle-small .toggle-positive input:checked + .track .handle {
      background-color: #387ef5; }

    .toggle-small .toggle-calm input:checked + .track {
      background-color: rgba(17, 193, 243, 0.5); }

    .toggle-small .toggle-calm input:checked + .track .handle {
      background-color: #11c1f3; }

    .toggle-small .toggle-assertive input:checked + .track {
      background-color: rgba(239, 71, 58, 0.5); }

    .toggle-small .toggle-assertive input:checked + .track .handle {
      background-color: #ef473a; }

    .toggle-small .toggle-balanced input:checked + .track {
      background-color: rgba(51, 205, 95, 0.5); }

    .toggle-small .toggle-balanced input:checked + .track .handle {
      background-color: #33cd5f; }

    .toggle-small .toggle-energized input:checked + .track {
      background-color: rgba(255, 201, 0, 0.5); }

    .toggle-small .toggle-energized input:checked + .track .handle {
      background-color: #ffc900; }

    .toggle-small .toggle-royal input:checked + .track {
      background-color: rgba(136, 106, 234, 0.5); }

    .toggle-small .toggle-royal input:checked + .track .handle {
      background-color: #886aea; }

    .toggle-small .toggle-dark input:checked + .track {
      background-color: rgba(68, 68, 68, 0.5); }

    .toggle-small .toggle-dark input:checked + .track .handle {
      background-color: #444; }

    /**
     * Radio Button Inputs
     * --------------------------------------------------
     */
    .item-radio {
      padding: 0; }
      .item-radio:hover {
        cursor: pointer; }

    .item-radio .item-content {
      /* give some room to the right for the checkmark icon */
      padding-right: 64px; }

    .item-radio .radio-icon {
      /* checkmark icon will be hidden by default */
      position: absolute;
      top: 0;
      right: 0;
      z-index: 3;
      visibility: hidden;
      padding: 14px;
      height: 100%;
      font-size: 24px; }

    .item-radio input {
      /* hide any radio button inputs elements (the ugly circles) */
      position: absolute;
      left: -9999px; }
      .item-radio input:checked + .radio-content .item-content {
        /* style the item content when its checked */
        background: #f7f7f7; }
      .item-radio input:checked + .radio-content .radio-icon {
        /* show the checkmark icon when its checked */
        visibility: visible; }

    /**
     * Buttons
     * --------------------------------------------------
     */
    .button {
      border-color: transparent;
      background-color: #f8f8f8;
      color: #444;
      position: relative;
      display: inline-block;
      margin: 0;
      padding: 0 12px;
      min-width: 52px;
      min-height: 47px;
      border-width: 1px;
      border-style: solid;
      border-radius: 4px;
      vertical-align: top;
      text-align: center;
      text-overflow: ellipsis;
      font-size: 16px;
      line-height: 42px;
      cursor: pointer; }
      .button:hover {
        color: #444;
        text-decoration: none; }
      .button.active, .button.activated {
        border-color: #a2a2a2;
        background-color: #e5e5e5; }
      .button:after {
        position: absolute;
        top: -6px;
        right: -6px;
        bottom: -6px;
        left: -6px;
        content: ' '; }
      .button .icon, .button .icon-help, .button .icon-alert, .button #menu .footer .icon-help, #menu .footer .button .icon-help {
        vertical-align: top;
        pointer-events: none; }
      .button .icon:before, .button .icon-help:before, .button .icon-alert:before, .button #menu .footer .icon-help:before, #menu .footer .button .icon-help:before, .button.icon:before, .button.icon-help:before, .button.icon-alert:before, #menu .footer .button.icon-help:before, .button.icon-left:before, .button.icon-right:before {
        display: inline-block;
        padding: 0 0 1px 0;
        vertical-align: inherit;
        font-size: 24px;
        line-height: 41px;
        pointer-events: none; }
      .button.icon-left:before {
        float: left;
        padding-right: .2em;
        padding-left: 0; }
      .button.icon-right:before {
        float: right;
        padding-right: 0;
        padding-left: .2em; }
      .button.button-block, .button.button-full {
        margin-top: 10px;
        margin-bottom: 10px; }
      .button.button-light {
        border-color: transparent;
        background-color: #fff;
        color: #444; }
        .button.button-light:hover {
          color: #444;
          text-decoration: none; }
        .button.button-light.active, .button.button-light.activated {
          border-color: #a2a2a2;
          background-color: #fafafa; }
        .button.button-light.button-clear, .button.button-light.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #ddd; }
        .button.button-light.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-light.button-outline {
          border-color: #ddd;
          background: transparent;
          color: #ddd; }
          .button.button-light.button-outline.active, .button.button-light.button-outline.activated {
            background-color: #ddd;
            box-shadow: none;
            color: #fff; }
      .button.button-stable {
        border-color: transparent;
        background-color: #f8f8f8;
        color: #444; }
        .button.button-stable:hover {
          color: #444;
          text-decoration: none; }
        .button.button-stable.active, .button.button-stable.activated {
          border-color: #a2a2a2;
          background-color: #e5e5e5; }
        .button.button-stable.button-clear, .button.button-stable.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #b2b2b2; }
        .button.button-stable.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-stable.button-outline {
          border-color: #b2b2b2;
          background: transparent;
          color: #b2b2b2; }
          .button.button-stable.button-outline.active, .button.button-stable.button-outline.activated {
            background-color: #b2b2b2;
            box-shadow: none;
            color: #fff; }
      .button.button-positive, .button.button-text {
        border-color: transparent;
        background-color: #387ef5;
        color: #fff; }
        .button.button-positive:hover, .button.button-text:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-positive.active, .button.active.button-text, .button.button-positive.activated, .button.activated.button-text {
          border-color: #a2a2a2;
          background-color: #0c60ee; }
        .button.button-positive.button-clear, .button.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #387ef5; }
        .button.button-positive.button-icon, .button.button-icon.button-text {
          border-color: transparent;
          background: none; }
        .button.button-positive.button-outline, .button.button-outline.button-text {
          border-color: #387ef5;
          background: transparent;
          color: #387ef5; }
          .button.button-positive.button-outline.active, .button.button-outline.active.button-text, .button.button-positive.button-outline.activated, .button.button-outline.activated.button-text {
            background-color: #387ef5;
            box-shadow: none;
            color: #fff; }
      .button.button-calm {
        border-color: transparent;
        background-color: #11c1f3;
        color: #fff; }
        .button.button-calm:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-calm.active, .button.button-calm.activated {
          border-color: #a2a2a2;
          background-color: #0a9dc7; }
        .button.button-calm.button-clear, .button.button-calm.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #11c1f3; }
        .button.button-calm.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-calm.button-outline {
          border-color: #11c1f3;
          background: transparent;
          color: #11c1f3; }
          .button.button-calm.button-outline.active, .button.button-calm.button-outline.activated {
            background-color: #11c1f3;
            box-shadow: none;
            color: #fff; }
      .button.button-assertive {
        border-color: transparent;
        background-color: #ef473a;
        color: #fff; }
        .button.button-assertive:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-assertive.active, .button.button-assertive.activated {
          border-color: #a2a2a2;
          background-color: #e42112; }
        .button.button-assertive.button-clear, .button.button-assertive.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #ef473a; }
        .button.button-assertive.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-assertive.button-outline {
          border-color: #ef473a;
          background: transparent;
          color: #ef473a; }
          .button.button-assertive.button-outline.active, .button.button-assertive.button-outline.activated {
            background-color: #ef473a;
            box-shadow: none;
            color: #fff; }
      .button.button-balanced {
        border-color: transparent;
        background-color: #33cd5f;
        color: #fff; }
        .button.button-balanced:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-balanced.active, .button.button-balanced.activated {
          border-color: #a2a2a2;
          background-color: #28a54c; }
        .button.button-balanced.button-clear, .button.button-balanced.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #33cd5f; }
        .button.button-balanced.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-balanced.button-outline {
          border-color: #33cd5f;
          background: transparent;
          color: #33cd5f; }
          .button.button-balanced.button-outline.active, .button.button-balanced.button-outline.activated {
            background-color: #33cd5f;
            box-shadow: none;
            color: #fff; }
      .button.button-energized {
        border-color: transparent;
        background-color: #ffc900;
        color: #fff; }
        .button.button-energized:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-energized.active, .button.button-energized.activated {
          border-color: #a2a2a2;
          background-color: #e6b500; }
        .button.button-energized.button-clear, .button.button-energized.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #ffc900; }
        .button.button-energized.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-energized.button-outline {
          border-color: #ffc900;
          background: transparent;
          color: #ffc900; }
          .button.button-energized.button-outline.active, .button.button-energized.button-outline.activated {
            background-color: #ffc900;
            box-shadow: none;
            color: #fff; }
      .button.button-royal {
        border-color: transparent;
        background-color: #886aea;
        color: #fff; }
        .button.button-royal:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-royal.active, .button.button-royal.activated {
          border-color: #a2a2a2;
          background-color: #6b46e5; }
        .button.button-royal.button-clear, .button.button-royal.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #886aea; }
        .button.button-royal.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-royal.button-outline {
          border-color: #886aea;
          background: transparent;
          color: #886aea; }
          .button.button-royal.button-outline.active, .button.button-royal.button-outline.activated {
            background-color: #886aea;
            box-shadow: none;
            color: #fff; }
      .button.button-dark {
        border-color: transparent;
        background-color: #444;
        color: #fff; }
        .button.button-dark:hover {
          color: #fff;
          text-decoration: none; }
        .button.button-dark.active, .button.button-dark.activated {
          border-color: #a2a2a2;
          background-color: #262626; }
        .button.button-dark.button-clear, .button.button-dark.button-text {
          border-color: transparent;
          background: none;
          box-shadow: none;
          color: #444; }
        .button.button-dark.button-icon {
          border-color: transparent;
          background: none; }
        .button.button-dark.button-outline {
          border-color: #444;
          background: transparent;
          color: #444; }
          .button.button-dark.button-outline.active, .button.button-dark.button-outline.activated {
            background-color: #444;
            box-shadow: none;
            color: #fff; }

    .button-small, .button-text.button-small {
      padding: 2px 4px 1px;
      min-width: 28px;
      min-height: 30px;
      font-size: 12px;
      line-height: 26px; }
      .button-small .icon:before, .button-small .icon-help:before, .button-small .icon-alert:before, .button-small #menu .footer .icon-help:before, #menu .footer .button-small .icon-help:before, .button-small.icon:before, .button-small.icon-help:before, .button-small.icon-alert:before, #menu .footer .button-small.icon-help:before, .button-small.icon-left:before, .button-small.icon-right:before {
        font-size: 16px;
        line-height: 19px;
        margin-top: 3px; }

    .button-large {
      padding: 0 16px;
      min-width: 68px;
      min-height: 59px;
      font-size: 20px;
      line-height: 53px; }
      .button-large .icon:before, .button-large .icon-help:before, .button-large .icon-alert:before, .button-large #menu .footer .icon-help:before, #menu .footer .button-large .icon-help:before, .button-large.icon:before, .button-large.icon-help:before, .button-large.icon-alert:before, #menu .footer .button-large.icon-help:before, .button-large.icon-left:before, .button-large.icon-right:before {
        padding-bottom: 2px;
        font-size: 32px;
        line-height: 51px; }

    .button-icon {
      -webkit-transition: opacity 0.1s;
      transition: opacity 0.1s;
      padding: 0 6px;
      min-width: initial;
      border-color: transparent;
      background: none; }
      .button-icon.button.active, .button-icon.button.activated {
        border-color: transparent;
        background: none;
        box-shadow: none;
        opacity: 0.3; }
      .button-icon .icon:before, .button-icon .icon-help:before, .button-icon .icon-alert:before, .button-icon #menu .footer .icon-help:before, #menu .footer .button-icon .icon-help:before, .button-icon.icon:before, .button-icon.icon-help:before, .button-icon.icon-alert:before, #menu .footer .button-icon.icon-help:before {
        font-size: 32px; }

    .button-clear, .button-text {
      -webkit-transition: opacity 0.1s;
      transition: opacity 0.1s;
      padding: 0 6px;
      max-height: 42px;
      border-color: transparent;
      background: none;
      box-shadow: none; }
      .button-clear.button-clear, .button-text {
        border-color: transparent;
        background: none;
        box-shadow: none;
        color: transparent; }
      .button-clear.button-icon, .button-icon.button-text {
        border-color: transparent;
        background: none; }
      .button-clear.active, .active.button-text, .button-clear.activated, .activated.button-text {
        opacity: 0.3; }

    .button-outline {
      -webkit-transition: opacity 0.1s;
      transition: opacity 0.1s;
      background: none;
      box-shadow: none; }
      .button-outline.button-outline {
        border-color: transparent;
        background: transparent;
        color: transparent; }
        .button-outline.button-outline.active, .button-outline.button-outline.activated {
          background-color: transparent;
          box-shadow: none;
          color: #fff; }

    .padding > .button.button-block:first-child, .item.large-button-bar > .button.button-block:first-child {
      margin-top: 0; }

    .button-block {
      display: block;
      clear: both; }
      .button-block:after {
        clear: both; }

    .button-full,
    .button-full > .button {
      display: block;
      margin-right: 0;
      margin-left: 0;
      border-right-width: 0;
      border-left-width: 0;
      border-radius: 0; }

    button.button-block,
    button.button-full,
    .button-full > button.button,
    input.button.button-block {
      width: 100%; }

    a.button {
      text-decoration: none; }
      a.button .icon:before, a.button .icon-help:before, a.button .icon-alert:before, a.button #menu .footer .icon-help:before, #menu .footer a.button .icon-help:before, a.button.icon:before, a.button.icon-help:before, a.button.icon-alert:before, #menu .footer a.button.icon-help:before, a.button.icon-left:before, a.button.icon-right:before {
        margin-top: 2px; }

    .button.disabled,
    .button[disabled] {
      opacity: .4;
      cursor: default !important;
      pointer-events: none; }

    /**
     * Button Bar
     * --------------------------------------------------
     */
    .button-bar {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-flex: 1;
      -webkit-flex: 1;
      -moz-box-flex: 1;
      -moz-flex: 1;
      -ms-flex: 1;
      flex: 1;
      width: 100%; }
      .button-bar.button-bar-inline {
        display: block;
        width: auto;
        *zoom: 1; }
        .button-bar.button-bar-inline:before, .button-bar.button-bar-inline:after {
          display: table;
          content: "";
          line-height: 0; }
        .button-bar.button-bar-inline:after {
          clear: both; }
        .button-bar.button-bar-inline > .button {
          width: auto;
          display: inline-block;
          float: left; }
      .button-bar.bar-light > .button {
        border-color: #ddd; }
      .button-bar.bar-stable > .button {
        border-color: #b2b2b2; }
      .button-bar.bar-positive > .button {
        border-color: #0c60ee; }
      .button-bar.bar-calm > .button {
        border-color: #0a9dc7; }
      .button-bar.bar-assertive > .button {
        border-color: #e42112; }
      .button-bar.bar-balanced > .button {
        border-color: #28a54c; }
      .button-bar.bar-energized > .button {
        border-color: #e6b500; }
      .button-bar.bar-royal > .button {
        border-color: #6b46e5; }
      .button-bar.bar-dark > .button {
        border-color: #111; }

    .button-bar > .button {
      -webkit-box-flex: 1;
      -webkit-flex: 1;
      -moz-box-flex: 1;
      -moz-flex: 1;
      -ms-flex: 1;
      flex: 1;
      display: block;
      overflow: hidden;
      padding: 0 16px;
      width: 0;
      border-width: 1px 0px 1px 1px;
      border-radius: 0;
      text-align: center;
      text-overflow: ellipsis;
      white-space: nowrap; }
      .button-bar > .button:before,
      .button-bar > .button .icon:before,
      .button-bar > .button .icon-help:before,
      .button-bar > .button .icon-alert:before,
      .button-bar > .button #menu .footer .icon-help:before, #menu .footer
      .button-bar > .button .icon-help:before {
        line-height: 44px; }
      .button-bar > .button:first-child {
        border-radius: 4px 0px 0px 4px; }
      .button-bar > .button:last-child {
        border-right-width: 1px;
        border-radius: 0px 4px 4px 0px; }
      .button-bar > .button:only-child {
        border-radius: 4px; }

    .button-bar > .button-small:before,
    .button-bar > .button-small .icon:before,
    .button-bar > .button-small .icon-help:before,
    .button-bar > .button-small .icon-alert:before,
    .button-bar > .button-small #menu .footer .icon-help:before, #menu .footer
    .button-bar > .button-small .icon-help:before {
      line-height: 28px; }

    /**
     * Grid
     * --------------------------------------------------
     * Using flexbox for the grid, inspired by Philip Walton:
     * http://philipwalton.github.io/solved-by-flexbox/demos/grids/
     * By default each .col within a .row will evenly take up
     * available width, and the height of each .col with take
     * up the height of the tallest .col in the same .row.
     */
    .row {
      display: -webkit-box;
      display: -webkit-flex;
      display: -moz-box;
      display: -moz-flex;
      display: -ms-flexbox;
      display: flex;
      padding: 5px;
      width: 100%; }

    .row-wrap {
      -webkit-flex-wrap: wrap;
      -moz-flex-wrap: wrap;
      -ms-flex-wrap: wrap;
      flex-wrap: wrap; }

    .row-no-padding {
      padding: 0; }
      .row-no-padding > .col {
        padding: 0; }

    .row + .row {
      margin-top: -5px;
      padding-top: 0; }

    .col {
      -webkit-box-flex: 1;
      -webkit-flex: 1;
      -moz-box-flex: 1;
      -moz-flex: 1;
      -ms-flex: 1;
      flex: 1;
      display: block;
      padding: 5px;
      width: 100%; }

    /* Vertically Align Columns */
    /* .row-* vertically aligns every .col in the .row */
    .row-top {
      -webkit-box-align: start;
      -ms-flex-align: start;
      -webkit-align-items: flex-start;
      -moz-align-items: flex-start;
      align-items: flex-start; }

    .row-bottom {
      -webkit-box-align: end;
      -ms-flex-align: end;
      -webkit-align-items: flex-end;
      -moz-align-items: flex-end;
      align-items: flex-end; }

    .row-center {
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center; }

    .row-stretch {
      -webkit-box-align: stretch;
      -ms-flex-align: stretch;
      -webkit-align-items: stretch;
      -moz-align-items: stretch;
      align-items: stretch; }

    .row-baseline {
      -webkit-box-align: baseline;
      -ms-flex-align: baseline;
      -webkit-align-items: baseline;
      -moz-align-items: baseline;
      align-items: baseline; }

    /* .col-* vertically aligns an individual .col */
    .col-top {
      -webkit-align-self: flex-start;
      -moz-align-self: flex-start;
      -ms-flex-item-align: start;
      align-self: flex-start; }

    .col-bottom {
      -webkit-align-self: flex-end;
      -moz-align-self: flex-end;
      -ms-flex-item-align: end;
      align-self: flex-end; }

    .col-center {
      -webkit-align-self: center;
      -moz-align-self: center;
      -ms-flex-item-align: center;
      align-self: center; }

    /* Column Offsets */
    .col-offset-10 {
      margin-left: 10%; }

    .col-offset-20 {
      margin-left: 20%; }

    .col-offset-25 {
      margin-left: 25%; }

    .col-offset-33, .col-offset-34 {
      margin-left: 33.3333%; }

    .col-offset-50 {
      margin-left: 50%; }

    .col-offset-66, .col-offset-67 {
      margin-left: 66.6666%; }

    .col-offset-75 {
      margin-left: 75%; }

    .col-offset-80 {
      margin-left: 80%; }

    .col-offset-90 {
      margin-left: 90%; }

    /* Explicit Column Percent Sizes */
    /* By default each grid column will evenly distribute */
    /* across the grid. However, you can specify individual */
    /* columns to take up a certain size of the available area */
    .col-10 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 10%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 10%;
      -ms-flex: 0 0 10%;
      flex: 0 0 10%;
      max-width: 10%; }

    .col-20 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 20%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 20%;
      -ms-flex: 0 0 20%;
      flex: 0 0 20%;
      max-width: 20%; }

    .col-25 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 25%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 25%;
      -ms-flex: 0 0 25%;
      flex: 0 0 25%;
      max-width: 25%; }

    .col-33, .col-34 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 33.3333%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 33.3333%;
      -ms-flex: 0 0 33.3333%;
      flex: 0 0 33.3333%;
      max-width: 33.3333%; }

    .col-40 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 40%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 40%;
      -ms-flex: 0 0 40%;
      flex: 0 0 40%;
      max-width: 40%; }

    .col-50 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 50%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 50%;
      -ms-flex: 0 0 50%;
      flex: 0 0 50%;
      max-width: 50%; }

    .col-60 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 60%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 60%;
      -ms-flex: 0 0 60%;
      flex: 0 0 60%;
      max-width: 60%; }

    .col-66, .col-67 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 66.6666%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 66.6666%;
      -ms-flex: 0 0 66.6666%;
      flex: 0 0 66.6666%;
      max-width: 66.6666%; }

    .col-75 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 75%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 75%;
      -ms-flex: 0 0 75%;
      flex: 0 0 75%;
      max-width: 75%; }

    .col-80 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 80%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 80%;
      -ms-flex: 0 0 80%;
      flex: 0 0 80%;
      max-width: 80%; }

    .col-90 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 90%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 90%;
      -ms-flex: 0 0 90%;
      flex: 0 0 90%;
      max-width: 90%; }

    /* Responsive Grid Classes */
    /* Adding a class of responsive-X to a row */
    /* will trigger the flex-direction to */
    /* change to column and add some margin */
    /* to any columns in the row for clearity */
    @media (max-width: 567px) {
      .responsive-sm {
        -webkit-box-direction: normal;
        -moz-box-direction: normal;
        -webkit-box-orient: vertical;
        -moz-box-orient: vertical;
        -webkit-flex-direction: column;
        -ms-flex-direction: column;
        flex-direction: column; }
        .responsive-sm .col, .responsive-sm .col-10, .responsive-sm .col-20, .responsive-sm .col-25, .responsive-sm .col-33, .responsive-sm .col-34, .responsive-sm .col-50, .responsive-sm .col-66, .responsive-sm .col-67, .responsive-sm .col-75, .responsive-sm .col-80, .responsive-sm .col-90 {
          -webkit-box-flex: 1;
          -webkit-flex: 1;
          -moz-box-flex: 1;
          -moz-flex: 1;
          -ms-flex: 1;
          flex: 1;
          margin-bottom: 15px;
          margin-left: 0;
          max-width: 100%;
          width: 100%; } }

    @media (max-width: 767px) {
      .responsive-md {
        -webkit-box-direction: normal;
        -moz-box-direction: normal;
        -webkit-box-orient: vertical;
        -moz-box-orient: vertical;
        -webkit-flex-direction: column;
        -ms-flex-direction: column;
        flex-direction: column; }
        .responsive-md .col, .responsive-md .col-10, .responsive-md .col-20, .responsive-md .col-25, .responsive-md .col-33, .responsive-md .col-34, .responsive-md .col-50, .responsive-md .col-66, .responsive-md .col-67, .responsive-md .col-75, .responsive-md .col-80, .responsive-md .col-90 {
          -webkit-box-flex: 1;
          -webkit-flex: 1;
          -moz-box-flex: 1;
          -moz-flex: 1;
          -ms-flex: 1;
          flex: 1;
          margin-bottom: 15px;
          margin-left: 0;
          max-width: 100%;
          width: 100%; } }

    @media (max-width: 1023px) {
      .responsive-lg {
        -webkit-box-direction: normal;
        -moz-box-direction: normal;
        -webkit-box-orient: vertical;
        -moz-box-orient: vertical;
        -webkit-flex-direction: column;
        -ms-flex-direction: column;
        flex-direction: column; }
        .responsive-lg .col, .responsive-lg .col-10, .responsive-lg .col-20, .responsive-lg .col-25, .responsive-lg .col-33, .responsive-lg .col-34, .responsive-lg .col-50, .responsive-lg .col-66, .responsive-lg .col-67, .responsive-lg .col-75, .responsive-lg .col-80, .responsive-lg .col-90 {
          -webkit-box-flex: 1;
          -webkit-flex: 1;
          -moz-box-flex: 1;
          -moz-flex: 1;
          -ms-flex: 1;
          flex: 1;
          margin-bottom: 15px;
          margin-left: 0;
          max-width: 100%;
          width: 100%; } }

    /**
     * Utility Classes
     * --------------------------------------------------
     */
    .hide {
      display: none; }

    .opacity-hide {
      opacity: 0; }

    .grade-b .opacity-hide,
    .grade-c .opacity-hide {
      opacity: 1;
      display: none; }

    .show {
      display: block; }

    .opacity-show {
      opacity: 1; }

    .invisible {
      visibility: hidden; }

    .keyboard-open .hide-on-keyboard-open {
      display: none; }

    .keyboard-open .tabs.hide-on-keyboard-open + .pane .has-tabs,
    .keyboard-open .bar-footer.hide-on-keyboard-open + .pane .has-footer {
      bottom: 0; }

    .inline {
      display: inline-block; }

    .disable-pointer-events {
      pointer-events: none; }

    .enable-pointer-events {
      pointer-events: auto; }

    .disable-user-behavior {
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      -webkit-touch-callout: none;
      -webkit-tap-highlight-color: transparent;
      -webkit-tap-highlight-color: transparent;
      -webkit-user-drag: none;
      -ms-touch-action: none;
      -ms-content-zooming: none; }

    .click-block {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      opacity: 0;
      z-index: 99999;
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      overflow: hidden; }

    .click-block-hide {
      -webkit-transform: translate3d(-9999px, 0, 0);
      transform: translate3d(-9999px, 0, 0); }

    .no-resize {
      resize: none; }

    .block {
      display: block;
      clear: both; }
      .block:after {
        display: block;
        visibility: hidden;
        clear: both;
        height: 0;
        content: "."; }

    .full-image {
      width: 100%; }

    .clearfix {
      *zoom: 1; }
      .clearfix:before, .clearfix:after {
        display: table;
        content: "";
        line-height: 0; }
      .clearfix:after {
        clear: both; }

    /**
     * Content Padding
     * --------------------------------------------------
     */
    .padding, .item.large-button-bar {
      padding: 10px; }

    .padding-top,
    .padding-vertical {
      padding-top: 10px; }

    .padding-right, .popover-share .bar-footer .button-close,
    .padding-horizontal {
      padding-right: 10px; }

    .padding-bottom, .popover-share .bar-footer .button-close,
    .padding-vertical {
      padding-bottom: 10px; }

    .padding-left,
    .padding-horizontal {
      padding-left: 10px; }

    /**
     * Scrollable iFrames
     * --------------------------------------------------
     */
    .iframe-wrapper {
      position: fixed;
      -webkit-overflow-scrolling: touch;
      overflow: scroll; }
      .iframe-wrapper iframe {
        height: 100%;
        width: 100%; }

    /**
     * Rounded
     * --------------------------------------------------
     */
    .rounded {
      border-radius: 4px; }

    /**
     * Utility Colors
     * --------------------------------------------------
     * Utility colors are added to help set a naming convention. You'll
     * notice we purposely do not use words like "red" or "blue", but
     * instead have colors which represent an emotion or generic theme.
     */
    .light, a.light {
      color: #fff; }

    .light-bg {
      background-color: #fff; }

    .light-border {
      border-color: #ddd; }

    .stable, a.stable {
      color: #f8f8f8; }

    .stable-bg {
      background-color: #f8f8f8; }

    .stable-border {
      border-color: #b2b2b2; }

    .positive, .icon-help, a.positive, a.icon-help {
      color: #387ef5; }

    .positive-bg {
      background-color: #387ef5; }

    .positive-border {
      border-color: #0c60ee; }

    .calm, #menu .footer .icon-help, a.calm, #menu .footer a.icon-help {
      color: #11c1f3; }

    .calm-bg {
      background-color: #11c1f3; }

    .calm-border {
      border-color: #0a9dc7; }

    .assertive, .icon-alert, a.assertive, a.icon-alert {
      color: #ef473a; }

    .assertive-bg {
      background-color: #ef473a; }

    .assertive-border {
      border-color: #e42112; }

    .balanced, a.balanced {
      color: #33cd5f; }

    .balanced-bg {
      background-color: #33cd5f; }

    .balanced-border {
      border-color: #28a54c; }

    .energized, a.energized {
      color: #ffc900; }

    .energized-bg {
      background-color: #ffc900; }

    .energized-border {
      border-color: #e6b500; }

    .royal, a.royal {
      color: #886aea; }

    .royal-bg {
      background-color: #886aea; }

    .royal-border {
      border-color: #6b46e5; }

    .dark, a.dark {
      color: #444; }

    .dark-bg {
      background-color: #444; }

    .dark-border {
      border-color: #111; }

    [collection-repeat] {
      /* Position is set by transforms */
      left: 0 !important;
      top: 0 !important;
      position: absolute !important;
      z-index: 1; }

    .collection-repeat-container {
      position: relative;
      z-index: 1; }

    .collection-repeat-after-container {
      z-index: 0;
      display: block;
      /* when scrolling horizontally, make sure the after container doesn't take up 100% width */ }
      .collection-repeat-after-container.horizontal {
        display: inline-block; }

    [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak,
    .x-ng-cloak, .ng-hide:not(.ng-hide-animate) {
      display: none !important; }

    /**
     * Platform
     * --------------------------------------------------
     * Platform specific tweaks
     */
    .platform-ios.platform-cordova:not(.fullscreen) .bar-header:not(.bar-subheader) {
      height: 64px; }
      .platform-ios.platform-cordova:not(.fullscreen) .bar-header:not(.bar-subheader).item-input-inset .item-input-wrapper {
        margin-top: 19px !important; }
      .platform-ios.platform-cordova:not(.fullscreen) .bar-header:not(.bar-subheader) > * {
        margin-top: 20px; }

    .platform-ios.platform-cordova:not(.fullscreen) .tabs-top > .tabs,
    .platform-ios.platform-cordova:not(.fullscreen) .tabs.tabs-top {
      top: 64px; }

    .platform-ios.platform-cordova:not(.fullscreen) .has-header,
    .platform-ios.platform-cordova:not(.fullscreen) .bar-subheader {
      top: 64px; }

    .platform-ios.platform-cordova:not(.fullscreen) .has-subheader {
      top: 108px; }

    .platform-ios.platform-cordova:not(.fullscreen) .has-header.has-tabs-top {
      top: 113px; }

    .platform-ios.platform-cordova:not(.fullscreen) .has-header.has-subheader.has-tabs-top {
      top: 157px; }

    .platform-ios.platform-cordova .popover .bar-header:not(.bar-subheader) {
      height: 44px; }
      .platform-ios.platform-cordova .popover .bar-header:not(.bar-subheader).item-input-inset .item-input-wrapper {
        margin-top: -1px; }
      .platform-ios.platform-cordova .popover .bar-header:not(.bar-subheader) > * {
        margin-top: 0; }

    .platform-ios.platform-cordova .popover .has-header,
    .platform-ios.platform-cordova .popover .bar-subheader {
      top: 44px; }

    .platform-ios.platform-cordova .popover .has-subheader {
      top: 88px; }

    .platform-ios.platform-cordova.status-bar-hide {
      margin-bottom: 20px; }

    @media (orientation: landscape) {
      .platform-ios.platform-browser.platform-ipad {
        position: fixed; } }

    .platform-c:not(.enable-transitions) * {
      -webkit-transition: none !important;
      transition: none !important; }

    .slide-in-up {
      -webkit-transform: translate3d(0, 100%, 0);
      transform: translate3d(0, 100%, 0); }

    .slide-in-up.ng-enter,
    .slide-in-up > .ng-enter {
      -webkit-transition: all cubic-bezier(0.1, 0.7, 0.1, 1) 400ms;
      transition: all cubic-bezier(0.1, 0.7, 0.1, 1) 400ms; }

    .slide-in-up.ng-enter-active,
    .slide-in-up > .ng-enter-active {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .slide-in-up.ng-leave,
    .slide-in-up > .ng-leave {
      -webkit-transition: all ease-in-out 250ms;
      transition: all ease-in-out 250ms; }

    @-webkit-keyframes scaleOut {
      from {
        -webkit-transform: scale(1);
        opacity: 1; }
      to {
        -webkit-transform: scale(0.8);
        opacity: 0; } }

    @keyframes scaleOut {
      from {
        transform: scale(1);
        opacity: 1; }
      to {
        transform: scale(0.8);
        opacity: 0; } }

    @-webkit-keyframes superScaleIn {
      from {
        -webkit-transform: scale(1.2);
        opacity: 0; }
      to {
        -webkit-transform: scale(1);
        opacity: 1; } }

    @keyframes superScaleIn {
      from {
        transform: scale(1.2);
        opacity: 0; }
      to {
        transform: scale(1);
        opacity: 1; } }

    [nav-view-transition="ios"] [nav-view="entering"],
    [nav-view-transition="ios"] [nav-view="leaving"] {
      -webkit-transition-duration: 500ms;
      transition-duration: 500ms;
      -webkit-transition-timing-function: cubic-bezier(0.36, 0.66, 0.04, 1);
      transition-timing-function: cubic-bezier(0.36, 0.66, 0.04, 1);
      -webkit-transition-property: opacity, -webkit-transform, box-shadow;
      transition-property: opacity, transform, box-shadow; }

    [nav-view-transition="ios"][nav-view-direction="forward"], [nav-view-transition="ios"][nav-view-direction="back"] {
      background-color: #000; }

    [nav-view-transition="ios"] [nav-view="active"],
    [nav-view-transition="ios"][nav-view-direction="forward"] [nav-view="entering"],
    [nav-view-transition="ios"][nav-view-direction="back"] [nav-view="leaving"] {
      z-index: 3; }

    [nav-view-transition="ios"][nav-view-direction="back"] [nav-view="entering"],
    [nav-view-transition="ios"][nav-view-direction="forward"] [nav-view="leaving"] {
      z-index: 2; }

    [nav-bar-transition="ios"] .title,
    [nav-bar-transition="ios"] .buttons,
    [nav-bar-transition="ios"] .back-text {
      -webkit-transition-duration: 500ms;
      transition-duration: 500ms;
      -webkit-transition-timing-function: cubic-bezier(0.36, 0.66, 0.04, 1);
      transition-timing-function: cubic-bezier(0.36, 0.66, 0.04, 1);
      -webkit-transition-property: opacity, -webkit-transform;
      transition-property: opacity, transform; }

    [nav-bar-transition="ios"] [nav-bar="active"],
    [nav-bar-transition="ios"] [nav-bar="entering"] {
      z-index: 10; }
      [nav-bar-transition="ios"] [nav-bar="active"] .bar,
      [nav-bar-transition="ios"] [nav-bar="entering"] .bar {
        background: transparent; }

    [nav-bar-transition="ios"] [nav-bar="cached"] {
      display: block; }
      [nav-bar-transition="ios"] [nav-bar="cached"] .header-item {
        display: none; }

    [nav-view-transition="android"] [nav-view="entering"],
    [nav-view-transition="android"] [nav-view="leaving"] {
      -webkit-transition-duration: 200ms;
      transition-duration: 200ms;
      -webkit-transition-timing-function: cubic-bezier(0.4, 0.6, 0.2, 1);
      transition-timing-function: cubic-bezier(0.4, 0.6, 0.2, 1);
      -webkit-transition-property: -webkit-transform;
      transition-property: transform; }

    [nav-view-transition="android"] [nav-view="active"],
    [nav-view-transition="android"][nav-view-direction="forward"] [nav-view="entering"],
    [nav-view-transition="android"][nav-view-direction="back"] [nav-view="leaving"] {
      z-index: 3; }

    [nav-view-transition="android"][nav-view-direction="back"] [nav-view="entering"],
    [nav-view-transition="android"][nav-view-direction="forward"] [nav-view="leaving"] {
      z-index: 2; }

    [nav-bar-transition="android"] .title,
    [nav-bar-transition="android"] .buttons {
      -webkit-transition-duration: 200ms;
      transition-duration: 200ms;
      -webkit-transition-timing-function: cubic-bezier(0.4, 0.6, 0.2, 1);
      transition-timing-function: cubic-bezier(0.4, 0.6, 0.2, 1);
      -webkit-transition-property: opacity;
      transition-property: opacity; }

    [nav-bar-transition="android"] [nav-bar="active"],
    [nav-bar-transition="android"] [nav-bar="entering"] {
      z-index: 10; }
      [nav-bar-transition="android"] [nav-bar="active"] .bar,
      [nav-bar-transition="android"] [nav-bar="entering"] .bar {
        background: transparent; }

    [nav-bar-transition="android"] [nav-bar="cached"] {
      display: block; }
      [nav-bar-transition="android"] [nav-bar="cached"] .header-item {
        display: none; }

    [nav-swipe="fast"] [nav-view],
    [nav-swipe="fast"] .title,
    [nav-swipe="fast"] .buttons,
    [nav-swipe="fast"] .back-text {
      -webkit-transition-duration: 50ms;
      transition-duration: 50ms;
      -webkit-transition-timing-function: linear;
      transition-timing-function: linear; }

    [nav-swipe="slow"] [nav-view],
    [nav-swipe="slow"] .title,
    [nav-swipe="slow"] .buttons,
    [nav-swipe="slow"] .back-text {
      -webkit-transition-duration: 160ms;
      transition-duration: 160ms;
      -webkit-transition-timing-function: linear;
      transition-timing-function: linear; }

    [nav-view="cached"],
    [nav-bar="cached"] {
      display: none; }

    [nav-view="stage"] {
      opacity: 0;
      -webkit-transition-duration: 0;
      transition-duration: 0; }

    [nav-bar="stage"] .title,
    [nav-bar="stage"] .buttons,
    [nav-bar="stage"] .back-text {
      position: absolute;
      opacity: 0;
      -webkit-transition-duration: 0s;
      transition-duration: 0s; }

    /* BEGIN Thin */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Thin/RobotoDraft-Thin.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Thin/RobotoDraft-Thin.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Thin/RobotoDraft-Thin.ttf?v=1.1.0") format("truetype");
      font-weight: 100;
      font-style: normal; }

    /* END Thin */
    /* BEGIN Light */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Light/RobotoDraft-Light.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Light/RobotoDraft-Light.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Light/RobotoDraft-Light.ttf?v=1.1.0") format("truetype");
      font-weight: 300;
      font-style: normal; }

    /* END Light */
    /* BEGIN Regular */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.ttf?v=1.1.0") format("truetype");
      font-weight: 400;
      font-style: normal; }

    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Regular/RobotoDraft-Regular.ttf?v=1.1.0") format("truetype");
      font-weight: normal;
      font-style: normal; }

    /* END Regular */
    /* BEGIN Italic */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.ttf?v=1.1.0") format("truetype");
      font-weight: 400;
      font-style: italic; }

    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Italic/RobotoDraft-Italic.ttf?v=1.1.0") format("truetype");
      font-weight: normal;
      font-style: italic; }

    /* END Italic */
    /* BEGIN Medium */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Medium/RobotoDraft-Medium.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Medium/RobotoDraft-Medium.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Medium/RobotoDraft-Medium.ttf?v=1.1.0") format("truetype");
      font-weight: 500;
      font-style: normal; }

    /* END Medium */
    /* BEGIN Bold */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.ttf?v=1.1.0") format("truetype");
      font-weight: 700;
      font-style: normal; }

    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Bold/RobotoDraft-Bold.ttf?v=1.1.0") format("truetype");
      font-weight: bold;
      font-style: normal; }

    /* END Bold */
    /* BEGIN Bold Italic */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.ttf?v=1.1.0") format("truetype");
      font-weight: 700;
      font-style: italic; }

    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/BoldItalic/RobotoDraft-BoldItalic.ttf?v=1.1.0") format("truetype");
      font-weight: bold;
      font-style: italic; }

    /* END Bold Italic */
    /* BEGIN Black */
    @font-face {
      font-family: RobotoDraft;
      src: url("../lib/ionic/fonts/robotdraft/Black/RobotoDraft-Black.woff2?v=1.1.0") format("woff2"), url("../lib/ionic/fonts/robotdraft/Black/RobotoDraft-Black.woff?v=1.1.0") format("woff"), url("../lib/ionic/fonts/robotdraft/Black/RobotoDraft-Black.ttf?v=1.1.0") format("truetype");
      font-weight: 900;
      font-style: normal; }

    /* END Black */
    /* Directives : MD Label
    ==================================*/
    .item-md-label {
      display: block;
      background: transparent;
      box-shadow: none;
      margin-left: 12px;
      margin-right: 12px;
      padding: 30px 0 0; }

    .item-md-label .input-label {
      position: absolute;
      padding: 5px 0 0;
      z-index: 2;
      -webkit-transform: translate3d(0, -30px, 0) scale(1);
      transform: translate3d(0, -30px, 0) scale(1);
      -webkit-transition: all 0.2s ease;
      transition: all 0.2s ease;
      color: #fff;
      opacity: 0.5;
      filter: alpha(opacity=50);
      -webkit-transform-origin: 0;
      -ms-transform-origin: 0;
      transform-origin: 0; }

    .item-md-label input {
      background-color: rgba(0, 0, 0, 0.6);
      bottom: 0;
      color: #fff;
      letter-spacing: 0.25rem;
      padding: 20px 10px;
      position: relative;
      z-index: 1; }

    .item-md-label .highlight {
      position: absolute;
      bottom: 0;
      height: 2px;
      left: 0;
      width: 100%;
      -webkit-transform: translate3d(-100%, 0, 0);
      transform: translate3d(-100%, 0, 0);
      -webkit-transition: all 0.15s ease;
      transition: all 0.15s ease;
      z-index: 1; }

    .item-md-label .highlight-light {
      background: #fff; }

    .item-md-label .highlight-stable {
      background: #f8f8f8; }

    .item-md-label .highlight-positive {
      background: #387ef5; }

    .item-md-label .highlight-calm {
      background: #11c1f3; }

    .item-md-label .highlight-balanced {
      background: #33cd5f; }

    .item-md-label .highlight-energized {
      background: #ffc900; }

    .item-md-label .highlight-assertive {
      background: #ef473a; }

    .item-md-label .highlight-royal {
      background: #886aea; }

    .item-md-label .highlight-dark {
      background: #444; }

    .item-md-label .input-label {
      letter-spacing: 0.25rem;
      padding: 0 10px; }

    .item-md-label input:focus ~ .input-label, .item-md-label input.used ~ .input-label {
      font-weight: bold;
      opacity: 0.7;
      filter: alpha(opacity=70);
      padding: 0;
      text-transform: uppercase;
      -webkit-transform: translate3d(0, -60px, 0) scale(0.9);
      transform: translate3d(0, -60px, 0) scale(0.9); }

    .item-md-label input:focus ~ .highlight {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    /* Bar - Header - Expanded
    ==================================*/
    .expanded .bar.bar-header,
    .bar.bar-header.expanded {
      height: 75px; }

    .expanded.bar.bar-header .title,
    .bar.bar-header.expanded .title {
      bottom: 0;
      top: initial;
      padding-left: 16px; }

    .expanded .bar.bar-header .title.fab-left,
    .bar.bar-header.expanded .title.fab-left {
      bottom: 0;
      left: 90px;
      position: absolute;
      right: initial;
      top: initial; }

    .expanded .bar.bar-header .title.fab-right,
    .bar.bar-header.expanded .title.fab-right {
      bottom: 0;
      left: 4px;
      position: absolute;
      top: initial;
      right: initial; }

    .expanded .bar.bar-header + .button-fab,
    .bar.bar-header.expanded + .button-fab {
      top: 50px; }

    .expanded .bar.bar-header.push-down,
    .bar.bar-header.expanded.push-down {
      height: 44px;
      overflow: hidden; }

    .expanded .bar.bar-header,
    .bar.bar-header.expanded {
      -webkit-transition: height 1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: height 1s cubic-bezier(0.55, 0, 0.1, 1);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .expanded .bar.bar-header + .button-fab,
    .bar.bar-header.expanded + .button-fab {
      -webkit-transition: all 1.1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 1.1s cubic-bezier(0.55, 0, 0.1, 1);
      -webkit-transform: translate3d(0, 0, 0) scale(1);
      transform: translate3d(0, 0, 0) scale(1); }

    .expanded .bar.bar-header.push-down + .button-fab,
    .bar.bar-header.expanded.push-down + .button-fab {
      top: 0;
      -webkit-transform: translate3d(-100px, -100px, 0) scale(2.5);
      transform: translate3d(-100px, -100px, 0) scale(2.5); }

    .expanded .bar.bar-header.push-down .title,
    .bar.bar-header.expanded.push-down .title {
      opacity: 0;
      filter: alpha(opacity=0);
      left: initial;
      right: initial; }

    .expanded .bar.bar-header .title,
    .bar.bar-header.expanded .title {
      opacity: 1;
      filter: alpha(opacity=100);
      -webkit-transition: all 2s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 2s cubic-bezier(0.55, 0, 0.1, 1); }

    .expanded .bar.bar-header .title, .bar.bar-header.expanded .title {
      bottom: 0;
      left: 42px !important;
      top: initial; }

    .expanded.has-header-fab-left .bar.bar-header .title, .bar.bar-header.expanded.has-header-fab-left .title {
      left: 76px !important; }

    /* Bar
    ==================================*/
    .bar {
      z-index: 2;
      font-size: 1.3em;
      width: 100%;
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26); }

    .bar .button {
      min-width: 38px;
      z-index: 3; }

    .bar .no-text span.back-text {
      display: none; }

    .bar .title sup {
      opacity: 0.7; }

    .bar.bar-header .button + .title {
      text-align: left;
      left: 35px;
      line-height: 46px; }

    /* Button Bar
    ==================================*/
    .button-bar {
      box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.15); }

    .button-bar > .button {
      box-shadow: none;
      /*    line-height: initial; */ }

    .button-bar > .button .icon:before, .button-bar > .button .icon-help:before, .button-bar > .button .icon-alert:before, .button-bar > .button #menu .footer .icon-help:before, #menu .footer .button-bar > .button .icon-help:before,
    .button-bar > .button:before {
      line-height: initial; }

    .bar-footer .button-fab {
      position: absolute;
      top: -26px;
      bottom: initial; }

    .bar-footer .buttons-left .button-fab {
      left: 8px; }

    .bar-footer .buttons-right .button-fab {
      right: 8px; }

    .bar .button.button-clear, .bar .button.button-text {
      box-shadow: none; }

    .left-buttons .button-fab {
      left: 8px;
      top: 16px; }

    .right-buttons .button-fab {
      right: 8px;
      top: 16px; }

    .fab-left.title-left,
    .fab-left.title.title-left {
      left: 68px; }

    /* Button : FAB
    ==================================*/
    .button.button-fab,
    .bar .button.button-fab {
      box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
      z-index: 9999;
      width: 56px;
      height: 56px;
      max-height: initial;
      max-width: initial;
      border-radius: 50%;
      border-radius: 50%;
      overflow: hidden;
      padding: 0;
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      -webkit-transition: 0.3s fade-in-out;
      transition: 0.3s fade-in-out;
      -webkit-transition-property: -webkit-transform, box-shadow;
      transition-property: transform, box-shadow; }

    .button.button-fab.button-fab-bottom-right,
    .bar .button.button-fab.button-fab-bottom-right {
      top: auto;
      right: 16px;
      bottom: 16px;
      left: auto;
      position: absolute; }

    .button.button-fab.button-fab-bottom-left,
    .bar .button.button-fab.button-fab-bottom-left {
      top: auto;
      right: auto;
      bottom: 16px;
      left: 16px;
      position: absolute; }

    .button.button-fab.button-fab-top-right,
    .bar .button.button-fab.button-fab-top-right {
      top: 32px;
      right: 16px;
      bottom: auto;
      left: auto;
      position: absolute; }

    .button.button-fab.button-fab-top-left,
    .bar .button.button-fab.button-fab-top-left {
      top: 32px;
      right: auto;
      bottom: auto;
      left: 16px;
      position: absolute; }

    .button.button-fab.button-fab-top-left.expanded,
    .button.button-fab.button-fab-top-right.expanded,
    .bar .button.button-fab.button-fab-top-left.expanded,
    .bar .button.button-fab.button-fab-top-right.expanded {
      top: 48px; }

    .button.button-fab i,
    .bar .button.button-fab i {
      font-size: 2.5rem;
      margin-top: 0; }

    .button.button-fab.mini,
    .bar .button.button-fab.mini {
      width: 40px;
      height: 40px; }

    .button.button-fab.mini i,
    .bar .button.button-fab.mini i {
      font-size: 2rem; }

    /* Motion */
    .motion {
      -webkit-transition: all 0.5s ease-out;
      transition: all 0.5s ease-out; }

    .fade {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spin-back {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(360deg) scale(0) !important;
      transform: translateZ(0) rotate(360deg) scale(0) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spiral {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(-360deg) scale(0) translate(-120px) !important;
      transform: translateZ(0) rotate(-360deg) scale(0) translate(-120px) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spiral-back {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(360deg) scale(0) translate(120px) !important;
      transform: translateZ(0) rotate(360deg) scale(0) translate(120px) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .menu-open .avatar {
      opacity: 1;
      filter: alpha(opacity=100);
      -webkit-transform: translateZ(0) rotate(0) scale(1) !important;
      transform: translateZ(0) rotate(0) scale(1) !important;
      -webkit-transition: all 0.3s ease-out !important;
      transition: all 0.3s ease-out !important; }

    .button.button-fab.button-fab-top-left.motion {
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      -webkit-transform: translate3d(-120px, 60px, 0);
      transform: translate3d(-120px, 60px, 0);
      -webkit-transition: all 0.1s ease-out;
      transition: all 0.1s ease-out; }

    .button.button-fab.button-fab-top-right.motion {
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      -webkit-transform: translate3d(120px, 60px, 0);
      transform: translate3d(120px, 60px, 0);
      -webkit-transition: all 0.1s ease-out;
      transition: all 0.1s ease-out; }

    .button.button-fab.button-fab-bottom-left.motion {
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      -webkit-transform: translate3d(-120px, 60px, 0);
      transform: translate3d(-120px, 60px, 0);
      -webkit-transition: all 0.1s ease-out;
      transition: all 0.1s ease-out; }

    .button.button-fab.button-fab-bottom-right.motion {
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      -webkit-transform: translate3d(120px, 60px, 0);
      transform: translate3d(120px, 60px, 0);
      -webkit-transition: all 0.1s ease-out;
      transition: all 0.1s ease-out; }

    .spin {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(0) scale(0) !important;
      transform: translateZ(0) rotate(0) scale(0) !important;
      -webkit-transition: all 0.3s ease-out !important;
      transition: all 0.3s ease-out !important; }

    .spin.on {
      -webkit-transform: translateZ(0) rotate(-360deg) scale(1) !important;
      transform: translateZ(0) rotate(-360deg) scale(1) !important; }

    .flap {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotateX(0) scale(0) translate(-120px) !important;
      transform: translateZ(0) rotateX(0) scale(0) translate(-120px) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flap.on {
      -webkit-transform: translateZ(0) rotateX(-720deg) scale(1) translate(0) !important;
      transform: translateZ(0) rotateX(-720deg) scale(1) translate(0) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .drop {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) scale(3) !important;
      transform: translateZ(0) scale(3) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .drop.on {
      -webkit-transform: translateZ(0) scale(1) !important;
      transform: translateZ(0) scale(1) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flip {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotateY(0) scale(0) !important;
      transform: translateZ(0) rotateY(0) scale(0) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flip.on {
      -webkit-transform: translateZ(0) rotateY(-720deg) scale(1) !important;
      transform: translateZ(0) rotateY(-720deg) scale(1) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    /* Button : Floating
    ==================================*/
    .button.button-floating, .bar .button.button-floating {
      display: inline-block;
      color: #FFF;
      position: relative;
      z-index: 1;
      width: 37px;
      height: 37px;
      line-height: 37px;
      padding: 0;
      border-radius: 50%;
      background-clip: padding-box;
      -webkit-transition: 0.3s;
      transition: 0.3s;
      cursor: pointer; }

    .button.button-floating i, .bar .button.button-floating i {
      width: inherit;
      display: inline-block;
      text-align: center;
      color: #FFF;
      font-size: 1.6rem;
      line-height: 37px; }

    .button.button-floating.button-large, .bar .button.button-floating.button-large {
      width: 55.5px;
      height: 55.5px; }

    .button.button-floating.button-large i, .bar .button.button-floating.button-large i {
      line-height: 55.5px; }

    /* Button
    ==================================*/
    .button,
    .button.button-large,
    .button.button-flat,
    .bar .button,
    .bar .button.button-large,
    .bar .button.button-flat {
      box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
      display: inline-block;
      height: 36px;
      padding: 0 2rem;
      border-radius: 2px;
      background-clip: padding-box;
      text-transform: uppercase;
      border: none;
      outline: 0;
      -webkit-tap-highlight-color: transparent; }

    .button.disabled,
    .button.disabled.button-large,
    .button.button-floating.disabled,
    .button.button-large.disabled,
    .button.button:disabled,
    .button.button-large:disabled,
    .button.button-large:disabled,
    .button.button-floating:disabled,
    .bar .button.disabled,
    .bar .button.disabled.button-large,
    .bar .button.button-floating.disabled,
    .bar .button.button-large.disabled,
    .bar .button.button:disabled,
    .bar .button.button-large:disabled,
    .bar .button.button-large:disabled,
    .bar .button.button-floating:disabled {
      background-color: #DFDFDF;
      box-shadow: none;
      color: #9F9F9F; }

    .button.disabled:hover,
    .button.disabled.button-large:hover,
    .button.button-floating.disabled:hover,
    .button.button-large.disabled:hover,
    .button.button:disabled:hover,
    .button.button-large:disabled:hover,
    .button.button-large:disabled:hover,
    .button.button-floating:disabled:hover,
    .bar .button.disabled:hover,
    .bar .button.disabled.button-large:hover,
    .bar .button.button-floating.disabled:hover,
    .bar .button.button-large.disabled:hover,
    .bar .button.button:disabled:hover,
    .bar .button.button-large:disabled:hover,
    .bar .button.button-large:disabled:hover,
    .bar .button.button-floating:disabled:hover {
      background-color: #DFDFDF;
      color: #9F9F9F; }

    .button i,
    .button.button-large i,
    .button.button-floating i,
    .button.button-large i,
    .button.button-flat i,
    .bar .button i,
    .bar .button.button-large i,
    .bar .button.button-floating i,
    .bar .button.button-large i,
    .bar .button.button-flat i {
      font-size: 1.3rem; }

    .button-bar .button {
      border-radius: 0; }

    .button,
    .button-large,
    .bar .button,
    .bar .button-large {
      text-decoration: none;
      text-align: center;
      letter-spacing: 0.5px;
      -webkit-transition: 0.2s ease-out;
      transition: 0.2s ease-out;
      cursor: pointer; }

    .button {
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      position: relative;
      outline: none;
      margin: 0;
      /* background: transparent; */
      white-space: nowrap;
      text-align: center;
      text-transform: uppercase;
      font-weight: 500;
      font-style: inherit;
      font-variant: inherit;
      font-size: inherit;
      text-decoration: none;
      cursor: pointer;
      overflow: hidden;
      -webkit-transition: box-shadow 0.4s cubic-bezier(0.25, 0.8, 0.25, 1), background-color 0.4s cubic-bezier(0.25, 0.8, 0.25, 1), -webkit-transform 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
      transition: box-shadow 0.4s cubic-bezier(0.25, 0.8, 0.25, 1), background-color 0.4s cubic-bezier(0.25, 0.8, 0.25, 1), transform 0.4s cubic-bezier(0.25, 0.8, 0.25, 1); }

    .button:focus {
      outline: none; }

    .button.ng-hide {
      -webkit-transition: none;
      transition: none; }

    .button.cornered {
      border-radius: 0; }

    .button.raised {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .button-outline,
    .button-outline:hover,
    .button-outline:active {
      border-style: solid;
      border-width: 1px; }

    .button.button-outline.button-assertive,
    .button.button-outline.button-balanced,
    .button.button-outline.button-calm,
    .button.button-outline.button-dark,
    .button.button-outline.button-energized,
    .button.button-outline.button-light,
    .button.button-outline.button-positive,
    .button.button-outline.button-text,
    .button.button-outline.button-royal,
    .button.button-outline.button-stable,
    .button.button-outline {
      border-color: rgba(0, 0, 0, 0.1); }

    .button-flat,
    .bar .button-flat {
      box-shadow: none;
      background-color: transparent;
      color: #343434;
      cursor: pointer; }

    .button.button-flat.disabled,
    .bar .button.button-flat.disabled {
      color: #b3b3b3; }

    .button.button-large i,
    .bar .button.button-large i {
      font-size: 1.6rem; }

    .button-pin-header.button-floating {
      position: absolute;
      z-index: 1000; }

    .button-pin-header.button-pin-left {
      left: 24px;
      top: -24px; }

    .button-pin-header.button-pin-right {
      right: 24px;
      top: -24px; }

    .button:not([disabled]).raised:focus,
    .button:not([disabled]).raised:hover,
    .button:not([disabled]).floating:focus,
    .button:not([disabled]).floating:hover {
      -webkit-transform: translate3d(0, -1px, 0);
      transform: translate3d(0, -1px, 0); }

    .button.button-flat {
      box-shadow: none;
      /* background: transparent; */
      color: inherit; }

    .button.button-flat:hover {
      color: inherit; }

    .button.button-flat,
    .button.button-flat:hover,
    .button.button-flat:active {
      color: #fff; }

    .button.button-clear, .button.button-text,
    .button.button-clear:hover,
    .button.button-text:hover,
    .button.button-clear:active,
    .button.button-text:active {
      background: transparent; }

    .button-full.ink,
    .button-block.ink {
      display: block; }

    /* Card
    ==================================*/
    .card-item.item {
      border: none;
      padding-bottom: 4px;
      padding-top: 4px; }

    .card-item.item:first-child {
      padding-top: 16px; }

    .card {
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26);
      display: block;
      margin: 8px;
      padding: 0;
      position: relative; }

    .card .image {
      display: block;
      margin-top: 10px;
      margin-bottom: 5px; }

    .card img {
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26);
      display: block;
      max-width: 100%;
      max-height: initial;
      position: static; }

    .card.card-gallery img {
      border: none;
      box-shadow: none;
      display: block; }

    .card .card-footer {
      font-size: 90%;
      opacity: 0.8;
      filter: alpha(opacity=80);
      padding-top: 10px; }

    .card > .item {
      border: none; }

    .card.card-gallery > .item {
      background: inherit; }

    .card .icon + .icon, .card .icon-help + .icon, .card .icon-alert + .icon, .card #menu .footer .icon-help + .icon, #menu .footer .card .icon-help + .icon, .card .icon + .icon-help, .card .icon-help + .icon-help, .card .icon-alert + .icon-help, .card .icon + .icon-alert, .card .icon-help + .icon-alert, .card .icon-alert + .icon-alert, .card #menu .footer .icon-help + .icon-alert, #menu .footer .card .icon-help + .icon-alert, .card #menu .footer .icon + .icon-help, #menu .footer .card .icon + .icon-help, .card #menu .footer .icon-alert + .icon-help, #menu .footer .card .icon-alert + .icon-help, .card #menu .footer .icon-help + .icon-help, #menu .footer .card .icon-help + .icon-help {
      padding-left: 1rem; }

    .card.animate-fade-in {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-transform: translate3d(-30px, 1px, 0);
      -webkit-transition: all 1s ease-in-out; }

    .card.animate-fade-in.done, .animate-fade-slide-in .expanded .card.animate-fade-in.item,
    .animate-fade-slide-in .card.animate-fade-in.expanded.item,
    .animate-fade-slide-in-right .expanded .card.animate-fade-in.item,
    .animate-fade-slide-in-right .card.animate-fade-in.expanded.item,
    .animate-ripple .expanded .card.animate-fade-in.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.animate-fade-in.card-comment {
      opacity: 1;
      filter: alpha(opacity=100);
      -webkit-transform: translate3d(0, 0, 0); }

    .card .item.item-avatar {
      min-height: 88px;
      padding-left: 88px; }

    /* Hero
    ==================================*/
    .hero {
      background-size: cover;
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26);
      color: #fff;
      height: 200px;
      position: relative;
      text-align: center;
      -webkit-transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      width: 100%; }

    .hero > * {
      -webkit-transition: opacity 2.5s cubic-bezier(0.55, 0, 0.1, 1);
      transition: opacity 2.5s cubic-bezier(0.55, 0, 0.1, 1);
      opacity: 1;
      filter: alpha(opacity=100); }

    .hero + .mid-bar {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      -webkit-transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      height: initial;
      opacity: 1;
      filter: alpha(opacity=100); }

    .hero .hero-icon {
      box-shadow: 0px 0 2px 0 rgba(0, 0, 0, 0.26);
      border-radius: 50%;
      display: inline-block;
      font-size: 65px;
      height: 150px;
      padding: 10px 30px;
      line-height: 136px;
      width: 150px; }

    .hero.no-header {
      height: 244px; }

    .hero > .content {
      bottom: 0;
      position: absolute;
      text-align: center;
      width: 100%;
      z-index: 1; }

    .hero > .content > .avatar {
      background-position: center;
      background-size: cover;
      border: solid 1px rgba(255, 255, 255, 0.8);
      border-radius: 50%;
      display: inline-block;
      height: 88px;
      left: auto;
      margin-bottom: 10px;
      position: relative;
      width: 88px; }

    .hero h1 .hero h2, .hero h3, .hero h4, .hero h5, .hero h6 {
      color: #fff;
      margin: 0; }

    .hero h4 {
      color: rgba(255, 255, 255, 0.7);
      margin: 3px 0 16px; }

    .hero h1 > a, .hero h2 > a, .hero h3 > a, .hero h4 > a, .hero h5 > a, .hero h6 > a {
      text-decoration: none; }

    .hero + .button-bar {
      border-radius: 0;
      margin-top: 0; }

    .hero + .button-bar > .button:first-child, .hero + .button-bar > .button:last-child {
      border-radius: 0; }

    .hero .hero-icon {
      color: #fff;
      font-size: 96px; }

    .hero .hero-icon + h1 {
      color: white;
      letter-spacing: 0.15rem; }

    .hero .button, .hero .button.button-large, .hero .button.button-flat {
      margin: 0; }

    .hero h1.title {
      color: #fff;
      font-size: 23px;
      margin: 0;
      text-align: left;
      padding-left: 80px;
      line-height: 59px; }

    .hero + .mid-bar {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
      -webkit-transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      height: initial;
      opacity: 1;
      filter: alpha(opacity=100); }

    .hero > * {
      -webkit-transition: opacity 2.5s cubic-bezier(0.55, 0, 0.1, 1);
      transition: opacity 2.5s cubic-bezier(0.55, 0, 0.1, 1);
      opacity: 1;
      filter: alpha(opacity=100); }

    /* Item
    ==================================*/
    .item {
      font-size: 14px;
      width: 100%; }

    .item-icon-left .icon, .item-icon-left .icon-help, .item-icon-left .icon-alert, .item-icon-left #menu .footer .icon-help, #menu .footer .item-icon-left .icon-help {
      left: 16px; }

    .item-icon-right .icon, .item-icon-right .icon-help, .item-icon-right .icon-alert, .item-icon-right #menu .footer .icon-help, #menu .footer .item-icon-right .icon-help {
      right: 16px; }

    /*
    .list .item.item-icon-right {
        padding-right: 60px;
    }
    */
    .item-thumbnail-left > img:first-child, .item-thumbnail-left .item-image, .item-thumbnail-left .item-content > img:first-child, .item-thumbnail-left .item-content .item-image {
      border-radius: 50%; }

    .tab-item.activated {
      height: calc(100% + 3px);
      /* Stretch */ }

    /* List
    ==================================*/
    .content + .list {
      padding-top: 0; }

    .list .item {
      border: none;
      /*
        padding-left: 16px;
        padding-right: 16px;
        */
      min-height: 48px;
      text-align: left; }

    .list .item.tabs {
      padding: initial; }

    .list .item.item-bg-image {
      max-height: 150px;
      min-height: 150px; }

    .list .item.item-bg-image > img {
      height: 100%;
      left: 0;
      max-width: initial;
      opacity: 0.65;
      filter: alpha(opacity=65);
      position: absolute;
      top: 0;
      width: 100%;
      z-index: 0; }

    .list a.item {
      opacity: 1;
      filter: alpha(opacity=100); }

    .list .item.item-bg-image h1, .list .item.item-bg-image h2, .list .item.item-bg-image h3, .list .item.item-bg-image h4, .list .item.item-bg-image h5, .list .item.item-bg-image h6 {
      color: #fff;
      font-weight: bold;
      position: relative;
      text-shadow: 0 0 3px rgba(0, 0, 0, 0.95);
      z-index: 1; }

    .list .item.item-bg-image h2 {
      font-size: 24px; }

    .list .item.item-bg-image h2 {
      font-size: 24px; }

    .list .item.item-bg-image p {
      color: white;
      font-size: 17px;
      position: relative;
      text-shadow: 0 0 4px rgba(0, 0, 0, 0.95);
      z-index: 1; }

    .item-avatar, .item-avatar .item-content, .item-avatar-left, .item-avatar-left .item-content {
      min-height: 80px; }

    /* List: Thumbnails
    ==================================*/
    .item-thumbnail-left, .card > .item.item-thumbnail-left, .item-thumbnail-left .item-content {
      padding-left: 106px; }

    .item-thumbnail-right, .card > .item.item-thumbnail-right, .item-thumbnail-right .item-content {
      padding-right: 106px; }

    /* List: Avatar
    ==================================*/
    .item-avatar > img:first-child, .item-avatar .item-image, .item-avatar .item-content > img:first-child, .item-avatar .item-content .item-image, .item-avatar-left > img:first-child, .item-avatar-left .item-image, .item-avatar-left .item-content > img:first-child, .item-avatar-left .item-content .item-image {
      border-radius: 50%;
      left: 16px;
      max-height: 40px;
      max-width: 40px; }

    /*
    .item-avatar, .list .item-avatar {
        padding-left: 100px;
    }
    */
    .avatar, .item-avatar .avatar {
      background-position: center;
      background-size: cover;
      border-radius: 50%;
      display: inline-block;
      height: 56px;
      left: 16px;
      position: absolute;
      width: 56px; }

    /* List: Gallery
    ==================================*/
    .list.half {
      display: inline-block;
      float: left;
      margin: 0;
      padding: 0;
      width: 50%; }

    .list.half:first-child {
      padding: 16px 8px 16px 16px; }

    .list.half:last-child {
      padding: 16px 16px 16px 8px; }

    .list.half:first-child .card.card-gallery {
      margin-left: 0;
      margin-right: 0; }

    .list.half:last-child .card.card-gallery {
      margin-left: 0;
      margin-right: 0; }

    .list.condensed-space > .card, .list.condensed-space > .item {
      margin: 0px 0px 2px; }

    .list .card.card-gallery {
      display: block;
      float: left;
      margin: 0 0 0 13px;
      padding: 0;
      width: auto; }

    .list.half .item {
      width: 100%; }

    .list.half .item.card {
      margin-bottom: 16px; }

    .list .card.card-gallery.item h2 {
      padding: 12px; }

    .list .item.item-gallery img {
      width: 100%; }

    .item.item-divider {
      border-top: solid 1px rgba(0, 0, 0, 0.12);
      font-size: 14px;
      font-weight: bold;
      height: 48px;
      line-height: 48px;
      color: rgba(0, 0, 0, 0.54); }
      .item.item-divider:first-child {
        border: none; }

    .item-avatar, .item-avatar .item-content, .item-avatar-left, .item-avatar-left .item-content, .card > .item-avatar {
      padding-left: 72px; }

    .item.active, .item.activated, .item-complex.active .item-content, .item-complex.activated .item-content, .item .item-content.active, .item .item-content.activated {
      background-color: transparent; }

    .list-inset {
      margin: 20px 30px;
      border-left: solid 1px #ccc;
      border-radius: 0;
      background-color: #fff; }

    .list .item.item-floating-label,
    .item-floating-label {
      border-bottom: solid 1px #ccc; }

    .loader {
      position: relative;
      margin: 0px auto;
      width: 100px;
      height: 100px;
      zoom: 1.7; }

    .circular {
      -webkit-animation: rotate 2s linear infinite;
      animation: rotate 2s linear infinite;
      height: 100px;
      position: relative;
      width: 100px; }

    .path {
      stroke-dasharray: 1,200;
      stroke-dashoffset: 0;
      -webkit-animation: dash 1.5s ease-in-out infinite, color 6s ease-in-out infinite;
      animation: dash 1.5s ease-in-out infinite, color 6s ease-in-out infinite;
      stroke-linecap: round; }

    @-webkit-keyframes rotate {
      100% {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg); } }

    @keyframes rotate {
      100% {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg); } }

    @-webkit-keyframes dash {
      0% {
        stroke-dasharray: 1,200;
        stroke-dashoffset: 0; }
      50% {
        stroke-dasharray: 89,200;
        stroke-dashoffset: -35; }
      100% {
        stroke-dasharray: 89,200;
        stroke-dashoffset: -124; } }

    @keyframes dash {
      0% {
        stroke-dasharray: 1,200;
        stroke-dashoffset: 0; }
      50% {
        stroke-dasharray: 89,200;
        stroke-dashoffset: -35; }
      100% {
        stroke-dasharray: 89,200;
        stroke-dashoffset: -124; } }

    @-webkit-keyframes color {
      100%, 0% {
        stroke: #d62d20; }
      40% {
        stroke: #0057e7; }
      66% {
        stroke: #008744; }
      80%, 90% {
        stroke: #ffa700; } }

    @keyframes color {
      100%, 0% {
        stroke: #d62d20; }
      40% {
        stroke: #0057e7; }
      66% {
        stroke: #008744; }
      80%, 90% {
        stroke: #ffa700; } }

    /* Layouts: Login
    ==================================*/
    .login {
      background-position: 25% 25%;
      background-size: 180% 180%;
      height: 100%;
      -webkit-transition: all 1.5s ease-in-out;
      transition: all 1.5s ease-in-out; }

    .login .item {
      margin: 0 12px;
      padding-left: 0;
      padding-right: 0;
      width: initial; }

    .login .button-bar {
      bottom: 0;
      margin: 28px 12px 0;
      width: initial; }

    .login .light-bg {
      background-color: #fff; }

    .icon.hero-icon:before, .hero-icon.icon-help:before, .hero-icon.icon-alert:before, #menu .footer .hero-icon.icon-help:before {
      line-height: 130px; }

    /* Mask
    ==================================*/
    .hero.has-mask:after, .item.has-mask:after, .card.has-mask:after {
      content: '';
      background: -webkit-linear-gradient(top, transparent 0%, rgba(0, 0, 0, 0.6) 100%);
      height: 100%;
      left: 0;
      position: absolute;
      top: 0;
      z-index: 0;
      width: 100%; }

    .hero.has-mask-reverse:after, .item.has-mask-reverse:after, .card.has-mask-reverse:after {
      content: '';
      background: -webkit-linear-gradient(top, rgba(0, 0, 0, 0.6) 0%, transparent 100%);
      height: 100%;
      left: 0;
      position: absolute;
      top: 0;
      z-index: 0;
      width: 100%; }

    /* Menu */
    .menu-bottom {
      bottom: 16px;
      left: 16px;
      right: 16px;
      position: absolute; }

    .menu-top {
      top: 16px;
      left: 16px;
      right: 16px;
      position: absolute; }

    .menu .avatar {
      top: 16px;
      left: 16px;
      height: 65px;
      width: 65px; }

    .menu .bar.bar-header.expanded {
      box-shadow: none;
      min-height: 150px;
      color: #fff; }

    .menu-open .bar.bar-header.expanded {
      background-position: 0;
      background-size: 100%; }

    .has-expanded-header {
      top: 150px !important; }

    .motion {
      -webkit-transition: all 0.5s ease-out;
      transition: all 0.5s ease-out; }

    .fade {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spin-back {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(360deg) scale(0) !important;
      transform: translateZ(0) rotate(360deg) scale(0) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spiral {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(-360deg) scale(0) translate(-120px) !important;
      transform: translateZ(0) rotate(-360deg) scale(0) translate(-120px) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .spiral-back {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(360deg) scale(0) translate(120px) !important;
      transform: translateZ(0) rotate(360deg) scale(0) translate(120px) !important;
      -webkit-transition: all 0.1s ease-out !important;
      transition: all 0.1s ease-out !important; }

    .menu-open .avatar {
      opacity: 1;
      filter: alpha(opacity=100);
      -webkit-transform: translateZ(0) rotate(0) scale(1) !important;
      transform: translateZ(0) rotate(0) scale(1) !important;
      -webkit-transition: all 0.3s ease-out !important;
      transition: all 0.3s ease-out !important; }

    .spin {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotate(0) scale(0) !important;
      transform: translateZ(0) rotate(0) scale(0) !important;
      -webkit-transition: all 0.3s ease-out !important;
      transition: all 0.3s ease-out !important; }

    .spin.on {
      -webkit-transform: translateZ(0) rotate(-360deg) scale(1) !important;
      transform: translateZ(0) rotate(-360deg) scale(1) !important; }

    .flap {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotateX(0) scale(0) translate(-120px) !important;
      transform: translateZ(0) rotateX(0) scale(0) translate(-120px) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flap.on {
      -webkit-transform: translateZ(0) rotateX(-720deg) scale(1) translate(0) !important;
      transform: translateZ(0) rotateX(-720deg) scale(1) translate(0) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .drop {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) scale(3) !important;
      transform: translateZ(0) scale(3) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .drop.on {
      -webkit-transform: translateZ(0) scale(1) !important;
      transform: translateZ(0) scale(1) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flip {
      -webkit-backface-visibility: hidden !important;
      backface-visibility: hidden !important;
      -webkit-transform: translateZ(0) rotateY(0) scale(0) !important;
      transform: translateZ(0) rotateY(0) scale(0) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    .flip.on {
      -webkit-transform: translateZ(0) rotateY(-720deg) scale(1) !important;
      transform: translateZ(0) rotateY(-720deg) scale(1) !important;
      -webkit-transition: all 0.5s ease-out !important;
      transition: all 0.5s ease-out !important; }

    /* Utilities
    ==================================*/
    .bold {
      font-weight: bold; }

    .static {
      position: static; }

    .pull-left, .popover-helptip .icon.icon-left, .popover-helptip .icon-left.icon-help, .popover-helptip .icon-left.icon-alert, .popover-helptip #menu .footer .icon-left.icon-help, #menu .footer .popover-helptip .icon-left.icon-help, .popover-helptip .icon.icon-bottom-left, .popover-helptip .icon-bottom-left.icon-help, .popover-helptip .icon-bottom-left.icon-alert, .popover-helptip #menu .footer .icon-bottom-left.icon-help, #menu .footer .popover-helptip .icon-bottom-left.icon-help {
      float: left; }

    .pull-right, .popover-helptip .icon.icon-right, .popover-helptip .icon-right.icon-help, .popover-helptip .icon-right.icon-alert, .popover-helptip #menu .footer .icon-right.icon-help, #menu .footer .popover-helptip .icon-right.icon-help, .popover-helptip .icon.icon-center, .popover-helptip .icon-center.icon-help, .popover-helptip .icon-center.icon-alert, .popover-helptip #menu .footer .icon-center.icon-help, #menu .footer .popover-helptip .icon-center.icon-help, .popover-helptip .icon.icon-bottom-right, .popover-helptip .icon-bottom-right.icon-help, .popover-helptip .icon-bottom-right.icon-alert, .popover-helptip #menu .footer .icon-bottom-right.icon-help, #menu .footer .popover-helptip .icon-bottom-right.icon-help, .popover-helptip .icon.icon-bottom-center, .popover-helptip .icon-bottom-center.icon-help, .popover-helptip .icon-bottom-center.icon-alert, .popover-helptip #menu .footer .icon-bottom-center.icon-help, #menu .footer .popover-helptip .icon-bottom-center.icon-help {
      float: right; }

    .double-padding, .ionic-content.double-padding {
      padding: 16px; }

    .double-padding-x {
      padding-left: 16px;
      padding-right: 16px; }

    .double-padding-y {
      padding-top: 16px;
      padding-bottom: 16px; }

    .outline {
      border-style: solid;
      border-width: 1px; }

    .border-top {
      border-top: solid 1px #ccc;
      padding-top: 30px; }

    .no-border {
      border: none; }

    .circle {
      border-radius: 50%; }

    .no-padding, .list.no-padding, .bar.no-padding, .button-bar.no-padding, .card.no-padding, .button.no-padding, .item.no-padding {
      padding: 0; }

    .flat, .flat.tabs, .flat.button, .flat.button.icon, .flat.button.icon-help, .flat.button.icon-alert, #menu .footer .flat.button.icon-help, .flat.hero {
      box-shadow: none;
      -webkit-box-shadow: none; }

    /* Utilities : Padding
    ==================================*/
    .im-wrapper, .padding, .item.large-button-bar {
      padding: 16px !important; }

    .padding-bottom, .popover-share .bar-footer .button-close {
      padding-bottom: 16px !important; }

    .padding-top {
      padding-top: 16px !important; }

    .padding-left {
      padding-left: 16px !important; }

    .padding-right, .popover-share .bar-footer .button-close {
      padding-right: 16px !important; }

    .no-padding-bottom {
      padding-bottom: 0 !important; }

    .no-padding-top {
      padding-top: 0 !important; }

    .no-padding-left {
      padding-left: 0 !important; }

    .no-padding-right {
      padding-right: 0 !important; }

    /* Utilities : Depth
    ==================================*/
    .z1 {
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26); }

    /* Utilities : Color
    ==================================*/
    .bar.bar-positive.darker {
      background-color: #164FAB; }

    /* TODO: Expand to other colors */
    .bar.bar-positive.dark-positive-bg {
      background-color: #2C5CAD; }

    /* TODO: Expand to other colors */
    .muted {
      color: #C3C3C3; }

    .clear-bg {
      background: transparent; }

    /* Motion: Blinds
    ==================================*/
    .animate-blinds .item,
    .animate-blinds .item {
      visibility: hidden; }

    .animate-blinds .item,
    .animate-blinds .item {
      -ms-transform: scale3d(0.8, 0, 1);
      -webkit-transform: scale3d(0.8, 0, 1);
      transform: scale3d(0.8, 0, 1);
      -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.3s cubic-bezier(0.55, 0, 0.1, 1); }

    .animate-blinds .item-bg-image > img.background,
    .animate-blinds .item-bg-image > img.background {
      box-shadow: none;
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1); }

    .animate-blinds .in, .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-blinds .expanded.item, .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-blinds .expanded.item, .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded .animate-blinds .item, .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds .card.card-comment,
    .animate-blinds.done > *, .animate-fade-slide-in .expanded .animate-blinds.item > *,
    .animate-fade-slide-in .animate-blinds.expanded.item > *,
    .animate-fade-slide-in-right .expanded .animate-blinds.item > *,
    .animate-fade-slide-in-right .animate-blinds.expanded.item > *,
    .animate-ripple .expanded .animate-blinds.item > *,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment > *,
    .animate-blinds .in,
    .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-blinds .card.card-comment,
    .animate-blinds.done > *, .animate-fade-slide-in .expanded .animate-blinds.item > *,
    .animate-fade-slide-in .animate-blinds.expanded.item > *,
    .animate-fade-slide-in-right .expanded .animate-blinds.item > *,
    .animate-fade-slide-in-right .animate-blinds.expanded.item > *,
    .animate-ripple .expanded .animate-blinds.item > *,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment > * {
      -ms-transform: translate3d(0, 0, 0);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .animate-blinds .in, .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-blinds .expanded.item, .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-blinds .expanded.item, .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded .animate-blinds .item, .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds .card.card-comment,
    .animate-blinds.done .item, .animate-fade-slide-in .expanded .animate-blinds.item .item,
    .animate-fade-slide-in .animate-blinds.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .item,
    .animate-ripple .expanded .animate-blinds.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .item,
    .animate-blinds .in,
    .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-blinds .card.card-comment,
    .animate-blinds.done .item, .animate-fade-slide-in .expanded .animate-blinds.item .item,
    .animate-fade-slide-in .animate-blinds.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .item,
    .animate-ripple .expanded .animate-blinds.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .item {
      visibility: visible; }

    .animate-blinds .item,
    .animate-blinds .item {
      visibility: hidden; }

    .animate-blinds .item,
    .animate-blinds .item {
      opacity: 0;
      filter: alpha(opacity=0); }

    .animate-blinds .in, .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-blinds .expanded.item, .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-blinds .expanded.item, .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded .animate-blinds .item, .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds .card.card-comment,
    .animate-blinds.done,
    .animate-fade-slide-in .expanded .animate-blinds.item,
    .animate-fade-slide-in .animate-blinds.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item,
    .animate-ripple .expanded .animate-blinds.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment,
    .animate-blinds .in,
    .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-blinds .card.card-comment,
    .animate-blinds.done,
    .animate-fade-slide-in .expanded .animate-blinds.item,
    .animate-fade-slide-in .animate-blinds.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item,
    .animate-ripple .expanded .animate-blinds.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment {
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out;
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-blinds .in, .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-blinds .expanded.item, .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded .animate-blinds .item, .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-blinds .expanded.item, .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded .animate-blinds .item, .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds .card.card-comment,
    .animate-blinds.done,
    .animate-fade-slide-in .expanded .animate-blinds.item,
    .animate-fade-slide-in .animate-blinds.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item,
    .animate-ripple .expanded .animate-blinds.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment,
    .animate-blinds .in,
    .animate-blinds .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-blinds .expanded.item,
    .animate-blinds
    .animate-ripple .expanded .item,
    .animate-ripple .expanded
    .animate-blinds .item,
    .animate-blinds
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-blinds .card.card-comment,
    .animate-blinds.done,
    .animate-fade-slide-in .expanded .animate-blinds.item,
    .animate-fade-slide-in .animate-blinds.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-blinds.item,
    .animate-fade-slide-in-right .animate-blinds.expanded.item,
    .animate-ripple .expanded .animate-blinds.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment {
      visibility: visible; }

    .animate-blinds.done .in, .animate-fade-slide-in .expanded .animate-blinds.item .in,
    .animate-fade-slide-in .animate-blinds.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .in,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .in,
    .animate-ripple .expanded .animate-blinds.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .in, .animate-blinds.done .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded .animate-blinds.done .item, .animate-fade-slide-in .expanded .animate-blinds.item .item, .animate-fade-slide-in .animate-blinds.expanded.item .item, .animate-blinds.done
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-blinds.done .expanded.item, .animate-blinds.done
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded .animate-blinds.done .item, .animate-fade-slide-in-right .expanded .animate-blinds.item .item, .animate-fade-slide-in-right .animate-blinds.expanded.item .item, .animate-blinds.done
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-blinds.done .expanded.item, .animate-blinds.done
    .animate-ripple .expanded .item,
    .animate-ripple .expanded .animate-blinds.done .item, .animate-ripple .expanded .animate-blinds.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .item, .animate-blinds.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.done .card.card-comment,
    .animate-fade-slide-in
    .animate-ripple .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in-right
    .animate-ripple .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in-right .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-blinds.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .card.card-comment,
    .animate-blinds.done .in, .animate-fade-slide-in .expanded .animate-blinds.item .in,
    .animate-fade-slide-in .animate-blinds.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .in,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .in,
    .animate-ripple .expanded .animate-blinds.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .in,
    .animate-blinds.done .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded
    .animate-blinds.done .item, .animate-fade-slide-in .expanded .animate-blinds.item .item, .animate-fade-slide-in .animate-blinds.expanded.item .item,
    .animate-blinds.done
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-blinds.done .expanded.item,
    .animate-blinds.done
    .animate-fade-slide-in-right .expanded .item,
    .animate-fade-slide-in-right .expanded
    .animate-blinds.done .item, .animate-fade-slide-in-right .expanded .animate-blinds.item .item, .animate-fade-slide-in-right .animate-blinds.expanded.item .item,
    .animate-blinds.done
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-blinds.done .expanded.item,
    .animate-blinds.done
    .animate-ripple .expanded .item,
    .animate-ripple .expanded
    .animate-blinds.done .item, .animate-ripple .expanded .animate-blinds.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .item,
    .animate-blinds.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-blinds.done .card.card-comment,
    .animate-fade-slide-in
    .animate-ripple .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in-right
    .animate-ripple .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in-right .animate-blinds.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-blinds.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .card.card-comment {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-blinds .has-mask-reverse:after,
    .animate-blinds .has-mask-reverse:after {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out; }

    .animate-blinds.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-blinds.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .has-mask-reverse:after,
    .animate-blinds.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-blinds.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-blinds.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-blinds.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-blinds.card.card-comment .has-mask-reverse:after {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-blinds .out,
    .animate-blinds .out {
      -ms-transform: scale3d(0, 0, 1);
      -webkit-transform: scale3d(0, 0, 1);
      transform: scale3d(0, 0, 1); }

    /* Motion: Pan In Left
    ==================================*/
    .animate-pan-in-left,
    .animate-pan-in-left {
      background-position: 0% 0%; }

    /* Motion: Ripple
    ==================================*/
    .animate-ripple .done, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .done, .animate-fade-slide-in .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment {
      visibility: hidden; }

    .animate-ripple .done, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .done, .animate-fade-slide-in .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment {
      -ms-transform: scale3d(0.8, 0, 1);
      -webkit-transform: scale3d(0.8, 0, 1);
      transform: scale3d(0.8, 0, 1);
      -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.3s cubic-bezier(0.55, 0, 0.1, 1); }

    .animate-ripple .item-bg-image img.background,
    .animate-ripple .item-bg-image img.background {
      box-shadow: none;
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1); }

    .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment,
    .animate-ripple .in, .animate-fade-slide-in .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment {
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1); }

    .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment,
    .animate-ripple .in, .animate-fade-slide-in .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment {
      visibility: visible; }

    .animate-ripple .item {
      -ms-transform: scale3d(0, 0, 1);
      -webkit-transform: scale3d(0, 0, 1);
      transform: scale3d(0, 0, 1);
      opacity: 0;
      filter: alpha(opacity=0); }

    .animate-ripple .item.in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .item.expanded,
    .animate-fade-slide-in .animate-ripple .item.expanded,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .item.expanded,
    .animate-fade-slide-in-right .animate-ripple .item.expanded, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .item.card.card-comment {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-ripple .done, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment {
      visibility: hidden; }

    .animate-ripple .done, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .done, .animate-fade-slide-in .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-ripple .item,
    .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment {
      -ms-transform: scale3d(0.8, 0, 1);
      -webkit-transform: scale3d(0.8, 0, 1);
      transform: scale3d(0.8, 0, 1);
      -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.3s cubic-bezier(0.55, 0, 0.1, 1); }

    /* Uncomment if you want images to fade in after the card

      .animate-ripple .in .item-bg-image img:last-child,
      .animate-ripple .in .item-bg-image img:last-child {
        opacity: 0;
      }

        .animate-ripple.done .item-bg-image img:last-child,
        .animate-ripple.done .item-bg-image img:last-child {
        opacity: 1;
        -moz-transition: all 1s ease-in-out;
        -o-transition: all 1s ease-in-out;
        -webkit-transition: all 1s ease-in-out;
        transition: all 1s ease-in-out;
        }

        .animate-ripple .item-bg-image img:last-child,
      .animate-ripple .item-bg-image img:last-child {
        box-shadow: none;
        -moz-transform: scale3d(1, 1, 1);
        -ms-transform: scale3d(1, 1, 1);
        -webkit-transform: scale3d(1, 1, 1);
        transform: scale3d(1, 1, 1);
      }
      .animate-ripple .in .item-bg-image img:last-child,
      .animate-ripple .in .item-bg-image img:last-child {
        opacity: 0;
      }

    .animate-ripple.done .item-bg-image img:last-child,
    .animate-ripple.done .item-bg-image img:last-child {
      opacity: 1;
        -moz-transition: all 0.3s ease-in-out;
        -o-transition: all 0.3s ease-in-out;
        -webkit-transition: all 0.3s ease-in-out;
        transition: all 0.3s ease-in-out;
    }

    .animate-ripple .in,
    .animate-ripple .in {
        opacity: 0.6;
    }
    */
    .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment, .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment {
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out; }

    .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment, .animate-ripple .in, .animate-fade-slide-in .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple .item, .animate-ripple
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple .expanded.item, .animate-ripple .expanded .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple.done, .animate-fade-slide-in .expanded .animate-ripple.item,
    .animate-fade-slide-in .animate-ripple.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.item,
    .animate-fade-slide-in-right .animate-ripple.expanded.item,
    .animate-ripple .expanded .animate-ripple.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment {
      visibility: visible; }

    .animate-ripple.done .in, .animate-fade-slide-in .expanded .animate-ripple.item .in,
    .animate-fade-slide-in .animate-ripple.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-ripple.item .in,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .in,
    .animate-ripple .expanded .animate-ripple.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .in, .animate-fade-slide-in .expanded .animate-ripple.done .item, .animate-fade-slide-in .expanded .animate-ripple.item .item, .animate-fade-slide-in .animate-ripple.expanded.item .item, .animate-ripple.done
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple.done .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.done .item, .animate-fade-slide-in-right .expanded .animate-ripple.item .item, .animate-fade-slide-in-right .animate-ripple.expanded.item .item, .animate-ripple.done
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple.done .expanded.item, .animate-ripple.done .expanded .item, .animate-ripple .expanded .animate-ripple.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .item, .animate-ripple.done .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in .animate-ripple.expanded.item .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-ripple.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .card.card-comment, .animate-ripple.done .in, .animate-fade-slide-in .expanded .animate-ripple.item .in,
    .animate-fade-slide-in .animate-ripple.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-ripple.item .in,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .in,
    .animate-ripple .expanded .animate-ripple.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .in, .animate-fade-slide-in .expanded .animate-ripple.done .item, .animate-fade-slide-in .expanded .animate-ripple.item .item, .animate-fade-slide-in .animate-ripple.expanded.item .item, .animate-ripple.done
    .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in .animate-ripple.done .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-ripple.done .item, .animate-fade-slide-in-right .expanded .animate-ripple.item .item, .animate-fade-slide-in-right .animate-ripple.expanded.item .item, .animate-ripple.done
    .animate-fade-slide-in-right .expanded.item,
    .animate-fade-slide-in-right .animate-ripple.done .expanded.item, .animate-ripple.done .expanded .item, .animate-ripple .expanded .animate-ripple.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .item, .animate-ripple.done .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in .animate-ripple.expanded.item .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-ripple.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .card.card-comment {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-ripple .has-mask-reverse:after, .animate-ripple .has-mask-reverse:after {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out; }

    .animate-ripple.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-ripple.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .has-mask-reverse:after, .animate-ripple.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-ripple.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-ripple.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-ripple.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-ripple.card.card-comment .has-mask-reverse:after {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-ripple .out, .animate-ripple .out {
      -ms-transform: scale3d(0, 0, 1);
      -webkit-transform: scale3d(0, 0, 1);
      transform: scale3d(0, 0, 1); }

    /* Motion: Slide / Fade In
    ==================================*/
    .animate-fade-slide-in .item,
    .animate-fade-slide-in .item {
      visibility: hidden; }

    .animate-fade-slide-in .item,
    .animate-fade-slide-in .item {
      -ms-transform: scale3d(0.8, 0, 1);
      -webkit-transform: scale3d(0.8, 0, 1);
      transform: scale3d(0.8, 0, 1);
      -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.3s cubic-bezier(0.55, 0, 0.1, 1); }

    .animate-fade-slide-in .item-bg-image img.background,
    .animate-fade-slide-in .item-bg-image img.background {
      box-shadow: none;
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1); }

    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in .item,
    .animate-ripple .expanded .animate-fade-slide-in .item, .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item,
    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-fade-slide-in .item,
    .animate-ripple .expanded
    .animate-fade-slide-in .item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item {
      -ms-transform: translate3d(0, 0, 0);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in .item,
    .animate-ripple .expanded .animate-fade-slide-in .item, .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item,
    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-fade-slide-in .item,
    .animate-ripple .expanded
    .animate-fade-slide-in .item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item {
      visibility: visible; }

    .list .item.item-bg-image,
    .list .item.item-bg-image {
      max-height: 150px; }

    .animate-fade-slide-in .item,
    .animate-fade-slide-in .item {
      visibility: hidden; }

    .animate-fade-slide-in .item,
    .animate-fade-slide-in .item {
      -ms-transform: translate3d(-250px, 250px, 0);
      -webkit-transform: translate3d(-250px, 250px, 0);
      transform: translate3d(-250px, 250px, 0);
      -webkit-transition: -webkit-transform 0.5s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.5s cubic-bezier(0.55, 0, 0.1, 1);
      opacity: 0;
      filter: alpha(opacity=0); }

    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in .item,
    .animate-ripple .expanded .animate-fade-slide-in .item, .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment,
    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-fade-slide-in .item,
    .animate-ripple .expanded
    .animate-fade-slide-in .item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment {
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
      -webkit-transition: all 0.5s ease-in-out;
      transition: all 0.5s ease-in-out;
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in .item,
    .animate-ripple .expanded .animate-fade-slide-in .item, .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment,
    .animate-fade-slide-in .in, .animate-fade-slide-in .expanded .item, .animate-fade-slide-in .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-fade-slide-in .item,
    .animate-ripple .expanded
    .animate-fade-slide-in .item,
    .animate-fade-slide-in
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in .card.card-comment,
    .animate-fade-slide-in.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment {
      visibility: visible; }

    .animate-fade-slide-in.done .in, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .in,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .in,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .in,
    .animate-ripple .expanded .animate-fade-slide-in.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .in, .animate-fade-slide-in.done .expanded .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item, .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item, .animate-fade-slide-in.done .expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.done .item, .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item, .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in.done .item, .animate-ripple .expanded .animate-fade-slide-in.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item, .animate-fade-slide-in.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.done .card.card-comment,
    .animate-ripple .animate-fade-slide-in.expanded.item .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.expanded.item .card.card-comment,
    .animate-fade-slide-in-right
    .animate-ripple .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-fade-slide-in.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .card.card-comment,
    .animate-fade-slide-in.done .in, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .in,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .in,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .in,
    .animate-ripple .expanded .animate-fade-slide-in.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .in, .animate-fade-slide-in.done .expanded .item, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .item, .animate-fade-slide-in .animate-fade-slide-in.expanded.item .item, .animate-fade-slide-in.done .expanded.item,
    .animate-fade-slide-in-right .expanded
    .animate-fade-slide-in.done .item, .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .item, .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .item,
    .animate-ripple .expanded
    .animate-fade-slide-in.done .item, .animate-ripple .expanded .animate-fade-slide-in.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .item,
    .animate-fade-slide-in.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in.done .card.card-comment,
    .animate-ripple .animate-fade-slide-in.expanded.item .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.expanded.item .card.card-comment,
    .animate-fade-slide-in-right
    .animate-ripple .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-fade-slide-in.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .card.card-comment {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in .has-mask-reverse:after,
    .animate-fade-slide-in .has-mask-reverse:after {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out; }

    .animate-fade-slide-in.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .has-mask-reverse:after,
    .animate-fade-slide-in.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-fade-slide-in.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-fade-slide-in.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-fade-slide-in.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in.card.card-comment .has-mask-reverse:after {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in .out,
    .animate-fade-slide-in .out {
      -ms-transform: scale3d(0, 0, 1);
      -webkit-transform: scale3d(0, 0, 1);
      transform: scale3d(0, 0, 1); }

    /* Motion: Slide In Right
    ==================================*/
    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right .item {
      visibility: hidden; }

    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right .item {
      -ms-transform: scale3d(0.8, 0, 1);
      -webkit-transform: scale3d(0.8, 0, 1);
      transform: scale3d(0.8, 0, 1);
      -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.3s cubic-bezier(0.55, 0, 0.1, 1); }

    .animate-fade-slide-in-right .item-bg-image > img.background,
    .animate-fade-slide-in-right .item-bg-image > img.background {
      box-shadow: none;
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1); }

    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done > *, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item > *,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item > *,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item > *,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item > *,
    .animate-ripple .expanded .animate-fade-slide-in-right.item > *,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment > *,
    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded
    .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded
    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done > *, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item > *,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item > *,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item > *,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item > *,
    .animate-ripple .expanded .animate-fade-slide-in-right.item > *,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment > * {
      -ms-transform: translate3d(0, 0, 0);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .item,
    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded
    .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded
    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .item {
      visibility: visible; }

    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right .item {
      visibility: hidden; }

    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right .item {
      -ms-transform: translate3d(250px, 250px, 0);
      -webkit-transform: translate3d(250px, 250px, 0);
      transform: translate3d(250px, 250px, 0);
      -webkit-transition: -webkit-transform 0.5s cubic-bezier(0.55, 0, 0.1, 1);
      transition: transform 0.5s cubic-bezier(0.55, 0, 0.1, 1);
      opacity: 0;
      filter: alpha(opacity=0); }

    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment,
    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded
    .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded
    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment {
      -ms-transform: scale3d(1, 1, 1);
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out;
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right .item, .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment,
    .animate-fade-slide-in-right .in, .animate-fade-slide-in .expanded
    .animate-fade-slide-in-right .item, .animate-fade-slide-in-right .expanded .item, .animate-fade-slide-in-right .expanded.item,
    .animate-ripple .expanded
    .animate-fade-slide-in-right .item,
    .animate-fade-slide-in-right
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in-right .card.card-comment,
    .animate-fade-slide-in-right.done,
    .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right.item,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment {
      visibility: visible; }

    .animate-fade-slide-in-right.done .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .in,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .in,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .in,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .item, .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .item, .animate-fade-slide-in-right.done .expanded .item, .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .item, .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .item, .animate-fade-slide-in-right.done .expanded.item,
    .animate-ripple .expanded .animate-fade-slide-in-right.done .item, .animate-ripple .expanded .animate-fade-slide-in-right.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .item, .animate-fade-slide-in-right.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.done .card.card-comment,
    .animate-fade-slide-in
    .animate-ripple .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in-right.expanded.item .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.expanded.item .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-fade-slide-in-right.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .card.card-comment,
    .animate-fade-slide-in-right.done .in, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .in,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .in,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .in,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .in,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .in,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .in, .animate-fade-slide-in .expanded
    .animate-fade-slide-in-right.done .item, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .item, .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .item, .animate-fade-slide-in-right.done .expanded .item, .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .item, .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .item, .animate-fade-slide-in-right.done .expanded.item,
    .animate-ripple .expanded
    .animate-fade-slide-in-right.done .item, .animate-ripple .expanded .animate-fade-slide-in-right.item .item, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .item,
    .animate-fade-slide-in-right.done
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded
    .animate-fade-slide-in-right.done .card.card-comment,
    .animate-fade-slide-in
    .animate-ripple .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in-right.expanded.item .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment,
    .animate-ripple .animate-fade-slide-in-right.expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.expanded.item .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .animate-fade-slide-in-right.item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .card.card-comment, .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .card.card-comment {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in-right .has-mask-reverse:after,
    .animate-fade-slide-in-right .has-mask-reverse:after {
      opacity: 0;
      filter: alpha(opacity=0);
      -webkit-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out; }

    .animate-fade-slide-in-right.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .has-mask-reverse:after,
    .animate-fade-slide-in-right.done .has-mask-reverse:after, .animate-fade-slide-in .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-fade-slide-in .animate-fade-slide-in-right.expanded.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-fade-slide-in-right .animate-fade-slide-in-right.expanded.item .has-mask-reverse:after,
    .animate-ripple .expanded .animate-fade-slide-in-right.item .has-mask-reverse:after,
    .animate-ripple .expanded.item .card-avatar-small.expanded .item .card-avatar-small .expanded .item .card-avatar-small.expanded .card.card-comment .card-avatar-small .expanded .animate-fade-slide-in-right.card.card-comment .has-mask-reverse:after {
      opacity: 1;
      filter: alpha(opacity=100); }

    .animate-fade-slide-in-right .out,
    .animate-fade-slide-in-right .out {
      -ms-transform: scale3d(0, 0, 1);
      -webkit-transform: scale3d(0, 0, 1);
      transform: scale3d(0, 0, 1); }

    /* Motion: Slide Up
    ==================================*/
    .slide-up,
    .slide-up,
    .hero.slide-up {
      height: 100%;
      overflow: hidden;
      text-align: center; }

    .slide-up {
      -webkit-transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      transition: all 1s cubic-bezier(0.55, 0, 0.1, 1);
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0); }

    .slide-up *,
    .slide-up *,
    .hero.slide-up * {
      opacity: 0;
      filter: alpha(opacity=0); }

    .hero.slide-up + .mid-bar,
    .slide-up + .mid-bar,
    .slide-up + .mid-bar {
      height: 100%;
      opacity: 0.7;
      filter: alpha(opacity=70);
      -webkit-transform: translate3d(100%, -240px, 0);
      transform: translate3d(100%, -240px, 0); }

    /*!
     * Waves v0.5.4
     * http://fian.my.id/Waves
     *
     * Copyright 2014 Alfiana E. Sibuea and other contributors
     * Forked by Zach Fitzgerald and other contributors for Ionic Material
     *
     * Released under the MIT license
     * https://github.com/fians/Waves/blob/master/LICENSE
     *
     */
    .ink, .button-fab, .button-flat, .button-raised, .button-clear, .button-text, .popup .button {
      position: relative;
      cursor: pointer;
      /*display: inline-block;*/
      overflow: hidden;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      -webkit-tap-highlight-color: transparent;
      -webkit-transition: all 0.3s ease-out;
      -moz-transition: all 0.3s ease-out;
      -o-transition: all 0.3s ease-out;
      transition: all 0.3s ease-out; }

    .ink-ripple {
      position: absolute;
      border-radius: 50%;
      width: 100px;
      height: 100px;
      margin-top: -50px;
      margin-left: -50px;
      opacity: 0;
      background-color: rgba(255, 255, 255, 0.4);
      -webkit-transition: all 0.5s ease-out;
      -moz-transition: all 0.5s ease-out;
      -o-transition: all 0.5s ease-out;
      transition: all 0.5s ease-out;
      -webkit-transition-property: -webkit-transform, opacity;
      -moz-transition-property: -moz-transform, opacity;
      -o-transition-property: -o-transform, opacity;
      transition-property: transform, opacity;
      -webkit-transform: scale(0);
      -moz-transform: scale(0);
      -ms-transform: scale(0);
      -o-transform: scale(0);
      transform: scale(0);
      pointer-events: none; }

    .ink-notransition {
      -webkit-transition: none !important;
      -moz-transition: none !important;
      -o-transition: none !important;
      transition: none !important; }

    .button-fab,
    .button-flat,
    .button-clear,
    .button-text,
    .button-raised,
    .ink-button,
    .ink-circle {
      -webkit-transform: translateZ(0);
      -moz-transform: translateZ(0);
      -ms-transform: translateZ(0);
      -o-transform: translateZ(0);
      transform: translateZ(0); }

    .button-fab.activated,
    .button-flat.activated,
    .button-raised.activated,
    .button-clear.activated,
    .activated.button-text,
    .ink-button.activated,
    .ink.activated,
    .ink-circle.activated,
    .popup .button.activated,
    .button-fab:active,
    .button-flat:active,
    .button-raised:active,
    .button-clear:active,
    .button-text:active,
    .ink-button:active,
    .ink:active,
    .ink-circle:active,
    .popup .button:active {
      -webkit-mask-image: -webkit-radial-gradient(circle, #ffffff 100%, #000000 100%); }

    .ink-button,
    .ink-button:visited,
    .ink-button:link,
    .button-fab,
    .button-fab:visited,
    .button-fab:link,
    .button-flat,
    .button-flat:visited,
    .button-flat:link,
    .button-raised,
    .button-raised:visited,
    .button-raised:link,
    .button-clear,
    .button-text,
    .button-clear:visited,
    .button-text:visited,
    .button-clear:link,
    .button-text:link {
      white-space: nowrap;
      vertical-align: middle;
      cursor: pointer;
      border: none;
      outline: none;
      /*  color: inherit; */
      /*  background-color: rgba(0, 0, 0, 0); */
      font-size: 14px;
      text-align: center;
      text-decoration: none;
      z-index: 1; }


    /* Ionic Overrides
    ==================================*/
    * {
      font-family: "RobotoDraft","Roboto","Helvetica Neue", "Segoe UI", sans-serif; }

    .rounded {
      border-radius: 4px; }

    a {
      cursor: pointer; }

    .has-header.expanded {
      /* Expanded modifier */
      top: 76px; }

    /* Bar Overrides
    ==================================*/
    .bar {
      border-bottom: none;
      padding: 0; }

    .bar .button {
      min-height: 44px;
      min-width: 44px;
      max-width: 48px;
      margin-bottom: 0;
      max-height: 44px;
      width: 48px; }

    .bar .title + .buttons.buttons-right {
      right: 0;
      top: 0; }

    /* Title Overrides
    ==================================*/
    .title-left,
    .title.title-left {
      left: 48px; }

    .title-right,
    .title.title-right {
      left: 48px; }

    /* Background Colors
    ==================================*/
    .positive-bg,
    .button-positive,
    .button-text,
    .bar .button-positive,
    .bar .button-text,
    .header-positive,
    .button-bar-positive,
    .bar-positive,
    .positive-border,
    .positive-bg:hover,
    .bar .button-positive:hover,
    .bar .button-text:hover,
    .button-positive:hover,
    .button-text:hover,
    .header-positive:hover,
    .button-bar-positive:hover,
    .bar-positive:hover,
    .positive-border:hover,
    .positive-bg:active,
    .bar .button-positive:active,
    .bar .button-text:active,
    .button-positive:active,
    .button-text:active,
    .header-positive:active,
    .button-bar-positive:active,
    .bar-positive:active,
    .positive-border:active,
    .positive-bg.activated,
    .bar .button-positive.activated,
    .bar .activated.button-text,
    .button-positive.activated,
    .activated.button-text,
    .header-positive.activated,
    .button-bar-positive.activated,
    .bar-positive.activated,
    .positive-border.activated {
      background-color: #3F51B5;
      color: #fff; }

    .positive-900-bg,
    .button-positive-900,
    .bar .button-positive-900,
    .header-positive-900,
    .button-bar-positive-900,
    .bar-positive-900,
    .positive-900-border,
    .positive-900-bg:hover,
    .button-positive-900:hover,
    .bar .button-positive-900:hover,
    .header-positive-900:hover,
    .button-bar-positive-900:hover,
    .bar-positive-900:hover,
    .positive-900-border:hover,
    .positive-900-bg:active,
    .bar .button-positive-900:active,
    .button-positive-900:active,
    .header-positive-900:active,
    .button-bar-positive-900:active,
    .bar-positive-900:active,
    .positive-900-border:active,
    .positive-900-bg.activated,
    .button-positive-900.activated,
    .bar .button-positive-900.activated,
    .header-positive-900.activated,
    .button-bar-positive-900.activated,
    .bar-positive-900.activated,
    .positive-900-border.activated {
      background-color: #1A237E;
      color: #fff; }

    .positive-100-bg,
    .button-positive-100,
    .bar .button-positive-100,
    .header-positive-100,
    .button-bar-positive-100,
    .bar-positive-100,
    .positive-100-border,
    .positive-100-bg:hover,
    .button-positive-100:hover,
    .bar .button-positive-100:hover,
    .header-positive-100:hover,
    .button-bar-positive-100:hover,
    .bar-positive-100:hover,
    .positive-100-border:hover,
    .positive-100-bg:active,
    .button-positive-100:active,
    .bar .button-positive-100:active,
    .header-positive-100:active,
    .button-bar-positive-100:active,
    .bar-positive-100:active,
    .positive-100-border:active,
    .positive-100-bg.activated,
    .button-positive-100.activated,
    .bar .button-positive-100.activated,
    .header-positive-100.activated,
    .button-bar-positive-100.activated,
    .bar-positive-100.activated,
    .positive-100-border.activated {
      background-color: #C5CAE9;
      color: #fff; }

    .calm-bg,
    .button-calm,
    .bar .button-calm,
    .header-calm,
    .button-bar-calm,
    .bar-calm,
    .calm-border,
    .calm-bg:hover,
    .button-calm:hover,
    .bar .button-calm:hover,
    .header-calm:hover,
    .button-bar-calm:hover,
    .bar-calm:hover,
    .calm-border:hover,
    .calm-bg:active,
    .button-calm:active,
    .bar .button-calm:active,
    .header-calm:active,
    .button-bar-calm:active,
    .bar-calm:active,
    .calm-border:active,
    .calm-bg.activated,
    .button-calm.activated,
    .bar .button-calm.activated,
    .header-calm.activated,
    .button-bar-calm.activated,
    .bar-calm.activated,
    .calm-border.activated {
      background-color: #2196F3;
      color: #fff; }

    .calm-900-bg,
    .button-calm-900,
    .bar .button-calm-900,
    .header-calm-900,
    .button-bar-calm-900,
    .bar-calm-900,
    .calm-900-border,
    .calm-900-bg:hover,
    .button-calm-900:hover,
    .bar .button-calm-900:hover,
    .header-calm-900:hover,
    .button-bar-calm-900:hover,
    .bar-calm-900:hover,
    .calm-900-border:hover,
    .calm-900-bg:active,
    .button-calm-900:active,
    .bar .button-calm-900:active,
    .header-calm-900:active,
    .button-bar-calm-900:active,
    .bar-calm-900:active,
    .calm-900-border:active,
    .calm-900-bg.activated,
    .button-calm-900.activated,
    .bar .button-calm-900.activated,
    .header-calm-900.activated,
    .button-bar-calm-900.activated,
    .bar-calm-900.activated,
    .calm-900-border.activated {
      background-color: #0D47A1;
      color: #fff; }

    .calm-100-bg,
    .button-calm-100,
    .bar .button-calm-100,
    .header-calm-100,
    .button-bar-calm-100,
    .bar-calm-100,
    .calm-100-border,
    .calm-100-bg:hover,
    .button-calm-100:hover,
    .bar .button-calm-100:hover,
    .header-calm-100:hover,
    .button-bar-calm-100:hover,
    .bar-calm-100:hover,
    .calm-100-border:hover,
    .calm-100-bg:active,
    .button-calm-100:active,
    .bar .button-calm-100:active,
    .header-calm-100:active,
    .button-bar-calm-100:active,
    .bar-calm-100:active,
    .calm-100-border:active,
    .calm-100-bg.activated,
    .button-calm-100.activated,
    .bar .button-calm-100.activated,
    .header-calm-100.activated,
    .button-bar-calm-100.activated,
    .bar-calm-100.activated,
    .calm-100-border.activated {
      background-color: #BBDEFB;
      color: #fff; }

    .royal-bg,
    .button-royal,
    .bar .button-royal,
    .header-royal,
    .button-bar-royal,
    .bar-royal,
    .royal-border,
    .royal-bg:hover,
    .button-royal:hover,
    .bar .button-royal:hover,
    .header-royal:hover,
    .button-bar-royal:hover,
    .bar-royal:hover,
    .royal-border:hover,
    .royal-bg:active,
    .button-royal:active,
    .bar .button-royal:active,
    .header-royal:active,
    .button-bar-royal:active,
    .bar-royal:active,
    .royal-border:active,
    .royal-bg.activated,
    .button-royal.activated,
    .bar .button-royal.activated,
    .header-royal.activated,
    .button-bar-royal.activated,
    .bar-royal.activated,
    .royal-border.activated {
      background-color: #673AB7;
      color: #fff; }

    .royal-900-bg,
    .button-royal-900,
    .bar .button-royal-900,
    .header-royal-900,
    .button-bar-royal-900,
    .bar-royal-900,
    .royal-900-border,
    .royal-900-bg:hover,
    .button-royal-900:hover,
    .bar .button-royal-900:hover,
    .header-royal-900:hover,
    .button-bar-royal-900:hover,
    .bar-royal-900:hover,
    .royal-900-border:hover,
    .royal-900-bg:active,
    .button-royal-900:active,
    .bar .button-royal-900:active,
    .header-royal-900:active,
    .button-bar-royal-900:active,
    .bar-royal-900:active,
    .royal-900-border:active,
    .royal-900-bg.activated,
    .button-royal-900.activated,
    .bar .button-royal-900.activated,
    .header-royal-900.activated,
    .button-bar-royal-900.activated,
    .bar-royal-900.activated,
    .royal-900-border.activated {
      background-color: #311B92;
      color: #fff; }

    .royal-100-bg,
    .button-royal-100,
    .bar .button-royal-100,
    .header-royal-100,
    .button-bar-royal-100,
    .bar-royal-100,
    .royal-100-border,
    .royal-100-bg:hover,
    .button-royal-100:hover,
    .bar .button-royal-100:hover,
    .header-royal-100:hover,
    .button-bar-royal-100:hover,
    .bar-royal-100:hover,
    .royal-100-border:hover,
    .royal-100-bg:active,
    .button-royal-100:active,
    .bar .button-royal-100:active,
    .header-royal-100:active,
    .button-bar-royal-100:active,
    .bar-royal-100:active,
    .royal-100-border:active,
    .royal-100-bg.activated,
    .button-royal-100.activated,
    .bar .button-royal-100.activated,
    .header-royal-100.activated,
    .button-bar-royal-100.activated,
    .bar-royal-100.activated,
    .royal-100-border.activated {
      background-color: #D1C4E9;
      color: #fff; }

    .balanced-bg,
    .button-balanced,
    .bar .button-balanced,
    .header-balanced,
    .button-bar-balanced,
    .bar-balanced,
    .balanced-border,
    .balanced-bg:hover,
    .button-balanced:hover,
    .bar .button-balanced:hover,
    .header-balanced:hover,
    .button-bar-balanced:hover,
    .bar-balanced:hover,
    .balanced-border:hover,
    .balanced-bg:active,
    .button-balanced:active,
    .bar .button-balanced:active,
    .header-balanced:active,
    .button-bar-balanced:active,
    .bar-balanced:active,
    .balanced-border:active,
    .balanced-bg.activated,
    .button-balanced.activated,
    .bar .button-balanced.activated,
    .header-balanced.activated,
    .button-bar-balanced.activated,
    .bar-balanced.activated,
    .balanced-border.activated {
      background-color: #4CAF50;
      color: #fff; }

    .balanced-900-bg,
    .button-balanced-900,
    .bar .button-balanced-900,
    .header-balanced-900,
    .button-bar-balanced-900,
    .bar-balanced-900,
    .balanced-900-border,
    .balanced-900-bg:hover,
    .button-balanced-900:hover,
    .bar .button-balanced-900:hover,
    .header-balanced-900:hover,
    .button-bar-balanced-900:hover,
    .bar-balanced-900:hover,
    .balanced-900-border:hover,
    .balanced-900-bg:active,
    .button-balanced-900:active,
    .bar .button-balanced-900:active,
    .header-balanced-900:active,
    .button-bar-balanced-900:active,
    .bar-balanced-900:active,
    .balanced-900-border:active,
    .balanced-900-bg.activated,
    .button-balanced-900.activated,
    .bar .button-balanced-900.activated,
    .header-balanced-900.activated,
    .button-bar-balanced-900.activated,
    .bar-balanced-900.activated,
    .balanced-900-border.activated {
      background-color: #1B5E20;
      color: #fff; }

    .balanced-100-bg,
    .button-balanced-100,
    .bar .button-balanced-100,
    .header-balanced-100,
    .button-bar-balanced-100,
    .bar-balanced-100,
    .balanced-100-border,
    .balanced-100-bg:hover,
    .button-balanced-100:hover,
    .bar .balanced-100-bg:hover,
    .header-balanced-100:hover,
    .button-bar-balanced-100:hover,
    .bar-balanced-100:hover,
    .balanced-100-border:hover,
    .balanced-100-bg:active,
    .button-balanced-100:active,
    .bar .button-balanced-100:active,
    .header-balanced-100:active,
    .button-bar-balanced-100:active,
    .bar-balanced-100:active,
    .balanced-100-border:active,
    .balanced-100-bg.activated,
    .button-balanced-100.activated,
    .bar .button-balanced-100.activated,
    .header-balanced-100.activated,
    .button-bar-balanced-100.activated,
    .bar-balanced-100.activated,
    .balanced-100-border.activated {
      background-color: #C8E6C9;
      color: #fff; }

    .energized-bg,
    .button-energized,
    .bar .button-energized,
    .header-energized,
    .button-bar-energized,
    .bar-energized,
    .energized-border,
    .energized-bg:hover,
    .button-energized:hover,
    .bar .button-energized:hover,
    .header-energized:hover,
    .button-bar-energized:hover,
    .bar-energized:hover,
    .energized-border:hover,
    .energized-bg:active,
    .button-energized:active,
    .bar .button-energized:active,
    .header-energized:active,
    .button-bar-energized:active,
    .bar-energized:active,
    .energized-border:active,
    .energized-bg.activated,
    .button-energized.activated,
    .bar .button-energized.activated,
    .header-energized.activated,
    .button-bar-energized.activated,
    .bar-energized.activated,
    .energized-border.activated {
      background-color: #FF9800;
      color: #fff; }

    .energized-900-bg,
    .button-energized-900,
    .bar .button-energized-900,
    .header-energized-900,
    .button-bar-energized-900,
    .bar-energized-900,
    .energized-900-border,
    .energized-900-bg:hover,
    .button-energized-900:hover,
    .bar .button-energized-900:hover,
    .header-energized-900:hover,
    .button-bar-energized-900:hover,
    .bar-energized-900:hover,
    .energized-900-border:hover,
    .energized-900-bg:active,
    .button-energized-900:active,
    .bar .button-energized-900:active,
    .header-energized-900:active,
    .button-bar-energized-900:active,
    .bar-energized-900:active,
    .energized-900-border:active,
    .energized-900-bg.activated,
    .button-energized-900.activated,
    .bar .button-energized-900.activated,
    .header-energized-900.activated,
    .button-bar-energized-900.activated,
    .bar-energized-900.activated,
    .energized-900-border.activated {
      background-color: #E65100;
      color: #fff; }

    .energized-100-bg,
    .button-energized-100,
    .bar .button-energized-100,
    .header-energized-100,
    .button-bar-energized-100,
    .bar-energized-100,
    .energized-100-border,
    .energized-100-bg:hover,
    .button-energized-100:hover,
    .bar .button-energized-100:hover,
    .header-energized-100:hover,
    .button-bar-energized-100:hover,
    .bar-energized-100:hover,
    .energized-100-border:hover,
    .energized-100-bg:active,
    .button-energized-100:active,
    .bar .button-energized-100:active,
    .header-energized-100:active,
    .button-bar-energized-100:active,
    .bar-energized-100:active,
    .energized-100-border:active,
    .energized-100-bg.activated,
    .button-energized-100.activated,
    .bar .button-energized-100.activated,
    .header-energized-100.activated,
    .button-bar-energized-100.activated,
    .bar-energized-100.activated,
    .energized-100-border.activated {
      background-color: #FFE0B2; }

    .assertive-bg,
    .button-assertive,
    .bar .button-assertive,
    .header-assertive,
    .button-bar-assertive,
    .bar-assertive,
    .assertive-border,
    .assertive-bg:hover,
    .button-assertive:hover,
    .bar .button-assertive:hover,
    .header-assertive:hover,
    .button-bar-assertive:hover,
    .bar-assertive:hover,
    .assertive-border:hover,
    .assertive-bg:active,
    .button-assertive:active,
    .bar .button-assertive:active,
    .header-assertive:active,
    .button-bar-assertive:active,
    .bar-assertive:active,
    .assertive-border:active,
    .assertive-bg.activated,
    .button-assertive.activated,
    .bar .button-assertive.activated,
    .header-assertive.activated,
    .button-bar-assertive.activated,
    .bar-assertive.activated,
    .assertive-border.activated {
      background-color: #F44336;
      color: #fff; }

    .assertive-900-bg,
    .button-assertive-900,
    .bar .button-assertive-900,
    .header-assertive-900,
    .button-bar-assertive-900,
    .bar-assertive-900,
    .assertive-900-border,
    .assertive-900-bg:hover,
    .button-assertive-900:hover,
    .bar .button-assertive-900:hover,
    .header-assertive-900:hover,
    .button-bar-assertive-900:hover,
    .bar-assertive-900:hover,
    .assertive-900-border:hover,
    .assertive-900-bg:active,
    .button-assertive-900:active,
    .bar .button-assertive-900:active,
    .header-assertive-900:active,
    .button-bar-assertive-900:active,
    .bar-assertive-900:active,
    .assertive-900-border:active,
    .assertive-900-bg.activated,
    .button-assertive-900.activated,
    .bar .button-assertive-900.activated,
    .header-assertive-900.activated,
    .button-bar-assertive-900.activated,
    .bar-assertive-900.activated,
    .assertive-900-border.activated {
      background-color: #B71C1C;
      color: #fff; }

    .assertive-100-bg,
    .button-assertive-100,
    .bar .button-assertive-100,
    .header-assertive-100,
    .button-bar-assertive-100,
    .bar-assertive-100,
    .assertive-100-border,
    .assertive-100-bg:hover,
    .button-assertive-100:hover,
    .bar .button-assertive-100:hover,
    .header-assertive-100:hover,
    .button-bar-assertive-100:hover,
    .bar-assertive-100:hover,
    .assertive-100-border:hover,
    .assertive-100-bg:active,
    .button-assertive-100:active,
    .bar .button-assertive-100:active,
    .header-assertive-100:active,
    .button-bar-assertive-100:active,
    .bar-assertive-100:active,
    .assertive-100-border:active,
    .assertive-100-bg.activated,
    .bar .button-assertive-100.activated,
    .button-assertive-100.activated,
    .header-assertive-100.activated,
    .button-bar-assertive-100.activated,
    .bar-assertive-100.activated,
    .assertive-100-border.activated {
      background-color: #FFCDD2;
      color: #fff; }

    .stable-bg,
    .button-stable,
    .bar .button-stable,
    .header-stable,
    .button-bar-stable,
    .bar-stable,
    .stable-border,
    .stable-bg:hover,
    .button-stable:hover,
    .bar .button-stable:hover,
    .header-stable:hover,
    .button-bar-stable:hover,
    .bar-stable:hover,
    .stable-border:hover,
    .stable-bg:active,
    .button-stable:active,
    .bar .button-stable:active,
    .header-stable:active,
    .button-bar-stable:active,
    .bar-stable:active,
    .stable-border:active,
    .stable-bg.activated,
    .button-stable.activated,
    .bar .button-stable.activated,
    .header-stable.activated,
    .button-bar-stable.activated,
    .bar-stable.activated,
    .stable-border.activated {
      background-color: #E0E0E0;
      color: #fff; }

    /* Text Colors
    ==================================*/
    .positive, .icon-help,
    .positive *, .icon-help *,
    *.positive,
    *.icon-help,
    .positive:hover,
    .icon-help:hover,
    .positive:hover *, .icon-help:hover *,
    *.positive:hover,
    *.icon-help:hover,
    .positive:active,
    .icon-help:active,
    .positive:active *, .icon-help:active *,
    *.positive:active,
    *.icon-help:active {
      color: #3F51B5; }

    .positive-900,
    .positive-900 *,
    *.positive-900,
    .positive-900:hover,
    .positive-900:hover *,
    *.positive-900:hover,
    .positive-900:active,
    .positive-900:active *,
    *.positive-900:active {
      color: #3F51B5; }

    .positive-100,
    .positive-100 *,
    *.positive-100,
    .positive-100:hover,
    .positive-100:hover *,
    *.positive-100:hover,
    .positive-100:active,
    .positive-100:active *,
    *.positive-100:active {
      color: #C5CAE9; }

    .calm-100,
    .calm-100 *,
    *.calm-100,
    .calm-100:hover,
    .calm-100:hover *,
    *.calm-100:hover,
    .calm-100:active,
    .calm-100:active *,
    *.calm-100:active {
      color: #2196F3; }

    .calm-900,
    .calm-900 *,
    *.calm-900,
    .calm-900:hover,
    .calm-900:hover *,
    *.calm-900:hover,
    .calm-900:active,
    .calm-900:active *,
    *.calm-900:active {
      color: #0D47A1; }

    .calm-100,
    .calm-100 *,
    *.calm-100,
    .calm-100:hover,
    .calm-100:hover *,
    *.calm-100:hover,
    .calm-100:active,
    .calm-100:active *,
    *.calm-100:active {
      color: #BBDEFB; }

    .royal,
    .royal *,
    *.royal,
    .royal:hover,
    .royal:hover *,
    *.royal:hover,
    .royal:active,
    .royal:active *,
    *.royal:active {
      color: #673AB7; }

    .royal-900,
    .royal-900 *,
    *.royal-900,
    .royal-900:hover,
    .royal-900:hover *,
    *.royal-900:hover,
    .royal-900:active,
    .royal-900:active *,
    *.royal-900:active {
      color: #311B92; }

    .royal-100,
    .royal-100 *,
    *.royal-100,
    .royal-100:hover,
    .royal-100:hover *,
    *.royal-100:hover,
    .royal-100:active,
    .royal-100:active *,
    *.royal-100:active {
      color: #D1C4E9; }

    .balanced,
    .balanced *,
    *.balanced,
    .balanced:hover,
    .balanced:hover *,
    *.balanced:hover,
    .balanced:active,
    .balanced:active *,
    *.balanced:active {
      color: #4CAF50; }

    .balanced-900,
    .balanced-900 *,
    *.balanced-900,
    .balanced-900:hover,
    .balanced-900:hover *,
    *.balanced-900:hover,
    .balanced-900:active,
    .balanced-900:active *,
    *.balanced-900:active {
      color: #1B5E20; }

    .balanced-100,
    .balanced-100 *,
    *.balanced-100,
    .balanced-100:hover,
    .balanced-100:hover *,
    *.balanced-100:hover,
    .balanced-100:active,
    .balanced-100:active *,
    *.balanced-100:active {
      color: #C8E6C9; }

    .energized,
    .energized *,
    *.energized,
    .energized:hover,
    .energized:hover *,
    *.energized:hover,
    .energized:active,
    .energized:active *,
    *.energized:active {
      color: #FF9800; }

    .energized-900,
    .energized-900 *,
    *.energized-900,
    .energized-900:hover,
    .energized-900:hover *,
    *.energized-900:hover,
    .energized-900:active,
    .energized-900:active *,
    *.energized-900:active {
      color: #E65100; }

    .energized-100,
    .energized-100 *,
    *.energized-100,
    .energized-100:hover,
    .energized-100:hover *,
    *.energized-100:hover,
    .energized-100:active,
    .energized-100:active *,
    *.energized-100:active {
      color: #FFE0B2; }

    .assertive, .icon-alert,
    .assertive *, .icon-alert *,
    *.assertive,
    *.icon-alert,
    .assertive:hover,
    .icon-alert:hover,
    .assertive:hover *, .icon-alert:hover *,
    *.assertive:hover,
    *.icon-alert:hover,
    .assertive:active,
    .icon-alert:active,
    .assertive:active *, .icon-alert:active *,
    *.assertive:active,
    *.icon-alert:active {
      color: #F44336; }

    .assertive-900,
    .assertive-900 *,
    *.assertive-900,
    .assertive-900:hover,
    .assertive-900:hover *,
    *.assertive-900:hover,
    .assertive-900:active,
    .assertive-900:active *,
    *.assertive-900:active {
      color: #B71C1C; }

    .assertive-100,
    .assertive-100 *,
    *.assertive-100,
    .assertive-100:hover,
    .assertive-100:hover *,
    *.assertive-100:hover,
    .assertive-100:active,
    .assertive-100:active *,
    *.assertive-100:active {
      color: #FFCDD2; }

    .stable,
    .stable *,
    *.stable,
    .stable:hover,
    .stable:hover *,
    *.stable:hover,
    .stable:active,
    .stable:active *,
    *.stable:active {
      color: #E0E0E0; }

    .light,
    .light *,
    *.light,
    .light:hover,
    .light:hover *,
    *.light:hover,
    .light:active,
    .light:active *,
    *.light:active {
      color: #fff; }

    .dark,
    .dark *,
    *.dark,
    .dark:hover,
    .dark:hover *,
    *.dark:hover,
    .dark:active,
    .dark:active *,
    *.dark:active {
      color: #444; }

    .light-border {
      border-color: #ddd; }

    .navbar-default .navbar-nav > li > a {
      margin: 0;
      padding-right: 26px;
      padding-left: 26px;
      border-top: 3px solid transparent;
      color: #BFD5C9;
      opacity: 1; }

    /* Mid-Bar
    ==================================*/
    .mid-bar {
      padding: 16px; }

    .mid-bar h1,
    .mid-bar h2,
    .mid-bar h3,
    .mid-bar h4,
    .mid-bar h5,
    .mid-bar h6 {
      color: #fff;
      margin-bottom: 5px; }

    .mid-bar p {
      color: rgba(255, 255, 255, 0.5);
      margin-bottom: 0; }

    /* Item
    ==================================*/
    .item-avatar,
    .item-avatar .item-content,
    .item-avatar-left,
    .item-avatar-left .item-content,
    .card > .item-avatar {
      padding-left: 95px; }

    .item,
    .item-complex .item-content,
    .item-radio .item-content {
      background-color: transparent; }

    .dark-bg h2,
    .item.dark-bg h2 {
      color: #fff; }

    .tabs-striped .tabs {
      box-shadow: 0px 2px 5px 0 rgba(0, 0, 0, 0.26); }

    .bar .button.button-clear, .bar .button.button-text {
      color: #fff; }

    .bar .button.button-icon .icon:before, .bar .button.button-icon .icon-help:before, .bar .button.button-icon .icon-alert:before, .bar .button.button-icon #menu .footer .icon-help:before, #menu .footer .bar .button.button-icon .icon-help:before,
    .bar .button.button-icon.icon-left:before,
    .bar .button.button-icon.icon-right:before,
    .bar .button.button-icon:before {
      vertical-align: top;
      font-size: 24px; }

    .button-icon.button.active,
    .button-icon.button.activated {
      opacity: initial; }

    /* Button
    ==================================*/
    .button {
      overflow: hidden !important; }

    @font-face {
      font-family: "Cesiumicons";
      src: url("../fonts/cesiumicons.eot?v=1.2");
      src: url("../fonts/cesiumicons.eot?v=1.2#iefix") format("embedded-opentype"), url("../fonts/cesiumicons.ttf?v=1.2") format("truetype"), url("../fonts/cesiumicons.woff?v=1.2") format("woff"), url("../fonts/cesiumicons.woff") format("woff"), url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/Pgo8IURPQ1RZUEUgc3ZnIFBVQkxJQyAiLS8vVzNDLy9EVEQgU1ZHIDEuMS8vRU4iICJodHRwOi8vd3d3LnczLm9yZy9HcmFwaGljcy9TVkcvMS4xL0RURC9zdmcxMS5kdGQiID4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPgo8bWV0YWRhdGE+R2VuZXJhdGVkIGJ5IEljb01vb248L21ldGFkYXRhPgo8ZGVmcz4KPGZvbnQgaWQ9ImNlc2l1bWljb25zIiBob3Jpei1hZHYteD0iMTAyNCI+Cjxmb250LWZhY2UgdW5pdHMtcGVyLWVtPSIxMDI0IiBhc2NlbnQ9Ijk2MCIgZGVzY2VudD0iLTY0IiAvPgo8bWlzc2luZy1nbHlwaCBob3Jpei1hZHYteD0iMTAyNCIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeDIwOyIgaG9yaXotYWR2LXg9IjUxMiIgZD0iIiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwMDsiIGQ9Ik03MDQuNjMxIDU3MS4xMjhjLTg4LjYxMy0xNDEuNDA5LTIyMC40MDMtMjYzLjQwOC0zODUuNTIxLTMwNS40NzMtNjcuMjUyLTE4LjQ5My0xMzcuMTEyLTI1LjgyMi0yMDYuNzA1LTI3LjAyMiA4NS41NDktMTYzLjA5NSAyNzUuODEtMjYyLjI3MiA0NTguNTQ1LTIzNy45NSAxODEuMTY2IDE5LjA3NiAzNDEuNjYyIDE1Ny4yMTUgMzg4LjMzOCAzMzMuMTYyLTU0LjYwMSAxMDMuOTI0LTE0MS40OSAxOTcuODc0LTI1NC42NTYgMjM3LjI4M3oiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTAxOyIgZD0iTTcwNS45MzcgNTcyLjA4OGMtMTYuNzU2LTExMS44MTUtODUuMDg2LTIwNy4yLTEwNy4yNjctMzE3LjIyMS05LjE4OC01MS41NDEgMjYuNzg2LTExMC42NDItMTQuNTc5LTE1NS41MTQtNDYuMjEtNTIuNDAyLTExOC45MDctNjUuMzc5LTE4MS45MzgtODcuNDY2IDE3Ny43My00Ny45MSAzNzkuNjYgMjQuMjE1IDQ4NS45ODIgMTc0Ljc4IDUwLjU1IDY5LjExNiA4MC45OTIgMTUyLjY4OSA4Ni42OTYgMjM4LjEyNS00Ni4wODUgOS40ODMtNjkuNDM2IDU1LjAyMC0xMTAuNTU2IDc0LjIwMS00OS45MTcgMjkuOTkyLTEwMy41NzEgNTMuNTE0LTE1OC4zMzkgNzMuMDk1eiIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeGU5MDI7IiBkPSJNNzA0LjQyMiA1NjMuNGMtMjMuNzg2LTUxLjQwOC0yNi40MzYtMTA5Ljc1Ny01NC4wODQtMTU5LjcyNi03LjIxMS0zMi45MDktNDEuMDkxLTg0LjI4OC0yOS40OTgtMTA2LjQ4OSAxMDEuNTktMTYuNTQyIDIwMy4xODEtMzMuMDg1IDMwNC43NzEtNDkuNjI3IDE4LjI3OCAzNS42NDkgMzEuODQ1IDczLjcxMSA0MC4yMDEgMTEyLjg5NC03My44NjggNzkuMjY5LTE1MC40NzEgMTYzLjQyNi0yNTUuMzU4IDIwMC42MzVsLTYuMDMyIDIuMzE0eiIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeGU5MDM7IiBkPSJNNjY4LjYxNiA0NDUuNzk0Yy0yMC45ODMtMzEuNjc5LTI3LjI0NS03MC40MDgtNDQuMTA0LTEwNC40MjYtMTYuMjIxLTM1LjY4OS01MS40MTUtMTE5LjQ3MiAxOS42ODQtMTA5Ljg5IDUwLjQ1OSAyNC42NTQgOTUuMDUwLTEzLjM3OSAxNDUuODE5LTE2LjkyOSA0MC4zMTctNS41NjEgODIuMTM3LTQuNjY1IDEyMS4yOTUgNy4yMDEgMTQuOTgzIDMxLjcxNiA1MC42ODYgODIuNTk0IDM4LjcyNiAxMDguMzA1LTQ4LjU2MyA5Ljg2Ny05OS40NzMgMTMuODc5LTE0NS4yMTUgMzMuNjkgMTkuMjczIDQ1LjU5NS01NC45NzQgNTUuNDY1LTg5Ljk3NiA2OC44NC0xNS4zNDEgNC42MzctMzAuNzY1IDguOTk4LTQ2LjIyOSAxMy4yMDh6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwNDsiIGQ9Ik02MDcuNzE0IDE2MC44NzNjLTMwLjE4My04NS4wMDItMTIzLjE4Ni0xMTkuOTQ4LTIwMS44MzItMTQ1Ljk0MyA1LjAyMC0xMS4xODYgNzEuMTg2LTE2LjMyNSAxMDAuNzg3LTE3LjMyNiAxMTcuMTE1LTMuMDY3IDIzNC41NDIgNDEuMDg2IDMyMC40OCAxMjAuNzUyLTc0LjgzMSAyLjgzNC0xNDkuNTQ5IDE0Ljc4NC0yMTkuNDM1IDQyLjUxN3oiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTA1OyIgZD0iTTU5OC40ODUgMjY4LjUzNGMtNC4xNzQtNDIuNzY0LTI1LjE4OC0xMTUuOTM2IDM0LjAyOC0xMjguNDk0IDU4LjUzNy0xNC41ODEgMTE5LjQzNC0xOC41MTEgMTc3LjA2Ny0zNy4wMjAgNDUuMjAxIDM3LjQ0NyA4My4xNjcgODMuNTk5IDExMS4xMiAxMzUuMjE3LTEwNy45OC0xLjY4MS0yMTkuNDMxLTEwLjIwNC0zMjIuMjE1IDMwLjI5N3oiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTA2OyIgZD0iTTYwNy4yMSA2MDAuNzhjMTguOTU5IDIuMjkgMzUuMjM0IDEuODk2IDI4LjI4OC0yMS43MiA1LjkzNS0xNC42NDQtMzYuNjE0LTE0LjYwNy0yOC4yODggMS44NDEgMCA2LjYyNiAwIDEzLjI1MiAwIDE5Ljg3OXoiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTA3OyIgZD0iTTQ0NC41NDEgNzAyLjk2MmMyMS4wODggMy45NTggMzIuNjU0LTEuNjA0IDI2Ljk1NC0yNC40MzUtNS42NzMtMTEuMDcxLTM3LjY5Ny03LjY1LTI2Ljk1NCAxNC4wMzYgMCAzLjQ2NiAwIDYuOTMzIDAgMTAuMzk5eiIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeGU5MDg7IiBkPSJNNTA0LjUzMiA2ODEuNzhjMTMuNDc4LTMuMjk4IDQzLjIzNiA5LjY4MiAzNi43NTUtMTMuNTc1IDYuMjU0LTIzLjAzMC03LjA1Ny0yNi40OTYtMjcuMTUtMjMuMTgtMTguNTY3LTUuNDk2LTUuODcxIDI2LjUyNy05LjYwNSAzNi43NTV6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwOTsiIGQ9Ik01MDIuNTM5IDQxNy40MjZjMTguNzg2IDAgMzcuNTcyIDAgNTYuMzU4IDAgMC0xOC43ODYgMC0zNy41NzIgMC01Ni4zNTgtMTguNzg2IDAtMzcuNTcyIDAtNTYuMzU4IDAgMCAxOC43ODYgMCAzNy41NzIgMCA1Ni4zNTh6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwYTsiIGQ9Ik01MDguODU2IDYzMC4xNTdjMTMuNDc4LTMuMjk4IDQzLjIzNiA5LjY4MiAzNi43NTUtMTMuNTc1IDYuMjU0LTIzLjAzMC03LjA1Ny0yNi40OTYtMjcuMTUtMjMuMTgtMTguNTY3LTUuNDk2LTUuODcxIDI2LjUyNy05LjYwNSAzNi43NTV6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwYjsiIGQ9Ik01NjkuMTAzIDQ3NC4xNGMxOC45NyAxLjkyOSAzNi40OTIgMi40MDkgMjkuNDA0LTIxLjcyIDYuODk0LTE1LjkzMi0zNy4yMzUtMTUuODMzLTI5LjQwNCAwLjcyNSAwIDYuOTk4IDAgMTMuOTk2IDAgMjAuOTk0eiIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeGU5MGM7IiBkPSJNNjI3LjA4NyA1MzEuNDAzYzE5LjM1IDcuODY5IDI5LjQwOC05LjU4MiAxOS4zMzgtMjIuMDUzLTIxLjU4My00LjE0Ny0yMC44MDEgMy4zODctMTkuMzM4IDIyLjA1M3oiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTBkOyIgZD0iTTU0Ny42MjcgNTI1LjIzMWMxMy40NzgtMy4yOTggNDMuMjM2IDkuNjgyIDM2Ljc1NS0xMy41NzUgNi4yNTQtMjMuMDMwLTcuMDU3LTI2LjQ5Ni0yNy4xNS0yMy4xOC0xOC41NjctNS40OTYtNS44NzEgMjYuNTI3LTkuNjA1IDM2Ljc1NXoiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTBlOyIgZD0iTTQwMy44MDIgMzM1LjAyNGMxNC40MjYtMS45OTggMzYuNzMgMy44ODIgNDYuNTU2LTIuNzE1LTIuMDIwLTE0LjMxNSAzLjkxMy0zNi41ODQtMi43MTUtNDYuMjkyLTEyLjg3OSAzLjU0NS00MC4wMTctNy4xNjktNDMuODQxIDUuNDMgMCAxNC41MjYgMCAyOS4wNTEgMCA0My41Nzd6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkwZjsiIGQ9Ik00NDcuOTA3IDQwNi4wODRjMTYuODM2LTAuMTE3IDM5LjY3NyA1Ljc3IDMxLjg1NC0xOS4wMDUgNy44Mi0yNC4zNTUtMTYuMDgwLTE2Ljk5LTMxLjg1NC0xNy43NSAwIDEyLjI1MiAwIDI0LjUwMyAwIDM2Ljc1NXoiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTEwOyIgZD0iTTUxNS4yOTEgNDY4LjU2N2MtMTguOTcxIDEuODk0LTM2LjYxMiAyLjQ1OS0yOS41MS0yMS43Mi0yLjUzOC0xMi45OSAzOS42MjctMTMuMzc0IDI5LjUxIDUuODkxIDAgNS4yNzYgMCAxMC41NTMgMCAxNS44Mjl6IiAvPgo8Z2x5cGggdW5pY29kZT0iJiN4ZTkxMTsiIGQ9Ik01NzguNDg2IDU3Mi44NjZjMjEuMjMyIDQuOTIgMjkuNDY5LTMuMTU2IDI0LjUwMy0yNC40MzUtMTMuMTA0LTguNTMyLTMyLjk1NS0yLjEwNi0yNC41MDMgMTkuMjAxdjUuMjM0eiIgLz4KPGdseXBoIHVuaWNvZGU9IiYjeGU5MTI7IiBkPSJNNjAxLjQ3MSA2NDEuNjg5Yy0xOC45NzEgMS44OTQtMzYuNjEyIDIuNDU5LTI5LjUxLTIxLjcyLTIuNTM4LTEyLjk5IDM5LjYyNy0xMy4zNzQgMjkuNTEgNS44OTEgMCA1LjI3NiAwIDEwLjU1MyAwIDE1LjgyOXoiIC8+CjxnbHlwaCB1bmljb2RlPSImI3hlOTE1OyIgZ2x5cGgtbmFtZT0iZGlhc3BvcmEiIGhvcml6LWFkdi14PSIxMDY0IiBkPSJNNjcwLjEgMjIuMTA5Yy0yOC42OTIgNDAuMDQ2LTczLjQ1OSAxMDIuNTcxLTk5LjQ4MiAxMzguOTQ1LTI2LjU3OSAzNy4xNTEtNDguMjAyIDY2LjE0Ni00OS4zNCA2Ni4xNjEtMS4xNjkgMC4wMTUtNDIuOTg2LTU2LjE5Ni05OC45MTItMTMyLjk2MS01My4yODgtNzMuMTQzLTk3LjMzLTEzMi45ODctOTcuODcyLTEzMi45ODctMS41MTYgMC0xOTIuMTE0IDEzNC4yNS0xOTIuNjA1IDEzNS42NjMtMC4yMzcgMC42ODQgNDIuODUgNjMuOTM1IDk1Ljc1MSAxNDAuNTU5czk2LjE4MiAxNDAuNTIzIDk2LjE4MiAxNDEuOTk5YzAgMi4zOC0xNy4xNDIgOC4zOTgtMTUxLjYwNSA1My4yMjYtODMuMzgzIDI3Ljc5OC0xNTIuNDIyIDUwLjg2MS0xNTMuNDIgNTEuMjUxLTEuMzg4IDAuNTQyIDYuNTUgMjcuMTAzIDMzLjc4MyAxMTMuMDMyIDE5LjU3OCA2MS43NzggMzYuMDQwIDExMi44MjggMzYuNTgxIDExMy40NDRzNzMuMDExLTIyLjU3NCAxNjEuMDQzLTUxLjUzNGM4OC4wMzMtMjguOTYgMTYwLjc5Ni01Mi42NTQgMTYxLjY5Ni01Mi42NTNzMS45MTkgMS4zNDggMi4yNjMgMi45OTRjMC4zNDQgMS42NDYgMS4xMDQgNzYuNjAxIDEuNjg4IDE2Ni41NjZzMS41NDYgMTY0LjMyMiAyLjEzNyAxNjUuMjM2YzAuODQ3IDEuMzA5IDI1LjU3NiAxLjY1OCAxMTYuMzY0IDEuNjQyIDYzLjQwOC0wLjAxMSAxMTUuOTQ4LTAuNDYgMTE2Ljc1Ni0wLjk5NyAxLjAxOC0wLjY3NyAyLjc1Ny01MC4xMzggNS42Ny0xNjEuMjI2IDQuNzgzLTE4Mi40MDggNC44ODYtMTg0Ljg1MiA3Ljc5NS0xODQuODUyIDEuMTI4IDAgNzAuODk2IDIzLjM1MyAxNTUuMDQxIDUxLjg5NnMxNTMuNCA1MS40MzMgMTUzLjkwMSA1MC44NjhjMS43LTEuOTE4IDcwLjcxNS0yMjYuNjAyIDY5Ljg2NS0yMjcuNDUxLTAuNDY0LTAuNDY0LTcwLjY5LTI0LjUxNC0xNTYuMDU2LTUzLjQ0NC0xMTcuNDMyLTM5Ljc5Ny0xNTUuMzE1LTUzLjE0My0xNTUuNjMzLTU0LjgyNy0wLjIzNS0xLjI0NCA0MC4xNDktNjEuNTk0IDkxLjQ5OS0xMzYuNzM3IDUwLjU1NS03My45ODEgOTEuNzMzLTEzNS4wNjIgOTEuNTA3LTEzNS43MzctMC41MjEtMS41NDktMTg5LjY4MS0xNDAuODg0LTE5MS4yNjMtMTQwLjg4NC0wLjY0MiAwLTI0LjY0MyAzMi43NjUtNTMuMzM1IDcyLjgxeiIgLz4KPC9mb250PjwvZGVmcz48L3N2Zz4=) format("svg");
      font-weight: normal;
      font-style: normal; }

    .cion, .cesiumicons,
    .ion-social-duniter:before,
    .ion-social-diaspora:before,
    .ion-office:before,
    .ion-library:before {
      display: inline-block;
      font-family: "Cesiumicons";
      speak: none;
      font-style: normal;
      font-weight: normal;
      font-variant: normal;
      text-transform: none;
      text-rendering: auto;
      line-height: 1;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale; }

    .ion-social-duniter:before {
      content: ""; }

    .ion-social-diaspora:before {
      content: ""; }

    .ion-office:before {
      content: ""; }

    .ion-library:before {
      content: ""; }

    @media screen and (max-width: 400px) {
      @-ms-viewport {
        width: 320px; }
      .item .badge {
        right: 16px; } }

    @media screen and (max-width: 767px) {
      .hidden-xs {
        display: none !important;
        visibility: hidden !important; }
      .badge {
        text-overflow: ellipsis !important;
        white-space: nowrap;
        overflow: hidden !important;
        max-width: 300px !important;
        display: block !important; }
      .badge:empty {
        display: none !important; }
      .item .badge {
        right: 16px; }
      .padding-top-xs {
        padding-top: 10px; } }

    @media screen and (min-width: 768px) {
      .hidden-xs {
        display: inherit;
        visibility: visible; }
      .row.hidden-xs {
        display: flex !important; }
      .button.hidden-xs {
        display: inline-block; }
      .item-toggle .toggle {
        right: 32px; } }

    @media screen and (max-width: 767px) {
      .visible-xs {
        display: inherit !important;
        visibility: visible !important; } }

    @media screen and (min-width: 768px) {
      .visible-xs {
        display: none !important;
        visibility: hidden !important; } }

    @media screen and (max-width: 767px) {
      .padding-xs {
        padding: 16px !important; } }

    @media screen and (min-width: 768px) {
      .padding-xs {
        padding: inherit; } }

    @media screen and (max-width: 767px) {
      .no-padding-xs {
        padding: 0px !important; } }

    @media screen and (min-width: 768px) {
      .no-padding-xs {
        padding: inherit; } }

    @media screen and (max-width: 767px) {
      .no-margin-xs {
        margin: 0px !important; } }

    @media screen and (min-width: 768px) {
      .no-margin-xs {
        margin: inherit; } }

    @media screen and (max-width: 991px) and (min-width: 768px) {
      .hidden-sm, .row-header.hidden-sm {
        display: none !important;
        visibility: hidden !important; }
      .badge {
        text-overflow: ellipsis !important;
        white-space: nowrap;
        overflow: hidden !important;
        max-width: 400px !important;
        display: block !important; }
      .badge:empty {
        display: none !important; } }

    @media screen and (min-width: 992px) {
      .hidden-sm {
        display: inherit;
        visibility: visible; }
      .row.hidden-sm {
        display: flex !important; }
      .button.hidden-sm {
        display: inline-block; } }

    @media screen and (max-width: 767px) {
      .hidden-sm {
        display: inherit;
        visibility: visible; } }

    @media screen and (max-width: 991px) {
      .visible-sm {
        display: inherit !important;
        visibility: visible !important; } }

    @media screen and (min-width: 992px) {
      .visible-sm {
        display: none;
        visibility: hidden; } }

    @media screen and (max-width: 767px) {
      .visible-sm {
        display: none;
        visibility: hidden; } }

    @media screen and (max-width: 991px) {
      body {
        cursor: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAN1wAADdcBQiibeAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAEbSURBVDiNndMxK4ZRGMbx3zmRMrwvM2XCQFFik/IJpCw+hJLPgfIhLBY+gEEGJQPFwGZg9TIoBrfhOfSQHl7XeM7/fw3nvk+KCPWklEawgGlMlOMrnOMoIm6/8B8FKaWMFazhGQ94LFwbg+jHDvYi4u2zIKWUsIk5XNfE72ljHKfYiIjI5WIZMzhrkJW7s8IuQ8IwdnGDpwa5nhbGsJqxiNcuZIV9xWJWvXY3cr1kOmMSnX8UdDCZEb+RDYmMS9WMu80gLrNqw1r/KGjhPOMQvaol+Wva6MFhjoh7bGEUuVGrkgu7HRH3H8IBTjCLgQZ5oDAnxfnymRKWsI4X1Zw7NbGFPmxjP4qYfvjOQ5jHlGpHqCZ1geOIuKvz76QSW1T3cwmnAAAAAElFTkSuQmCC), auto; } }

    @media screen and (min-width: 992px) {
      body {
        cursor: inherit; } }

    @media screen and (min-width: 992px) and (max-width: 1199px) {
      .hidden-md {
        display: none !important;
        visibility: hidden !important; }
      .badge {
        text-overflow: ellipsis !important;
        white-space: nowrap;
        overflow: hidden !important;
        max-width: 400px !important;
        display: block !important; }
      .badge:empty {
        display: none !important; }
      /*
      see issue #
      html{
        -webkit-user-selectuser-select: all !important;
        -moz-user-select: all !important;
        -ms-user-select: all !important;
        user-select: all !important;
      }*/ }

    @media screen and (min-width: 1200px) {
      .hidden-md {
        display: inherit;
        visibility: visible; } }

    @media screen and (max-width: 991px) {
      .hidden-md {
        display: inherit;
        visibility: visible; } }

    @media screen and (min-width: 992px) and (max-width: 1199px) {
      .visible-md {
        display: inherit !important;
        visibility: visible !important; } }

    @media screen and (min-width: 1200px) {
      .visible-md {
        display: none;
        visibility: hidden; } }

    @media screen and (max-width: 991px) {
      .visible-md {
        display: none;
        visibility: hidden; } }

    @media screen and (min-width: 1200px) {
      .hidden-lg {
        display: none !important;
        visibility: hidden !important; }
      .visible-lg {
        display: inherit !important;
        visibility: visible !important; }
      .badge {
        text-overflow: ellipsis !important;
        white-space: nowrap;
        overflow: hidden !important;
        max-width: 450px !important;
        display: block !important; }
      .badge:empty {
        display: none !important; }
      /*html{
        -webkit-user-select: all !important;
        -moz-user-select: all !important;
        -ms-user-select: all !important;
        user-select: all !important;
      }*/ }

    @media screen and (max-width: 1199px) {
      .hidden-lg {
        display: inherit;
        visibility: visible; } }

    @media screen and (max-width: 1199px) {
      .visible-lg {
        display: none;
        visibility: hidden; } }

    @media screen and (max-width: 768px) {
      .no-padding-xs {
        padding: inherit; } }

    @media screen and (max-width: 767px) {
      .no-margin-xs {
        margin: 0px !important; } }

    /**********
       Notifications view
    **********/
    .view-notification .item.unread {
      background-color: #ecf0f7 !important;
      border-color: #dddfe2 !important; }

    .view-notification ion-item h4 i.icon, .view-notification ion-item h4 i.icon-help, .view-notification ion-item h4 i.icon-alert, .view-notification ion-item h4 #menu .footer i.icon-help, #menu .footer .view-notification ion-item h4 i.icon-help {
      font-size: 18px !important;
      line-height: 12px !important;
      vertical-align: middle !important; }

    /* ============
     Buttons
    =============== */
    .bar.bar-header .button.button-clear.button-icon.ion-android-more-vertical, .bar.bar-header .button.button-icon.ion-android-more-vertical.button-text,
    .bar.bar-header .button.button-clear.button-icon i.ion-android-more-vertical,
    .bar.bar-header .button.button-icon.button-text i.ion-android-more-vertical {
      padding-left: 8px; }

    .bar.bar-header .buttons .secondary-buttons >
    .button.button-clear.button-icon.ion-android-more-vertical:first-child, .bar.bar-header .buttons .secondary-buttons >
    .button.button-icon.ion-android-more-vertical.button-text:first-child {
      padding-left: 0px !important; }

    .bar .buttons.pull-right, .bar .popover-helptip .buttons.icon.icon-right, .popover-helptip .bar .buttons.icon.icon-right, .bar .popover-helptip .buttons.icon-right.icon-help, .popover-helptip .bar .buttons.icon-right.icon-help, .bar .popover-helptip .buttons.icon-right.icon-alert, .popover-helptip .bar .buttons.icon-right.icon-alert, .bar .popover-helptip #menu .footer .buttons.icon-right.icon-help, .popover-helptip #menu .footer .bar .buttons.icon-right.icon-help, .bar #menu .footer .popover-helptip .buttons.icon-right.icon-help, #menu .footer .popover-helptip .bar .buttons.icon-right.icon-help, .bar .popover-helptip .buttons.icon.icon-center, .popover-helptip .bar .buttons.icon.icon-center, .bar .popover-helptip .buttons.icon-center.icon-help, .popover-helptip .bar .buttons.icon-center.icon-help, .bar .popover-helptip .buttons.icon-center.icon-alert, .popover-helptip .bar .buttons.icon-center.icon-alert, .bar .popover-helptip #menu .footer .buttons.icon-center.icon-help, .popover-helptip #menu .footer .bar .buttons.icon-center.icon-help, .bar #menu .footer .popover-helptip .buttons.icon-center.icon-help, #menu .footer .popover-helptip .bar .buttons.icon-center.icon-help, .bar .popover-helptip .buttons.icon.icon-bottom-right, .popover-helptip .bar .buttons.icon.icon-bottom-right, .bar .popover-helptip .buttons.icon-bottom-right.icon-help, .popover-helptip .bar .buttons.icon-bottom-right.icon-help, .bar .popover-helptip .buttons.icon-bottom-right.icon-alert, .popover-helptip .bar .buttons.icon-bottom-right.icon-alert, .bar .popover-helptip #menu .footer .buttons.icon-bottom-right.icon-help, .popover-helptip #menu .footer .bar .buttons.icon-bottom-right.icon-help, .bar #menu .footer .popover-helptip .buttons.icon-bottom-right.icon-help, #menu .footer .popover-helptip .bar .buttons.icon-bottom-right.icon-help, .bar .popover-helptip .buttons.icon.icon-bottom-center, .popover-helptip .bar .buttons.icon.icon-bottom-center, .bar .popover-helptip .buttons.icon-bottom-center.icon-help, .popover-helptip .bar .buttons.icon-bottom-center.icon-help, .bar .popover-helptip .buttons.icon-bottom-center.icon-alert, .popover-helptip .bar .buttons.icon-bottom-center.icon-alert, .bar .popover-helptip #menu .footer .buttons.icon-bottom-center.icon-help, .popover-helptip #menu .footer .bar .buttons.icon-bottom-center.icon-help, .bar #menu .footer .popover-helptip .buttons.icon-bottom-center.icon-help, #menu .footer .popover-helptip .bar .buttons.icon-bottom-center.icon-help, .bar .title + .button:last-child, .bar .title + .buttons, .bar > .button + .button:last-child, .bar > .button.pull-right, .popover-helptip .bar > .button.icon.icon-right, .popover-helptip .bar > .button.icon-right.icon-help, .popover-helptip .bar > .button.icon-right.icon-alert, .popover-helptip #menu .footer .bar > .button.icon-right.icon-help, #menu .footer .popover-helptip .bar > .button.icon-right.icon-help, .popover-helptip .bar > .button.icon.icon-center, .popover-helptip .bar > .button.icon-center.icon-help, .popover-helptip .bar > .button.icon-center.icon-alert, .popover-helptip #menu .footer .bar > .button.icon-center.icon-help, #menu .footer .popover-helptip .bar > .button.icon-center.icon-help, .popover-helptip .bar > .button.icon.icon-bottom-right, .popover-helptip .bar > .button.icon-bottom-right.icon-help, .popover-helptip .bar > .button.icon-bottom-right.icon-alert, .popover-helptip #menu .footer .bar > .button.icon-bottom-right.icon-help, #menu .footer .popover-helptip .bar > .button.icon-bottom-right.icon-help, .popover-helptip .bar > .button.icon.icon-bottom-center, .popover-helptip .bar > .button.icon-bottom-center.icon-help, .popover-helptip .bar > .button.icon-bottom-center.icon-alert, .popover-helptip #menu .footer .bar > .button.icon-bottom-center.icon-help, #menu .footer .popover-helptip .bar > .button.icon-bottom-center.icon-help {
      top: 0px !important; }

    .bar.bar-header {
      padding-right: 5px !important; }
      .bar.bar-header .buttons-right span {
        margin-left: 0px !important; }

    .bar .title + .buttons.buttons-right {
      right: 5px; }

    .button-icon {
      border-color: transparent;
      box-shadow: none !important; }

    .button-small-padding {
      padding: 0 7px !important; }

    .button-text {
      color: grey !important;
      font-size: 12px; }

    .button-text.button-small {
      padding: 5px 2px;
      font-size: 12px !important; }

    .button-text-positive {
      color: #387ef5 !important; }

    .button-text-stable {
      color: #b2b2b2 !important; }


    .gray, .popover-share .bar-header span, .popover-share .bar-footer .button-close, .popover-helptip .button-close {
      color: grey !important; }
      .gray b, .popover-share .bar-header span b, .popover-share .bar-footer .button-close b, .popover-helptip .button-close b {
        color: grey !important; }

    .gray a, .popover-share .bar-header span a, .popover-share .bar-footer .button-close a, .popover-helptip .button-close a, .positive a, .icon-help a {
      color: inherit; }

    .gray a:hover, .popover-share .bar-header span a:hover, .popover-share .bar-footer .button-close a:hover, .popover-helptip .button-close a:hover, .positive a:hover, .icon-help a:hover {
      color: inherit; }

    .gray a:visited, .popover-share .bar-header span a:visited, .popover-share .bar-footer .button-close a:visited, .popover-helptip .button-close a:visited, .positive a:visited, .icon-help a:visited {
      color: inherit; }

    .item a {
      text-decoration: none; }

    .no-padding {
      padding: 0px !important; }

    .avatar-member {
      background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAQAAABpN6lAAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAADdcAAA3XAUIom3gAAAAHdElNRQfgBA0LKSJACf7RAAAFqElEQVR42u2dbUjdZRjGf+c4dTndFnO+TRcD3ZIxdVlJL2PSIHDZBqtRaINojWRrgR8a4fDbIOrzoIKIMay2iGpQsg0y4xRZIk5i1NTaik3TZeRcvmzl0wc7qTPz6Lnv5/kfz/86XzxyuM59Xf/r//o893MCBqtIoYgSSsgjndWkk8RV+v959dFPP9/wm82CAtYM2MCTPMZGgnN87iZNvMdH1mww+q8Es9e0m/nhhjltnjG361enL3+HOT9P8ZMYNC+YhFg2IN2cWbD4MNrN3bFqQIm5FLV8Y4wZMbtj0YBdZlhEvjHGjJsXY82A+8yomPwJ1OhUqnMazKOVTGHOUe7lW/lS5zorLwSJnBKXD0s5SUpsGHCAzQqsUMgr8qTyu8AqulmpYgCMcQd9spTyCahTkw/JHJCmlE5AIj2kqxkAA6xlWJJQOgEVqvJhFY/LEkobUKUqH2Cbtw0oVTdgqyyd7DEgiWES1C1YxyU5MtkEFFiQD/dLkskacKcF+ZAb7waIXmbHogEZ3jVANJyxaEDAigHJ8W6AKDRuh2MKfgJcF+AafgJ8A+IcfgJcF+AafgJcF+AafgJE2URvU2aFqM2SzwSXM8ASCwaMkc9lKTLJBNRbkQ/JvCRHJpeANPpZasUAuEa21PiQXAJ2WJMPy9ktRSVnwBPW5IPgCJScAXdZNSDHewbcsGpAmvcMGLVqgNhkGTkDfrVqQLv3DHjfqgGnpYjkrgMyuGLpQgigkO9liOQS0M+H1uS/JSVf9l5gBV9RaEH+TxRxTYpM8l5gkEcZUJc/zB45+dK3wz9QhW4LiuERQpKE0k+EzvKuqgENNMsSyj8SO6lqwFFpQvmpskv4kTwl+e3ydxzyCfhTfiv9i9flKTX6BVZymWUK8ofI4bo0qcZj8d85psAKDfLytRonC7ig8Ii8hA75UnUGRrr4RJyzRUO+3sjQq8J8hjqdQrUMCAkfsV/jM51C9Zqnl3GOfCGuixRpHABBc3D0D/bwlwiTYa+WfN3R4RZeFuFRiz9orx+QSEvUF6+K8Qft+QE3eSpqDsX4g40VJKL9AuU5B/4MEdcFuIZvgOsCXEPbgFQPMDg1INsDDL4BLg2IfqRIeaxJ24CdHmD4X+heCaZxNerJk2OsZkivRN0EVAvMHU2mWrNEzQSk0C1yCOslX3bViKnQTECt0BE8m1q9IvUSUEaz2NTJUcr5OrYMyKWVLEG+X7hHboL0VOjsArk0isqHLBp1WrM1DCijlU3irJtopcz7BqRwmGbhrR9GFs0cFl9PTHBpujRTY3qEF9GbiR5TY9K8tJxeKtlkU8hOHrLUMgNjNHGK7+ilN9pHpvMxYAXVPKu0Upwc2nmTtxmM+PMRRiXB7DcD6vGWwoDZH+lqtJEl4EGOUux6084THTzPF3N/bO6zQA4NhGJOPhQToiGCxoo5InLQDLnOc1QYMgcXvgsk8QZPu96QAjjGc7P3s8xuQAYf8IDr2oXwJbvon58BG2lkreu6BfEz2zkfuQEb+FxhYVy36GMrFyIzYB0h1riuVwFX2MLFW/858zSYR9OilA9raJo5i/nWBGQSosB1pYroYsv0lYmnJyDIiUUtHwo4MV3zdAPqKXddoTrKqZ/6duouUM6ncTFcPs62yb6TSQPS6ZBrSfY4eigOd7qGt3iA43EjH3I4Hp58FTZgHxWuq7KKCvZN/DGxC6TSveiu/OZCH/lcDyfgUNzJh0wOwUQCcujS+PEKz2OYAnqCwJG4lA8pHIGAKaI9Ls7+/4VxNgfMGR52XYdDnA0Yyz846DXEa/h9A3wDfAN8A3wDfAOAIL2uS3CK3iBtrmtwira4NyBg1nOO21zX4QgjlATp1OrMjwHU0RkwEOBjtruuxQEaqcQEAUMltYy4rscqRqilEjN1XGA9VZRSqt2k5By9tNHGO3ROvP0bpkYvBeY8k00AAAAldEVYdGRhdGU6Y3JlYXRlADIwMTYtMDQtMTNUMTE6NDE6MzQrMDI6MDCAxbwoAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDE2LTA0LTEzVDExOjQxOjM0KzAyOjAw8ZgElAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAAASUVORK5CYII=); }

    .avatar.disable {
      opacity: 0.7; }

    .avatar-wallet {
      background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAQAAABpN6lAAAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAADdcAAA3XAUIom3gAAAAHdElNRQfgBA0JMBOJU4OdAAAB2klEQVR42u3au0ocUQCH8e+IxFRKUohrkG0Eu4BiLrCFDxDBNtjoI1gYTJnGQrRInc4HyBuopNHgBVZIwCJFsHBXLURtQiIyKYyFN3B3zJyd3e/X7e7MmXP+e26zOyBJkiRJkiRJkiRJkiRJkppVSF9E0sk0r+jKuOYnrLMQTqMHkHSzQTHS17fLy3CYroi21JWYj9Z8KDIfvwdUKAC7bGfc+CH6gGrojTyJJBemMr/u+4sLpy2njRZnAAZgAK2t/YHKGUzeZlzz5w2yFU6/EKVsQHAIGIABGIABGIABRN4ILbOVcc1fM9IQCfp7gHOAARiAAbgMXpuhH/GJ0brK/sG78OVfKR0cXPnsQ/iYkwAYY6LOM4eZpXR5q3/t77bH+RkCO5HObZQeEL4nQ7ypcwh8boIAIJQpuwoYQMsugwBJ/73L/h1+3vr+OYtXXn/Lzz7gCV8ZqOH4LUrhz4155IzJvA6B0VqaDwzHu7f/PwEsc1zT8RXWmmoOCJWkl9L95wBWbw6AvO8DfrHkMmgABmAABtDw0j8gUaWHeA9K7odC7GVwhXGgGOmB2ZX4PaDAJs8i9d89XoRq5AAgecoMg3Rm3PhTysyFIyRJkiRJkiRJkiRJkiRJku7wFyuiadmIs5Q9AAAAAElFTkSuQmCC); }

    .item.item-icon-left > i.avatar:first-child {
      position: absolute;
      display: flex;
      height: 100%;
      align-items: center;
      font-size: 16px;
      left: 16px;
      top: 8px;
      max-height: 32px;
      max-width: 32px; }

    .item.item-checkbox.item-avatar > i.avatar:first-child,
    .item.item-checkbox.item-avatar > i.item-image:first-child,
    .item.item-checkbox.item-avatar .item-content > i.avatar:first-child,
    .item.item-checkbox.item-avatar .item-content > i.item-image:first-child,
    .item.item-checkbox.item-avatar * > i.avatar:first-child,
    .item.item-checkbox.item-avatar * > i.item-image:first-child,
    .item.item-checkbox.item-avatar * .item-content > i.avatar:first-child,
    .item.item-checkbox.item-avatar * .item-content > i.item-image:first-child {
      left: 65px; }

    .item.item-checkbox.item-avatar .item-content,
    .item.item-checkbox .item-content .item-avatar {
      padding-left: 65px; }

    .card .card-header,
    .card .card-header {
      font-size: 90%;
      opacity: 0.8;
      filter: alpha(opacity=80); }

    .card.stable-900-bg,
    .card .stable-900-bg,
    .item.stable-900-bg,
    .item .stable-900-bg,
    .item-complex .item-content .stable-900-bg,
    .item-radio .item-content .stable-900-bg {
      background-color: #e0e0e0 !important; }

    .card .item {
      background: inherit; }

    .card.stable-bg,
    .card .stable-bg,
    .item.stable-bg,
    .item .stable-bg,
    .item-complex .item-content .stable-bg,
    .item-radio .item-content .stable-bg {
      background-color: #f8f8f8 !important; }

    .card .card-header {
      padding-top: 5px !important;
      padding-bottom: 0px !important;
      min-height: 25px; }

    .card .item .card-footer {
      margin-bottom: 5px; }

    .card .card-avatar .avatar,
    .card.card-avatar .avatar {
      box-shadow: 0px 3px 4px 0 rgba(0, 0, 0, 0.26);
      top: 7px;
      background-color: #D9D9D9; }

    .card .card-avatar img.avatar,
    .card.card-avatar img.avatar {
      border: 0;
      min-height: 54px;
      min-width: 54px; }

    .card .card-avatar .item.item-avatar,
    .card.card-avatar .item.item-avatar {
      padding-top: 10px;
      padding-bottom: 2px;
      min-height: 45px !important; }

    .card .card-avatar .card-footer,
    .card.card-avatar .card-footer {
      padding-left: 88px; }
      .card .card-avatar .card-footer .pull-right a, .card .card-avatar .card-footer .popover-helptip .icon.icon-right a, .popover-helptip .card .card-avatar .card-footer .icon.icon-right a, .card .card-avatar .card-footer .popover-helptip .icon-right.icon-help a, .popover-helptip .card .card-avatar .card-footer .icon-right.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon-right.icon-alert a, .popover-helptip .card .card-avatar .card-footer .icon-right.icon-alert a, .card .card-avatar .card-footer .popover-helptip #menu .footer .icon-right.icon-help a, .popover-helptip #menu .footer .card .card-avatar .card-footer .icon-right.icon-help a, .card .card-avatar .card-footer #menu .footer .popover-helptip .icon-right.icon-help a, #menu .footer .popover-helptip .card .card-avatar .card-footer .icon-right.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon.icon-center a, .popover-helptip .card .card-avatar .card-footer .icon.icon-center a, .card .card-avatar .card-footer .popover-helptip .icon-center.icon-help a, .popover-helptip .card .card-avatar .card-footer .icon-center.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon-center.icon-alert a, .popover-helptip .card .card-avatar .card-footer .icon-center.icon-alert a, .card .card-avatar .card-footer .popover-helptip #menu .footer .icon-center.icon-help a, .popover-helptip #menu .footer .card .card-avatar .card-footer .icon-center.icon-help a, .card .card-avatar .card-footer #menu .footer .popover-helptip .icon-center.icon-help a, #menu .footer .popover-helptip .card .card-avatar .card-footer .icon-center.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon.icon-bottom-right a, .popover-helptip .card .card-avatar .card-footer .icon.icon-bottom-right a, .card .card-avatar .card-footer .popover-helptip .icon-bottom-right.icon-help a, .popover-helptip .card .card-avatar .card-footer .icon-bottom-right.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon-bottom-right.icon-alert a, .popover-helptip .card .card-avatar .card-footer .icon-bottom-right.icon-alert a, .card .card-avatar .card-footer .popover-helptip #menu .footer .icon-bottom-right.icon-help a, .popover-helptip #menu .footer .card .card-avatar .card-footer .icon-bottom-right.icon-help a, .card .card-avatar .card-footer #menu .footer .popover-helptip .icon-bottom-right.icon-help a, #menu .footer .popover-helptip .card .card-avatar .card-footer .icon-bottom-right.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon.icon-bottom-center a, .popover-helptip .card .card-avatar .card-footer .icon.icon-bottom-center a, .card .card-avatar .card-footer .popover-helptip .icon-bottom-center.icon-help a, .popover-helptip .card .card-avatar .card-footer .icon-bottom-center.icon-help a, .card .card-avatar .card-footer .popover-helptip .icon-bottom-center.icon-alert a, .popover-helptip .card .card-avatar .card-footer .icon-bottom-center.icon-alert a, .card .card-avatar .card-footer .popover-helptip #menu .footer .icon-bottom-center.icon-help a, .popover-helptip #menu .footer .card .card-avatar .card-footer .icon-bottom-center.icon-help a, .card .card-avatar .card-footer #menu .footer .popover-helptip .icon-bottom-center.icon-help a, #menu .footer .popover-helptip .card .card-avatar .card-footer .icon-bottom-center.icon-help a,
      .card.card-avatar .card-footer .pull-right a,
      .card.card-avatar .card-footer .popover-helptip .icon.icon-right a, .popover-helptip
      .card.card-avatar .card-footer .icon.icon-right a,
      .card.card-avatar .card-footer .popover-helptip .icon-right.icon-help a, .popover-helptip
      .card.card-avatar .card-footer .icon-right.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon-right.icon-alert a, .popover-helptip
      .card.card-avatar .card-footer .icon-right.icon-alert a,
      .card.card-avatar .card-footer .popover-helptip #menu .footer .icon-right.icon-help a, .popover-helptip #menu .footer
      .card.card-avatar .card-footer .icon-right.icon-help a,
      .card.card-avatar .card-footer #menu .footer .popover-helptip .icon-right.icon-help a, #menu .footer .popover-helptip
      .card.card-avatar .card-footer .icon-right.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon.icon-center a, .popover-helptip
      .card.card-avatar .card-footer .icon.icon-center a,
      .card.card-avatar .card-footer .popover-helptip .icon-center.icon-help a, .popover-helptip
      .card.card-avatar .card-footer .icon-center.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon-center.icon-alert a, .popover-helptip
      .card.card-avatar .card-footer .icon-center.icon-alert a,
      .card.card-avatar .card-footer .popover-helptip #menu .footer .icon-center.icon-help a, .popover-helptip #menu .footer
      .card.card-avatar .card-footer .icon-center.icon-help a,
      .card.card-avatar .card-footer #menu .footer .popover-helptip .icon-center.icon-help a, #menu .footer .popover-helptip
      .card.card-avatar .card-footer .icon-center.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon.icon-bottom-right a, .popover-helptip
      .card.card-avatar .card-footer .icon.icon-bottom-right a,
      .card.card-avatar .card-footer .popover-helptip .icon-bottom-right.icon-help a, .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-right.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon-bottom-right.icon-alert a, .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-right.icon-alert a,
      .card.card-avatar .card-footer .popover-helptip #menu .footer .icon-bottom-right.icon-help a, .popover-helptip #menu .footer
      .card.card-avatar .card-footer .icon-bottom-right.icon-help a,
      .card.card-avatar .card-footer #menu .footer .popover-helptip .icon-bottom-right.icon-help a, #menu .footer .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-right.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon.icon-bottom-center a, .popover-helptip
      .card.card-avatar .card-footer .icon.icon-bottom-center a,
      .card.card-avatar .card-footer .popover-helptip .icon-bottom-center.icon-help a, .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-center.icon-help a,
      .card.card-avatar .card-footer .popover-helptip .icon-bottom-center.icon-alert a, .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-center.icon-alert a,
      .card.card-avatar .card-footer .popover-helptip #menu .footer .icon-bottom-center.icon-help a, .popover-helptip #menu .footer
      .card.card-avatar .card-footer .icon-bottom-center.icon-help a,
      .card.card-avatar .card-footer #menu .footer .popover-helptip .icon-bottom-center.icon-help a, #menu .footer .popover-helptip
      .card.card-avatar .card-footer .icon-bottom-center.icon-help a {
        margin-right: 8px; }

    a.underline:focus,
    .underline a:focus,
    .a.underline:active,
    .underline a:active,
    a.underline:hover,
    .underline a:hover {
      outline: 1px !important;
      text-decoration: underline !important; }

    .card-avatar-small.card,
    .card-avatar-small .card,
    .card-avatar-small .card.card-avatar,
    .card-avatar-small .card .card-avatar {
      min-height: 45px; }
      .card-avatar-small.card .avatar,
      .card-avatar-small.card .item-avatar .avatar,
      .card-avatar-small .card .avatar,
      .card-avatar-small .card .item-avatar .avatar,
      .card-avatar-small .card.card-avatar .avatar,
      .card-avatar-small .card.card-avatar .item-avatar .avatar,
      .card-avatar-small .card .card-avatar .avatar,
      .card-avatar-small .card .card-avatar .item-avatar .avatar {
        box-shadow: 0px 2px 2px 0 rgba(0, 0, 0, 0.26);
        height: 30px !important;
        width: 30px !important;
        left: 5px !important; }
      .card-avatar-small.card .item.item-avatar,
      .card-avatar-small .card .item.item-avatar,
      .card-avatar-small .card.card-avatar .item.item-avatar,
      .card-avatar-small .card .card-avatar .item.item-avatar {
        min-height: 25px !important;
        padding-left: 42px !important; }
      .card-avatar-small.card .card-footer,
      .card-avatar-small .card .card-footer,
      .card-avatar-small .card.card-avatar .card-footer,
      .card-avatar-small .card .card-avatar .card-footer {
        padding-top: 0px;
        padding-left: 42px !important; }

    .list .item.text-left {
      text-align: left !important; }

    .list .item.text-center, .list .item.large-button-bar {
      text-align: center !important; }

    .list .item.text-right {
      text-align: right !important; }

    .list .item-divider.item-divider-top-border {
      border-top: solid 1px rgba(0, 0, 0, 0.12); }

    /**********
       Item > Avatar
    **********/
    .item-avatar {
      min-height: 80px !important; }

    .item-avatar > i:first-child,
    .item-avatar > img:first-child,
    .item-avatar i.item-image:first-child,
    .item-avatar img.item-image:first-child,
    .item-avatar .item-content > i:first-child,
    .item-avatar .item-content > img:first-child,
    .item-avatar .item-content i.item-image:first-child,
    .item-avatar .item-content img.item-image:first-child,
    .item-avatar-left > i:first-child,
    .item-avatar-left > img:first-child,
    .item-avatar-left i.item-image:first-child,
    .item-avatar-left img.item-image:first-child,
    .item-avatar-left .item-content > i:first-child,
    .item-avatar-left .item-content > img:first-child,
    .item-avatar-left .item-content i.item-image:first-child,
    .item-avatar-left .item-content img.item-image:first-child {
      color: #D9D9D9;
      background-color: #f8f8f8;
      border: solid 1px #D9D9D9;
      overflow: hidden !important;
      font-size: 45px !important;
      /*padding-left: 0px;
      padding-top: 0px;
      text-align: center;
      vertical-align: middle;*/
      line-height: 56px;
      width: 100% !important;
      /*display: block !important;*/
      max-height: 56px !important;
      max-width: 56px !important;
      top: 12px !important; }

    .item-avatar > .icon:first-child:before, .item-avatar > .icon-help:first-child:before, .item-avatar > .icon-alert:first-child:before, #menu .footer .item-avatar > .icon-help:first-child:before,
    .item-avatar .icon.item-image:first-child:before,
    .item-avatar .item-image.icon-help:first-child:before,
    .item-avatar .item-image.icon-alert:first-child:before,
    .item-avatar #menu .footer .item-image.icon-help:first-child:before, #menu .footer
    .item-avatar .item-image.icon-help:first-child:before,
    .item-avatar .item-content > .icon:first-child:before,
    .item-avatar .item-content > .icon-help:first-child:before,
    .item-avatar .item-content > .icon-alert:first-child:before,
    .item-avatar #menu .footer .item-content > .icon-help:first-child:before, #menu .footer
    .item-avatar .item-content > .icon-help:first-child:before,
    .item-avatar.item-icon-right .icon:first-child:before,
    .item-avatar.item-icon-right .icon-help:first-child:before,
    .item-avatar.item-icon-right .icon-alert:first-child:before,
    .item-avatar.item-icon-right #menu .footer .icon-help:first-child:before, #menu .footer
    .item-avatar.item-icon-right .icon-help:first-child:before,
    .item-avatar.item-icon-right .icon-help:first-child:before,
    .item-avatar.item-icon-right .icon-alert:first-child:before,
    .item-avatar.item-icon-right #menu .footer .icon-help:first-child:before,
    #menu .footer .item-avatar.item-icon-right .icon-help:first-child:before {
      width: 56px !important; }

    .item-avatar.item-icon-right .icon:last-child, .item-avatar.item-icon-right .icon-help:last-child, .item-avatar.item-icon-right .icon-alert:last-child, .item-avatar.item-icon-right #menu .footer .icon-help:last-child, #menu .footer .item-avatar.item-icon-right .icon-help:last-child,
    .item-avatar.item-icon-right .icon-help:last-child,
    .item-avatar.item-icon-right .icon-alert:last-child,
    .item-avatar.item-icon-right #menu .footer .icon-help:last-child,
    #menu .footer .item-avatar.item-icon-right .icon-help:last-child {
      left: auto; }

    /**********
       Item > Other
    **********/
    .item em {
      font-weight: bold !important; }

    @media screen and (min-width: 992px) {
      .list .item.item-border-large {
        border-bottom: solid 1px #ccc !important; }
      .list.item-border-large .item {
        border-bottom: solid 1px #ccc !important;
        margin: 0px; }
      .list.item-border-large .item-divider {
        border-top: 0; } }

    .list .item.item-border {
      border-bottom: solid 1px #ccc !important; }

    .list .item.item-small-height {
      padding-top: 2px;
      padding-bottom: 0px;
      min-height: 24px; }
      .list .item.item-small-height .badge {
        padding-top: 0px !important;
        top: inherit; }
      .list .item.item-small-height .badge.badge-balanced,
      .list .item.item-small-height .badge.badge-positive,
      .list .item.item-small-height .badge.badge-assertive,
      .list .item.item-small-height .badge.badge-editable:hover,
      .list .item.item-small-height .badge.badge-royal,
      .list .item.item-small-height .badge.badge-calm,
      .list .item.item-small-height .badge.badge-energized {
        top: 1px !important;
        padding-top: 3px !important;
        padding-bottom: 2px !important; }

    /*
    * Badge
    */
    .item.item-icon-right .badge,
    .item.item-button-right .badge {
      right: 43px; }

    .badge-editable:hover {
      cursor: pointer; }

    .badge-editable:hover:before {
      content: " "; }

    .badge-button {
      position: absolute !important;
      margin: 0 !important;
      padding: 0px 4px !important;
      font-size: 10px;
      top: 5px !important;
      right: 3px !important; }

    /**********
       Item buttons
    **********/
    .item-button-right > .button,
    .item-button-right .item-content > .button,
    .item-button-right > .buttons,
    .item-button-right .item-content > .buttons {
      top: 16px; }

    .item-button-right > .button.button-small,
    .item-button-right .item-content > .button.button-small,
    .item-button-right > .buttons .button-small,
    .item-button-right .item-content > .buttons .button-small {
      font-size: 14px; }

    .item.large-button-bar {
      margin-bottom: 10px; }

    /**********
       Item avatar
    **********/
    .item-avatar-left-padding {
      padding-left: 95px; }

    /**********
       Item thumbnail
    **********/
    .item-thumbnail-left-padding {
      padding-left: 106px; }

    .item.item-thumbnail-left, .item-thumbnail-left {
      min-height: 100px !important; }

    .item-thumbnail-left > i:first-child,
    .item-thumbnail-left i.item-image,
    .item-thumbnail-left .item-content > i:first-child,
    .item-thumbnail-left .item-content i.item-image {
      color: #D9D9D9;
      background-color: #f8f8f8;
      overflow: hidden !important;
      font-size: 50px !important;
      line-height: 80px;
      padding: 0 15px;
      background-position: center;
      background-size: cover;
      display: inline-block; }

    @media screen and (max-width: 400px) {
      .card > .item.item-thumbnail-left,
      .item-thumbnail-left,
      .item-thumbnail-left .item-content {
        padding-left: 84px !important; }
      .item-thumbnail-left > img:first-child,
      .item-thumbnail-left img.item-image,
      .item-thumbnail-left .item-content > img:first-child,
      .item-thumbnail-left .item-content img.item-image {
        max-width: 70px;
        max-height: 70px; }
      .item h2 {
        font-size: 13px !important; } }

    /**********
       Item icons
    **********/
    .item-icon-left-padding {
      padding-left: 40px; }

    .item-icon-right-padding {
      padding-right: 40px; }

    .text-keep-lines {
      white-space: pre-line !important; }

    .text-italic {
      font-style: italic !important; }

    /**********
       Help
    **********/
    .icon-help {
      font-size: 38px;
      vertical-align: middle; }

    .icon-alert {
      font-size: 38px;
      vertical-align: middle; }

    .bar-header .button-icon .avatar {
      height: 35px;
      width: 35px;
      position: relative;
      left: 0px;
      top: 4px;
      border: solid 1px #D9D9D9; }

    .bar-header .button-icon .avatar.active {
      background-color: #e0e0e0; }

    .bar-header .button-icon .avatar {
      height: 31px;
      width: 31px;
      position: relative;
      left: 0px;
      top: 6px; }

    .badge.badge-secondary,
    .badge .badge-secondary {
      font-size: 12px;
      font-style: italic;
      top: 37px !important;
      font-weight: normal !important;
      margin-right: 0px;
      padding-right: 0px; }

    .ion-es-user-api:before {
      content: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPHN2ZyB3aWR0aD0iMzBweCIgaGVpZ2h0PSIzMHB4IiB2aWV3Qm94PSIwIDAgNTAwIDUwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczpieD0iaHR0cHM6Ly9ib3h5LXN2Zy5jb20iPgogIDxwYXRoIGQ9Ik0gMzkwLjk5MiAxMjIuMjc2IEMgNDE4LjIwOSAxNTcuMTgzIDQzNC45MjQgMjAwLjI3OCA0NDEuMjAyIDI0OC4yNCBDIDQ0MS4xODcgMjc2LjcxMSA0MzUuNzEgMzA1LjI5MiA0MjUuMzA4IDMzMy4yODIgQyAzNzEuMTAxIDQxNi43MjcgMjc0Ljk1MSA0NDkuMjEzIDE2Ny42NjkgNDM0LjI3OSBDIDEzNi40MDYgNDE2LjAzNiAxMDkuMDA5IDM5MC4wNzYgODYuMjcyIDM1OC4wMTYgQyA2Ny43NzggMzIzLjc5NCA1Ny45NDIgMjg0LjQgNTYuNDg5IDI0Mi4wNjIgQyA2Mi4yMDYgMTk5Ljc2NiA3OS42MTcgMTYxLjkxMiAxMDYuMzAxIDEyOS42MTcgQyAxNjIuNTI0IDg1LjQzOSAyMzkuMDMgNzAuMTgxIDMyMS45NjggODIuNTIgQyAzNDUuNTA4IDkyLjUzNSAzNjguNjU3IDEwNS44ODMgMzkwLjk5MiAxMjIuMjc2IFoiIHN0eWxlPSJmaWxsOiByZ2IoMjU1LCAyNTUsIDI1NSk7IiBieDpvcmlnaW49IjAgMCIvPgogIDxnIHRyYW5zZm9ybT0ibWF0cml4KDAuNTE3ODQ4LCAwLCAwLCAwLjUxNzg0OCwgLTUzLjMwNjYyNSwgLTU5OS45MzEyMTMpIiBzdHlsZT0ib3BhY2l0eTogMTsiPgogICAgPGcgaWQ9ImctMTQiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDAuNTsiIHRyYW5zZm9ybT0ibWF0cml4KDEsIDAsIDAsIDEsIDE0NC41NzA3MjQsIDEwMDcuMDk5NDI2KSI+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OmlubGluZTtmaWxsOiNmZmQwODY7ZmlsbC1vcGFjaXR5OjE7ZmlsbC1ydWxlOmV2ZW5vZGQ7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjFweDtzdHJva2UtbGluZWNhcDpidXR0O3N0cm9rZS1saW5lam9pbjptaXRlcjtzdHJva2Utb3BhY2l0eToxIiBkPSJNIDU4NS4xNjk5Miw1MjQuOTEyMTEgQyA0NDcuNDE3NDUsNzM4Ljg0MDE1IDI4NS45MzA5Myw3OTcuNjgxNDIgOTMuMzQ3NjU2LDgwMS4wMzcxMSAxNTguNjY0NTIsOTIxLjg0MDgzIDI4Ny4yMDAzOCwxMDAzLjE2OTggNDM0LjAzOTA2LDEwMDEuMjU1OSA2MDcuNTY4NTEsOTk4Ljk5NDQ2IDc1Mi41MTMxNyw4ODEuMTg0ODggNzk2LjY1MjM0LDcyMS45NjY4IGMgLTIuNjg2NCwtNi41Nzc2NCAtNi4yMDEwNiwtMTMuNjIwMzcgLTEwLjgxNjQsLTIxLjEzNDc3IEMgNzY4LjY4OTg3LDY3Ny4wMzg3OCA3MDkuMTA0NzgsNTY4LjQ3NzIxIDU4NS4xNjk5Miw1MjQuOTEyMTEgWiIgaWQ9InBhdGgtMTAyIi8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OmlubGluZTtmaWxsOiMyNzBiMGI7ZmlsbC1vcGFjaXR5OjAuOTkzOTM5Mzk7ZmlsbC1ydWxlOmV2ZW5vZGQ7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjFweDtzdHJva2UtbGluZWNhcDpidXR0O3N0cm9rZS1saW5lam9pbjptaXRlcjtzdHJva2Utb3BhY2l0eToxIiBkPSJNIDU4Ni4yNTQgNTI0LjExNSBDIDU2NC42ODIgNjUwLjQ1MyA0NzcuOTc0IDc1NC40NzIgNDk4LjU4NCA4MzUuNjAyIEMgNTI0LjY0OCA5MzguMTk5IDQxOS40NTggOTYxLjUxNSAzMzMuOTczIDk4OS4zNDIgQyAzNjUuOTIzIDk5Ny41NDUgMzk5LjQ3NSAxMDAxLjcwNiA0MzQuMDM5IDEwMDEuMjU2IEMgNjM0LjA1MiA5OTguNjQ5IDc5Ni4wOTMgODQyLjUzNiA4MDkuNTYxIDY0Ni40MzkgQyA3OTMuMTk3IDY0MS43NzcgNzc3LjQyNSA2MzQuNjg5IDc2Ni43MjMgNjIyLjc0NiBDIDczNC4wMzUgNTg2LjI3MiA2NTAuMTE3IDU0Ni41NjQgNTg2LjI1NCA1MjQuMTE1IFoiIGlkPSJwYXRoLTEwMyIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYigyMDMsIDEzNywgMyk7IGZpbGwtb3BhY2l0eTogMC45OTM5Mzk7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyIgZD0iTSA1ODYuMjU0IDUyNC4xMTUgQyA1NjQuNjgyIDY1MC40NTMgNDc3Ljk3NCA3NTQuNDcyIDQ5OC41ODQgODM1LjYwMiBDIDUyNC42NDggOTM4LjE5OSA0MTkuNDU4IDk2MS41MTUgMzMzLjk3MyA5ODkuMzQyIEMgMzY1LjkyMyA5OTcuNTQ1IDM5OS40NzUgMTAwMS43MDYgNDM0LjAzOSAxMDAxLjI1NiBDIDYzNC4wNTIgOTk4LjY0OSA3OTYuMDkzIDg0Mi41MzYgODA5LjU2MSA2NDYuNDM5IEMgNzkzLjE5NyA2NDEuNzc3IDc3Ny40MjUgNjM0LjY4OSA3NjYuNzIzIDYyMi43NDYgQyA3MzQuMDM1IDU4Ni4yNzIgNjUwLjExNyA1NDYuNTY0IDU4Ni4yNTQgNTI0LjExNSBaIiBpZD0icGF0aC0xMDQiIGJ4Om9yaWdpbj0iMC41IDAuNSIvPgogICAgPC9nPgogICAgPGcgaWQ9ImctMTUiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDAuNTsiIHRyYW5zZm9ybT0ibWF0cml4KDEsIDAsIDAsIDEsIDE0NC41NzA3MjQsIDEwMDcuMDk5NDI2KSI+CiAgICAgIDxyZWN0IHg9IjUwNC4yNjUiIHk9IjUwMC4yODciIHdpZHRoPSIyMy40OTIiIGhlaWdodD0iMjUuNTI3IiBzdHlsZT0iZGlzcGxheTppbmxpbmU7b3BhY2l0eToxO2ZpbGw6I2ZhYmIzNztmaWxsLW9wYWNpdHk6MTtzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MzQuOTAwMDAxNTM7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eTowLjk5NjA3ODQzIiBpZD0icGF0aC0xMDUiLz4KICAgICAgPHJlY3QgeD0iMzY5LjE3NCIgeT0iNDE1LjQyOSIgd2lkdGg9IjIyLjM4NCIgaGVpZ2h0PSIyNC40MTkiIHN0eWxlPSJkaXNwbGF5OmlubGluZTtvcGFjaXR5OjE7ZmlsbDojZmZkMDg2O2ZpbGwtb3BhY2l0eToxO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDozNC45MDAwMDE1MztzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjAuOTk2MDc4NDMiIGlkPSJwYXRoLTEwNiIvPgogICAgICA8cmVjdCB4PSI0MTguOTk1IiB5PSI0MzMuMDE5IiB3aWR0aD0iMzAuNTI0IiBoZWlnaHQ9IjMwLjUyNCIgc3R5bGU9ImRpc3BsYXk6aW5saW5lO29wYWNpdHk6MTtmaWxsOiNmYWJiMzc7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjM0LjkwMDAwMTUzO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MC45OTYwNzg0MyIgaWQ9InBhdGgtMTA3Ii8+CiAgICAgIDxyZWN0IHg9IjQxNy4zNCIgeT0iNjUyLjU1NiIgd2lkdGg9IjQ2LjgwMyIgaGVpZ2h0PSI0Ni44MDMiIHN0eWxlPSJkaXNwbGF5OmlubGluZTtvcGFjaXR5OjE7ZmlsbDojZmJjMTRjO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDozNC45MDAwMDE1MztzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjAuOTk2MDc4NDMiIGlkPSJwYXRoLTEwOCIvPgogICAgICA8cmVjdCB4PSI0MjIuNTg2IiB5PSI0NzUuODkxIiB3aWR0aD0iMzAuNTI0IiBoZWlnaHQ9IjMwLjUyNCIgc3R5bGU9ImRpc3BsYXk6aW5saW5lO29wYWNpdHk6MTtmaWxsOiNmYWJiMzc7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjM0LjkwMDAwMTUzO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MC45OTYwNzg0MyIgaWQ9InBhdGgtMTA5Ii8+CiAgICAgIDxyZWN0IHg9IjQ3Mi42MTgiIHk9IjYwNS40NTciIHdpZHRoPSIyNC40MTkiIGhlaWdodD0iMjYuNDU0IiBzdHlsZT0iZGlzcGxheTppbmxpbmU7b3BhY2l0eToxO2ZpbGw6I2NjODkwMjtmaWxsLW9wYWNpdHk6MTtzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MzQuOTAwMDAxNTM7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eTowLjk5NjA3ODQzIiBpZD0icGF0aC0xMTAiLz4KICAgICAgPHJlY3QgeD0iNTIwLjc3MiIgeT0iNTU3LjkwMiIgd2lkdGg9IjE4LjMxNCIgaGVpZ2h0PSIxOC4zMTQiIHN0eWxlPSJkaXNwbGF5OmlubGluZTtvcGFjaXR5OjE7ZmlsbDojZmFiYjM3O2ZpbGwtb3BhY2l0eToxO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDozNC45MDAwMDE1MztzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjAuOTk2MDc4NDMiIGlkPSJwYXRoLTExMSIvPgogICAgICA8cmVjdCB4PSI0NTQuNzg0IiB5PSI1NjMuMDI4IiB3aWR0aD0iMzAuNTI0IiBoZWlnaHQ9IjMwLjUyNCIgc3R5bGU9ImRpc3BsYXk6aW5saW5lO29wYWNpdHk6MTtmaWxsOiNmYWJiMzc7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjM0LjkwMDAwMTUzO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MC45OTYwNzg0MyIgaWQ9InBhdGgtMTEyIi8+CiAgICAgIDxyZWN0IHg9IjMzNS4zNDIiIHk9IjcyMC45ODciIHdpZHRoPSIzOC42NjMiIGhlaWdodD0iNDAuNjk4IiBzdHlsZT0iZGlzcGxheTppbmxpbmU7b3BhY2l0eToxO2ZpbGw6I2ZiYzE0YztmaWxsLW9wYWNpdHk6MTtzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MzQuOTAwMDAxNTM7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eTowLjk5NjA3ODQzIiBpZD0icGF0aC0xMTMiLz4KICAgICAgPHJlY3QgeD0iMzcxLjk3IiB5PSI2NjEuOTc1IiB3aWR0aD0iMjYuNDU0IiBoZWlnaHQ9IjMwLjUyNCIgc3R5bGU9ImRpc3BsYXk6aW5saW5lO29wYWNpdHk6MTtmaWxsOiNmYmMxNGM7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOm5vbmU7c3Ryb2tlLXdpZHRoOjM0LjkwMDAwMTUzO3N0cm9rZS1taXRlcmxpbWl0OjQ7c3Ryb2tlLWRhc2hhcnJheTpub25lO3N0cm9rZS1kYXNob2Zmc2V0OjA7c3Ryb2tlLW9wYWNpdHk6MC45OTYwNzg0MyIgaWQ9InBhdGgtMTE0Ii8+CiAgICAgIDxyZWN0IHg9Ii00MjcuOTMiIHk9IjYxMC4wODUiIHdpZHRoPSIyNC41MDciIGhlaWdodD0iMjQuNDE5IiBzdHlsZT0iZGlzcGxheTppbmxpbmU7b3BhY2l0eToxO2ZpbGw6I2NjODkwMjtmaWxsLW9wYWNpdHk6MTtzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MzQuOTAwMDAxNTM7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eTowLjk5NjA3ODQzIiBpZD0icGF0aC0xMTUiIHRyYW5zZm9ybT0ic2NhbGUoLTEsMSkiLz4KICAgICAgPHJlY3QgeD0iNDgwLjQxMSIgeT0iNTIzLjQ2OSIgd2lkdGg9IjIwLjM0OSIgaGVpZ2h0PSIyMi4zODQiIHN0eWxlPSJkaXNwbGF5OmlubGluZTtvcGFjaXR5OjE7ZmlsbDojZmZkMDg2O2ZpbGwtb3BhY2l0eToxO3N0cm9rZTpub25lO3N0cm9rZS13aWR0aDozNC45MDAwMDE1MztzdHJva2UtbWl0ZXJsaW1pdDo0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2UtZGFzaG9mZnNldDowO3N0cm9rZS1vcGFjaXR5OjAuOTk2MDc4NDMiIGlkPSJwYXRoLTExNiIvPgogICAgICA8cmVjdCB4PSItNDk5LjUiIHk9IjQ2Ni4zMTQiIHdpZHRoPSIyNC41MDciIGhlaWdodD0iMjQuNDE5IiBzdHlsZT0iZGlzcGxheTppbmxpbmU7b3BhY2l0eToxO2ZpbGw6I2NjODkwMjtmaWxsLW9wYWNpdHk6MTtzdHJva2U6bm9uZTtzdHJva2Utd2lkdGg6MzQuOTAwMDAxNTM7c3Ryb2tlLW1pdGVybGltaXQ6NDtzdHJva2UtZGFzaGFycmF5Om5vbmU7c3Ryb2tlLWRhc2hvZmZzZXQ6MDtzdHJva2Utb3BhY2l0eTowLjk5NjA3ODQzIiBpZD0icGF0aC0xMTciIHRyYW5zZm9ybT0ic2NhbGUoLTEsMSkiLz4KICAgIDwvZz4KICAgIDxnIGlkPSJnLTE2IiBzdHlsZT0iZGlzcGxheTogaW5saW5lOyBvcGFjaXR5OiAxOyIgdHJhbnNmb3JtPSJtYXRyaXgoMS4wMDAwMDAwNzAwMDIwNDk1LCAwLCAwLCAxLjAwMDAwMDA3MDAwMjA0OTUsIDE0NC41NzA3MjM5MTA3OTYxMiwgMTAwNy4wOTk0Mzk4NzE1OTk0KSI+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IG5vbmU7IGZpbGwtb3BhY2l0eTogMTsgZmlsbC1ydWxlOiBldmVub2RkOyBzdHJva2U6IHJnYigyNTUsIDEyMiwgMCk7IHN0cm9rZS13aWR0aDogMi4zNDg4ODsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyIgZD0iTSA1ODUuMTY5OTIsNTI0LjkxMjExIEMgNDQ3LjQxNzQ1LDczOC44NDAxNSAyODUuOTMwOTMsNzk3LjY4MTQyIDkzLjM0NzY1Niw4MDEuMDM3MTEgMTU4LjY2NDUyLDkyMS44NDA4MyAyODcuMjAwMzgsMTAwMy4xNjk4IDQzNC4wMzkwNiwxMDAxLjI1NTkgNjA3LjU2ODUxLDk5OC45OTQ0NiA3NTIuNTEzMTcsODgxLjE4NDg4IDc5Ni42NTIzNCw3MjEuOTY2OCBjIC0yLjY4NjQsLTYuNTc3NjQgLTYuMjAxMDYsLTEzLjYyMDM3IC0xMC44MTY0LC0yMS4xMzQ3NyBDIDc2OC42ODk4Nyw2NzcuMDM4NzggNzA5LjEwNDc4LDU2OC40NzcyMSA1ODUuMTY5OTIsNTI0LjkxMjExIFoiIGlkPSJwYXRoLTExOCIvPgogICAgICA8cGF0aCBzdHlsZT0iZGlzcGxheTogaW5saW5lOyBmaWxsOiBub25lOyBmaWxsLW9wYWNpdHk6IDAuOTkzOTM5OyBmaWxsLXJ1bGU6IGV2ZW5vZGQ7IHN0cm9rZTogcmdiKDI1NSwgMTIyLCAwKTsgc3Ryb2tlLXdpZHRoOiAyLjM0ODg4OyBzdHJva2UtbGluZWNhcDogYnV0dDsgc3Ryb2tlLWxpbmVqb2luOiBtaXRlcjsgc3Ryb2tlLW9wYWNpdHk6IDE7IiBkPSJNIDU4Ni4yNTQgNTI0LjExNSBDIDU2NC42ODIgNjUwLjQ1MyA0NzcuOTc0IDc1NC40NzIgNDk4LjU4NCA4MzUuNjAyIEMgNTI0LjY0OCA5MzguMTk5IDQxOS40NTggOTYxLjUxNSAzMzMuOTczIDk4OS4zNDIgQyAzNjUuOTIzIDk5Ny41NDUgMzk5LjQ3NSAxMDAxLjcwNiA0MzQuMDM5IDEwMDEuMjU2IEMgNjM0LjA1MiA5OTguNjQ5IDc5Ni4wOTMgODQyLjUzNiA4MDkuNTYxIDY0Ni40MzkgQyA3OTMuMTk3IDY0MS43NzcgNzc3LjQyNSA2MzQuNjg5IDc2Ni43MjMgNjIyLjc0NiBDIDczNC4wMzUgNTg2LjI3MiA2NTAuMTE3IDU0Ni41NjQgNTg2LjI1NCA1MjQuMTE1IFoiIGlkPSJwYXRoLTExOSIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IG5vbmU7IGZpbGwtb3BhY2l0eTogMC45OTM5Mzk7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiByZ2IoMjU1LCAxMjIsIDApOyBzdHJva2Utd2lkdGg6IDIuMzQ4ODg7IHN0cm9rZS1saW5lY2FwOiBidXR0OyBzdHJva2UtbGluZWpvaW46IG1pdGVyOyBzdHJva2Utb3BhY2l0eTogMTsiIGQ9Ik0gNTg2LjI1NCA1MjQuMTE1IEMgNTY0LjY4MiA2NTAuNDUzIDQ3Ny45NzQgNzU0LjQ3MiA0OTguNTg0IDgzNS42MDIgQyA1MjQuNjQ4IDkzOC4xOTkgNDE5LjQ1OCA5NjEuNTE1IDMzMy45NzMgOTg5LjM0MiBDIDM2NS45MjMgOTk3LjU0NSAzOTkuNDc1IDEwMDEuNzA2IDQzNC4wMzkgMTAwMS4yNTYgQyA2MzQuMDUyIDk5OC42NDkgNzk2LjA5MyA4NDIuNTM2IDgwOS41NjEgNjQ2LjQzOSBDIDc5My4xOTcgNjQxLjc3NyA3NzcuNDI1IDYzNC42ODkgNzY2LjcyMyA2MjIuNzQ2IEMgNzM0LjAzNSA1ODYuMjcyIDY1MC4xMTcgNTQ2LjU2NCA1ODYuMjU0IDUyNC4xMTUgWiIgaWQ9InBhdGgtMTIwIiBieDpvcmlnaW49IjAuNSAwLjUiLz4KICAgIDwvZz4KICA8L2c+CiAgPGcgdHJhbnNmb3JtPSJtYXRyaXgoMC42MDgyNjEsIDAsIDAsIDAuNjA4MjYxLCAtMjAuMDg0OTc2LCAzLjI1NTczNikiPgogICAgPGc+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYig2NCwgMTc4LCAyNTUpOyBmaWxsLW9wYWNpdHk6IDE7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyBvcGFjaXR5OiAwLjc4OyIgaWQ9InBhdGgtMTAiIGQ9Ik0gNDE5LjEzMyA4NS43MzggQyA0MTcuMjk1IDg1LjczOSA0MTUuNDU1IDg1Ljc1NCA0MTMuNjExIDg1Ljc3OCBDIDM1NC44NTIgODYuNTYxIDI5OS4wMjMgOTkuMTc0IDI0OC4zNTggMTIxLjMyIEMgNTg0LjEyMSAxMy4yMDYgNzk2LjE3NSAyMTEuMjYgODMxLjcgNTA4Ljc3MyBMIDg0Ni4zODMgNTA3LjEyNCBDIDgxNS41ODYgMjM1LjgwMSA2NTIuNDc3IDg1LjU4OCA0MTkuMTMzIDg1LjczOCBaIiB0cmFuc2Zvcm09Im1hdHJpeCgwLjk2MzczLCAwLjI2Njg3OCwgLTAuMjY2ODc4LCAwLjk2MzczLCA4OS41MDUyMzEsIC0xMzYuNjE1MDYxKSIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYig2NCwgMTc4LCAyNTUpOyBmaWxsLW9wYWNpdHk6IDE7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyBvcGFjaXR5OiAxOyIgaWQ9InBhdGgtMyIgZD0iTSAxNDYuMDM3IDE5NC4wNjUgQyAxNDQuMjAxIDE5NC4wNjYgMTQyLjM2MSAxOTQuMDgxIDE0MC41MTcgMTk0LjEwNCBDIDgxLjc2IDE5NC44ODggMjUuOTMxIDIwNy41MDMgLTI0LjczNiAyMjkuNjQ4IEMgMzExLjAyOSAxMjEuNTMxIDUyMy4wODMgMzE5LjU4MyA1NTguNjA0IDYxNy4wOTMgTCA1NzMuMjg0IDYxNS40NDMgQyA1NDIuNDkgMzQ0LjEyMiAzNzkuMzg1IDE5My45MTMgMTQ2LjAzNyAxOTQuMDY1IFoiIHRyYW5zZm9ybT0ibWF0cml4KC0wLjE4MDk4OCwgLTAuOTgzNDg1LCAwLjk4MzQ4NSwgLTAuMTgwOTg4LCAtMzkuMjk4NTgxLCA3MDUuODk3NTQ0KSIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYig2NCwgMTc4LCAyNTUpOyBmaWxsLW9wYWNpdHk6IDE7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyBvcGFjaXR5OiAwLjQ7IiBpZD0icGF0aC0xMSIgZD0iTSA0ODcuNzQ0IDE5My4wODQgQyA0ODUuOTA2IDE5My4wODUgNDg0LjA2NyAxOTMuMSA0ODIuMjIzIDE5My4xMjMgQyA0MjMuNDY0IDE5My45MDcgMzY3LjYzNSAyMDYuNTIgMzE2Ljk3NSAyMjguNjY4IEMgNjUyLjcyNiAxMjAuNTUyIDg2NC43NzUgMzE4LjYwNSA5MDAuMjk5IDYxNi4xMDkgTCA5MTQuOTc5IDYxNC40NiBDIDg4NC4xODQgMzQzLjE0NCA3MjEuMDgxIDE5Mi45MzUgNDg3Ljc0NCAxOTMuMDg0IFoiIHRyYW5zZm9ybT0ibWF0cml4KDAuNjE5OTk3LCAwLjc4NDYwNSwgLTAuNzg0NjA1LCAwLjYxOTk5NywgNTIzLjA2NzIzMSwgLTM0My4zMzE1MzkpIiBieDpvcmlnaW49IjAuNSAwLjUiLz4KICAgICAgPHBhdGggc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgZmlsbDogcmdiKDY0LCAxNzgsIDI1NSk7IGZpbGwtb3BhY2l0eTogMTsgZmlsbC1ydWxlOiBldmVub2RkOyBzdHJva2U6IG5vbmU7IHN0cm9rZS13aWR0aDogMXB4OyBzdHJva2UtbGluZWNhcDogYnV0dDsgc3Ryb2tlLWxpbmVqb2luOiBtaXRlcjsgc3Ryb2tlLW9wYWNpdHk6IDE7IG9wYWNpdHk6IDAuODQ7IiBpZD0icGF0aC0xMiIgZD0iTSAyOTUuOTY5IDQzMi43NTcgQyAyOTQuMTMxIDQzMi43NTggMjkyLjI5MyA0MzIuNzczIDI5MC40NDkgNDMyLjc5NyBDIDIzMS42ODggNDMzLjU4IDE3NS44NiA0NDYuMTk0IDEyNS4xOTggNDY4LjM0MSBDIDQ2MC45NTcgMzYwLjIyNiA2NzMuMDA1IDU1OC4yOCA3MDguNTI2IDg1NS43ODYgTCA3MjMuMjA3IDg1NC4xMzkgQyA2OTIuNDEyIDU4Mi44MTkgNTI5LjMxIDQzMi42MDkgMjk1Ljk2OSA0MzIuNzU3IFoiIHRyYW5zZm9ybT0ibWF0cml4KC0wLjk3ODg0OSwgMC4yMDQ1ODQsIC0wLjIwNDU4NCwgLTAuOTc4ODQ5LCA5NjMuODIwNzk2LCAxMTE2LjM2NzI5MykiIGJ4Om9yaWdpbj0iMC41IDAuNSIvPgogICAgICA8cGF0aCBzdHlsZT0iZGlzcGxheTogaW5saW5lOyBmaWxsOiByZ2IoNjQsIDE3OCwgMjU1KTsgZmlsbC1vcGFjaXR5OiAxOyBmaWxsLXJ1bGU6IGV2ZW5vZGQ7IHN0cm9rZTogbm9uZTsgc3Ryb2tlLXdpZHRoOiAxcHg7IHN0cm9rZS1saW5lY2FwOiBidXR0OyBzdHJva2UtbGluZWpvaW46IG1pdGVyOyBzdHJva2Utb3BhY2l0eTogMTsgb3BhY2l0eTogMC40OyIgaWQ9InBhdGgtMTMiIGQ9Ik0gMTU4LjAzNiAxNDguODMgQyAxNTYuMiAxNDguODMzIDE1NC4zNiAxNDguODQ2IDE1Mi41MTYgMTQ4Ljg3MiBDIDkzLjc1OSAxNDkuNjUzIDM3LjkzIDE2Mi4yNjcgLTEyLjczNCAxODQuNDExIEMgMzIzLjAyMiA3Ni4zMDEgNTM1LjA3NiAyNzQuMzUyIDU3MC42MDQgNTcxLjg1NyBMIDU4NS4yODYgNTcwLjIwOCBDIDU1NC40ODMgMjk4Ljg5MSAzOTEuMzggMTQ4LjY4MyAxNTguMDM2IDE0OC44MyBaIiB0cmFuc2Zvcm09Im1hdHJpeCgwLjMzODYxNCwgLTAuOTQwOTI2LCAwLjk0MDkyNiwgMC4zMzg2MTQsIC0xMTUuNTk1MTU4LCA0ODMuNzA1OTg0KSIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYig2NCwgMTc4LCAyNTUpOyBmaWxsLW9wYWNpdHk6IDE7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyBvcGFjaXR5OiAxOyIgaWQ9InBhdGgtMTQiIGQ9Ik0gNTAxLjAxOSAyODguMjkyIEMgNDk5LjE4MSAyODguMjkzIDQ5Ny4zNCAyODguMzA4IDQ5NS40OTYgMjg4LjMzMSBDIDQzNi43MzYgMjg5LjExNSAzODAuOTA4IDMwMS43MjYgMzMwLjI0MyAzMjMuODc0IEMgNjY2LjAwNCAyMTUuNzU3IDg3OC4wNjIgNDEzLjgxMiA5MTMuNTg2IDcxMS4zMjIgTCA5MjguMjY4IDcwOS42NzMgQyA4OTcuNDY4IDQzOC4zNTIgNzM0LjM2MSAyODguMTQxIDUwMS4wMTkgMjg4LjI5MiBaIiB0cmFuc2Zvcm09Im1hdHJpeCgwLjE2NDY1NiwgMC45ODYzNTEsIC0wLjk4NjM1MSwgMC4xNjQ2NTYsIDk4Mi44NTcyNjYsIC0yMzMuNDUyMDYzKSIgYng6b3JpZ2luPSIwLjUgMC41Ii8+CiAgICAgIDxwYXRoIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IGZpbGw6IHJnYig2NCwgMTc4LCAyNTUpOyBmaWxsLW9wYWNpdHk6IDE7IGZpbGwtcnVsZTogZXZlbm9kZDsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDFweDsgc3Ryb2tlLWxpbmVjYXA6IGJ1dHQ7IHN0cm9rZS1saW5lam9pbjogbWl0ZXI7IHN0cm9rZS1vcGFjaXR5OiAxOyBvcGFjaXR5OiAwLjQ7IiBpZD0icGF0aC0xNSIgZD0iTSAxOTIuOTU0IDM3Ni41MjEgQyAxOTEuMTE1IDM3Ni41MjIgMTg5LjI3NyAzNzYuNTM3IDE4Ny40MzMgMzc2LjU2MSBDIDEyOC42NzQgMzc3LjM0NCA3Mi44NDUgMzg5Ljk1OCAyMi4xODEgNDEyLjEwMyBDIDM1Ny45NCAzMDMuOTg5IDU2OS45ODcgNTAyLjA0NSA2MDUuNTA1IDc5OS41NTMgTCA2MjAuMTg4IDc5Ny45MDUgQyA1ODkuMzkyIDUyNi41ODQgNDI2LjI5NSAzNzYuMzY5IDE5Mi45NTQgMzc2LjUyMSBaIiB0cmFuc2Zvcm09Im1hdHJpeCgtMC45MzE0MDQsIC0wLjM2Mzk4OCwgMC4zNjM5ODgsIC0wLjkzMTQwNCwgNDE5LjQ5OTAwNiwgMTE4Mi41OTk4MDkpIiBieDpvcmlnaW49IjAuNSAwLjUiLz4KICAgICAgPHBhdGggc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgZmlsbDogcmdiKDY0LCAxNzgsIDI1NSk7IGZpbGwtb3BhY2l0eTogMTsgZmlsbC1ydWxlOiBldmVub2RkOyBzdHJva2U6IG5vbmU7IHN0cm9rZS13aWR0aDogMXB4OyBzdHJva2UtbGluZWNhcDogYnV0dDsgc3Ryb2tlLWxpbmVqb2luOiBtaXRlcjsgc3Ryb2tlLW9wYWNpdHk6IDE7IG9wYWNpdHk6IDE7IiBpZD0icGF0aC0xNiIgZD0iTSAyMjEuNjE0IDYzLjk3MyBDIDIxOS43NzcgNjMuOTc2IDIxNy45MzggNjMuOTkgMjE2LjA5NCA2NC4wMTUgQyAxNTcuMzM0IDY0Ljc5NSAxMDEuNTA2IDc3LjQwOSA1MC44NDEgOTkuNTU0IEMgMzg2LjU5OCAtOC41NTkgNTk4LjY1MSAxODkuNDk3IDYzNC4xNzMgNDg3LjAwMSBMIDY0OC44NTQgNDg1LjM1NCBDIDYxOC4wNTggMjE0LjAzNyA0NTQuOTU0IDYzLjgyNCAyMjEuNjE0IDYzLjk3MyBaIiB0cmFuc2Zvcm09Im1hdHJpeCgwLjc1MDQ1MiwgLTAuNjYwOTI1LCAwLjY2MDkyNSwgMC43NTA0NTIsIC03MC44MDMzMjIsIDI5MC45MjAyNzMpIiBieDpvcmlnaW49IjAuNSAwLjUiLz4KICAgICAgPHBhdGggc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgZmlsbDogcmdiKDY0LCAxNzgsIDI1NSk7IGZpbGwtb3BhY2l0eTogMTsgZmlsbC1ydWxlOiBldmVub2RkOyBzdHJva2U6IG5vbmU7IHN0cm9rZS13aWR0aDogMXB4OyBzdHJva2UtbGluZWNhcDogYnV0dDsgc3Ryb2tlLWxpbmVqb2luOiBtaXRlcjsgc3Ryb2tlLW9wYWNpdHk6IDE7IG9wYWNpdHk6IDAuNDsiIGlkPSJwYXRoLTE3IiBkPSJNIDQ0MS44ODggMzQyLjA5OCBDIDQ0MC4wNDcgMzQyLjA5OCA0MzguMjA3IDM0Mi4xMTQgNDM2LjM2MiAzNDIuMTM4IEMgMzc3LjYwMSAzNDIuOTIyIDMyMS43NjUgMzU1LjUzNSAyNzEuMTAzIDM3Ny42ODEgQyA2MDYuODg2IDI2OS41NjcgODE4Ljk0NCA0NjcuNjE1IDg1NC40NjIgNzY1LjEzMyBMIDg2OS4xNDIgNzYzLjQ4MyBDIDgzOC4zNTQgNDkyLjE1NCA2NzUuMjQyIDM0MS45NDggNDQxLjg4OCAzNDIuMDk4IFoiIHRyYW5zZm9ybT0ibWF0cml4KC0wLjM1NjU4MiwgMC45MzQyNjQsIC0wLjkzNDI2NCwgLTAuMzU2NTgyLCAxMjU2Ljc1OTQ5MywgMTY5LjE4MjU1MikiIGJ4Om9yaWdpbj0iMC41IDAuNSIvPgogICAgICA8ZyB0cmFuc2Zvcm09Im1hdHJpeCgwLjc2MjQ5MywgMCwgMCwgMC43NjI0OTMsIDE5LjI5NDY0NywgLTcxNi4zMjI4MTUpIj4KICAgICAgICA8cGF0aCBkPSJNIDU2Ni40NjYgMTA4OS4xMzcgTCA1NjYuNDY2IDExNDguMjIyIEMgNTY1Ljg2NyAxMTQ4LjIxOSA1NjUuMjY3IDExNDguMjE3IDU2NC42NjcgMTE0OC4yMTcgQyA1NjEuNDgxIDExNDguMjE3IDU1OC4zMSAxMTQ4LjI1OSA1NTUuMTU0IDExNDguMzQxIEwgNTU1LjE1NCAxMDg5LjEzNyBaIE0gOTA4LjI5IDE0NzcuMTQ2IEwgOTU2Ljk4NyAxNDc3LjE0NiBMIDk1Ni45ODcgMTQ4OC40NTggTCA5MDguNTY5IDE0ODguNDU4IEMgOTA4LjUzOSAxNDg0LjY5MyA5MDguNDQ3IDE0ODAuOTIyIDkwOC4yOSAxNDc3LjE0NiBaIE0gNTY2LjQ2NiAxODM1LjAxMyBMIDU2Ni40NjYgMTg3Ni40NjcgTCA1NTUuMTU0IDE4NzYuNDY3IEwgNTU1LjE1NCAxODM0Ljg4NiBDIDU1OC4zMTggMTgzNC45NzMgNTYxLjQ5IDE4MzUuMDE3IDU2NC42NjcgMTgzNS4wMTcgQyA1NjUuMjY3IDE4MzUuMDE3IDU2NS44NjcgMTgzNS4wMTYgNTY2LjQ2NiAxODM1LjAxMyBaIE0gMjIwLjc3NSAxNDg4LjQ1OCBMIDE2OS42NTcgMTQ4OC40NTggTCAxNjkuNjU3IDE0NzcuMTQ2IEwgMjIxLjA4OSAxNDc3LjE0NiBDIDIyMC45MjEgMTQ4MC45MDkgMjIwLjgxNiAxNDg0LjY4IDIyMC43NzUgMTQ4OC40NTggWiIgc3R5bGU9ImZpbGw6IHJnYig2NCwgNDAsIDApOyBzdHJva2U6IHJnYig2NCwgNDAsIDApOyBzdHJva2Utd2lkdGg6IDEwLjc4MDY7IiBieDpvcmlnaW49IjAgMCIvPgogICAgICAgIDxwYXRoIGQ9Ik0gNzYwLjEyMSAxMTQzLjMxMSBMIDcxNy4zMzYgMTIxNy40MTcgQyA3MTUuNzIgMTIxNi41MTggNzE0LjA5NiAxMjE1LjYzMiA3MTIuNDY1IDEyMTQuNzYxIEwgNzU1LjMxOCAxMTQwLjUzNyBaIE0gODMwLjEyMiAxMzI0LjIwMyBMIDkwMC40MjQgMTI4My42MTQgTCA5MDMuMTk4IDEyODguNDE3IEwgODMzLjA0IDEzMjguOTIzIEMgODMyLjA4IDEzMjcuMzM5IDgzMS4xMDcgMTMyNS43NjYgODMwLjEyMiAxMzI0LjIwMyBaIE0gODQwLjU5OSAxNjQxLjEzNyBMIDkwMy4xOTcgMTY3Ny4yNzggTCA5MDAuNDI0IDE2ODIuMDgyIEwgODM3LjkxMSAxNjQ1Ljk5IEMgODM4LjgyMSAxNjQ0LjM4IDgzOS43MTcgMTY0Mi43NjIgODQwLjU5OSAxNjQxLjEzNyBaIE0gNzI1LjE2OCAxNzYxLjg0MyBMIDc2MC4xMjIgMTgyMi4zODUgTCA3NTUuMzE4IDE4MjUuMTU4IEwgNzIwLjM3OSAxNzY0LjY0MSBDIDcyMS45ODUgMTc2My43MjIgNzIzLjU4MiAxNzYyLjc4OSA3MjUuMTY4IDE3NjEuODQzIFogTSA0MDMuNTM4IDE3NjAuOTMyIEwgMzY2LjQ1NiAxODI1LjE1OSBMIDM2MS42NTMgMTgyMi4zODYgTCAzOTguODAyIDE3NTguMDQyIEMgNDAwLjM3MiAxNzU5LjAxOSA0MDEuOTUxIDE3NTkuOTgyIDQwMy41MzggMTc2MC45MzIgWiBNIDI4OS41OCAxNjQyLjY4OSBMIDIyMS4zNSAxNjgyLjA4MiBMIDIxOC41NzcgMTY3Ny4yNzkgTCAyODYuOTQ0IDE2MzcuODA3IEMgMjg3LjgwNyAxNjM5LjQzNyAyODguNjg1IDE2NDEuMDY1IDI4OS41OCAxNjQyLjY4OSBaIE0gMjk0LjM1NCAxMzMyLjE2OCBMIDIxOC41NzYgMTI4OC40MTcgTCAyMjEuMzUgMTI4My42MTQgTCAyOTcuMjEzIDEzMjcuNDE0IEMgMjk2LjI0NiAxMzI4Ljk5IDI5NS4yOTMgMTMzMC41NzUgMjk0LjM1NCAxMzMyLjE2OCBaIE0gNDA2LjA3OSAxMjIwLjI1OSBMIDM2MS42NTMgMTE0My4zMTEgTCAzNjYuNDU3IDExNDAuNTM3IEwgNDEwLjg4OSAxMjE3LjQ5NSBDIDQwOS4yNzYgMTIxOC40MDMgNDA3LjY3MyAxMjE5LjMyNCA0MDYuMDc5IDEyMjAuMjU5IFoiIHN0eWxlPSJmaWxsOiByZ2IoNjQsIDQwLCAwKTsgc3Ryb2tlOiByZ2IoNjQsIDQwLCAwKTsgc3Ryb2tlLXdpZHRoOiAxMC43ODA2OyIgYng6b3JpZ2luPSIwIDAiLz4KICAgICAgPC9nPgogICAgPC9nPgogICAgPHJlY3QgeD0iNDI5LjM2NyIgeT0iNDI1Ljk3OSIgd2lkdGg9IjIwLjAwMyIgaGVpZ2h0PSIyMS43MzUiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDE7IGZpbGw6IHJnYig4MCwgMTUwLCAyMDApOyBmaWxsLW9wYWNpdHk6IDE7IHN0cm9rZTogbm9uZTsgc3Ryb2tlLXdpZHRoOiAzNC45OyBzdHJva2UtbWl0ZXJsaW1pdDogNDsgc3Ryb2tlLWRhc2hhcnJheTogbm9uZTsgc3Ryb2tlLWRhc2hvZmZzZXQ6IDA7IHN0cm9rZS1vcGFjaXR5OiAwLjk5NjA3ODsiIGlkPSJwYXRoLTkxIiB0cmFuc2Zvcm09Im1hdHJpeCgxLCAwLCAwLCAwLjk5OTk5OSwgLTMwMS4zNzcwNzUsIC0yMTkuNDk4NDM0KSIvPgogICAgPHJlY3QgeD0iNDQ2Ljg1MyIgeT0iMjUuNDYyIiB3aWR0aD0iMjUuOTkiIGhlaWdodD0iMjUuOTkiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDE7IGZpbGw6IHJnYig4MCwgMTUwLCAyMDApOyBmaWxsLW9wYWNpdHk6IDE7IHN0cm9rZTogbm9uZTsgc3Ryb2tlLXdpZHRoOiAzNC45OyBzdHJva2UtbWl0ZXJsaW1pdDogNDsgc3Ryb2tlLWRhc2hhcnJheTogbm9uZTsgc3Ryb2tlLWRhc2hvZmZzZXQ6IDA7IHN0cm9rZS1vcGFjaXR5OiAwLjk5NjA3ODsiIGlkPSJwYXRoLTkzIi8+CiAgICA8cmVjdCB4PSItMTM1Ljk1NiIgeT0iNjA1LjE0MSIgd2lkdGg9IjM5Ljg1MSIgaGVpZ2h0PSIzOS44NTEiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDE7IGZpbGw6IHJnYig4MCwgMTUwLCAyMDApOyBmaWxsLW9wYWNpdHk6IDE7IHN0cm9rZTogbm9uZTsgc3Ryb2tlLXdpZHRoOiAzNC45OyBzdHJva2UtbWl0ZXJsaW1pdDogNDsgc3Ryb2tlLWRhc2hhcnJheTogbm9uZTsgc3Ryb2tlLWRhc2hvZmZzZXQ6IDA7IHN0cm9rZS1vcGFjaXR5OiAwLjk5NjA3ODsiIGlkPSJwYXRoLTk0IiB0cmFuc2Zvcm09Im1hdHJpeCgwLjk5OTk5OSwgMCwgMCwgMSwgODQ2LjE3Njc1NiwgLTM5NC45NTYwMjQpIi8+CiAgICA8cmVjdCB4PSIyMjUuNzc5IiB5PSI2ODkuODM2IiB3aWR0aD0iMjUuOTkiIGhlaWdodD0iMjUuOTkiIHN0eWxlPSJkaXNwbGF5OiBpbmxpbmU7IG9wYWNpdHk6IDE7IGZpbGw6IHJnYig4MCwgMTUwLCAyMDApOyBmaWxsLW9wYWNpdHk6IDE7IHN0cm9rZTogbm9uZTsgc3Ryb2tlLXdpZHRoOiAzNC45OyBzdHJva2UtbWl0ZXJsaW1pdDogNDsgc3Ryb2tlLWRhc2hhcnJheTogbm9uZTsgc3Ryb2tlLWRhc2hvZmZzZXQ6IDA7IHN0cm9rZS1vcGFjaXR5OiAwLjk5NjA3ODsiIGlkPSJwYXRoLTk1Ii8+CiAgICA8cmVjdCB4PSI1OTQuMDEzIiB5PSI3MzcuMTQyIiB3aWR0aD0iMjAuNzkyIiBoZWlnaHQ9IjIyLjUyNSIgc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgb3BhY2l0eTogMTsgZmlsbDogcmdiKDgwLCAxNTAsIDIwMCk7IGZpbGwtb3BhY2l0eTogMTsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDM0Ljk7IHN0cm9rZS1taXRlcmxpbWl0OiA0OyBzdHJva2UtZGFzaGFycmF5OiBub25lOyBzdHJva2UtZGFzaG9mZnNldDogMDsgc3Ryb2tlLW9wYWNpdHk6IDAuOTk2MDc4OyIgaWQ9InBhdGgtOTYiLz4KICAgIDxyZWN0IHg9IjM4Ny4yMzUiIHk9IjQ3OS4zOTciIHdpZHRoPSIyNS45OSIgaGVpZ2h0PSIyNS45OSIgc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgb3BhY2l0eTogMTsgZmlsbDogcmdiKDgwLCAxNTAsIDIwMCk7IGZpbGwtb3BhY2l0eTogMTsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDM0Ljk7IHN0cm9rZS1taXRlcmxpbWl0OiA0OyBzdHJva2UtZGFzaGFycmF5OiBub25lOyBzdHJva2UtZGFzaG9mZnNldDogMDsgc3Ryb2tlLW9wYWNpdHk6IDAuOTk2MDc4OyIgaWQ9InBhdGgtOTgiIHRyYW5zZm9ybT0ibWF0cml4KDEuMDAwMDAxLCAwLCAwLCAwLjk5OTk5OSwgMzY1LjI3NDk2NiwgNzkuMjk1MjQxKSIvPgogICAgPHJlY3QgeD0iNDIxLjk2MiIgeT0iNzc1LjkzNyIgd2lkdGg9IjMyLjkyIiBoZWlnaHQ9IjM0LjY1MyIgc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgb3BhY2l0eTogMTsgZmlsbDogcmdiKDgwLCAxNTAsIDIwMCk7IGZpbGwtb3BhY2l0eTogMTsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDM0Ljk7IHN0cm9rZS1taXRlcmxpbWl0OiA0OyBzdHJva2UtZGFzaGFycmF5OiBub25lOyBzdHJva2UtZGFzaG9mZnNldDogMDsgc3Ryb2tlLW9wYWNpdHk6IDAuOTk2MDc4OyIgaWQ9InBhdGgtOTkiLz4KICAgIDxyZWN0IHg9Ii02Ni45MzkiIHk9Ii01NjguOTc4IiB3aWR0aD0iMjAuODY3IiBoZWlnaHQ9IjIwLjc5MiIgc3R5bGU9ImRpc3BsYXk6IGlubGluZTsgb3BhY2l0eTogMTsgZmlsbDogcmdiKDgwLCAxNTAsIDIwMCk7IGZpbGwtb3BhY2l0eTogMTsgc3Ryb2tlOiBub25lOyBzdHJva2Utd2lkdGg6IDM0Ljk7IHN0cm9rZS1taXRlcmxpbWl0OiA0OyBzdHJva2UtZGFzaGFycmF5OiBub25lOyBzdHJva2UtZGFzaG9mZnNldDogMDsgc3Ryb2tlLW9wYWNpdHk6IDAuOTk2MDc4OyIgaWQ9InBhdGgtMTAxIiB0cmFuc2Zvcm09Im1hdHJpeCgtMSwgMCwgMCwgMC45OTk5OTcsIDMxLjM1NjkyOCwgOTg0LjczNjMwNikiLz4KICA8L2c+CiAgPHBhdGggZD0iTSAyMTMuOTAzIDE4Ni43NzIgQyAxOTUuODU2IDE4Ni43NzIgMTgxLjYxIDE5Mi43ODIgMTcxLjE2MyAyMDQuODAyIEMgMTYwLjcyMyAyMTYuODE1IDE1NS41MDMgMjMzLjI2OSAxNTUuNTAzIDI1NC4xNjIgQyAxNTUuNTAzIDI3NS42NDkgMTYwLjUzNiAyOTIuMjQ5IDE3MC42MDMgMzAzLjk2MiBDIDE4MC42NzYgMzE1LjY4MiAxOTUuMDMzIDMyMS41NDIgMjEzLjY3MyAzMjEuNTQyIEMgMjI1LjEyNiAzMjEuNTQyIDIzOC4xOTMgMzE5LjQ4MiAyNTIuODczIDMxNS4zNjIgTCAyNTIuODczIDMzMi4wOTIgQyAyNDEuNDkzIDMzNi4zNjUgMjI3LjQ1MyAzMzguNTAyIDIxMC43NTMgMzM4LjUwMiBDIDE4Ni41NzMgMzM4LjUwMiAxNjcuOTEzIDMzMS4xNjIgMTU0Ljc3MyAzMTYuNDgyIEMgMTQxLjYzMyAzMDEuODA5IDEzNS4wNjMgMjgwLjk1OSAxMzUuMDYzIDI1My45MzIgQyAxMzUuMDYzIDIzNy4wMTIgMTM4LjIyNiAyMjIuMTg5IDE0NC41NTMgMjA5LjQ2MiBDIDE1MC44OCAxOTYuNzM1IDE2MC4wMTMgMTg2LjkyNSAxNzEuOTUzIDE4MC4wMzIgQyAxODMuODkzIDE3My4xNDUgMTk3Ljk1IDE2OS43MDIgMjE0LjEyMyAxNjkuNzAyIEMgMjMxLjM0MyAxNjkuNzAyIDI0Ni4zOTMgMTcyLjg0OSAyNTkuMjczIDE3OS4xNDIgTCAyNTEuMTgzIDE5NS41MzIgQyAyMzguNzU2IDE4OS42OTIgMjI2LjMzIDE4Ni43NzIgMjEzLjkwMyAxODYuNzcyIFogTSAzNjUuMTU2IDMwMi42NzIgQyAzNjUuMTU2IDMxNC4xMjUgMzYwLjg4NiAzMjIuOTU5IDM1Mi4zNDYgMzI5LjE3MiBDIDM0My44MTIgMzM1LjM5MiAzMzEuODMyIDMzOC41MDIgMzE2LjQwNiAzMzguNTAyIEMgMzAwLjA4NiAzMzguNTAyIDI4Ny4zNTkgMzM1LjkxOSAyNzguMjI2IDMzMC43NTIgTCAyNzguMjI2IDMxMy40NTIgQyAyODQuMTM5IDMxNi40NDUgMjkwLjQ4NiAzMTguODA1IDI5Ny4yNjYgMzIwLjUzMiBDIDMwNC4wMzkgMzIyLjI1MiAzMTAuNTY5IDMyMy4xMTIgMzE2Ljg1NiAzMjMuMTEyIEMgMzI2LjU4OSAzMjMuMTEyIDMzNC4wNzYgMzIxLjU1OSAzMzkuMzE2IDMxOC40NTIgQyAzNDQuNTYyIDMxNS4zNDUgMzQ3LjE4NiAzMTAuNjA5IDM0Ny4xODYgMzA0LjI0MiBDIDM0Ny4xODYgMjk5LjQ1NSAzNDUuMTA2IDI5NS4zNTkgMzQwLjk0NiAyOTEuOTUyIEMgMzM2Ljc5MiAyODguNTQ1IDMyOC42ODkgMjg0LjUxOSAzMTYuNjM2IDI3OS44NzIgQyAzMDUuMTgyIDI3NS42MDUgMjk3LjAzOSAyNzEuODgyIDI5Mi4yMDYgMjY4LjcwMiBDIDI4Ny4zNzkgMjY1LjUyMiAyODMuNzg2IDI2MS45MDkgMjgxLjQyNiAyNTcuODYyIEMgMjc5LjA2NiAyNTMuODIyIDI3Ny44ODYgMjQ4Ljk5MiAyNzcuODg2IDI0My4zNzIgQyAyNzcuODg2IDIzMy4zNDUgMjgxLjk2OSAyMjUuNDI5IDI5MC4xMzYgMjE5LjYyMiBDIDI5OC4yOTYgMjEzLjgyMiAzMDkuNDg2IDIxMC45MjIgMzIzLjcwNiAyMTAuOTIyIEMgMzM2Ljk1OSAyMTAuOTIyIDM0OS45MTIgMjEzLjYxNSAzNjIuNTY2IDIxOS4wMDIgTCAzNTUuOTQ2IDIzNC4xNzIgQyAzNDMuNTkyIDIyOS4wNzkgMzMyLjM5OSAyMjYuNTMyIDMyMi4zNjYgMjI2LjUzMiBDIDMxMy41MzIgMjI2LjUzMiAzMDYuODY5IDIyNy45MTUgMzAyLjM3NiAyMzAuNjgyIEMgMjk3Ljg4MiAyMzMuNDU1IDI5NS42MzYgMjM3LjI3NSAyOTUuNjM2IDI0Mi4xNDIgQyAyOTUuNjM2IDI0NS40MzUgMjk2LjQ3OSAyNDguMjQyIDI5OC4xNjYgMjUwLjU2MiBDIDI5OS44NDYgMjUyLjg4MiAzMDIuNTU5IDI1NS4wOTIgMzA2LjMwNiAyNTcuMTkyIEMgMzEwLjA0NiAyNTkuMjg1IDMxNy4yMzIgMjYyLjMxNSAzMjcuODY2IDI2Ni4yODIgQyAzNDIuNDY2IDI3MS42MDIgMzUyLjMyOSAyNzYuOTU1IDM1Ny40NTYgMjgyLjM0MiBDIDM2Mi41ODkgMjg3LjczNSAzNjUuMTU2IDI5NC41MTIgMzY1LjE1NiAzMDIuNjcyIFoiIHN0eWxlPSJmaWxsOiByZ2IoNjQsIDQwLCAwKTsiIGJ4Om9yaWdpbj0iMC41IDAuNSIvPgo8L3N2Zz4K); }

    .row-header {
      border-bottom: solid 1px #ccc !important;
      margin: 0px;
      min-height: 28px !important; }

    .col-header {
      text-align: center;
      display: block !important; }

    .col-15 {
      -webkit-box-flex: 0;
      -webkit-flex: 0 0 15%;
      -moz-box-flex: 0;
      -moz-flex: 0 0 15%;
      -ms-flex: 0 0 15%;
      flex: 0 0 15%;
      max-width: 15%; }

    .col-border-left {
      border-left: solid 1px #ccc !important; }

    .col-border-right {
      border-right: solid 1px #ccc !important; }

    .text-no-transform {
      text-transform: inherit; }

>>