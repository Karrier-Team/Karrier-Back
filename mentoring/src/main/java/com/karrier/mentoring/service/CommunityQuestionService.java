package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.QuestionDetailDto;
import com.karrier.mentoring.dto.QuestionListDto;
import com.karrier.mentoring.dto.ReviewDetailDto;
import com.karrier.mentoring.dto.ReviewListDto;
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

    private final QuestionLikeRepository questionLikeRepository;

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
        question.setQuestionNo(max+1); // 수강후기 번호 세팅

        return questionRepository.save(question);
    }

    //질문 1개 찾기
    public Question findQuestion(long programNo, long questionNo) {
        return questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);
    }

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public List<QuestionListDto> findQuestionList(long programNo) {

        List<Question> questionList = questionRepository.findByProgramNo(programNo);
        if (questionList.size() == 0) {
            return null;
        }

        ArrayList<QuestionListDto> questionListDto = getQuestionListDtoList(programNo, questionList);
        return questionListDto;
    }

    //한 프로그램내의 수강후기 키워드로 검색
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

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public QuestionDetailDto getQuestionDetail(long programNo, long questionNo) {

        Question question = questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);

        Program program = programRepository.findByProgramNo(programNo);//프로그램 정보 찾기
        String writerNickname = memberRepository.findByEmail(question.getEmail()).getNickname(); //작성자 닉네임 찾기
        String name = mentorRepository.findByEmail(program.getEmail()).getName();//멘토 이름 찾기

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        return QuestionDetailDto.createQuestionDetailDto(question, program, writerNickname, name, email);
    }

    @Transactional
    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    //리뷰 삭제
    @Transactional
    public long deleteQuestion(long programNo, long questionNo) {
        return questionRepository.deleteByProgramNoAndQuestionNo(programNo, questionNo);
    }

    //리뷰 댓글 삭제
    @Transactional
    public Question deleteAnswer(long programNo, long questionNo) {
        Question question = findQuestion(programNo, questionNo);
        question.setAnswer(null);
        question.setAnswerDate(null);
        question.setAnswerLikeNo(0);
        return questionRepository.save(question);
    }

    //리뷰 좋아요 정보 찾기 (좋아요 눌렀는지 확인하기 위해)
    public QuestionLike findQuestionLike(long programNo, long questionNo, String email) {
            return questionLikeRepository.findByProgramNoAndQuestionNoAndEmail(programNo, questionNo, email);
    }

    //좋아요1 증가된 것 저장과 누가 좋아요 눌렀는지 저장
    @Transactional
    public ArrayList<Object> likeQuestion(Question question, QuestionLike questionLike) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(questionRepository.save(question));
        objects.add(questionLikeRepository.save(questionLike));

        return objects;
    }
}
