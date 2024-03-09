/*
 * Mistral AI API
 * Chat Completion and Embeddings APIs
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.simiacryptus.api.java.mistral.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.simiacryptus.api.java.mistral.model.ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInnerFunction;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner
 */
@JsonPropertyOrder({
  ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner.JSON_PROPERTY_FUNCTION
})
@JsonTypeName("ChatCompletionResponseFunctionCall_choices_inner_message_tool_calls_inner")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:59.450958200-05:00[America/New_York]")
public class ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner {
  public static final String JSON_PROPERTY_FUNCTION = "function";
  private ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInnerFunction function;

  public ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner() {
  }

  public ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner function(ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInnerFunction function) {
    
    this.function = function;
    return this;
  }

   /**
   * Get function
   * @return function
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FUNCTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInnerFunction getFunction() {
    return function;
  }


  @JsonProperty(JSON_PROPERTY_FUNCTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFunction(ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInnerFunction function) {
    this.function = function;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner chatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner = (ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner) o;
    return Objects.equals(this.function, chatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner.function);
  }

  @Override
  public int hashCode() {
    return Objects.hash(function);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatCompletionResponseFunctionCallChoicesInnerMessageToolCallsInner {\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
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

    // add `function` to the URL query string
    if (getFunction() != null) {
      joiner.add(getFunction().toUrlQueryString(prefix + "function" + suffix));
    }

    return joiner.toString();
  }

}

