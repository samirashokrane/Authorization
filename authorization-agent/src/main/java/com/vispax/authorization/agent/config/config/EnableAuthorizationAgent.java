package com.vispax.authorization.agent.config.config;


import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(value = TYPE)
@Retention(value = RUNTIME)
@Import(value = AuthorizationAgentConfig.class)
@Documented
public @interface EnableAuthorizationAgent {
}
