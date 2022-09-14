package com.karrier.mentoring.http.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_CHECK_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
    ACCOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 틀렸습니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다"),
    BLANK_FORM(HttpStatus.BAD_REQUEST, "필수 입력 값이 비었습니다."),
    MAX_PEOPLE_DATA_TYPE_NOT_INT(HttpStatus.BAD_REQUEST, "최대 수강 인원 입력값을 다시 작성해야 합니다."),
    MAX_PEOPLE_RANGE_ERROR(HttpStatus.BAD_REQUEST, "최대 수강 인원은 1~6명입니다."),

    PRICE_DATA_TYPE_NOT_INT(HttpStatus.BAD_REQUEST, "가격 설정 입력값을 다시 작성해야 합니다."),
    PRICE_RANGE_ERROR(HttpStatus.BAD_REQUEST, "가능한 가격은 0~1000000원입니다."),

    ROLE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, "미처리 된 멘토가 아닙니다."),

    TARGET_SIZE_ERROR(HttpStatus.BAD_REQUEST, "추천대상 수가 너무 많습니다."),

    TAG_SIZE_ERROR(HttpStatus.BAD_REQUEST, "태그 수가 너무 많습니다."),

    TITLE_SIZE_ERROR(HttpStatus.BAD_REQUEST, "프로그램 제목이 너무 깁니다."),
    INTRODUCE_SIZE_ERROR(HttpStatus.BAD_REQUEST, "소개가 너무 짧습니다."),
    MENTOR_INTRODUCE_SIZE_ERROR(HttpStatus.BAD_REQUEST, "멘토 소개가 너무 짧습니다."),

    PROGRAM_SIZE_ERROR(HttpStatus.BAD_REQUEST, "프로그램 인원이 꽉 찼습니다."),
    PROGRAM_CHANGE_ERROR(HttpStatus.BAD_REQUEST, "이미 저장된 프로그램은 임시로 바꿀수 없습니다."),
    VALIDATE_SOCIAL_USER(HttpStatus.BAD_REQUEST, "소셜 로그인 회원가입자는 해당 절차를 진행할 수 없습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자 입니다."),


    /* 403 FORBIDDEN : 해당 권한으로 접근이 허락 되지 않는 정보*/
    AUTH_ENTRY_DENIED(HttpStatus.FORBIDDEN, "유효한 토큰이 아닙니다."),



    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰 정보를 찾을 수 없습니다"),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문 정보를 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 정보를 찾을 수 없습니다"),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변 정보를 찾을 수 없습니다"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효한 토큰 정보를 찾을 수 없습니다."),
    VERIFIED_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "인증된 이메일 정보가 없습니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 정보를 찾을 수 없습니다."),
    WISHLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "찜 정보를 찾을 수 없습니다."),
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "프로그램 정보를 찾을 수 없습니다."),
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "참여 정보를 찾을 수 없습니다."),
    MENTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "멘토 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT : DB 데이터 관리 충돌 */
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다"),
    DUPLICATE_PARTICIPATION(HttpStatus.CONFLICT, "이미 참여한 프로그램입니다"),

    DUPLICATE_LIKE(HttpStatus.CONFLICT, "이미 좋아요 한 회원입니다"),

    /* 415 UNSUPPORTED_MEDIA_TYPE : 지원하지 않는 미디어 타입 에러 */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"지원하지 않는 미디어 타입입니다."),


    /* 500 서버 내부 에러 */
    MESSAGING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"이메일 전송과정에서 에러가 발생했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 에러입니다."),
    TYPE_CAST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"형변환 과정에서 에러가 발생했습니다.");
    ;


    private final HttpStatus httpStatus;
    private final String detail;
}
