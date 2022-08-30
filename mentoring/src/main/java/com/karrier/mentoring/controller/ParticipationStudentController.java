package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.ParticipationStudentFormDto;
import com.karrier.mentoring.dto.ProgramViewDto;
import com.karrier.mentoring.entity.ParticipationStudent;
import com.karrier.mentoring.repository.ParticipationStudentRepository;
import com.karrier.mentoring.service.ParticipationStudentService;
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
import java.util.List;

@RequestMapping("/participation")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ParticipationStudentController {

    private final ParticipationStudentService participationStudentService;

    private final ParticipationStudentRepository participationStudentRepository;

    // 프로그램에 멤버가 참여
    @PostMapping(value = "/new")
    public ResponseEntity<Object> participateProgram(@RequestParam("programNo") long programNo, @Valid ParticipationStudentFormDto participationStudentFormDto, BindingResult bindingResult) throws IOException {
        // 입력하지 않은것이 있다면
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        ParticipationStudent participationStudent = ParticipationStudent.createParticipationStudent(participationStudentFormDto, email, programNo);

        // DB에 추가
        ParticipationStudent newParticipationStudent = participationStudentService.createParticipationStudent(participationStudent);

        return ResponseEntity.status(HttpStatus.CREATED).body(newParticipationStudent);
    }

    @GetMapping(value = "/my")
    public ResponseEntity<Object> myParticipation(@RequestParam("state") String state){

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ParticipationStudent> participationStudentList = participationStudentRepository.findByEmail(email);

        List<ProgramViewDto> programViewDtoList = participationStudentService.getParticipationProgramViewDto(participationStudentList);

        List<ProgramViewDto> onlineProgramViewDtoList = new ArrayList<>();
        List<ProgramViewDto> offlineProgramViewDtoList = new ArrayList<>();

        if(state.equals("no")){
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
}
