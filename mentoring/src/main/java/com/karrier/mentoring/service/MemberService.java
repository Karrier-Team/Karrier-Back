package com.karrier.mentoring.service;

import com.karrier.mentoring.controller.MemberController;
import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.*;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final MentorRepository mentorRepository;

    private final QuestionRepository questionRepository;

    private final QuestionLikeRepository questionLikeRepository;

    private final QuestionCommentRepository questionCommentRepository;

    private final AnswerLikeRepository answerLikeRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final ProgramRepository programRepository;

    private final CurriculumRepository curriculumRepository;

    private final FollowRepository followRepository;

    private final ParticipationStudentRepository participationStudentRepository;

    private final RecommendedTargetRepository recommendedTargetRepository;

    private final WishListRepository wishListRepository;

    private final S3Uploader s3Uploader;

    //멤버 저장
    @Transactional
    public Member saveMember(Member member) {

        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //멤버 수정
    @Transactional
    public Member modifyMember(Member member) {
        return memberRepository.save(member);
    }

    //회원 탈퇴
    @Transactional
    public void deleteMember(Member member) {
        if (member.getRole().equals(Role.MENTOR_APPROVE) || member.getRole().equals(Role.MENTOR_WAIT)) { //멘토일 경우 멘토 관련 테이블 삭제
            Mentor byEmail = mentorRepository.findByEmail(member.getEmail());
            s3Uploader.deleteStudentInfo(byEmail.getStudentInfo().getStoreFileName()); //재학증명서 삭제
            List<Program> programList = programRepository.findByEmail(member.getEmail());
            for (Program program : programList) {
                s3Uploader.deleteMainImage(program.getMainImage().getStoreFileName());//프로그램 대표 사진 삭제
                reviewRepository.deleteByProgramNo(program.getProgramNo());//수강후기 삭제
                reviewLikeRepository.deleteByProgramNo(program.getProgramNo());//수강후기 좋아요 삭제
                questionRepository.deleteByProgramNo(program.getProgramNo());//질의응답 삭제
                questionLikeRepository.deleteByProgramNo(program.getProgramNo());//질문 좋아요 삭제
                answerLikeRepository.deleteByProgramNo(program.getProgramNo());//답변 좋아요 삭제
                questionCommentRepository.deleteByProgramNo(program.getProgramNo());//댓글 삭제
                programRepository.delete(program);//프로그램 삭제
                curriculumRepository.deleteByProgramNo(program.getProgramNo());//커리큘럼 삭제
                participationStudentRepository.deleteByProgramNo(program.getProgramNo());//참여학생 정보 삭제
                wishListRepository.deleteByProgramNo(program.getProgramNo());//찜목록 삭제
                recommendedTargetRepository.deleteByProgramNo(program.getProgramNo());//추천대상 삭제
                //차단회원 삭제
                //태그??
            }
            followRepository.deleteByMentorEmail(member.getEmail());//팔로우 정보 삭제
            mentorRepository.deleteByEmail(member.getEmail()); //멘토 삭제
        }
        if (member.getProfileImage() != null) {
            s3Uploader.deleteProfileImage(member.getProfileImage().getStoreFileName());//프로필 사진 삭제
        }
        List<Review> reviewList = reviewRepository.findByEmail(member.getEmail());//해당 멤버가 작성한 수강후기 검색
        for (Review review : reviewList) {
            reviewLikeRepository.deleteByEmail(member.getEmail()); //해당 멤버가 좋아요 누른 기록 삭제
            reviewLikeRepository.deleteByProgramNoAndReviewNo(review.getProgramNo(), review.getReviewNo()); // 해당 멤버가 작성한 수강후기의 좋아요 정보 삭제
            reviewRepository.delete(review);//수강후기 삭제
        }
        List<Question> questionList = questionRepository.findByEmail(member.getEmail());//해당멤버가 작성한 질문 검색
        for (Question question : questionList) {
            questionLikeRepository.deleteByEmail(member.getEmail());//해당 멤버가 좋아요 누른 기록 삭제
            questionLikeRepository.deleteByProgramNoAndQuestionNo(question.getProgramNo(), question.getQuestionNo());//해당멤버가 작성한 질의응답 답변 좋아요 정보 삭제
            answerLikeRepository.deleteByEmail(member.getEmail());//해당 멤버가 좋아요 누른 기록 삭제
            questionCommentRepository.deleteByEmail(member.getEmail());//해당 멤버가 작성한 댓글 삭제
            questionCommentRepository.deleteByProgramNoAndQuestionNo(question.getProgramNo(), question.getQuestionNo()); //해당 멤버가 작성한 질문의 관련 댓글 전부 삭제
            questionRepository.delete(question);//질문 삭제
        }
        questionCommentRepository.deleteByEmail(member.getEmail());//댓글 삭제
        followRepository.deleteByMemberEmail(member.getEmail());//팔로우 정보 삭제
        participationStudentRepository.deleteByEmail(member.getEmail());//참여학생 정보 삭제
        wishListRepository.deleteByEmail(member.getEmail());//찜목록 삭제
        //차단회원 삭제
        memberRepository.delete(member); //회원정보 테이블에서 삭제
    }

    //멤버 정보 가져오기
    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }

    //닉네임 중복 체크
    public boolean checkDuplicateNickName(String nickname) {

        Member findMember = memberRepository.findByNickname(nickname);
        //이미 존재하는 닉네임일 경우
        if (findMember != null) {
            return true;
        }
        //중복이 아닐 경우
        return false;
    }

    //이메일 중복 체크
    private void validateDuplicateMember(Member member) {

        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
