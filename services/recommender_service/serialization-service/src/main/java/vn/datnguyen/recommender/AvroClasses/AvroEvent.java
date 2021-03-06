/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package vn.datnguyen.recommender.AvroClasses;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -6463814174522799869L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroEvent\",\"namespace\":\"vn.datnguyen.recommender.AvroClasses\",\"fields\":[{\"name\":\"eventId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"partitionId\",\"type\":\"int\"},{\"name\":\"timestamp\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"eventType\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"data\",\"type\":[{\"type\":\"record\",\"name\":\"AvroPublishRating\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"score\",\"type\":\"int\"}]},{\"type\":\"record\",\"name\":\"AvroUpdateRating\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"score\",\"type\":\"int\"}]},{\"type\":\"record\",\"name\":\"AvroDeleteRating\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"AvroQueryRating\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"AvroBuyBehavior\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"AvroAddToCartBehavior\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"AvroAddItem\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"properties_1\",\"type\":\"int\"},{\"name\":\"properties_2\",\"type\":\"int\"},{\"name\":\"properties_3\",\"type\":\"int\"}]},{\"type\":\"record\",\"name\":\"AvroDeleteItem\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]},{\"type\":\"record\",\"name\":\"AvroRecommendForUser\",\"fields\":[{\"name\":\"clientId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"limit\",\"type\":\"int\"}]},{\"type\":\"record\",\"name\":\"AvroRecommendForItem\",\"fields\":[{\"name\":\"itemId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"limit\",\"type\":\"int\"},{\"name\":\"properties_1\",\"type\":\"int\"},{\"name\":\"properties_2\",\"type\":\"int\"},{\"name\":\"properties_3\",\"type\":\"int\"}]}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String eventId;
  @Deprecated public int partitionId;
  @Deprecated public java.lang.String timestamp;
  @Deprecated public java.lang.String eventType;
  @Deprecated public java.lang.Object data;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroEvent() {}

  /**
   * All-args constructor.
   * @param eventId The new value for eventId
   * @param partitionId The new value for partitionId
   * @param timestamp The new value for timestamp
   * @param eventType The new value for eventType
   * @param data The new value for data
   */
  public AvroEvent(java.lang.String eventId, java.lang.Integer partitionId, java.lang.String timestamp, java.lang.String eventType, java.lang.Object data) {
    this.eventId = eventId;
    this.partitionId = partitionId;
    this.timestamp = timestamp;
    this.eventType = eventType;
    this.data = data;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return eventId;
    case 1: return partitionId;
    case 2: return timestamp;
    case 3: return eventType;
    case 4: return data;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: eventId = (java.lang.String)value$; break;
    case 1: partitionId = (java.lang.Integer)value$; break;
    case 2: timestamp = (java.lang.String)value$; break;
    case 3: eventType = (java.lang.String)value$; break;
    case 4: data = (java.lang.Object)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'eventId' field.
   * @return The value of the 'eventId' field.
   */
  public java.lang.String getEventId() {
    return eventId;
  }

  /**
   * Sets the value of the 'eventId' field.
   * @param value the value to set.
   */
  public void setEventId(java.lang.String value) {
    this.eventId = value;
  }

  /**
   * Gets the value of the 'partitionId' field.
   * @return The value of the 'partitionId' field.
   */
  public java.lang.Integer getPartitionId() {
    return partitionId;
  }

  /**
   * Sets the value of the 'partitionId' field.
   * @param value the value to set.
   */
  public void setPartitionId(java.lang.Integer value) {
    this.partitionId = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public java.lang.String getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.String value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the 'eventType' field.
   * @return The value of the 'eventType' field.
   */
  public java.lang.String getEventType() {
    return eventType;
  }

  /**
   * Sets the value of the 'eventType' field.
   * @param value the value to set.
   */
  public void setEventType(java.lang.String value) {
    this.eventType = value;
  }

  /**
   * Gets the value of the 'data' field.
   * @return The value of the 'data' field.
   */
  public java.lang.Object getData() {
    return data;
  }

  /**
   * Sets the value of the 'data' field.
   * @param value the value to set.
   */
  public void setData(java.lang.Object value) {
    this.data = value;
  }

  /**
   * Creates a new AvroEvent RecordBuilder.
   * @return A new AvroEvent RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder newBuilder() {
    return new vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder();
  }

  /**
   * Creates a new AvroEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroEvent RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder(other);
  }

  /**
   * Creates a new AvroEvent RecordBuilder by copying an existing AvroEvent instance.
   * @param other The existing instance to copy.
   * @return A new AvroEvent RecordBuilder
   */
  public static vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder newBuilder(vn.datnguyen.recommender.AvroClasses.AvroEvent other) {
    return new vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder(other);
  }

  /**
   * RecordBuilder for AvroEvent instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroEvent>
    implements org.apache.avro.data.RecordBuilder<AvroEvent> {

    private java.lang.String eventId;
    private int partitionId;
    private java.lang.String timestamp;
    private java.lang.String eventType;
    private java.lang.Object data;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.eventId)) {
        this.eventId = data().deepCopy(fields()[0].schema(), other.eventId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.partitionId)) {
        this.partitionId = data().deepCopy(fields()[1].schema(), other.partitionId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[2].schema(), other.timestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.eventType)) {
        this.eventType = data().deepCopy(fields()[3].schema(), other.eventType);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.data)) {
        this.data = data().deepCopy(fields()[4].schema(), other.data);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing AvroEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(vn.datnguyen.recommender.AvroClasses.AvroEvent other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.eventId)) {
        this.eventId = data().deepCopy(fields()[0].schema(), other.eventId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.partitionId)) {
        this.partitionId = data().deepCopy(fields()[1].schema(), other.partitionId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[2].schema(), other.timestamp);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.eventType)) {
        this.eventType = data().deepCopy(fields()[3].schema(), other.eventType);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.data)) {
        this.data = data().deepCopy(fields()[4].schema(), other.data);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'eventId' field.
      * @return The value.
      */
    public java.lang.String getEventId() {
      return eventId;
    }

    /**
      * Sets the value of the 'eventId' field.
      * @param value The value of 'eventId'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder setEventId(java.lang.String value) {
      validate(fields()[0], value);
      this.eventId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'eventId' field has been set.
      * @return True if the 'eventId' field has been set, false otherwise.
      */
    public boolean hasEventId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'eventId' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder clearEventId() {
      eventId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'partitionId' field.
      * @return The value.
      */
    public java.lang.Integer getPartitionId() {
      return partitionId;
    }

    /**
      * Sets the value of the 'partitionId' field.
      * @param value The value of 'partitionId'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder setPartitionId(int value) {
      validate(fields()[1], value);
      this.partitionId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'partitionId' field has been set.
      * @return True if the 'partitionId' field has been set, false otherwise.
      */
    public boolean hasPartitionId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'partitionId' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder clearPartitionId() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public java.lang.String getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder setTimestamp(java.lang.String value) {
      validate(fields()[2], value);
      this.timestamp = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'eventType' field.
      * @return The value.
      */
    public java.lang.String getEventType() {
      return eventType;
    }

    /**
      * Sets the value of the 'eventType' field.
      * @param value The value of 'eventType'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder setEventType(java.lang.String value) {
      validate(fields()[3], value);
      this.eventType = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'eventType' field has been set.
      * @return True if the 'eventType' field has been set, false otherwise.
      */
    public boolean hasEventType() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'eventType' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder clearEventType() {
      eventType = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'data' field.
      * @return The value.
      */
    public java.lang.Object getData() {
      return data;
    }

    /**
      * Sets the value of the 'data' field.
      * @param value The value of 'data'.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder setData(java.lang.Object value) {
      validate(fields()[4], value);
      this.data = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'data' field has been set.
      * @return True if the 'data' field has been set, false otherwise.
      */
    public boolean hasData() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'data' field.
      * @return This builder.
      */
    public vn.datnguyen.recommender.AvroClasses.AvroEvent.Builder clearData() {
      data = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public AvroEvent build() {
      try {
        AvroEvent record = new AvroEvent();
        record.eventId = fieldSetFlags()[0] ? this.eventId : (java.lang.String) defaultValue(fields()[0]);
        record.partitionId = fieldSetFlags()[1] ? this.partitionId : (java.lang.Integer) defaultValue(fields()[1]);
        record.timestamp = fieldSetFlags()[2] ? this.timestamp : (java.lang.String) defaultValue(fields()[2]);
        record.eventType = fieldSetFlags()[3] ? this.eventType : (java.lang.String) defaultValue(fields()[3]);
        record.data = fieldSetFlags()[4] ? this.data : (java.lang.Object) defaultValue(fields()[4]);
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
