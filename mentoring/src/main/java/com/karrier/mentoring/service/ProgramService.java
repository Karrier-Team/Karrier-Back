package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;

    private final MentorRepository mentorRepository;

    private final MemberRepository memberRepository;

    private final CurriculumRepository curriculumRepository;

    private final ParticipationStudentRepository participationStudentRepository;

    private final RecommendedTargetRepository recommendedTargetRepository;

    private final TagRepository tagRepository;

    private final WishListRepository wishListRepository;

    private final FollowRepository followRepository;

    private final ReviewRepository reviewRepository;

    private final QuestionRepository questionRepository;

    private final QuestionCommentRepository questionCommentRepository;

    @Transactional
    public Program createProgram(Program program){
        return programRepository.save(program);
    }

    @Transactional
    public Program updateProgram(Program program){
        return programRepository.save(program);
    }

    public Program getProgramByNo(long programNo){
        return programRepository.findByProgramNo(programNo);

    }


    public List<ProgramViewDto> getPrograms(List<String> emails, String orderType, String searchType, String searchWord)
    {
        List<Program> tempProgramList = new ArrayList<>();
        List<Program> programList = new ArrayList<>();

        if(searchType.equals("프로그램제목")){

            for(String email : emails){
                tempProgramList.addAll(programRepository.findByEmail(email));
            }

            if(searchWord == null){
                for(Program program : tempProgramList){
                    if(program.getProgramState().equals(true)){
                        programList.add(program);
                    }
                }
            }
            else{
                for(Program program : tempProgramList){
                    if((program.getTitle().contains(searchWord)) && (program.getProgramState().equals(true))){
                        programList.add(program);
                    }
                }
            }

            if(orderType.equals("최신순")){
                programList.sort(Comparator.comparing(Program::getCreateDate).reversed());
            }
            else if(orderType.equals("찜갯수")){
                programList.sort(Comparator.comparing(Program::getLikeCount).reversed());
            }
            else if(orderType.equals("이름순")){
                programList.sort(Comparator.comparing(Program::getTitle));
            }

        }
        else if(searchType.equals("멘토이름")){

            for(String email : emails){
                tempProgramList.addAll(programRepository.findByEmail(email));
            }

            if(searchWord == null){
                for(Program program : tempProgramList){
                    if(program.getProgramState().equals(true)){
                        programList.add(program);
                    }
                }
            }
            else{
                for(Program program : tempProgramList){
                    if((mentorRepository.findByEmail(program.getEmail()).getName().contains(searchWord))&& (program.getProgramState().equals(true))){
                        programList.add(program);
                    }
                }
            }

            if(orderType.equals("최신순")){
                programList.sort(Comparator.comparing(Program::getCreateDate).reversed());
            }
            else if(orderType.equals("찜갯수")){
                programList.sort(Comparator.comparing(Program::getLikeCount).reversed());
            }
            else if(orderType.equals("이름순")){
                programList.sort(Comparator.comparing(Program::getTitle));
            }
        }

        ArrayList<ProgramViewDto> programViewDtoArrayList = getProgramViewDtoList(programList);

        return programViewDtoArrayList;
    }

    public List<ProgramViewDto> getWishPrograms(List<Program> programs, String orderType, String searchType, String searchWord)
    {
        List<Program> programList = new ArrayList<>();

        if(searchType.equals("프로그램제목")){

            if(searchWord == null){
                for(Program program : programs){
                    if(program.getProgramState().equals(true)){
                        programList.add(program);
                    }
                }
            }
            else{
                for(Program program : programs){
                    if((program.getTitle().contains(searchWord)) && (program.getProgramState().equals(true))){
                        programList.add(program);
                    }
                }
            }

            if(orderType.equals("최신순")){
                programList.sort(Comparator.comparing(Program::getCreateDate).reversed());
            }
            else if(orderType.equals("제목순")){
                programList.sort(Comparator.comparing(Program::getTitle));
            }

        }
        else if(searchType.equals("멘토이름")){

            if(searchWord == null){
                for(Program program : programs){
                    if(program.getProgramState().equals(true)){
                        programList.add(program);
                    }
                }
            }
            else{
                for(Program program : programs){
                    if((mentorRepository.findByEmail(program.getEmail()).getName().contains(searchWord))&& (program.getProgramState().equals(true))){
                        programList.add(program);
                    }
                }
            }

            if(orderType.equals("최신순")){
                programList.sort(Comparator.comparing(Program::getCreateDate).reversed());
            }
            else if(orderType.equals("제목순")){
                programList.sort(Comparator.comparing(Program::getTitle));
            }
        }

        ArrayList<ProgramViewDto> programViewDtoArrayList = getProgramViewDtoList(programList);

        return programViewDtoArrayList;
    }

    public ArrayList<ProgramViewDto> getProgramViewDtoList(List<Program> programs){
        ArrayList<ProgramViewDto> programViewDtoArrayList = new ArrayList<>();

        for(Program program : programs){
            String name = mentorRepository.findByEmail(program.getEmail()).getName();
            String major = mentorRepository.findByEmail(program.getEmail()).getMajor();
            String profileImage = memberRepository.findByEmail(program.getEmail()).getProfileImage().getStoreFileName();

            programViewDtoArrayList.add(ProgramViewDto.createProgramViewDto(program, name, profileImage, major));
        }

        return programViewDtoArrayList;

    }

    public List<Program> getProgramsByEmail(String email){
        return programRepository.findByEmail(email);
    }

    public ProgramInformationDto getProgramInformationDto(long programNo, String email){
        Program program = programRepository.findByProgramNo(programNo);
        Mentor mentor = mentorRepository.findByEmail(program.getEmail());
        String profileImage = memberRepository.findByEmail(program.getEmail()).getProfileImage().getFileUrl();
        List<Curriculum> curriculumList = curriculumRepository.findByProgramNo(programNo);
        List<RecommendedTarget> recommendedTargetList = recommendedTargetRepository.findByProgramNo(programNo);
        List<Tag> tagList = tagRepository.findByProgramNo(programNo);
        List<ParticipationStudent> participationStudentList = participationStudentRepository.findByProgramNo(programNo);

        List<ReviewDetailDto> reviewDetailDtoList = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findByProgramNo(programNo);

        for (Review review : reviewList) {
            Member writer = memberRepository.findByEmail(review.getEmail());//작성자 닉네임, 프로필사진 찾기 위해
            String name = mentorRepository.findByEmail(program.getEmail()).getName();//멘토 이름 찾기
            String mentorProfileImageUrl = memberRepository.findByEmail(program.getEmail()).getProfileImage().getFileUrl();

            reviewDetailDtoList.add(ReviewDetailDto.createReviewDetailDto(review, program, writer, name, mentorProfileImageUrl, email));
        }

        List<QuestionDetailDto> questionDetailDtoList = new ArrayList<>();
        List<Question> questionList = questionRepository.findByProgramNo(programNo);

        for(Question question : questionList){
            Member writer = memberRepository.findByEmail(question.getEmail());//작성자 닉네임, 프로필을 위해 찾기
            String name = mentorRepository.findByEmail(program.getEmail()).getName();//멘토 이름 찾기
            String mentorProfileImageUrl = memberRepository.findByEmail(program.getEmail()).getProfileImage().getFileUrl();//멘토 프로필 사진 찾기
            List<QuestionComment> questionCommentList = questionCommentRepository.findByProgramNoAndQuestionNo(programNo, question.getQuestionNo());//댓글 찾기

            List<QuestionCommentListDto> questionCommentListDto = new ArrayList<>();
            for (QuestionComment questionComment : questionCommentList) {
                Member member = memberRepository.findByEmail(questionComment.getEmail());//프로필 사진, 닉네임을 위해
                String profileImageUrl = null;//프로필 사진 정보
                String commentName; // 닉네임 정보
                if (member.getProfileImage() != null) {
                    profileImageUrl = member.getProfileImage().getFileUrl(); // 프로필이 있는 회원만
                }
                if (member.getRole().equals(Role.MENTOR_APPROVE)) { // 멘토일 때는 이름을 가져와야 하므로
                    commentName = mentorRepository.findByEmail(member.getEmail()).getName();
                } else {
                    commentName = member.getNickname();
                }
                questionCommentListDto.add(QuestionCommentListDto.createQuestionCommentListDto(questionComment, commentName, profileImageUrl, email));// 댓글 보여주기 위한 정보형태로 변환
            }

            questionDetailDtoList.add(QuestionDetailDto.createQuestionDetailDto(question, program, writer, name, mentorProfileImageUrl, email, questionCommentListDto));
        }

        Boolean isMyWishList = true;
        Boolean isMyFollowList = true;
        Boolean isMyParticipate = true;

        if(wishListRepository.findByProgramNoAndEmail(programNo, email) == null){
            isMyWishList = false;
        }
        if(followRepository.findByMemberEmailAndMentorEmail(email, mentor.getEmail())==null){
            isMyFollowList = false;
        }
        if(participationStudentRepository.findByEmailAndProgramNo(email, programNo)==null){
            isMyParticipate = false;
        }


        ProgramInformationDto programInformationDto = ProgramInformationDto.createProgramInformationDto(program, mentor, profileImage, curriculumList, recommendedTargetList, tagList, participationStudentList, isMyWishList, isMyFollowList, isMyParticipate, reviewDetailDtoList, questionDetailDtoList);

        return programInformationDto;
    }
}
