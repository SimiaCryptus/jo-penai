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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * EmbeddingRequest
 */
@JsonPropertyOrder({
  EmbeddingRequest.JSON_PROPERTY_MODEL,
  EmbeddingRequest.JSON_PROPERTY_INPUT,
  EmbeddingRequest.JSON_PROPERTY_ENCODING_FORMAT
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:59.450958200-05:00[America/New_York]")
public class EmbeddingRequest {
  public static final String JSON_PROPERTY_MODEL = "model";
  private String model;

  public static final String JSON_PROPERTY_INPUT = "input";
  private List<String> input;

  /**
   * The format of the output data. 
   */
  public enum EncodingFormatEnum {
    FLOAT("float");

    private String value;

    EncodingFormatEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EncodingFormatEnum fromValue(String value) {
      for (EncodingFormatEnum b : EncodingFormatEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_ENCODING_FORMAT = "encoding_format";
  private EncodingFormatEnum encodingFormat;

  public EmbeddingRequest() {
  }

  public EmbeddingRequest model(String model) {
    
    this.model = model;
    return this;
  }

   /**
   * The ID of the model to use for this request. 
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


  public EmbeddingRequest input(List<String> input) {
    
    this.input = input;
    return this;
  }

  public EmbeddingRequest addInputItem(String inputItem) {
    if (this.input == null) {
      this.input = new ArrayList<>();
    }
    this.input.add(inputItem);
    return this;
  }

   /**
   * The list of strings to embed. 
   * @return input
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INPUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getInput() {
    return input;
  }


  @JsonProperty(JSON_PROPERTY_INPUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInput(List<String> input) {
    this.input = input;
  }


  public EmbeddingRequest encodingFormat(EncodingFormatEnum encodingFormat) {
    
    this.encodingFormat = encodingFormat;
    return this;
  }

   /**
   * The format of the output data. 
   * @return encodingFormat
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENCODING_FORMAT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EncodingFormatEnum getEncodingFormat() {
    return encodingFormat;
  }


  @JsonProperty(JSON_PROPERTY_ENCODING_FORMAT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEncodingFormat(EncodingFormatEnum encodingFormat) {
    this.encodingFormat = encodingFormat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmbeddingRequest embeddingRequest = (EmbeddingRequest) o;
    return Objects.equals(this.model, embeddingRequest.model) &&
        Objects.equals(this.input, embeddingRequest.input) &&
        Objects.equals(this.encodingFormat, embeddingRequest.encodingFormat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(model, input, encodingFormat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmbeddingRequest {\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    encodingFormat: ").append(toIndentedString(encodingFormat)).append("\n");
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

    // add `model` to the URL query string
    if (getModel() != null) {
      try {
        joiner.add(String.format("%smodel%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getModel()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `input` to the URL query string
    if (getInput() != null) {
      for (int i = 0; i < getInput().size(); i++) {
        try {
          joiner.add(String.format("%sinput%s%s=%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix),
              URLEncoder.encode(String.valueOf(getInput().get(i)), "UTF-8").replaceAll("\\+", "%20")));
        } catch (UnsupportedEncodingException e) {
          // Should never happen, UTF-8 is always supported
          throw new RuntimeException(e);
        }
      }
    }

    // add `encoding_format` to the URL query string
    if (getEncodingFormat() != null) {
      try {
        joiner.add(String.format("%sencoding_format%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getEncodingFormat()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

