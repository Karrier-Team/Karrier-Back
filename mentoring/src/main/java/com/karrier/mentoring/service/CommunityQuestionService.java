package com.karrier.mentoring.service;

import com.karrier.mentoring.controller.MemberController;
import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityQuestionService {


    private final QuestionRepository questionRepository;

    private final QuestionCommentRepository questionCommentRepository;

    private final QuestionLikeRepository questionLikeRepository;

    private final AnswerLikeRepository answerLikeRepository;

    private final MemberRepository memberRepository;

    private final MentorRepository mentorRepository;

    private final ProgramRepository programRepository;
    //새로운 질문 등록
    @Transactional
    public Question saveQuestion(Question question) {

        List<Question> questionList = questionRepository.findByProgramNo(question.getProgramNo());

        long max = 0;
        if (questionList != null) { // 질문 번호 자동 생성
            for (Question question1 : questionList) {
                if (question1.getQuestionNo() > max) {
                    max = question1.getQuestionNo();
                }
            }
        }
        question.setQuestionNo(max+1); // 질문 번호 세팅

        return questionRepository.save(question);
    }

    //새로운 댓글 등록
    @Transactional
    public QuestionComment saveComment(QuestionComment questionComment) {

        List<QuestionComment> questionCommentList = questionCommentRepository.findByProgramNoAndQuestionNo(questionComment.getProgramNo(), questionComment.getQuestionNo());

        long max = 0;
        if (questionCommentList != null) { //댓글 번호 자동 생성
            for (QuestionComment questionComment1 : questionCommentList) {
                if (questionComment1.getCommentNo() > max) {
                    max = questionComment1.getCommentNo();
                }
            }
        }
        questionComment.setCommentNo(max+1); //댓글 번호 세팅

        return questionCommentRepository.save(questionComment);
    }

    //댓글 수정
    @Transactional
    public QuestionComment updateComment(QuestionComment questionComment) {
        return questionCommentRepository.save(questionComment);
    }

    //댓글 삭제
    @Transactional
    public long deleteComment(QuestionCommentFormDto questionCommentFormDto) {
        return questionCommentRepository.deleteByProgramNoAndQuestionNoAndCommentNo(questionCommentFormDto.getProgramNo(), questionCommentFormDto.getQuestionNo(), questionCommentFormDto.getCommentNo());
    }

    //질문 1개 찾기
    public Question findQuestion(long programNo, long questionNo) {
        return questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);
    }

    //한 프로그램의 모든 질문 찾아서 필요한 데이터 추가해서 반환
    public List<QuestionListDto> findQuestionList(long programNo) {

        List<Question> questionList = questionRepository.findByProgramNo(programNo);
        if (questionList.size() == 0) {
            return null;
        }

        ArrayList<QuestionListDto> questionListDto = getQuestionListDtoList(programNo, questionList);
        return questionListDto;
    }

    //한 프로그램내의 질문 키워드로 검색
    public List<QuestionListDto> QuestionSearchList(long programNo, String category, String keyword) {

        List<Question> questionList = new ArrayList<>();

        if (category.equals("질문제목")) {
            questionList = questionRepository.findByProgramNoAndTitleContaining(programNo, keyword);
        }
        else if (category.equals("질문내용")) {
            questionList = questionRepository.findByProgramNoAndContentContaining(programNo, keyword);
        }
        else if (category.equals("닉네임")) {
            List<Member> memberList = memberRepository.findByNicknameContaining(keyword); //해당 닉네임의 member 정보 찾기 (이메일을 찾기 위해)
            for (Member member : memberList) {
                questionList.addAll(questionRepository.findByProgramNoAndEmail(programNo, member.getEmail()));//이메일과 프로그램 번호로 리뷰찾아서 리스트에 추가
            }
        }
        if (questionList.size() == 0) {
            return null;
        }
        ArrayList<QuestionListDto> questionListDto = getQuestionListDtoList(programNo, questionList);

        return questionListDto;
    }

    //questionList에서 QuestionListDto로 변환
    private ArrayList<QuestionListDto> getQuestionListDtoList(long programNo, List<Question> questionList) {

        String title = programRepository.findByProgramNo(programNo).getTitle();//프로그램 이름 찾기
        ArrayList<QuestionListDto> questionListDto = new ArrayList<>();
        for (Question question : questionList) {
            String nickname = memberRepository.findByEmail(question.getEmail()).getNickname(); //닉네임 찾기
            questionListDto.add(QuestionListDto.createQuestionListDto(question, title, nickname));
        }
        return questionListDto;
    }

    //한 프로그램의 모든 질문 찾아서 필요한 데이터 추가해서 반환
    public QuestionDetailDto getQuestionDetail(long programNo, long questionNo) {

        Question question = questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);

        Program program = programRepository.findByProgramNo(programNo);//프로그램 정보 찾기
        Member writer = memberRepository.findByEmail(question.getEmail());//작성자 닉네임, 프로필을 위해 찾기
        String name = mentorRepository.findByEmail(program.getEmail()).getName();//멘토 이름 찾기
        String mentorProfileImage = memberRepository.findByEmail(program.getEmail()).getProfileImage().getStoreFileName();//멘토 프로필 사진 찾기
        List<QuestionComment> questionCommentList = questionCommentRepository.findByProgramNoAndQuestionNo(programNo, questionNo);//댓글 찾기

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<QuestionCommentListDto> questionCommentListDto = new ArrayList<>();
        for (QuestionComment questionComment : questionCommentList) {
            Member member = memberRepository.findByEmail(questionComment.getEmail());//프로필 사진, 닉네임을 위해
            String profileImageUrl = null;//프로필 사진 정보
            String commentName; // 닉네임 정보
            if (member.getProfileImage() != null) {
                profileImageUrl = MemberController.profileImageBaseUrl + member.getProfileImage().getStoreFileName(); // 프로필이 있는 회원만
            }
            if (member.getRole().equals(Role.MENTOR_APPROVE)) { // 멘토일 때는 이름을 가져와야 하므로
                commentName = mentorRepository.findByEmail(member.getEmail()).getName();
            } else {
                commentName = member.getNickname();
            }
            questionCommentListDto.add(QuestionCommentListDto.createQuestionCommentListDto(questionComment, commentName, profileImageUrl, email));// 댓글 보여주기 위한 정보형태로 변환
        }
        return QuestionDetailDto.createQuestionDetailDto(question, program, writer, name, mentorProfileImage, MemberController.profileImageBaseUrl, email, questionCommentListDto);
    }

    //question 수정, 답변 추가, 수정 됐을 때
    @Transactional
    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    //질문 삭제
    @Transactional
    public long deleteQuestion(long programNo, long questionNo) {
        return questionRepository.deleteByProgramNoAndQuestionNo(programNo, questionNo);
    }

    //질문 댓글 삭제
    @Transactional
    public Question deleteAnswer(long programNo, long questionNo) {
        Question question = findQuestion(programNo, questionNo);
        question.setAnswer(null);
        question.setAnswerDate(null);
        question.setAnswerLikeNo(0);
        return questionRepository.save(question);
    }

    //질문 좋아요 정보 찾기 (좋아요 눌렀는지 확인하기 위해)
    public QuestionLike findQuestionLike(long programNo, long questionNo, String email) {
            return questionLikeRepository.findByProgramNoAndQuestionNoAndEmail(programNo, questionNo, email);
    }

    //질문 답변 좋아요 정보 찾기 (좋아요 눌렀는지 확인하기 위해)
    public AnswerLike findAnswerLike(long programNo, long questionNo, String email) {
        return answerLikeRepository.findByProgramNoAndQuestionNoAndEmail(programNo, questionNo, email);
    }

    //질문 좋아요1 증가된 것 저장과 누가 좋아요 눌렀는지 저장
    @Transactional
    public ArrayList<Object> likeQuestion(Question question, QuestionLike questionLike) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(questionRepository.save(question));
        objects.add(questionLikeRepository.save(questionLike));

        return objects;
    }

    //답변 좋아요1 증가된 것 저장과 누가 좋아요 눌렀는지 저장
    @Transactional
    public ArrayList<Object> likeAnswer(Question question, AnswerLike answerLike) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(questionRepository.save(question));
        objects.add(answerLikeRepository.save(answerLike));

        return objects;
    }
}
