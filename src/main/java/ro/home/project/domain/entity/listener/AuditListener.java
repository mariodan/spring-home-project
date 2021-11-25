package ro.home.project.domain.entity.listener;

import ro.home.project.domain.entity.BaseEntity;
import ro.home.project.util.DateUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {


	private static final String DEFAULT_USER = "system";

	@PrePersist
	public void prePersist(final BaseEntity baseEntity) {
		final LocalDateTime now = DateUtils.currentDateTime();
		baseEntity.setUpdatedAt(now);
		baseEntity.setInsertedAt(now);

		final String insertedBy = baseEntity.getInsertedBy() == null ? getUser() : baseEntity.getInsertedBy();
		baseEntity.setInsertedBy(insertedBy);
		baseEntity.setUpdatedBy(insertedBy);
	}

	@PreUpdate
	public void preUpdate(final BaseEntity baseEntity) {
		baseEntity.setUpdatedAt(DateUtils.currentDateTime());
		baseEntity.setUpdatedBy(getUser());
	}

	private String getUser() {
		return DEFAULT_USER;
	}
}
