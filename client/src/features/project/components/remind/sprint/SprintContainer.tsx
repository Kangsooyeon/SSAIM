import { useState } from 'react';
import styles from './SprintContainer.module.css';
import FilterHeader from './FilterHeader';
import Button from '../../../../../components/button/Button';
import WeekCalendar from './WeekCalendar'
import MySprint from './my/MySprint';
import TeamSprint from './team/TeamSprint';
import SprintModal from './SprintModal';


const SprintContainer = () => {
    const [myTeam, setMyTeam] = useState('나의 회고');
    const [selectedDate, setSelectedDate] = useState<Date>(new Date());
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    const formattedDate = new Intl.DateTimeFormat('ko', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'short',
      }).format(selectedDate).replace(/ (\S+)$/, ' ($1)');

      const handleOpenModal = () => {
        setIsModalOpen(true);
      };
    
      const handleCloseModal = () => {
        setIsModalOpen(false);
      };

  return (
    <div className={styles.container}>
      <div className={styles.left}>
        <FilterHeader
            myTeam={myTeam}
            setMyTeam={setMyTeam}
            formattedDate={formattedDate}
        />
        <div className={styles.remindContent}>
          {myTeam === '나의 회고' && <MySprint />}
          {myTeam === '팀원 회고' && <TeamSprint/>}
        </div>
        
      </div>
      <div className={styles.right}>
        <Button size="large" colorType="purple" onClick={handleOpenModal}>
          🚀 주간 회고 생성
        </Button>
        <p className={styles.description}>조회할 날짜를 선택해주세요</p>
        <WeekCalendar selectedDate={selectedDate} onDateChange={setSelectedDate} />
      </div>

      <SprintModal isOpen={isModalOpen} onClose={handleCloseModal}>

      </SprintModal>
    </div>
  );
};

export default SprintContainer;