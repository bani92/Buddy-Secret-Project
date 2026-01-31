package io.bani.buddysecretcore.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Slf4j
@Component
public class MdcLoggingFilter extends OncePerRequestFilter {
    // Filter 대신 OncePerRequestFilter 상속 (한 요청당 한번 호출)
    // Filter 는 여러번 호출되는 상황 발생

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. txId 생성 (8자리)
        String txId = UUID.randomUUID().toString().substring(0, 8);

        // 2. MDC에 저장 (이제 형변환 없이도 request 정보를 마음껏 쓸 수 있습니다!)
        MDC.put("txId", txId);
        log.info("▶ [REQUEST] {} {}", request.getMethod(), request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 3. 마지막에 비우기
            MDC.clear();
        }
    }
}