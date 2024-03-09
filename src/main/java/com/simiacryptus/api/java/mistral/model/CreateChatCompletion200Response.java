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
import com.simiacryptus.api.java.mistral.model.ChatCompletionResponse;
import com.simiacryptus.api.java.mistral.model.ChatCompletionResponseFunctionCall;
import com.simiacryptus.api.java.mistral.model.ChatCompletionResponseFunctionCallChoicesInner;
import com.simiacryptus.api.java.mistral.model.ChatCompletionResponseFunctionCallUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * CreateChatCompletion200Response
 */
@JsonPropertyOrder({
  CreateChatCompletion200Response.JSON_PROPERTY_ID,
  CreateChatCompletion200Response.JSON_PROPERTY_OBJECT,
  CreateChatCompletion200Response.JSON_PROPERTY_CREATED,
  CreateChatCompletion200Response.JSON_PROPERTY_MODEL,
  CreateChatCompletion200Response.JSON_PROPERTY_CHOICES,
  CreateChatCompletion200Response.JSON_PROPERTY_USAGE
})
@JsonTypeName("createChatCompletion_200_response")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:59.450958200-05:00[America/New_York]")
public class CreateChatCompletion200Response {
  public static final String JSON_PROPERTY_ID = "id";
  private String id;

  public static final String JSON_PROPERTY_OBJECT = "object";
  private String _object;

  public static final String JSON_PROPERTY_CREATED = "created";
  private Integer created;

  public static final String JSON_PROPERTY_MODEL = "model";
  private String model;

  public static final String JSON_PROPERTY_CHOICES = "choices";
  private List<ChatCompletionResponseFunctionCallChoicesInner> choices;

  public static final String JSON_PROPERTY_USAGE = "usage";
  private ChatCompletionResponseFunctionCallUsage usage;

  public CreateChatCompletion200Response() {
  }

  public CreateChatCompletion200Response id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setId(String id) {
    this.id = id;
  }


  public CreateChatCompletion200Response _object(String _object) {
    
    this._object = _object;
    return this;
  }

   /**
   * Get _object
   * @return _object
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OBJECT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getObject() {
    return _object;
  }


  @JsonProperty(JSON_PROPERTY_OBJECT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setObject(String _object) {
    this._object = _object;
  }


  public CreateChatCompletion200Response created(Integer created) {
    
    this.created = created;
    return this;
  }

   /**
   * Get created
   * @return created
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CREATED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getCreated() {
    return created;
  }


  @JsonProperty(JSON_PROPERTY_CREATED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCreated(Integer created) {
    this.created = created;
  }


  public CreateChatCompletion200Response model(String model) {
    
    this.model = model;
    return this;
  }

   /**
   * Get model
   * @return model
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MODEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getModel() {
    return model;
  }


  @JsonProperty(JSON_PROPERTY_MODEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setModel(String model) {
    this.model = model;
  }


  public CreateChatCompletion200Response choices(List<ChatCompletionResponseFunctionCallChoicesInner> choices) {
    
    this.choices = choices;
    return this;
  }

  public CreateChatCompletion200Response addChoicesItem(ChatCompletionResponseFunctionCallChoicesInner choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

   /**
   * Get choices
   * @return choices
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CHOICES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<ChatCompletionResponseFunctionCallChoicesInner> getChoices() {
    return choices;
  }


  @JsonProperty(JSON_PROPERTY_CHOICES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setChoices(List<ChatCompletionResponseFunctionCallChoicesInner> choices) {
    this.choices = choices;
  }


  public CreateChatCompletion200Response usage(ChatCompletionResponseFunctionCallUsage usage) {
    
    this.usage = usage;
    return this;
  }

   /**
   * Get usage
   * @return usage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ChatCompletionResponseFunctionCallUsage getUsage() {
    return usage;
  }


  @JsonProperty(JSON_PROPERTY_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUsage(ChatCompletionResponseFunctionCallUsage usage) {
    this.usage = usage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateChatCompletion200Response createChatCompletion200Response = (CreateChatCompletion200Response) o;
    return Objects.equals(this.id, createChatCompletion200Response.id) &&
        Objects.equals(this._object, createChatCompletion200Response._object) &&
        Objects.equals(this.created, createChatCompletion200Response.created) &&
        Objects.equals(this.model, createChatCompletion200Response.model) &&
        Objects.equals(this.choices, createChatCompletion200Response.choices) &&
        Objects.equals(this.usage, createChatCompletion200Response.usage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, _object, created, model, choices, usage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateChatCompletion200Response {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    _object: ").append(toIndentedString(_object)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
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

    // add `id` to the URL query string
    if (getId() != null) {
      try {
        joiner.add(String.format("%sid%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getId()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `object` to the URL query string
    if (getObject() != null) {
      try {
        joiner.add(String.format("%sobject%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getObject()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `created` to the URL query string
    if (getCreated() != null) {
      try {
        joiner.add(String.format("%screated%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getCreated()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `model` to the URL query string
    if (getModel() != null) {
      try {
        joiner.add(String.format("%smodel%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getModel()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `choices` to the URL query string
    if (getChoices() != null) {
      for (int i = 0; i < getChoices().size(); i++) {
        if (getChoices().get(i) != null) {
          joiner.add(getChoices().get(i).toUrlQueryString(String.format("%schoices%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `usage` to the URL query string
    if (getUsage() != null) {
      joiner.add(getUsage().toUrlQueryString(prefix + "usage" + suffix));
    }

    return joiner.toString();
  }

}

