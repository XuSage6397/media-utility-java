package com.vieup.utility.ontology;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OntologyEntity implements java.io.Serializable {

    //  @Serial
    //  private static long serializer = 1L;

    String uniquid;

    /**
     * the description locale
     */
    String locale;

    /**
     * the schema of current data
     */
    String schema;

    /**
     * the Predicate between initiative and passive.
     * if the data is a node, aspect = 'Node'
     */
    String aspect;

    /**
     * the subject of relation
     */
    String initiative;

    /**
     * the object of relation
     */
    String passive;

    /**
     * the detail content of current item,
     * the struct depends on the schema
     */
    String content;

    /**
     * the annotation from creator and invisible to other member
     */
    String annotation;

    /**
     * the creator of current item
     */
    String creator;

    /**
     * the version number, which is the latest update timestamp.
     */
    long version;
}
