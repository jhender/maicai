/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-04-01 18:14:47 UTC)
 * on 2014-04-03 at 14:20:29 UTC 
 * Modify at your own risk.
 */

package com.google.samplesolutions.mobileassistant.recommendationendpoint.model;

/**
 * Model definition for Recommendation.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the recommendationendpoint. For a detailed explanation
 * see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Recommendation extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime expiration;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String imageUrl;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key key;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String title;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public Recommendation setDescription(java.lang.String description) {
    this.description = description;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getExpiration() {
    return expiration;
  }

  /**
   * @param expiration expiration or {@code null} for none
   */
  public Recommendation setExpiration(com.google.api.client.util.DateTime expiration) {
    this.expiration = expiration;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getImageUrl() {
    return imageUrl;
  }

  /**
   * @param imageUrl imageUrl or {@code null} for none
   */
  public Recommendation setImageUrl(java.lang.String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Key getKey() {
    return key;
  }

  /**
   * @param key key or {@code null} for none
   */
  public Recommendation setKey(Key key) {
    this.key = key;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTitle() {
    return title;
  }

  /**
   * @param title title or {@code null} for none
   */
  public Recommendation setTitle(java.lang.String title) {
    this.title = title;
    return this;
  }

  @Override
  public Recommendation set(String fieldName, Object value) {
    return (Recommendation) super.set(fieldName, value);
  }

  @Override
  public Recommendation clone() {
    return (Recommendation) super.clone();
  }

}
