package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationCompleteDto {

    private String title;

    private String mentorName;

    private String type;

    private String openDate;

    private String closeDate;

    private String runningTime;

    private Boolean onlineOffline;

    public static ParticipationCompleteDto createParticipationCompleteDto(Program program, Mentor mentor){

        ParticipationCompleteDto participationCompleteDto = new ParticipationCompleteDto();

        participationCompleteDto.setTitle(program.getTitle());
        participationCompleteDto.setMentorName(mentor.getName());
        participationCompleteDto.setType(program.getType());
        participationCompleteDto.setOpenDate(program.getOpenDate());
        participationCompleteDto.setCloseDate(program.getCloseDate());
        participationCompleteDto.setRunningTime(program.getRunningTime());
        participationCompleteDto.setOnlineOffline(program.getOnlineOffline());

        return participationCompleteDto;
    }
}
