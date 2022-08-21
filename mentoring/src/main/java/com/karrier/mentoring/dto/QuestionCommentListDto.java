package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.QuestionComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionCommentListDto {

    private long commentNo;

    private String content;

    private String writerName;

    private String writerProfileImage;

    private boolean writer; // 글쓴이 인지 여부 (수정, 삭제를 위해)

    private LocalDateTime commentDate;

    // 댓글 표시에 필요한 정보들 반환
    public static QuestionCommentListDto createQuestionCommentListDto(QuestionComment questionComment, String writerName, String writerProfileImage, String myEmail) {

        QuestionCommentListDto questionCommentListDto = new QuestionCommentListDto();

        questionCommentListDto.setCommentNo(questionComment.getCommentNo());
        questionCommentListDto.setContent(questionComment.getContent());
        questionCommentListDto.setWriterName(writerName);
        questionCommentListDto.setWriterProfileImage(writerProfileImage);
        questionCommentListDto.setCommentDate(questionComment.getCommentDate());
        if (questionComment.getEmail().equals(myEmail)) {
            questionCommentListDto.setWriter(true);
        }
        return questionCommentListDto;
    }
}
