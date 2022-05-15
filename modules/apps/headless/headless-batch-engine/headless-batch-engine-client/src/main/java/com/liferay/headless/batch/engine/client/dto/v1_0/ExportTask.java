/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.headless.batch.engine.client.dto.v1_0;

import com.liferay.headless.batch.engine.client.function.UnsafeSupplier;
import com.liferay.headless.batch.engine.client.serdes.v1_0.ExportTaskSerDes;

import java.io.Serializable;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Ivica Cardic
 * @generated
 */
@Generated("")
public class ExportTask implements Cloneable, Serializable {

	public static ExportTask toDTO(String json) {
		return ExportTaskSerDes.toDTO(json);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassName(
		UnsafeSupplier<String, Exception> classNameUnsafeSupplier) {

		try {
			className = classNameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String className;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentType(
		UnsafeSupplier<String, Exception> contentTypeUnsafeSupplier) {

		try {
			contentType = contentTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String contentType;

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndTime(
		UnsafeSupplier<Date, Exception> endTimeUnsafeSupplier) {

		try {
			endTime = endTimeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date endTime;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setErrorMessage(
		UnsafeSupplier<String, Exception> errorMessageUnsafeSupplier) {

		try {
			errorMessage = errorMessageUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String errorMessage;

	public ExecuteStatus getExecuteStatus() {
		return executeStatus;
	}

	public String getExecuteStatusAsString() {
		if (executeStatus == null) {
			return null;
		}

		return executeStatus.toString();
	}

	public void setExecuteStatus(ExecuteStatus executeStatus) {
		this.executeStatus = executeStatus;
	}

	public void setExecuteStatus(
		UnsafeSupplier<ExecuteStatus, Exception> executeStatusUnsafeSupplier) {

		try {
			executeStatus = executeStatusUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ExecuteStatus executeStatus;

	public String getExternalReferenceCode() {
		return externalReferenceCode;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		this.externalReferenceCode = externalReferenceCode;
	}

	public void setExternalReferenceCode(
		UnsafeSupplier<String, Exception> externalReferenceCodeUnsafeSupplier) {

		try {
			externalReferenceCode = externalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String externalReferenceCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public Integer getProcessedItemsCount() {
		return processedItemsCount;
	}

	public void setProcessedItemsCount(Integer processedItemsCount) {
		this.processedItemsCount = processedItemsCount;
	}

	public void setProcessedItemsCount(
		UnsafeSupplier<Integer, Exception> processedItemsCountUnsafeSupplier) {

		try {
			processedItemsCount = processedItemsCountUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer processedItemsCount;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setStartTime(
		UnsafeSupplier<Date, Exception> startTimeUnsafeSupplier) {

		try {
			startTime = startTimeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date startTime;

	public Integer getTotalItemsCount() {
		return totalItemsCount;
	}

	public void setTotalItemsCount(Integer totalItemsCount) {
		this.totalItemsCount = totalItemsCount;
	}

	public void setTotalItemsCount(
		UnsafeSupplier<Integer, Exception> totalItemsCountUnsafeSupplier) {

		try {
			totalItemsCount = totalItemsCountUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer totalItemsCount;

	@Override
	public ExportTask clone() throws CloneNotSupportedException {
		return (ExportTask)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ExportTask)) {
			return false;
		}

		ExportTask exportTask = (ExportTask)object;

		return Objects.equals(toString(), exportTask.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ExportTaskSerDes.toJSON(this);
	}

	public static enum ExecuteStatus {

		COMPLETED("COMPLETED"), FAILED("FAILED"), INITIAL("INITIAL"),
		STARTED("STARTED");

		public static ExecuteStatus create(String value) {
			for (ExecuteStatus executeStatus : values()) {
				if (Objects.equals(executeStatus.getValue(), value) ||
					Objects.equals(executeStatus.name(), value)) {

					return executeStatus;
				}
			}

			return null;
		}

		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private ExecuteStatus(String value) {
			_value = value;
		}

		private final String _value;

	}

}