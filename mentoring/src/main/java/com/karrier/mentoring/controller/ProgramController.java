package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.ProgramFormDto;
import com.karrier.mentoring.dto.ProgramManageMentorInfoDto;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.UploadFile;
import com.karrier.mentoring.service.MentorService;
import com.karrier.mentoring.service.ProgramService;
import com.karrier.mentoring.service.S3Uploader;
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
import java.util.ArrayList;

@RequestMapping("/programs")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProgramController {

    private final MentorService mentorService;

    private final ProgramService programService;

    private final S3Uploader s3Uploader;

    //기존에 없었던 프로그램을 만들때 mentor정보 보여주기 위해
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
    public ResponseEntity<Object> programForm(@Valid ProgramFormDto programFormDto, BindingResult bindingResult) throws IOException{

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

        //완성된 프로그램임을 표시
        program.setProgramState(true);

        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        //DB에 저장
        Program savedProgram = programService.createProgram(program);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
    }

    //기존에 없었던 프로그램을 임시 저장할때 mentor 정보 보여주기 위해
    @GetMapping(value = "/temp")
    public ResponseEntity<ProgramManageMentorInfoDto> programTempForm(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //mentor 정보를 찾기
        Mentor mentor = mentorService.getMentor(email);

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(programManageMentorInfoDto);
    }

    //기존에 없었던 프로그램 임시 저장
    @PostMapping(value = "/temp")
    public ResponseEntity<Object> programTempForm(@Valid ProgramFormDto programFormDto, BindingResult bindingResult) throws IOException{

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

        //mentor detail 정보도 입력 받은 대로 변경해주기
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        //DB에 저장
        Program savedProgram = programService.createProgram(program);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProgram);
    }

    //기존에 있었던 프로그램 완전 저장 위해서 mentor 정보 얻기
    @GetMapping(value = "/new/{programNo}")
    public ResponseEntity<Object> programFormOldToNew(@PathVariable("programNo") Long programNo){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);

        //기존 program에 있던 정보 뽑기
        Program program = programService.getProgram(programNo);
        ProgramFormDto programFormDto = ProgramFormDto.createProgramFormDto(program);

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(programManageMentorInfoDto);
        objects.add(programFormDto);

        return ResponseEntity.status(HttpStatus.OK).body(objects);
    }

    //기존에 있었던 프로그램 완전 저장으로 수정
    @PostMapping(value = "/new/{programNo}")
    public ResponseEntity<Object> programFormOldToNew(@PathVariable("programNo") Long programNo, @Valid ProgramFormDto programFormDto, BindingResult bindingResult) throws IOException{

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
        Program program = programService.getProgram(programNo);
        Program updatedProgram = Program.updateProgram(program, mainImage, programFormDto);

        //프로그램 정보 저장
        Program updatedProgramInfo = programService.updateProgram(updatedProgram);

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        //멘토 정보 저장
        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProgramInfo);
    }


    //기존에 있었던 프로그램 임시 저장 위해서 mentor 정보 얻기
    @GetMapping(value = "/temp/{programNo}")
    public ResponseEntity<Object> programTempFormOldToNew(@PathVariable("programNo") Long programNo){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);

        //기존 program에 있던 정보 뽑기
        Program program = programService.getProgram(programNo);
        ProgramFormDto programFormDto = ProgramFormDto.createProgramFormDto(program);

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(programManageMentorInfoDto);
        objects.add(programFormDto);

        return ResponseEntity.status(HttpStatus.OK).body(objects);
    }

    //기존에 있었던 프로그램 임시 저장으로 수정
    @PostMapping(value = "/temp/{programNo}")
    public ResponseEntity<Object> programTempFormOldToNew(@PathVariable("programNo") Long programNo, @Valid ProgramFormDto programFormDto, BindingResult bindingResult) throws IOException{

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
        Program program = programService.getProgram(programNo);
        Program updatedProgram = Program.updateTempProgram(program, mainImage, programFormDto);

        //프로그램 정보 저장
        Program updatedProgramInfo = programService.updateProgram(updatedProgram);

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        //멘토 정보 저장
        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProgramInfo);
    }
}
