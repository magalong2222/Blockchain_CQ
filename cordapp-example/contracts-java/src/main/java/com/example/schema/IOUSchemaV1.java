package com.example.schema;

import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.UUID;

/**
 * An IOUState schema.
 */
public class IOUSchemaV1 extends MappedSchema {
    public IOUSchemaV1() {
        super(IOUSchema.class, 1, Arrays.asList(PersistentIOU.class));
    }

    @Entity
    @Table(name = "iou_states")
    public static class PersistentIOU extends PersistentState {
        @Column(name = "seller") private final String seller;
        @Column(name = "buyer") private final String buyer;
        @Column(name = "cashvalue") private final int cashvalue;
        @Column(name = "oilvalue") private final int oilvalue;
        @Column(name = "linear_id") private final UUID linearId;


        public PersistentIOU(String seller, String buyer, int cashvalue, int oilvalue, UUID linearId) {
            this.seller = seller;
            this.buyer = buyer;
            this.cashvalue = cashvalue;
            this.oilvalue = oilvalue;
            this.linearId = linearId;
        }

        // Default constructor required by hibernate.
        public PersistentIOU() {
            this.seller = null;
            this.buyer = null;
            this.cashvalue = 0;
            this.oilvalue = 0;
            this.linearId = null;
        }

        public String getSeller() {
            return seller;
        }

        public String getBuyer() {
            return buyer;
        }

        public int getcashValue() {
            return cashvalue;
        }

        public int getoilValue() {
            return oilvalue;
        }

        public UUID getId() {
            return linearId;
        }
    }
}