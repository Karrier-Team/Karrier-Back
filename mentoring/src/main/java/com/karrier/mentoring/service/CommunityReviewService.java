package com.karrier.mentoring.service;

import com.karrier.mentoring.controller.MemberController;
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
public class CommunityReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final ProgramRepository programRepository;

    private final MemberRepository memberRepository;

    private final MentorRepository mentorRepository;

    //새로운 수강후기 등록
    @Transactional
    public Review saveReview(Review review) {

        List<Review> reviewList = reviewRepository.findByProgramNo(review.getProgramNo());

        long max = 0;
        long index = 1;
        float sum = review.getStar();
        float averageStar = 0;
        if (reviewList != null) { // 수강후기 번호 자동 생성
            for (Review review1 : reviewList) {
                index++;
                sum += review1.getStar(); // 평균 별점 계산을 위해
                if (review1.getReviewNo() > max) {
                    max = review1.getReviewNo();
                }
            }
        }
        review.setReviewNo(max+1); // 수강후기 번호 세팅

        averageStar = sum / (float) index;//평균 별점 계산
        Program program = programRepository.findByProgramNo(review.getProgramNo());
        program.setAverageStar(averageStar);
        programRepository.save(program);

        return reviewRepository.save(review);
    }

    //리뷰 1개 찾기
    public Review findReview(long programNo, long reviewNo) {
        return reviewRepository.findByProgramNoAndReviewNo(programNo, reviewNo);
    }

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public List<ReviewListDto> findReviewList(long programNo) {

        List<Review> reviewList = reviewRepository.findByProgramNo(programNo);
        if (reviewList.size() == 0) {
            return null;
        }

        ArrayList<ReviewListDto> reviewListDto = getReviewListDtoList(programNo, reviewList);
        return reviewListDto;
    }

    //한 프로그램내의 수강후기 키워드로 검색
    public List<ReviewListDto> ReviewSearchList(long programNo, String category, String keyword) {

        List<Review> reviewList = new ArrayList<>();

        if (category.equals("후기제목")) {
            reviewList = reviewRepository.findByProgramNoAndTitleContaining(programNo, keyword);
        }
        else if (category.equals("후기내용")) {
            reviewList = reviewRepository.findByProgramNoAndContentContaining(programNo, keyword);
        }
        else if (category.equals("닉네임")) {
            List<Member> memberList = memberRepository.findByNicknameContaining(keyword); //해당 닉네임의 member 정보 찾기 (이메일을 찾기 위해)
            for (Member member : memberList) {
                reviewList.addAll(reviewRepository.findByProgramNoAndEmail(programNo, member.getEmail()));//이메일과 프로그램 번호로 리뷰찾아서 리스트에 추가
            }
        }
        if (reviewList.size() == 0) {
            return null;
        }
        ArrayList<ReviewListDto> reviewListDto = getReviewListDtoList(programNo, reviewList);

        return reviewListDto;
    }

    //나의 모든 수강후기 찾아서 필요한 데이터 추가해서 반환
    public List<ReviewListDto> findMyPageReviewList(String email) {

        List<Review> reviewList = reviewRepository.findByEmail(email);
        if (reviewList.size() == 0) {
            return null;
        }
        List<ReviewListDto> reviewListDto = new ArrayList<>();
        for (Review review : reviewList) { // 수강후기 한개씩 reviewListDto 형태로 변환
            List<Review> reviewList1 = new ArrayList<>();
            reviewList1.add(review);
            reviewListDto.addAll(getReviewListDtoList(review.getProgramNo(), reviewList1)); //변환된 reviewListDto List에 추가
        }
        return reviewListDto;
    }

    //리뷰리스트에서 ReviewListDto로 변환
    private ArrayList<ReviewListDto> getReviewListDtoList(long programNo, List<Review> reviewList) {

        Program byProgramNo = programRepository.findByProgramNo(programNo);//프로그램 이름 찾기
        ArrayList<ReviewListDto> reviewListDto = new ArrayList<>();
        for (Review review : reviewList) {
            String nickname = memberRepository.findByEmail(review.getEmail()).getNickname(); //닉네임 찾기
            reviewListDto.add(ReviewListDto.createReviewListDto(review, byProgramNo.getTitle(), nickname, byProgramNo.getAverageStar()));
        }
        return reviewListDto;
    }

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public ReviewDetailDto getReviewDetail(long programNo, long reviewNo) {

        Review review = reviewRepository.findByProgramNoAndReviewNo(programNo, reviewNo);

        Program program = programRepository.findByProgramNo(programNo);//프로그램 정보 찾기
        Member writer = memberRepository.findByEmail(review.getEmail());//작성자 닉네임, 프로필사진 찾기 위해
        String name = mentorRepository.findByEmail(program.getEmail()).getName();//멘토 이름 찾기
        String mentorProfileImageUrl = memberRepository.findByEmail(program.getEmail()).getProfileImage().getFileUrl();

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        
        return ReviewDetailDto.createReviewDetailDto(review, program, writer, name, mentorProfileImageUrl, email);
    }

    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    //리뷰 삭제
    @Transactional
    public long deleteReview(long programNo, long reviewNo) {
        return reviewRepository.deleteByProgramNoAndReviewNo(programNo, reviewNo);
    }

    //리뷰 댓글 삭제
    @Transactional
    public Review deleteComment(long programNo, long reviewNo) {
        Review review = findReview(programNo, reviewNo);
        review.setComment(null);
        review.setCommentDate(null);
        return reviewRepository.save(review);
    }

    //리뷰 좋아요 정보 찾기 (좋아요 눌렀는지 확인하기 위해)
    public ReviewLike findReviewLike(long programNo, long reviewNo, String email) {
        return reviewLikeRepository.findByProgramNoAndReviewNoAndEmail(programNo, reviewNo, email);
    }

    //좋아요1 증가와 누가 좋아요 눌렀는지 저장
    @Transactional
    public ArrayList<Object> likeReview(Review review, ReviewLike reviewLike) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(reviewRepository.save(review));
        objects.add(reviewLikeRepository.save(reviewLike));

        return objects;
    }
}
