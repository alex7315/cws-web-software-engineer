package org.cws.web.software.engineer.task.sync.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.exception.InvalidGithubResponseException;
import org.cws.web.software.engineer.task.sync.exception.NotAuthorizedGithubRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GithubUsersServiceImpl implements GithubUsersService {

    private String              githubApiVersion;

    private String              githubAuthorizationToken;

    private ObjectMapper        objectMapper;

    private RestClient.Builder builder;

    public GithubUsersServiceImpl(@Value("${cws.github.api.version:2022-11-28}") String githubApiVersion,
            @Value("${cws.github.authorization.token}") String githubAuthorizationToken, RestClient.Builder builder, ObjectMapper objectMapper) {
        this.githubApiVersion = githubApiVersion;
        this.githubAuthorizationToken = githubAuthorizationToken;
        this.builder = builder;
        this.objectMapper = objectMapper;
    }

    public List<GithubUserDTO> getUsers(Long fromId, Integer pageSize) {
        //@formatter:off
        return builder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                                    .path("https:///")
                                    .pathSegment("api.github.com", "users")
                                    .queryParam("since", fromId)
                                    .queryParam("per_page", pageSize)
                                    .build(new HashMap<>()))
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", githubApiVersion)
                .header("Authorization", "Bearer " + githubAuthorizationToken)
                .exchange((request, response) -> exchangeGithubUsersGetRequest(fromId, pageSize, response)
                );
        //@formatter:on
    }

    private List<GithubUserDTO> exchangeGithubUsersGetRequest(Long fromId, Integer pageSize, ConvertibleClientHttpResponse response) throws IOException {
        //@formatter:off
        if(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
            return objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GithubUserDTO.class));
        } else if(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401))) {
            throw new NotAuthorizedGithubRequestException(String.format("Error to get from: %s Status code: %d", "/api.github.com/users", response.getStatusCode().value()));
        } else {
            throw new InvalidGithubResponseException(String.format("Error to get from: %s since: %d per_page: %d Status code: %d", "/api.github.com/users"
                    , fromId
                    , pageSize
                    , response.getStatusCode().value()));
        }
        //@formatter:on
    }

    public RestClient.Builder getBuilder() {
        return builder;
    }
}
