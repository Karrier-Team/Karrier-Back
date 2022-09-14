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

        if (myPageQuestionList == null) { //나의 질문이 없을 경우
            return ResponseEntity.ok().body(new SuccessDataResponse<>(new ArrayList()));
        }
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
        List<QuestionListDto> questionListDtoList = new ArrayList<>();
        if (myPageQuestionList == null) {//나의 질문이 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(questionListDtoList));
        }
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
            return ResponseEntity.ok().body(new SuccessDataResponse<>(questionListDtoList));
        }
        return ResponseEntity.ok().body(new SuccessDataResponse<>(questionListDtoList));
    }

    @PutMapping("/manage/question/solve")
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
            return ResponseEntity.ok().body(new SuccessDataResponse<>(new ArrayList()));
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
        List<ReviewListDto> reviewListDtoList = new ArrayList<>();
        if (myPageReviewList == null) { //나의 수강후기 없을 때
            return ResponseEntity.ok().body(new SuccessDataResponse<>(reviewListDtoList));
        }
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

    @GetMapping(value = "/manage/program-list")
    public ResponseEntity<? extends BasicResponse> myParticipation(@RequestParam("state") String state){

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ParticipationStudent> participationStudentList = participationStudentService.getParticipationStudentsByEmail(email);
        List<ProgramViewDto> programViewDtoList = participationStudentService.getParticipationProgramViewDto(participationStudentList);

        List<ProgramViewDto> onlineProgramViewDtoList = new ArrayList<>();
        List<ProgramViewDto> offlineProgramViewDtoList = new ArrayList<>();

        if(state.equals("all")){
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(programViewDtoList));
        }
        else if(state.equals("online")){
            for(ProgramViewDto programViewDto : programViewDtoList){
                if(programViewDto.getOnlineOffline()){
                    onlineProgramViewDtoList.add(programViewDto);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(onlineProgramViewDtoList));
        }
        else if(state.equals("offline")){
            for(ProgramViewDto programViewDto : programViewDtoList){
                if(!programViewDto.getOnlineOffline()){
                    offlineProgramViewDtoList.add(programViewDto);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(offlineProgramViewDtoList));
        }
        else{
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
    }

    //member 입장에서 나의 찜 목록 보기(최신순, 제목순 정렬) (제목, 멘토이름 검색)
    @GetMapping(value = "/manage/wish-list")
    public ResponseEntity<? extends BasicResponse> showWishList(@RequestParam("order") String order, @RequestParam("category") String category, @RequestParam("keyword") String keyword){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();


        List<WishList> wishLists = wishListService.getMyWishLists(email);


        List<Program> programs = new ArrayList<>();

        for(WishList wishList : wishLists){
            programs.add(programService.getProgramByNo(wishList.getProgramNo()));
        }

        List<ProgramViewDto> programViewDtoList = programService.getWishPrograms(programs, order, category, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(programViewDtoList));
    }

    // 멘티 입장에서 팔로우된 멘토 list 팔로우에서 지우기
    @PostMapping(value = "/manage/wish-list/delete")
    public ResponseEntity<? extends BasicResponse> deleteWishList(ProgramNoData programNoData) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        for(Long programNo : programNoData.getProgramNoList()){
            WishList wishList = wishListService.getWishList(programNo, email);

            if(wishList == null){
                throw new NotFoundException(ErrorCode.WISHLIST_NOT_FOUND);
            }
        }

        for(Long programNo : programNoData.getProgramNoList()){
            Program program = programService.getProgramByNo(programNo);

            if(program == null){
                throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
            }
        }

        for(Long programNo : programNoData.getProgramNoList()){
            wishListService.deleteWishList(programNo, email);

            Program program = programService.getProgramByNo(programNo);
            program.setLikeCount(program.getLikeCount()-1);

            programService.updateProgram(program);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }

    //member 입장에서 내가 팔로우 하고 있는 mentor 정보 보기
    @GetMapping(value = "/manage/following-list")
    public ResponseEntity<? extends BasicResponse> myFollowings(@RequestParam("category") String category, @RequestParam("keyword") String keyword){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberEmail = ((UserDetails) principal).getUsername();

        List<Follow> followings = followService.getFollowings(memberEmail);
        List<Mentor> mentors = new ArrayList<>();

        for(Follow follow : followings){
            mentors.add(mentorService.getMentor(follow.getMentorEmail()));
        }

        List<FollowShowDto> followShowDtoList = followService.getFollowingDtoList(mentors, category, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(followShowDtoList));
    }


    // 멘티 입장에서 팔로우된 멘토 list 팔로우에서 지우기
    @PostMapping(value = "/manage/following-list/delete")
    public ResponseEntity<? extends BasicResponse> deleteFollow(EmailData emails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<Mentor> mentorList = new ArrayList<>();

        for (String mentorEmail : emails.getEmailList()) {
            Mentor mentor = mentorService.getMentor(mentorEmail);
            //멘토를 찾지 못한 경우
            if (mentor == null) {
                throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
            }
            mentorList.add(mentor);
        }

        for(Mentor mentor : mentorList){
            Follow follow = followService.getFollow(email, mentor.getEmail());
            //follow 항목이 없는 경우
            if(follow == null){
                throw new NotFoundException(ErrorCode.FOLLOW_NOT_FOUND);
            }
        }

        for(Mentor mentor : mentorList){
            followService.deleteFollow(email,mentor.getEmail());

            mentor.setFollowNo(mentor.getFollowNo()-1);
            mentorService.updateMentor(mentor);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }
}
