import styles from './Sprint.module.css';
import Issue from './Issue';
import Button from '../../../../components/button/Button';
import { useState, useMemo, useEffect } from 'react';
import SprintCreateModal from './SprintCreateModal';
import { useSprintIssueQuery } from '../../hooks/useSprintIssueData';
import { useProjectInfo } from '../../hooks/useProjectInfo';
import { useParams } from 'react-router-dom';
import calculateWeeks from '../../utils/calculateWeeks';
import { dateToString } from '@/utils/dateToString';

const WeeklyProgress = () => {
  const { projectId } = useParams<{ projectId: string }>();
  const { data: projectInfo } = useProjectInfo(Number(projectId));
  const projectWeekList = calculateWeeks(
    new Date(projectInfo.startDate as Date),
    new Date(projectInfo.endDate as Date)
  );

  const [currentWeek, setCurrentWeek] = useState(0);
  useMemo(()=>{
    setCurrentWeek(projectWeekList.length-1)
  },[projectWeekList.length])
  const { data: sprintIssues } = useSprintIssueQuery(
    Number(projectId),
    dateToString(projectWeekList[currentWeek - 1]?.startDate, '-'),
    dateToString(projectWeekList[currentWeek - 1]?.endDate, '-')
  );

  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleModalOpen = () => {
    setIsModalOpen(true);
  };
  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  const handleIncreaseWeek = () => {
    if (currentWeek < projectWeekList.length - 1) {
      setCurrentWeek(currentWeek + 1);
    }
  };

  const handleDecreaseWeek = () => {
    if (currentWeek > 0) {
      setCurrentWeek(currentWeek - 1);
    }
  };

  // 요일 매핑 테이블 생성 (예: 월, 화, 수, 목, 금)
  const dayMap = ["월", "화", "수", "목", "금", "날짜 미지정"];

  const dateMap = useMemo(() => {
    if (!projectWeekList[currentWeek - 1]) return {};
  
    const endDate = new Date(projectWeekList[currentWeek - 1].endDate);
  
    return {
      "금": new Date(endDate).getDate(), // 금요일은 그대로 endDate
      "목": new Date(new Date(endDate).setDate(endDate.getDate() - 1)).getDate(),
      "수": new Date(new Date(endDate).setDate(endDate.getDate() - 2)).getDate(),
      "화": new Date(new Date(endDate).setDate(endDate.getDate() - 3)).getDate(),
      "월": new Date(new Date(endDate).setDate(endDate.getDate() - 4)).getDate(),
    };
  }, [projectWeekList, currentWeek]);
      
  console.log(dateMap)
      // 요일별로 sprintIssues를 분류
      const issuesByDay = useMemo(() => {
        // dayMap을 기반으로 요일별 빈 배열로 초기화
        const dayIssueMap = dayMap.reduce((acc, day) => {
          acc[day] = [];
          return acc;
        }, {} as Record<string, any[]>);
      
        // 각 issue를 dayIssueMap에 분류
        sprintIssues?.forEach((issue: any) => {
          const match = issue.title.match(/(월|화|수|목|금)/); // 요일 추출
          const day = match ? match[0] : "날짜 미지정";
          if (dayIssueMap[day]) {
            dayIssueMap[day].push(issue); // 해당 요일에 issue 추가
          } else {
            dayIssueMap["날짜 미지정"].push(issue); // 매핑되지 않으면 "날짜 미지정"에 추가
          }
        });
      
        return dayIssueMap;
      }, [sprintIssues, dayMap]);
      
  return (
    <>
      <div className={styles.header}>
        <div className={styles.teamProfiles}>
          <div className={styles.profilePicture}></div>
          <div className={styles.profilePicture}></div>
          <div className={styles.profilePicture}></div>
        </div>
        <button className={styles.arrowButton} onClick={handleDecreaseWeek}>
          &lt;
        </button>
        <h2 className={styles.sprintTitle}>{currentWeek}주차</h2>
        <p>
          {dateToString(projectWeekList[currentWeek - 1]?.startDate)}~
          {dateToString(projectWeekList[currentWeek - 1]?.endDate)}
        </p>
        <button className={styles.arrowButton} onClick={handleIncreaseWeek}>
          &gt;
        </button>

        <div className={styles.buttonPlaceholder}>
          <Button
            children="스프린트 생성"
            colorType="blue"
            size="small"
            onClick={handleModalOpen}
          ></Button>
        </div>
      </div>
      <div className={styles.weeklyProgressContainer}>
        {/* 요일 헤더 */}
        <div className={styles.contentheader}>
          {dayMap.map((day) => (
            <div key={day} className={styles.dayHeader}>
              {day}{dateMap[day]}
            </div>
          ))}
        </div>

        {/* 요일별 이슈 */}
        <div className={styles.issueGrid}>
          {dayMap.map((day) => (
            <div key={day} className={styles.dayColumn}>
              {issuesByDay[day].map((issue: any) => (
                <Issue
                  key={issue.issueKey}
                  title={issue.title}
                  status={issue.progress}
                  epicCode={issue.epicCode}
                  storyPoint={issue.storyPoint}
                  issueKey={issue.issueKey}
                />
              ))}
            </div>
          ))}
        </div>
      </div>
      {isModalOpen && <SprintCreateModal onClose={handleModalClose} />}
    </>
  );
};

export default WeeklyProgress;
