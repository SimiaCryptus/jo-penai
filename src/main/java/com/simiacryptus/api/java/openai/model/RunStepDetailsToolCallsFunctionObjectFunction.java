/*
 * OpenAI API
 * The OpenAI REST API. Please see https://platform.openai.com/docs/api-reference for more details.
 *
 * The version of the OpenAPI document: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.simiacryptus.api.java.openai.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * The definition of the function that was called.
 */
@JsonPropertyOrder({
  RunStepDetailsToolCallsFunctionObjectFunction.JSON_PROPERTY_NAME,
  RunStepDetailsToolCallsFunctionObjectFunction.JSON_PROPERTY_ARGUMENTS,
  RunStepDetailsToolCallsFunctionObjectFunction.JSON_PROPERTY_OUTPUT
})
@JsonTypeName("RunStepDetailsToolCallsFunctionObject_function")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:46.732753800-05:00[America/New_York]")
public class RunStepDetailsToolCallsFunctionObjectFunction {
  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_ARGUMENTS = "arguments";
  private String arguments;

  public static final String JSON_PROPERTY_OUTPUT = "output";
  private String output;

  public RunStepDetailsToolCallsFunctionObjectFunction() {
  }

  public RunStepDetailsToolCallsFunctionObjectFunction name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * The name of the function.
   * @return name
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
    return name;
  }


  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setName(String name) {
    this.name = name;
  }


  public RunStepDetailsToolCallsFunctionObjectFunction arguments(String arguments) {
    
    this.arguments = arguments;
    return this;
  }

   /**
   * The arguments passed to the function.
   * @return arguments
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ARGUMENTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getArguments() {
    return arguments;
  }


  @JsonProperty(JSON_PROPERTY_ARGUMENTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setArguments(String arguments) {
    this.arguments = arguments;
  }


  public RunStepDetailsToolCallsFunctionObjectFunction output(String output) {
    
    this.output = output;
    return this;
  }

   /**
   * The output of the function. This will be &#x60;null&#x60; if the outputs have not been [submitted](/docs/api-reference/runs/submitToolOutputs) yet.
   * @return output
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OUTPUT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getOutput() {
    return output;
  }


  @JsonProperty(JSON_PROPERTY_OUTPUT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setOutput(String output) {
    this.output = output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RunStepDetailsToolCallsFunctionObjectFunction runStepDetailsToolCallsFunctionObjectFunction = (RunStepDetailsToolCallsFunctionObjectFunction) o;
    return Objects.equals(this.name, runStepDetailsToolCallsFunctionObjectFunction.name) &&
        Objects.equals(this.arguments, runStepDetailsToolCallsFunctionObjectFunction.arguments) &&
        Objects.equals(this.output, runStepDetailsToolCallsFunctionObjectFunction.output);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, arguments, output);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RunStepDetailsToolCallsFunctionObjectFunction {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    arguments: ").append(toIndentedString(arguments)).append("\n");
    sb.append("    output: ").append(toIndentedString(output)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Convert the instance into URL query string.
   *
   * @return URL query string
   */
  public String toUrlQueryString() {
    return toUrlQueryString(null);
  }

  /**
   * Convert the instance into URL query string.
   *
   * @param prefix prefix of the query string
   * @return URL query string
   */
  public String toUrlQueryString(String prefix) {
    String suffix = "";
    String containerSuffix = "";
    String containerPrefix = "";
    if (prefix == null) {
      // style=form, explode=true, e.g. /pet?name=cat&type=manx
      prefix = "";
    } else {
      // deepObject style e.g. /pet?id[name]=cat&id[type]=manx
      prefix = prefix + "[";
      suffix = "]";
      containerSuffix = "]";
      containerPrefix = "[";
    }

    StringJoiner joiner = new StringJoiner("&");

    // add `name` to the URL query string
    if (getName() != null) {
      try {
        joiner.add(String.format("%sname%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getName()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `arguments` to the URL query string
    if (getArguments() != null) {
      try {
        joiner.add(String.format("%sarguments%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getArguments()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `output` to the URL query string
    if (getOutput() != null) {
      try {
        joiner.add(String.format("%soutput%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getOutput()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

