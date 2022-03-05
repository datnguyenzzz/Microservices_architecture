/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package vn.datnguyen.recommender.AvroClasses;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroRecommendForItem extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -5146111590790660348L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroRecommendForItem\",\"namespace\":\"vn.datnguyen.recommender.AvroClasses\",\"fields\":[{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"limit\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String itemId;
  @Deprecated public int limit;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroRecommendForItem() {}

  /**
   * All-args constructor.
   * @param itemId The new value for itemId
   * @param limit The new value for limit
   */
  public AvroRecommendForItem(java.lang.String itemId, java.lang.Integer limit) {
    this.itemId = itemId;
    this.limit = limit;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return itemId;
    case 1: return limit;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: itemId = (java.lang.String)value$; break;
    case 1: limit = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
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
   * Gets the value of the 'limit' field.
   * @return The value of the 'limit' field.
   */
  public java.lang.Integer getLimit() {
    return limit;
  }

  /**
   * Sets the value of the 'limit' field.
   * @param value the value to set.
   */
  public void setLimit(java.lang.Integer value) {
    this.limit = value;
  }

  /**
   * Creates a new AvroRecommendForItem RecordBuilder.
   * @return A new AvroRecommendForItem RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder newBuilder() {
    return new vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder();
  }

  /**
   * Creates a new AvroRecommendForItem RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroRecommendForItem RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder(other);
  }

  /**
   * Creates a new AvroRecommendForItem RecordBuilder by copying an existing AvroRecommendForItem instance.
   * @param other The existing instance to copy.
   * @return A new AvroRecommendForItem RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder(other);
  }

  /**
   * RecordBuilder for AvroRecommendForItem instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroRecommendForItem>
    implements org.apache.avro.data.RecordBuilder<AvroRecommendForItem> {

    private java.lang.String itemId;
    private int limit;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.itemId)) {
        this.itemId = data().deepCopy(fields()[0].schema(), other.itemId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.limit)) {
        this.limit = data().deepCopy(fields()[1].schema(), other.limit);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing AvroRecommendForItem instance
     * @param other The existing instance to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.itemId)) {
        this.itemId = data().deepCopy(fields()[0].schema(), other.itemId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.limit)) {
        this.limit = data().deepCopy(fields()[1].schema(), other.limit);
        fieldSetFlags()[1] = true;
      }
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
    public vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder setItemId(java.lang.String value) {
      validate(fields()[0], value);
      this.itemId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'itemId' field has been set.
      * @return True if the 'itemId' field has been set, false otherwise.
      */
    public boolean hasItemId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'itemId' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder clearItemId() {
      itemId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'limit' field.
      * @return The value.
      */
    public java.lang.Integer getLimit() {
      return limit;
    }

    /**
      * Sets the value of the 'limit' field.
      * @param value The value of 'limit'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder setLimit(int value) {
      validate(fields()[1], value);
      this.limit = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'limit' field has been set.
      * @return True if the 'limit' field has been set, false otherwise.
      */
    public boolean hasLimit() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'limit' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroRecommendForItem.Builder clearLimit() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public AvroRecommendForItem build() {
      try {
        AvroRecommendForItem record = new AvroRecommendForItem();
        record.itemId = fieldSetFlags()[0] ? this.itemId : (java.lang.String) defaultValue(fields()[0]);
        record.limit = fieldSetFlags()[1] ? this.limit : (java.lang.Integer) defaultValue(fields()[1]);
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
