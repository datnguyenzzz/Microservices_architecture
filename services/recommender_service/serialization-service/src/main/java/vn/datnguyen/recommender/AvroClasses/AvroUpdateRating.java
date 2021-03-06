/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package vn.datnguyen.recommender.AvroClasses;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroUpdateRating extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1309938357679763886L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroUpdateRating\",\"namespace\":\"vn.datnguyen.recommender.AvroClasses\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"score\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String clientId;
  @Deprecated public java.lang.String itemId;
  @Deprecated public int score;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroUpdateRating() {}

  /**
   * All-args constructor.
   * @param clientId The new value for clientId
   * @param itemId The new value for itemId
   * @param score The new value for score
   */
  public AvroUpdateRating(java.lang.String clientId, java.lang.String itemId, java.lang.Integer score) {
    this.clientId = clientId;
    this.itemId = itemId;
    this.score = score;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return clientId;
    case 1: return itemId;
    case 2: return score;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: clientId = (java.lang.String)value$; break;
    case 1: itemId = (java.lang.String)value$; break;
    case 2: score = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'clientId' field.
   * @return The value of the 'clientId' field.
   */
  public java.lang.String getClientId() {
    return clientId;
  }

  /**
   * Sets the value of the 'clientId' field.
   * @param value the value to set.
   */
  public void setClientId(java.lang.String value) {
    this.clientId = value;
  }

  /**
   * Gets the value of the 'itemId' field.
   * @return The value of the 'itemId' field.
   */
  public java.lang.String getItemId() {
    return itemId;
  }

  /**
   * Sets the value of the 'itemId' field.
   * @param value the value to set.
   */
  public void setItemId(java.lang.String value) {
    this.itemId = value;
  }

  /**
   * Gets the value of the 'score' field.
   * @return The value of the 'score' field.
   */
  public java.lang.Integer getScore() {
    return score;
  }

  /**
   * Sets the value of the 'score' field.
   * @param value the value to set.
   */
  public void setScore(java.lang.Integer value) {
    this.score = value;
  }

  /**
   * Creates a new AvroUpdateRating RecordBuilder.
   * @return A new AvroUpdateRating RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder newBuilder() {
    return new vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder();
  }

  /**
   * Creates a new AvroUpdateRating RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroUpdateRating RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder(other);
  }

  /**
   * Creates a new AvroUpdateRating RecordBuilder by copying an existing AvroUpdateRating instance.
   * @param other The existing instance to copy.
   * @return A new AvroUpdateRating RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroUpdateRating other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder(other);
  }

  /**
   * RecordBuilder for AvroUpdateRating instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroUpdateRating>
    implements org.apache.avro.data.RecordBuilder<AvroUpdateRating> {

    private java.lang.String clientId;
    private java.lang.String itemId;
    private int score;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.clientId)) {
        this.clientId = data().deepCopy(fields()[0].schema(), other.clientId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.itemId)) {
        this.itemId = data().deepCopy(fields()[1].schema(), other.itemId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.score)) {
        this.score = data().deepCopy(fields()[2].schema(), other.score);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing AvroUpdateRating instance
     * @param other The existing instance to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroUpdateRating other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.clientId)) {
        this.clientId = data().deepCopy(fields()[0].schema(), other.clientId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.itemId)) {
        this.itemId = data().deepCopy(fields()[1].schema(), other.itemId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.score)) {
        this.score = data().deepCopy(fields()[2].schema(), other.score);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'clientId' field.
      * @return The value.
      */
    public java.lang.String getClientId() {
      return clientId;
    }

    /**
      * Sets the value of the 'clientId' field.
      * @param value The value of 'clientId'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder setClientId(java.lang.String value) {
      validate(fields()[0], value);
      this.clientId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'clientId' field has been set.
      * @return True if the 'clientId' field has been set, false otherwise.
      */
    public boolean hasClientId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'clientId' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder clearClientId() {
      clientId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'itemId' field.
      * @return The value.
      */
    public java.lang.String getItemId() {
      return itemId;
    }

    /**
      * Sets the value of the 'itemId' field.
      * @param value The value of 'itemId'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder setItemId(java.lang.String value) {
      validate(fields()[1], value);
      this.itemId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'itemId' field has been set.
      * @return True if the 'itemId' field has been set, false otherwise.
      */
    public boolean hasItemId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'itemId' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder clearItemId() {
      itemId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'score' field.
      * @return The value.
      */
    public java.lang.Integer getScore() {
      return score;
    }

    /**
      * Sets the value of the 'score' field.
      * @param value The value of 'score'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder setScore(int value) {
      validate(fields()[2], value);
      this.score = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'score' field has been set.
      * @return True if the 'score' field has been set, false otherwise.
      */
    public boolean hasScore() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'score' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroUpdateRating.Builder clearScore() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public AvroUpdateRating build() {
      try {
        AvroUpdateRating record = new AvroUpdateRating();
        record.clientId = fieldSetFlags()[0] ? this.clientId : (java.lang.String) defaultValue(fields()[0]);
        record.itemId = fieldSetFlags()[1] ? this.itemId : (java.lang.String) defaultValue(fields()[1]);
        record.score = fieldSetFlags()[2] ? this.score : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
