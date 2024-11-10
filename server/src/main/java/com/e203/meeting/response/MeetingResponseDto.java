package com.e203.meeting.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingResponseDto {

    private Integer meetingId;
    private Integer projectId;
    private String meetingTitle;
    private String meetingVoiceUrl;
    private SttResponseDto meetingVoiceScript;
    private LocalDateTime meetingCreateTime;
    private Integer meetingVoiceTime;

    @Builder
    private MeetingResponseDto(Integer meetingId, Integer projectId, String meetingTitle
            , String meetingVoiceUrl, SttResponseDto meetingVoiceScript, LocalDateTime meetingCreateTime
            , Integer meetingVoiceTime) {

        this.meetingId = meetingId;
        this.projectId = projectId;
        this.meetingTitle = meetingTitle;
        this.meetingVoiceUrl = meetingVoiceUrl;
        this.meetingVoiceScript = meetingVoiceScript;
        this.meetingCreateTime = meetingCreateTime;
        this.meetingVoiceTime = meetingVoiceTime;
    }
}
