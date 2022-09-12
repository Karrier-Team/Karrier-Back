package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QuestionDetailDto {

    private long programNo;

    private String programName;

    private long questionNo;

    private String writerNickname;

    private String writerProfileImage;

    private String mentorName;

    private String mentorProfileImage;

    private String title;

    private String content;

    private String answer;

    private LocalDateTime modifyDate;

    private LocalDateTime answerDate;

    private long questionLikeNo;

    private long answerLikeNo;

    private boolean writer;

    private boolean mentor;

    private List<QuestionCommentListDto> questionCommentListDto;

    //세부화면에 필요한 정보들 담기
    public static QuestionDetailDto createQuestionDetailDto(Question question, Program program, Member member, String mentorName, String mentorProfileImageUrl, String myEmail, List<QuestionCommentListDto> questionCommentListDto) {

        QuestionDetailDto questionDetailDto = new QuestionDetailDto();

        questionDetailDto.setProgramName(program.getTitle());
        questionDetailDto.setProgramNo(question.getProgramNo());
        questionDetailDto.setQuestionNo(question.getQuestionNo());
        questionDetailDto.setWriterNickname(member.getNickname());
        if (member.getProfileImage() != null) { // 작성자 프로필 사진이 있을 때만
            questionDetailDto.setWriterProfileImage(member.getProfileImage().getFileUrl());
        }
        questionDetailDto.setMentorName(mentorName);
        questionDetailDto.setMentorProfileImage(mentorProfileImageUrl);
        questionDetailDto.setTitle(question.getTitle());
        questionDetailDto.setContent(question.getContent());
        questionDetailDto.setAnswer(question.getAnswer());
        questionDetailDto.setQuestionLikeNo(question.getQuestionLikeNo());
        questionDetailDto.setAnswerLikeNo(question.getAnswerLikeNo());
        questionDetailDto.setModifyDate(question.getModifyDate());
        questionDetailDto.setAnswerDate(question.getAnswerDate());
        questionDetailDto.setQuestionCommentListDto(questionCommentListDto);
        if (question.getEmail().equals(myEmail)) {//질문 삭제, 수정 가능 여부
            questionDetailDto.setWriter(true);
        }
        if (program.getEmail().equals(myEmail)) {//답변 삭제, 수정 가능 여부 && 답변 작성 가능 여부 판단 가능
            questionDetailDto.setMentor(true);
        }
        return questionDetailDto;
    }
}
