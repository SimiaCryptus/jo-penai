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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * LoadVaeRequest
 */
@JsonPropertyOrder({
  LoadVaeRequest.JSON_PROPERTY_KEY,
  LoadVaeRequest.JSON_PROPERTY_VAE_ID,
  LoadVaeRequest.JSON_PROPERTY_VAE_TYPE,
  LoadVaeRequest.JSON_PROPERTY_VAE_URL,
  LoadVaeRequest.JSON_PROPERTY_WEBHOOK
})
@JsonTypeName("loadVae_request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class LoadVaeRequest {
  public static final String JSON_PROPERTY_KEY = "key";
  private String key;

  public static final String JSON_PROPERTY_VAE_ID = "vae_id";
  private String vaeId;

  public static final String JSON_PROPERTY_VAE_TYPE = "vae_type";
  private String vaeType;

  public static final String JSON_PROPERTY_VAE_URL = "vae_url";
  private String vaeUrl;

  public static final String JSON_PROPERTY_WEBHOOK = "webhook";
  private String webhook;

  public LoadVaeRequest() {
  }

  public LoadVaeRequest key(String key) {
    
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


  public LoadVaeRequest vaeId(String vaeId) {
    
    this.vaeId = vaeId;
    return this;
  }

   /**
   * Get vaeId
   * @return vaeId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VAE_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getVaeId() {
    return vaeId;
  }


  @JsonProperty(JSON_PROPERTY_VAE_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVaeId(String vaeId) {
    this.vaeId = vaeId;
  }


  public LoadVaeRequest vaeType(String vaeType) {
    
    this.vaeType = vaeType;
    return this;
  }

   /**
   * Get vaeType
   * @return vaeType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VAE_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getVaeType() {
    return vaeType;
  }


  @JsonProperty(JSON_PROPERTY_VAE_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVaeType(String vaeType) {
    this.vaeType = vaeType;
  }


  public LoadVaeRequest vaeUrl(String vaeUrl) {
    
    this.vaeUrl = vaeUrl;
    return this;
  }

   /**
   * Get vaeUrl
   * @return vaeUrl
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VAE_URL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getVaeUrl() {
    return vaeUrl;
  }


  @JsonProperty(JSON_PROPERTY_VAE_URL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVaeUrl(String vaeUrl) {
    this.vaeUrl = vaeUrl;
  }


  public LoadVaeRequest webhook(String webhook) {
    
    this.webhook = webhook;
    return this;
  }

   /**
   * Get webhook
   * @return webhook
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WEBHOOK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getWebhook() {
    return webhook;
  }


  @JsonProperty(JSON_PROPERTY_WEBHOOK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWebhook(String webhook) {
    this.webhook = webhook;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoadVaeRequest loadVaeRequest = (LoadVaeRequest) o;
    return Objects.equals(this.key, loadVaeRequest.key) &&
        Objects.equals(this.vaeId, loadVaeRequest.vaeId) &&
        Objects.equals(this.vaeType, loadVaeRequest.vaeType) &&
        Objects.equals(this.vaeUrl, loadVaeRequest.vaeUrl) &&
        Objects.equals(this.webhook, loadVaeRequest.webhook);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, vaeId, vaeType, vaeUrl, webhook);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoadVaeRequest {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    vaeId: ").append(toIndentedString(vaeId)).append("\n");
    sb.append("    vaeType: ").append(toIndentedString(vaeType)).append("\n");
    sb.append("    vaeUrl: ").append(toIndentedString(vaeUrl)).append("\n");
    sb.append("    webhook: ").append(toIndentedString(webhook)).append("\n");
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

    // add `key` to the URL query string
    if (getKey() != null) {
      try {
        joiner.add(String.format("%skey%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getKey()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `vae_id` to the URL query string
    if (getVaeId() != null) {
      try {
        joiner.add(String.format("%svae_id%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getVaeId()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `vae_type` to the URL query string
    if (getVaeType() != null) {
      try {
        joiner.add(String.format("%svae_type%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getVaeType()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `vae_url` to the URL query string
    if (getVaeUrl() != null) {
      try {
        joiner.add(String.format("%svae_url%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getVaeUrl()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `webhook` to the URL query string
    if (getWebhook() != null) {
      try {
        joiner.add(String.format("%swebhook%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getWebhook()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

