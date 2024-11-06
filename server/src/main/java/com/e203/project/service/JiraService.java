package com.e203.project.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.e203.project.dto.request.JiraIssueResponseDto;
import com.e203.project.dto.request.ProjectJiraConnectDto;
import com.e203.project.dto.jiraapi.JiraResponse;
import com.e203.project.entity.Project;
import com.e203.project.entity.ProjectMember;
import com.e203.project.repository.ProjectMemberRepository;
import com.e203.project.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JiraService {
	// jira api key를 db에 저장하기
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	private final String JIRA_URL = "https://ssafy.atlassian.net/rest/api/3/search";

	@Transactional
	public Boolean setJiraApi(ProjectJiraConnectDto jiraApi, int projectId) {
		Project project = projectRepository.findById(projectId).orElse(null);
		if (project == null) {
			return false;
		}
		System.out.println(jiraApi.getJiraApi());
		project.setJiraApi(jiraApi.getJiraApi());
		return true;
	}

	@Transactional
	public List<JiraIssueResponseDto> findAllJiraIssues(String startDate, String endDate, int projectId) {
		JiraResponse jiraIssues = getJiraIssues(startDate, endDate, projectId);
		return jiraIssues.getIssues() == null ? null : jiraIssues.getIssues().stream()
			.map(JiraIssueResponseDto::transferDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public JiraResponse getJiraIssues(String startDate, String endDate, int projectId) {
		ProjectMember leader = getProjectLeader(projectId);
		String jiraApi = leader.getProject().getJiraApi();
		String userEmail = leader.getUser().getUserEmail();

		String jql = "project=S11P31E203 AND created >= \"" + startDate + "\" AND created <= \"" + endDate + "\"";
		String fields = "summary,status,assignee,customfield_10014,customfield_10031";
		String url = JIRA_URL + "?jql=" + jql + "&fields=" + fields;

		String encodedCredentials = Base64.getEncoder().encodeToString((userEmail + ":" + jiraApi).getBytes());

		try {
			String responseBody = restClient.get()
				.uri(url)
				.header("Authorization", "Basic " + encodedCredentials)
				.header("Content-Type", "application/json")
				.retrieve()
				.body(String.class);
			return objectMapper.readValue(responseBody, JiraResponse.class); // JSON 응답을 JiraResponse 객체로 변환
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
			return null;
		}
	}

	private ProjectMember getProjectLeader(int projectId) {
		List<ProjectMember> leader = projectMemberRepository.findByProjectIdAndRole(projectId, 1);
		if (leader.size() != 1) {
			return null;
		}
		return leader.get(0);

	}
}