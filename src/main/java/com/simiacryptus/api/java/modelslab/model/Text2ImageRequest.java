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
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * Text2ImageRequest
 */
@JsonPropertyOrder({
  Text2ImageRequest.JSON_PROPERTY_GUIDANCE_SCALE,
  Text2ImageRequest.JSON_PROPERTY_HEIGHT,
  Text2ImageRequest.JSON_PROPERTY_KEY,
  Text2ImageRequest.JSON_PROPERTY_NEGATIVE_PROMPT,
  Text2ImageRequest.JSON_PROPERTY_NUM_INFERENCE_STEPS,
  Text2ImageRequest.JSON_PROPERTY_PROMPT,
  Text2ImageRequest.JSON_PROPERTY_SAMPLES,
  Text2ImageRequest.JSON_PROPERTY_SEED,
  Text2ImageRequest.JSON_PROPERTY_TRACK_ID,
  Text2ImageRequest.JSON_PROPERTY_WEBHOOK,
  Text2ImageRequest.JSON_PROPERTY_WIDTH
})
@JsonTypeName("text2Image_request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class Text2ImageRequest {
  public static final String JSON_PROPERTY_GUIDANCE_SCALE = "guidance_scale";
  private BigDecimal guidanceScale;

  public static final String JSON_PROPERTY_HEIGHT = "height";
  private String height;

  public static final String JSON_PROPERTY_KEY = "key";
  private String key;

  public static final String JSON_PROPERTY_NEGATIVE_PROMPT = "negative_prompt";
  private String negativePrompt;

  public static final String JSON_PROPERTY_NUM_INFERENCE_STEPS = "num_inference_steps";
  private String numInferenceSteps;

  public static final String JSON_PROPERTY_PROMPT = "prompt";
  private String prompt;

  public static final String JSON_PROPERTY_SAMPLES = "samples";
  private String samples;

  public static final String JSON_PROPERTY_SEED = "seed";
  private JsonNullable<Object> seed = JsonNullable.<Object>of(null);

  public static final String JSON_PROPERTY_TRACK_ID = "track_id";
  private JsonNullable<Object> trackId = JsonNullable.<Object>of(null);

  public static final String JSON_PROPERTY_WEBHOOK = "webhook";
  private JsonNullable<Object> webhook = JsonNullable.<Object>of(null);

  public static final String JSON_PROPERTY_WIDTH = "width";
  private String width;

  public Text2ImageRequest() {
  }

  public Text2ImageRequest guidanceScale(BigDecimal guidanceScale) {
    
    this.guidanceScale = guidanceScale;
    return this;
  }

   /**
   * Get guidanceScale
   * @return guidanceScale
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUIDANCE_SCALE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getGuidanceScale() {
    return guidanceScale;
  }


  @JsonProperty(JSON_PROPERTY_GUIDANCE_SCALE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuidanceScale(BigDecimal guidanceScale) {
    this.guidanceScale = guidanceScale;
  }


  public Text2ImageRequest height(String height) {
    
    this.height = height;
    return this;
  }

   /**
   * Get height
   * @return height
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_HEIGHT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getHeight() {
    return height;
  }


  @JsonProperty(JSON_PROPERTY_HEIGHT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setHeight(String height) {
    this.height = height;
  }


  public Text2ImageRequest key(String key) {
    
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


  public Text2ImageRequest negativePrompt(String negativePrompt) {
    
    this.negativePrompt = negativePrompt;
    return this;
  }

   /**
   * Get negativePrompt
   * @return negativePrompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NEGATIVE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getNegativePrompt() {
    return negativePrompt;
  }


  @JsonProperty(JSON_PROPERTY_NEGATIVE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNegativePrompt(String negativePrompt) {
    this.negativePrompt = negativePrompt;
  }


  public Text2ImageRequest numInferenceSteps(String numInferenceSteps) {
    
    this.numInferenceSteps = numInferenceSteps;
    return this;
  }

   /**
   * Get numInferenceSteps
   * @return numInferenceSteps
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NUM_INFERENCE_STEPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getNumInferenceSteps() {
    return numInferenceSteps;
  }


  @JsonProperty(JSON_PROPERTY_NUM_INFERENCE_STEPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNumInferenceSteps(String numInferenceSteps) {
    this.numInferenceSteps = numInferenceSteps;
  }


  public Text2ImageRequest prompt(String prompt) {
    
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


  public Text2ImageRequest samples(String samples) {
    
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

  public String getSamples() {
    return samples;
  }


  @JsonProperty(JSON_PROPERTY_SAMPLES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSamples(String samples) {
    this.samples = samples;
  }


  public Text2ImageRequest seed(Object seed) {
    this.seed = JsonNullable.<Object>of(seed);
    
    return this;
  }

   /**
   * Get seed
   * @return seed
  **/
  @javax.annotation.Nullable
  @JsonIgnore

  public Object getSeed() {
        return seed.orElse(null);
  }

  @JsonProperty(JSON_PROPERTY_SEED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public JsonNullable<Object> getSeed_JsonNullable() {
    return seed;
  }
  
  @JsonProperty(JSON_PROPERTY_SEED)
  public void setSeed_JsonNullable(JsonNullable<Object> seed) {
    this.seed = seed;
  }

  public void setSeed(Object seed) {
    this.seed = JsonNullable.<Object>of(seed);
  }


  public Text2ImageRequest trackId(Object trackId) {
    this.trackId = JsonNullable.<Object>of(trackId);
    
    return this;
  }

   /**
   * Get trackId
   * @return trackId
  **/
  @javax.annotation.Nullable
  @JsonIgnore

  public Object getTrackId() {
        return trackId.orElse(null);
  }

  @JsonProperty(JSON_PROPERTY_TRACK_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public JsonNullable<Object> getTrackId_JsonNullable() {
    return trackId;
  }
  
  @JsonProperty(JSON_PROPERTY_TRACK_ID)
  public void setTrackId_JsonNullable(JsonNullable<Object> trackId) {
    this.trackId = trackId;
  }

  public void setTrackId(Object trackId) {
    this.trackId = JsonNullable.<Object>of(trackId);
  }


  public Text2ImageRequest webhook(Object webhook) {
    this.webhook = JsonNullable.<Object>of(webhook);
    
    return this;
  }

   /**
   * Get webhook
   * @return webhook
  **/
  @javax.annotation.Nullable
  @JsonIgnore

  public Object getWebhook() {
        return webhook.orElse(null);
  }

  @JsonProperty(JSON_PROPERTY_WEBHOOK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public JsonNullable<Object> getWebhook_JsonNullable() {
    return webhook;
  }
  
  @JsonProperty(JSON_PROPERTY_WEBHOOK)
  public void setWebhook_JsonNullable(JsonNullable<Object> webhook) {
    this.webhook = webhook;
  }

  public void setWebhook(Object webhook) {
    this.webhook = JsonNullable.<Object>of(webhook);
  }


  public Text2ImageRequest width(String width) {
    
    this.width = width;
    return this;
  }

   /**
   * Get width
   * @return width
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WIDTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getWidth() {
    return width;
  }


  @JsonProperty(JSON_PROPERTY_WIDTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWidth(String width) {
    this.width = width;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Text2ImageRequest text2ImageRequest = (Text2ImageRequest) o;
    return Objects.equals(this.guidanceScale, text2ImageRequest.guidanceScale) &&
        Objects.equals(this.height, text2ImageRequest.height) &&
        Objects.equals(this.key, text2ImageRequest.key) &&
        Objects.equals(this.negativePrompt, text2ImageRequest.negativePrompt) &&
        Objects.equals(this.numInferenceSteps, text2ImageRequest.numInferenceSteps) &&
        Objects.equals(this.prompt, text2ImageRequest.prompt) &&
        Objects.equals(this.samples, text2ImageRequest.samples) &&
        equalsNullable(this.seed, text2ImageRequest.seed) &&
        equalsNullable(this.trackId, text2ImageRequest.trackId) &&
        equalsNullable(this.webhook, text2ImageRequest.webhook) &&
        Objects.equals(this.width, text2ImageRequest.width);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(guidanceScale, height, key, negativePrompt, numInferenceSteps, prompt, samples, hashCodeNullable(seed), hashCodeNullable(trackId), hashCodeNullable(webhook), width);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Text2ImageRequest {\n");
    sb.append("    guidanceScale: ").append(toIndentedString(guidanceScale)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    negativePrompt: ").append(toIndentedString(negativePrompt)).append("\n");
    sb.append("    numInferenceSteps: ").append(toIndentedString(numInferenceSteps)).append("\n");
    sb.append("    prompt: ").append(toIndentedString(prompt)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
    sb.append("    trackId: ").append(toIndentedString(trackId)).append("\n");
    sb.append("    webhook: ").append(toIndentedString(webhook)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
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

    // add `guidance_scale` to the URL query string
    if (getGuidanceScale() != null) {
      try {
        joiner.add(String.format("%sguidance_scale%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getGuidanceScale()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `height` to the URL query string
    if (getHeight() != null) {
      try {
        joiner.add(String.format("%sheight%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getHeight()), "UTF-8").replaceAll("\\+", "%20")));
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

    // add `negative_prompt` to the URL query string
    if (getNegativePrompt() != null) {
      try {
        joiner.add(String.format("%snegative_prompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getNegativePrompt()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `num_inference_steps` to the URL query string
    if (getNumInferenceSteps() != null) {
      try {
        joiner.add(String.format("%snum_inference_steps%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getNumInferenceSteps()), "UTF-8").replaceAll("\\+", "%20")));
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

    // add `seed` to the URL query string
    if (getSeed() != null) {
      try {
        joiner.add(String.format("%sseed%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSeed()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `track_id` to the URL query string
    if (getTrackId() != null) {
      try {
        joiner.add(String.format("%strack_id%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getTrackId()), "UTF-8").replaceAll("\\+", "%20")));
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

    // add `width` to the URL query string
    if (getWidth() != null) {
      try {
        joiner.add(String.format("%swidth%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getWidth()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

