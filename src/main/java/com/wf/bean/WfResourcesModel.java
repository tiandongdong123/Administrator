package com.wf.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class WfResourcesModel implements Serializable {

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "Field")
	private String Field;
	@JsonProperty(value = "Verb")
	private String Verb;
	@JsonProperty(value = "Value")
	private Object Value;
	@JsonProperty(value = "ValueType")
	private String ValueType;
	@JsonProperty(value = "Logic")
	private String Logic;

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public String getVerb() {
		return Verb;
	}

	public void setVerb(String verb) {
		Verb = verb;
	}

	public Object getValue() {
		return Value;
	}

	public void setValue(Object value) {
		Value = value;
	}

	public String getValueType() {
		return ValueType;
	}

	public void setValueType(String valueType) {
		ValueType = valueType;
	}

	public String getLogic() {
		return Logic;
	}

	public void setLogic(String logic) {
		Logic = logic;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{WfResourcesModel:{");
		sb.append("\"Field\":\"" + this.Field + "\"");
		sb.append("\"Verb\":\"" + this.Verb + "\"");
		sb.append("\"Value\":\"" + this.Value + "\"");
		sb.append("\"ValueType\":\"" + this.ValueType + "\"");
		sb.append("\"Logic\":\"" + this.Logic + "\"");
		sb.append("}}");
		return sb.toString();
	}
}
