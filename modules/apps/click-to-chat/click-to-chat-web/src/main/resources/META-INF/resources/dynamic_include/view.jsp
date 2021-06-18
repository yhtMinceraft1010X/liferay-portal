<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<liferay-util:include page='<%= "/dynamic_include/" + clickToChatChatProviderId + ".jsp" %>' servletContext="<%= application %>" />

<style type="text/css">
	.click-to-chat-hidden {
		display: none !important;
	}
</style>

<script type="text/javascript">
    (function () {
        function changeVisible(selectors, show) {
            let selectorsList = selectors.split(',')
            if(show) {
                selectorsList.forEach((selector) => {
                    document.querySelectorAll(selector).forEach(el => el.classList.add('click-to-chat-hidden'));
                })
            }else{
                selectorsList.forEach((selector) => {
                    document.querySelectorAll(selector).forEach(el => el.classList.remove('click-to-chat-hidden'));
                })
            }
        }

        let providers = {
            'chatwoot': function (hide) {
                if(hide) {
                    document.querySelectorAll('.woot--bubble-holder,.woot-widget-holder').forEach(el => el.remove());
                }
            },
            'crisp': '.crisp-client',
            'hubspot': '#hubspot-messages-iframe-container',
            'jivochat': '.globalClass_07d6',
            'livechat': '#chat-widget-container',
            'liveperson': '.LPMcontainer.LPMoverlay,.lp_desktop',
            'smartsupp': '#chat-application',
            'tawkto': function (hide) {
                if(hide) {
                    window?.Tawk_API?.hideWidget();
                }else{
                    if (typeof window?.Tawk_API?.showWidget === 'function') {
                        window?.Tawk_API?.showWidget();
                    }
                }
            },
            'tidio': '#tidio-chat',
            'zendesk': '#launcher,#webWidget',
        }

        Object.entries(providers).forEach(([key, value]) => {
            if(key === "<%= clickToChatChatProviderId %>") {
                if(typeof value === 'string') {
                    changeVisible(value, true);
                }else{
                    value(false)
                }
            }
            else{
                if(typeof value === 'string') {
                    changeVisible(value, false);
                }else{
                    value(true)
                }

            }
        })
    })()
</script>