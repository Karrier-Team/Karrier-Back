package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.MentorFormDto;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.UploadFile;
import com.karrier.mentoring.service.MentorService;
import com.karrier.mentoring.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/mentors")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MentorController {

    private final MentorService mentorService;

    private final S3Uploader s3Uploader;

    @GetMapping(value = "/new")
    public String mentorForm(Model model) {
        model.addAttribute("mentorFormDto", new MentorFormDto());
        return "OK";
    }

    @PostMapping(value = "/new")
    public String mentorForm(@Valid MentorFormDto mentorFormDto, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {

            log.info("모두 입력해주세요");

            return "mentor/mentorForm";
        }

        UploadFile studentInfo = s3Uploader.upload(mentorFormDto.getStudentInfoFile(), "student_info");
        UploadFile profile = s3Uploader.upload(mentorFormDto.getProfileImageFile(), "profile_image");

        Mentor mentor = Mentor.createMentor(mentorFormDto, studentInfo, profile);
        mentorService.saveMentor(mentor);

        return "redirect:/";
    }

}
