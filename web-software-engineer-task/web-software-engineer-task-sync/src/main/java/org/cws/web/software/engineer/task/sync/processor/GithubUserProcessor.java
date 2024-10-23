package org.cws.web.software.engineer.task.sync.processor;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * implements spring batch {@link ItemProcessor} that creates new
 * {@link GithubUser} object or modifies existed one with data from
 * {@code GithubUserDTO}
 */
public class GithubUserProcessor implements ItemProcessor<GithubUserDTO, GithubUser> {

	private GithubUserRepository githubUserRepository;

	private Long modificationId;

	public GithubUserProcessor(GithubUserRepository githubUserRepository, Long modificationId) {
		this.githubUserRepository = githubUserRepository;
		this.modificationId = modificationId;
		LoggerFactory.getLogger(GithubUserProcessor.class).debug("modification id: {}", modificationId);
	}

	@Override
	public GithubUser process(GithubUserDTO item) throws Exception {
		GithubUser user = githubUserRepository.getByGithubId(Long.valueOf(item.getId()));
		//@formatter:off
        if(user == null) {
            return GithubUser.builder()
                        .githubId(Long.valueOf(item.getId()))
                        .login(item.getLogin())
                        .modificationId(modificationId)
                        .build();
        } else {
            user.setModificationId(modificationId);
            return user;
        }
        //@formatter:on
	}

}
