package ro.home.project.domain.entity;

import lombok.Data;
import ro.home.project.domain.entity.listener.AuditListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditListener.class)
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	@Column(name = "INSERTED_AT", updatable = false)
	private LocalDateTime insertedAt;

	@Column(name = "UPDATED_AT")
	private LocalDateTime updatedAt;

	@Column(name = "INSERTED_BY", updatable = false)
	private String insertedBy;

	@Column(name = "UPDATED_BY", updatable = false)
	private String updatedBy;

}
