package com.webyun.seagrid.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

/**
 * @author caozf
 */
@MappedSuperclass
public abstract class IdEntity<PK extends Serializable> implements Serializable {

	private static final long serialVersionUID = -4501224705356837581L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ID")
	@SequenceGenerator(name = "S_ID", allocationSize = 1, initialValue = 1, sequenceName = "S_ID")
	@Column(name = "id")
	protected PK id;

	public void setId(PK id) {
		this.id = id;
	}

	public PK getId() {
		return id;
	}

	@Transient
	public boolean isNew() {
		return null == getId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		IdEntity<?> idEntity = (IdEntity<?>) o;

		if (!id.equals(idEntity.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
