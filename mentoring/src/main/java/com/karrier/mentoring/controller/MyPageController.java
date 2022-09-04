package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.QuestionListDto;
import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedException;
import com.karrier.mentoring.repository.QuestionRepository;
import com.karrier.mentoring.dto.FollowShowDto;
import com.karrier.mentoring.dto.ProgramViewDto;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.ParticipationStudentRepository;
import com.karrier.mentoring.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RequestMapping("/my-page")
@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final QuestionRepository questionRepository;

    private final CommunityQuestionService communityQuestionService;

    private final CommunityReviewService communityReviewService;

    private final MyPageService myPageService;
    
    private final ParticipationStudentService participationStudentService;

    private final ProgramService programService;

    private final WishListService wishListService;

    private final FollowService followService;

    private final MentorService mentorService;

    private final ParticipationStudentRepository participationStudentRepository;

    //나의 전체 질문 리스트 띄우기
    @GetMapping("/manage/question")
    public ResponseEntity<? extends BasicResponse> questionList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<QuestionListDto> myPageQuestionList = communityQuestionService.findMyPageQuestionList(email);

        return ResponseEntity.ok().body(new SuccessDataResponse<>(myPageQuestionList));
    }

    //나의 전체 질문 리스트 검색
    @GetMapping("/manage/question/search")
    public ResponseEntity<? extends BasicResponse> questionListSearch(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {

        if (category.isEmpty() || keyword.isEmpty()) { //빈칸있을 경우
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<QuestionListDto> myPageQuestionList = communityQuestionService.findMyPageQuestionList(email);
        if (myPageQuestionList == null) {//나의 질문이 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(null));
        }
        List<QuestionListDto> questionListDtoList = new ArrayList<>();
        for (QuestionListDto questionListDto : myPageQuestionList) {
            if (category.equals("질문제목")) {
                if (questionListDto.getTitle().contains(keyword)) {//해당 키워드가 있을 경우
                    questionListDtoList.add(questionListDto);
                }
            }
            else if (category.equals("질문내용")) {
                if (questionListDto.getContent().contains(keyword)) {//해당 키워드가 있을 경우
                    questionListDtoList.add(questionListDto);
                }
            }
        }
        if (questionListDtoList.size() == 0) {//검색 조건의 질문이 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(null));
        }
        return ResponseEntity.ok().body(new SuccessDataResponse<>(questionListDtoList));
    }

    @PostMapping("/manage/question/solve")
    public ResponseEntity<? extends BasicResponse> questionSolve(@RequestParam("programNo") long programNo, @RequestParam("questionNo") long questionNo) {

        Question byProgramNoAndQuestionNo = questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);
        if (byProgramNoAndQuestionNo == null) { // 해결할 프로그램이 존재하지 않을 때
            throw new NotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        if (!email.equals(byProgramNoAndQuestionNo.getEmail())) { //작성자와 해결 누른사람이 일치하지 않을 때
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        Question question = myPageService.solveQuestion(byProgramNoAndQuestionNo);//해결완료로 변경
        return ResponseEntity.ok().body(new SuccessDataResponse<>(question));
    }

    //나의 전체 리뷰 리스트 띄우기
    @GetMapping("/manage/review")
    public ResponseEntity<? extends BasicResponse> reviewList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ReviewListDto> myPageReviewList = communityReviewService.findMyPageReviewList(email);
        if (myPageReviewList == null) { //나의 수강후기 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(null));
        }
        return ResponseEntity.ok().body(new SuccessDataResponse<>(myPageReviewList));
    }

    //나의 전체 리뷰 리스트 검색
    @GetMapping("/manage/review/search")
    public ResponseEntity<? extends BasicResponse> reviewListSearch(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {

        if (category.isEmpty() || keyword.isEmpty()) { //빈칸있을 경우
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ReviewListDto> myPageReviewList = communityReviewService.findMyPageReviewList(email);
        if (myPageReviewList == null) { //나의 수강후기 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(null));
        }
        List<ReviewListDto> reviewListDtoList = new ArrayList<>();
        for (ReviewListDto reviewListDto : myPageReviewList) {
            if (category.equals("후기제목")) {
                if (reviewListDto.getTitle().contains(keyword)) {//해당 키워드가 있을 경우
                    reviewListDtoList.add(reviewListDto);
                }
            }
            else if (category.equals("후기내용")) {
                if (reviewListDto.getContent().contains(keyword)) {//해당 키워드가 있을 경우
                    reviewListDtoList.add(reviewListDto);
                }
            }
        }
        return ResponseEntity.ok().body(new SuccessDataResponse<>(reviewListDtoList));
    }

    @GetMapping(value = "/participation")
    public ResponseEntity<Object> myParticipation(@RequestParam("state") String state){

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ParticipationStudent> participationStudentList = participationStudentRepository.findByEmail(email);

        if (participationStudentList == null) {//해당 유저가 참여하는 프로그램이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no participation error");
        }

        List<ProgramViewDto> programViewDtoList = participationStudentService.getParticipationProgramViewDto(participationStudentList);

        List<ProgramViewDto> onlineProgramViewDtoList = new ArrayList<>();
        List<ProgramViewDto> offlineProgramViewDtoList = new ArrayList<>();

        if(state.equals("noOption")){
            return ResponseEntity.status(HttpStatus.OK).body(programViewDtoList);
        }
        else if(state.equals("online")){
            for(ProgramViewDto programViewDto : programViewDtoList){
                if(programViewDto.getOnlineOffline()){
                    onlineProgramViewDtoList.add(programViewDto);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(onlineProgramViewDtoList);
        }
        else if(state.equals("offline")){
            for(ProgramViewDto programViewDto : programViewDtoList){
                if(!programViewDto.getOnlineOffline()){
                    offlineProgramViewDtoList.add(programViewDto);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(offlineProgramViewDtoList);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping(value = "/wishlist")
    public ResponseEntity<Object> showWishList(@RequestParam("orderType") String orderType, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<WishList> wishLists = wishListService.getMyWishLists(email);

        if (wishLists == null) {//해당 유저의 찜 목록이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no wishlist error");
        }

        List<Program> programs = new ArrayList<>();

        for(WishList wishList : wishLists){
            programs.add(programService.getProgramByNo(wishList.getProgramNo()));
        }

        List<ProgramViewDto> programViewDtoList = programService.getWishPrograms(programs, orderType, searchType, searchWord);

        if (programViewDtoList == null) {//조건에 맞는 프로그램 목록이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no wishlist error with condition");
        }

        return ResponseEntity.status(HttpStatus.OK).body(programViewDtoList);
    }

    //member 입장에서 내가 팔로우 하고 있는 mentor 정보 보기
    @GetMapping(value = "/follow-list")
    public ResponseEntity<Object> myFollowings(@RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberEmail = ((UserDetails) principal).getUsername();

        List<Follow> followings = followService.getFollowings(memberEmail);

        if (followings == null) {//해당 유저의 팔로우 목록이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no follow-list error");
        }

        List<Mentor> mentors = new ArrayList<>();

        for(Follow follow : followings){
            mentors.add(mentorService.getMentor(follow.getMentorEmail()));
        }
        List<FollowShowDto> followShowDtoList = followService.getFollowingDtoList(mentors, searchType, searchWord);

        if (followShowDtoList == null) {//해당 조건의 팔로우 목록이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no follow-list error with condition");
        }

        return ResponseEntity.status(HttpStatus.OK).body(followShowDtoList);
    }
}
