package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionListDto {

    private long programNo;

    private String programName;

    private long questionNo;

    private String nickname;

    private String title;

    private String content;

    private boolean solve;

    private LocalDateTime modifyDate;

    private long questionLikeNo;
    
    //Question을 QuestionListDto로 변환
    public static QuestionListDto createQuestionListDto(Question question, String programName, String nickname) {

        QuestionListDto questionListDto = new QuestionListDto();

        questionListDto.setProgramName(programName);
        questionListDto.setProgramNo(question.getProgramNo());
        questionListDto.setQuestionNo(question.getQuestionNo());
        questionListDto.setNickname(nickname);
        questionListDto.setTitle(question.getTitle());
        questionListDto.setContent(question.getContent());
        questionListDto.setQuestionLikeNo(question.getQuestionLikeNo());
        questionListDto.setModifyDate(question.getModifyDate());
        if (question.getAnswerDate() != null) { //수강후기 답변 여부 반환
            questionListDto.setSolve(true);
        }
        return questionListDto;
    }
}
