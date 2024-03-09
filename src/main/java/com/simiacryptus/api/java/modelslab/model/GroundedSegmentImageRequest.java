/*
 * ModelsLab AI API's
 * This is ModelsLab Stable Diffusion and Multiple AI APIs, here you can pass details to generate images using API, without needs of GPU locally.  You will need to have _**api key**_ to generate images, if you don't have it, get it from [https://modelslab.com](https://modelslab.com/)  Send Json post request with data and links, don't send files as raw format, send accessible links instead
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.simiacryptus.api.java.modelslab.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * GroundedSegmentImageRequest
 */
@JsonPropertyOrder({
  GroundedSegmentImageRequest.JSON_PROPERTY_IMAGE_PROMPT,
  GroundedSegmentImageRequest.JSON_PROPERTY_KEY,
  GroundedSegmentImageRequest.JSON_PROPERTY_MASK_PROMPT,
  GroundedSegmentImageRequest.JSON_PROPERTY_PROMPT,
  GroundedSegmentImageRequest.JSON_PROPERTY_SAMPLES
})
@JsonTypeName("groundedSegmentImage_request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class GroundedSegmentImageRequest {
  public static final String JSON_PROPERTY_IMAGE_PROMPT = "image_prompt";
  private String imagePrompt;

  public static final String JSON_PROPERTY_KEY = "key";
  private String key;

  public static final String JSON_PROPERTY_MASK_PROMPT = "mask_prompt";
  private String maskPrompt;

  public static final String JSON_PROPERTY_PROMPT = "prompt";
  private String prompt;

  public static final String JSON_PROPERTY_SAMPLES = "samples";
  private BigDecimal samples;

  public GroundedSegmentImageRequest() {
  }

  public GroundedSegmentImageRequest imagePrompt(String imagePrompt) {
    
    this.imagePrompt = imagePrompt;
    return this;
  }

   /**
   * Get imagePrompt
   * @return imagePrompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IMAGE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getImagePrompt() {
    return imagePrompt;
  }


  @JsonProperty(JSON_PROPERTY_IMAGE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setImagePrompt(String imagePrompt) {
    this.imagePrompt = imagePrompt;
  }


  public GroundedSegmentImageRequest key(String key) {
    
    this.key = key;
    return this;
  }

   /**
   * Get key
   * @return key
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getKey() {
    return key;
  }


  @JsonProperty(JSON_PROPERTY_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setKey(String key) {
    this.key = key;
  }


  public GroundedSegmentImageRequest maskPrompt(String maskPrompt) {
    
    this.maskPrompt = maskPrompt;
    return this;
  }

   /**
   * Get maskPrompt
   * @return maskPrompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MASK_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMaskPrompt() {
    return maskPrompt;
  }


  @JsonProperty(JSON_PROPERTY_MASK_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaskPrompt(String maskPrompt) {
    this.maskPrompt = maskPrompt;
  }


  public GroundedSegmentImageRequest prompt(String prompt) {
    
    this.prompt = prompt;
    return this;
  }

   /**
   * Get prompt
   * @return prompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPrompt() {
    return prompt;
  }


  @JsonProperty(JSON_PROPERTY_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }


  public GroundedSegmentImageRequest samples(BigDecimal samples) {
    
    this.samples = samples;
    return this;
  }

   /**
   * Get samples
   * @return samples
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SAMPLES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getSamples() {
    return samples;
  }


  @JsonProperty(JSON_PROPERTY_SAMPLES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSamples(BigDecimal samples) {
    this.samples = samples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroundedSegmentImageRequest groundedSegmentImageRequest = (GroundedSegmentImageRequest) o;
    return Objects.equals(this.imagePrompt, groundedSegmentImageRequest.imagePrompt) &&
        Objects.equals(this.key, groundedSegmentImageRequest.key) &&
        Objects.equals(this.maskPrompt, groundedSegmentImageRequest.maskPrompt) &&
        Objects.equals(this.prompt, groundedSegmentImageRequest.prompt) &&
        Objects.equals(this.samples, groundedSegmentImageRequest.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imagePrompt, key, maskPrompt, prompt, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroundedSegmentImageRequest {\n");
    sb.append("    imagePrompt: ").append(toIndentedString(imagePrompt)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    maskPrompt: ").append(toIndentedString(maskPrompt)).append("\n");
    sb.append("    prompt: ").append(toIndentedString(prompt)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
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

    // add `image_prompt` to the URL query string
    if (getImagePrompt() != null) {
      try {
        joiner.add(String.format("%simage_prompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getImagePrompt()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `key` to the URL query string
    if (getKey() != null) {
      try {
        joiner.add(String.format("%skey%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getKey()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `mask_prompt` to the URL query string
    if (getMaskPrompt() != null) {
      try {
        joiner.add(String.format("%smask_prompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMaskPrompt()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `prompt` to the URL query string
    if (getPrompt() != null) {
      try {
        joiner.add(String.format("%sprompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getPrompt()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `samples` to the URL query string
    if (getSamples() != null) {
      try {
        joiner.add(String.format("%ssamples%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSamples()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

