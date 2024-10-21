package org.cws.web.software.engineer.task.sync.processor;

import java.time.Instant;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.persistence.repository.GithubUserRepository;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class GithubUserProcessor implements ItemProcessor<GithubUserDTO, GithubUser> {

    private GithubUserRepository githubUserRepository;

    private Instant              modificationTimestamp;


    public GithubUserProcessor(GithubUserRepository githubUserRepository, Instant modificationTimestamp) {
        this.githubUserRepository = githubUserRepository;
        this.modificationTimestamp = modificationTimestamp;
        LoggerFactory.getLogger(GithubUserProcessor.class).info("modification timestamp: {}", modificationTimestamp);
    }

    @Override
    public GithubUser process(GithubUserDTO item) throws Exception {
        GithubUser user = githubUserRepository.getByGithubId(Long.valueOf(item.getId()));
        //@formatter:off
        if(user == null) {
            return GithubUser.builder()
                        .githubId(Long.valueOf(item.getId()))
                        .login(item.getLogin())
                        .modifiedAt(modificationTimestamp)
                        .build();
        } else {
            user.setModifiedAt(modificationTimestamp);
            return user;
        }
        //@formatter:on
    }

}
