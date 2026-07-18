package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
        StringBuilder hql = new StringBuilder("FROM Participant");
        boolean hasKey = key != null && !key.isEmpty();
        if (hasKey) {
            hql.append(" WHERE login LIKE :key");
        }
        if ("login".equalsIgnoreCase(sortBy)) {
            hql.append(" ORDER BY login ");
            hql.append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        }
        Query query = connector.getSession().createQuery(hql.toString());
        if (hasKey) {
            query.setParameter("key", "%" + key + "%");
        }
        return query.list();
    }

    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

}
