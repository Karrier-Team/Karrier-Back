package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.ProgramViewDto;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.WishList;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.ProgramService;
import com.karrier.mentoring.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin({"http://localhost:3000", "https://web-reactapp-48f224l75lf6ut.gksl1.cloudtype.app/", "https://www.karrier.co.kr/"})
@RequestMapping("/wishlist")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WishListController {

    private final ProgramService programService;

    private final WishListService wishListService;

    // wishList 추가
    @PostMapping(value = "/add")
    public ResponseEntity<WishList> addWishList(@RequestParam("programNo") long programNo){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        // 이미 위시리스트에 있는 경우
        if(wishListService.getWishList(programNo, email) != null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Program program = programService.getProgramByNo(programNo);

        WishList wishList = WishList.createWishList(programNo, email);

        WishList newWishList = wishListService.createWishList(wishList);

        program.setLikeCount(program.getLikeCount()+1);
        programService.updateProgram(program);

        return ResponseEntity.status(HttpStatus.CREATED).body(newWishList);
    }

    // wishList 제거
    @PostMapping(value = "/delete")
    public ResponseEntity<Object> deleteWishList(@RequestParam("programNo") long programNo){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        // 위시리스트에 없는데 삭제하려는 경우
        if(wishListService.getWishList(programNo, email) == null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("already deleted");
        }

        Program program = programService.getProgramByNo(programNo);

        wishListService.deleteWishList(programNo, email);

        program.setLikeCount(program.getLikeCount()-1);
        programService.updateProgram(program);

        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }


    // 정렬 조건과 검색 조건에 따라서 보여주기
    @GetMapping(value = "/show")
    public ResponseEntity<Object> showWishList(@RequestParam("orderType") String orderType, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();


        List<WishList> wishLists = wishListService.getMyWishLists(email);


        List<Program> programs = new ArrayList<>();

        for(WishList wishList : wishLists){
            programs.add(programService.getProgramByNo(wishList.getProgramNo()));
        }

        List<ProgramViewDto> programViewDtoList = programService.getWishPrograms(programs, orderType, searchType, searchWord);

        return ResponseEntity.status(HttpStatus.OK).body(programViewDtoList);
    }

}
