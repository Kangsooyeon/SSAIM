package com.e203.document.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.e203.document.collection.Proposal;
import com.e203.document.repository.ProposalRepository;
import com.mongodb.client.result.UpdateResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalService {

	private final MongoTemplate mongoTemplate;
	private final ProposalRepository proposalRepository;

	public void createIndex() {
		// Collation 설정
		Collation collation = Collation.of("ko");

		// 인덱스 정의
		IndexDefinition indexDefinition = new Index()
			.on("createdAt", Sort.Direction.DESC)
			.collation(collation);

		// 인덱스 추가
		IndexOperations indexOps = mongoTemplate.indexOps(Proposal.class);
		indexOps.ensureIndex(indexDefinition);
	}

	public Proposal getProposal(String projectId) {
		List<Proposal> results = proposalRepository.findTopByProjectIdOrderByCreatedAtDesc(
			projectId, Sort.by(Sort.Direction.DESC, "createdAt"));
		return results.isEmpty() ? null : results.get(0); // 결과가 없으면 null 반환
	}

	public String getProposalContent(String projectId){
		return getProposal(projectId).getContent();
	}

	public Proposal updateProposalContent(String projectId, String content) {
		Query query = new Query(Criteria.where("projectId").is(projectId));

		Update update = new Update().set("content", content);

		UpdateResult documents = mongoTemplate.updateFirst(query, update, "Proposal");

		return getProposal(projectId);
	}

	public Proposal saveProposal(String projectId) {
		Proposal proposal = Proposal.builder()
			.projectId(projectId)
			.content("")
			.build();
		return proposalRepository.save(proposal);
	}
}
