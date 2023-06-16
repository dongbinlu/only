package com.only.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.DefaultBlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomerBlockRequestHandler extends DefaultBlockRequestHandler {

    private static final String DEFAULT_BLOCK_MSG_PREFIX = "Blocked by Sentinel: ";

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
        if (acceptsHtml(exchange)) {
            return htmlErrorResponse(ex);
        }
        // JSON result by default.
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(buildErrorResult(ex)));
    }

    private Mono<ServerResponse> htmlErrorResponse(Throwable ex) {

        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.TEXT_PLAIN).syncBody(JSON.toJSONString(buildErrorResult(ex)));
    }

    private CustomerBlockRequestHandler.ErrorResult buildErrorResult(Throwable ex) {
        if (ex instanceof FlowException) {
            return new CustomerBlockRequestHandler.ErrorResult(HttpStatus.TOO_MANY_REQUESTS.value(), "block");
        } else if (ex instanceof DegradeException) {
            return new CustomerBlockRequestHandler.ErrorResult(HttpStatus.TOO_MANY_REQUESTS.value(), "fallback");
        } else {
            return new CustomerBlockRequestHandler.ErrorResult(HttpStatus.BAD_GATEWAY.value(), "gateway error");
        }
    }

    /**
     * Reference from {@code DefaultErrorWebExceptionHandler} of Spring Boot.
     */
    private boolean acceptsHtml(ServerWebExchange exchange) {
        try {
            List<MediaType> acceptedMediaTypes = exchange.getRequest().getHeaders().getAccept();
            acceptedMediaTypes.remove(MediaType.ALL);
            MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
            return acceptedMediaTypes.stream().anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
        } catch (InvalidMediaTypeException ex) {
            return false;
        }
    }

    private static class ErrorResult {
        private final int code;
        private final String message;

        ErrorResult(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
