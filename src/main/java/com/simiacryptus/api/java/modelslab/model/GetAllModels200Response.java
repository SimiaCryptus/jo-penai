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
import com.simiacryptus.api.java.modelslab.model.GetAllModels200ResponseControlnetModelsInner;
import com.simiacryptus.api.java.modelslab.model.GetAllModels200ResponseEmbeddingsModelsInner;
import com.simiacryptus.api.java.modelslab.model.GetAllModels200ResponseLoraModelsInner;
import com.simiacryptus.api.java.modelslab.model.GetAllModels200ResponseModelsInner;
import com.simiacryptus.api.java.modelslab.model.GetAllModels200ResponseVaeModelsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * GetAllModels200Response
 */
@JsonPropertyOrder({
  GetAllModels200Response.JSON_PROPERTY_CONTROLNET_MODELS,
  GetAllModels200Response.JSON_PROPERTY_EMBEDDINGS_MODELS,
  GetAllModels200Response.JSON_PROPERTY_LORA_MODELS,
  GetAllModels200Response.JSON_PROPERTY_MODELS,
  GetAllModels200Response.JSON_PROPERTY_STATUS,
  GetAllModels200Response.JSON_PROPERTY_VAE_MODELS
})
@JsonTypeName("getAllModels_200_response")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class GetAllModels200Response {
  public static final String JSON_PROPERTY_CONTROLNET_MODELS = "controlnet_models";
  private List<GetAllModels200ResponseControlnetModelsInner> controlnetModels;

  public static final String JSON_PROPERTY_EMBEDDINGS_MODELS = "embeddings_models";
  private List<GetAllModels200ResponseEmbeddingsModelsInner> embeddingsModels;

  public static final String JSON_PROPERTY_LORA_MODELS = "lora_models";
  private List<GetAllModels200ResponseLoraModelsInner> loraModels;

  public static final String JSON_PROPERTY_MODELS = "models";
  private List<GetAllModels200ResponseModelsInner> models;

  public static final String JSON_PROPERTY_STATUS = "status";
  private String status;

  public static final String JSON_PROPERTY_VAE_MODELS = "vae_models";
  private List<GetAllModels200ResponseVaeModelsInner> vaeModels;

  public GetAllModels200Response() {
  }

  public GetAllModels200Response controlnetModels(List<GetAllModels200ResponseControlnetModelsInner> controlnetModels) {
    
    this.controlnetModels = controlnetModels;
    return this;
  }

  public GetAllModels200Response addControlnetModelsItem(GetAllModels200ResponseControlnetModelsInner controlnetModelsItem) {
    if (this.controlnetModels == null) {
      this.controlnetModels = new ArrayList<>();
    }
    this.controlnetModels.add(controlnetModelsItem);
    return this;
  }

   /**
   * Get controlnetModels
   * @return controlnetModels
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONTROLNET_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GetAllModels200ResponseControlnetModelsInner> getControlnetModels() {
    return controlnetModels;
  }


  @JsonProperty(JSON_PROPERTY_CONTROLNET_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setControlnetModels(List<GetAllModels200ResponseControlnetModelsInner> controlnetModels) {
    this.controlnetModels = controlnetModels;
  }


  public GetAllModels200Response embeddingsModels(List<GetAllModels200ResponseEmbeddingsModelsInner> embeddingsModels) {
    
    this.embeddingsModels = embeddingsModels;
    return this;
  }

  public GetAllModels200Response addEmbeddingsModelsItem(GetAllModels200ResponseEmbeddingsModelsInner embeddingsModelsItem) {
    if (this.embeddingsModels == null) {
      this.embeddingsModels = new ArrayList<>();
    }
    this.embeddingsModels.add(embeddingsModelsItem);
    return this;
  }

   /**
   * Get embeddingsModels
   * @return embeddingsModels
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EMBEDDINGS_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GetAllModels200ResponseEmbeddingsModelsInner> getEmbeddingsModels() {
    return embeddingsModels;
  }


  @JsonProperty(JSON_PROPERTY_EMBEDDINGS_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEmbeddingsModels(List<GetAllModels200ResponseEmbeddingsModelsInner> embeddingsModels) {
    this.embeddingsModels = embeddingsModels;
  }


  public GetAllModels200Response loraModels(List<GetAllModels200ResponseLoraModelsInner> loraModels) {
    
    this.loraModels = loraModels;
    return this;
  }

  public GetAllModels200Response addLoraModelsItem(GetAllModels200ResponseLoraModelsInner loraModelsItem) {
    if (this.loraModels == null) {
      this.loraModels = new ArrayList<>();
    }
    this.loraModels.add(loraModelsItem);
    return this;
  }

   /**
   * Get loraModels
   * @return loraModels
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LORA_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GetAllModels200ResponseLoraModelsInner> getLoraModels() {
    return loraModels;
  }


  @JsonProperty(JSON_PROPERTY_LORA_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLoraModels(List<GetAllModels200ResponseLoraModelsInner> loraModels) {
    this.loraModels = loraModels;
  }


  public GetAllModels200Response models(List<GetAllModels200ResponseModelsInner> models) {
    
    this.models = models;
    return this;
  }

  public GetAllModels200Response addModelsItem(GetAllModels200ResponseModelsInner modelsItem) {
    if (this.models == null) {
      this.models = new ArrayList<>();
    }
    this.models.add(modelsItem);
    return this;
  }

   /**
   * Get models
   * @return models
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GetAllModels200ResponseModelsInner> getModels() {
    return models;
  }


  @JsonProperty(JSON_PROPERTY_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setModels(List<GetAllModels200ResponseModelsInner> models) {
    this.models = models;
  }


  public GetAllModels200Response status(String status) {
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getStatus() {
    return status;
  }


  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStatus(String status) {
    this.status = status;
  }


  public GetAllModels200Response vaeModels(List<GetAllModels200ResponseVaeModelsInner> vaeModels) {
    
    this.vaeModels = vaeModels;
    return this;
  }

  public GetAllModels200Response addVaeModelsItem(GetAllModels200ResponseVaeModelsInner vaeModelsItem) {
    if (this.vaeModels == null) {
      this.vaeModels = new ArrayList<>();
    }
    this.vaeModels.add(vaeModelsItem);
    return this;
  }

   /**
   * Get vaeModels
   * @return vaeModels
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VAE_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GetAllModels200ResponseVaeModelsInner> getVaeModels() {
    return vaeModels;
  }


  @JsonProperty(JSON_PROPERTY_VAE_MODELS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setVaeModels(List<GetAllModels200ResponseVaeModelsInner> vaeModels) {
    this.vaeModels = vaeModels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAllModels200Response getAllModels200Response = (GetAllModels200Response) o;
    return Objects.equals(this.controlnetModels, getAllModels200Response.controlnetModels) &&
        Objects.equals(this.embeddingsModels, getAllModels200Response.embeddingsModels) &&
        Objects.equals(this.loraModels, getAllModels200Response.loraModels) &&
        Objects.equals(this.models, getAllModels200Response.models) &&
        Objects.equals(this.status, getAllModels200Response.status) &&
        Objects.equals(this.vaeModels, getAllModels200Response.vaeModels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(controlnetModels, embeddingsModels, loraModels, models, status, vaeModels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAllModels200Response {\n");
    sb.append("    controlnetModels: ").append(toIndentedString(controlnetModels)).append("\n");
    sb.append("    embeddingsModels: ").append(toIndentedString(embeddingsModels)).append("\n");
    sb.append("    loraModels: ").append(toIndentedString(loraModels)).append("\n");
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    vaeModels: ").append(toIndentedString(vaeModels)).append("\n");
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

    // add `controlnet_models` to the URL query string
    if (getControlnetModels() != null) {
      for (int i = 0; i < getControlnetModels().size(); i++) {
        if (getControlnetModels().get(i) != null) {
          joiner.add(getControlnetModels().get(i).toUrlQueryString(String.format("%scontrolnet_models%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `embeddings_models` to the URL query string
    if (getEmbeddingsModels() != null) {
      for (int i = 0; i < getEmbeddingsModels().size(); i++) {
        if (getEmbeddingsModels().get(i) != null) {
          joiner.add(getEmbeddingsModels().get(i).toUrlQueryString(String.format("%sembeddings_models%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `lora_models` to the URL query string
    if (getLoraModels() != null) {
      for (int i = 0; i < getLoraModels().size(); i++) {
        if (getLoraModels().get(i) != null) {
          joiner.add(getLoraModels().get(i).toUrlQueryString(String.format("%slora_models%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `models` to the URL query string
    if (getModels() != null) {
      for (int i = 0; i < getModels().size(); i++) {
        if (getModels().get(i) != null) {
          joiner.add(getModels().get(i).toUrlQueryString(String.format("%smodels%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    // add `status` to the URL query string
    if (getStatus() != null) {
      try {
        joiner.add(String.format("%sstatus%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getStatus()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `vae_models` to the URL query string
    if (getVaeModels() != null) {
      for (int i = 0; i < getVaeModels().size(); i++) {
        if (getVaeModels().get(i) != null) {
          joiner.add(getVaeModels().get(i).toUrlQueryString(String.format("%svae_models%s%s", prefix, suffix,
              "".equals(suffix) ? "" : String.format("%s%d%s", containerPrefix, i, containerSuffix))));
        }
      }
    }

    return joiner.toString();
  }

}

