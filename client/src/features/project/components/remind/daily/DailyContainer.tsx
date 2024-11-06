import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './DailyContainer.module.css';
import FilterHeader from './FilterHeader';
import DayTeamRemind from '../daily/dayTeam/DayTeamRemind'; 
import DayMyRemind from '../daily/dayMy/DayMyRemind';
import WeekRemind from '..//daily/week/WeekRemind';
import Button from '../../../../../components/button/Button';
import DayCalendar from './DayCalendar';
import { useProjectInfo } from '@features/project/hooks/useProjectInfo';
import useUserStore from '@/stores/useUserStore';
import usePmIdStore from '@/features/project/stores/remind/usePmIdStore';
import { useDailyRemind } from '@/features/project/hooks/remind/useDailyRemind'; // useDailyRemind 훅을 임포트
import { format } from 'date-fns';



interface ProjectMember {
  userId: number;
  pmId: number;
}



const DailyContainer = () => {
  const navigate = useNavigate();
  const { projectId } = useParams<{ projectId: string }>();
  const [dayWeek, setDayWeek] = useState('1일');
  const [myTeam, setMyTeam] = useState('나의 회고');
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  const { data: projectInfo } = useProjectInfo(Number(projectId));
  const { userId } = useUserStore();
  const { pmId, setPmId } = usePmIdStore();

  const { data: dailyRemindData, isError, error } = useDailyRemind({
    projectId: Number(projectId), // projectId를 넘겨줘야 함
    projectMemberId: undefined,   // 예시로 undefined로 설정
    startDate: undefined,      // 날짜 예시
    endDate: undefined,        // 날짜 예시
  });

  useEffect(() => {
    if (projectInfo && projectInfo.projectMemberFindResponseDtoList && userId) {
      const projectMember = projectInfo.projectMemberFindResponseDtoList.find(
        (member: ProjectMember) => member.userId === userId
      );
      if (projectMember) {
        setPmId(projectMember.pmId); // pmId 상태 업데이트
      }
    }

    // API 응답 데이터 콘솔 출력
    if (dailyRemindData) {
      console.log('Daily Remind Data:', dailyRemindData);
    }

    if (isError) {
      console.error('Error fetching daily remind:', error);
    }
  }, [projectInfo, userId, setPmId, projectId, dailyRemindData, isError, error]);

  const formattedDate = new Intl.DateTimeFormat('ko', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'short',
  }).format(selectedDate).replace(/ (\S+)$/, ' ($1)');

  const handleButtonClick = () => {
    navigate(`/project/${projectId}/remind/create`); 
  };

  const formattedSelectedDate = format(selectedDate, 'yyyy-MM-dd');

  const filteredMessages = dailyRemindData?.filter((item) =>
    item.projectMemberId === pmId && item.dailyRemindDate === formattedSelectedDate
  ) || [];

  return (
    <div className={styles.container}>
      <div className={styles.left}>
        <FilterHeader
          dayWeek={dayWeek}
          setDayWeek={setDayWeek}
          myTeam={myTeam}
          setMyTeam={setMyTeam}
          formattedDate={formattedDate}
        />
        <div className={styles.remindContent}>
          {dayWeek === '1일' && myTeam === '나의 회고' && <DayMyRemind messages={filteredMessages} />}
          {dayWeek === '1일' && myTeam === '팀원 회고' && <DayTeamRemind />}
          {dayWeek === '1주일' && myTeam === '나의 회고' && <WeekRemind />}
          {dayWeek === '1주일' && myTeam === '팀원 회고' && <WeekRemind />}
        </div>
      </div>
      <div className={styles.right}>
        <Button size="large" colorType="blue" onClick={handleButtonClick}>
          📝 일일 회고 작성
        </Button>
        <p className={styles.description}>조회할 날짜를 선택해주세요</p>
        <DayCalendar selectedDate={selectedDate} onDateChange={setSelectedDate} />
      </div>
    </div>
  );
};

export default DailyContainer;
