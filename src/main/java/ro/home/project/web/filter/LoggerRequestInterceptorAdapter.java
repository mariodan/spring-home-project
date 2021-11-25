package ro.home.project.web.filter;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import static ro.home.project.util.MarkerFields.*;

@Component
@Slf4j
public class LoggerRequestInterceptorAdapter implements HandlerInterceptor {

    private static final ThreadLocal<LoggingContext> LOGGING_CTX_TL = new ThreadLocal<>();

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {

        final LoggingContext lc = new LoggingContext();
        lc.startTime = System.currentTimeMillis();
        final String clientIp = extractClientIp(request);
        lc.commonMarkers = Markers.append(HTTP_CLIENT_IP, clientIp)
                .and(Markers.append(HTTP_REQ_URL, request.getRequestURI()))
                .and(Markers.append(HTTP_USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT)))
                .and(Markers.append(HTTP_METHOD, request.getMethod()));

        if (LOGGER.isTraceEnabled()) {
            lc.commonMarkers = lc.commonMarkers.and(Markers.append(HTTP_HEADERS, extractHeaders(request)));
        }

        LOGGING_CTX_TL.set(lc);

        LOGGER.info(lc.commonMarkers, "Request processing started... " + request.getRequestURI());
        return true;
    }

    private String extractHeaders(final HttpServletRequest request) {

        final Enumeration<String> headerNames = request.getHeaderNames();
        final StringBuilder headerValuesBuilder = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headerValuesBuilder.append(headerName).append(" = [").append(request.getHeader(headerName)).append("]\n");
        }

        return headerValuesBuilder.toString();
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                @Nullable final Exception ex) {
        try {
            final LoggingContext loggingContext = LOGGING_CTX_TL.get();

            final long startTime = loggingContext.startTime;

            final LogstashMarker httpAuditMarker = loggingContext.commonMarkers
                    .and(Markers.append(HTTP_METHOD, request.getMethod()))
                    .and(Markers.append(HTTP_STATUS_CODE, response.getStatus()));

            if (request instanceof ContentCachingRequestWrapper) {
                httpAuditMarker.and(Markers.append(HTTP_REQUEST_BODY, getMessagePayload(request)));
            }

            final String queryString = request.getQueryString();
            if (queryString != null) {
                httpAuditMarker.and(Markers.append(HTTP_QUERY_PARAMS, queryString));
            }

            LOGGER.info(httpAuditMarker.and(Markers.append(EXECUTION_TIME, System.currentTimeMillis() - startTime)),
                    "Request processing ended.");
        } finally {
            LOGGING_CTX_TL.remove();
        }
    }

    private String extractClientIp(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null) {
            ip = request.getRemoteHost();
        } else {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private String getMessagePayload(final HttpServletRequest request) {
        final ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            final byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 2000);
                try {
                    return hideSensitiveDetails(new String(buf, 0, length, wrapper.getCharacterEncoding()));
                } catch (final UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }

    private String hideSensitiveDetails(final String requestJson) {
        return requestJson.replaceAll(
                "(\\n?\\s*\"password\"\\s*:\\s*\")[^\\n\"]*(\",?\\n?)", "$1*$2");
    }

    private static class LoggingContext {

        private LogstashMarker commonMarkers;
        private Long startTime;
    }
}