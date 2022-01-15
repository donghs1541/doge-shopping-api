package com.example.dogeapi.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response){

        //세션 id를 생성하고, 값을 세션에 젖아
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        
        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SessionConst.LOGIN_MEMBER, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SessionConst.LOGIN_MEMBER);
        if (sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SessionConst.LOGIN_MEMBER);
        if (sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return null;
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}

