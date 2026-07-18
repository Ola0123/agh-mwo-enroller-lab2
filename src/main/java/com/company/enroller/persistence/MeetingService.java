package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll(String sortBy, String sortOrder, String key) {
		StringBuilder hql = new StringBuilder("FROM Meeting");
		boolean hasKey = key != null && !key.isEmpty();
		if (hasKey) {
			hql.append(" WHERE title LIKE :key");
		}
		if ("title".equalsIgnoreCase(sortBy) || "date".equalsIgnoreCase(sortBy)) {
			hql.append(" ORDER BY ").append(sortBy.toLowerCase()).append(" ");
			hql.append("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
		}
		Query query = this.session.createQuery(hql.toString());
		if (hasKey) {
			query.setParameter("key", "%" + key + "%");
		}
		return query.list();
	}

	public Meeting findById(long id) {
		return session.get(Meeting.class, id);
	}

	public Meeting add(Meeting meeting) {
		Transaction transaction = session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}

	public void update(Meeting meeting) {
		Transaction transaction = session.beginTransaction();
		session.merge(meeting);
		transaction.commit();
	}

	public void delete(Meeting meeting) {
		Transaction transaction = session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}

}
