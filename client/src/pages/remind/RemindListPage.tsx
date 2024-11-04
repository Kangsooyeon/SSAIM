import styles from './RemindListPage.module.css'
import remindBG from '../../assets/remind/remindBG.png'


const RemindListPage = () => {
  return (
  <div>
    <img src={remindBG} alt="remindBG" className={styles.remindBG}/>
    <div className={styles.container}>
      <div className={styles.remindIntroText}>
        <div className={styles.remindIntroDesc}>
            프로젝트를 마무리하며<br /> 나의 개발 이야기를 생성해보세요
        </div>
      </div>
      <div className={styles.remindButton}>
      </div>
    </div>

  
  </div>

  );
};

export default RemindListPage;