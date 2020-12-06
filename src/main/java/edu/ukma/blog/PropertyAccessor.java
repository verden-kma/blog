package edu.ukma.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PropertyAccessor {
    @Autowired
    private Environment environment;

    public static final String PROPERTY_ACCESSOR_BEAN_NAME;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        PROPERTY_ACCESSOR_BEAN_NAME = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }

    public long getExpirationTime() {
        return Long.parseLong(Objects.requireNonNull(environment.getProperty("expirationTime")));
    }

    public String getTokenPrefix() {
        return environment.getProperty("tokenPrefix");
    }

    public String getAuthHeader() {
        return environment.getProperty("authHeader");
    }

    public String getSignUpUrl() {
        return environment.getProperty("signUpUrl");
    }

    public String getTokenSecret() {
        return environment.getProperty("tokenSecret");
    }

    public int getMinUsernameLen() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("minUsernameLen")));
    }

    public int getMaxUsernameLen() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("maxUsernameLen")));
    }

    public int getPageSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("recordsPerPage")));
    }

    public int getEvalBlockSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("evaluatorsPerBlock")));
    }

    public int getCommentBlockSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("commentsPerBlock")));
    }

    public int getFollowersBlockSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("followersPerBlock")));
    }

    public int getDigestPageSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("digestPageSize")));
    }

    public int getSearchPageSize() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("searchPageSize")));
    }
}
