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

package com.liferay.headless.batch.engine.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ivica Cardic
 * @generated
 */
@Generated("")
@GraphQLName("ImportTask")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ImportTask")
public class ImportTask implements Serializable {

	public static ImportTask toDTO(String json) {
		return ObjectMapperUtil.readValue(ImportTask.class, json);
	}

	public static ImportTask unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(ImportTask.class, json);
	}

	@Schema(
		description = "The item class name for which data will be processed in batch."
	)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@JsonIgnore
	public void setClassName(
		UnsafeSupplier<String, Exception> classNameUnsafeSupplier) {

		try {
			className = classNameUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "The item class name for which data will be processed in batch."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String className;

	@Schema(description = "The file content type.")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@JsonIgnore
	public void setContentType(
		UnsafeSupplier<String, Exception> contentTypeUnsafeSupplier) {

		try {
			contentType = contentTypeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The file content type.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String contentType;

	@Schema(description = "The end time of import task operation.")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@JsonIgnore
	public void setEndTime(
		UnsafeSupplier<Date, Exception> endTimeUnsafeSupplier) {

		try {
			endTime = endTimeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The end time of import task operation.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Date endTime;

	@Schema(
		description = "The error message in case of import task's failed execution."
	)
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@JsonIgnore
	public void setErrorMessage(
		UnsafeSupplier<String, Exception> errorMessageUnsafeSupplier) {

		try {
			errorMessage = errorMessageUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "The error message in case of import task's failed execution."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String errorMessage;

	@Schema(description = "The status of import task's execution.")
	@Valid
	public ExecuteStatus getExecuteStatus() {
		return executeStatus;
	}

	@JsonIgnore
	public String getExecuteStatusAsString() {
		if (executeStatus == null) {
			return null;
		}

		return executeStatus.toString();
	}

	public void setExecuteStatus(ExecuteStatus executeStatus) {
		this.executeStatus = executeStatus;
	}

	@JsonIgnore
	public void setExecuteStatus(
		UnsafeSupplier<ExecuteStatus, Exception> executeStatusUnsafeSupplier) {

		try {
			executeStatus = executeStatusUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The status of import task's execution.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected ExecuteStatus executeStatus;

	@Schema(description = "The optional external key of this account.")
	public String getExternalReferenceCode() {
		return externalReferenceCode;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		this.externalReferenceCode = externalReferenceCode;
	}

	@JsonIgnore
	public void setExternalReferenceCode(
		UnsafeSupplier<String, Exception> externalReferenceCodeUnsafeSupplier) {

		try {
			externalReferenceCode = externalReferenceCodeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The optional external key of this account.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String externalReferenceCode;

	@Schema
	@Valid
	public FailedItem[] getFailedItems() {
		return failedItems;
	}

	public void setFailedItems(FailedItem[] failedItems) {
		this.failedItems = failedItems;
	}

	@JsonIgnore
	public void setFailedItems(
		UnsafeSupplier<FailedItem[], Exception> failedItemsUnsafeSupplier) {

		try {
			failedItems = failedItemsUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected FailedItem[] failedItems;

	@DecimalMin("0")
	@Schema(description = "The task's ID.")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The task's ID.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long id;

	@Schema(
		description = "Defines if import task will fail when error occurs or continue importing rest of the items."
	)
	@Valid
	public ImportStrategy getImportStrategy() {
		return importStrategy;
	}

	@JsonIgnore
	public String getImportStrategyAsString() {
		if (importStrategy == null) {
			return null;
		}

		return importStrategy.toString();
	}

	public void setImportStrategy(ImportStrategy importStrategy) {
		this.importStrategy = importStrategy;
	}

	@JsonIgnore
	public void setImportStrategy(
		UnsafeSupplier<ImportStrategy, Exception>
			importStrategyUnsafeSupplier) {

		try {
			importStrategy = importStrategyUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "Defines if import task will fail when error occurs or continue importing rest of the items."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected ImportStrategy importStrategy;

	@Schema(description = "The operation of import task.")
	@Valid
	public Operation getOperation() {
		return operation;
	}

	@JsonIgnore
	public String getOperationAsString() {
		if (operation == null) {
			return null;
		}

		return operation.toString();
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	@JsonIgnore
	public void setOperation(
		UnsafeSupplier<Operation, Exception> operationUnsafeSupplier) {

		try {
			operation = operationUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The operation of import task.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Operation operation;

	@DecimalMin("0")
	@Schema(description = "Number of items processed by import task opeartion.")
	public Integer getProcessedItemsCount() {
		return processedItemsCount;
	}

	public void setProcessedItemsCount(Integer processedItemsCount) {
		this.processedItemsCount = processedItemsCount;
	}

	@JsonIgnore
	public void setProcessedItemsCount(
		UnsafeSupplier<Integer, Exception> processedItemsCountUnsafeSupplier) {

		try {
			processedItemsCount = processedItemsCountUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "Number of items processed by import task opeartion."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer processedItemsCount;

	@Schema(description = "The start time of import task operation.")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonIgnore
	public void setStartTime(
		UnsafeSupplier<Date, Exception> startTimeUnsafeSupplier) {

		try {
			startTime = startTimeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "The start time of import task operation.")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Date startTime;

	@DecimalMin("0")
	@Schema(
		description = "Total number of items that will be processed by import task operation."
	)
	public Integer getTotalItemsCount() {
		return totalItemsCount;
	}

	public void setTotalItemsCount(Integer totalItemsCount) {
		this.totalItemsCount = totalItemsCount;
	}

	@JsonIgnore
	public void setTotalItemsCount(
		UnsafeSupplier<Integer, Exception> totalItemsCountUnsafeSupplier) {

		try {
			totalItemsCount = totalItemsCountUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "Total number of items that will be processed by import task operation."
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer totalItemsCount;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ImportTask)) {
			return false;
		}

		ImportTask importTask = (ImportTask)object;

		return Objects.equals(toString(), importTask.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		if (className != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"className\": ");

			sb.append("\"");

			sb.append(_escape(className));

			sb.append("\"");
		}

		if (contentType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"contentType\": ");

			sb.append("\"");

			sb.append(_escape(contentType));

			sb.append("\"");
		}

		if (endTime != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"endTime\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(endTime));

			sb.append("\"");
		}

		if (errorMessage != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"errorMessage\": ");

			sb.append("\"");

			sb.append(_escape(errorMessage));

			sb.append("\"");
		}

		if (executeStatus != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"executeStatus\": ");

			sb.append("\"");

			sb.append(executeStatus);

			sb.append("\"");
		}

		if (externalReferenceCode != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"externalReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(externalReferenceCode));

			sb.append("\"");
		}

		if (failedItems != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"failedItems\": ");

			sb.append("[");

			for (int i = 0; i < failedItems.length; i++) {
				sb.append(String.valueOf(failedItems[i]));

				if ((i + 1) < failedItems.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (id != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(id);
		}

		if (importStrategy != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"importStrategy\": ");

			sb.append("\"");

			sb.append(importStrategy);

			sb.append("\"");
		}

		if (operation != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"operation\": ");

			sb.append("\"");

			sb.append(operation);

			sb.append("\"");
		}

		if (processedItemsCount != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"processedItemsCount\": ");

			sb.append(processedItemsCount);
		}

		if (startTime != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"startTime\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(startTime));

			sb.append("\"");
		}

		if (totalItemsCount != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalItemsCount\": ");

			sb.append(totalItemsCount);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.headless.batch.engine.dto.v1_0.ImportTask",
		name = "x-class-name"
	)
	public String xClassName;

	@GraphQLName("ExecuteStatus")
	public static enum ExecuteStatus {

		COMPLETED("COMPLETED"), FAILED("FAILED"), INITIAL("INITIAL"),
		STARTED("STARTED");

		@JsonCreator
		public static ExecuteStatus create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (ExecuteStatus executeStatus : values()) {
				if (Objects.equals(executeStatus.getValue(), value)) {
					return executeStatus;
				}
			}

			throw new IllegalArgumentException("Invalid enum value: " + value);
		}

		@JsonValue
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

	@GraphQLName("ImportStrategy")
	public static enum ImportStrategy {

		ON_ERROR_CONTINUE("ON_ERROR_CONTINUE"), ON_ERROR_FAIL("ON_ERROR_FAIL");

		@JsonCreator
		public static ImportStrategy create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (ImportStrategy importStrategy : values()) {
				if (Objects.equals(importStrategy.getValue(), value)) {
					return importStrategy;
				}
			}

			throw new IllegalArgumentException("Invalid enum value: " + value);
		}

		@JsonValue
		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private ImportStrategy(String value) {
			_value = value;
		}

		private final String _value;

	}

	@GraphQLName("Operation")
	public static enum Operation {

		CREATE("CREATE"), DELETE("DELETE"), UPDATE("UPDATE");

		@JsonCreator
		public static Operation create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (Operation operation : values()) {
				if (Objects.equals(operation.getValue(), value)) {
					return operation;
				}
			}

			throw new IllegalArgumentException("Invalid enum value: " + value);
		}

		@JsonValue
		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private Operation(String value) {
			_value = value;
		}

		private final String _value;

	}

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

}