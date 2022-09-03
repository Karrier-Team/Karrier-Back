package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.NoticeFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notice")
@Getter
@Setter
@ToString
public class Notice {

    // 공지사항

    // 공지사항 번호
    @Id
    private long noticeNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime registerDate;

    @Column(nullable = false)
    private String registerWriter;

    @Column(nullable = false)
    private LocalDateTime modifyDate;

    @Column(nullable = false)
    private String modifyWriter;

    @Column(nullable = false)
    private LocalDateTime openDate;

    @Column(nullable = false)
    private LocalDateTime closeDate;

    // 조회수
    private int hits;

    public static Notice createNotice(NoticeFormDto noticeFormDto, Long noticeNo) {

        Notice notice = new Notice();

        notice.setNoticeNo(noticeFormDto.getNoticeNo());
        notice.setTitle(noticeFormDto.getTitle());
        notice.setContent(noticeFormDto.getContent());
        notice.setRegisterDate(LocalDateTime.now());
        notice.setModifyDate(LocalDateTime.now());
        notice.setOpenDate(LocalDateTime.now());

        return notice;
    }

}
