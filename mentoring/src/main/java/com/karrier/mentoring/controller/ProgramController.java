package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.ProgramFormDto;
import com.karrier.mentoring.dto.ProgramInformationDto;
import com.karrier.mentoring.dto.ProgramManageMentorInfoDto;
import com.karrier.mentoring.dto.ProgramViewDto;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.CurriculumRepository;
import com.karrier.mentoring.repository.ProgramRepository;
import com.karrier.mentoring.repository.RecommendedTargetRepository;
import com.karrier.mentoring.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/programs")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProgramController {

    private final MentorService mentorService;

    private final ProgramService programService;

    private final S3Uploader s3Uploader;

    private final MemberService memberService;

    private final CurriculumService curriculumService;

    private final RecommendedTargetService recommendedTargetService;

    private final CurriculumRepository curriculumRepository;

    private final RecommendedTargetRepository recommendedTargetRepository;

    private final ProgramRepository programRepository;

    String mainImageBaseUrl = "https://karrier.s3.ap-northeast-2.amazonaws.com/main-image/";

    //기존에 없었던 프로그램을 만들때 mentor 정보 보여주기 위해
    @GetMapping(value = "/new")
    public ResponseEntity<ProgramManageMentorInfoDto> programForm(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(programManageMentorInfoDto);
    }

    //기존에 없었던 프로그램 완전한 저장
    @PostMapping(value = "/new")
    public ResponseEntity<Object> programForm(@Valid ProgramFormDto programFormDto, @RequestParam("programNo") long programNo, @RequestParam("nextState") String nextState, BindingResult bindingResult) throws IOException{
        if(nextState.equals("complete")){
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
            }

            //main 사진이 없을 때
            if(programFormDto.getMainImageFile().isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("main image empty error");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //S3 스토리지에 파일 저장 후 파일 이름 반환
            UploadFile mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");


            //프로그램 정보 저장
            Program program = Program.createProgram(programNo, programFormDto, mainImage, email);
            program.setCreateDate(LocalDateTime.now());

            //완성된 프로그램임을 표시
            program.setProgramState(true);

            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            //DB에 저장
            Program savedProgram = programService.createProgram(program);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
        }
        else if(nextState.equals("temp")){
            UploadFile mainImage = null;
            //main 사진이 있을 때
            if(!(programFormDto.getMainImageFile().isEmpty())){
                //S3 스토리지에 파일 저장 후 파일 이름 반환
                mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //프로그램 정보 저장
            Program program = Program.createProgram(5L, programFormDto, mainImage, email);
            program.setProgramNo(5);

            //임시 저장된 프로그램임을 표시
            program.setProgramState(false);
            program.setTempDate(LocalDateTime.now());

            //mentor detail 정보도 입력 받은 대로 변경해주기
            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            //DB에 저장
            Program savedProgram = programService.createProgram(program);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /*
    //기존에 없었던 프로그램 완전한 저장
    @PostMapping(value = "/new")
    public ResponseEntity<Object> programForm(@Valid ProgramFormDto programFormDto, @RequestParam List<Curriculum> curriculumList, @RequestParam List<RecommendedTarget> recommendedTargets, @RequestParam("nextState") String nextState, BindingResult bindingResult) throws IOException{
        if(nextState.equals("complete")){
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
            }

            //main 사진이 없을 때
            if(programFormDto.getMainImageFile().isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("main image empty error");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //S3 스토리지에 파일 저장 후 파일 이름 반환
            UploadFile mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");

            //프로그램 정보 저장
            Program program = Program.createProgram(programFormDto, mainImage, email);
            program.setCreateDate(LocalDateTime.now());

            //완성된 프로그램임을 표시
            program.setProgramState(true);

            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            //DB에 저장
            Program savedProgram = programService.createProgram(program);
            for(Curriculum curriculum : curriculumList){
                curriculumService.createCurriculum(curriculum);
            }
            for(RecommendedTarget recommendedTarget : recommendedTargets){
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
        }
        else if(nextState.equals("temp")){
            UploadFile mainImage = null;
            //main 사진이 있을 때
            if(!(programFormDto.getMainImageFile().isEmpty())){
                //S3 스토리지에 파일 저장 후 파일 이름 반환
                mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //프로그램 정보 저장
            Program program = Program.createProgram(programFormDto, mainImage, email);

            //임시 저장된 프로그램임을 표시
            program.setProgramState(false);
            program.setTempDate(LocalDateTime.now());

            //mentor detail 정보도 입력 받은 대로 변경해주기
            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            //DB에 저장
            Program savedProgram = programService.createProgram(program);
            for(Curriculum curriculum : curriculumList){
                curriculumService.createCurriculum(curriculum);
            }
            for(RecommendedTarget recommendedTarget : recommendedTargets){
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

     */
    //기존에 있었던 프로그램 완전 저장 위해서 mentor 정보 얻기
    @GetMapping(value = "/change")
    public ResponseEntity<Object> programFormOldToNew(@RequestParam("programNo") long programNo){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);


        //기존 program에 있던 정보 뽑기
        Program program = programService.getProgramByNo(programNo);
        ProgramFormDto programFormDto = ProgramFormDto.createProgramFormDto(program);
        List<Curriculum> curriculumList = curriculumRepository.findByProgramNo(programNo);
        List<RecommendedTarget> recommendedTargetList = recommendedTargetRepository.findByProgramNo(programNo);

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(programManageMentorInfoDto);
        objects.add(programFormDto);
        objects.add(curriculumList);
        objects.add(recommendedTargetList);

        return ResponseEntity.status(HttpStatus.OK).body(objects);
    }

    //기존에 있었던 프로그램 완전 저장으로 수정
    @PostMapping(value = "/change")
    public ResponseEntity<Object> programFormOldToNew(@RequestParam("programNo") long programNo, @RequestParam List<Curriculum> curriculumList, @RequestParam List<RecommendedTarget> recommendedTargets, @RequestParam("nextState") String nextState, @Valid ProgramFormDto programFormDto, BindingResult bindingResult) throws IOException{
        if(nextState.equals("complete")){
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
            }

            //main 사진이 없을 때
            if(programFormDto.getMainImageFile().isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("main image empty error");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //S3 스토리지에 파일 저장 후 파일 이름 반환
            UploadFile mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");

            //프로그램 정보 업데이트
            Program program = programService.getProgramByNo(programNo);
            program.setModifiedDate(LocalDateTime.now());
            Program updatedProgram = Program.updateProgram(program, mainImage, programFormDto);

            //프로그램 정보 저장
            Program updatedProgramInfo = programService.updateProgram(updatedProgram);

            for(Curriculum curriculum : curriculumList){
                curriculumService.createCurriculum(curriculum);
            }
            for(RecommendedTarget recommendedTarget : recommendedTargets){
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            //멘토 정보 업데이트
            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            //멘토 정보 저장
            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            return ResponseEntity.status(HttpStatus.OK).body(updatedProgramInfo);
        }
        else if(nextState.equals("temp")){
            UploadFile mainImage = null;
            //main 사진이 있을 때
            if(!(programFormDto.getMainImageFile().isEmpty())){
                //S3 스토리지에 파일 저장 후 파일 이름 반환
                mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main_image");
            }

            //사용자 email 얻기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();

            //프로그램 정보 업데이트
            Program program = programService.getProgramByNo(programNo);
            program.setTempDate(LocalDateTime.now());
            Program updatedProgram = Program.updateTempProgram(program, mainImage, programFormDto);

            //프로그램 정보 저장
            Program updatedProgramInfo = programService.updateProgram(updatedProgram);

            for(Curriculum curriculum : curriculumList){
                curriculumService.createCurriculum(curriculum);
            }
            for(RecommendedTarget recommendedTarget : recommendedTargets){
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            //멘토 정보 업데이트
            Mentor mentor = mentorService.getMentor(email);
            Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

            //멘토 정보 저장
            Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

            return ResponseEntity.status(HttpStatus.OK).body(updatedProgramInfo);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    //특정 major program들 조건에 따라 순서로 얻기
    @GetMapping(value = "/major")
    public ResponseEntity<List<ProgramViewDto>> viewPrograms(@RequestParam("major") String major, @RequestParam("orderType") String orderType, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord){
        List<Mentor> mentors = mentorService.getEmailsByMajor(major);

        // 해당 mentor들이 만든 program list 만들기
        List<String> emails = new ArrayList<>();

        for(Mentor mentor : mentors){
            emails.add(mentor.getEmail());
        }

        List<ProgramViewDto> programViewDtoList = programService.getPrograms(emails, orderType, searchType, searchWord);

        return ResponseEntity.status(HttpStatus.OK).body(programViewDtoList);
    }

    //프로그램 관련 정보 모두 보여주기(멘토, 프로그램, 질의응답, 수강후기, 참여하는 멘티정보)
    @GetMapping(value = "/show")
    public ResponseEntity<Object> viewProgram(@RequestParam("programNo") Long programNo){

        // 해당 programNo에 해당하는 프로그램이 없는 경우
        if(programService.getProgramByNo(programNo) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 있는 경우 program관련 정보들 Dto로 넘김
        ProgramInformationDto programInformationDto = programService.getProgramInformationDto(programNo);

        return ResponseEntity.status(HttpStatus.OK).body(programInformationDto);
    }

    //mentor 자신의 모든 프로그램 정보 보기
    @GetMapping(value = "/my")
    public ResponseEntity<List<Program>> viewMyProgram(){
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //mentor가 만든 program 보기
        List<Program> programs = programService.getProgramsByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(programs);
    }

    //mentor 프로그램중 temp 상태인것 보기
    @GetMapping(value = "/my-temp")
    public ResponseEntity<List<Program>> viewMyTempProgram(){
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();


        //mentor가 만든 program 중 임시 저장 보기
        List<Program> programs = programService.getProgramsByEmailAndState(email, false);

        return ResponseEntity.status(HttpStatus.OK).body(programs);

    }

    //mentor 프로그램중 완성된 것 보기
    @GetMapping(value = "/my-complete")
    public ResponseEntity<List<Program>> viewMyCompleteProgram(){
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //mentor가 만든 program 중 완성된 것 보기
        List<Program> programs = programService.getProgramsByEmailAndState(email, true);

        return ResponseEntity.status(HttpStatus.OK).body(programs);
    }

    // 완성된 프로그램중 제목으로 찾기
    @GetMapping(value = "/viewCompleteProgramsSearchByTitle")
    public ResponseEntity<List<Program>> viewCompleteProgramsSearchByTitle(@RequestParam("major") String major, @RequestParam("title") String title){

        // 해당 major의 mentor들 만들기
        List<Mentor> mentors = mentorService.getEmailsByMajor(major);

        // 해당 mentor들이 만든 program list 만들기

        List<String> emails = new ArrayList<>();

        for(Mentor mentor : mentors){
            emails.add(mentor.getEmail());
        }

        // 해당 mentor들이 만든 program 중 제목으로 검색하기
        List<Program> programs = programService.getProgramsByTitleContaining(emails, title);

        return ResponseEntity.status(HttpStatus.OK).body(programs);
    }


    // 완성된 프로그램중 mentor 이름으로 찾기
    @GetMapping(value = "/viewCompleteProgramsSearchByName")
    public ResponseEntity<List<Program>> viewCompleteProgramsSearchByName(@RequestParam("major") String major, @RequestParam("name") String name){

        //  해당 major의 mentor list 만들기
        List<Mentor> mentors = mentorService.getEmailsByMajor(major);

        // 해당 mentor들이 만든 program list 만들기

        List<String> emails = new ArrayList<>();

        for(Mentor mentor : mentors){
            emails.add(mentor.getEmail());
        }

        List<Program> programs = programService.getProgramsByEmailsAndState(emails);


        // program list 중 mentor nickname으로 검색하기
        List<Program> programList = new ArrayList<>();
        for(Program program : programs){
            if(((memberService.getMember(program.getEmail())).getNickname()!=null)&&((memberService.getMember(program.getEmail())).getNickname().contains(name))){
                programList.add(program);
            }
        }



        return ResponseEntity.status(HttpStatus.OK).body(programList);
    }
}
