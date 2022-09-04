package com.karrier.mentoring.http.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자 입니다."),

    /* 403 FORBIDDEN : 해당 권한으로 접근이 허락 되지 않는 정보*/
    AUTH_ENTRY_DENIED(HttpStatus.FORBIDDEN, "유효한 토큰이 아닙니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버 정보를 찾을 수 없습니다."),
    /* 409 CONFLICT : DB 데이터 관리 충돌 */

    /* 500 핸들링 하지 않은 에러 */
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 에러입니다.");
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
