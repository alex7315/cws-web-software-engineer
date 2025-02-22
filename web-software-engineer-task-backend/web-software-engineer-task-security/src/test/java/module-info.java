open module org.cws.web.software.engineer.task.security {


    requires org.apache.tomcat.embed.core;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.classmate;

    requires jjwt.api;
    requires jjwt.impl;
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
    requires spring.boot;
    requires spring.boot.starter;
    requires spring.boot.starter.data.jpa;
    requires spring.boot.starter.aop;
    requires spring.boot.starter.jdbc;
    requires spring.boot.starter.security;
    requires spring.boot.starter.logging;
    requires spring.boot.test.autoconfigure;
    requires com.zaxxer.hikari;
    //    requires org.junit.jupiter;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.engine;
    requires org.assertj.core;
    requires jsonassert;
    requires org.jboss.logging;
    requires jakarta.transaction;
    requires jakarta.cdi;
    requires jakarta.xml.bind;
    requires org.hibernate.commons.annotations;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;
    requires org.objenesis;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires mockito.subclass;


    requires transitive lombok;
    requires transitive jakarta.persistence;
    requires transitive org.hibernate.orm.core;
    requires transitive spring.data.jpa;
    requires transitive spring.data.commons;

    requires org.cws.web.software.engineer.task.persistence;

    exports org.cws.web.software.engineer.task.security.service;
    exports org.cws.web.software.engineer.task.security.exception;
}
