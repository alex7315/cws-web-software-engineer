package org.cws.web.software.engineer.task.sync.service;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.cws.web.software.engineer.task.sync.dto.GithubUserDTO;
import org.cws.web.software.engineer.task.sync.exception.InvalidGithubResponseException;
import org.cws.web.software.engineer.task.sync.exception.NotAuthorizedGithubRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link GithubUsersService} gets GitHub users using request to GitHub REST API
 */
@Service
public class GithubUsersServiceImpl implements GithubUsersService {

    private static final String HEADER_AUTHORIZATION   = "Authorization";

    private static final String HEADER_API_VERSION     = "X-GitHub-Api-Version";

    private static final String HEADER_ACCEPT          = "Accept";

    private static final String PARAM_NAME_PAGE_SIZE   = "per_page";

    private static final String PARAM_NAME_FROM_ID     = "since";

    private static final String ACCEPTED_MEDIA_DEFAULT = "application/vnd.github+json";

    private final Logger        logger                 = LoggerFactory.getLogger(GithubUsersServiceImpl.class);

	private String githubApiVersion;

	private String githubAuthorizationToken;

    private String             githubBaseUrl;

	private ObjectMapper objectMapper;

	private RestClient.Builder builder;

    //@formatter:off
	public GithubUsersServiceImpl(@Value("${cws.github.api.version:2022-11-28}") String githubApiVersion
			, @Value("${cws.github.api.authorization.token}") String githubAuthorizationToken
			, @Value("${cws.github.api.base.url}") String githubBaseUrl
			, RestClient.Builder builder
			, ObjectMapper objectMapper) {
		this.githubApiVersion = githubApiVersion;
		this.githubAuthorizationToken = githubAuthorizationToken;
		this.githubBaseUrl = githubBaseUrl;
		this.builder = builder;
		this.objectMapper = objectMapper;
	}

	/**
	 * gets list of github users from GitHub REST API e.g. {@code https://api.github.com/users}
	 * 
	 * @see GithubUsersService#getUsers(Long, Integer)
	 */
	public List<GithubUserDTO> getUsers(Long fromId, Integer pageSize) {
		logger.debug("gets github users from user id {} page size {}", fromId, pageSize);

		//@formatter:off
        return this.builder
				.baseUrl(githubBaseUrl + "/users")
				.defaultHeader(HEADER_ACCEPT, ACCEPTED_MEDIA_DEFAULT)
				.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                                    .queryParam(PARAM_NAME_FROM_ID, fromId)
                                    .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
                                    .build(new HashMap<>()))
                .header(HEADER_API_VERSION, githubApiVersion)
                .header(HEADER_AUTHORIZATION, "Bearer " + githubAuthorizationToken)
                .exchange((request, response) -> exchangeGithubUsersGetRequest(fromId, pageSize, response)
                );
        //@formatter:on
	}

	private List<GithubUserDTO> exchangeGithubUsersGetRequest(Long fromId, Integer pageSize,
			ConvertibleClientHttpResponse response) throws IOException {
		//@formatter:off
        if(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(OK.value()))) {
            return objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GithubUserDTO.class));
        } else if(response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(UNAUTHORIZED.value()))) {
            throw new NotAuthorizedGithubRequestException(String.format("Error to get from: %s Status code: %d"
                    , githubBaseUrl
                    , response.getStatusCode().value()));
        } else {
            throw new InvalidGithubResponseException(String.format("Error to get from: %s since: %d per_page: %d Status code: %d"
                    , githubBaseUrl
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
