package com.e203.weeklyremind.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyRemindResponseDto {

    private Integer projectMemberId;
    private Integer projectId;
    private String username;
    private String userImage;
    private Integer userId;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private Integer weeklyRemindId;
    private String content;
    private LocalDate weeklyremindstartDate;
    private LocalDate weeklyremindendDate;
    @Builder
    private WeeklyRemindResponseDto(Integer projectMemberId, Integer projectId, String username
    , String userImage, Integer userId, Integer weeklyRemindId, String projectName
    , LocalDate projectStartDate, LocalDate projectEndDate, String content
    , LocalDate weeklyremindstartDate, LocalDate weeklyremindendDate) {

        this.projectMemberId = projectMemberId;
        this.projectId = projectId;
        this.username = username;
        this.userImage = userImage;
        this.userId = userId;
        this.weeklyRemindId = weeklyRemindId;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.weeklyremindstartDate = weeklyremindstartDate;
        this.weeklyremindendDate = weeklyremindendDate;
        this.content = content;
    }
}
