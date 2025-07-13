package org.zerock.mallapi.util;

public class CustomJWTException extends RuntimeException // 사용자 정의 예외(Custom Exception) 클래스
{
    public CustomJWTException(String msg)
    {
        super(msg);
    }
    
}
