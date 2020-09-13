package edu.ukma.blog.security;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME;
    public static final String TOKEN_PREFIX;
    public static final String AUTH_HEADER;
    public static final String SIGN_UP_URL;
    public static final String TOKEN_SECRET;

    private static final String propertyAccessorBeanName;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

        EXPIRATION_TIME = getExpirationTime();
        TOKEN_PREFIX = getTokenPrefix();
        AUTH_HEADER = getAuthHeader();
        SIGN_UP_URL = getSignUpUrl();
        TOKEN_SECRET = getTokenSecret();
    }

    private static long getExpirationTime() {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getExpirationTime();
    }

    private static String getTokenPrefix() {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getTokenPrefix();
    }

    private static String getAuthHeader() {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getAuthHeader();
    }

    private static String getSignUpUrl() {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getSignUpUrl();
    }

    private static String getTokenSecret() {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getTokenSecret();
    }
}
