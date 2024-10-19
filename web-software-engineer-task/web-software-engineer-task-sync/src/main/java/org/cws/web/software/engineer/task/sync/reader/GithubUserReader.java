package org.cws.web.software.engineer.task.sync.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.exception.GithubSynchJobReaderException;
import org.cws.web.software.engineer.task.sync.mapper.GithubUserMapper;
import org.cws.web.software.engineer.task.sync.service.GithubUsersService;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubUserReader implements ItemStreamReader<GithubUser> {

    private int                     maxUserCount;

    private int                     userPageSize;

    private int                     userCounter = 1;

    private long                    currentUserId;

    private Iterator<GithubUserDTO> userIterator;

    private GithubUsersService      githubUsersService;
    private GithubUserMapper        githubUserMapper;


    public GithubUserReader(GithubUsersService githubUsersService, GithubUserMapper githubUserMapper,
            @Value("${cws.github.user.page.size:100}") int userPageSize, @Value("${cws.github.user.count.max:1000}") int maxUserCount) {
        this.githubUsersService = githubUsersService;
        this.githubUserMapper = githubUserMapper;
        this.maxUserCount = maxUserCount;
        this.userPageSize = userPageSize;

        List<GithubUserDTO> userList = new ArrayList<>();
        this.userIterator = userList.iterator();
    }

    @Override
    public GithubUser read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (userIterator.hasNext()) {
            return getNextUser();
        } else {
            userIterator = githubUsersService.getUsers(currentUserId, userPageSize).iterator();
            return getNextUser();
        }
    }

    private GithubUser getNextUser() {
        if (maxUserCount < userCounter) {
            return null;
        }

        try {
            GithubUserDTO githubUserDto = userIterator.next();
            userCounter += 1;
            currentUserId = Long.valueOf(githubUserDto.getId());
            return githubUserMapper.toModel(githubUserDto);
        } catch (NumberFormatException e) {
            throw new GithubSynchJobReaderException("Error to parse github user id value", e);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
