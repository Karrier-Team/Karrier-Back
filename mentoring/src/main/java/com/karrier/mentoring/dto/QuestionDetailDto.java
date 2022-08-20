package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionDetailDto {

    private long programNo;

    private String programName;

    private long questionNo;

    private String writerNickname;

    private String mentorName;

    private String title;

    private String content;

    private String answer;

    private LocalDateTime modifyDate;

    private LocalDateTime answerDate;

    private long questionLikeNo;

    private long answerLikeNo;

    private boolean writer;

    private boolean mentor;

    public static QuestionDetailDto createQuestionDetailDto(Question question, Program program, String writerNickname, String mentorName, String myEmail) {

        QuestionDetailDto questionDetailDto = new QuestionDetailDto();

        questionDetailDto.setProgramName(program.getTitle());
        questionDetailDto.setProgramNo(question.getProgramNo());
        questionDetailDto.setQuestionNo(question.getQuestionNo());
        questionDetailDto.setWriterNickname(writerNickname);
        questionDetailDto.setMentorName(mentorName);
        questionDetailDto.setTitle(question.getTitle());
        questionDetailDto.setContent(question.getContent());
        questionDetailDto.setAnswer(question.getAnswer());
        questionDetailDto.setQuestionLikeNo(question.getQuestionLikeNo());
        questionDetailDto.setAnswerLikeNo(question.getAnswerLikeNo());
        questionDetailDto.setModifyDate(question.getModifyDate());
        questionDetailDto.setAnswerDate(question.getAnswerDate());
        if (question.getEmail().equals(myEmail)) {//질문 삭제, 수정 가능 여부
            questionDetailDto.setWriter(true);
        }
        if (program.getEmail().equals(myEmail)) {//답변 삭제, 수정 가능 여부 && 답변 작성 가능 여부 판단 가능
            questionDetailDto.setMentor(true);
        }

        return questionDetailDto;
    }
}
