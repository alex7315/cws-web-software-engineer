open module org.cws.web.software.engineer.task.backend {

    requires org.apache.tomcat.embed.core;
    //    requires org.apache.tomcat.embed.el;

    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires jjwt.api;
    requires jjwt.impl;
    requires spring.data.commons;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
    requires spring.jdbc;
    requires spring.orm;
    requires spring.aspects;
    requires spring.test;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.security.crypto;

    //additional test requirements 
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter;
    requires spring.boot.starter.data.jpa;
    requires spring.boot.starter.aop;
    requires spring.boot.starter.jdbc;
    requires spring.boot.starter.security;
    requires spring.boot.starter.logging;
    requires spring.boot.test.autoconfigure;
    requires spring.data.jpa;

    requires org.mapstruct;

    requires org.hibernate.orm.core;
    requires org.hibernate.validator;

    requires org.apache.httpcomponents.client5.httpclient5;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.classmate;

    requires com.zaxxer.hikari;

    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.engine;
    requires org.assertj.core;

    requires android.json;
    //requires jsonassert;

    requires org.jboss.logging;

    requires jakarta.transaction;
    requires jakarta.validation;
    //    requires jakarta.cdi;
    //    requires jakarta.xml.bind;
    //    requires jakarta.persistence;

    requires java.annotation;
    requires java.compiler;

    requires org.hibernate.commons.annotations;

    requires org.mockito;
    requires org.mockito.junit.jupiter;
    //    requires mockito.subclass;
    requires org.objenesis;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;





    requires org.cws.web.software.engineer.task.persistence;
    requires org.cws.web.software.engineer.task.security;

    exports org.cws.web.software.engineer.task.backend.dto;
    exports org.cws.web.software.engineer.task.backend.dto.request;
    exports org.cws.web.software.engineer.task.backend.dto.response;
    exports org.cws.web.software.engineer.task.backend.controller;
}
