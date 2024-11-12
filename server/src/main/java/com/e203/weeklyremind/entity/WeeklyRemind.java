package com.e203.weeklyremind.entity;

import com.e203.global.entity.BaseEntity;
import com.e203.project.entity.Project;
import com.e203.project.entity.ProjectMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "weekly_remind")
public class WeeklyRemind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_remind_id")
    private int weeklyRemindId;

    @Lob
    @Column(columnDefinition = "TEXT", name = "weekly_remind_contents")
    private String weeklyRemindContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_remind_author")
    private ProjectMember weeklyRemindAuthor;

    private LocalDate weeklyRemindStardDate;

    private LocalDate weeklyRemindEndDate;

    @Builder
    private WeeklyRemind(String weeklyRemindContents, Project projectId, ProjectMember weeklyRemindAuthor
    , LocalDate weeklyRemindStardDate, LocalDate weeklyRemindEndDate) {
        this.weeklyRemindContents = weeklyRemindContents;
        this.projectId = projectId;
        this.weeklyRemindAuthor = weeklyRemindAuthor;
        this.weeklyRemindStardDate = weeklyRemindStardDate;
        this.weeklyRemindEndDate = weeklyRemindEndDate;
    }

    public void updateWeeklyRemind(String contents) {
        this.weeklyRemindContents = contents;
    }

}