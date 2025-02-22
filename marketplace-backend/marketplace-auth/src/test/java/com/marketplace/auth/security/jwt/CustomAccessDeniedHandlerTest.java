package com.marketplace.auth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.dto.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AccessDeniedException accessDeniedException;

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void commence_WithAuthExceptionMessage_ShouldUseProvidedMessage() throws IOException {
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    @Test
    void commence_WithNullAuthExceptionMessage_ShouldUseDefaultMessage() throws IOException {
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    @Test
    void commence_WithOriginalUrlAttribute_ShouldUseOriginalUrl() throws IOException {
        String originalUrl = "/original/url";
        request.setAttribute("originalUrl", originalUrl);

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
    }

    @Test
    void commence_ShouldCreateExceptionResponseWithCorrectData() throws IOException {
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
    }
}