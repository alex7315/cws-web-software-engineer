module org.cws.web.software.engineer.task.security {

    requires org.apache.tomcat.embed.core;
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
    requires spring.security.core;
    requires spring.security.web;
    requires spring.security.crypto;
    requires spring.data.jpa;
    requires org.hibernate.orm.core;

    requires transitive lombok;

    requires org.cws.web.software.engineer.task.persistence;
    
    exports org.cws.web.software.engineer.task.security.authority;
    exports org.cws.web.software.engineer.task.security.service;
    exports org.cws.web.software.engineer.task.security.exception;
}
