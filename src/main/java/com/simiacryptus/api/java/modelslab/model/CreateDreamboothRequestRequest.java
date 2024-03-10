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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * CreateDreamboothRequestRequest
 */
@JsonPropertyOrder({
  CreateDreamboothRequestRequest.JSON_PROPERTY_CLASS_PROMPT,
  CreateDreamboothRequestRequest.JSON_PROPERTY_IMAGES,
  CreateDreamboothRequestRequest.JSON_PROPERTY_INSTANCE_PROMPT,
  CreateDreamboothRequestRequest.JSON_PROPERTY_KEY,
  CreateDreamboothRequestRequest.JSON_PROPERTY_MAX_TRAIN_STEPS,
  CreateDreamboothRequestRequest.JSON_PROPERTY_SEED
})
@JsonTypeName("createDreamboothRequest_request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class CreateDreamboothRequestRequest {
  public static final String JSON_PROPERTY_CLASS_PROMPT = "class_prompt";
  private String classPrompt;

  public static final String JSON_PROPERTY_IMAGES = "images";
  private List<String> images;

  public static final String JSON_PROPERTY_INSTANCE_PROMPT = "instance_prompt";
  private String instancePrompt;

  public static final String JSON_PROPERTY_KEY = "key";
  private String key;

  public static final String JSON_PROPERTY_MAX_TRAIN_STEPS = "max_train_steps";
  private String maxTrainSteps;

  public static final String JSON_PROPERTY_SEED = "seed";
  private String seed;

  public CreateDreamboothRequestRequest() {
  }

  public CreateDreamboothRequestRequest classPrompt(String classPrompt) {
    
    this.classPrompt = classPrompt;
    return this;
  }

   /**
   * Get classPrompt
   * @return classPrompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLASS_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClassPrompt() {
    return classPrompt;
  }


  @JsonProperty(JSON_PROPERTY_CLASS_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClassPrompt(String classPrompt) {
    this.classPrompt = classPrompt;
  }


  public CreateDreamboothRequestRequest images(List<String> images) {
    
    this.images = images;
    return this;
  }

  public CreateDreamboothRequestRequest addImagesItem(String imagesItem) {
    if (this.images == null) {
      this.images = new ArrayList<>();
    }
    this.images.add(imagesItem);
    return this;
  }

   /**
   * Get images
   * @return images
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IMAGES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getImages() {
    return images;
  }


  @JsonProperty(JSON_PROPERTY_IMAGES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setImages(List<String> images) {
    this.images = images;
  }


  public CreateDreamboothRequestRequest instancePrompt(String instancePrompt) {
    
    this.instancePrompt = instancePrompt;
    return this;
  }

   /**
   * Get instancePrompt
   * @return instancePrompt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INSTANCE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getInstancePrompt() {
    return instancePrompt;
  }


  @JsonProperty(JSON_PROPERTY_INSTANCE_PROMPT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInstancePrompt(String instancePrompt) {
    this.instancePrompt = instancePrompt;
  }


  public CreateDreamboothRequestRequest key(String key) {
    
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


  public CreateDreamboothRequestRequest maxTrainSteps(String maxTrainSteps) {
    
    this.maxTrainSteps = maxTrainSteps;
    return this;
  }

   /**
   * Get maxTrainSteps
   * @return maxTrainSteps
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_TRAIN_STEPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMaxTrainSteps() {
    return maxTrainSteps;
  }


  @JsonProperty(JSON_PROPERTY_MAX_TRAIN_STEPS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxTrainSteps(String maxTrainSteps) {
    this.maxTrainSteps = maxTrainSteps;
  }


  public CreateDreamboothRequestRequest seed(String seed) {
    
    this.seed = seed;
    return this;
  }

   /**
   * Get seed
   * @return seed
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSeed() {
    return seed;
  }


  @JsonProperty(JSON_PROPERTY_SEED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSeed(String seed) {
    this.seed = seed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateDreamboothRequestRequest createDreamboothRequestRequest = (CreateDreamboothRequestRequest) o;
    return Objects.equals(this.classPrompt, createDreamboothRequestRequest.classPrompt) &&
        Objects.equals(this.images, createDreamboothRequestRequest.images) &&
        Objects.equals(this.instancePrompt, createDreamboothRequestRequest.instancePrompt) &&
        Objects.equals(this.key, createDreamboothRequestRequest.key) &&
        Objects.equals(this.maxTrainSteps, createDreamboothRequestRequest.maxTrainSteps) &&
        Objects.equals(this.seed, createDreamboothRequestRequest.seed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classPrompt, images, instancePrompt, key, maxTrainSteps, seed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateDreamboothRequestRequest {\n");
    sb.append("    classPrompt: ").append(toIndentedString(classPrompt)).append("\n");
    sb.append("    images: ").append(toIndentedString(images)).append("\n");
    sb.append("    instancePrompt: ").append(toIndentedString(instancePrompt)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    maxTrainSteps: ").append(toIndentedString(maxTrainSteps)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
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

    // add `class_prompt` to the URL query string
    if (getClassPrompt() != null) {
      try {
        joiner.add(String.format("%sclass_prompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getClassPrompt()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `images` to the URL query string
    if (getImages() != null) {
      for (int i = 0; i < getImages().size(); i++) {
        try {
          joiner.add(String.format("%simages%s%s=%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix),
              URLEncoder.encode(String.valueOf(getImages().get(i)), "UTF-8").replaceAll("\\+", "%20")));
        } catch (UnsupportedEncodingException e) {
          // Should never happen, UTF-8 is always supported
          throw new RuntimeException(e);
        }
      }
    }

    // add `instance_prompt` to the URL query string
    if (getInstancePrompt() != null) {
      try {
        joiner.add(String.format("%sinstance_prompt%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getInstancePrompt()), "UTF-8").replaceAll("\\+", "%20")));
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

    // add `max_train_steps` to the URL query string
    if (getMaxTrainSteps() != null) {
      try {
        joiner.add(String.format("%smax_train_steps%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getMaxTrainSteps()), "UTF-8").replaceAll("\\+", "%20")));
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

    return joiner.toString();
  }

}

