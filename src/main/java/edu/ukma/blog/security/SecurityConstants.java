package edu.ukma.blog.security;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME;
    public static final String TOKEN_PREFIX;
    public static final String AUTH_HEADER;
    public static final String SIGN_UP_URL;
    public static final String TOKEN_SECRET;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        String propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

        EXPIRATION_TIME = getExpirationTime(propertyAccessorBeanName);
        TOKEN_PREFIX = getTokenPrefix(propertyAccessorBeanName);
        AUTH_HEADER = getAuthHeader(propertyAccessorBeanName);
        SIGN_UP_URL = getSignUpUrl(propertyAccessorBeanName);
        TOKEN_SECRET = getTokenSecret(propertyAccessorBeanName);
    }

    private static long getExpirationTime(String propertyAccessorBeanName) {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getExpirationTime();
    }

    private static String getTokenPrefix(String propertyAccessorBeanName) {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getTokenPrefix();
    }

    private static String getAuthHeader(String propertyAccessorBeanName) {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getAuthHeader();
    }

    private static String getSignUpUrl(String propertyAccessorBeanName) {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getSignUpUrl();
    }

    private static String getTokenSecret(String propertyAccessorBeanName) {
        return ((PropertyAccessor) SpringApplicationContext.getBean(propertyAccessorBeanName)).getTokenSecret();
    }
}
