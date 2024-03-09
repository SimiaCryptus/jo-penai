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
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

/**
 * A list of the categories along with their scores as predicted by model.
 */
@JsonPropertyOrder({
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_HATE,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_HATE_THREATENING,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_HARASSMENT,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_HARASSMENT_THREATENING,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_SELF_HARM,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_SELF_HARM_INTENT,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_SELF_HARM_INSTRUCTIONS,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_SEXUAL,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_SEXUAL_MINORS,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_VIOLENCE,
  CreateModerationResponseResultsInnerCategoryScores.JSON_PROPERTY_VIOLENCE_GRAPHIC
})
@JsonTypeName("CreateModerationResponse_results_inner_category_scores")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:46.732753800-05:00[America/New_York]")
public class CreateModerationResponseResultsInnerCategoryScores {
  public static final String JSON_PROPERTY_HATE = "hate";
  private BigDecimal hate;

  public static final String JSON_PROPERTY_HATE_THREATENING = "hate/threatening";
  private BigDecimal hateThreatening;

  public static final String JSON_PROPERTY_HARASSMENT = "harassment";
  private BigDecimal harassment;

  public static final String JSON_PROPERTY_HARASSMENT_THREATENING = "harassment/threatening";
  private BigDecimal harassmentThreatening;

  public static final String JSON_PROPERTY_SELF_HARM = "self-harm";
  private BigDecimal selfHarm;

  public static final String JSON_PROPERTY_SELF_HARM_INTENT = "self-harm/intent";
  private BigDecimal selfHarmIntent;

  public static final String JSON_PROPERTY_SELF_HARM_INSTRUCTIONS = "self-harm/instructions";
  private BigDecimal selfHarmInstructions;

  public static final String JSON_PROPERTY_SEXUAL = "sexual";
  private BigDecimal sexual;

  public static final String JSON_PROPERTY_SEXUAL_MINORS = "sexual/minors";
  private BigDecimal sexualMinors;

  public static final String JSON_PROPERTY_VIOLENCE = "violence";
  private BigDecimal violence;

  public static final String JSON_PROPERTY_VIOLENCE_GRAPHIC = "violence/graphic";
  private BigDecimal violenceGraphic;

  public CreateModerationResponseResultsInnerCategoryScores() {
  }

  public CreateModerationResponseResultsInnerCategoryScores hate(BigDecimal hate) {
    
    this.hate = hate;
    return this;
  }

   /**
   * The score for the category &#39;hate&#39;.
   * @return hate
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_HATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getHate() {
    return hate;
  }


  @JsonProperty(JSON_PROPERTY_HATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHate(BigDecimal hate) {
    this.hate = hate;
  }


  public CreateModerationResponseResultsInnerCategoryScores hateThreatening(BigDecimal hateThreatening) {
    
    this.hateThreatening = hateThreatening;
    return this;
  }

   /**
   * The score for the category &#39;hate/threatening&#39;.
   * @return hateThreatening
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_HATE_THREATENING)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getHateThreatening() {
    return hateThreatening;
  }


  @JsonProperty(JSON_PROPERTY_HATE_THREATENING)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHateThreatening(BigDecimal hateThreatening) {
    this.hateThreatening = hateThreatening;
  }


  public CreateModerationResponseResultsInnerCategoryScores harassment(BigDecimal harassment) {
    
    this.harassment = harassment;
    return this;
  }

   /**
   * The score for the category &#39;harassment&#39;.
   * @return harassment
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_HARASSMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getHarassment() {
    return harassment;
  }


  @JsonProperty(JSON_PROPERTY_HARASSMENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHarassment(BigDecimal harassment) {
    this.harassment = harassment;
  }


  public CreateModerationResponseResultsInnerCategoryScores harassmentThreatening(BigDecimal harassmentThreatening) {
    
    this.harassmentThreatening = harassmentThreatening;
    return this;
  }

   /**
   * The score for the category &#39;harassment/threatening&#39;.
   * @return harassmentThreatening
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_HARASSMENT_THREATENING)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getHarassmentThreatening() {
    return harassmentThreatening;
  }


  @JsonProperty(JSON_PROPERTY_HARASSMENT_THREATENING)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHarassmentThreatening(BigDecimal harassmentThreatening) {
    this.harassmentThreatening = harassmentThreatening;
  }


  public CreateModerationResponseResultsInnerCategoryScores selfHarm(BigDecimal selfHarm) {
    
    this.selfHarm = selfHarm;
    return this;
  }

   /**
   * The score for the category &#39;self-harm&#39;.
   * @return selfHarm
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SELF_HARM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getSelfHarm() {
    return selfHarm;
  }


  @JsonProperty(JSON_PROPERTY_SELF_HARM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSelfHarm(BigDecimal selfHarm) {
    this.selfHarm = selfHarm;
  }


  public CreateModerationResponseResultsInnerCategoryScores selfHarmIntent(BigDecimal selfHarmIntent) {
    
    this.selfHarmIntent = selfHarmIntent;
    return this;
  }

   /**
   * The score for the category &#39;self-harm/intent&#39;.
   * @return selfHarmIntent
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SELF_HARM_INTENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getSelfHarmIntent() {
    return selfHarmIntent;
  }


  @JsonProperty(JSON_PROPERTY_SELF_HARM_INTENT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSelfHarmIntent(BigDecimal selfHarmIntent) {
    this.selfHarmIntent = selfHarmIntent;
  }


  public CreateModerationResponseResultsInnerCategoryScores selfHarmInstructions(BigDecimal selfHarmInstructions) {
    
    this.selfHarmInstructions = selfHarmInstructions;
    return this;
  }

   /**
   * The score for the category &#39;self-harm/instructions&#39;.
   * @return selfHarmInstructions
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SELF_HARM_INSTRUCTIONS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getSelfHarmInstructions() {
    return selfHarmInstructions;
  }


  @JsonProperty(JSON_PROPERTY_SELF_HARM_INSTRUCTIONS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSelfHarmInstructions(BigDecimal selfHarmInstructions) {
    this.selfHarmInstructions = selfHarmInstructions;
  }


  public CreateModerationResponseResultsInnerCategoryScores sexual(BigDecimal sexual) {
    
    this.sexual = sexual;
    return this;
  }

   /**
   * The score for the category &#39;sexual&#39;.
   * @return sexual
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SEXUAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getSexual() {
    return sexual;
  }


  @JsonProperty(JSON_PROPERTY_SEXUAL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSexual(BigDecimal sexual) {
    this.sexual = sexual;
  }


  public CreateModerationResponseResultsInnerCategoryScores sexualMinors(BigDecimal sexualMinors) {
    
    this.sexualMinors = sexualMinors;
    return this;
  }

   /**
   * The score for the category &#39;sexual/minors&#39;.
   * @return sexualMinors
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SEXUAL_MINORS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getSexualMinors() {
    return sexualMinors;
  }


  @JsonProperty(JSON_PROPERTY_SEXUAL_MINORS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSexualMinors(BigDecimal sexualMinors) {
    this.sexualMinors = sexualMinors;
  }


  public CreateModerationResponseResultsInnerCategoryScores violence(BigDecimal violence) {
    
    this.violence = violence;
    return this;
  }

   /**
   * The score for the category &#39;violence&#39;.
   * @return violence
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VIOLENCE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getViolence() {
    return violence;
  }


  @JsonProperty(JSON_PROPERTY_VIOLENCE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setViolence(BigDecimal violence) {
    this.violence = violence;
  }


  public CreateModerationResponseResultsInnerCategoryScores violenceGraphic(BigDecimal violenceGraphic) {
    
    this.violenceGraphic = violenceGraphic;
    return this;
  }

   /**
   * The score for the category &#39;violence/graphic&#39;.
   * @return violenceGraphic
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VIOLENCE_GRAPHIC)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getViolenceGraphic() {
    return violenceGraphic;
  }


  @JsonProperty(JSON_PROPERTY_VIOLENCE_GRAPHIC)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setViolenceGraphic(BigDecimal violenceGraphic) {
    this.violenceGraphic = violenceGraphic;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateModerationResponseResultsInnerCategoryScores createModerationResponseResultsInnerCategoryScores = (CreateModerationResponseResultsInnerCategoryScores) o;
    return Objects.equals(this.hate, createModerationResponseResultsInnerCategoryScores.hate) &&
        Objects.equals(this.hateThreatening, createModerationResponseResultsInnerCategoryScores.hateThreatening) &&
        Objects.equals(this.harassment, createModerationResponseResultsInnerCategoryScores.harassment) &&
        Objects.equals(this.harassmentThreatening, createModerationResponseResultsInnerCategoryScores.harassmentThreatening) &&
        Objects.equals(this.selfHarm, createModerationResponseResultsInnerCategoryScores.selfHarm) &&
        Objects.equals(this.selfHarmIntent, createModerationResponseResultsInnerCategoryScores.selfHarmIntent) &&
        Objects.equals(this.selfHarmInstructions, createModerationResponseResultsInnerCategoryScores.selfHarmInstructions) &&
        Objects.equals(this.sexual, createModerationResponseResultsInnerCategoryScores.sexual) &&
        Objects.equals(this.sexualMinors, createModerationResponseResultsInnerCategoryScores.sexualMinors) &&
        Objects.equals(this.violence, createModerationResponseResultsInnerCategoryScores.violence) &&
        Objects.equals(this.violenceGraphic, createModerationResponseResultsInnerCategoryScores.violenceGraphic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hate, hateThreatening, harassment, harassmentThreatening, selfHarm, selfHarmIntent, selfHarmInstructions, sexual, sexualMinors, violence, violenceGraphic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateModerationResponseResultsInnerCategoryScores {\n");
    sb.append("    hate: ").append(toIndentedString(hate)).append("\n");
    sb.append("    hateThreatening: ").append(toIndentedString(hateThreatening)).append("\n");
    sb.append("    harassment: ").append(toIndentedString(harassment)).append("\n");
    sb.append("    harassmentThreatening: ").append(toIndentedString(harassmentThreatening)).append("\n");
    sb.append("    selfHarm: ").append(toIndentedString(selfHarm)).append("\n");
    sb.append("    selfHarmIntent: ").append(toIndentedString(selfHarmIntent)).append("\n");
    sb.append("    selfHarmInstructions: ").append(toIndentedString(selfHarmInstructions)).append("\n");
    sb.append("    sexual: ").append(toIndentedString(sexual)).append("\n");
    sb.append("    sexualMinors: ").append(toIndentedString(sexualMinors)).append("\n");
    sb.append("    violence: ").append(toIndentedString(violence)).append("\n");
    sb.append("    violenceGraphic: ").append(toIndentedString(violenceGraphic)).append("\n");
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

    // add `hate` to the URL query string
    if (getHate() != null) {
      try {
        joiner.add(String.format("%shate%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getHate()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `hate/threatening` to the URL query string
    if (getHateThreatening() != null) {
      try {
        joiner.add(String.format("%shate/threatening%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getHateThreatening()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `harassment` to the URL query string
    if (getHarassment() != null) {
      try {
        joiner.add(String.format("%sharassment%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getHarassment()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `harassment/threatening` to the URL query string
    if (getHarassmentThreatening() != null) {
      try {
        joiner.add(String.format("%sharassment/threatening%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getHarassmentThreatening()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `self-harm` to the URL query string
    if (getSelfHarm() != null) {
      try {
        joiner.add(String.format("%sself-harm%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSelfHarm()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `self-harm/intent` to the URL query string
    if (getSelfHarmIntent() != null) {
      try {
        joiner.add(String.format("%sself-harm/intent%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSelfHarmIntent()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `self-harm/instructions` to the URL query string
    if (getSelfHarmInstructions() != null) {
      try {
        joiner.add(String.format("%sself-harm/instructions%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSelfHarmInstructions()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `sexual` to the URL query string
    if (getSexual() != null) {
      try {
        joiner.add(String.format("%ssexual%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSexual()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `sexual/minors` to the URL query string
    if (getSexualMinors() != null) {
      try {
        joiner.add(String.format("%ssexual/minors%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getSexualMinors()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `violence` to the URL query string
    if (getViolence() != null) {
      try {
        joiner.add(String.format("%sviolence%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getViolence()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    // add `violence/graphic` to the URL query string
    if (getViolenceGraphic() != null) {
      try {
        joiner.add(String.format("%sviolence/graphic%s=%s", prefix, suffix, URLEncoder.encode(String.valueOf(getViolenceGraphic()), "UTF-8").replaceAll("\\+", "%20")));
      } catch (UnsupportedEncodingException e) {
        // Should never happen, UTF-8 is always supported
        throw new RuntimeException(e);
      }
    }

    return joiner.toString();
  }

}

