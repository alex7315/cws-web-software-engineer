open module org.cws.web.software.engineer.task.backend {

    requires org.slf4j;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.web;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.security.config;
    requires spring.security.crypto;
    requires spring.webmvc;
    requires spring.data.commons;

    requires spring.boot;
    requires spring.boot.starter.web;
    requires spring.boot.autoconfigure;

    requires org.hibernate.validator;

    requires org.apache.tomcat.embed.core;
    requires org.apache.tomcat.embed.el;

    requires io.swagger.v3.oas.annotations;

    requires jakarta.validation;
    requires jakarta.transaction;
    requires java.annotation;
    requires java.compiler;

    requires org.mapstruct;

    requires org.cws.web.software.engineer.task.persistence;
    requires org.cws.web.software.engineer.task.security;
}
