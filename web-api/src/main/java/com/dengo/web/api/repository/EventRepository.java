package com.dengo.web.api.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.dengo.model.common.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WEB API Repository
 *
 * Repository that handles all requests from Cassandra data base
 *
 * @author Sergey Petrovsky
 * @since 0.0.1
 */

@Repository
public class EventRepository {

    private static final String cassandraName = "cassandra";
    private static final String ALL_EVENTS = "SELECT * FROM api.event";

    private Cluster cluster;
    private Session session;

    private void connect() {
        cluster = Cluster.builder().addContactPoint(cassandraName).build();
        session = cluster.connect();
    }

    public List<Event> getEvents() {
        connect();
        ResultSet resultSet = session.execute(ALL_EVENTS);
        List<Event> events = new ArrayList<>();
        resultSet.forEach(row -> events.add(new Event(row.getString("id"), row.getString("name"))));
        return events;
    }
}
