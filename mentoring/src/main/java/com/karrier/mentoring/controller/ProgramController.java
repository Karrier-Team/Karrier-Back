package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.ConflictException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedException;
import com.karrier.mentoring.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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


    private final CurriculumService curriculumService;

    private final RecommendedTargetService recommendedTargetService;

    private final TagService tagService;

    private final ParticipationStudentService participationStudentService;

    String mainImageBaseUrl = "https://karrier.s3.ap-northeast-2.amazonaws.com/main-image/";

    //기존에 없었던 프로그램을 만들때 mentor 정보 보여주기 위해
    @GetMapping(value = "/new")
    public  ResponseEntity<? extends BasicResponse> programForm(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        if(mentor == null){
            throw new NotFoundException(ErrorCode.MENTOR_NOT_FOUND);
        }

        //program 만들때 사용하는 mentorDetail 정보만 뽑기
        ProgramManageMentorInfoDto programManageMentorInfoDto = ProgramManageMentorInfoDto.createProgramManageMentorInfoDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<ProgramManageMentorInfoDto>(programManageMentorInfoDto));
    }

    //기존에 없었던 프로그램 완전한 저장
    @PostMapping(value = "/new")
    public ResponseEntity<? extends BasicResponse> programForm(@Valid ProgramFormDto programFormDto, RecommendedTargetData recommendedTargetData, CurriculumData curriculumData, TagData tagData, @RequestParam("nextState") String nextState, BindingResult bindingResult) throws IOException{

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Program savedProgram;

        // 만드려는 프로그램을 완전 저장할때
        if(nextState.equals("complete")){
            if (bindingResult.hasErrors()) {
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //main 사진이 없을 때
            if(programFormDto.getMainImageFile().isEmpty()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //제목을 입력하지 않은 경우
            if(programFormDto.getTitle().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            if(programFormDto.getTitle().length() > 15){
                throw new BadRequestException(ErrorCode.TITLE_SIZE_ERROR);
            }

            //소개를 입력하지 않은 경우
            if(programFormDto.getIntroduce().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            if(programFormDto.getIntroduce().length() < 150){
                throw new BadRequestException(ErrorCode.INTRODUCE_SIZE_ERROR);
            }

            //온라인 오프라인 여부를 입력하지 않은 경우
            if(programFormDto.getOnlineOffline().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행기간 시작일을 입력하지 않은 경우
            if(programFormDto.getOpenDate().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행기간 종료일을 입력하지 않은 경우
            if(programFormDto.getCloseDate().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행시간을 입력하지 않은 경우
            if(programFormDto.getRunningTime().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //가격을 입력하지 않은 경우
            if(programFormDto.getPrice().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            try {
                int price = Integer.parseInt(programFormDto.getPrice());

                //가격 범위를 벗어난 경우
                if(price > 100000 || price < 0){
                    throw new BadRequestException(ErrorCode.PRICE_RANGE_ERROR);
                }
                //가격에 숫자를 입력하지 않은 경우
            } catch (NumberFormatException e){
                throw new BadRequestException(ErrorCode.PRICE_DATA_TYPE_NOT_INT);
            }

            //최대 수강 인원을 입력하지 않은 경우
            if(programFormDto.getMaxPeople().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            try {
                int maxPeople = Integer.parseInt(programFormDto.getMaxPeople());

                //최대 수강인원 범위를 벗어난 경우
                if(maxPeople > 6 || maxPeople < 0){
                    throw new BadRequestException(ErrorCode.MAX_PEOPLE_RANGE_ERROR);
                }
                //최대 수강 인원에 숫자를 입력하지 않은 경우
            } catch (NumberFormatException e){
                throw new BadRequestException(ErrorCode.MAX_PEOPLE_DATA_TYPE_NOT_INT);
            }

            //추천 대상을 입력하지 않은 경우
            if(recommendedTargetData.getRecommendedTargetDtoList()==null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            //추천 대상 리스트가 하나라도 빈 경우
            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                if(recommendedTargetDto == null){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(recommendedTargetDto.getTarget().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }
            //추천 대상이 너무 많은 경우
            if(recommendedTargetData.getRecommendedTargetDtoList().size() > 10){
                throw new BadRequestException(ErrorCode.TARGET_SIZE_ERROR);
            }

            //강의 구성을 작성하지 않은 경우
            if(curriculumData.getCurriculumDtoList()==null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            //강의 구성 리스트가 하나라도 빈 경우
            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                if(curriculumDto == null){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(curriculumDto.getCurriculumTitle().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(curriculumDto.getCurriculumContent().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }

            //멘토 소개가 30자보다 적은 경우
            if(programFormDto.getMentorIntroduce().length() < 30){
                throw new BadRequestException(ErrorCode.MENTOR_INTRODUCE_SIZE_ERROR);
            }

            //태그가 없는 경우
            if(tagData.getTagDtoList() == null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            for(TagDto tagDto : tagData.getTagDtoList()){
                if(tagDto.getTagName().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }
            //태그 가 10개보다 많은 경우
            if(tagData.getTagDtoList().size() > 10){
                throw new BadRequestException(ErrorCode.TAG_SIZE_ERROR);
            }

            //S3 스토리지에 파일 저장 후 파일 이름 반환
            UploadFile mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main-image");

            //프로그램 정보 저장
            Program program = Program.createProgram(programFormDto, mainImage, email);
            program.setCreateDate(LocalDateTime.now());

            //완성된 프로그램임을 표시
            program.setProgramState(true);
            program.setState("Recruiting");

            //DB에 저장
            savedProgram = programService.createProgram(program);

            long programNo = savedProgram.getProgramNo();

            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                RecommendedTarget recommendedTarget = RecommendedTarget.createRecommendedTarget(programNo, recommendedTargetDto);
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                Curriculum curriculum = Curriculum.createCurriculum(programNo, curriculumDto);
                curriculumService.createCurriculum(curriculum);
            }

            for(TagDto tagDto : tagData.getTagDtoList()){
                Tag tag = Tag.createTag(programNo, tagDto);
                tagService.createTag(tag);
            }
        }

        // 만드려는 프로그램을 임시 저장할때
        else if(nextState.equals("temp")){
            UploadFile mainImage =  null;

            //main 사진이 있을 때
            if(!(programFormDto.getMainImageFile().isEmpty())){
                //S3 스토리지에 파일 저장 후 파일 이름 반환
                mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main-image");
            }

            //프로그램 정보 저장
            Program program = Program.createProgram(programFormDto, mainImage, email);
            program.setCreateDate(LocalDateTime.now());

            //임시 저장된 프로그램임을 표시
            program.setProgramState(false);
            program.setTempDate(LocalDateTime.now());

            //DB에 저장
            savedProgram = programService.createProgram(program);

            long programNo = savedProgram.getProgramNo();

            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                RecommendedTarget recommendedTarget = RecommendedTarget.createRecommendedTarget(programNo, recommendedTargetDto);
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                Curriculum curriculum = Curriculum.createCurriculum(programNo, curriculumDto);
                curriculumService.createCurriculum(curriculum);
            }

            for(TagDto tagDto : tagData.getTagDtoList()){
                Tag tag = Tag.createTag(programNo, tagDto);
                tagService.createTag(tag);
            }

        }

        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        mentorService.updateMentor(updatedMentor);


        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }


    //기존에 있었던 프로그램 완전 저장 위해서 mentor 정보 얻기
    @GetMapping(value = "/change")
    public ResponseEntity<? extends BasicResponse> programFormOldToNew(@RequestParam("programNo") long programNo){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Mentor mentor = mentorService.getMentor(email);

        if(mentor == null){
            throw new NotFoundException(ErrorCode.MENTOR_NOT_FOUND);
        }

        //기존 program에 있던 정보 뽑기
        Program program = programService.getProgramByNo(programNo);
        if(program == null){
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }

        ProgramFormDto programFormDto = ProgramFormDto.createProgramFormDto(program, mentor);
        List<Curriculum> curriculumList = curriculumService.getCurriculumByProgramNo(programNo);
        List<RecommendedTarget> recommendedTargetList = recommendedTargetService.getRecommendedTargetListByNo(programNo);
        List<Tag> tagList = tagService.getTagByNo(programNo);


        ArrayList<Object> objects = new ArrayList<>();
        objects.add(programFormDto);
        objects.add(curriculumList);
        objects.add(recommendedTargetList);
        objects.add(tagList);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<Object>(objects));
    }

    //기존에 있었던 프로그램 완전 저장으로 수정
    @PostMapping(value = "/change")
    public ResponseEntity<? extends BasicResponse> programFormOldToNew(@Valid ProgramFormDto programFormDto, @RequestParam("programNo") long programNo, RecommendedTargetData recommendedTargetData, CurriculumData curriculumData, TagData tagData, @RequestParam("nextState") String nextState, BindingResult bindingResult) throws IOException{

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Program program = programService.getProgramByNo(programNo);

        if(!(program.getEmail().equals(email))){
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        if(program.getProgramState()){
            if(nextState.equals("temp")){
                throw new BadRequestException(ErrorCode.PROGRAM_CHANGE_ERROR);
            }
        }

        // 만드려는 프로그램을 완전 저장할때
        if(nextState.equals("complete")){
            if (bindingResult.hasErrors()) {
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //main 사진이 없을 때
            if(programFormDto.getMainImageFile().isEmpty()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //제목을 입력하지 않은 경우
            if(programFormDto.getTitle().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            if(programFormDto.getTitle().length() > 15){
                throw new BadRequestException(ErrorCode.TITLE_SIZE_ERROR);
            }

            //소개를 입력하지 않은 경우
            if(programFormDto.getIntroduce().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            if(programFormDto.getIntroduce().length() < 150){
                throw new BadRequestException(ErrorCode.INTRODUCE_SIZE_ERROR);
            }

            //온라인 오프라인 여부를 입력하지 않은 경우
            if(programFormDto.getOnlineOffline().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행기간 시작일을 입력하지 않은 경우
            if(programFormDto.getOpenDate().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행기간 종료일을 입력하지 않은 경우
            if(programFormDto.getCloseDate().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //진행시간을 입력하지 않은 경우
            if(programFormDto.getRunningTime().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            //가격을 입력하지 않은 경우
            if(programFormDto.getPrice().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            try {
                int price = Integer.parseInt(programFormDto.getPrice());

                //가격 범위를 벗어난 경우
                if(price > 100000 || price < 0){
                    throw new BadRequestException(ErrorCode.PRICE_RANGE_ERROR);
                }
                //가격에 숫자를 입력하지 않은 경우
            } catch (NumberFormatException e){
                throw new BadRequestException(ErrorCode.PRICE_DATA_TYPE_NOT_INT);
            }

            //최대 수강 인원을 입력하지 않은 경우
            if(programFormDto.getMaxPeople().isBlank()){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }

            try {
                int maxPeople = Integer.parseInt(programFormDto.getMaxPeople());

                //최대 수강인원 범위를 벗어난 경우
                if(maxPeople > 6 || maxPeople < 0){
                    throw new BadRequestException(ErrorCode.MAX_PEOPLE_RANGE_ERROR);
                }
                //최대 수강 인원에 숫자를 입력하지 않은 경우
            } catch (NumberFormatException e){
                throw new BadRequestException(ErrorCode.MAX_PEOPLE_DATA_TYPE_NOT_INT);
            }

            //추천 대상을 입력하지 않은 경우
            if(recommendedTargetData.getRecommendedTargetDtoList()==null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            //추천 대상 리스트가 하나라도 빈 경우
            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                if(recommendedTargetDto == null){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(recommendedTargetDto.getTarget().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }
            //추천 대상이 너무 많은 경우
            if(recommendedTargetData.getRecommendedTargetDtoList().size() > 10){
                throw new BadRequestException(ErrorCode.TARGET_SIZE_ERROR);
            }

            //강의 구성을 작성하지 않은 경우
            if(curriculumData.getCurriculumDtoList()==null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            //강의 구성 리스트가 하나라도 빈 경우
            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                if(curriculumDto == null){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(curriculumDto.getCurriculumTitle().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
                if(curriculumDto.getCurriculumContent().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }

            //멘토 소개가 30자보다 적은 경우
            if(programFormDto.getMentorIntroduce().length() < 30){
                throw new BadRequestException(ErrorCode.MENTOR_INTRODUCE_SIZE_ERROR);
            }

            //태그가 없는 경우
            if(tagData.getTagDtoList() == null){
                throw new BadRequestException(ErrorCode.BLANK_FORM);
            }
            for(TagDto tagDto : tagData.getTagDtoList()){
                if(tagDto.getTagName().isBlank()){
                    throw new BadRequestException(ErrorCode.BLANK_FORM);
                }
            }
            //태그 가 10개보다 많은 경우
            if(tagData.getTagDtoList().size() > 10){
                throw new BadRequestException(ErrorCode.TAG_SIZE_ERROR);
            }

            //S3 스토리지에 파일 저장 후 파일 이름 반환
            UploadFile mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main-image");

            //프로그램 정보 변경
            program.setModifiedDate(LocalDateTime.now());

            //완성된 프로그램임을 표시
            program.setProgramState(true);

            //프로그램 상태 바꿔주기
            if(program.getApplyPeople() < program.getMaxPeople()){
                if((program.getCloseDate()).compareTo(LocalDateTime.now().toString()) > 0){
                    program.setState("Recruiting");
                }
            }

            Program savedProgram = Program.updateProgram(program, mainImage, programFormDto);
            programService.updateProgram(savedProgram);

            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                RecommendedTarget recommendedTarget = RecommendedTarget.createRecommendedTarget(programNo, recommendedTargetDto);
                recommendedTargetService.createRecommendedTarget(recommendedTarget);
            }

            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                Curriculum curriculum = Curriculum.createCurriculum(programNo, curriculumDto);
                curriculumService.createCurriculum(curriculum);
            }

            for(TagDto tagDto : tagData.getTagDtoList()){
                Tag tag = Tag.createTag(programNo, tagDto);
                tagService.createTag(tag);
            }
        }

        else if(nextState.equals("temp")){
            UploadFile mainImage = null;
            //main 사진이 있을 때
            if(!(programFormDto.getMainImageFile().isEmpty())){
                //S3 스토리지에 파일 저장 후 파일 이름 반환
                mainImage = s3Uploader.upload(programFormDto.getMainImageFile(), "main-image");
            }

            //프로그램 정보 업데이트
            program.setTempDate(LocalDateTime.now());
            Program updatedProgram = Program.updateTempProgram(program, mainImage, programFormDto);

            //프로그램 정보 저장
            programService.updateProgram(updatedProgram);

            // 추천대상 정보 변경
            for(RecommendedTargetDto recommendedTargetDto : recommendedTargetData.getRecommendedTargetDtoList()){
                RecommendedTarget recommendedTarget = RecommendedTarget.createRecommendedTarget(programNo, recommendedTargetDto);
                recommendedTargetService.modifyRecommendedTarget(recommendedTarget);
            }

            for(CurriculumDto curriculumDto : curriculumData.getCurriculumDtoList()){
                Curriculum curriculum = Curriculum.createCurriculum(programNo, curriculumDto);
                curriculumService.modifyCurriculum(curriculum);
            }

            for(TagDto tagDto : tagData.getTagDtoList()){
                Tag tag = Tag.createTag(programNo, tagDto);
                tagService.modifyTag(tag);
            }
        }

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetailByProgram(mentor, programFormDto);

        //멘토 정보 저장
        mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }


    //특정 major program들 조건에 따라 순서로 얻기
    @GetMapping(value = "/major")
    public ResponseEntity<? extends BasicResponse> viewPrograms(@RequestParam("major") String major, @RequestParam("order") String order, @RequestParam("category") String category, @RequestParam("keyword") String keyword){
        List<Mentor> mentors = mentorService.getEmailsByMajor(major);

        // 해당 mentor들이 만든 program list 만들기
        List<String> emails = new ArrayList<>();

        for(Mentor mentor : mentors){
            emails.add(mentor.getEmail());
        }

        List<ProgramViewDto> programViewDtoList = programService.getPrograms(emails, order, category, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(programViewDtoList));
    }

    //프로그램 관련 정보 모두 보여주기(멘토, 프로그램, 질의응답, 수강후기, 참여하는 멘티정보)
    @GetMapping(value = "/show")
    public ResponseEntity<? extends BasicResponse> viewProgram(@RequestParam("programNo") long programNo){

        // 해당 programNo에 해당하는 프로그램이 없는 경우
        if(programService.getProgramByNo(programNo) == null){
            throw new ConflictException(ErrorCode.PROGRAM_NOT_FOUND);
        }

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Program program = programService.getProgramByNo(programNo);

        // 있는 경우 program관련 정보들 Dto로 넘김
        ProgramInformationDto programInformationDto = programService.getProgramInformationDto(programNo, email);

        if(!program.getEmail().equals(email)){
            programInformationDto.setParticipationStudentList(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(programInformationDto));
    }


    // 프로그램에 멤버가 참여
    @PostMapping(value = "/participate")
    public ResponseEntity<? extends BasicResponse> participateProgram(@RequestParam("programNo") long programNo, @Valid ParticipationStudentFormDto participationStudentFormDto, BindingResult bindingResult) throws IOException {

        // 입력하지 않은것이 있다면
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        ParticipationStudent participationStudent = ParticipationStudent.createParticipationStudent(participationStudentFormDto, email, programNo);

        Program program = programService.getProgramByNo(programNo);

        if(participationStudentService.getParticipationStudentByEmailAndProgramNo(email, programNo) != null){
            throw new ConflictException(ErrorCode.DUPLICATE_PARTICIPATION);
        }

        if((program.getApplyPeople() >= program.getMaxPeople())||(!program.getState().equals("Recruiting"))){
            throw new BadRequestException(ErrorCode.PROGRAM_SIZE_ERROR);
        }
        else{
            program.setApplyPeople(program.getApplyPeople()+1);
            if(program.getApplyPeople() == program.getMaxPeople()){
                program.setState("Recruit_complete");
            }
            programService.updateProgram(program);
        }

        // DB에 추가
        participationStudentService.createParticipationStudent(participationStudent);

        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @GetMapping(value = "/participate-complete")
    public ResponseEntity<? extends BasicResponse> participateComplete(@RequestParam("programNo") long programNo){
        // 사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        if(participationStudentService.getParticipationStudentByEmailAndProgramNo(email, programNo) == null){
            throw new NotFoundException(ErrorCode.PARTICIPATION_NOT_FOUND);
        }

        Program program = programService.getProgramByNo(programNo);

        if(program == null){
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }

        Mentor mentor = mentorService.getMentor(program.getEmail());

        if(mentor == null){
            throw new NotFoundException(ErrorCode.MENTOR_NOT_FOUND);
        }

        ParticipationCompleteDto participationCompleteDto = ParticipationCompleteDto.createParticipationCompleteDto(program, mentor);


        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<ParticipationCompleteDto>(participationCompleteDto));
    }
}
