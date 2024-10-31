import { Routes, Route, Navigate } from 'react-router-dom';
import MainPage from '../pages/mainPage/MainPage';
import ProjectListPage from '../pages/project/ProjectListPage';
import ProjectDetailPage from '../pages/project/ProjectDetailPage';
import ProjectSprintPage from '../pages/project/ProjectSprintPage';
import ProjectOutputPage from '../pages/project/ProjectOutputPage';
import ProjectMeetingPage from '../pages/project/ProjectMeetingPage';
import ProjectRemindPage from '../pages/project/ProjectRemindPage';
import MainLayout from '../components/mainLayout/MainLayout';
import MeetingListPage from '../pages/meeting/MeetingListPage';
import RemindListPage from '../pages/remind/RemindListPage';
import MeetingCreatePage from '../pages/meeting/MeetingCreatePage';
import MyPage from '../pages/myPage/MyPage';
const AppRoutes = () => (
  <Routes>
    <Route element={<MainLayout />}>
      <Route path="/" element={<MainPage />} />
      {/* 프로젝트 */}
      <Route path="/project" element={<ProjectListPage />}></Route>
      <Route path="/project/:projectId" element={<ProjectDetailPage />}></Route>
      <Route path="/project/:projectId/sprint" element={<ProjectSprintPage />}></Route>
      <Route path="/project/:projectId/output" element={<ProjectOutputPage />}></Route>
      <Route path="/project/:projectId/meeting" element={<ProjectMeetingPage />}></Route>
      <Route path="/project/:projectId/remind" element={<ProjectRemindPage />}></Route>
      {/* 팀빌딩 */}
      <Route path="/meeting" element={<MeetingListPage />}></Route>
      <Route path="/meeting/create" element={<MeetingCreatePage />}></Route>
      {/* 회고 */}
      <Route path="/remind" element={<RemindListPage />}></Route>
      {/* 마이페이지 */}
      <Route path="/mypage" element={<MyPage />}></Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Route>
  </Routes>
);

export default AppRoutes;