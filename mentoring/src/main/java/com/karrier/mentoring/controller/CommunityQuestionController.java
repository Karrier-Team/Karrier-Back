package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.ConflictException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedException;
import com.karrier.mentoring.repository.ProgramRepository;
import com.karrier.mentoring.repository.QuestionCommentRepository;
import com.karrier.mentoring.service.CommunityQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin({"http://localhost:3000", "https://web-reactapp-48f224l75lf6ut.gksl1.cloudtype.app/", "https://www.karrier.co.kr/"})
@RequestMapping("/community")
@RestController
@RequiredArgsConstructor
public class CommunityQuestionController {

    private final CommunityQuestionService communityQuestionService;

    private final QuestionCommentRepository questionCommentRepository;
    private final ProgramRepository programRepository;

    //해당 프로그램 전체 리뷰 리스트 띄우기 + 검색할 경우 (질문제목, 질문내용, 닉네임)
    @GetMapping("/question")
    public ResponseEntity<? extends BasicResponse> questionList(@RequestParam("programNo") long programNo,
                                                                @RequestParam(name = "category", required = false) String category,
                                                                @RequestParam(name = "keyword", required = false) String keyword) {
        if (category == null || keyword == null) { // 검색조건 중 하나라도 입력하지 않을 경우 전체 리스트 띄우기
            List<QuestionListDto> questionList = communityQuestionService.findQuestionList(programNo);
            if (questionList == null) {//해당 프로그램에 해당하는 데이터가 없을 때
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(new ArrayList()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(questionList));
        }

        List<QuestionListDto> questionList = communityQuestionService.QuestionSearchList(programNo, category, keyword);
        if (questionList == null) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(new ArrayList()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(questionList));
    }

    //질문 등록 요청
    @PostMapping("/question/new")
    public ResponseEntity<? extends BasicResponse> createQuestion(@Valid QuestionFormDto questionFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Program byProgramNo = programRepository.findByProgramNo(questionFormDto.getProgramNo());
        if (byProgramNo == null) { // 프로그램이 존재하지 않을 경우
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }
        if (byProgramNo.getProgramState() == false) { // 임시저장일 경우
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Question question = Question.createQuestion(questionFormDto, email);

        Question savedQuestion = communityQuestionService.saveQuestion(question);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(savedQuestion));
    }

    //해당 질문 세부내용 출력
    @GetMapping("/question/detail")
    public ResponseEntity<? extends BasicResponse> questionList(@RequestParam("programNo") long programNo,@RequestParam("questionNo") long questionNo) {

        Question question = communityQuestionService.findQuestion(programNo, questionNo);
        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }

        QuestionDetailDto questionDetail = communityQuestionService.getQuestionDetail(programNo, questionNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(questionDetail));
    }

    //질문 답변 등록 요청
    @PutMapping({"/question/answer/new", "/question/answer"})
    public ResponseEntity<? extends BasicResponse> createAnswer(@Valid QuestionAnswerFormDto questionAnswerFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Question question = communityQuestionService.findQuestion(questionAnswerFormDto.getProgramNo(), questionAnswerFormDto.getQuestionNo());//이전에 저장했던 후기 정보 가져오기

        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Program program = programRepository.findByProgramNo(questionAnswerFormDto.getProgramNo());
        if (!program.getEmail().equals(email)) { // 해당 프로그램 멘토인지 확인
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        Question questionWithAnswer = Question.createAnswer(questionAnswerFormDto, question);//후기에 댓글 정보 추가하기
        Question updatedQuestion = communityQuestionService.updateQuestion(questionWithAnswer);//DB에 저장

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedQuestion));
    }

    //질문 좋아요 요청시
    @PostMapping("/question/heart")
    public ResponseEntity<? extends BasicResponse> likeAnswer(@RequestParam("programNo") long programNo,@RequestParam("questionNo") long questionNo) {

        Question question = communityQuestionService.findQuestion(programNo, questionNo);
        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        QuestionLike questionLike = communityQuestionService.findQuestionLike(programNo, questionNo, email);
        if (questionLike != null) { //이미 좋아요 한 회원일 경우
            question.setQuestionLikeNo(question.getQuestionLikeNo()-1); // 좋아요 1 감소
            Question updatedQuestion = communityQuestionService.deleteQuestionLike(question, email);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedQuestion));
        }

        question.setQuestionLikeNo(question.getQuestionLikeNo()+1); // 좋아요 1 증가
        QuestionLike newQuestionLike = QuestionLike.createQuestionLike(programNo, questionNo, email);
        ArrayList<Object> objects = communityQuestionService.likeQuestion(question, newQuestionLike);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(objects));
    }

    //답변 좋아요 요청시
    @PostMapping("/question/answer/heart")
    public ResponseEntity<? extends BasicResponse> likeQuestion(@RequestParam("programNo") long programNo,@RequestParam("questionNo") long questionNo) {

        Question question = communityQuestionService.findQuestion(programNo, questionNo);
        if (question == null) { //해당 프로그램 번호와 질문에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        if (question.getAnswerDate() == null) { //해당 프로그램 번호와 질문 번호에 해당하는 답변이 없을 때
            throw new NotFoundException(ErrorCode.ANSWER_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        AnswerLike answerLike = communityQuestionService.findAnswerLike(programNo, questionNo, email);
        if (answerLike != null) { //이미 좋아요 한 회원일 경우
            question.setAnswerLikeNo(question.getAnswerLikeNo()-1); // 좋아요 1 감소
            Question updatedQuestion = communityQuestionService.deleteAnswerLike(question, email);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedQuestion));
        }

        question.setAnswerLikeNo(question.getAnswerLikeNo()+1); // 좋아요 1 증가
        AnswerLike newAnswerLike = AnswerLike.createAnswerLike(programNo, questionNo, email);
        ArrayList<Object> objects = communityQuestionService.likeAnswer(question, newAnswerLike);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(objects));
    }
    
    //질문 수정 요청시
    @PutMapping("/question")
    public ResponseEntity<? extends BasicResponse> modifyQuestion(@Valid QuestionFormDto questionFormDto, BindingResult bindingResult, @RequestParam("questionNo") long questionNo) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Question question = communityQuestionService.findQuestion(questionFormDto.getProgramNo(), questionNo);
        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        if (!question.getEmail().equals(email)) { //작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        Question.updateQuestion(questionFormDto, question);
        Question savedQuestion = communityQuestionService.updateQuestion(question);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(savedQuestion));
    }

    //질문 삭제 요청시
    @DeleteMapping("/question")
    public ResponseEntity<? extends BasicResponse> deleteQuestion(@RequestParam("programNo") long programNo, @RequestParam("questionNo") long questionNo) {

        Question question = communityQuestionService.findQuestion(programNo, questionNo);
        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        if (!question.getEmail().equals(email)) { //작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        communityQuestionService.deleteQuestion(programNo, questionNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }

    //답변 삭제 요청시
    @DeleteMapping("/question/answer")
    public ResponseEntity<? extends BasicResponse> deleteAnswer(@RequestParam("programNo") long programNo, @RequestParam("questionNo") long questionNo) {

        Question question = communityQuestionService.findQuestion(programNo, questionNo);
        if (question == null) { //해당 프로그램 번호와 질문에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        if (question.getAnswerDate() == null) { //해당 프로그램 번호와 질문 번호에 해당하는 답변이 없을 때
            throw new NotFoundException(ErrorCode.ANSWER_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        String answerEmail = programRepository.findByProgramNo(programNo).getEmail();//질문 답변 단 사람 이메일 찾기
        if (!email.equals(answerEmail)) { //작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        Question updatedQuestion = communityQuestionService.deleteAnswer(programNo, questionNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedQuestion));
    }

    //댓글 달기 요청시
    @PostMapping("/question/comment/new")
    public ResponseEntity<? extends BasicResponse> createComment(@Valid QuestionCommentFormDto questionCommentFormDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //빈칸있을 경우
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Question question = communityQuestionService.findQuestion(questionCommentFormDto.getProgramNo(), questionCommentFormDto.getQuestionNo());
        if (question == null) { //해당 프로그램 번호와 질문 번호에 해당하는 질문이 없을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        QuestionComment questionComment = QuestionComment.createComment(questionCommentFormDto, email);

        QuestionComment savedQuestionComment = communityQuestionService.saveComment(questionComment);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(savedQuestionComment));
    }

    //댓글 수정 요청시
    @PutMapping("/question/comment")
    public ResponseEntity<? extends BasicResponse> modifyComment(@Valid QuestionCommentFormDto questionCommentFormDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || questionCommentFormDto.getCommentNo() == 0) { //빈칸있을 경우
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        QuestionComment questionComment = questionCommentRepository.findByProgramNoAndQuestionNoAndCommentNo(questionCommentFormDto.getProgramNo(), questionCommentFormDto.getQuestionNo(), questionCommentFormDto.getCommentNo());
        if (questionComment == null) { //해당 프로그램 번호와 질문 번호, 댓글 번호에 해당하는 질문이 없을 때
            throw new NotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String myEmail = ((UserDetails) principal).getUsername();
        if (!questionComment.getEmail().equals(myEmail)) { // 작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        QuestionComment updatedQuestionComment = QuestionComment.updateComment(questionCommentFormDto, questionComment);

        QuestionComment savedQuestionComment = communityQuestionService.updateComment(updatedQuestionComment);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(savedQuestionComment));
    }

    //댓글 삭제 요청시
    @DeleteMapping("/question/comment")
    public ResponseEntity<? extends BasicResponse> deleteComment(@ModelAttribute QuestionCommentFormDto questionCommentFormDto) {

        QuestionComment questionComment = questionCommentRepository.findByProgramNoAndQuestionNoAndCommentNo(questionCommentFormDto.getProgramNo(), questionCommentFormDto.getQuestionNo(), questionCommentFormDto.getCommentNo());
        if (questionComment == null) { //해당 프로그램 번호와 질문 번호, 댓글 번호에 해당하는 질문이 없을 때
            throw new NotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String myEmail = ((UserDetails) principal).getUsername();
        if (!questionComment.getEmail().equals(myEmail)) { // 작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        communityQuestionService.deleteComment(questionCommentFormDto);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }
}
