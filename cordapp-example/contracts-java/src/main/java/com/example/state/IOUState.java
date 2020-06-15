package com.example.state;

import com.example.contract.IOUContract;
import com.example.schema.IOUSchemaV1;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import javax.transaction.UserTransaction;
import java.util.Arrays;
import java.util.List;

/**
 * The state object recording IOU agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 */
@BelongsToContract(IOUContract.class)
public class IOUState implements LinearState, QueryableState {
    private final Integer cash;
    private final Integer oil;
    private final Party seller;
    private final Party buyer;
//    private String chseller = "";
//    private String chbuyer = "";
    private final UniqueIdentifier linearId;

    /**
     * @param value the value of the IOU.
     * @param lender the party issuing the IOU.
     * @param borrower the party receiving and approving the IOU.
     */
    public IOUState(Integer cash,
                    Integer oil,
                    Party seller,
                    Party buyer,
                    UniqueIdentifier linearId)
    {
        this.cash = cash;
        this.oil = oil;
        this.seller = seller;
        this.buyer = buyer;
        this.linearId = linearId;
//        this.chbuyer = "";
//        this.chseller = "";
    }

    public Integer getcashValue() { return cash; }
    public Integer getoilValue() { return oil; }
    public Party getSeller() { return seller; }
    public Party getBuyer() { return buyer; }
//    public void setchSeller(String name) {
//        chseller = name;
//    }
//    public void setchBuyer(String name) {
//        chbuyer = name;
//    }
    @Override public UniqueIdentifier getLinearId() { return linearId; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(seller, buyer);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof IOUSchemaV1) {
            return new IOUSchemaV1.PersistentIOU(
                    this.seller.getName().toString(),
                    this.buyer.getName().toString(),
                    this.cash,
                    this.oil,
                    this.linearId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return Arrays.asList(new IOUSchemaV1());
    }

    @Override
    public String toString() {
        return String.format("IOUState(cashvalue=%s, oilvalue=%s, seller=%s, buyer=%s, linearId=%s)", cash, oil, seller, buyer, linearId);
    }
}