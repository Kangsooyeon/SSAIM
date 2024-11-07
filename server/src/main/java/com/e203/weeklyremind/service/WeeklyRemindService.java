package com.e203.weeklyremind.service;

import com.e203.dailyremind.entity.DailyRemind;
import com.e203.dailyremind.repository.DailyRemindRepository;
import com.e203.project.entity.Project;
import com.e203.project.entity.ProjectMember;
import com.e203.project.repository.ProjectMemberRepository;
import com.e203.project.repository.ProjectRepository;
import com.e203.user.entity.User;
import com.e203.user.repository.UserRepository;
import com.e203.weeklyremind.entity.WeeklyRemind;
import com.e203.weeklyremind.repository.WeeklyRemindRepository;
import com.e203.weeklyremind.request.WeeklyRemindRequestDto;
import com.e203.weeklyremind.response.DevelopmentStoryResponseDto;
import com.e203.weeklyremind.response.WeeklyRemindDto;
import com.e203.weeklyremind.response.WeeklyRemindResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyRemindService {

    private final WeeklyRemindRepository weeklyRemindRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ChatAiService chatAiService;
    private final ProjectRepository projectRepository;
    private final DailyRemindRepository dailyRemindRepository;

    public boolean saveWeeklyRemind(WeeklyRemindRequestDto message, int projectId) {

        ProjectMember projectMember = projectMemberRepository
                .findById(message.getProjectMemberId())
                .orElse(null);

        if (projectMember == null) {
            return false;
        }
        Project project = projectRepository.findById(projectId).orElse(null);

        List<DailyRemind> dailyRemindList = dailyRemindRepository.searchDailyReminds(message.getProjectMemberId(),
                message.getStartDate(), message.getEndDate(), projectId);

        StringBuilder dailyReminds = new StringBuilder();

        for (DailyRemind dailyRemind : dailyRemindList) {
            dailyReminds.append(dailyRemind.getDailyRemindContents());
        }

        String summary = chatAiService.generateWeeklyRemind(dailyReminds.toString(), projectId);

        WeeklyRemind weeklyRemind = WeeklyRemind.builder()
                .weeklyRemindContents(summary)
                .projectId(project)
                .weeklyRemindAuthor(projectMember)
                .weeklyRemindStardDate(message.getStartDate())
                .weeklyRemindEndDate(message.getEndDate()).build();

        weeklyRemindRepository.save(weeklyRemind);

        return true;
    }

    public List<WeeklyRemindResponseDto> searchWeeklyRemind (int projectId, Integer author
            ,LocalDate checkDate ,LocalDate startDate, LocalDate endDate) {

        List<WeeklyRemind> weeklyRemindList = weeklyRemindRepository.findWeeklyReminds(projectId, author
                , checkDate, startDate ,endDate);
        List<WeeklyRemindResponseDto> weeklyRemindResponseDtoList = new ArrayList<>();

        for(WeeklyRemind weeklyRemind : weeklyRemindList) {

            weeklyRemindResponseDtoList.add(WeeklyRemindResponseDto.builder()
                    .projectMemberId(weeklyRemind.getWeeklyRemindAuthor().getId())
                    .projectId(weeklyRemind.getProjectId().getId())
                    .username(weeklyRemind.getWeeklyRemindAuthor().getUser().getUserName())
                    .userId(weeklyRemind.getWeeklyRemindAuthor().getUser().getUserId())
                    .projectName(weeklyRemind.getProjectId().getTitle())
                    .projectStartDate(weeklyRemind.getProjectId().getStartDate().toLocalDate())
                    .projectEndDate(weeklyRemind.getProjectId().getEndDate().toLocalDate())
                    .userImage(weeklyRemind.getWeeklyRemindAuthor().getUser().getUserProfileImage())
                    .content(weeklyRemind.getWeeklyRemindContents())
                    .weeklyRemindId(weeklyRemind.getWeeklyRemindId())
                    .weeklyremindstartDate(weeklyRemind.getWeeklyRemindStardDate())
                    .weeklyremindendDate(weeklyRemind.getWeeklyRemindEndDate()).build());
        }

        return weeklyRemindResponseDtoList;
    }

    public List<DevelopmentStoryResponseDto> searchDevelopmentStory (Integer userId) {

        List<WeeklyRemind> weeklyRemindList = weeklyRemindRepository.findWeeklyRemindsByUserId(userId);
        List<DevelopmentStoryResponseDto> weeklyRemindResponseDtoList = new ArrayList<>();

        for (WeeklyRemind weeklyRemind : weeklyRemindList) {

            WeeklyRemindDto weeklyRemindDto = WeeklyRemindDto.builder()
                    .weeklyRemindId(weeklyRemind.getWeeklyRemindId())
                    .content(weeklyRemind.getWeeklyRemindContents())
                    .startDate(weeklyRemind.getWeeklyRemindStardDate())
                    .endDate(weeklyRemind.getWeeklyRemindEndDate()).build();

            weeklyRemindResponseDtoList.add(DevelopmentStoryResponseDto.builder()
                    .projectMemberId(weeklyRemind.getWeeklyRemindAuthor().getId())
                    .projectId(weeklyRemind.getProjectId().getId())
                    .username(weeklyRemind.getWeeklyRemindAuthor().getUser().getUserName())
                    .userId(weeklyRemind.getWeeklyRemindAuthor().getUser().getUserId())
                    .projectName(weeklyRemind.getProjectId().getTitle())
                    .projectStartDate(weeklyRemind.getProjectId().getStartDate().toLocalDate())
                    .projectEndDate(weeklyRemind.getProjectId().getEndDate().toLocalDate())
                    .weeklyRemind(weeklyRemindDto).build());
        }

        return weeklyRemindResponseDtoList;

    }

    @Transactional
    public boolean editWeeklyRemind(int weeklyRemindId, int projectId, WeeklyRemindRequestDto message) {
        WeeklyRemind weeklyRemind = weeklyRemindRepository.findById(weeklyRemindId).orElse(null);
        if (weeklyRemind == null) {
            return false;
        }

        List<DailyRemind> dailyRemindList = dailyRemindRepository.searchDailyReminds(message.getProjectMemberId(),
                message.getStartDate(), message.getEndDate(), projectId);

        StringBuilder dailyReminds = new StringBuilder();

        for (DailyRemind dailyRemind : dailyRemindList) {
            dailyReminds.append(dailyRemind.getDailyRemindContents());
        }

        String summary = chatAiService.generateWeeklyRemind(dailyReminds.toString(), projectId);

        weeklyRemind.updateWeeklyRemind(summary);

        return true;
    }
}
