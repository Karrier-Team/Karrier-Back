package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.FollowShowDto;
import com.karrier.mentoring.entity.Follow;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.service.FollowService;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.MentorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/follow")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final MentorService mentorService;

    private final FollowService followService;

    private final MemberService memberService;

    //Follow 기능(이미 Follow가 되어 있을 경우 다시 호출되면 취소
    @PostMapping(value = "/click")
    public ResponseEntity<Object> followMentor(@RequestParam("mentorEmail") String mentorEmail){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(mentorEmail);

        //mentorEmail 잘못된 값이 입력된 경우
        if(mentor == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong mentor");
        }

        Follow follow = followService.getFollow(email, mentorEmail);

        //follow가 되어있지 않은 상태라면
        if(follow == null){
            follow = Follow.createFollow(email, mentorEmail);

            Follow newFollow = followService.createFollow(follow);

            //mentor follow 수 1 증가시켜 주기
            mentor.setFollowNo(mentor.getFollowNo()+1);
            mentorService.updateMentor(mentor);

            return ResponseEntity.status(HttpStatus.CREATED).body(newFollow);
        }
        //이미 follow가 되어있는 상태라면
        else{
            followService.deleteFollow(email, mentorEmail);

            //mentor follow 수 1 감소시켜 주기
            mentor.setFollowNo(mentor.getFollowNo()-1);
            mentorService.updateMentor(mentor);

            return ResponseEntity.status(HttpStatus.OK).body("deleted");
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Object> addFollow(@RequestParam("mentorEmail") String mentorEmail){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(mentorEmail);

        //mentorEmail 잘못된 값이 입력된 경우
        if(mentor == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong mentor");
        }

        Follow follow = followService.getFollow(email, mentorEmail);

        //이미 follow가 되어있는 상태라면
        if(follow != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("already following");
        }
        //follow가 되어있지 않은 상태라면
        else{
            follow = Follow.createFollow(email, mentorEmail);

            Follow newFollow = followService.createFollow(follow);

            //mentor follow 수 1 증가시켜 주기
            mentor.setFollowNo(mentor.getFollowNo()+1);
            mentorService.updateMentor(mentor);

            return ResponseEntity.status(HttpStatus.CREATED).body(newFollow);
        }
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<Object> deleteFollow(@RequestParam("mentorEmail") String mentorEmail){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(mentorEmail);

        //mentorEmail 잘못된 값이 입력된 경우
        if(mentor == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong mentor");
        }

        Follow follow = followService.getFollow(email, mentorEmail);

        //follow가 되어있지 않은 상태라면
        if(follow == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("delete fail");
        }
        //이미 follow가 되어있는 상태라면
        else{
            followService.deleteFollow(email, mentorEmail);

            //mentor follow 수 1 감소시켜 주기
            mentor.setFollowNo(mentor.getFollowNo()-1);
            mentorService.updateMentor(mentor);

            return ResponseEntity.status(HttpStatus.OK).body("delete complete");
        }
    }

    //mentor 입장에서 나를 팔로우 하고 있는 member 정보 보기 필요한 경우 dto 로 수정할 예정
    @GetMapping(value = "/my-followers")
    public ResponseEntity<Object> myFollowers(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mentorEmail = ((UserDetails) principal).getUsername();

        List<Follow> followers =  followService.getFollowers(mentorEmail);
        if(followers == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no followers error");
        }

        List<Member> memberList = new ArrayList<>();

        // followers의 email을 통해 member 뽑아주기
        for (Follow follow : followers){
            Member member = memberService.getMember(follow.getMemberEmail());
            memberList.add(member);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberList);
    }

    //mentor 입장에서 나를 팔로우 하고 있는 member 중 이름 검색 -> 나를 팔로우 하는 사람 필요한 경우 검색, 정렬 옵션으로 수정 예정
    @GetMapping(value = "/my-followersByName")
    public ResponseEntity<List<Member>> myFollowersByName(@RequestParam("memberName") String memberName){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mentorEmail = ((UserDetails) principal).getUsername();

        List<Follow> followers =  followService.getFollowers(mentorEmail);
        List<Member> memberList = new ArrayList<>();

        // followers의 email을 통해 member 뽑아주기
        for (Follow follow : followers){
            Member member = memberService.getMember(follow.getMemberEmail());
            if((member.getNickname()!=null) && (member.getNickname().contains(memberName))){
                memberList.add(member);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberList);
    }


    //member 입장에서 내가 팔로우 하고 있는 mentor 정보 보기
    @GetMapping(value = "/follow-list")
    public ResponseEntity<Object> myFollowings(@RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberEmail = ((UserDetails) principal).getUsername();

        List<Follow> followings = followService.getFollowings(memberEmail);
        if(followings == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no followers error");
        }

        List<Mentor> mentors = new ArrayList<>();

        for(Follow follow : followings){
            mentors.add(mentorService.getMentor(follow.getMentorEmail()));
        }
        List<FollowShowDto> followShowDtoList = followService.getFollowingDtoList(mentors, searchType, searchWord);

        return ResponseEntity.status(HttpStatus.OK).body(followShowDtoList);
    }
}
