package ro.home.project.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final List<MediaType> LOGGED_CONTENT_TYPES = List.of(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.TEXT_PLAIN);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);

        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending {} {} request with headers: {}", request.getMethod(), request.getURI(), request.getHeaders());
            if (canLogBody(request.getHeaders())) {
                LOGGER.trace("Request body: {}", body == null ? "null" : new String(body, StandardCharsets.UTF_8));
            }
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Response received with status {}, headers: {}", response.getStatusCode(), response.getHeaders());
            if (canLogBody(response.getHeaders())) {
                LOGGER.trace("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            }
        }
    }

    private boolean canLogBody(final HttpHeaders headers) {
        return LOGGER.isDebugEnabled() && headers.getContentType() != null &&
                LOGGED_CONTENT_TYPES.contains(headers.getContentType());
    }

}