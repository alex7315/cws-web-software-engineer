module org.cws.web.software.engineer.task.persistence {

    requires transitive lombok;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires org.hibernate.orm.core;

    exports org.cws.web.software.engineer.task.persistence.repository;
    exports org.cws.web.software.engineer.task.persistence.model;

    opens org.cws.web.software.engineer.task.persistence.model to org.cws.web.software.engineer.task.security, org.hibernate.orm.core, spring.core;
    opens org.cws.web.software.engineer.task.persistence.repository to org.cws.web.software.engineer.task.security;
}
